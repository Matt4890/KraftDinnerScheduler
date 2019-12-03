package tree;

import coursesULabs.Unit;
import schedule.Slot;

public class Pair {

    private Slot slot;
    private Unit unit;

    public Pair(Slot slot, Unit unit){
        this.slot = slot;
        this.unit = unit;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }



}