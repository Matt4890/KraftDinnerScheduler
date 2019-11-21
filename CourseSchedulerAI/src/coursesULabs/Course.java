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

    public Course (int id, int lectureNum, String courseType, int courseNum, Slot slot, String key) {
        this.lectureNum = lectureNum;
        this.courseType = courseType;
        this.courseNum = courseNum;
        this.slot = slot;
        super.id = id;
        super.key = key;
    }

    public Course(int id, int lectureNum, String courseType, int courseNum, String key){
        this.lectureNum = lectureNum;
        this.courseType = courseType;
        this.courseNum = courseNum;
        super.id = id;
        super.key = key;
    }

}
