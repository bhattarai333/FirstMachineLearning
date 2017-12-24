import java.util.ArrayList;

public class Node {
    ArrayList<Edge> edges;
    private int numberOfEdges;
    private int value;

    Node(){
        edges = new ArrayList<>();
    }

    int getValue() {
        return value;
    }

    void setValue(int value) {
        this.value = value;
    }

    void addValue(int val){
        value = value + val;
    }

    void appendEdge(Edge e){
        edges.add(e);
        numberOfEdges++;
    }

    public int getNumberOfEdges() {
        return numberOfEdges;
    }
}
