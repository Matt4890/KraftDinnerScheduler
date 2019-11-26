package schedule;

import java.util.*;
import coursesULabs.*;
import enums.*;

public class Schedule {

    private HashMap<Integer, Slot> MWFLec = new HashMap<Integer, Slot>();
    private HashMap<Integer, Slot> TuThLec = new HashMap<Integer, Slot>();
    private HashMap<Integer, Slot> MWLab = new HashMap<Integer, Slot>();
    private HashMap<Integer, Slot> TuThLab = new HashMap<Integer, Slot>();
    private HashMap<Integer, Slot> FLab = new HashMap<Integer, Slot>();

    public Schedule(Schedule s) {
        // Do a Deep Copy of each hash map
        for (Map.Entry<Integer, Slot> entry : s.MWFLec.entrySet()) {

            this.MWFLec.put(entry.getKey(), new CourseSlot((CourseSlot) entry.getValue()));
        }
        for (Map.Entry<Integer, Slot> entry : s.TuThLec.entrySet()) {

            this.TuThLec.put(entry.getKey(), new CourseSlot((CourseSlot) entry.getValue()));

        }
        for (Map.Entry<Integer, Slot> entry : s.MWLab.entrySet()) {
            this.MWLab.put(entry.getKey(), new LabSlot((LabSlot) entry.getValue()));
        }
        for (Map.Entry<Integer, Slot> entry : s.TuThLab.entrySet()) {
            this.TuThLab.put(entry.getKey(), new LabSlot((LabSlot) entry.getValue()));

        }
        for (Map.Entry<Integer, Slot> entry : s.FLab.entrySet()) {

            this.FLab.put(entry.getKey(), new LabSlot((LabSlot) entry.getValue()));

        }
        setupOverlaps();

    }

    public Schedule(ArrayList<CourseSlot> courseSlots, ArrayList<LabSlot> labSlots) {
        // Create all Slots in the correct places
        for (int i = 0; i < courseSlots.size(); i++) {
            if (courseSlots.get(i).getDay() == CourseDays.MONWEDFRI) {
                this.MWFLec.put(courseSlots.get(i).time, courseSlots.get(i));
            } else {
                this.TuThLec.put(courseSlots.get(i).time, courseSlots.get(i));
            }
        }
        for (int i = 0; i < labSlots.size(); i++) {
            if (labSlots.get(i).getDay() == LabDays.MONWED) {
                this.MWLab.put(labSlots.get(i).getTime(), labSlots.get(i));
            } else if (labSlots.get(i).getDay() == LabDays.TUETHR) {
                this.TuThLab.put(labSlots.get(i).getTime(), labSlots.get(i));
            } else {
                this.FLab.put(labSlots.get(i).getTime(), labSlots.get(i));
            }
        }
        setupOverlaps();
    }

    public HashMap<Integer, Slot> getMWFLec() {
        return this.MWFLec;
    }
    public void setMWFLec(HashMap<Integer, Slot> replacement){
        //TODO: Validation so we don't accidentally set something crazy
        this.MWFLec = replacement;
    }

    public HashMap<Integer, Slot> getTuThLec() {
        return this.TuThLec;
    }
    public void setTuThLec(HashMap<Integer, Slot> replacement){
        //TODO: Validation so we don't accidentally set something crazy
        this.TuThLec = replacement;
    }

    public HashMap<Integer, Slot> getMWLab() {
        return this.MWLab;
    }
    public void setMWLab(HashMap<Integer, Slot> replacement){
        //TODO: Validation so we don't accidentally set something crazy
        this.MWLab = replacement;
    }

    public HashMap<Integer, Slot> getTuThLab() {
        return this.TuThLab;
    }
    public void setTuThLab(HashMap<Integer, Slot> replacement){
        //TODO: Validation so we don't accidentally set something crazy
        this.TuThLab = replacement;
    }

    public HashMap<Integer, Slot> getFLab() {
        return this.FLab;
    }
    public void setFLab(HashMap<Integer, Slot> replacement){
        //TODO: Validation so we don't accidentally set something crazy
        this.FLab = replacement;
    }

    public void assignMWFLecTimeToSlot(Slot slot) {
        MWFLec.put(slot.getTime(), slot);
    }

