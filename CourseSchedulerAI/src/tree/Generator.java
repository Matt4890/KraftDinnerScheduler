package tree;

import java.util.ArrayList;
import java.util.Stack;

import Constraints.HardConstrainsts;
import coursesULabs.Course;
import coursesULabs.Lab;
import coursesULabs.Unit;
import schedule.CourseSlot;
import schedule.LabSlot;
import schedule.Slot;

public class Generator {

    private int bound;

    private TreeNode startNode;
    private TreeNode bestSchedule;
    private int totalSize;

    /**
     * Creates a basic Generator class.
     * 
     * @param starter   The node to start generating from.
     * @param totalSize The total size.
     */
    public Generator(TreeNode starter, int totalSize) {

        this.startNode = starter;
        this.bound = Integer.MAX_VALUE;
        this.bestSchedule = null;
        this.totalSize = totalSize;

    }

    /**
     * Tries to add a child to the tree.
     * 
     * @param current       The unit to try and schedule.
     * @param slot          The slot to try and schedule it in.
     * @param parent        The parent node to try and add the child to.
     * @param currentBound  The current bound of the solution.
     * @param allUnitsTotal The total of all units.
     * @param emptySlots    An ArrayList of empty slots.
     */
    private void checkAndMaybeAddChild(Unit current, Slot slot, TreeNode parent, int currentBound, int allUnitsTotal,
            ArrayList<Slot> emptySlots) {
        TreeNode nodeToAdd = new TreeNode(new Pair(slot, current), 0, parent);
        nodeToAdd.setDepth(parent.getDepth() + 1);

        // Check if we break the hard constraint
        boolean HardConstraintOk;
        if (current instanceof Course) {
            HardConstraintOk = HardConstrainsts.checkAssignmentHardConstriantsCourse((Course) current,
                    (CourseSlot) slot, nodeToAdd);
        } else {
            HardConstraintOk = HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current, (LabSlot) slot,
                    nodeToAdd);
        }
        if (HardConstraintOk) {
            // Calculate the penalty value here
            int calc = Kontrol.evalAssignmentPairing(slot, current, nodeToAdd) + parent.getPenaltyValueOfTreeNode();
            if (calc < currentBound) {
                nodeToAdd.setPenalty(calc);
                parent.addChild(nodeToAdd);

                if (nodeToAdd.getDepth() == totalSize) {
                    // Calculate the penalty for the remaining slots
                    nodeToAdd.setPenalty(
                            nodeToAdd.getPenaltyValueOfTreeNode() + Kontrol.calculateMin(nodeToAdd, slot, current));

                }
            }
        }
    }

    /**
     * Generates children pairs and adds them to the tree.
     * 
     * @param current       The current unit to try and schedule.
     * @param parent        The parent node to add the child node to.
     * @param slotsToPair   An ArrayList of slots to try and pair with the unit.
     * @param currentBound  The current bound of the solution.
     * @param allUnitsTotal The total of all units.
     * @param emptySlots    An ArrayList of empty slots.
     */
    private void generateChildrenPairs(Unit current, TreeNode parent, ArrayList<Slot> slotsToPair, int currentBound,
            int allUnitsTotal, ArrayList<Slot> emptySlots) {
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

    /**
     * Skeleton of a BNB using a stack Assumes we have a bound function called
     * calculateBound which will Using a heuristic, find a solution xh to the
     * optimization problem. Store its value, B = f(xh). (If no heuristic is
     * available, set B to infinity.) B will denote the best solution found so far,
     * and will be used as an upper bound on candidate solutions. Initialize a queue
     * to hold a partial solution with none of the variables of the problem
     * assigned. Loop until the queue is empty: Take a node N off the queue. If N
     * represents a single candidate solution x and f(x) < B, then x is the best
     * solution so far. Record it and set B ← f(x). Else, branch on N to produce new
     * nodes Ni. For each of these: If bound(Ni) > B, do nothing; since the lower
     * bound on this node is greater than the upper bound of the problem, it will
     * never lead to the optimal solution, and can be discarded. Else, store Ni on
     * the queue.
     * 
     * @param starter               The tree node to start BNB at.
     * @param unitsToBeScheduled    An ArrayList of units that need to be scheduled.
     * @param slotToScheduleIn      An ArrayList of slots to schedule the units
     *                              into.
     * @param numPartialAssignments The number of assignments that have already been
     *                              made pre-BNB.
     */
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

        allStackNodes.add(nodeToAdd);

        int count = 0;
        while (!allStackNodes.isEmpty()) {
            count++;
            TreeNode currentNode = allStackNodes.pop();

            if (currentNode.getDepth() == unitsToBeScheduled.size() + Math.max(numPartialAssignments - 1, 0)) {
                // The Schedule Inside Represents a Full Solution
                if (currentNode.getPenaltyValueOfTreeNode() < this.bound) {

                    this.bound = currentNode.getPenaltyValueOfTreeNode();
                    this.bestSchedule = currentNode;
                    System.out.println("So Bound got replaced with" + this.bound);
                    formatOutput(currentNode);
                    System.out.println("Bound was broken after " + count);

                }
            } else {

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
                    }

                    if (scheduleMe.equals(unitsToBeScheduled.get(unitsToBeScheduled.size() - 1))) {
                        // We are reaching the bottom - i.e. the last thing is being scheduled

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
                }
                // System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

                // System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

                // for (int i = 0; i < currentNode.getOrderedChildren().size(); i++) {
                // int x = 0;
                // // allStackNodes.push(currentNode.getOrderedChildren().poll());
                // allStackNodes.push(currentNode.getChildren().get(i));
                // }

                while (!currentNode.getOrderedChildren().isEmpty()) {
                    allStackNodes.push(currentNode.getOrderedChildren().remove());
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
            System.out.println("The total number of nodes checked was " + count);

        }

    }

    /**
     * Does a formatted output of the schedule.
     */
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
    private void formatOutput(TreeNode schedule) {
        TreeNode current = schedule;
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
