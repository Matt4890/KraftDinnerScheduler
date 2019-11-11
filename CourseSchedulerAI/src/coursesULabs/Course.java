package coursesULabs;

import java.util.ArrayList;

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

    public Course (int lectureNum, String courseType, int courseNum, Object slot) {
        this.lectureNum = lectureNum;
        this.courseType = courseType;
        this.courseNum = courseNum;
        this.slot = slot;
    }

    public Course(int lectureNum, String courseType, int courseNum){
        this.lectureNum = lectureNum;
        this.courseType = courseType;
        this.courseNum = courseNum;
    }

}