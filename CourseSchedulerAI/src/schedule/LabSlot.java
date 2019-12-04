package schedule;
import java.util.*;
import coursesULabs.*;
import enums.LabDays;
import enums.SlotType;
public class LabSlot extends Slot{

    //private ArrayList<Lab> labs = new ArrayList<Lab>();
    private int labMax;
    private int labMin;
    private int labCount;
    private LabDays day;

    public LabSlot (int id, int time, LabDays day, int labMax, int labMin, HashMap<Unit, Integer> map ){
        super(id, time, SlotType.LAB, map);
        this.labMax = labMax;
        this.labMin = labMin;
        this.day = day;
        this.labCount = 0;
    }
    public LabSlot(LabSlot l){
        super(l);
        this.day = l.day;
        this.labMax = l.labMax;
        this.labMin = l.labMin;
        this.labCount = l.labCount;
        for (int i = 0; i<l.getClassAssignment().size(); i++){
            this.getClassAssignment().add((l.getClassAssignment().get(i)));
        }

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
    public ArrayList<Lab> getAssignedLabs(){
        // THIS SHOULD ONLY BE USED FOR LOOKUP SO ITS NOT GOING TO BE THE SAME REFERENCES
        ArrayList<Lab> displaylabs  = new ArrayList<Lab>();
        for (int i = 0; i< getClassAssignment().size(); i++){
            displaylabs.add((Lab)getClassAssignment().get(i));
        }
        return displaylabs;

    }

    public void addOccupant(Object lo){
        Lab l = (Lab) lo;
        if (this.labCount < this.labMax){
            getClassAssignment().add(l);
            this.labCount++;
        } else {
            // System.out.println("Hard Constraint labMax Broken");
        }

    }

    public boolean isString(String parserInput){
        String formatString = toString();
        return parserInput.equals(formatString);
    }
    public String toStringShowElements(){
        String toReturn = "Labs Assigned To Slot: ";
        for (int i = 0; i< this.getClassAssignment().size()-1; i++){
            toReturn += this.getClassAssignment().get(i).toString() + ", ";
        }
        if (this.getClassAssignment().size() != 0) {
            toReturn += this.getClassAssignment().get(this.getClassAssignment().size() - 1) + " ";
        }
        return toReturn;
    }
    public String toString(){
        return day.toString() + "," + Integer.toString(time).replaceAll("(\\d{2})$", ":$1");
    }



}
