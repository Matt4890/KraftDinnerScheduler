package schedule;
import java.util.ArrayList;
public class Schedule{
    
    private ArrayList<Slot>  theSchedule = new ArrayList<Slot>();
    //Do we want these to have any particular ordering at the beginning 

    
    public void assignCourseToSlot(int slotID, Courses c){
        theSchedule.get(slotID).addCourse(c);
    }

    public String toString(){
        for (int i = 0; i< theSchedule.size(); i++){
            

        }
    }

}