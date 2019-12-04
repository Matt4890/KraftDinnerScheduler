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

<<<<<<< Updated upstream
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
=======
    private void checkAndMaybeAddChild(Unit current, Slot slot, TreeNode parent, int currentBound, int allUnitsTotal,
            ArrayList<Slot> emptySlots) {
        // currentBound /= 100;
        TreeNode nodeToAdd = new TreeNode(new Pair(slot, current), 0, parent);
        nodeToAdd.setDepth(parent.getDepth() + 1);
            
        // Check if we break the hard constraint
        boolean HardConstraintOk;
        if (current instanceof Course) {
            HardConstraintOk = HardConstrainsts.checkAssignmentHardConstriantsCourse((Course) current,
                    (CourseSlot) slot, parent);
            // System.out.println("The Hard constraint check for:  Slot:" + slot + " Course: " + current +"is: " + HardConstraintOk);
        } else {
            HardConstraintOk = HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current, (LabSlot) slot,
                    parent);
            // System.out.println("The Hard constraint check for:  Slot:" + slot + " Lab: " + current +"is: " + HardConstraintOk);
        }
        if (HardConstraintOk) {
            // Calculate the penalty value here
            int calc = Kontrol.evalAssignmentPairing(slot, current, parent);
            // System.out.println("-- ANCHOR -- " + currentBound);
            if (calc < currentBound / 20) {
                nodeToAdd.setPenalty(calc + parent.getPenaltyValueOfTreeNode());
                parent.addChild(nodeToAdd);
                // System.out.println("######################################");
                // System.out.println(nodeToAdd);
                // System.out.println("######################################");

                if (nodeToAdd.getDepth() == allUnitsTotal - 1) { // This should change to be something else
                    // Calculate the penalty for the remaining slots
                    // Create a helper method in Generator to calculate all empty slot coursemin and
                    // preference s
                    nodeToAdd.addToPenaltyForBaseNode(includeEmptySlotsInPenalty(emptySlots)); // THIS IS GOING TO HAVE
                                                                                               // TO CHANGE
>>>>>>> Stashed changes

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

<<<<<<< Updated upstream
    public void generateFBound(ArrayList<Unit> toBeAdded) {
        TreeNode lastTreeNode = new TreeNode(this.starter, this.initialPenalty);
        for (int i = 0; i < toBeAdded.size(); i++) {
=======
    private void generateChildrenPairs(Unit current, TreeNode parent, ArrayList<Slot> slotsToPair, int currentBound,
            int allUnitsTotal, ArrayList<Slot> emptySlots) {
        // System.out.println("Running Generate Children ");
        for (Slot slot : slotsToPair) {
            if (current instanceof Course) {
                if (slot instanceof CourseSlot) {
                    checkAndMaybeAddChild(current, slot, parent, currentBound, allUnitsTotal, emptySlots);
                }
>>>>>>> Stashed changes

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

<<<<<<< Updated upstream
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
=======
    // Skeleton of a BNB using a stack
    // Assumes we have a bound function called calculateBound which will
    // Using a heuristic, find a solution xh to the optimization problem. Store its
    // value, B = f(xh). (If no heuristic is available, set B to infinity.) B will
    // denote the best solution found so far, and will be used as an upper bound on
    // candidate solutions.
    // Initialize a queue to hold a partial solution with none of the variables of
    // the problem assigned.
    // Loop until the queue is empty:
    // Take a node N off the queue.
    // If N represents a single candidate solution x and f(x) < B, then x is the
    // best solution so far. Record it and set B ← f(x).
    // Else, branch on N to produce new nodes Ni. For each of these:
    // If bound(Ni) > B, do nothing; since the lower bound on this node is greater
    // than the upper bound of the problem, it will never lead to the optimal
    // solution, and can be discarded.
    // Else, store Ni on the queue.
    public void branchAndBoundSkeleton(TreeNode starter, ArrayList<Unit> unitsToBeScheduled,
            ArrayList<Slot> slotToScheduleIn, int numPartialAssignments) {

        // System.out.println("RUNNING BNB");
        Stack<TreeNode> allStackNodes = new Stack<TreeNode>();
        this.startNode = starter;
        this.bound = Integer.MAX_VALUE;
       
        TreeNode nodeToAdd  = this.startNode;
        while (nodeToAdd.getChildren().size() != 0){
            nodeToAdd = nodeToAdd.getChildren().get(0);
            

        }
        //// System.out.println(nodeToAdd);
        //// System.out.println("Node Depth: " + nodeToAdd.getDepth());

        allStackNodes.add(nodeToAdd);
        // this.bound = Integer.MAX_VALUE;
        int numUnitsToSchedule = unitsToBeScheduled.size();
        int depth = 0;
        // this.bestSchedule = null;

        // // System.out.println(allStackNodes);
        while (!allStackNodes.isEmpty()) {
            // System.out.println("While looooop ran");
            // // System.out.println(allStackNodes);
            TreeNode currentNode = allStackNodes.pop();

            if (currentNode.getDepth() == unitsToBeScheduled.size() + (numPartialAssignments - 1)) { // TheScheduleInsideRepresents a Full
                                                                       // Solution
                // System.out.println(
                        // "WE GOT TO THE BOTTOM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                // System.out.println("Bound is: " + currentNode.getPenaltyValueOfTreeNode());
                // System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                // System.out.println(currentNode.toString());
                // System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                if (currentNode.getPenaltyValueOfTreeNode() < this.bound) {
                    // System.out.println("So Bound got replaced");
                    this.bound = currentNode.getPenaltyValueOfTreeNode();
                    this.bestSchedule = currentNode;
>>>>>>> Stashed changes

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
<<<<<<< Updated upstream
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

        TreeNode x = branchAndBound(lastTreeNode, lastTreeNode.getPenaltyValueOfTreeNode(), toBeAdded.size()-4, toBeAdded);
        try{
            System.out.println(x.getPenaltyValueOfTreeNode());  
        }
        catch(Exception e){
            System.out.println("branch and bound was null");  
        }
              
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

    // public void createFBound(ArrayList<Unit> toBeAdded) {

    // this.tree.addRoot(new Node(this.starter, this.initialPenalty));
    // Node lastNode = this.tree.getRoot();

    // for (int i = 0; i < toBeAdded.size(); i++) {
    // // Check what type of unit it is and then create the nodes accordingly
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
    // Node newCreatedNode = new Node(this.starter, minimum +
    // lastNode.getPenaltyValueOfNode(), lastNode);
    // lastNode.addChild(newCreatedNode);
    // lastNode = newCreatedNode;

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
    // Node newCreatedNode = new Node(this.starter, minimum +
    // lastNode.getPenaltyValueOfNode(), lastNode);
    // lastNode.addChild(newCreatedNode);
    // lastNode = newCreatedNode;

    // }
    // this.bound += minimum;
    // System.out.println(bound);
    // }
    // }
    //

    public TreeNode branchAndBound(TreeNode sol, int penalty, int depthCondition, ArrayList<Unit> ListToBeAdded) {

        TreeNode foundBetterSol = sol;
        TreeNode prevNode = sol;
        int currentBestValue = penalty;
        for (int i = ListToBeAdded.size()-2; i >= depthCondition; i--) {
            prevNode.setAlreadyLookedAt(true);
            prevNode = prevNode.getParent();
            TreeNode betterSolNode = betterSol(prevNode, currentBestValue, i, ListToBeAdded);
            

            if (betterSolNode != null){ 
                if(betterSolNode.getPenaltyValueOfTreeNode() < foundBetterSol.getPenaltyValueOfTreeNode()) {
                    foundBetterSol = betterSolNode;
                    currentBestValue = betterSolNode.getPenaltyValueOfTreeNode();
=======
                // System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

                // System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

                for (int i = 0; i < currentNode.getOrderedChildren().size(); i++) {
                    // allStackNodes.push(currentNode.getOrderedChildren().remove());
                    allStackNodes.push(currentNode.getOrderedChildren().remove());
>>>>>>> Stashed changes
                }
            }
        }

        if(foundBetterSol.getPenaltyValueOfTreeNode() >= sol.getPenaltyValueOfTreeNode()){
            return null;
        }
        return foundBetterSol;
    }

    private TreeNode betterSol(TreeNode n, int penaltyValue, int currentDepth, ArrayList<Unit> ListToBeAdded) {
        //TODO still need a control just spelt with a k instead of a c 

        ArrayList<TreeNode> childrenList = n.getChildren();
        // if child has no children 
        //      - check if has no more children
        //      - if it has more children generate them    
        if (childrenList.isEmpty()) {
            if (currentDepth == ListToBeAdded.size()-1) {
                n.setAlreadyLookedAt(true);
                n.setgetBestBottomTN(n);
                return n;
            } 
            else if (currentDepth < ListToBeAdded.size()-1){
                //TODO this node has children that need to be generated since it's not the very last node  
            }
           
        }

        //keep track of all the nodes child nodes
        ArrayList<TreeNode> keepTrack = new ArrayList<TreeNode>();
        for (int i = 0; i < childrenList.size(); i++) {
            TreeNode considered = childrenList.get(i);
            if (considered.getPenaltyValueOfTreeNode() > penaltyValue) {
                considered.setAlreadyLookedAt(true);
            } else {
                if (!considered.getAlreadyLookedAt()) {
                    TreeNode temp = betterSol(considered, penaltyValue, currentDepth + 1, ListToBeAdded);
                    if(temp != null){
                        keepTrack.add(temp.getBestBottomTN());
                    }
                }
            }
        }
        n.setAlreadyLookedAt(true);

        //find the best child node here 
        int bestPen = Integer.MAX_VALUE;
        TreeNode bestNode = null;
        for(TreeNode tn : keepTrack){
            if(tn != null && tn.getPenaltyValueOfTreeNode() < bestPen){
                bestNode = tn;
                bestPen = tn.getPenaltyValueOfTreeNode();
            }
        }

        //return the best node if it is not null otherwise return null  
        if(bestNode != null && bestNode.getPenaltyValueOfTreeNode() < penaltyValue){
            n.setgetBestBottomTN(bestNode);
            return bestNode;
        }
        return null;
    }
}
