package tree;

import Constraints.*;
import schedule.*;
import coursesULabs.*;
import java.util.*;

public class Generator {

    public int createFBound(Schedule startingScheduleFromParser, int initialPenaltyValueFromParser,
            ArrayList<Unit> toBeAdded) {
        Tree tree = new Tree();
        tree.addRoot(new Node(startingScheduleFromParser, initialPenaltyValueFromParser));
        Node lastNode = tree.getRoot();
        for (int i = 0; i < toBeAdded.size(); i++) {
            // Check what type of unit it is and then create the nodes accordingly
            Unit current = toBeAdded.get(i);
            if (current instanceof Course) {
                int minimum = Integer.MAX_VALUE;
                int idx = Integer.MAX_VALUE;
                int day = -1;
                for (Map.Entry<Integer, Slot> entry : startingScheduleFromParser.getMWFLec().entrySet()) {
                    // Calculate the penalty of the course slot pairing
                    int calc = SoftConstraints.calculatePenalty(entry.getValue(), current);
                    if (calc < minimum && HardConstrainsts.checkAssignmentHardConstriantsCourse((Course) current,
                            (CourseSlot) entry.getValue(), startingScheduleFromParser.getMWFLec(),
                            startingScheduleFromParser.getTuThLec(), startingScheduleFromParser.getMWLab(),
                            startingScheduleFromParser.getTuThLab(), startingScheduleFromParser.getFLab())) {
                        // Run a hard constraint check

                        calc = minimum;
                        idx = entry.getKey();
                        day = 0;
                    }
                }
                for (Map.Entry<Integer, Slot> entry : startingScheduleFromParser.getTuThLec().entrySet()) {
                    int calc = SoftConstraints.calculatePenalty(entry.getValue(), current);
                    // Check if we pass the hard constraints and its a new minimal
                    if (calc < minimum && HardConstrainsts.checkAssignmentHardConstriantsCourse((Course) current,
                            (CourseSlot) entry.getValue(), startingScheduleFromParser.getMWFLec(),
                            startingScheduleFromParser.getTuThLec(), startingScheduleFromParser.getMWLab(),
                            startingScheduleFromParser.getTuThLab(), startingScheduleFromParser.getFLab())) {
                        calc = minimum;
                        idx = entry.getKey();
                        day = 1;
                    }
                }
                // Assign the course to the slot
                if (day == 0) { // MondayWednesdayFriday
                    startingScheduleFromParser.getMWFLec().get(idx).assignUnitToSlot(current);
                } else { // TuesdayThursday
                    startingScheduleFromParser.getTuThLec().get(idx).assignUnitToSlot(current);
                }
                // Not Sure if this should be a deep copy or a shallow reference is okay.
                Node newCreatedNode =new Node(startingScheduleFromParser, minimum + lastNode.getPenaltyValueOfNode(), lastNode);
                lastNode.addChild(newCreatedNode);
                lastNode = newCreatedNode;

            } else {
                int minimum = Integer.MAX_VALUE;
                int idx = Integer.MAX_VALUE;
                int day = -1;
                // Check all the lab slot assignments that would be best
                for (Map.Entry<Integer, Slot> entry : startingScheduleFromParser.getMWLab().entrySet()) {
                    int calc = SoftConstraints.calculatePenalty(entry.getValue(), current);
                    if (calc < minimum && HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current,
                            (LabSlot) entry.getValue(), startingScheduleFromParser.getMWFLec(),
                            startingScheduleFromParser.getTuThLec(), startingScheduleFromParser.getMWLab(),
                            startingScheduleFromParser.getTuThLab(), startingScheduleFromParser.getFLab())) {

                        calc = minimum;
                        idx = entry.getKey();
                        day = 0;
                    }

                }
                for (Map.Entry<Integer, Slot> entry : startingScheduleFromParser.getTuThLab().entrySet()) {
                    int calc = SoftConstraints.calculatePenalty(entry.getValue(), current);
                    if (calc < minimum && HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current,
                            (LabSlot) entry.getValue(), startingScheduleFromParser.getMWFLec(),
                            startingScheduleFromParser.getTuThLec(), startingScheduleFromParser.getMWLab(),
                            startingScheduleFromParser.getTuThLab(), startingScheduleFromParser.getFLab())) {

                        calc = minimum;
                        idx = entry.getKey();
                        day = 1;
                    }

                }
                for (Map.Entry<Integer, Slot> entry : startingScheduleFromParser.getFLab().entrySet()) {
                    int calc = SoftConstraints.calculatePenalty(entry.getValue(), current);
                    if (calc < minimum && HardConstrainsts.checkAssignmentHardConstriantsLab((Lab) current,
                            (LabSlot) entry.getValue(), startingScheduleFromParser.getMWFLec(),
                            startingScheduleFromParser.getTuThLec(), startingScheduleFromParser.getMWLab(),
                            startingScheduleFromParser.getTuThLab(), startingScheduleFromParser.getFLab())) {

                        calc = minimum;
                        idx = entry.getKey();
                        day = 1;
                    }
                }
                if (day == 0) { // MondayWednesday
                    startingScheduleFromParser.getMWLab().get(idx).assignUnitToSlot(current);
                } else if (day == 1) { // TuesdayThursday
                    startingScheduleFromParser.getTuThLab().get(idx).assignUnitToSlot(current);
                } else { //Friday
                    startingScheduleFromParser.getFLab().get(idx).assignUnitToSlot(current);
                }
                Node newCreatedNode =new Node(startingScheduleFromParser, minimum + lastNode.getPenaltyValueOfNode(), lastNode);
                lastNode.addChild(newCreatedNode);
                lastNode = newCreatedNode;

            }
        }
        return 0;
    }
    
}