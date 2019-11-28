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
    private int courseMinPen;
    private int pairsPen;
    private int brothersPen;

    public Generator(Schedule starter, int penalty, int i, int j, int k) {
        this.tree = new Tree();
        // this.tree.addRoot(new TreeNode(this.starter, this.initialPenalty));
        this.starter = starter;
        this.initialPenalty = penalty;
        this.bound = Integer.MAX_VALUE;
        this.courseMinPen = i;
        this.pairsPen = j;
        this.brothersPen = k;

    }

    private void addPotentialsCourseSlot(TreeNode lastTreeNode, Unit current, HashMap<Integer, Slot> slotsToAddFrom) {
        for (Map.Entry<Integer, Slot> entry : slotsToAddFrom.entrySet()) {
            // Calculate the penalty of the course slot pairing
            int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, courseMinPen, pairsPen, brothersPen);
            System.out.println("Penalty of Pairing: " + calc);
            boolean HardConstraintOk = HardConstrainsts.checkAssignmentHardConstriantsCourse((Course) current,
                    (CourseSlot) entry.getValue());
            System.out.println("Assignment " + ((Course) current).toString() + " and Slot "
                    + entry.getValue().toString() + " hard constraint check is: " + HardConstraintOk);
            if (HardConstraintOk) {
                // Add the course to the slot in the schedule
                entry.getValue().addOccupant(current); // PROBLEM
                int desire = Kontrol.desireability(entry.getValue(), current);
                // Create a TreeNode as a child of the last TreeNode
                TreeNode n = new TreeNode(new Schedule(lastTreeNode.getSchedule()),
                        calc + lastTreeNode.getPenaltyValueOfTreeNode(), lastTreeNode, desire);
                System.out.println("Schedule in Node: " + n.toString());
                lastTreeNode.addChild(n);

            }
        }

    }

    private void addPotentialsLabSlot(TreeNode lastTreeNode, Unit current, HashMap<Integer, Slot> slotsToAddFrom) {
        for (Map.Entry<Integer, Slot> entry : slotsToAddFrom.entrySet()) {
            // Calculate the penalty of the lab slot pairing
            int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, courseMinPen, pairsPen, brothersPen);
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
                int desire = Kontrol.desireability(entry.getValue(), current);
                // Create a TreeNode as a child of the last TreeNode
                TreeNode n = new TreeNode(new Schedule(lastTreeNode.getSchedule()),
                        calc + lastTreeNode.getPenaltyValueOfTreeNode(), lastTreeNode, desire);
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
            if (!lastTreeNode.getOrderedChildren().isEmpty()) {
                lastTreeNode = lastTreeNode.getLowestPenaltyChild();
                System.out.println("\n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n");

            } else {

                // for (int i = toBeAdded.size() -1; i >=0; i--) {
                Unit current = toBeAdded.get(i);
                // Run through each possible course slot pair and create children
                if (current instanceof Course) {

                    // Copy the hashMap to Reference
                    for (Map.Entry<Integer, Slot> entry : lastTreeNode.getSchedule().getMWFLec().entrySet()) {
                        // Create a copy to manipulate
                        HashMap<Integer, Slot> MWFMapToManipulate = DeepCopyCourseSlotMap(
                                lastTreeNode.getSchedule().getMWFLec());

                        // int calc = 0;

                        boolean HardConstraintOk = HardConstrainsts
                                .checkAssignmentHardConstriantsCourse((Course) current, (CourseSlot) entry.getValue());
                        System.out.println("Assignment " + ((Course) current).toString() + " and Slot "
                                + entry.getValue().toString() + " hard constraint check is: " + HardConstraintOk);
                        if (HardConstraintOk) {

                            // Calculate the penalty of the course slot pairing
                            int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, courseMinPen, pairsPen,
                            brothersPen);
                            System.out.println("Penalty of Pairing: " + calc);
                            // GET THE KEY OF THE ENTRY WE WANT TO ADD TO THEN GET THE VALUE FROM THE
                            // MANIPULATED TABLE
                            // AND ACCESS THE SLOT AT THAT POINT AND ADD THE OCCUPANT
                            // Add the course to the slot in the schedule
                            MWFMapToManipulate.get(entry.getKey()).addOccupant(current);

                            // Create a new Schedule to add
                            Schedule newSchedule = new Schedule(lastTreeNode.getSchedule());

                            // Replace the MWF in the newSchedule with the manipulated one
                            newSchedule.setMWFLec(MWFMapToManipulate);
                            int desire = Kontrol.desireability(entry.getValue(), current);

                            // Create a TreeNode as a child of the last TreeNode
                            TreeNode n = new TreeNode(newSchedule, calc + lastTreeNode.getPenaltyValueOfTreeNode(),
                                    lastTreeNode, desire); // Note we need to calculate the total penatly differently
                            System.out.println("Schedule in Node: " + n.toString());
                            lastTreeNode.addChild(n);

                        }
                    }

                    for (Map.Entry<Integer, Slot> entry : lastTreeNode.getSchedule().getTuThLec().entrySet()) {
                        // Create a copy to manipulate
                        HashMap<Integer, Slot> TuThMapToManipulate = DeepCopyCourseSlotMap(
                                lastTreeNode.getSchedule().getTuThLec());
                        // int calc = 0;

                        boolean HardConstraintOk = HardConstrainsts
                                .checkAssignmentHardConstriantsCourse((Course) current, (CourseSlot) entry.getValue());
                        System.out.println("Assignment " + ((Course) current).toString() + " and Slot "
                                + entry.getValue().toString() + " hard constraint check is: " + HardConstraintOk);
                        if (HardConstraintOk) {

                            // Calculate the penalty of the course slot pairing
                            int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, courseMinPen, pairsPen,
                            brothersPen);         
                            System.out.println("Penalty of Pairing: " + calc);                   
                            // GET THE KEY OF THE ENTRY WE WANT TO ADD TO THEN GET THE VALUE FROM THE
                            // MANIPULATED TABLE
                            // AND ACCESS THE SLOT AT THAT POINT AND ADD THE OCCUPANT
                            // Add the course to the slot in the schedule
                            TuThMapToManipulate.get(entry.getKey()).addOccupant(current);

                            // Create a new Schedule to add
                            Schedule newSchedule = new Schedule(lastTreeNode.getSchedule());

                            // Replace the TuTh in the newSchedule with the manipulated one
                            newSchedule.setTuThLec(TuThMapToManipulate);
                            int desire = Kontrol.desireability(entry.getValue(), current);

                            // Create a TreeNode as a child of the last TreeNode
                            TreeNode n = new TreeNode(newSchedule, calc + lastTreeNode.getPenaltyValueOfTreeNode(),
                                    lastTreeNode, desire); // Note we need to calculate the total penatly differently
                            System.out.println("Schedule in Node: " + n.toString());
                            lastTreeNode.addChild(n);

                        }
                    }

                } else {
                    for (Map.Entry<Integer, Slot> entry : lastTreeNode.getSchedule().getMWLab().entrySet()) {
                        // Create a copy to manipulate
                        HashMap<Integer, Slot> MWLabMapToManipulate = DeepCopyLabSlotMap(
                                lastTreeNode.getSchedule().getMWLab());

                        boolean HardConstraintOk = HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current,
                                (LabSlot) entry.getValue(), lastTreeNode.getSchedule().getMWFLec(),
                                lastTreeNode.getSchedule().getTuThLec(), lastTreeNode.getSchedule().getMWLab(),
                                lastTreeNode.getSchedule().getTuThLab(), lastTreeNode.getSchedule().getFLab());
                        System.out.println("Assignment " + ((Lab) current).toString() + " and Slot "
                                + entry.getValue().toString() + " hard constraint check is: " + HardConstraintOk);
                        if (HardConstraintOk) {

                            // Calculate the penalty of the course slot pairing
                            int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, courseMinPen, pairsPen,
                            brothersPen);   
                            System.out.println("Penalty of Pairing: " + calc);                         

                            // GET THE KEY OF THE ENTRY WE WANT TO ADD TO THEN GET THE VALUE FROM THE
                            // MANIPULATED TABLE
                            // AND ACCESS THE SLOT AT THAT POINT AND ADD THE OCCUPANT
                            // Add the course to the slot in the schedule
                            MWLabMapToManipulate.get(entry.getKey()).addOccupant(current);

                            // Create a new Schedule to add
                            Schedule newSchedule = new Schedule(lastTreeNode.getSchedule());

                            // Replace the TuTh in the newSchedule with the manipulated one
                            newSchedule.setMWLab(MWLabMapToManipulate);

                            int desire = Kontrol.desireability(entry.getValue(), current);

                            // Create a TreeNode as a child of the last TreeNode
                            TreeNode n = new TreeNode(newSchedule, calc + lastTreeNode.getPenaltyValueOfTreeNode(),
                                    lastTreeNode, desire); // Note we need to calculate the total penatly differently
                            System.out.println("Schedule in Node: " + n.toString());
                            lastTreeNode.addChild(n);

                        }
                    }
                    for (Map.Entry<Integer, Slot> entry : lastTreeNode.getSchedule().getTuThLab().entrySet()) {
                        // Create a copy to manipulate
                        HashMap<Integer, Slot> TuThLabMapToManipulate = DeepCopyLabSlotMap(
                                lastTreeNode.getSchedule().getTuThLab());

                        // int calc = 0;

                        boolean HardConstraintOk = HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current,
                                (LabSlot) entry.getValue(), lastTreeNode.getSchedule().getMWFLec(),
                                lastTreeNode.getSchedule().getTuThLec(), lastTreeNode.getSchedule().getMWLab(),
                                lastTreeNode.getSchedule().getTuThLab(), lastTreeNode.getSchedule().getFLab());

                        System.out.println("Assignment " + ((Lab) current).toString() + " and Slot "
                                + entry.getValue().toString() + " hard constraint check is: " + HardConstraintOk);
                        if (HardConstraintOk) {
                            
                            // Calculate the penalty of the course slot pairing
                            int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, courseMinPen, pairsPen,
                            brothersPen);
                            System.out.println("Penalty of Pairing: " + calc);

                            // GET THE KEY OF THE ENTRY WE WANT TO ADD TO THEN GET THE VALUE FROM THE
                            // MANIPULATED TABLE
                            // AND ACCESS THE SLOT AT THAT POINT AND ADD THE OCCUPANT
                            // Add the course to the slot in the schedule
                            TuThLabMapToManipulate.get(entry.getKey()).addOccupant(current);

                            // Create a new Schedule to add
                            Schedule newSchedule = new Schedule(lastTreeNode.getSchedule());

                            // Replace the TuTh in the newSchedule with the manipulated one
                            newSchedule.setTuThLab(TuThLabMapToManipulate);

                            int desire = Kontrol.desireability(entry.getValue(), current);

                            // Create a TreeNode as a child of the last TreeNode
                            TreeNode n = new TreeNode(newSchedule, calc + lastTreeNode.getPenaltyValueOfTreeNode(),
                                    lastTreeNode, desire); // Note we need to calculate the total penatly differently
                            System.out.println("Schedule in Node: " + n.toString());
                            lastTreeNode.addChild(n);

                        }
                    }
                    for (Map.Entry<Integer, Slot> entry : lastTreeNode.getSchedule().getFLab().entrySet()) {
                        // Create a copy to manipulate
                        HashMap<Integer, Slot> FLabMapToManipulate = DeepCopyLabSlotMap(
                                lastTreeNode.getSchedule().getFLab());
                        // Calculate the penalty of the course slot pairing

                        boolean HardConstraintOk = HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current,
                                (LabSlot) entry.getValue(), lastTreeNode.getSchedule().getMWFLec(),
                                lastTreeNode.getSchedule().getTuThLec(), lastTreeNode.getSchedule().getMWLab(),
                                lastTreeNode.getSchedule().getTuThLab(), lastTreeNode.getSchedule().getFLab());
                        System.out.println("Assignment " + ((Lab) current).toString() + " and Slot "
                                + entry.getValue().toString() + " hard constraint check is: " + HardConstraintOk);
                        if (HardConstraintOk) {


                            // Calculate the penalty of the course slot pairing
                            int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, courseMinPen, pairsPen,
                            brothersPen);
                            System.out.println("Penalty of Pairing: " + calc);                            
                            // GET THE KEY OF THE ENTRY WE WANT TO ADD TO THEN GET THE VALUE FROM THE
                            // MANIPULATED TABLE
                            // AND ACCESS THE SLOT AT THAT POINT AND ADD THE OCCUPANT
                            // Add the course to the slot in the schedule
                            FLabMapToManipulate.get(entry.getKey()).addOccupant(current);

                            // Create a new Schedule to add
                            Schedule newSchedule = new Schedule(lastTreeNode.getSchedule());

                            // Replace the TuTh in the newSchedule with the manipulated one
                            newSchedule.setFLab(FLabMapToManipulate);
                            int desire = Kontrol.desireability(entry.getValue(), current);

                            // Create a TreeNode as a child of the last TreeNode
                            TreeNode n = new TreeNode(newSchedule, calc + lastTreeNode.getPenaltyValueOfTreeNode(),
                                    lastTreeNode, desire); // Note we need to calculate the total penatly differently
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
                System.out.println(lastTreeNode.getChildren().size());
                // lastTreeNode = lastTreeNode.getChildren().get(0);
                // for (int k = 0; k< lastTreeNode.getChildren().size(); k++){
                // System.out.println(lastTreeNode.getChildren().get(k).getSchedule().toString());
                // }
                // System.out.println(lastTreeNode.getSchedule().toString());
                if (lastTreeNode.getOrderedChildren().isEmpty()) {
                    i = i - 2;
                    if(i ==-2){
                        System.out.println("No valid solution found in depth first search");
                        System.exit(0);
                    }
                    lastTreeNode = lastTreeNode.getParent();
                } else {
                    lastTreeNode = lastTreeNode.getLowestPenaltyChild();
                }
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


    // TODO: Implement branch and Bound using a control to evaluate
    public TreeNode branchAndBound(TreeNode sol, int penalty, int depthCondition) {
        /*
         * Need solution, node and the penalty for the solution. This will serve as
         * reference Next need to define recursive solution that will "backtrack"
         * through the tree checking the Eval with the solution eval - Once i find a
         * better note the solution and continue
         */

        TreeNode foundBetterSol = null;
        TreeNode prevNode = sol;
        int currentBestValue = penalty;
        for (int i = 0; i < depthCondition; i++) {
            prevNode.setAlreadyLookedAt(true);
            prevNode = prevNode.getParent();
            foundBetterSol = betterSol(prevNode, currentBestValue);

            if (foundBetterSol != null) {
                // TODO: see if this needs to be changed
                // I think this needs to be changed because getPenaltyValueOfNode needs to be
                // the current penalty value up to this node not the node itself
                currentBestValue = foundBetterSol.getPenaltyValueOfTreeNode();
            }
        }

        return foundBetterSol;
    }

    // TODO THIS IS STILL A WORK IN PROGRESS (WIP)
    private TreeNode betterSol(TreeNode n, int penaltyValue) {
        // TODO need to set this node as already looked at somewhere

        ArrayList<TreeNode> childrenList = n.getChildren();
        // if child has no children create them here
        if (childrenList.isEmpty()) {
            // TODO: need to generate the children if the childrenlist is empty
            // however if we are not able to generate more children because this node is the
            // last we need to return the val
            if (n.getPenaltyValueOfTreeNode() < penaltyValue) {
                return n;
            } else {
                return null;
            }
        }

        for (int i = 0; i < childrenList.size(); i++) {
            TreeNode considered = childrenList.get(i);
            if (considered.getPenaltyValueOfTreeNode() >= penaltyValue) {
                considered.setAlreadyLookedAt(true);
            } else {
                if (!considered.getAlreadyLookedAt()) {
                    // TODO need to find how to recursivly return solution
                    // TODO also need to find how to update the nValue once we find a better one
                    betterSol(considered, penaltyValue);
                }
            }
        }

        // return null because we didn't find a solution that was better
        // TODO: see if we still need this, im 90% certain that this will need to go as
        // the
        return null;
    }
}
