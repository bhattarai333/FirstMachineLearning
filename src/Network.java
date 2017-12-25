import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

class Network {
    GetResources get = new GetResources();
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

    void proliferate(){
        for(int i = 0; i < layers.size() - 1; ++i){
            Layer l = layers.get(i);
            for(int j = 0; j < l.nodes.size(); ++j){
                Node n = l.nodes.get(j);
                for(int k = 0; k < n.edges.size(); ++k){
                    Edge e = n.edges.get(k);
                    Node endNode = e.getEndNode();
                    endNode.addValue(n.getValue()*e.getWeight());
                }
            }
        }
    }

    @Override
    public String toString(){
        String output = "";
        output = output + "Number of Layers: " + numberOfLayers + "\n";
        output = output + "Path: " + path + "\n";
        int layerCounter = 0;
        for (int i = 0; i < layers.size(); ++i) {
            ++layerCounter;
            Layer l = layers.get(i);
            output = output + "\nLayer " + layerCounter + ", " + l.getNumberOfNodes() + " node(s): " + "\n";
            int nodeCounter = 0;
            for (Node n : l.nodes) {
                ++nodeCounter;
                output = output + "Node " + nodeCounter + ": " + n.getValue() + "  |  Connections: ";
                for(Edge e : n.edges){
                    int nodeNum;
                    Node endNode = e.getEndNode();
                    nodeNum = layers.get(i+1).nodes.indexOf(endNode);
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



    void loadNetwork(){
        JSONObject networkObject = loadFile();
        System.out.println(networkObject);
    }
    private JSONObject loadFile(){
        String s = get.getTextFromFile(path);
        return new JSONObject(s);
    }

    void saveNetwork(){
        get.makeDirectory("./Network/");
        JSONObject networkJSON = createJSON();
        get.writeFile(networkJSON.toString(),path);

    }
    private JSONObject createJSON(){
        JSONObject networkObject = new JSONObject();
        networkObject.accumulate("NumLayers",numberOfLayers);
        networkObject.accumulate("Path", path);
        JSONArray layersArray = new JSONArray();
        for(Layer l : layers){
            JSONObject layerObject = new JSONObject();
            layerObject.accumulate("NumNodes",l.getNumberOfNodes());
            JSONArray nodesArray = new JSONArray();
            for(Node n : l.nodes){
                JSONObject nodeObject =  new JSONObject();
                nodeObject.accumulate("NumEdges",n.getNumberOfEdges());
                JSONArray edgesArray = new JSONArray();
                for(Edge e : n.edges){
                    JSONObject edgeObject = new JSONObject();
                    edgeObject.accumulate("Weight",e.getWeight());
                    Node endNode = e.getEndNode();
                    int nextLayer = layers.indexOf(l) + 1;
                    int endNodeNumInLayer = layers.get(nextLayer).nodes.indexOf(endNode) + 1;
                    edgeObject.accumulate("EndNodeNum", endNodeNumInLayer);
                    edgesArray.put(edgeObject);
                }
                nodeObject.put("Edges",edgesArray);
                nodesArray.put(nodeObject);
            }
            layerObject.put("Nodes",nodesArray);
            layersArray.put(layerObject);
        }
        networkObject.put("Layers",layersArray);
        return networkObject;
    }

}
