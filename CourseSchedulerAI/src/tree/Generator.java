package tree;

import Constraints.*;
import schedule.*;
import coursesULabs.*;
import java.util.*;

public class Generator {

    private TreeNode starter;

    private int bound;
    private int weightMin;
    private int weightPairs;
    private int weightBrothersSectionDiff;
    private TreeNode startNode;
    private TreeNode bestSchedule;

    public Generator(TreeNode starter, int weight_min, int weight_pairs, int wait_section_diff) {

        this.starter = starter;

        this.bound = Integer.MAX_VALUE;
        this.weightMin = weight_min;
        this.weightPairs = weight_pairs;
        this.weightBrothersSectionDiff = wait_section_diff;
        this.bestSchedule = null;

    }

    private void checkAndMaybeAddChild(Unit current, Slot slot, TreeNode parent, int currentBound, int allUnitsTotal) {
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
                if (nodeToAdd.getDepth() == allUnitsTotal - 1) {
                    // Calculate the penalty for the remaining slots
                    // Create a helper method in Generator to calculate all empty slot coursemin and
                    // preference s
                    nodeToAdd.addToPenaltyForBaseNode(baseOfTreePenaltyCalculation(nodeToAdd)); // THIS IS GOING TO HAVE TO CHANGE

                }
            }
        }
    }

    private void generateChildrenPairs(Unit current, TreeNode parent, ArrayList<Slot> slotsToPair, int currentBound, int allUnitsTotal) {
        for (Slot slot : slotsToPair) {
            if (current instanceof Course) {
                if (slot instanceof CourseSlot) {
                    checkAndMaybeAddChild(current, slot, parent, currentBound, allUnitsTotal);
                }

            } else {
                if (slot instanceof LabSlot) {
                    checkAndMaybeAddChild(current, slot, parent, currentBound, allUnitsTotal);
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
    public void branchAndBoundSkeleton(TreeNode starter, ArrayList<Unit> unitsToBeScheduled, ArrayList<Slot> slotToScheduleIn, int numPartialAssignments) {
        System.out.println("RUNNING BNB");
        Stack<TreeNode> allStackNodes = new Stack<TreeNode>();
        this.startNode = starter;
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
                Unit scheduleMe = unitsToBeScheduled.get(currentNode.getDepth() - numPartialAssignments ); // This will break right now NEEDS TESTING

                if (currentNode.getChildren().size() == 0) {

                    generateChildrenPairs(scheduleMe, currentNode, slotToScheduleIn, this.bound, unitsToBeScheduled.size());
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