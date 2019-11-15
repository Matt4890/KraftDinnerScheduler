package tree;

import schedule.*;
import java.util.*;


public class Node {

    private Node parent;
    private int penaltyValue; 
    private Schedule schedule;
    private ArrayList<Node> children; //This could be a heap so the one with the lowest pen value is always on top



    public Node (Schedule schedule, int penaltyValue){

        this.penaltyValue = penaltyValue;
        this.schedule = schedule;
        this.children = new ArrayList<Node>();
        this.parent = null;
    }
    
    public Node (Schedule schedule, int penaltyValue, ArrayList<Node> children){
        this.penaltyValue = penaltyValue;
        this.schedule = schedule;
        for (int i = 0; i<children.size(); i++){

        }
        this.parent = null;
    }
    public Node (Schedule schedule, int penaltyValue, Node parent){

        this.penaltyValue = penaltyValue;
        this.schedule = schedule;
        this.children = new ArrayList<Node>();
        this.parent = parent;
    }
    public Node (Schedule schedule, int penaltyValue, ArrayList<Node> children, Node parent){
        this.penaltyValue = penaltyValue;
        this.schedule = schedule;
        for (int i = 0; i<children.size(); i++){

        }
        this.parent = parent;
    }

    

    public int getPenaltyValueOfNode(){
        return this.penaltyValue;
    }
    public void addChild(Node n){
        this.children.add(n);
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
    public Node getChildWithPenaltyValue(int n){
       
        for (int i = 0; i< this.children.size(); i++){
            if (n == this.children.get(i).getPenaltyValueOfNode()){
                return this.children.get(i);
            }
        }
        return null;
    }

}