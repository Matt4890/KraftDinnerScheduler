package coursesULabs;
import java.util.ArrayList;
import schedule.Slot;

public class Unit {

    protected Slot slot;
    protected int lectureNum;
    protected String courseType;
    protected int courseNum;
    protected int id;
    protected String key;
    protected int constrained;
    protected int partAssignIncrease = 100;
    protected int unwantedIncrease = 3;
    protected int nonCompatibleIncrease = 5;

    protected ArrayList<Slot> unwanted = new ArrayList<Slot>();
    protected ArrayList<Unit> notCompatible =  new ArrayList<Unit>();
    protected ArrayList<Unit> preferences  = new ArrayList<Unit>();
    protected ArrayList<Unit> pairs = new ArrayList<Unit>(); 
    
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getConstrained() {
        return constrained;
    }

    public void incrementPartAssign() {
        this.constrained += partAssignIncrease;
    }
    public void incrementUnwanted() {
        this.constrained += unwantedIncrease;
    }
    public void incrementNonCompatible() {
        this.constrained += nonCompatibleIncrease;
    }

    public ArrayList<Slot> getUnwanted() {
        return unwanted;
    }
    public void addToUnwanted(Slot s){
        this.unwanted.add(s);
    }

    public ArrayList<Unit> getNotCompatible() {
        return this.notCompatible;
    }
    public void addToNotCompatible(Unit u){
        this.notCompatible.add(u);
    }

    public ArrayList<Unit> getPreferences() {
        return this.preferences;
    }
    public void addToPreferences(Unit u){
        this.preferences.add(u);
    }

    public ArrayList<Unit> getPairs() {
        return this.pairs;
    }
    public void addToPairs(Unit u){
        this.pairs.add(u);
    }


}