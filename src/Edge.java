class Edge {
    private Node startNode;
    private Node endNode;
    private double weight;

    Edge(Node startNode, Node endNode, double weight){
        this.startNode = startNode;
        this.endNode = endNode;
        this.weight = weight;
    }

    Edge(Node startNode, Node endNode){
        this.startNode = startNode;
        this.endNode = endNode;
        weight = 0;
    }

    void setWeight(double weight){
        this.weight = weight;
    }

    double getWeight(){
        return weight;
    }

    Node getStartNode(){
        return startNode;
    }

    Node getEndNode(){
        return endNode;
    }
}
