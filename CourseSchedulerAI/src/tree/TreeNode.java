package tree;

import schedule.*;
import java.util.*;

public class TreeNode implements Comparable<TreeNode> {

    private TreeNode parent;
    private int penaltyValue;
    private Schedule schedule;
    private ArrayList<TreeNode> children; // This could be a heap so the one with the lowest pen value is always on top
    private PriorityQueue<TreeNode> orderedChildren = new PriorityQueue<TreeNode>();

    private int desirability;
    private boolean still_considered;
    private boolean already_looked_at;

    public TreeNode(Schedule schedule, int penaltyValue) {

        this.penaltyValue = penaltyValue;
        this.schedule = new Schedule(schedule);
        this.children = new ArrayList<TreeNode>();
        this.parent = null;
        this.desirability = Integer.MAX_VALUE;
        this.orderedChildren = new PriorityQueue<TreeNode>();
        this.still_considered = true;

    }

    public TreeNode(TreeNode n) {
        this.penaltyValue = n.getPenaltyValueOfTreeNode();
        this.schedule = new Schedule(n.getSchedule());
        this.children = n.getChildren();
        this.desirability = n.getDesirablilty();
        this.orderedChildren = n.getOrderedChildren();
        this.still_considered = n.still_considered;
    }

    public TreeNode(Schedule schedule, int penaltyValue, ArrayList<TreeNode> children) {
        this.penaltyValue = penaltyValue;
        this.schedule = new Schedule(schedule);
        for (int i = 0; i < children.size(); i++) {
            this.children.add(children.get(i));
            this.orderedChildren.add(children.get(i));
        }
        this.parent = null;
        this.desirability = Integer.MAX_VALUE;
        this.still_considered = true;
    }

    public TreeNode(Schedule schedule, int penaltyValue, TreeNode parent, int desirability ) {

        this.penaltyValue = penaltyValue;
        this.schedule = new Schedule(schedule);
        this.children = new ArrayList<TreeNode>();
        this.orderedChildren = new PriorityQueue<TreeNode>();
        this.parent = parent;
        this.desirability = desirability;
        this.still_considered = true;
    }

    public TreeNode(Schedule schedule, int penaltyValue, ArrayList<TreeNode> children, TreeNode parent) {
        this.penaltyValue = penaltyValue;
        this.schedule = new Schedule(schedule);
        for (int i = 0; i < children.size(); i++) {
            this.children.add(children.get(i));
            this.orderedChildren.add(children.get(i));
        }
        this.parent = parent;
        this.desirability = Integer.MAX_VALUE;
        this.still_considered = true;
    }

    public boolean getConsideration() {
        return this.still_considered;
    }

    public void noLongerConsidered() {
        this.still_considered = false;
    }

    public int getPenaltyValueOfTreeNode() {
        return this.penaltyValue;
    }

    public void addChild(TreeNode n) {
        this.children.add(n);

        this.orderedChildren.add(n);
        // System.out.println(Arrays.toString(this.orderedChildren.toArray()));
    }

    public TreeNode getParent() {
        return this.parent;
    }

    public Schedule getSchedule() {
        return this.schedule;
    }

    public ArrayList<TreeNode> getChildren() {
        // for (int i = 0; i < this.children.size(); i++){
        // System.out.print("TreeNode with Pen: " + this.children.get(i).penaltyValue +
        // ", " );
        // }
        // System.out.println();
        // System.out.println();
        // for (int k = 0; k< this.children.size(); k++){
        // System.out.println(this.children.get(k).getSchedule().toString());
        // }
        // System.out.println();

        return this.children;

    }

    public TreeNode getLowestPenaltyChild() {
        return this.orderedChildren.remove();
    }

    public TreeNode getChildWithPenaltyValue(int n) {

        for (int i = 0; i < this.children.size(); i++) {
            if (n == this.children.get(i).getPenaltyValueOfTreeNode()) {
                return this.children.get(i);
            }
        }
        return null;
    }

    public int getDesirablilty() {
        return this.desirability;
    }

    @Override
    public int compareTo(TreeNode o) {
        return (((Integer) (this.getDesirablilty())).compareTo((Integer) o.getDesirablilty()));
    }

    public String toString() {
        return "TreeNode: Penalty: " + this.penaltyValue + " " + this.schedule.toString();
    }

    public PriorityQueue<TreeNode> getOrderedChildren() {
        return orderedChildren;
    }

    public boolean getAlreadyLookedAt() {
        return already_looked_at;
    }

    public void setAlreadyLookedAt(boolean value) {
        already_looked_at = value;
    }

}