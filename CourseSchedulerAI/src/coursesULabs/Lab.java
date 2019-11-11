package coursesULabs;

import schedule.Slot;

public class Lab extends Unit{

    private int tutNum;

    public int getTutNum() {
        return tutNum;
    }
    
    public void setTutNum(int tutNum) {
        this.tutNum = tutNum;
    }

    public Lab (int id, int lectureNum, String courseType, int courseNum, int tutNum ){
        super.id = id;
        super.lectureNum = lectureNum;
        super.courseType = courseType;
        super.courseNum = courseNum;
        this.tutNum = tutNum;
    }

    public Lab (int id, String courseType, int courseNum, int tutNum ){
        super.id = id;
        super.courseType = courseType;
        super.courseNum = courseNum;
        this.tutNum = tutNum;
    }

    public Lab (int id, int lectureNum, String courseType, int courseNum, Slot slot, int tutNum) {
        super.id = id;
        this.lectureNum = lectureNum;
        this.courseType = courseType;
        this.courseNum = courseNum;
        this.slot = slot;
        this.tutNum = tutNum;
    }

}