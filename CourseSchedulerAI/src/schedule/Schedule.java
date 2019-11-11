package schedule;
import java.util.ArrayList;
import coursesULabs.*;
public class Schedule{
    
    private Map<int, Slot> MWFLec = new Map<>();
    private Map<int, Slot> TuThLec = new Map<>();
    private Map<int, Slot> MWLab = new Map<>();
    private Map<int, Slot> TuThLab = new Map<>();
    private Map<int, Slot> FLab = new Map<>();
    //Do we want these to have any particular ordering at the beginning 

    
    public void assignMWFLecTimeToSlot(Slot slot){
        MWFLec.put(slot.getTime, slot);
    }
    
    public void assignTuThLecTimeToSlot(Slot slot){
        TuThLec.put(slot.getTime, slot);
    }
    
    public void assignMWLabTimeToSlot(Slot slot){
        MWLab.put(slot.getTime, slot);
    }
    
    public void assignTuThLabTimeToSlot(Slot slot){
        TuThLab.put(slot.getTime, slot);
    }
    
    public void assignFLabTimeToSlot(Slot slot){
        FLab.put(slot.getTime, slot);
    }
    
	
    public String toString(){
        return "";
    }

}
