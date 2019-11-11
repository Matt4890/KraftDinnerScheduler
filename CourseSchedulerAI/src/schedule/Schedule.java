package schedule;
import java.util.*;
public class Schedule{
    
    private HashMap<Integer, Slot> MWFLec = new HashMap<Integer, Slot>();
    private HashMap<Integer, Slot> TuThLec = new HashMap<Integer, Slot>();
    private HashMap<Integer, Slot> MWLab = new HashMap<Integer, Slot>();
    private HashMap<Integer, Slot> TuThLab = new HashMap<Integer, Slot>();
    private HashMap<Integer, Slot> FLab = new HashMap<Integer, Slot>();
    //Do we want these to have any particular ordering at the beginning 

    
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
