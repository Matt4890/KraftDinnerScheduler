package coursesULabs;

import java.util.ArrayList;

public class Unit {

    //TODO change Object to Slots
    protected Object slot;
    protected int lectureNum;
    protected String courseType;
    protected int courseNum;
    protected ArrayList<Object> unwanted = new ArrayList<Object>();
    protected ArrayList<Unit> notCompatible = new ArrayList<Unit>();
    protected ArrayList<Object> preferences = new ArrayList<Object>();
    protected ArrayList<Unit> pair = new ArrayList<Unit>();
    protected Object partialAssignment;


    /**
     * 
     */
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

    public void addUnwanted(Object slot){
        unwanted.add(slot);
    }
    public void addPreference(Object slot){
        preferences.add(slot);
    }
    public void addNotCompatible(Unit unit){
        notCompatible.add(unit);
    }
    public void addPair(Unit unit){
        pair.add(unit);
    }


    public boolean findUnwanted(Object slot){
        return unwanted.contains(slot);
    }

    public boolean findPreference(Object slot){
        return preferences.contains(slot);
    }
    public boolean findNotCompatible(Unit unit){
        return notCompatible.contains(unit);
    }
    public boolean findPair(Unit unit){
        return pair.contains(unit);
    }

    public Unit() {

    }




}