package schedule;
import java.util.ArrayList;
public class Schedule{
    
    private ArrayList<Slot>()  theSchedule = new ArrayList<Slot>();
    //Do we want these to have any particular ordering at the beginning 
    public Schedule (){
    }
    public void initialSetup(){
        //Add all the Lecture Slots
            //Monday
            int count = 0;
            for (int i = 8; i < 21; i++){
                theSchedule.add(new CourseSlot(count, )); // CourseSlot 

            }


            //Tuesday
        //Add all the Lab Slots 
            //Monday
            //Tuesday
            //Friday

    }
    public void assignCourseToSlot(int slotID, Course c){


    }

}