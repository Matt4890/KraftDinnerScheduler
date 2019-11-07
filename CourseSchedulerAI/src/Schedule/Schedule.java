import java.util.*; 

public class Schedule {
    private ArrayList<Slot> timeSlots = new ArrayList<Slot>();
    private ArrayList<Slot> assignedSlots =  new ArrayList<Slot>();
    private ArrayList<Slot> unassignedSlots = new ArrayList<Slot>();

    public Schedule(ArrayList<Slot> initialSlots){
        for (int i = 0; i< initialSlots.size(); i++){
            unassignedSlots.add(initialSlots.get(i));

        }

    }
    public void addUnasignedSlot(Slot s ){
        this.unassignedSlots.add(s);
    }

    
    public String toString(){
        System.out.println("Slots Assigned:");
        for (int i = 0; i<assignedSlots.size(); i++){
                System.out.println(assignedSlots.get(i));
        }
        System.out.println("Unnassigned Slots: ");
        for (int i = 0; i<unassignedSlots.size(); i++){
            System.out.println(unassignedSlots.get(i));
        }
    }


    
}