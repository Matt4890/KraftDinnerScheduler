package coursesULabs;
import java.util.ArrayList;
import java.util.HashMap;
import schedule.Slot;

public class Unit {

    protected int lectureNum;
    protected String courseType;
    protected int courseNum;
    protected int id;
    protected String key;
    protected int constrained = 1;
    protected static int partAssignIncrease = 100;
    protected static int unwantedIncrease = 3;
    protected static int nonCompatibleIncrease = 5;

    protected ArrayList<Slot> unwanted = new ArrayList<Slot>();
    protected ArrayList<Unit> notCompatible =  new ArrayList<Unit>();
    protected HashMap<Slot, Integer> preferences  = new HashMap<Slot, Integer>();
    protected ArrayList<Unit> pairs = new ArrayList<Unit>();
    protected static int eveningIncrease = 100;
    protected static int increase500 = 50;
    protected double potential = 0;
    

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

    public void incrementEvening(){
        this.constrained += eveningIncrease;
    }
    public void increment500(){
        this.constrained += increase500;
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

    public HashMap<Slot, Integer> getPreferences() {
        return this.preferences;
    }
    public void addToPreferences(Slot s, int score){
        this.preferences.put(s, score);
    }

    public ArrayList<Unit> getPairs() {
        return this.pairs;
    }
    public void addToPairs(Unit u){
        this.pairs.add(u);
    }
	public static void setPartAssignIncrease(int partAssignIncrease) {
		Unit.partAssignIncrease = partAssignIncrease;
	}

	public static void setUnwantedIncrease(int unwantedIncrease) {
		Unit.unwantedIncrease = unwantedIncrease;
	}

	public static void setNonCompatibleIncrease(int nonCompatibleIncrease) {
		Unit.nonCompatibleIncrease = nonCompatibleIncrease;
	}

	public static void setEveningIncrease(int eveningIncrease) {
		Unit.eveningIncrease = eveningIncrease;
	}

	public static void setIncrease500(int increase500) {
		Unit.increase500 = increase500;
	}

    public double getPotential() {
        return potential;
    }

    public void incrementPotential(double potential) {
        this.potential += potential;
    }

    public static double calculatePotential(ArrayList<Unit> units){
        double total = 0;
        for (Unit unit : units){
            total += unit.getPotential();
        }
        return total;
    }

}