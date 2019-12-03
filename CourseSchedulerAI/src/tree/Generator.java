package tree;

import Constraints.*;
import schedule.*;
import coursesULabs.*;
import java.util.*;

public class Generator {

    private TreeNode starter;
    private int initialPenalty;
    private int bound;
    private int weightMin;
    private int weightPairs;
    private int weightBrothersSectionDiff;
    private TreeNode startNode;
    private TreeNode bestSchedule;

    public Generator(TreeNode starter, int penalty, int weight_min, int weight_pairs, int wait_section_diff) {

        // this.tree.addRoot(new TreeNode(this.starter, this.initialPenalty));
        this.starter = starter;
        this.initialPenalty = penalty;
        this.bound = Integer.MAX_VALUE;
        this.weightMin = weight_min;
        this.weightPairs = weight_pairs;
        this.weightBrothersSectionDiff = wait_section_diff;
        this.bestSchedule = null;

    }

    private void checkAndMaybeAddChild(Unit current, Slot slot, TreeNode parent, int currentBound) {
        TreeNode nodeToAdd = new TreeNode(new Pair(slot, current), 0, parent);
        nodeToAdd.setDepth(parent.getDepth() + 1);

        // Check if we break the hard constraint
        boolean HardConstraintOk = HardConstrainsts.checkAssignmentHardConstriants(nodeToAdd);
        if (HardConstraintOk) {
            // Calculate the penalty value here
            int calc = SoftConstraints.calculatePenalty(nodeToAdd);
            if (calc < currentBound) {
                nodeToAdd.setPenalty(calc);
                parent.addChild(nodeToAdd);
            }
        }
    }

    private void generateChildrenPairs(Unit current, TreeNode parent, ArrayList<Slot> slotsToPair, int currentBound) {
        for (Slot slot : slotsToPair) {
            if (current instanceof Course) {
                if (slot instanceof CourseSlot) {
                    checkAndMaybeAddChild(current, slot, parent, currentBound);
                }

            } else {
                if (slot instanceof LabSlot) {
                    checkAndMaybeAddChild(current, slot, parent, currentBound);
                }

            }
        }

    }

