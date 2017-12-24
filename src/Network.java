import java.util.ArrayList;

class Network {
    ArrayList<Layer> layers;
    private String path = "./Network/network.json";
    private int numberOfLayers;

    Network(){

        layers = new ArrayList<>();
    }

    void appendLayer(Layer l){
        layers.add(l);
        numberOfLayers++;
    }

    @Override
    public String toString(){
        String output = "";
        output = output + "Number of Layers: " + numberOfLayers + "\n";
        output = output + "Path: " + path + "\n";
        int i = 0;
        for (int ii = 0; ii < layers.size(); ++ii) {
            ++i;
            Layer l = layers.get(ii);
            output = output + "\nLayer " + i + ", " + l.getNumberOfNodes() + " node(s): " + "\n";
            int j = 0;
            for (Node n : l.nodes) {
                ++j;
                output = output + "Node " + j + ": " + n.getValue() + "  |  Connections: ";
                for(Edge e : n.edges){
                    int nodeNum;
                    Node endNode = e.getEndNode();
                    nodeNum = layers.get(ii+1).nodes.indexOf(endNode);
                    nodeNum = nodeNum + 1;
                    output = output + nodeNum + "(W:" + e.getWeight() + "), ";
                }
                output = output.substring(0, output.length() - 2);
                output = output + "\n";
            }
            output = output + "\n";
        }


        return output;
    }
}
