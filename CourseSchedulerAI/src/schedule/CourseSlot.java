package schedule;

import java.util.ArrayList;
import java.util.HashMap;

import coursesULabs.Course;
import coursesULabs.Unit;
import enums.CourseDays;
import enums.SlotType;

public class CourseSlot extends Slot {

    private ArrayList<Course> courses = new ArrayList<Course>();
    private int courseMax;
    private int courseMin;
    private int courseCount;
    private CourseDays day;

    public CourseSlot(int id, int time, CourseDays day, int courseMax, int courseMin, HashMap<Unit, Integer> map) {
        super(id, time, SlotType.COURSE, map);
        this.courseMax = courseMax;
        this.courseMin = courseMin;
        this.day = day;
        this.courseCount = 0;
    }
    public CourseSlot(CourseSlot c){
        super(c);
        this.day = c.day;
        this.courseMax = c.courseMax;
        this.courseMin = c.courseMin;
        this.courseCount = c.courseCount;
        for (int i = 0; i< c.courses.size(); i++){
            this.courses.add(new Course(c.courses.get(i)));
        }

    }

    public CourseDays getDay() {
        return this.day;
    }

    public int getCourseMax() {
        return this.courseMax;
    }

    public int getCourseMin() {
        return this.courseMin;
    }

    public int getCourseCount() {
        return this.courseCount;
    }

    public ArrayList<Course> getAssignedCourses() {
        // THIS SHOULD ONLY BE USED FOR LOOKUP SO ITS NOT GOING TO BE THE SAME
        // REFERENCES
        ArrayList<Course> displayCourses = new ArrayList<Course>();
        for (int i = 0; i < courses.size(); i++) {
            displayCourses.add(courses.get(i));
        }
        return displayCourses;

    }

    public void addOccupant(Object co) {
        Course c = (Course) co;
        if (this.courseCount < this.courseMax) {
            courses.add(c);
            this.courseCount ++;
            //System.out.println("Course: " + co.toString() + " Successfully Added to: " + this.toString());
        } else {
            System.out.println("Hard Constraint CourseMax Broken");
        }

    }

    public boolean isString(String parserInput) {
        String formatString = toString();
        return parserInput.equals(formatString);
    }

    public String toStringShowElements() {
        String toReturn = "Courses Assigned To Slot: ";
        for (int i = 0; i < this.courses.size() - 1; i++) {
            toReturn += this.courses.get(i).toString() + ", ";
        }
        if (this.courses.size() != 0) {
            toReturn += this.courses.get(this.courses.size() - 1) + " ";
        }
        return toReturn;
    }

    public String toString() {
        return day.toString() + "," + Integer.toString(time).replaceAll("(\\d{2})$", ":$1");
    }

}
