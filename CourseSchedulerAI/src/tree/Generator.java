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
    private int weightMin;
    private int weightPairs;
    private int weightBrothersSectionDiff;
    private TreeNode startNode;
    private TreeNode bestSchedule;

    public Generator(Schedule starter, int penalty, int weight_min, int weight_pairs, int wait_section_diff) {
        this.tree = new Tree();
        // this.tree.addRoot(new TreeNode(this.starter, this.initialPenalty));
        this.starter = starter;
        this.initialPenalty = penalty;
        this.bound = Integer.MAX_VALUE;
        this.weightMin = weight_min;
        this.weightPairs = weight_pairs;
        this.weightBrothersSectionDiff = wait_section_diff;

    }

    private void generateChildren(Unit current, TreeNode parent, int allUnitsTotal) {
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
                // System.out.println("Assignment " + ((Course) current).toString() + " and Slot "
                        // + entry.getValue().toString() + " hard constraint check is: " + HardConstraintOk);
                if (HardConstraintOk) {

                    // Calculate the penalty of the course slot pairing
                    int calc = Kontrol.evalAssignment(entry.getValue(), current);
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
                    if(n.getDepth() == allUnitsTotal -1){
                        //Calculate the penalty for the remaining slots
                        //Create a helper method in Generator to calculate all empty slot coursemin and preference s
                        n.addToPenaltyForBaseNode(baseOfTreePenaltyCalculation(n));

                    }
                    // System.out.println("Schedule in Node: " + n.toString());
                    parent.addChild(n);
                    updatePotentialCourse((CourseSlot) MWFMapToManipulate.get(entry.getKey()), current, n);
                    // System.out.println(potentialLost + " <------------------ potential");
                    n.incrementPotential(potentialLost + parent.getPotential());
                    // System.out.println("the starting potential of PARENT tree node is " + parent.getPotential());
                    // System.out.println("the starting potential of child tree node is " + n.getPotential());
                }
            }
            for (Map.Entry<Integer, Slot> entry : parent.getSchedule().getTuThLec().entrySet()) {
                // Create a copy to manipulate
                HashMap<Integer, Slot> TuThMapToManipulate = DeepCopyCourseSlotMap(parent.getSchedule().getTuThLec());
                // int calc = 0;

                boolean HardConstraintOk = HardConstrainsts.checkAssignmentHardConstriantsCourse((Course) current,
                        (CourseSlot) entry.getValue());
                // System.out.println("Assignment " + ((Course) current).toString() + " and Slot "
                        // + entry.getValue().toString() + " hard constraint check is: " + HardConstraintOk);
                if (HardConstraintOk) {

                    // Calculate the penalty of the course slot pairing
                    int calc = Kontrol.evalAssignment(entry.getValue(), current);
                    System.out.println("Penalty of Pairing: " + calc);
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
                    if(n.getDepth() == allUnitsTotal -1){
                        //Calculate the penalty for the remaining slots
                        //Create a helper method in Generator to calculate all empty slot coursemin and preference s
                        n.addToPenaltyForBaseNode(baseOfTreePenaltyCalculation(n));

                    }
                    
                    // System.out.println("Schedule in Node: " + n.toString());
                    parent.addChild(n);
                    updatePotentialCourse((CourseSlot) TuThMapToManipulate.get(entry.getKey()), current, n);
                    // System.out.println(potentialLost + " <------------------ potential");
                    n.incrementPotential(potentialLost + parent.getPotential());
                    // System.out.println("the starting potential of PARENT tree node is " + parent.getPotential());
                    // System.out.println("the starting potential of child tree node is " + n.getPotential());

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
                        // + entry.getValue().toString() + " hard constraint check is: " + HardConstraintOk);
                if (HardConstraintOk) {

                    // Calculate the penalty of the course slot pairing
                    int calc = Kontrol.evalAssignment(entry.getValue(), current);
                    System.out.println("Penalty of Pairing: " + calc);

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
                    if(n.getDepth() == allUnitsTotal -1){
                        //Calculate the penalty for the remaining slots
                        //Create a helper method in Generator to calculate all empty slot coursemin and preference s
                        n.addToPenaltyForBaseNode(baseOfTreePenaltyCalculation(n));

                    }
                    //System.out.println("Schedule in Node: " + n.toString());
                    parent.addChild(n);
                    updatePotentialLab((LabSlot) MWLabMapToManipulate.get(entry.getKey()), current, n);
                    //System.out.println(potentialLost + " <------------------ potential");
                    n.incrementPotential(potentialLost + parent.getPotential());
                   // System.out.println("the starting potential of PARENT tree node is " + parent.getPotential());
                   // System.out.println("the starting potential of child tree node is " + n.getPotential());

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

                //System.out.println("Assignment " + ((Lab) current).toString() + " and Slot "
                 //       + entry.getValue().toString() + " hard constraint check is: " + HardConstraintOk);
                if (HardConstraintOk) {

                    // Calculate the penalty of the course slot pairing
                    int calc = Kontrol.evalAssignment(entry.getValue(), current);
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
                    if(n.getDepth() == allUnitsTotal -1){
                        //Calculate the penalty for the remaining slots
                        //Create a helper method in Generator to calculate all empty slot coursemin and preference s
                        n.addToPenaltyForBaseNode(baseOfTreePenaltyCalculation(n));

                    }
                    // System.out.println("Schedule in Node: " + n.toString());
                    parent.addChild(n);
                    updatePotentialLab((LabSlot) TuThLabMapToManipulate.get(entry.getKey()), current, n);
                    // System.out.println(n.getPotential() + " <------------------ potential of n");
                    // System.out.println(potentialLost + " <------------------ potential");
                    n.incrementPotential(potentialLost + parent.getPotential());
                    // System.out.println("the starting potential of PARENT tree node is " + parent.getPotential());
                    // System.out.println("the starting potential of child tree node is " + n.getPotential());

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
                        // + entry.getValue().toString() + " hard constraint check is: " + HardConstraintOk);
                if (HardConstraintOk) {

                    // Calculate the penalty of the course slot pairing
                    int calc = Kontrol.evalAssignment(entry.getValue(), current);
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
                    if(n.getDepth() == allUnitsTotal -1){
                        //Calculate the penalty for the remaining slots
                        //Create a helper method in Generator to calculate all empty slot coursemin and preference s
                        n.addToPenaltyForBaseNode(baseOfTreePenaltyCalculation(n));

                    }
                    // System.out.println("Schedule in Node: " + n.toString());
                    parent.addChild(n);
                    updatePotentialLab((LabSlot) FLabMapToManipulate.get(entry.getKey()), current, n);
                    // System.out.println(n.getPotential() + " <------------------ potential of n");
                    // System.out.println(potentialLost + " <------------------ potential");
                    n.incrementPotential(potentialLost + parent.getPotential());
                    // System.out.println("the starting potential of PARENT tree node is " + parent.getPotential());
                    // System.out.println("the starting potential of child tree node is " + n.getPotential());

                }
            }
        }
    }
    private int baseOfTreePenaltyCalculation(TreeNode baseNode){
        Schedule toCheck = baseNode.getSchedule();
        int evalToAdd = 0;
        calculatePenaltyForEmptySlotsCourses(toCheck.getMWFLec());
        calculatePenaltyForEmptySlotsCourses(toCheck.getTuThLec());
        calculatePenaltyForEmptySlotsLabs(toCheck.getMWLab());
        calculatePenaltyForEmptySlotsLabs(toCheck.getTuThLab());
        calculatePenaltyForEmptySlotsLabs(toCheck.getFLab());

        return evalToAdd;
        
    }
    private int calculatePenaltyForEmptySlotsCourses(HashMap<Integer, Slot> checkMe){
        int evalToAdd = 0;
        for (Map.Entry<Integer, Slot> entry : checkMe.entrySet()) {
            if (entry.getValue().getClassAssignment().size() == 0){
                //CourseMin Check 
                 if (((CourseSlot)entry.getValue()).getCourseMin() != 0){
                    evalToAdd += (1 * Kontrol.getWeight_min_filled());
                }
                //Preferences
                if(entry.getValue().getPreference().size() !=0){
                    for (Map.Entry<Unit, Integer> entry1 : entry.getValue().getPreference().entrySet()){
                        evalToAdd += entry1.getValue() * Kontrol.getWeight_pref();
                    }
                }
            }
        }
        return evalToAdd;
    }
    private int calculatePenaltyForEmptySlotsLabs(HashMap<Integer, Slot> checkMe){
        int evalToAdd = 0;
        for (Map.Entry<Integer, Slot> entry : checkMe.entrySet()) {
            if (entry.getValue().getClassAssignment().size() == 0){
                //CourseMin Check 
                 if (((LabSlot)entry.getValue()).getLabMin() != 0){
                    evalToAdd += (1 * Kontrol.getWeight_min_filled());
                }
                //Preferences
                if(entry.getValue().getPreference().size() !=0){
                    for (Map.Entry<Unit, Integer> entry1 : entry.getValue().getPreference().entrySet()){
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
        this.bound = Integer.MAX_VALUE;
        int numUnitsToSchedule = unitsToBeScheduled.size();
        int depth = 0;

        System.out.println(allStackNodes);
        while (!allStackNodes.isEmpty()) {
            System.out.println("While looooop ran");
            System.out.println(allStackNodes);
            TreeNode currentNode = allStackNodes.pop();

            if (currentNode.getDepth() == unitsToBeScheduled.size()-1) { // TheScheduleInsideRepresents a Full Solution
                System.out.println(
                        "WE GOT TO THE BOTTOM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("Bound is: " + currentNode.getPenaltyValueOfTreeNode());
                if (currentNode.getPenaltyValueOfTreeNode() < this.bound) {
                    System.out.println("So Bound got replaced");
                    this.bound = currentNode.getPenaltyValueOfTreeNode();
                    this.bestSchedule = currentNode;

                }

            } else {
                // Check to see if doesn't have children made
                Unit scheduleMe = unitsToBeScheduled.get(currentNode.getDepth());
                if (currentNode.getChildren().size() == 0) {
                    generateChildren(scheduleMe, currentNode,unitsToBeScheduled.size());
                    depth++;
                }
                // Go through all of the children and add them to the stack if the eval of the
                // We can change this to the priority queue and order the children by their
                for (TreeNode node : currentNode.getChildren()) {
                    System.out
                            .println("The value we get is: " + node.getPenaltyValueOfTreeNode());
                    if (node.getPenaltyValueOfTreeNode() < bound) {
                        // Add it to the stack
                        allStackNodes.add(node);
                    } else {
                        //Debug
                        System.out.println("-------------------------It Broke the bound so I didn't add it --------------------------------");
                    }
                }

            }

        }
        System.out.println("We are out of things to add...");
        System.out.println("The Best:");
        System.out.println(this.bestSchedule.toString());
        System.out.println(this.bestSchedule.getPenaltyValueOfTreeNode());

    }

    public TreeNode branchAndBound(TreeNode sol, int penalty, int depthCondition, ArrayList<Unit> ListToBeAdded) {

        TreeNode foundBetterSol = sol;
        TreeNode prevNode = sol;
        int currentBestValue = penalty;
        for (int i = ListToBeAdded.size() - 2; i >= depthCondition; i--) {
            prevNode.setAlreadyLookedAt(true);
            prevNode = prevNode.getParent();
            TreeNode betterSolNode = betterSol(prevNode, currentBestValue, i, ListToBeAdded);

            if (betterSolNode != null) {
                if (betterSolNode.getPenaltyValueOfTreeNode() < foundBetterSol.getPenaltyValueOfTreeNode()) {
                    foundBetterSol = betterSolNode;
                    currentBestValue = betterSolNode.getPenaltyValueOfTreeNode();
                }
            }
        }

        if (foundBetterSol.getPenaltyValueOfTreeNode() >= sol.getPenaltyValueOfTreeNode()) {
            return null;
        }
        return foundBetterSol;
    }

    private TreeNode betterSol(TreeNode n, int penaltyValue, int currentDepth, ArrayList<Unit> ListToBeAdded) {
        // TODO still need a control just spelt with a k instead of a c

        ArrayList<TreeNode> childrenList = n.getChildren();
        // if child has no children
        // - check if has no more children
        // - if it has more children generate them
        if (childrenList.isEmpty()) {
            if (currentDepth == ListToBeAdded.size() - 1) {
                n.setAlreadyLookedAt(true);
                n.setgetBestBottomTN(n);
                return n;
            } else if (currentDepth < ListToBeAdded.size() - 1) {
                // TODO this node has children that need to be generated since it's not the very
                // last node
            }

        }

        // keep track of all the nodes child nodes
        ArrayList<TreeNode> keepTrack = new ArrayList<TreeNode>();
        for (int i = 0; i < childrenList.size(); i++) {
            TreeNode considered = childrenList.get(i);
            if (considered.getPenaltyValueOfTreeNode() > penaltyValue) {
                considered.setAlreadyLookedAt(true);
            } else {
                if (!considered.getAlreadyLookedAt()) {
                    TreeNode temp = betterSol(considered, penaltyValue, currentDepth + 1, ListToBeAdded);
                    if (temp != null) {
                        keepTrack.add(temp.getBestBottomTN());
                    }
                }
            }
        }
        n.setAlreadyLookedAt(true);

        // find the best child node here
        int bestPen = Integer.MAX_VALUE;
        TreeNode bestNode = null;
        for (TreeNode tn : keepTrack) {
            if (tn != null && tn.getPenaltyValueOfTreeNode() < bestPen) {
                bestNode = tn;
                bestPen = tn.getPenaltyValueOfTreeNode();
            }
        }

        // return the best node if it is not null otherwise return null
        if (bestNode != null && bestNode.getPenaltyValueOfTreeNode() < penaltyValue) {
            n.setgetBestBottomTN(bestNode);
            return bestNode;
        }
        return null;
    }
}
