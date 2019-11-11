package coursesULabs;

import schedule.Slot;

public class Unit {

    protected Slot slot;
    protected int lectureNum;
    protected String courseType;
    protected int courseNum;
    protected int id;
    protected void setId(int i){
        this.id = i;
    }
    public int getID(){
        return this.id;
    }
    public int getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(int courseNum) {
        this.courseNum = courseNum;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public int getLectureNum() {
        return lectureNum;
    }

    public void setLectureNum(int lectureNum) {
        this.lectureNum = lectureNum;
    }

    public Object getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public Unit() {

    }
}