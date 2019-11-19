package tree;

public class Tree {
    private Node root;

    public Tree (){
        this.root = null;
    }
    public void addRoot(Node n){
        if (n.getParent() ==null){
            this.root = n;
        } else {
            System.out.println("Failed to make Root - node has parent");
        }
    }
    public Node getRoot(){
        return this.root;
    }
    
}