    public void assignTuThLecTimeToSlot(Slot slot) {
        TuThLec.put(slot.getTime(), slot);
    }

    public void assignMWLabTimeToSlot(Slot slot) {
        MWLab.put(slot.getTime(), slot);
    }

    public void assignTuThLabTimeToSlot(Slot slot) {
        TuThLab.put(slot.getTime(), slot);
    }

    public void assignFLabTimeToSlot(Slot slot) {
        FLab.put(slot.getTime(), slot);
    }

    public String toString() {
        // Iterate through each Map and print the slots with the courses in each of them
        String toReturn = "Schedule: \n";
        toReturn += "MWF Lecture Slots: \n";
        for (Map.Entry<Integer, Slot> entry : MWFLec.entrySet()) {
            
            toReturn += "" + ((CourseSlot)entry.getValue()).toString() + " ";
            toReturn += ((CourseSlot) (entry.getValue())).toStringShowElements() + " ";
            toReturn += "\n";

        }
        for (Map.Entry<Integer, Slot> entry : TuThLec.entrySet()) {

            toReturn += "" + entry.getValue().toString() + " ";
            toReturn += ((CourseSlot) (entry.getValue())).toStringShowElements() + " ";
            toReturn += "\n";

        }
        for (Map.Entry<Integer, Slot> entry : MWLab.entrySet()) {

            toReturn += "" + entry.getValue().toString() + " ";
            toReturn += ((LabSlot) (entry.getValue())).toStringShowElements() + " ";
            toReturn += "\n";

        }
        for (Map.Entry<Integer, Slot> entry : TuThLab.entrySet()) {

            toReturn += "" + entry.getValue().toString() + " ";
            toReturn += ((LabSlot) (entry.getValue())).toStringShowElements() + " ";
            toReturn += "\n";

        }
        for (Map.Entry<Integer, Slot> entry : FLab.entrySet()) {

            toReturn += "" + entry.getValue().toString() + " ";
            toReturn += ((LabSlot) (entry.getValue())).toStringShowElements() + " ";
            toReturn += "\n";

        }
        toReturn += "\n\n\n";

        return toReturn;
    }

    /**
     * setups the slots that have overlapping time slots
     */
    private void setupOverlaps() {
        // MWF lectures adds MW automatically with same time lab
        for (Map.Entry<Integer, Slot> entry : MWFLec.entrySet()) {
            Slot slot = (Slot) entry.getValue();
            slot.addOverlaps(slot);
            Slot s = MWLab.get(slot.getTime());
            if(s != null){
                slot.addOverlaps(s);
                s.addOverlaps(slot);
                s.addOverlaps(s);
            }
            // adding friday labs
            for (Map.Entry<Integer, Slot> entry2 : FLab.entrySet()) {
                Slot slot2 = (Slot) entry2.getValue();
                slot2.addOverlaps(slot2);
                if (slot.getTime() == slot2.getTime() || slot.getTime() == slot2.getTime() + 100) {
                    slot.addOverlaps(slot2);
                    slot2.addOverlaps(slot);
                }
            }
        }
        // Tuesday lectures
        for (Map.Entry<Integer, Slot> entry : TuThLec.entrySet()) {
            Slot slot = (Slot) entry.getValue();
            slot.addOverlaps(slot);
            for (Map.Entry<Integer, Slot> entry2 : TuThLab.entrySet()) {
                Slot slot2 = (Slot) entry2.getValue();
                slot2.addOverlaps(slot2);
                if (slot.getTime() % 100 == 0) {
                    if (slot2.getTime() == slot.getTime()) {
                        slot.addOverlaps(slot2);
                        slot2.addOverlaps(slot);
                    } else if (slot.getTime() == slot2.getTime() - 100) {
                        slot.addOverlaps(slot2);
                        slot2.addOverlaps(slot);
                    }
                } else {
                    if (slot.getTime() == slot2.getTime() + 30) {
                        slot.addOverlaps(slot2);
                        slot2.addOverlaps(slot);
                    } else if (slot.getTime() == slot2.getTime() - 70) {
                        slot.addOverlaps(slot2);
                        slot2.addOverlaps(slot);
                    }
                }
            }
        }
    }

}
