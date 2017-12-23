import java.util.ArrayList;

class Layer {
    private ArrayList<Node> nodes;

    Layer() {
        nodes = new ArrayList<>();
    }

    void appendNode(Node n){
        nodes.add(n);
    }
}
