package coursesULabs;

import java.util.ArrayList;

import schedule.Slot;


public class Course extends Unit{

    private ArrayList<Lab> labs = new ArrayList<Lab>();

    private ArrayList<Course> brothers = new ArrayList<Course>();


    public void addLab(Lab lab){
        labs.add(lab);
    }

    public ArrayList<Lab> getLabs(){
        return labs;
    }

    public boolean findLab(Lab lab){
        return labs.contains(lab);
    }

    public void addBrother(Course course){
        brothers.add(course);
    }

    public ArrayList<Course> getBrothers(){
        return brothers;
    }

    public boolean findBrother(Course course){
        return brothers.contains(course);
    }

    public boolean isString(String parserInput){
        String formatCourse = toString();

        return formatCourse.equals(parserInput);
    }

    public String toString(){
        //CPSC433LEC01
        return courseType + courseNum + "LEC" + String.format("%02d", lectureNum);
    }

    public Course (int id, int lectureNum, String courseType, int courseNum, Slot slot) {
        this.lectureNum = lectureNum;
        this.courseType = courseType;
        this.courseNum = courseNum;

        super.id = id;
    }

    public Course(int id, int lectureNum, String courseType, int courseNum){
        this.lectureNum = lectureNum;
        this.courseType = courseType;
        this.courseNum = courseNum;
        super.id = id;
    }
    public Course(Course c){
        this.lectureNum = c.lectureNum;
        this.courseType = new String(c.courseType);
        this.courseNum = c.courseNum;
        super.id = c.id;
    }

}
