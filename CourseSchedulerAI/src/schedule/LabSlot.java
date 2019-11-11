package schedule;

import java.util.ArrayList;
import coursesULabs.*;
import enums.LabDays;
import enums.SlotType;
public class LabSlot extends Slot{

    private ArrayList<Lab> courses = new ArrayList<Lab>();
    private int labMax;
    private int labMin;
    private int labCount;
    private LabDays day;

    public LabSlot (int id, int time, LabDays day, int labMax, int labMin ){
        super(id, time, SlotType.LAB);
        this.labMax = labMax;
        this.labMin = labMin;
        this.day = day;
        this.labCount = 0;
    }

    public LabDays getDay(){
        return this.day;
    }
    public int getLabMax(){
        return this.labMax;
    }
    public int getLabMin(){
        return this.labMin;
    }
    public int getLabCount(){
        return this.labCount;
    }

    public void addOccupant(Object lo){
        Lab l = (Lab) lo;
        if (this.labCount < this.labMax){
            courses.add(l);
        } else {
            System.out.println("Hard Constraint labMax Broken");
        }

    }


    
}