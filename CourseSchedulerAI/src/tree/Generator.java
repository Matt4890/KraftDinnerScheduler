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
        this.tree.addRoot(new Node(this.starter, this.initialPenalty));
        this.starter = starter;
        this.initialPenalty = penalty;
        this.bound = Integer.MAX_VALUE;

    }
    private void addPotentialsCourseSlot (Node lastNode, Unit current, HashMap<Integer, Slot> slotsToAddFrom){
        for (Map.Entry<Integer, Slot> entry : slotsToAddFrom.entrySet()) {
                    // Calculate the penalty of the course slot pairing
                    int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, 0, 0, 0);
                    if (HardConstrainsts.checkAssignmentHardConstriantsCourse((Course) current,
                            (CourseSlot) entry.getValue(), this.starter.getMWFLec(), this.starter.getTuThLec(),
                            this.starter.getMWLab(), this.starter.getTuThLab(), this.starter.getFLab())) {
                        // Add the course to the slot in the schedule
                        entry.getValue().addOccupant(current);
                        // Create a node as a child of the last node
                        Node n = new Node(this.starter, calc + lastNode.getPenaltyValueOfNode(), lastNode);
                        lastNode.addChild(n);
                    }
                }

    }
    private void addPotentialsLabSlot(Node lastNode, Unit current, HashMap<Integer, Slot> slotsToAddFrom) {
        for (Map.Entry<Integer, Slot> entry : slotsToAddFrom.entrySet()) {
            // Calculate the penalty of the lab slot pairing
            int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, 0, 0, 0);
            if (HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current,
                    (LabSlot) entry.getValue(), this.starter.getMWFLec(), this.starter.getTuThLec(),
                    this.starter.getMWLab(), this.starter.getTuThLab(), this.starter.getFLab())) {
                // Add the lab to the slot in the schedule
                entry.getValue().addOccupant(current);
                // Create a node as a child of the last node
                Node n = new Node(this.starter, calc + lastNode.getPenaltyValueOfNode(), lastNode);
                lastNode.addChild(n);
            }
        }
    }
    public void generateFBound(ArrayList<Unit> toBeAdded) {
        Node lastNode = this.tree.getRoot();
        for (int i = 0; i < toBeAdded.size(); i++) {
            Unit current = toBeAdded.get(i);
            // Run through each possible course slot pair and create children
            if (current instanceof Course) {
                this.addPotentialsCourseSlot(lastNode, current, this.starter.getMWFLec());
                this.addPotentialsCourseSlot(lastNode, current, this.starter.getTuThLec());
            } else {
                this.addPotentialsLabSlot(lastNode, current, this.starter.getMWLab());
                this.addPotentialsLabSlot(lastNode, current, this.starter.getTuThLab());
                this.addPotentialsLabSlot(lastNode, current, this.starter.getFLab());
                
            }
            //At this point we should have all the potential Pairs stored in lastNode's children now we need to select one using the control
            //Pick the best node - i.e. top of the heap and make it the lastNode to expand out
            lastNode = lastNode.getLowestPenaltyChild();
        }
        //Now we get the penaltyValue of lastNode and make that the bound 
        this.bound = lastNode.getPenaltyValueOfNode();

        System.out.println("Bound Generated: " + this.bound);
        //We can make this better once the KONTROL is implemented We can modify this so that only the nodes that 
        // are added are the ones that meet the desirablility criteria
        //This control would have to be seperate for the branch and bound control otherwise we are likely to pick the same thing


    }
    

    // public void createFBound(ArrayList<Unit> toBeAdded) {

    //     this.tree.addRoot(new Node(this.starter, this.initialPenalty));
    //     Node lastNode = this.tree.getRoot();

    //     for (int i = 0; i < toBeAdded.size(); i++) {
    //         // Check what type of unit it is and then create the nodes accordingly
    //         Unit current = toBeAdded.get(i);
    //         int minimum = Integer.MAX_VALUE;
    //         if (current instanceof Course) {

    //             int idx = Integer.MAX_VALUE;
    //             int day = -1;
    //             for (Map.Entry<Integer, Slot> entry : this.starter.getMWFLec().entrySet()) {
    //                 // Calculate the penalty of the course slot pairing
    //                 int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, 0, 0, 0);
    //                 if (HardConstrainsts.checkAssignmentHardConstriantsCourse((Course) current,
    //                         (CourseSlot) entry.getValue(), this.starter.getMWFLec(), this.starter.getTuThLec(),
    //                         this.starter.getMWLab(), this.starter.getTuThLab(), this.starter.getFLab())) {
    //                     // Run a hard constraint check

    //                     calc = minimum;
    //                     idx = entry.getKey();
    //                     day = 0;
    //                 }
    //             }
    //             for (Map.Entry<Integer, Slot> entry : this.starter.getTuThLec().entrySet()) {
    //                 int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, 0, 0, 0);
    //                 // Check if we pass the hard constraints and its a new minimal
    //                 if (calc < minimum && HardConstrainsts.checkAssignmentHardConstriantsCourse((Course) current,
    //                         (CourseSlot) entry.getValue(), this.starter.getMWFLec(), this.starter.getTuThLec(),
    //                         this.starter.getMWLab(), this.starter.getTuThLab(), this.starter.getFLab())) {
    //                     calc = minimum;
    //                     idx = entry.getKey();
    //                     day = 1;
    //                 }
    //             }
    //             // Assign the course to the slot
    //             if (day == 0) { // MondayWednesdayFriday
    //                 this.starter.getMWFLec().get(idx).assignUnitToSlot(current);
    //             } else { // TuesdayThursday
    //                 this.starter.getTuThLec().get(idx).assignUnitToSlot(current);
    //             }
    //             // Not Sure if this should be a deep copy or a shallow reference is okay.
    //             Node newCreatedNode = new Node(this.starter, minimum + lastNode.getPenaltyValueOfNode(), lastNode);
    //             lastNode.addChild(newCreatedNode);
    //             lastNode = newCreatedNode;

    //         } else {
    //             int idx = Integer.MAX_VALUE;
    //             int day = -1;
    //             // Check all the lab slot assignments that would be best
    //             for (Map.Entry<Integer, Slot> entry : this.starter.getMWLab().entrySet()) {
    //                 int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, 0, 0, 0);
    //                 if (calc < minimum && HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current,
    //                         (LabSlot) entry.getValue(), this.starter.getMWFLec(), this.starter.getTuThLec(),
    //                         this.starter.getMWLab(), this.starter.getTuThLab(), this.starter.getFLab())) {

    //                     calc = minimum;
    //                     idx = entry.getKey();
    //                     day = 0;
    //                 }

    //             }
    //             for (Map.Entry<Integer, Slot> entry : this.starter.getTuThLab().entrySet()) {
    //                 int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, 0, 0, 0);
    //                 if (calc < minimum && HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current,
    //                         (LabSlot) entry.getValue(), this.starter.getMWFLec(), this.starter.getTuThLec(),
    //                         this.starter.getMWLab(), this.starter.getTuThLab(), this.starter.getFLab())) {

    //                     calc = minimum;
    //                     idx = entry.getKey();
    //                     day = 1;
    //                 }

    //             }
    //             for (Map.Entry<Integer, Slot> entry : this.starter.getFLab().entrySet()) {
    //                 int calc = SoftConstraints.calculatePenalty(entry.getValue(), current, 0, 0, 0);
    //                 if (calc < minimum && HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current,
    //                         (LabSlot) entry.getValue(), this.starter.getMWFLec(), this.starter.getTuThLec(),
    //                         this.starter.getMWLab(), this.starter.getTuThLab(), this.starter.getFLab())) {

    //                     calc = minimum;
    //                     idx = entry.getKey();
    //                     day = 1;
    //                 }
    //             }
    //             if (day == 0) { // MondayWednesday
    //                 this.starter.getMWLab().get(idx).assignUnitToSlot(current);
    //             } else if (day == 1) { // TuesdayThursday
    //                 this.starter.getTuThLab().get(idx).assignUnitToSlot(current);
    //             } else { // Friday
    //                 this.starter.getFLab().get(idx).assignUnitToSlot(current);
    //             }
    //             Node newCreatedNode = new Node(this.starter, minimum + lastNode.getPenaltyValueOfNode(), lastNode);
    //             lastNode.addChild(newCreatedNode);
    //             lastNode = newCreatedNode;

    //         }
    //         this.bound += minimum;
    //         System.out.println(bound);
    //     }
    // }

    // public Node branchAndBound() {
    //     // TODO: Implement branch and Bound using a control to evaluate

    //     // Basic methodology same as the dfs except we store all of them as children and
    //     // make a choice based on the KONTROL
    //     // the Bound basically acts as another hard constraint?
    //     // or no that would just be brute force
    //     // we have to find the

    //     return this.tree.getRoot();

    // }
}