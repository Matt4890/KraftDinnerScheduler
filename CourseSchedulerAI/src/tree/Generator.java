package tree;

import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

import Constraints.HardConstrainsts;
import coursesULabs.Course;
import coursesULabs.Lab;
import coursesULabs.Unit;
import schedule.CourseSlot;
import schedule.LabSlot;
import schedule.Slot;

public class Generator {

    private TreeNode starter;

    private int bound;

    private TreeNode startNode;
    private TreeNode bestSchedule;
    private int totalSize;

    public Generator(TreeNode starter, int totalSize) {

        this.starter = starter;
        this.bound = Integer.MAX_VALUE;
        this.bestSchedule = null;
        this.totalSize = totalSize;

    }

    private void checkAndMaybeAddChild(Unit current, Slot slot, TreeNode parent, int currentBound, int allUnitsTotal,
            ArrayList<Slot> emptySlots) {
        TreeNode nodeToAdd = new TreeNode(new Pair(slot, current), 0, parent);
        nodeToAdd.setDepth(parent.getDepth() + 1);

        // Check if we break the hard constraint
        boolean HardConstraintOk;
        if (current instanceof Course) {
            HardConstraintOk = HardConstrainsts.checkAssignmentHardConstriantsCourse((Course) current,
                    (CourseSlot) slot, nodeToAdd);
            // System.out.println("The Hard constraint check for: Slot:" + slot + " Course:
            // " + current +"is: " + HardConstraintOk);
        } else {
            HardConstraintOk = HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current, (LabSlot) slot,
                    nodeToAdd);
            // System.out.println("The Hard constraint check for: Slot:" + slot + " Lab: " +
            // current +"is: " + HardConstraintOk);
        }
        if (HardConstraintOk) {
            // Calculate the penalty value here
            int calc = Kontrol.evalAssignmentPairing(slot, current, nodeToAdd) + parent.getPenaltyValueOfTreeNode();
            if (calc < currentBound) {
                nodeToAdd.setPenalty(calc);
                parent.addChild(nodeToAdd);
                // System.out.println("######################################");
                // System.out.println(nodeToAdd);
                // System.out.println("######################################");

                if (nodeToAdd.getDepth() == totalSize) { // This should change to be something else
                    // Calculate the penalty for the remaining slots
                    // Create a helper method in Generator to calculate all empty slot coursemin and
                    // preference s
                    // nodeToAdd.addToPenaltyForBaseNode(includeEmptySlotsInPenalty(emptySlots)); //
                    // THIS IS GOING TO HAVE
                    // TO CHANGE
                    nodeToAdd.setPenalty(
                            nodeToAdd.getPenaltyValueOfTreeNode() + Kontrol.calculateMin(nodeToAdd, slot, current));

                }
            } else {
                // System.out.println("It Broke the bound so I didn't add it at depth " +
                // nodeToAdd.getDepth());
            }
        }
    }

    private void generateChildrenPairs(Unit current, TreeNode parent, ArrayList<Slot> slotsToPair, int currentBound,
            int allUnitsTotal, ArrayList<Slot> emptySlots) {
        // System.out.println("Running Generate Children ");
        for (Slot slot : slotsToPair) {
            if (current instanceof Course) {
                if (slot instanceof CourseSlot) {
                    checkAndMaybeAddChild(current, slot, parent, currentBound, allUnitsTotal, emptySlots);
                }

            } else {
                if (slot instanceof LabSlot) {
                    checkAndMaybeAddChild(current, slot, parent, currentBound, allUnitsTotal, emptySlots);
                }

            }
        }

    }

    private int includeEmptySlotsInPenalty(ArrayList<Slot> emptySlots) {
        int evalToAdd = 0;
        for (Slot slot : emptySlots) {
            if (slot instanceof CourseSlot) {
                if (((CourseSlot) slot).getCourseMin() != 0) {
                    evalToAdd += (1 * Kontrol.getWeight_min_filled());
                }
                // Preferences
                if (slot.getPreference().size() != 0) {
                    for (Map.Entry<Unit, Integer> entry1 : slot.getPreference().entrySet()) {
                        evalToAdd += entry1.getValue() * Kontrol.getWeight_pref();
                    }
                }
            } else {
                if (((LabSlot) slot).getLabMin() != 0) {
                    evalToAdd += (1 * Kontrol.getWeight_min_filled());
                }
                // Preferences
                if (slot.getPreference().size() != 0) {
                    for (Map.Entry<Unit, Integer> entry1 : slot.getPreference().entrySet()) {
                        evalToAdd += entry1.getValue() * Kontrol.getWeight_pref();
                    }
                }

            }
        }
        return evalToAdd;
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
    public void branchAndBoundSkeleton(TreeNode starter, ArrayList<Unit> unitsToBeScheduled,
            ArrayList<Slot> slotToScheduleIn, int numPartialAssignments) {

        // System.out.println("RUNNING BNB");
        Stack<TreeNode> allStackNodes = new Stack<TreeNode>();
        this.startNode = starter;
        this.bound = Integer.MAX_VALUE;

        TreeNode nodeToAdd = this.startNode;
        while (nodeToAdd.getChildren().size() != 0) {
            nodeToAdd = nodeToAdd.getChildren().get(0);

        }
        // System.out.println(nodeToAdd);
        // System.out.println("Node Depth: " + nodeToAdd.getDepth());

        allStackNodes.add(nodeToAdd);
        // this.bound = Integer.MAX_VALUE;
        int numUnitsToSchedule = unitsToBeScheduled.size();
        int depth = 0;
        // this.bestSchedule = null;

        // System.out.println(allStackNodes);
        while (!allStackNodes.isEmpty()) {
            // System.out.println("While looooop ran");
            // System.out.println(allStackNodes);
            TreeNode currentNode = allStackNodes.pop();

            if (currentNode.getDepth() == unitsToBeScheduled.size() + Math.max(numPartialAssignments - 1, 0)) { // TheScheduleInsideRepresents
                                                                                                                // a
                                                                                                                // Full
                // Solution
                // System.out.println(
                // "WE GOT TO THE
                // BOTTOM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                // System.out.println("Bound is: " + currentNode.getPenaltyValueOfTreeNode());
                // System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                // System.out.println(currentNode.toString());
                // System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                if (currentNode.getPenaltyValueOfTreeNode() < this.bound) {

                    this.bound = currentNode.getPenaltyValueOfTreeNode();
                    this.bestSchedule = currentNode;
                    // System.out.println("So Bound got replaced with" + this.bound);

                }
            }

            else {
                // Check to see if doesn't have children made

                ArrayList<Slot> emptySlots = new ArrayList<Slot>();
                if (currentNode.getOrderedChildren().size() == 0) {
                    // Do a check up the tree to see if the slot is included along the path
                    // Whats a more efficient way to do this??
                    Unit scheduleMe = null;
                    if (currentNode.getDepth() == 0) {
                        scheduleMe = unitsToBeScheduled.get(currentNode.getDepth());
                    } else {
                        scheduleMe = unitsToBeScheduled
                                .get(currentNode.getDepth() - Math.max(numPartialAssignments - 1, 0));
                    } // TEST ME

                    if (scheduleMe.equals(unitsToBeScheduled.get(unitsToBeScheduled.size() - 1))) { // We are reaching
                                                                                                    // the bottom - i.e.
                                                                                                    // the last thing is
                                                                                                    // being scheduled

                        TreeNode check = currentNode;
                        ArrayList<Slot> scheduledSlots = new ArrayList<Slot>();
                        while (check.getParent() != null) {
                            scheduledSlots.add(check.getAssign().getSlot());
                            check = check.getParent();
                        }
                        for (Slot slot : slotToScheduleIn) {
                            if (!scheduledSlots.contains(slot)) {
                                emptySlots.add(slot);
                            }
                        }
                    }

                    generateChildrenPairs(scheduleMe, currentNode, slotToScheduleIn, this.bound,
                            unitsToBeScheduled.size(), emptySlots);
                    depth++;
                }
                // System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

                // System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

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

            formatOutput();

        }
    }

    private void formatOutput() {
        TreeNode current = this.bestSchedule;
        ArrayList<String> orderedStrings = new ArrayList<String>();
        System.out.println("Eval-value: " + this.bestSchedule.getPenaltyValueOfTreeNode());
        while (current != null) {
            if (current.getAssign().getUnit() != null)
                orderedStrings
                        .add(String.format("%-30.30s : %-30.30s%n", current.getAssign().getUnit().toPrettyString(),
                                current.getAssign().getSlot().toPrettyString()));
            current = current.getParent();
        }
        orderedStrings.sort(String::compareToIgnoreCase);
        for (String s : orderedStrings) {
            System.out.print(s);
        }

    }

}
