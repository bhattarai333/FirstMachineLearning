import java.util.ArrayList;

public class Node {
    ArrayList<Edge> edges;
    private int numberOfEdges;
    private double value;

    Node(){
        edges = new ArrayList<>();
    }

    Node(int value){
        edges = new ArrayList<>();
        this.value = value;
    }

    double getValue() {
        return value;
    }

    void setValue(double value) {
        //this.value = 1 / (1 + Math.exp(-value));
        setValueWithoutSigmoid(value);
    }

    void setValueWithoutSigmoid(double value){
        this.value = value;
    }

    void addValue(double val){
        setValue(value + val);
    }

    void appendEdge(Edge e){
        edges.add(e);
        numberOfEdges++;
    }

    public int getNumberOfEdges() {
        return numberOfEdges;
    }
}
