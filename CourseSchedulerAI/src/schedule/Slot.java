package schedule;


public abstract class Slot{
    protected String type;
    protected int time;
    protected int id;
	private ArrayList<Unit> classAssignment = new ArrayList<Unit>();
    protected Map<Unit, int> prefMap = new Map<>();

    public Slot (int id, int time, String type, Map hashMap){
        this.type = type;
        this.time = time;
        this.id = id;
        //new
        this.prefMap = hashMap;  

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
    
    public void setPreference(Unit unit, int score){
		this.prefMap.put(unit, score);
	
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
	public Map getPreference(){
		return this.prefMap;
		
	}
	public int getEval(){
		if(!getPreference.containsKey(unit)){
			return getPreference.get(unit);
		}
		return 0;
	}
	
	public void assignUnitToSlot(Unit unit){
		classAssignment.add(unit);
		
	}
	
	
    
    public abstract void addOccupant(Object o);
    
	
    
    
}
