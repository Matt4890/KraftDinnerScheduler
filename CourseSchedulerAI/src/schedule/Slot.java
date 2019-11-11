package schedule;

import enums.SlotType;
import java.util.*;
import coursesULabs.*;
public abstract class Slot {
    protected SlotType type;
    protected int time;
    protected int id;
    private ArrayList<Unit> classAssignment = new ArrayList<Unit>();
    protected HashMap<Unit, Integer> prefMap = new HashMap<Unit, Integer>();

    public Slot(int id, int time, SlotType type, HashMap<Unit, Integer> hashMap) {
        this.type = type;
        this.time = time;
        this.id = id;
        // new
        this.prefMap = hashMap;

    }

    protected void setID(int id) {
        this.id = id;

    }

    protected void setTime(int time) {
        this.time = time;

    }
    protected void setType(SlotType type){
        this.type = type;

    }

    public void setPreference(Unit unit, int score) {
        this.prefMap.put(unit, score);

    }

    public int getID() {
        return this.id;

    }

    public int getTime() {
        return this.time;

    }
    public SlotType getType(){
        return this.type;

    }

    public HashMap<Unit, Integer> getPreference() {
        return this.prefMap;

    }

    public int getEval() {
        int total = 0;
        for (int i = 0; i < classAssignment.size(); i++) {
            if (!getPreference().containsKey(classAssignment.get(i))) {
                total += (Integer)getPreference().get(classAssignment.get(i));
            }
        }
        return total;
    }

    public void assignUnitToSlot(Unit unit) {
        classAssignment.add(unit);

    }

    public abstract void addOccupant(Object o);

}
