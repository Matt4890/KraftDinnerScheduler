package tree;

public class Tree {
    private TreeNode root;

    public Tree (){
        this.root = null;
    }
    public void addRoot(TreeNode n){
        if (n.getParent() ==null){
            this.root = n;
        } else {
            // System.out.println("Failed to make Root - TreeNode has parent");
        }
    }
    public TreeNode getRoot(){
        return this.root;
    }
    
}