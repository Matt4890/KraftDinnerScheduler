package coursesULabs;

public class Lab extends Unit{

    private int tutNum;

    public int getTutNum() {
        return tutNum;
    }
    
    public void setTutNum(int tutNum) {
        this.tutNum = tutNum;
    }

    public Lab (int lectureNum, String courseType, int courseNum, int tutNum ){
        super.lectureNum = lectureNum;
        super.courseType = courseType;
        super.courseNum = courseNum;
        this.tutNum = tutNum;
    }

    public Lab (String courseType, int courseNum, int tutNum ){
        super.courseType = courseType;
        super.courseNum = courseNum;
        this.tutNum = tutNum;
    }

    public Lab (int lectureNum, String courseType, int courseNum, Object slot, int tutNum) {
        this.lectureNum = lectureNum;
        this.courseType = courseType;
        this.courseNum = courseNum;
        this.slot = slot;
        this.tutNum = tutNum;
    }

}