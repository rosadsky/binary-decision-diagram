public class Node {
    String kluc;
    Node left, right, parent;
    int level;
    boolean isReduc = false;


    public Node(String kluc, Node left, Node right, Node parent, int level) {
        this.kluc = kluc;
        this.left = left;
        this.right = right;
        this.parent = parent;
        this.level = level;
        boolean isReduc = false;
    }

    public Node() {


    }
}
