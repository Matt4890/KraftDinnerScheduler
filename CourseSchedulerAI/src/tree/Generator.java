package tree;

import Constraints.*;
import schedule.*;
import coursesULabs.*;
import java.util.*;

public class Generator {
    private Tree tree;
    private Schedule starter;
    private int initialPenalty;
    private int bound;

    public Generator(Schedule starter, int penalty) {
        this.tree = new Tree();
        //this.tree.addRoot(new TreeNode(this.starter, this.initialPenalty));
        this.starter = starter;
        this.initialPenalty = penalty;
        this.bound = Integer.MAX_VALUE;

    }

    private void addPotentialsCourseSlot(TreeNode lastTreeNode, Unit current, HashMap<Integer, Slot> slotsToAddFrom) {
        for (Map.Entry<Integer, Slot> entry : slotsToAddFrom.entrySet()) {
            // Calculate the penalty of the course slot pairing
            int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, 5, 3, 2);
            System.out.println("Penalty of Pairing: " + calc);
            if (HardConstrainsts.checkAssignmentHardConstriantsCourse((Course) current,
                    (CourseSlot) entry.getValue())) {
                // Add the course to the slot in the schedule
                entry.getValue().addOccupant(current);
                // Create a TreeNode as a child of the last TreeNode
                TreeNode n = new TreeNode(lastTreeNode.getSchedule(), calc + lastTreeNode.getPenaltyValueOfTreeNode(), lastTreeNode);
                System.out.println("Schedule in Node: " + n.toString());
                lastTreeNode.addChild(n);

            }
        }

    }

    private void addPotentialsLabSlot(TreeNode lastTreeNode, Unit current, HashMap<Integer, Slot> slotsToAddFrom) {
        for (Map.Entry<Integer, Slot> entry : slotsToAddFrom.entrySet()) {
            // Calculate the penalty of the lab slot pairing
            int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, 0, 0, 0);
            if (HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current, (LabSlot) entry.getValue(),
            lastTreeNode.getSchedule().getMWFLec(), lastTreeNode.getSchedule().getTuThLec(), lastTreeNode.getSchedule().getMWLab(),
            lastTreeNode.getSchedule().getTuThLab(), lastTreeNode.getSchedule().getFLab())) {
                // Add the lab to the slot in the schedule
                entry.getValue().addOccupant(current);
                // Create a TreeNode as a child of the last TreeNode
                TreeNode n = new TreeNode(lastTreeNode.getSchedule(), calc + lastTreeNode.getPenaltyValueOfTreeNode(), lastTreeNode);
                lastTreeNode.addChild(n);
                for (int i = 0; i < lastTreeNode.getChildren().size(); i++) {
                    System.out.print(lastTreeNode.getChildren().get(i).toString());
                }
            }
        }
    }

    public void generateFBound(ArrayList<Unit> toBeAdded) {
        TreeNode lastTreeNode = new TreeNode(this.starter, this.initialPenalty);
        for (int i = 0; i < toBeAdded.size(); i++) {
            Unit current = toBeAdded.get(i);
            // Run through each possible course slot pair and create children
            if (current instanceof Course) {
                this.addPotentialsCourseSlot(lastTreeNode, current, lastTreeNode.getSchedule().getMWFLec());
                this.addPotentialsCourseSlot(lastTreeNode, current, lastTreeNode.getSchedule().getTuThLec());
            } else {
                this.addPotentialsLabSlot(lastTreeNode, current, lastTreeNode.getSchedule().getMWLab());
                this.addPotentialsLabSlot(lastTreeNode, current, lastTreeNode.getSchedule().getTuThLab());
                this.addPotentialsLabSlot(lastTreeNode, current, lastTreeNode.getSchedule().getFLab());

            }

            // At this point we should have all the potential Pairs stored in lastTreeNode's
            // children now we need to select one using the control
            // Pick the best TreeNode - i.e. top of the heap and make it the lastTreeNode to expand
            // out
            try{
            lastTreeNode = lastTreeNode.getChildren().get(0);
            for (int k = 0; k< lastTreeNode.getChildren().size(); k++){
                System.out.println(lastTreeNode.getChildren().get(k).getSchedule().toString());
            }
            //System.out.println(lastTreeNode.getSchedule().toString());
            //lastTreeNode = lastTreeNode.getLowestPenaltyChild();
            }
            catch (Exception e){
                
                System.out.println("No more children created, current FBound = " + lastTreeNode.getPenaltyValueOfTreeNode());
                break;
            }
            //lastTreeNode = lastTreeNode.getChildren().get(0);
        }

        // Now we get the penaltyValue of lastTreeNode and make that the bound
        this.bound = lastTreeNode.getPenaltyValueOfTreeNode();

        System.out.println("Bound Generated: " + this.bound);
        // We can make this better once the KONTROL is implemented We can modify this so
        // that only the TreeNodes that
        // are added are the ones that meet the desirablility criteria
        // This control would have to be seperate for the branch and bound control
        // otherwise we are likely to pick the same thing

    }

    public void biggerGenerateFBound(ArrayList<Unit> toBeAdded) {
        TreeNode lastTreeNode = new TreeNode(this.starter, this.initialPenalty);
        for (int i = 0; i < toBeAdded.size(); i++) {
            Unit current = toBeAdded.get(i);
            int penalty = lastTreeNode.getPenaltyValueOfTreeNode();
            // Run through each possible course slot pair and create children
            if (current instanceof Course) {
                for (Map.Entry<Integer, Slot> entry : this.starter.getMWFLec().entrySet()) {
                    // Calculate the penalty of the course slot pairing
                    int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, 0, 0, 0);
                    if (HardConstrainsts.checkAssignmentHardConstriantsCourse((Course) current,
                            (CourseSlot) entry.getValue())) {
                        // Add the course to the slot in the schedule
                        entry.getValue().addOccupant(current);
                        // Create a TreeNode as a child of the last TreeNode
                        TreeNode n = new TreeNode(this.starter, calc + penalty, lastTreeNode);
                        lastTreeNode.addChild(n);
                    }
                }
                for (Map.Entry<Integer, Slot> entry : this.starter.getTuThLec().entrySet()) {
                    // Calculate the penalty of the course slot pairing
                    int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, 0, 0, 0);
                    if (HardConstrainsts.checkAssignmentHardConstriantsCourse((Course) current,
                            (CourseSlot) entry.getValue())) {
                        // Add the course to the slot in the schedule
                        entry.getValue().addOccupant(current);
                        // Create a TreeNode as a child of the last TreeNode
                        TreeNode n = new TreeNode(this.starter, calc + penalty, lastTreeNode);
                        lastTreeNode.addChild(n);
                    }
                }
            } else {
                for (Map.Entry<Integer, Slot> entry : this.starter.getMWLab().entrySet()) {
                    // Calculate the penalty of the lab slot pairing
                    int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, 0, 0, 0);
                    if (HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current, (LabSlot) entry.getValue(),
                            this.starter.getMWFLec(), this.starter.getTuThLec(), this.starter.getMWLab(),
                            this.starter.getTuThLab(), this.starter.getFLab())) {
                        // Add the lab to the slot in the schedule
                        entry.getValue().addOccupant(current);
                        // Create a TreeNode as a child of the last TreeNode
                        TreeNode n = new TreeNode(this.starter, calc + penalty, lastTreeNode);
                        lastTreeNode.addChild(n);
                    }
                }
                for (Map.Entry<Integer, Slot> entry : this.starter.getTuThLab().entrySet()) {
                    // Calculate the penalty of the lab slot pairing
                    int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, 0, 0, 0);
                    if (HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current, (LabSlot) entry.getValue(),
                            this.starter.getMWFLec(), this.starter.getTuThLec(), this.starter.getMWLab(),
                            this.starter.getTuThLab(), this.starter.getFLab())) {
                        // Add the lab to the slot in the schedule
                        entry.getValue().addOccupant(current);
                        // Create a TreeNode as a child of the last TreeNode
                        TreeNode n = new TreeNode(this.starter, calc + penalty, lastTreeNode);
                        lastTreeNode.addChild(n);

                    }
                }
                for (Map.Entry<Integer, Slot> entry : this.starter.getFLab().entrySet()) {
                    // Calculate the penalty of the lab slot pairing
                    int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, 0, 0, 0);
                    if (HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current, (LabSlot) entry.getValue(),
                            this.starter.getMWFLec(), this.starter.getTuThLec(), this.starter.getMWLab(),
                            this.starter.getTuThLab(), this.starter.getFLab())) {
                        // Add the lab to the slot in the schedule
                        entry.getValue().addOccupant(current);
                        // Create a TreeNode as a child of the last TreeNode
                        TreeNode n = new TreeNode(this.starter, calc + penalty, lastTreeNode);
                        lastTreeNode.addChild(n);

                    }
                }
            }
            lastTreeNode = lastTreeNode.getChildren().get(0);
            //lastTreeNode = lastTreeNode.getLowestPenaltyChild();

        }
        this.bound = lastTreeNode.getPenaltyValueOfTreeNode();

        System.out.println("Bound Generated: " + this.bound);

    }

    // public void createFBound(ArrayList<Unit> toBeAdded) {

    // this.tree.addRoot(new TreeNode(this.starter, this.initialPenalty));
    // TreeNode lastTreeNode = this.tree.getRoot();

    // for (int i = 0; i < toBeAdded.size(); i++) {
    // // Check what type of unit it is and then create the TreeNodes accordingly
    // Unit current = toBeAdded.get(i);
    // int minimum = Integer.MAX_VALUE;
    // if (current instanceof Course) {

    // int idx = Integer.MAX_VALUE;
    // int day = -1;
    // for (Map.Entry<Integer, Slot> entry : this.starter.getMWFLec().entrySet()) {
    // // Calculate the penalty of the course slot pairing
    // int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, 0, 0,
    // 0);
    // if (HardConstrainsts.checkAssignmentHardConstriantsCourse((Course) current,
    // (CourseSlot) entry.getValue(), this.starter.getMWFLec(),
    // this.starter.getTuThLec(),
    // this.starter.getMWLab(), this.starter.getTuThLab(), this.starter.getFLab()))
    // {
    // // Run a hard constraint check

    // calc = minimum;
    // idx = entry.getKey();
    // day = 0;
    // }
    // }
    // for (Map.Entry<Integer, Slot> entry : this.starter.getTuThLec().entrySet()) {
    // int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, 0, 0,
    // 0);
    // // Check if we pass the hard constraints and its a new minimal
    // if (calc < minimum &&
    // HardConstrainsts.checkAssignmentHardConstriantsCourse((Course) current,
    // (CourseSlot) entry.getValue(), this.starter.getMWFLec(),
    // this.starter.getTuThLec(),
    // this.starter.getMWLab(), this.starter.getTuThLab(), this.starter.getFLab()))
    // {
    // calc = minimum;
    // idx = entry.getKey();
    // day = 1;
    // }
    // }
    // // Assign the course to the slot
    // if (day == 0) { // MondayWednesdayFriday
    // this.starter.getMWFLec().get(idx).assignUnitToSlot(current);
    // } else { // TuesdayThursday
    // this.starter.getTuThLec().get(idx).assignUnitToSlot(current);
    // }
    // // Not Sure if this should be a deep copy or a shallow reference is okay.
    // TreeNode newCreatedTreeNode = new TreeNode(this.starter, minimum +
    // lastTreeNode.getPenaltyValueOfTreeNode(), lastTreeNode);
    // lastTreeNode.addChild(newCreatedTreeNode);
    // lastTreeNode = newCreatedTreeNode;

    // } else {
    // int idx = Integer.MAX_VALUE;
    // int day = -1;
    // // Check all the lab slot assignments that would be best
    // for (Map.Entry<Integer, Slot> entry : this.starter.getMWLab().entrySet()) {
    // int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, 0, 0,
    // 0);
    // if (calc < minimum &&
    // HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current,
    // (LabSlot) entry.getValue(), this.starter.getMWFLec(),
    // this.starter.getTuThLec(),
    // this.starter.getMWLab(), this.starter.getTuThLab(), this.starter.getFLab()))
    // {

    // calc = minimum;
    // idx = entry.getKey();
    // day = 0;
    // }

    // }
    // for (Map.Entry<Integer, Slot> entry : this.starter.getTuThLab().entrySet()) {
    // int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, 0, 0,
    // 0);
    // if (calc < minimum &&
    // HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current,
    // (LabSlot) entry.getValue(), this.starter.getMWFLec(),
    // this.starter.getTuThLec(),
    // this.starter.getMWLab(), this.starter.getTuThLab(), this.starter.getFLab()))
    // {

    // calc = minimum;
    // idx = entry.getKey();
    // day = 1;
    // }

    // }
    // for (Map.Entry<Integer, Slot> entry : this.starter.getFLab().entrySet()) {
    // int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, 0, 0,
    // 0);
    // if (calc < minimum &&
    // HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current,
    // (LabSlot) entry.getValue(), this.starter.getMWFLec(),
    // this.starter.getTuThLec(),
    // this.starter.getMWLab(), this.starter.getTuThLab(), this.starter.getFLab()))
    // {

    // calc = minimum;
    // idx = entry.getKey();
    // day = 1;
    // }
    // }
    // if (day == 0) { // MondayWednesday
    // this.starter.getMWLab().get(idx).assignUnitToSlot(current);
    // } else if (day == 1) { // TuesdayThursday
    // this.starter.getTuThLab().get(idx).assignUnitToSlot(current);
    // } else { // Friday
    // this.starter.getFLab().get(idx).assignUnitToSlot(current);
    // }
    // TreeNode newCreatedTreeNode = new TreeNode(this.starter, minimum +
    // lastTreeNode.getPenaltyValueOfTreeNode(), lastTreeNode);
    // lastTreeNode.addChild(newCreatedTreeNode);
    // lastTreeNode = newCreatedTreeNode;

    // }
    // this.bound += minimum;
    // System.out.println(bound);
    // }
    // }
    //
    // public TreeNode branchAndBound() {
    // // TODO: Implement branch and Bound using a control to evaluate

    // // Basic methodology same as the dfs except we store all of them as children
    // and
    // // make a choice based on the KONTROL
    // // the Bound basically acts as another hard constraint?
    // // or no that would just be brute force
    // // we have to find the

    // return this.tree.getRoot();

    // }
}