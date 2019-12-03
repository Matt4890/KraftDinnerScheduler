package tree;

import schedule.*;
import java.util.*;


import coursesULabs.Unit;

public class TreeNode implements Comparable<TreeNode> {

    private TreeNode parent;
    private int penaltyValue;
    private Schedule schedule;
    private Pair assign;
    private ArrayList<TreeNode> children; // This could be a heap so the one with the lowest pen value is always on top
    private PriorityQueue<TreeNode> orderedChildren = new PriorityQueue<TreeNode>();
    private int depth;


    private int desirability;
    private boolean still_considered;
    private boolean already_looked_at;
    private TreeNode best_bottom_tn;
    private double potential = 0;


    public TreeNode(Pair pair, int penaltyValue) {

        this.penaltyValue = penaltyValue;
        this.assign = pair;
        this.children = new ArrayList<TreeNode>();
        this.parent = null;
        this.desirability = Integer.MAX_VALUE;
        this.orderedChildren = new PriorityQueue<TreeNode>();
        this.still_considered = true;
        this.best_bottom_tn = null;

    }

    public TreeNode(TreeNode n) {
        this.penaltyValue = n.getPenaltyValueOfTreeNode();
        this.schedule = new Schedule(n.getSchedule());
        this.children = n.getChildren();
        this.desirability = n.getDesirablilty();
        this.orderedChildren = n.getOrderedChildren();
        this.still_considered = n.still_considered;
    }

    public TreeNode(Pair pair, int penaltyValue, ArrayList<TreeNode> children) {
        this.penaltyValue = penaltyValue;
        this.schedule = new Schedule(schedule);
        this.assign = pair;
        this.parent = null;
        this.desirability = Integer.MAX_VALUE;
        this.still_considered = true;
    }

    public TreeNode(Pair assignment, int penaltyValue, TreeNode parent) {

        this.penaltyValue = penaltyValue;
        this.assign = assignment;
        this.children = new ArrayList<TreeNode>();
        this.orderedChildren = new PriorityQueue<TreeNode>();
        this.parent = parent;
    }

    public TreeNode(Pair assign, int penaltyValue, ArrayList<TreeNode> children, TreeNode parent) {
        this.penaltyValue = penaltyValue;
        this.assign = assign;
        for (int i = 0; i < children.size(); i++) {
            this.children.add(children.get(i));
            this.orderedChildren.add(children.get(i));
        }
        this.parent = parent;
        this.desirability = Integer.MAX_VALUE;
        this.still_considered = true;
    }
    public void setPenalty(int pen){
        this.penaltyValue = pen;
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
    public void addToPenaltyForBaseNode(int penaltyToAdd){
        this.penaltyValue += penaltyToAdd;
    }

    public void addChild(TreeNode n) {
        this.children.add(n);
        this.orderedChildren.add(n);
    }

    public TreeNode getParent() {
        return this.parent;
    }

    public Schedule getSchedule() {
        return this.schedule;
    }

    public ArrayList<TreeNode> getChildren() {
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
        // return (((Integer) (this.getDesirablilty())).compareTo((Integer) o.getDesirablilty()));
        return (
            ((Integer) (this.getConstrainedMeasuremnet()))
            .compareTo((Integer) o.getConstrainedMeasuremnet())
        );
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

    public TreeNode getBestBottomTN() {
        return best_bottom_tn;
    }

    public void setgetBestBottomTN(TreeNode n) {
        best_bottom_tn = n;
    }

    public double getPotential() {
        return potential;
    }

    public void incrementPotential(double potential) {
        this.potential += potential;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * Gets a measurement of how constrained the node is.
     * The higher the value, the more it is constrained.
     * When calculating, each constraint should ideally be evaluated to a number between 0 and 1,
     * then multiplied by a course/lab weight.
     * @return An integer expressing 
     */
    public int getConstrainedMeasuremnet() {

        int constrainedVal = 0;

        final int COURSE_WEIGHT = 1;
        final int LAB_WEIGHT = 1;

        // Measure course contraints
        for (Slot s : this.schedule.getAllCourseSlots()) {
            int cval = 0;
            for (Unit u : s.getClassAssignment()) {
                cval += u.getConstrained();
            }
            constrainedVal += cval * COURSE_WEIGHT;
        }

        // Measure lab contraints
        for (Slot s : this.schedule.getAllLabSlots()) {
            int cval = 0;
            for (Unit u : s.getClassAssignment()) {
                cval += u.getConstrained();
            }
            constrainedVal += cval * LAB_WEIGHT;
        }
        
        return constrainedVal;

    }

}