package schedule;
import java.util.*;
import coursesULabs.*;
import enums.*;

public class Schedule{
    
    private HashMap<Integer, Slot> MWFLec = new HashMap<Integer, Slot>();
    private HashMap<Integer, Slot> TuThLec = new HashMap<Integer, Slot>();
    private HashMap<Integer, Slot> MWLab = new HashMap<Integer, Slot>();
    private HashMap<Integer, Slot> TuThLab = new HashMap<Integer, Slot>();
    private HashMap<Integer, Slot> FLab = new HashMap<Integer, Slot>();
    //Do we want these to have any particular ordering at the beginning 

    public Schedule (ArrayList<CourseSlot> courseSlots, ArrayList<LabSlot> labSlots) {
        //Create all Slots in the correct places 
        for (int i = 0; i< courseSlots.size(); i++){
            if (courseSlots.get(i).getDay() == CourseDays.MONWEDFRI){
                this.MWFLec.put(courseSlots.get(i).time, courseSlots.get(i));
            } else {
                this.TuThLec.put(courseSlots.get(i).time, courseSlots.get(i));
            }
        }
        for (int i = 0; i<labSlots.size(); i++){
            if (labSlots.get(i).getDay() == LabDays.MONWED){
                this.MWLab.put(labSlots.get(i).getTime(), labSlots.get(i));
            } else if (labSlots.get(i).getDay() == LabDays.TUETHR){
                this.TuThLab.put(labSlots.get(i).getTime(), labSlots.get(i));
            } else {
                this.FLab.put(labSlots.get(i).getTime(), labSlots.get(i));
            }
        } 
    

    }

    public  HashMap<Integer, Slot> getMWFLec(){
        return this.MWFLec;
    }
    public  HashMap<Integer, Slot> getTuThLec(){
        return this.TuThLec;
    }
    public  HashMap<Integer, Slot> getMWLab(){
        return this.MWLab;
    }
    public  HashMap<Integer, Slot> getTuThLab(){
        return this.TuThLab;
    }
    public  HashMap<Integer, Slot> getFLab(){
        return this.FLab;
    }
    
    public void assignMWFLecTimeToSlot(Slot slot){
        MWFLec.put(slot.getTime(), slot);
    }
    
    public void assignTuThLecTimeToSlot(Slot slot){
        TuThLec.put(slot.getTime(), slot);
    }
    
    public void assignMWLabTimeToSlot(Slot slot){
        MWLab.put(slot.getTime(), slot);
    }
    
    public void assignTuThLabTimeToSlot(Slot slot){
        TuThLab.put(slot.getTime(), slot);
    }
    
    public void assignFLabTimeToSlot(Slot slot){
        FLab.put(slot.getTime(), slot);
    }
    
	
    public String toString(){
        return "";
    }

}
