package coursesULabs;

public class Unit {

    //TODO change Object to Slots
    protected Object slot;
    protected int lectureNum;
    protected String courseType;
    protected int courseNum;
    protected Object partialAssignment;

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

    public void setSlot(Object slot) {
        this.slot = slot;
    }

    public Unit() {

    }
}