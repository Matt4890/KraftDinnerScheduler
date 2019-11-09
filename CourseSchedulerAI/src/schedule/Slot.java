package schedule;
public class Slot{
    protected String type;
    protected int time;
    protected int id; 

    public Slot (int id, int time, String type){
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
    protected void setType(String type){
        this.type = type;

    }

    public int getID(){
        return this.id;

    }
    public int getTime(){
        return this.time;

    }
    public String getType(){
        return this.type;

    }


    
}