import java.util.ArrayList;

class Layer {
    ArrayList<Node> nodes;
    private int numberOfNodes;

    Layer() {
        nodes = new ArrayList<>();
    }

    void appendNode(Node n){
        nodes.add(n);
        numberOfNodes++;
    }

    int getNumberOfNodes(){
        return numberOfNodes;
    }
}
