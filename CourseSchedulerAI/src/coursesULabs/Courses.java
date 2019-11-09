package coursesULabs;

import java.util.ArrayList;

public class Course extends Unit{

    private ArrayList<Labs> labs = new ArrayList<Labs>();


    public void addLab(Labs lab){
        labs.add(lab);
    }

    public ArrayList<Labs> getLabs(){
        return labs;
    }

    public boolean findLab(Labs lab){
        return labs.contains(lab);
    }



    public (int lectureNum, String courseType, int courseNum, Object slot) {
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