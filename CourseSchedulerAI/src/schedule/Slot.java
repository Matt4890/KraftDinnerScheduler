package schedule;

import enums.SlotType;

public abstract class Slot {
    protected SlotType type;
    protected int time;
    protected int id; 

    public Slot (int id, int time, SlotType type){
        this.type = type;
        this.time = time;
        this.id = id;

    }
    protected void setID(int id){
        this.id = id;

    }
    protected void setTime(int time){
        this.time = time;

    }
    protected void setType(SlotType type){
        this.type = type;

    }

    public int getID(){
        return this.id;

    }
    public int getTime(){
        return this.time;

    }
    public SlotType getType(){
        return this.type;

    }
    
    public abstract void addOccupant(Object o);
    
    
}