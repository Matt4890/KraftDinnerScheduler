package tree;

import schedule.*;
import java.util.*;

import coursesULabs.Unit;

public class TreeNode implements Comparable<TreeNode> {

    private TreeNode parent;
    private int penaltyValue;

    private Pair assign;
    private ArrayList<TreeNode> children; // This could be a heap so the one with the lowest pen value is always on top
    private PriorityQueue<TreeNode> orderedChildren = new PriorityQueue<TreeNode>();
    private int depth;

    private int desirability;

    private boolean already_looked_at;
    private TreeNode best_bottom_tn;
    private double potential = 0;
    /**
     * creates a root node with a pair and a penalty value 
     * @param pair
     * @param penaltyValue
     */
    public TreeNode(Pair pair, int penaltyValue) {

        this.penaltyValue = penaltyValue;
        this.assign = pair;
        this.children = new ArrayList<TreeNode>();
        this.parent = null;
        this.orderedChildren = new PriorityQueue<TreeNode>();

        this.best_bottom_tn = null;

    }
    /**
     * Deepcopy of TreeNode
     * @param n
     */
    public TreeNode(TreeNode n) {
        this.penaltyValue = n.getPenaltyValueOfTreeNode();
        this.children = n.getChildren();
        this.desirability = n.getDesirablilty();
        this.orderedChildren = n.getOrderedChildren();

    }

    /**
     * Creates tree node 
     * @param assignment: the slot unit pair
     * @param penaltyValue: the penalty value
     * @param parent: parent 
     */
    public TreeNode(Pair assignment, int penaltyValue, TreeNode parent) {

        this.penaltyValue = penaltyValue;
        this.assign = assignment;
        this.children = new ArrayList<TreeNode>();
        this.orderedChildren = new PriorityQueue<TreeNode>();
        this.parent = parent;
    }

    /**
     * Set the penalty value of the node 
     * @param pen
     */
    public void setPenalty(int pen) {
        this.penaltyValue = pen;
    }

    /**
     * gets the eval of Node
     * @return eval
     */
    public int getPenaltyValueOfTreeNode() {
        return this.penaltyValue;
    }

    /**
     * Add to penalty for base node
     * @param penaltyToAdd
     */
    public void addToPenaltyForBaseNode(int penaltyToAdd) {
        this.penaltyValue += penaltyToAdd;
    }
    /**
     * Add Child node n to list of children
     * 
     * @param n
     */
    public void addChild(TreeNode n) {
        this.children.add(n);
        this.orderedChildren.add(n);
    }

    public TreeNode getParent() {
        return this.parent;
    }

    public ArrayList<TreeNode> getChildren() {
        return this.children;

    }


    /**
     * 
     * @return desireability of node
     */
    public int getDesirablilty() {
        return this.desirability;
    }
    /**
     * Increment desireablility of node by num
     * @param num
     */
    public void incrementDesire(int num) {
        this.desirability += num;
    }
    
    @Override
    /**
     * Compares nodes based on desirability function defined in Kontrol
     */
    public int compareTo(TreeNode o) {
        return (((Integer) (o.getDesirablilty())).compareTo((Integer) this.getDesirablilty()));

    }

    public String toString() {
        String toReturn = "TreeNode: Depth: " + this.depth + " Penalty: " + this.penaltyValue + "\n";
        if (this.assign.getUnit() != null) {
            toReturn += "Unit: " + this.assign.getUnit().toPrettyString() + " in Slot: " + this.assign.getSlot().toPrettyString()
                    + "\n";
        }

        toReturn += "Parent " + this.parent;
        return toReturn;
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

    public Pair getAssign() {
        return assign;
    }

    public void setAssign(Pair assign) {
        this.assign = assign;
    }

}