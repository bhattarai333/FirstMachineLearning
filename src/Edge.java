public class Edge {
    private Node startNode;
    private Node endNode;
    private int weight;

    Edge(Node startNode, Node endNode, int weight){
        this.startNode = startNode;
        this.endNode = endNode;
        this.weight = weight;
    }

    Edge(Node startNode, Node endNode){
        this.startNode = startNode;
        this.endNode = endNode;
        weight = 0;
    }

    void setWeight(int weight){
        this.weight = weight;
    }

    int getWeight(){
        return weight;
    }

    Node getStartNode(){
        return startNode;
    }

    Node getEndNode(){
        return endNode;
    }
}
