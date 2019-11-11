package schedule;
import java.util.ArrayList;
import coursesULabs.*;
public class CourseSlot extends Slot{

    private ArrayList<Course> courses = new ArrayList<Course>();
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
    public int getCourseCount(){
        return this.courseCount;
    }
    public ArrayList<Course> getAssignedCourses(){
        // THIS SHOULD ONLY BE USED FOR LOOKUP SO ITS NOT GOING TO BE THE SAME REFERENCES 
        ArrayList<Course> displayCourses  = new ArrayList<Course>();
        for (int i = 0; i< courses.size(); i++){
            displayCourses.add(courses.get(i));
        }
        return displayCourses; 


    }
    public void addOccupant(Object co){
        Course c = (Course) co;
        if (this.courseCount < this.courseMax){
            courses.add(c);
        } else {
            System.out.println("Hard Constraint CourseMax Broken");
        }

    }

    public boolean isString(String parserInput){
        String formatString = toString();
        return parserInput.equals(formatString);
    }

    public String toString(){
        return day + time;
    }
    
}