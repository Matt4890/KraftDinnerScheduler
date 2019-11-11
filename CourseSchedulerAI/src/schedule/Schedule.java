package schedule;
import java.util.ArrayList;
import coursesULabs.*;
public class Schedule{
    
    private ArrayList<Slot> theSchedule = new ArrayList<Slot>();
    //Do we want these to have any particular ordering at the beginning 

    
    public void assignCourseToSlot(int slotID, Course c){
        theSchedule.get(slotID).addOccupant(c);
    }

    public String toString(){
        return "";
    }

}