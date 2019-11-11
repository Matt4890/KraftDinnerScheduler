package schedule;
import java.util.ArrayList;
import coursesULabs.*;
public class CourseSlot extends Slot{

    private ArrayList<Courses> courses = new ArrayList<Courses>();
    private int courseMax;
    private int courseMin;
    private int courseCount;
    private String day;

    public CourseSlot (int id, int time, String day, int courseMax, int courseMin ){
        super(id, time, "Course");
        this.courseMax = courseMax;
        this.courseMin = courseMin;
        this.day = day;
        this.courseCount = 0;
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
    public void addCourse(Courses c){
        if (this.courseCount < this.courseMax){
            courses.add(c);
        } else {
            System.out.println("Hard Constraint CourseMax Broken");
        }

    }


    
}