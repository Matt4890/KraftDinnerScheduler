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
        // this.tree.addRoot(new TreeNode(this.starter, this.initialPenalty));
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
            System.out.println("Assignment " + ((Course) current).toString() + " and Slot "
                    + entry.getValue().toString() + " hard constraint check is: " + HardConstraintOk);
            if (HardConstraintOk) {
                // Add the course to the slot in the schedule
                entry.getValue().addOccupant(current); // PROBLEM
                // Create a TreeNode as a child of the last TreeNode
                TreeNode n = new TreeNode(new Schedule(lastTreeNode.getSchedule()),
                        calc + lastTreeNode.getPenaltyValueOfTreeNode(), lastTreeNode);
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

            boolean HardConstraintOk = HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current,
                    (LabSlot) entry.getValue(), lastTreeNode.getSchedule().getMWFLec(),
                    lastTreeNode.getSchedule().getTuThLec(), lastTreeNode.getSchedule().getMWLab(),
                    lastTreeNode.getSchedule().getTuThLab(), lastTreeNode.getSchedule().getFLab());
            System.out.println("Assignment " + ((Lab) current).toString() + " and Slot " + entry.getValue().toString()
                    + " hard constraint check is: " + HardConstraintOk);

            if (HardConstraintOk) {
                // Add the lab to the slot in the schedule
                entry.getValue().addOccupant(current);
                // Create a TreeNode as a child of the last TreeNode
                TreeNode n = new TreeNode(new Schedule(lastTreeNode.getSchedule()),
                        calc + lastTreeNode.getPenaltyValueOfTreeNode(), lastTreeNode);
                System.out.println("Schedule in Node: " + n.toString());
                lastTreeNode.addChild(n);

            }
        }

    }

    public void generateFBound(ArrayList<Unit> toBeAdded) {
        TreeNode lastTreeNode = new TreeNode(this.starter, this.initialPenalty);
        for (int i = 0; i < toBeAdded.size(); i++) {

            // for (int i = toBeAdded.size() -1; i >=0; i--) {
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
            // Pick the best TreeNode - i.e. top of the heap and make it the lastTreeNode to
            // expand
            // out
            try {
                // lastTreeNode = lastTreeNode.getChildren().get(0);
                // for (int k = 0; k< lastTreeNode.getChildren().size(); k++){
                // System.out.println(lastTreeNode.getChildren().get(k).getSchedule().toString());
                // }
                // System.out.println(lastTreeNode.getSchedule().toString());
                lastTreeNode = lastTreeNode.getLowestPenaltyChild();
            } catch (Exception e) {

                System.out.println(
                        "No more children created, current FBound = " + lastTreeNode.getPenaltyValueOfTreeNode());
                break;
            }
            // lastTreeNode = lastTreeNode.getChildren().get(0);
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

    public void generateFBoundBIG(ArrayList<Unit> toBeAdded) {
        TreeNode lastTreeNode = new TreeNode(this.starter, this.initialPenalty);
        for (int i = 0; i < toBeAdded.size(); i++) {

            // for (int i = toBeAdded.size() -1; i >=0; i--) {
            Unit current = toBeAdded.get(i);
            // Run through each possible course slot pair and create children
            if (current instanceof Course) {

                // Copy the hashMap to Reference
                for (Map.Entry<Integer, Slot> entry : lastTreeNode.getSchedule().getMWFLec().entrySet()) {
                    // Create a copy to manipulate
                    HashMap<Integer, Slot> MWFMapToManipulate = DeepCopyCourseSlotMap(
                            lastTreeNode.getSchedule().getMWFLec());
                    // Calculate the penalty of the course slot pairing
                    int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, 5, 3, 2);
                    System.out.println("Penalty of Pairing: " + calc);
                    boolean HardConstraintOk = HardConstrainsts.checkAssignmentHardConstriantsCourse((Course) current,
                            (CourseSlot) entry.getValue());
                    System.out.println("Assignment " + ((Course) current).toString() + " and Slot "
                            + entry.getValue().toString() + " hard constraint check is: " + HardConstraintOk);
                    if (HardConstraintOk) {

                        // GET THE KEY OF THE ENTRY WE WANT TO ADD TO THEN GET THE VALUE FROM THE
                        // MANIPULATED TABLE
                        // AND ACCESS THE SLOT AT THAT POINT AND ADD THE OCCUPANT
                        // Add the course to the slot in the schedule
                        MWFMapToManipulate.get(entry.getKey()).addOccupant(current);

                        // Create a new Schedule to add
                        Schedule newSchedule = new Schedule(lastTreeNode.getSchedule());

                        // Replace the MWF in the newSchedule with the manipulated one
                        newSchedule.setMWFLec(MWFMapToManipulate);

                        // Create a TreeNode as a child of the last TreeNode
                        TreeNode n = new TreeNode(newSchedule, calc + lastTreeNode.getPenaltyValueOfTreeNode(),
                                lastTreeNode); // Note we need to calculate the total penatly differently
                        System.out.println("Schedule in Node: " + n.toString());
                        lastTreeNode.addChild(n);

                    }
                }

                for (Map.Entry<Integer, Slot> entry : lastTreeNode.getSchedule().getTuThLec().entrySet()) {
                    // Create a copy to manipulate
                    HashMap<Integer, Slot> TuThMapToManipulate = DeepCopyCourseSlotMap(
                            lastTreeNode.getSchedule().getTuThLec());
                    // Calculate the penalty of the course slot pairing
                    int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, 5, 3, 2);
                    System.out.println("Penalty of Pairing: " + calc);
                    boolean HardConstraintOk = HardConstrainsts.checkAssignmentHardConstriantsCourse((Course) current,
                            (CourseSlot) entry.getValue());
                    System.out.println("Assignment " + ((Course) current).toString() + " and Slot "
                            + entry.getValue().toString() + " hard constraint check is: " + HardConstraintOk);
                    if (HardConstraintOk) {

                        // GET THE KEY OF THE ENTRY WE WANT TO ADD TO THEN GET THE VALUE FROM THE
                        // MANIPULATED TABLE
                        // AND ACCESS THE SLOT AT THAT POINT AND ADD THE OCCUPANT
                        // Add the course to the slot in the schedule
                        TuThMapToManipulate.get(entry.getKey()).addOccupant(current);

                        // Create a new Schedule to add
                        Schedule newSchedule = new Schedule(lastTreeNode.getSchedule());

                        // Replace the TuTh in the newSchedule with the manipulated one
                        newSchedule.setTuThLec(TuThMapToManipulate);

                        // Create a TreeNode as a child of the last TreeNode
                        TreeNode n = new TreeNode(newSchedule, calc + lastTreeNode.getPenaltyValueOfTreeNode(),
                                lastTreeNode); // Note we need to calculate the total penatly differently
                        System.out.println("Schedule in Node: " + n.toString());
                        lastTreeNode.addChild(n);

                    }
                }

            } else {
                for (Map.Entry<Integer, Slot> entry : lastTreeNode.getSchedule().getMWLab().entrySet()) {
                    // Create a copy to manipulate
                    HashMap<Integer, Slot> MWLabMapToManipulate = DeepCopyLabSlotMap(
                            lastTreeNode.getSchedule().getMWLab());
                    // Calculate the penalty of the course slot pairing
                    int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, 5, 3, 2);
                    System.out.println("Penalty of Pairing: " + calc);
                    boolean HardConstraintOk = HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current,
                            (LabSlot) entry.getValue(), lastTreeNode.getSchedule().getMWFLec(),
                            lastTreeNode.getSchedule().getTuThLec(), lastTreeNode.getSchedule().getMWLab(),
                            lastTreeNode.getSchedule().getTuThLab(), lastTreeNode.getSchedule().getFLab());
                    System.out.println("Assignment " + ((Lab) current).toString() + " and Slot "
                            + entry.getValue().toString() + " hard constraint check is: " + HardConstraintOk);
                    if (HardConstraintOk) {

                        // GET THE KEY OF THE ENTRY WE WANT TO ADD TO THEN GET THE VALUE FROM THE
                        // MANIPULATED TABLE
                        // AND ACCESS THE SLOT AT THAT POINT AND ADD THE OCCUPANT
                        // Add the course to the slot in the schedule
                        MWLabMapToManipulate.get(entry.getKey()).addOccupant(current);

                        // Create a new Schedule to add
                        Schedule newSchedule = new Schedule(lastTreeNode.getSchedule());

                        // Replace the TuTh in the newSchedule with the manipulated one
                        newSchedule.setMWLab(MWLabMapToManipulate);

                        // Create a TreeNode as a child of the last TreeNode
                        TreeNode n = new TreeNode(newSchedule, calc + lastTreeNode.getPenaltyValueOfTreeNode(),
                                lastTreeNode); // Note we need to calculate the total penatly differently
                        System.out.println("Schedule in Node: " + n.toString());
                        lastTreeNode.addChild(n);

                    }
                }
                for (Map.Entry<Integer, Slot> entry : lastTreeNode.getSchedule().getTuThLab().entrySet()) {
                    // Create a copy to manipulate
                    HashMap<Integer, Slot> TuThLabMapToManipulate = DeepCopyLabSlotMap(
                            lastTreeNode.getSchedule().getTuThLab());
                    // Calculate the penalty of the course slot pairing
                    int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, 5, 3, 2);
                    System.out.println("Penalty of Pairing: " + calc);
                    boolean HardConstraintOk = HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current,
                            (LabSlot) entry.getValue(), lastTreeNode.getSchedule().getMWFLec(),
                            lastTreeNode.getSchedule().getTuThLec(), lastTreeNode.getSchedule().getMWLab(),
                            lastTreeNode.getSchedule().getTuThLab(), lastTreeNode.getSchedule().getFLab());

                    System.out.println("Assignment " + ((Lab) current).toString() + " and Slot "
                            + entry.getValue().toString() + " hard constraint check is: " + HardConstraintOk);
                    if (HardConstraintOk) {

                        // GET THE KEY OF THE ENTRY WE WANT TO ADD TO THEN GET THE VALUE FROM THE
                        // MANIPULATED TABLE
                        // AND ACCESS THE SLOT AT THAT POINT AND ADD THE OCCUPANT
                        // Add the course to the slot in the schedule
                        TuThLabMapToManipulate.get(entry.getKey()).addOccupant(current);

                        // Create a new Schedule to add
                        Schedule newSchedule = new Schedule(lastTreeNode.getSchedule());

                        // Replace the TuTh in the newSchedule with the manipulated one
                        newSchedule.setTuThLab(TuThLabMapToManipulate);

                        // Create a TreeNode as a child of the last TreeNode
                        TreeNode n = new TreeNode(newSchedule, calc + lastTreeNode.getPenaltyValueOfTreeNode(),
                                lastTreeNode); // Note we need to calculate the total penatly differently
                        System.out.println("Schedule in Node: " + n.toString());
                        lastTreeNode.addChild(n);

                    }
                }
                for (Map.Entry<Integer, Slot> entry : lastTreeNode.getSchedule().getFLab().entrySet()) {
                    // Create a copy to manipulate
                    HashMap<Integer, Slot> FLabMapToManipulate = DeepCopyLabSlotMap(
                            lastTreeNode.getSchedule().getFLab());
                    // Calculate the penalty of the course slot pairing
                    int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, 5, 3, 2);
                    System.out.println("Penalty of Pairing: " + calc);
                    boolean HardConstraintOk = HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current,
                            (LabSlot) entry.getValue(), lastTreeNode.getSchedule().getMWFLec(),
                            lastTreeNode.getSchedule().getTuThLec(), lastTreeNode.getSchedule().getMWLab(),
                            lastTreeNode.getSchedule().getTuThLab(), lastTreeNode.getSchedule().getFLab());
                    System.out.println("Assignment " + ((Lab) current).toString() + " and Slot "
                            + entry.getValue().toString() + " hard constraint check is: " + HardConstraintOk);
                    if (HardConstraintOk) {

                        // GET THE KEY OF THE ENTRY WE WANT TO ADD TO THEN GET THE VALUE FROM THE
                        // MANIPULATED TABLE
                        // AND ACCESS THE SLOT AT THAT POINT AND ADD THE OCCUPANT
                        // Add the course to the slot in the schedule
                        FLabMapToManipulate.get(entry.getKey()).addOccupant(current);

                        // Create a new Schedule to add
                        Schedule newSchedule = new Schedule(lastTreeNode.getSchedule());

                        // Replace the TuTh in the newSchedule with the manipulated one
                        newSchedule.setFLab(FLabMapToManipulate);

                        // Create a TreeNode as a child of the last TreeNode
                        TreeNode n = new TreeNode(newSchedule, calc + lastTreeNode.getPenaltyValueOfTreeNode(),
                                lastTreeNode); // Note we need to calculate the total penatly differently
                        System.out.println("Schedule in Node: " + n.toString());
                        lastTreeNode.addChild(n);

                    }
                }
            }

            // At this point we should have all the potential Pairs stored in lastTreeNode's
            // children now we need to select one using the control
            // Pick the best TreeNode - i.e. top of the heap and make it the lastTreeNode to
            // expand
            // out
            try {
                // lastTreeNode = lastTreeNode.getChildren().get(0);
                // for (int k = 0; k< lastTreeNode.getChildren().size(); k++){
                // System.out.println(lastTreeNode.getChildren().get(k).getSchedule().toString());
                // }
                // System.out.println(lastTreeNode.getSchedule().toString());
                lastTreeNode = lastTreeNode.getLowestPenaltyChild();
            } catch (Exception e) {

                System.out.println(
                        "No more children created, current FBound = " + lastTreeNode.getPenaltyValueOfTreeNode());
                break;
            }
            // lastTreeNode = lastTreeNode.getChildren().get(0);
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

    private HashMap<Integer, Slot> DeepCopyCourseSlotMap(HashMap<Integer, Slot> toCopy) {
        HashMap<Integer, Slot> toReturn = new HashMap<Integer, Slot>();
        for (Map.Entry<Integer, Slot> entry : toCopy.entrySet()) {
            toReturn.put(entry.getKey(), new CourseSlot((CourseSlot) entry.getValue()));
        }
        return toReturn;

    }

    private HashMap<Integer, Slot> DeepCopyLabSlotMap(HashMap<Integer, Slot> toCopy) {
        HashMap<Integer, Slot> toReturn = new HashMap<Integer, Slot>();
        for (Map.Entry<Integer, Slot> entry : toCopy.entrySet()) {
            toReturn.put(entry.getKey(), new LabSlot((LabSlot) entry.getValue()));
        }
        return toReturn;

    }

}