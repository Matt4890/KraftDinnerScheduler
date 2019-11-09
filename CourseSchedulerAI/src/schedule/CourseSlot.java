package schedule;
import java.util.ArrayList;
import coursesULabs.*;
public class CourseSlot extends Slot{

    private ArrayList<Courses> courses = new ArrayList<Courses>();
    private int courseMax;
    private int courseMin;
    private String day;

    public CourseSlot (int id, int time, String day, int courseMax, int courseMin ){
        super.setID(id);
        super.setTime(time);
        super.setType(type);
        this.courseMax = courseMax;
        this.courseMin = courseMin;
        this.day = day;
    }

    public String getDay(){
        return this.day;
    }
    public int getCourseMax(){
        return this.courseMax;
    }
    public int getCourseMin(){
        return this.courseMin;
    }



    
}