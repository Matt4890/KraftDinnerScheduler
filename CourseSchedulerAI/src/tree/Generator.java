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
    private TreeNode startNode;
    private TreeNode bestSchedule;

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
    private void generateChildren(Unit current, TreeNode parent){
        if (current instanceof Course) {

            // Copy the hashMap to Reference
            for (Map.Entry<Integer, Slot> entry : parent.getSchedule().getMWFLec().entrySet()) {
                // Create a copy to manipulate
                HashMap<Integer, Slot> MWFMapToManipulate = DeepCopyCourseSlotMap(
                        parent.getSchedule().getMWFLec());

                // int calc = 0;

                boolean HardConstraintOk = HardConstrainsts
                        .checkAssignmentHardConstriantsCourse((Course) current, (CourseSlot) entry.getValue());
                System.out.println("Assignment " + ((Course) current).toString() + " and Slot "
                        + entry.getValue().toString() + " hard constraint check is: " + HardConstraintOk);
                if (HardConstraintOk ) {

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
                    Schedule newSchedule = new Schedule(parent.getSchedule());

                    // Replace the MWF in the newSchedule with the manipulated one
                    newSchedule.setMWFLec(MWFMapToManipulate);
                    int desire = Kontrol.desireability(entry.getValue(), current);

                    // Create a TreeNode as a child of the last TreeNode
                    TreeNode n = new TreeNode(newSchedule, calc + parent.getPenaltyValueOfTreeNode(),
                            parent, desire); // Note we need to calculate the total penatly differently
                    System.out.println("Schedule in Node: " + n.toString());
                    parent.addChild(n);

                }
            }

            for (Map.Entry<Integer, Slot> entry : parent.getSchedule().getTuThLec().entrySet()) {
                // Create a copy to manipulate
                HashMap<Integer, Slot> TuThMapToManipulate = DeepCopyCourseSlotMap(
                        parent.getSchedule().getTuThLec());
                // int calc = 0;

                boolean HardConstraintOk = HardConstrainsts
                        .checkAssignmentHardConstriantsCourse((Course) current, (CourseSlot) entry.getValue());
                System.out.println("Assignment " + ((Course) current).toString() + " and Slot "
                        + entry.getValue().toString() + " hard constraint check is: " + HardConstraintOk);
                if (HardConstraintOk ) {

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
                    Schedule newSchedule = new Schedule(parent.getSchedule());

                    // Replace the TuTh in the newSchedule with the manipulated one
                    newSchedule.setTuThLec(TuThMapToManipulate);
                    int desire = Kontrol.desireability(entry.getValue(), current);

                    // Create a TreeNode as a child of the last TreeNode
                    TreeNode n = new TreeNode(newSchedule, calc + parent.getPenaltyValueOfTreeNode(),
                            parent, desire); // Note we need to calculate the total penatly differently
                    System.out.println("Schedule in Node: " + n.toString());
                    parent.addChild(n);

                }
            }

        } else {
            for (Map.Entry<Integer, Slot> entry : parent.getSchedule().getMWLab().entrySet()) {
                // Create a copy to manipulate
                HashMap<Integer, Slot> MWLabMapToManipulate = DeepCopyLabSlotMap(
                        parent.getSchedule().getMWLab());

                boolean HardConstraintOk = HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current,
                        (LabSlot) entry.getValue(), parent.getSchedule().getMWFLec(),
                        parent.getSchedule().getTuThLec(), parent.getSchedule().getMWLab(),
                        parent.getSchedule().getTuThLab(), parent.getSchedule().getFLab());
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
                    Schedule newSchedule = new Schedule(parent.getSchedule());

                    // Replace the TuTh in the newSchedule with the manipulated one
                    newSchedule.setMWLab(MWLabMapToManipulate);

                    int desire = Kontrol.desireability(entry.getValue(), current);

                    // Create a TreeNode as a child of the last TreeNode
                    TreeNode n = new TreeNode(newSchedule, calc + parent.getPenaltyValueOfTreeNode(),
                            parent, desire); // Note we need to calculate the total penatly differently
                    System.out.println("Schedule in Node: " + n.toString());
                    parent.addChild(n);

                }
            }
            for (Map.Entry<Integer, Slot> entry : parent.getSchedule().getTuThLab().entrySet()) {
                // Create a copy to manipulate
                HashMap<Integer, Slot> TuThLabMapToManipulate = DeepCopyLabSlotMap(
                        parent.getSchedule().getTuThLab());

                // int calc = 0;

                boolean HardConstraintOk = HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current,
                        (LabSlot) entry.getValue(), parent.getSchedule().getMWFLec(),
                        parent.getSchedule().getTuThLec(), parent.getSchedule().getMWLab(),
                        parent.getSchedule().getTuThLab(), parent.getSchedule().getFLab());

                System.out.println("Assignment " + ((Lab) current).toString() + " and Slot "
                        + entry.getValue().toString() + " hard constraint check is: " + HardConstraintOk);
                if (HardConstraintOk ) {
                    
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
                    Schedule newSchedule = new Schedule(parent.getSchedule());

                    // Replace the TuTh in the newSchedule with the manipulated one
                    newSchedule.setTuThLab(TuThLabMapToManipulate);

                    int desire = Kontrol.desireability(entry.getValue(), current);

                    // Create a TreeNode as a child of the last TreeNode
                    TreeNode n = new TreeNode(newSchedule, calc + parent.getPenaltyValueOfTreeNode(),
                            parent, desire); // Note we need to calculate the total penatly differently
                    System.out.println("Schedule in Node: " + n.toString());
                    parent.addChild(n);

                }
            }
            for (Map.Entry<Integer, Slot> entry : parent.getSchedule().getFLab().entrySet()) {
                // Create a copy to manipulate
                HashMap<Integer, Slot> FLabMapToManipulate = DeepCopyLabSlotMap(
                        parent.getSchedule().getFLab());
                // Calculate the penalty of the course slot pairing

                boolean HardConstraintOk = HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current,
                        (LabSlot) entry.getValue(), parent.getSchedule().getMWFLec(),
                        parent.getSchedule().getTuThLec(), parent.getSchedule().getMWLab(),
                        parent.getSchedule().getTuThLab(), parent.getSchedule().getFLab());
                System.out.println("Assignment " + ((Lab) current).toString() + " and Slot "
                        + entry.getValue().toString() + " hard constraint check is: " + HardConstraintOk);
                if (HardConstraintOk ) {


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
                    Schedule newSchedule = new Schedule(parent.getSchedule());

                    // Replace the TuTh in the newSchedule with the manipulated one
                    newSchedule.setFLab(FLabMapToManipulate);
                    int desire = Kontrol.desireability(entry.getValue(), current);

                    // Create a TreeNode as a child of the last TreeNode
                    TreeNode n = new TreeNode(newSchedule, calc + parent.getPenaltyValueOfTreeNode(),
                            parent, desire); // Note we need to calculate the total penatly differently
                    System.out.println("Schedule in Node: " + n.toString());
                    parent.addChild(n);

                }
            }
        }
    }



    public void generateFBoundBIG(ArrayList<Unit> toBeAdded) {
        TreeNode lastTreeNode = new TreeNode(this.starter, this.initialPenalty);
        this.startNode = lastTreeNode;

        for (int i = 0; i < toBeAdded.size(); i++) {
            if (!lastTreeNode.getOrderedChildren().isEmpty()) {
                lastTreeNode = lastTreeNode.getLowestPenaltyChild();
                System.out.println("\n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n");

            } else {

                // for (int i = toBeAdded.size() -1; i >=0; i--) {
                Unit current = toBeAdded.get(i);
                // Run through each possible course slot pair and create children
                generateChildren( current, lastTreeNode);

                System.out.println(lastTreeNode.getChildren().size());

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

        }
        // Now we get the penaltyValue of lastTreeNode and make that the bound
        this.bound = lastTreeNode.getPenaltyValueOfTreeNode();
        this.bestSchedule = lastTreeNode;

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

    //Skeleton of a BNB using a stack 
    //Assumes we have a bound function called calculateBound which will 
//     Using a heuristic, find a solution xh to the optimization problem. Store its value, B = f(xh). (If no heuristic is available, set B to infinity.) B will denote the best solution found so far, and will be used as an upper bound on candidate solutions.
// Initialize a queue to hold a partial solution with none of the variables of the problem assigned.
// Loop until the queue is empty:
// Take a node N off the queue.
// If N represents a single candidate solution x and f(x) < B, then x is the best solution so far. Record it and set B ← f(x).
// Else, branch on N to produce new nodes Ni. For each of these:
// If bound(Ni) > B, do nothing; since the lower bound on this node is greater than the upper bound of the problem, it will never lead to the optimal solution, and can be discarded.
// Else, store Ni on the queue.
    public void branchAndBoundSkeleton(ArrayList<Unit> unitsToBeScheduled){
        Stack<TreeNode> allStackNodes = new Stack<TreeNode>();
        allStackNodes.add(this.startNode);
        while(!allStackNodes.isEmpty()){

            TreeNode currentNode = allStackNodes.pop();

            if (unitsToBeScheduled.isEmpty()){ //TheScheduleInsideRepresents a Full Solution 
                if (currentNode.getPenaltyValueOfTreeNode() < this.bound){
                    this.bound = currentNode.getPenaltyValueOfTreeNode();
                    this.bestSchedule = currentNode;
                }

            } else {
                //Check to see if doesn't have children made 
                if (currentNode.getChildren().size() == 0){
                    generateChildren(unitsToBeScheduled.remove(0), currentNode);
                
                }
                //Go through all of the children and add them to the stack if the eval of the 
                for (TreeNode node : currentNode.getChildren() ){
                    if (node.getPenaltyValueOfTreeNode() + node.getPotential() < bound){
                        //Add it to the stack
                        allStackNodes.add(node);
                    }
                }

            }

        }



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
