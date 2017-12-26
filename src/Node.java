import java.util.ArrayList;

public class Node {
    private ArrayList<Edge> edges;
    private int numberOfEdges;
    private int value;

    Node(){
        edges = new ArrayList<>();
    }

    Node(int value){
        edges = new ArrayList<>();
        this.value = value;
    }

    ArrayList<Edge> getEdges(){
        return edges;
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

    void deleteEdge(Edge e){
        edges.remove(e);
        numberOfEdges--;
    }

    int getNumberOfEdges() {
        return numberOfEdges;
    }
}
