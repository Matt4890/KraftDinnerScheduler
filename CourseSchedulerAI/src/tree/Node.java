package tree;

import schedule.*;
import java.util.*;



public class Node implements Comparable<Node>{

    private Node parent;
    private int penaltyValue; 
    private Schedule schedule;
    private ArrayList<Node> children; //This could be a heap so the one with the lowest pen value is always on top
    private PriorityQueue<Node> orderedChildren;

    private int desirability;
    private boolean still_considered;



    public Node (Schedule schedule, int penaltyValue){

        this.penaltyValue = penaltyValue;
        this.schedule = schedule;
        this.children = new ArrayList<Node>();
        this.parent = null;
        this.desirability = Integer.MAX_VALUE;
        this.orderedChildren = new PriorityQueue<Node>();
        this.still_considered = true;

    }
    
    public Node (Schedule schedule, int penaltyValue, ArrayList<Node> children){
        this.penaltyValue = penaltyValue;
        this.schedule = schedule;
        for (int i = 0; i<children.size(); i++){
            this.children.add(children.get(i));
            this.orderedChildren.add(children.get(i));
        }
        this.parent = null;
        this.desirability = Integer.MAX_VALUE;
        this.still_considered = true;
    }
    public Node (Schedule schedule, int penaltyValue, Node parent){

        this.penaltyValue = penaltyValue;
        this.schedule = schedule;
        this.children = new ArrayList<Node>();
        this.parent = parent;
        this.desirability = Integer.MAX_VALUE;
        this.still_considered = true;
    }
    public Node (Schedule schedule, int penaltyValue, ArrayList<Node> children, Node parent){
        this.penaltyValue = penaltyValue;
        this.schedule = schedule;
        for (int i = 0; i<children.size(); i++){
            this.children.add(children.get(i));
            this.orderedChildren.add(children.get(i));
        }
        this.parent = parent;
        this.desirability = Integer.MAX_VALUE;
        this.still_considered = true;
    }
    public boolean getConsideration(){
        return this.still_considered;
    }
    public void noLongerConsidered(){
        this.still_considered = false;
    }
    public int getPenaltyValueOfNode(){
        return this.penaltyValue;
    }
    public void addChild(Node n){
        this.children.add(n);
        this.orderedChildren.add(n);
    }
    public Node getParent(){
        return this.parent;
    }

    public Schedule getSchedule(){
        return this.schedule;
    }

    public ArrayList<Node> getChildren(){
        return this.children;
    }
    public Node getLowestPenaltyChild(){
        return this.orderedChildren.peek();
    }
    
    public Node getChildWithPenaltyValue(int n){
       
        for (int i = 0; i< this.children.size(); i++){
            if (n == this.children.get(i).getPenaltyValueOfNode()){
                return this.children.get(i);
            }
        }
        return null;
    }
    public int getDesireablilty(){
        return this.desirability;
    }
    public void calculateDesireablility(){
        //This is the F_leaf function that we want to calculate whether its a good assignment pair or not.
        

    }

    @Override
    public int compareTo(Node o) {
        return ((Integer)(this.getPenaltyValueOfNode())).compareTo(((Integer)o.getPenaltyValueOfNode()));
    }


}