package tree;

import coursesULabs.Unit;
import schedule.Slot;

public class Pair {

    private Slot slot;
    private Unit unit;

    /**
     * creates a pair with a slot and a unit
     * @param slot
     * @param unit
     */
    public Pair(Slot slot, Unit unit){
        this.slot = slot;
        this.unit = unit;
    }
    /**
     * returns the slot in pair
     * @return Slot in pair
     */
    public Slot getSlot() {
        return slot;
    }
    /**
     * sets the slot in the pair
     * @param slot
     */
    public void setSlot(Slot slot) {
        this.slot = slot;
    }
    /**
     * get the unit in the pair
     * @return
     */
    public Unit getUnit() {
        return unit;
    }
    /**
     * Sets the unit in the pair
     * @param unit
     */
    public void setUnit(Unit unit) {
        this.unit = unit;
    }
    /**
     * toString method
     */
    public String toString(){
        return "Pair Course: " + unit + " with Slot: " + slot;
    }



}