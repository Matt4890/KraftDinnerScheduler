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
            boolean HardConstraintOk = HardConstrainsts.checkAssignmentHardConstriantsCourse((Course) current,
            (CourseSlot) entry.getValue());
            System.out.println("Assignment " + ((Course)current).toString() + "and Slot " + entry.getValue().toString()+ "hard constraint check is: "+ HardConstraintOk);
            if (HardConstraintOk) {
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
            System.out.println("Penalty of Pairing: " + calc);
            
            boolean HardConstraintOk = HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current, (LabSlot) entry.getValue(),
            lastTreeNode.getSchedule().getMWFLec(), lastTreeNode.getSchedule().getTuThLec(), lastTreeNode.getSchedule().getMWLab(),
            lastTreeNode.getSchedule().getTuThLab(), lastTreeNode.getSchedule().getFLab());
            System.out.println("Assignment " + ((Lab)current).toString() + "and Slot " + entry.getValue().toString()+ "hard constraint check is: "+ HardConstraintOk);

            if (HardConstraintOk) {
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
       // for (int i = 0; i < toBeAdded.size(); i++) {
        
        for (int i = toBeAdded.size() -1; i >=0; i--) {
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

}