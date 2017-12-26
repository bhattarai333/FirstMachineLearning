import java.util.ArrayList;

class Layer {
    private ArrayList<Node> nodes;
    private int numberOfNodes;

    Layer() {
        nodes = new ArrayList<>();
    }

    ArrayList<Node> getNodes(){
        return nodes;
    }

    void appendNode(Node n){
        nodes.add(n);
        numberOfNodes++;
    }

    int getNumberOfNodes(){
        return numberOfNodes;
    }
}