    private void generateChildren(Unit current, TreeNode parent, int allUnitsTotal, int currBound) {
        double potentialLost = -current.getPotential();
        // System.out.println(current.getPotential() + " <------------- potential");

        // Run through each possible course slot pair and create children
        if (current instanceof Course) {

            // Copy the hashMap to Reference
            for (Map.Entry<Integer, Slot> entry : parent.getSchedule().getMWFLec().entrySet()) {
                // Create a copy to manipulate

                HashMap<Integer, Slot> MWFMapToManipulate = DeepCopyCourseSlotMap(parent.getSchedule().getMWFLec());

                // int calc = 0;

                boolean HardConstraintOk = HardConstrainsts.checkAssignmentHardConstriantsCourse((Course) current,
                        (CourseSlot) entry.getValue());
                // System.out.println("Assignment " + ((Course) current).toString() + " and Slot
                // "
                // + entry.getValue().toString() + " hard constraint check is: " +
                // HardConstraintOk);
                if (HardConstraintOk) {

                    // Calculate the penalty of the course slot pairing
                    int calc = Kontrol.evalAssignment(entry.getValue(), current);
                    if (calc < currBound) {
                        System.out.println("Penalty of Pairing: " + calc);
                        // GET THE KEY OF THE ENTRY WE WANT TO ADD TO THEN GET THE VALUE FROM THE
                        // MANIPULATED TABLE
                        // AND ACCESS THE SLOT AT THAT POINT AND ADD THE OCCUPANT
                        // Add the course to the slot in the schedule
                        MWFMapToManipulate.get(entry.getKey()).addOccupant(current);

                        // Create a new Schedule to add
                        Schedule newSchedule = new Schedule(parent.getSchedule());

                        // Replace the MWF in the newSchedule with the manipulated one
                        newSchedule.setMWFLec(MWFMapToManipulate);
                        // int desire = Kontrol.desireability(entry.getValue(), current);

                        // Create a TreeNode as a child of the last TreeNode
                        TreeNode n = new TreeNode(newSchedule, calc + parent.getPenaltyValueOfTreeNode(), parent, 0); // Note
                                                                                                                      // we
                                                                                                                      // need
                                                                                                                      // to
                                                                                                                      // calculate
                                                                                                                      // the
                                                                                                                      // total
                                                                                                                      // penatly
                                                                                                                      // differently
                        n.setDepth(parent.getDepth() + 1);
                        if (n.getDepth() == allUnitsTotal - 1) {
                            // Calculate the penalty for the remaining slots
                            // Create a helper method in Generator to calculate all empty slot coursemin and
                            // preference s
                            n.addToPenaltyForBaseNode(baseOfTreePenaltyCalculation(n));

                        }
                        // System.out.println("Schedule in Node: " + n.toString());
                        parent.addChild(n);
                        updatePotentialCourse((CourseSlot) MWFMapToManipulate.get(entry.getKey()), current, n);
                        // System.out.println(potentialLost + " <------------------ potential");
                        n.incrementPotential(potentialLost + parent.getPotential());
                        // System.out.println("the starting potential of PARENT tree node is " +
                        // parent.getPotential());
                        // System.out.println("the starting potential of child tree node is " +
                        // n.getPotential());
                    }
                }
            }
            for (Map.Entry<Integer, Slot> entry : parent.getSchedule().getTuThLec().entrySet()) {
                System.out.println("We got to here 4");

                // Create a copy to manipulate
                HashMap<Integer, Slot> TuThMapToManipulate = DeepCopyCourseSlotMap(parent.getSchedule().getTuThLec());
                // int calc = 0;

                boolean HardConstraintOk = HardConstrainsts.checkAssignmentHardConstriantsCourse((Course) current,
                        (CourseSlot) entry.getValue());
                // System.out.println("Assignment " + ((Course) current).toString() + " and Slot
                // "
                // + entry.getValue().toString() + " hard constraint check is: " +
                // HardConstraintOk);
                if (HardConstraintOk) {

                    // Calculate the penalty of the course slot pairing
                    int calc = Kontrol.evalAssignment(entry.getValue(), current);

                    System.out.println("Penalty of Pairing: " + calc);
                    if (calc < currBound) {
                        // GET THE KEY OF THE ENTRY WE WANT TO ADD TO THEN GET THE VALUE FROM THE
                        // MANIPULATED TABLE
                        // AND ACCESS THE SLOT AT THAT POINT AND ADD THE OCCUPANT
                        // Add the course to the slot in the schedule
                        TuThMapToManipulate.get(entry.getKey()).addOccupant(current);

                        // Create a new Schedule to add
                        Schedule newSchedule = new Schedule(parent.getSchedule());

                        // Replace the TuTh in the newSchedule with the manipulated one
                        newSchedule.setTuThLec(TuThMapToManipulate);
                        // int desire = Kontrol.desireability(entry.getValue(), current);

                        // Create a TreeNode as a child of the last TreeNode
                        TreeNode n = new TreeNode(newSchedule, calc + parent.getPenaltyValueOfTreeNode(), parent, 0); // Note
                                                                                                                      // we
                                                                                                                      // need
                                                                                                                      // to
                                                                                                                      // calculate
                                                                                                                      // the
                                                                                                                      // total
                                                                                                                      // penatly
                                                                                                                      // differently

                        n.setDepth(parent.getDepth() + 1);
                        if (n.getDepth() == allUnitsTotal - 1) {
                            // Calculate the penalty for the remaining slots
                            // Create a helper method in Generator to calculate all empty slot coursemin and
                            // preference s
                            n.addToPenaltyForBaseNode(baseOfTreePenaltyCalculation(n));

                        }

                        // System.out.println("Schedule in Node: " + n.toString());
                        parent.addChild(n);
                        updatePotentialCourse((CourseSlot) TuThMapToManipulate.get(entry.getKey()), current, n);
                        // System.out.println(potentialLost + " <------------------ potential");
                        n.incrementPotential(potentialLost + parent.getPotential());
                        // System.out.println("the starting potential of PARENT tree node is " +
                        // parent.getPotential());
                        // System.out.println("the starting potential of child tree node is " +
                        // n.getPotential());

                    }
                }
            }

        } else {
            for (Map.Entry<Integer, Slot> entry : parent.getSchedule().getMWLab().entrySet()) {

                // Create a copy to manipulate
                HashMap<Integer, Slot> MWLabMapToManipulate = DeepCopyLabSlotMap(parent.getSchedule().getMWLab());

                boolean HardConstraintOk = HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current,
                        (LabSlot) entry.getValue(), parent.getSchedule().getMWFLec(), parent.getSchedule().getTuThLec(),
                        parent.getSchedule().getMWLab(), parent.getSchedule().getTuThLab(),
                        parent.getSchedule().getFLab());
                // System.out.println("Assignment " + ((Lab) current).toString() + " and Slot "
                // + entry.getValue().toString() + " hard constraint check is: " +
                // HardConstraintOk);
                if (HardConstraintOk) {

                    // Calculate the penalty of the course slot pairing
                    int calc = Kontrol.evalAssignment(entry.getValue(), current);

                    System.out.println("Penalty of Pairing: " + calc);
                    if (calc < currBound) {
                        // GET THE KEY OF THE ENTRY WE WANT TO ADD TO THEN GET THE VALUE FROM THE
                        // MANIPULATED TABLE
                        // AND ACCESS THE SLOT AT THAT POINT AND ADD THE OCCUPANT
                        // Add the course to the slot in the schedule
                        MWLabMapToManipulate.get(entry.getKey()).addOccupant(current);

                        // Create a new Schedule to add
                        Schedule newSchedule = new Schedule(parent.getSchedule());

                        // Replace the TuTh in the newSchedule with the manipulated one
                        newSchedule.setMWLab(MWLabMapToManipulate);

                        // int desire = Kontrol.desireability(entry.getValue(), current);

                        // Create a TreeNode as a child of the last TreeNode
                        TreeNode n = new TreeNode(newSchedule, calc + parent.getPenaltyValueOfTreeNode(), parent, 0); // Note
                                                                                                                      // we
                                                                                                                      // need
                                                                                                                      // to
                                                                                                                      // calculate
                                                                                                                      // the
                                                                                                                      // total
                                                                                                                      // penatly
                                                                                                                      // differently
                        n.setDepth(parent.getDepth() + 1);
                        if (n.getDepth() == allUnitsTotal - 1) {
                            // Calculate the penalty for the remaining slots
                            // Create a helper method in Generator to calculate all empty slot coursemin and
                            // preference s
                            n.addToPenaltyForBaseNode(baseOfTreePenaltyCalculation(n));

                        }
                        // System.out.println("Schedule in Node: " + n.toString());
                        parent.addChild(n);
                        updatePotentialLab((LabSlot) MWLabMapToManipulate.get(entry.getKey()), current, n);
                        // System.out.println(potentialLost + " <------------------ potential");
                        n.incrementPotential(potentialLost + parent.getPotential());
                        // System.out.println("the starting potential of PARENT tree node is " +
                        // parent.getPotential());
                        // System.out.println("the starting potential of child tree node is " +
                        // n.getPotential());

                    }
                }
            }

            for (Map.Entry<Integer, Slot> entry : parent.getSchedule().getTuThLab().entrySet()) {

                // Create a copy to manipulate
                HashMap<Integer, Slot> TuThLabMapToManipulate = DeepCopyLabSlotMap(parent.getSchedule().getTuThLab());

                // int calc = 0;

                boolean HardConstraintOk = HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current,
                        (LabSlot) entry.getValue(), parent.getSchedule().getMWFLec(), parent.getSchedule().getTuThLec(),
                        parent.getSchedule().getMWLab(), parent.getSchedule().getTuThLab(),
                        parent.getSchedule().getFLab());

                // System.out.println("Assignment " + ((Lab) current).toString() + " and Slot "
                // + entry.getValue().toString() + " hard constraint check is: " +
                // HardConstraintOk);
                if (HardConstraintOk) {

                    // Calculate the penalty of the course slot pairing
                    int calc = Kontrol.evalAssignment(entry.getValue(), current);
                    if (calc < currBound) {
                        System.out.println("Penalty of Pairing: " + calc);

                        // GET THE KEY OF THE ENTRY WE WANT TO ADD TO THEN GET THE VALUE FROM THE
                        // MANIPULATED TABLE
                        // AND ACCESS THE SLOT AT THAT POINT AND ADD THE OCCUPANT
                        // Add the course to the slot in the schedule
                        TuThLabMapToManipulate.get(entry.getKey()).addOccupant(current);

                        // Create a new Schedule to add
                        Schedule newSchedule = new Schedule(parent.getSchedule());

                        // Replace the TuTh in the newSchedule with the manipulated one
                        newSchedule.setTuThLab(TuThLabMapToManipulate);

                        // int desire = Kontrol.desireability(entry.getValue(), current);

                        // Create a TreeNode as a child of the last TreeNode
                        TreeNode n = new TreeNode(newSchedule, calc + parent.getPenaltyValueOfTreeNode(), parent, 0); // Note
                                                                                                                      // we
                                                                                                                      // need
                                                                                                                      // to
                                                                                                                      // calculate
                                                                                                                      // the
                                                                                                                      // total
                                                                                                                      // penatly
                                                                                                                      // differently
                        n.setDepth(parent.getDepth() + 1);
                        if (n.getDepth() == allUnitsTotal - 1) {
                            // Calculate the penalty for the remaining slots
                            // Create a helper method in Generator to calculate all empty slot coursemin and
                            // preference s
                            n.addToPenaltyForBaseNode(baseOfTreePenaltyCalculation(n));

                        }
                        // System.out.println("Schedule in Node: " + n.toString());
                        parent.addChild(n);
                        updatePotentialLab((LabSlot) TuThLabMapToManipulate.get(entry.getKey()), current, n);
                        // System.out.println(n.getPotential() + " <------------------ potential of n");
                        // System.out.println(potentialLost + " <------------------ potential");
                        n.incrementPotential(potentialLost + parent.getPotential());
                        // System.out.println("the starting potential of PARENT tree node is " +
                        // parent.getPotential());
                        // System.out.println("the starting potential of child tree node is " +
                        // n.getPotential());

                    }

                }
            }
            for (Map.Entry<Integer, Slot> entry : parent.getSchedule().getFLab().entrySet()) {

                // Create a copy to manipulate
                HashMap<Integer, Slot> FLabMapToManipulate = DeepCopyLabSlotMap(parent.getSchedule().getFLab());
                // Calculate the penalty of the course slot pairing

                boolean HardConstraintOk = HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current,
                        (LabSlot) entry.getValue(), parent.getSchedule().getMWFLec(), parent.getSchedule().getTuThLec(),
                        parent.getSchedule().getMWLab(), parent.getSchedule().getTuThLab(),
                        parent.getSchedule().getFLab());
                // System.out.println("Assignment " + ((Lab) current).toString() + " and Slot "
                // + entry.getValue().toString() + " hard constraint check is: " +
                // HardConstraintOk);
                if (HardConstraintOk) {

                    // Calculate the penalty of the course slot pairing
                    int calc = Kontrol.evalAssignment(entry.getValue(), current);
                    if (calc < currBound) {

                        System.out.println("Penalty of Pairing: " + calc);
                        // GET THE KEY OF THE ENTRY WE WANT TO ADD TO THEN GET THE VALUE FROM THE
                        // MANIPULATED TABLE
                        // AND ACCESS THE SLOT AT THAT POINT AND ADD THE OCCUPANT
                        // Add the course to the slot in the schedule
                        FLabMapToManipulate.get(entry.getKey()).addOccupant(current);

                        // Create a new Schedule to add
                        Schedule newSchedule = new Schedule(parent.getSchedule());

                        // Replace the TuTh in the newSchedule with the manipulated one
                        newSchedule.setFLab(FLabMapToManipulate);
                        // int desire = Kontrol.desireability(entry.getValue(), current);

                        // Create a TreeNode as a child of the last TreeNode
                        TreeNode n = new TreeNode(newSchedule, calc + parent.getPenaltyValueOfTreeNode(), parent, 0); // Note
                                                                                                                      // we
                                                                                                                      // need
                                                                                                                      // to
                                                                                                                      // calculate
                                                                                                                      // the
                                                                                                                      // total
                                                                                                                      // penatly
                                                                                                                      // differently
                        n.setDepth(parent.getDepth() + 1);
                        if (n.getDepth() == allUnitsTotal - 1) {
                            // Calculate the penalty for the remaining slots
                            // Create a helper method in Generator to calculate all empty slot coursemin and
                            // preference s
                            n.addToPenaltyForBaseNode(baseOfTreePenaltyCalculation(n));

                        }
                        // System.out.println("Schedule in Node: " + n.toString());
                        parent.addChild(n);
                        updatePotentialLab((LabSlot) FLabMapToManipulate.get(entry.getKey()), current, n);
                        // System.out.println(n.getPotential() + " <------------------ potential of n");
                        // System.out.println(potentialLost + " <------------------ potential");
                        n.incrementPotential(potentialLost + parent.getPotential());
                        // System.out.println("the starting potential of PARENT tree node is " +
                        // parent.getPotential());
                        // System.out.println("the starting potential of child tree node is " +
                        // n.getPotential());

                    }
                }
            }
        }
    }

    private int baseOfTreePenaltyCalculation(TreeNode baseNode) {
        // System.out.println("RAN THE BASE OF TREE PENALTY CALC");
        Schedule toCheck = baseNode.getSchedule();
        int evalToAdd = 0;
        evalToAdd += calculatePenaltyForEmptySlotsCourses(toCheck.getMWFLec());
        evalToAdd += calculatePenaltyForEmptySlotsCourses(toCheck.getTuThLec());
        evalToAdd += calculatePenaltyForEmptySlotsLabs(toCheck.getMWLab());
        evalToAdd += calculatePenaltyForEmptySlotsLabs(toCheck.getTuThLab());
        evalToAdd += calculatePenaltyForEmptySlotsLabs(toCheck.getFLab());

        return evalToAdd;

    }

    private int calculatePenaltyForEmptySlotsCourses(HashMap<Integer, Slot> checkMe) {
        int evalToAdd = 0;
        for (Map.Entry<Integer, Slot> entry : checkMe.entrySet()) {
            if (entry.getValue().getClassAssignment().size() == 0) {
                // CourseMin Check
                if (((CourseSlot) entry.getValue()).getCourseMin() != 0) {
                    evalToAdd += (1 * Kontrol.getWeight_min_filled());
                }
                // Preferences
                if (entry.getValue().getPreference().size() != 0) {
                    for (Map.Entry<Unit, Integer> entry1 : entry.getValue().getPreference().entrySet()) {
                        evalToAdd += entry1.getValue() * Kontrol.getWeight_pref();
                    }
                }
            }
        }
        return evalToAdd;
    }

    private int calculatePenaltyForEmptySlotsLabs(HashMap<Integer, Slot> checkMe) {
        int evalToAdd = 0;
        for (Map.Entry<Integer, Slot> entry : checkMe.entrySet()) {
            if (entry.getValue().getClassAssignment().size() == 0) {
                // CourseMin Check
                if (((LabSlot) entry.getValue()).getLabMin() != 0) {
                    evalToAdd += (1 * Kontrol.getWeight_min_filled());
                }
                // Preferences
                if (entry.getValue().getPreference().size() != 0) {
                    for (Map.Entry<Unit, Integer> entry1 : entry.getValue().getPreference().entrySet()) {
                        evalToAdd += entry1.getValue() * Kontrol.getWeight_pref();
                    }
                }
            }
        }
        return evalToAdd;
    }

    private void updatePotentialLab(LabSlot labSlot, Unit current, TreeNode node) {
        if (labSlot.getLabCount() == labSlot.getLabMin()) {
            node.incrementPotential(weightMin);
        }
    }

    private void updatePotentialCourse(CourseSlot courseSlot, Unit current, TreeNode node) {
        if (courseSlot.getCourseCount() == courseSlot.getCourseMin()) {
            node.incrementPotential(weightMin);
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
    public void branchAndBoundSkeleton(ArrayList<Unit> unitsToBeScheduled) {
        System.out.println("RUNNING BNB");
        Stack<TreeNode> allStackNodes = new Stack<TreeNode>();
        TreeNode parent = new TreeNode(this.starter, this.initialPenalty);
        parent.setDepth(0);
        this.startNode = parent;
        allStackNodes.add(this.startNode);
        // this.bound = Integer.MAX_VALUE;
        int numUnitsToSchedule = unitsToBeScheduled.size();
        int depth = 0;
        // this.bestSchedule = null;

        // System.out.println(allStackNodes);
        while (!allStackNodes.isEmpty()) {
            System.out.println("While looooop ran");
            // System.out.println(allStackNodes);
            TreeNode currentNode = allStackNodes.pop();

            if (currentNode.getDepth() == unitsToBeScheduled.size()) { // TheScheduleInsideRepresents a Full
                                                                       // Solution
                System.out.println(
                        "WE GOT TO THE BOTTOM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("Bound is: " + currentNode.getPenaltyValueOfTreeNode());
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                System.out.println(currentNode.toString());
                System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                if (currentNode.getPenaltyValueOfTreeNode() < this.bound) {
                    System.out.println("So Bound got replaced");
                    this.bound = currentNode.getPenaltyValueOfTreeNode();
                    this.bestSchedule = currentNode;

                }
            }

            else {
                // Check to see if doesn't have children made
                Unit scheduleMe = unitsToBeScheduled.get(currentNode.getDepth());

                if (currentNode.getChildren().size() == 0) {

                    generateChildren(scheduleMe, currentNode, unitsToBeScheduled.size(), this.bound);
                    depth++;
                }
                // Go through all of the children and add them to the stack if the eval of the
                // We can change this to the priority queue and order the children by their

                // add the est chilren to the stack
                for (int i = 0; i < currentNode.getOrderedChildren().size(); i++) {
                    // allStackNodes.push(currentNode.getOrderedChildren().remove());
                    allStackNodes.push(currentNode.getChildren().get(i));
                }

            }

        }
        if (bestSchedule == null) {
            System.out.println("No Valid Solution");
        } else {
            System.out.println("We are out of things to add...");
            System.out.println("The Best:");
            System.out.println(this.bestSchedule.toString());
            System.out.println(this.bestSchedule.getPenaltyValueOfTreeNode());
        }
    }

}