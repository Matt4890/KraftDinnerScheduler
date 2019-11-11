package schedule;
import java.util.ArrayList;
import coursesULabs.*;
public class LabSlot extends Slot{

    private ArrayList<Lab> labs = new ArrayList<Lab>();
    private int labMax;
    private int labMin;
    private int labCount;
    private String day;

    public LabSlot (int id, int time, String day, int labMax, int labMin ){
        super(id, time, "Lab");
        this.labMax = labMax;
        this.labMin = labMin;
        this.day = day;
        this.labCount = 0;
    }

    public String getDay(){
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
        for (int i = 0; i< labs.size(); i++){
            displaylabs.add(labs.get(i));
        }
        return displaylabs; 

    }

    public void addOccupant(Object lo){
        Lab l = (Lab) lo;
        if (this.labCount < this.labMax){
            labs.add(l);
        } else {
            System.out.println("Hard Constraint labMax Broken");
        }

    }


    
}