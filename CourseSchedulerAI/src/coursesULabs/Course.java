package coursesULabs;

import java.util.ArrayList;

import schedule.Slot;


public class Course extends Unit{

    private ArrayList<Lab> labs = new ArrayList<Lab>();
    

    public void addLab(Lab lab){
        labs.add(lab);
    }

    public ArrayList<Lab> getLabs(){
        return labs;
    }

    public boolean findLab(Lab lab){
        return labs.contains(lab);
    }

    public boolean isString(String parserInput){
        //CPSC433LEC01
        String formatCourse = courseType + courseNum + "LEC" + lectureNum;

        return formatCourse.equals(parserInput);
    }

    public String toString(){
        return courseType + courseNum + "LEC" + lectureNum;
    }

    public Course (int id, int lectureNum, String courseType, int courseNum, Slot slot) {
        this.lectureNum = lectureNum;
        this.courseType = courseType;
        this.courseNum = courseNum;
        this.slot = slot;
        super.id = id;
    }

    public Course(int id, int lectureNum, String courseType, int courseNum){
        this.lectureNum = lectureNum;
        this.courseType = courseType;
        this.courseNum = courseNum;
        super.id = id;
    }

}