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
    protected ArrayList<Slot> overlaps = new ArrayList<Slot>();

    public Slot(int id, int time, SlotType type, HashMap<Unit, Integer> hashMap) {
        this.type = type;
        this.time = time;
        this.id = id;
        // new
        this.prefMap = hashMap;

    }
    public Slot (Slot s){
        this.type = s.type;
        this.time = s.time;
        this.id = s.id;
        this.prefMap = new HashMap<Unit, Integer>();
        for (Map.Entry<Unit, Integer> entry : s.prefMap.entrySet()) {
            if (entry.getKey() instanceof Lab){
                this.prefMap.put(new Lab((Lab)entry.getKey()), entry.getValue());
            } else {
                this.prefMap.put(new Course((Course)entry.getKey()), entry.getValue());
            }
        }
        
        
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
    public ArrayList<Unit> getClassAssignment(){
        return this.classAssignment;

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

    public void assignUnitToSlot(Unit unit) {
        classAssignment.add(unit);

    }

    public abstract void addOccupant(Object o);

    public ArrayList<Slot> getOverlaps() {
        return overlaps;
    }

    public void addOverlaps(Slot slot){
        overlaps.add(slot);
    }

    public boolean isSameSlot(Slot s){
        if(this.getTime() == s.getTime()){
            if(this instanceof CourseSlot){
                if(s instanceof CourseSlot){
                    if(((CourseSlot)this).getDay().equals(((CourseSlot)s).getDay())){
                        return true;
                    }
                }
            }
            else{
                if(s instanceof LabSlot){
                    if(((LabSlot)this).getDay().equals(((LabSlot)s).getDay())){
                        return true;
                    }                    
                }
            }
        }

        return true;
    }

}
