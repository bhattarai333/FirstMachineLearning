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

    void newInputLayer(Layer l){
        Layer firstLayer = layers.get(0);
        for(int i = 0; i < firstLayer.nodes.size(); ++i){
            try {
                firstLayer.nodes.get(i).setValue(l.nodes.get(i).getValue());
            }catch (Exception e){
                firstLayer.nodes.get(i).setValue(0);
            }
        }
        layers.set(0,firstLayer);
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
    int getResult(){
        Layer lastLayer = layers.get(layers.size()-1);
        int indexOfHighestNode = 0;
        int maxVal = Integer.MIN_VALUE;
        for(Node n : lastLayer.nodes){
            if(n.getValue() > maxVal){
                maxVal = n.getValue();
                indexOfHighestNode = lastLayer.nodes.indexOf(n);
            }
        }
        return indexOfHighestNode;
    }

    void randomChange(){
        int typeOfChange = get.randomWithRange(0,2);
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
        JSONObject networkObject = new JSONObject(get.getTextFromFile(path));
        decodeJSON(networkObject);
    }
    private void decodeJSON(JSONObject networkObject){
        JSONArray layersArray = networkObject.getJSONArray("Layers");
        JSONObject finalLayerObject = layersArray.getJSONObject(layersArray.length()-1);
        Layer finalLayer = new Layer();
        JSONArray finalNodes = finalLayerObject.getJSONArray("Nodes");
        for(int a = 0; a < finalNodes.length(); ++a){
            Node n = new Node();
            finalLayer.appendNode(n);
        }


        appendLayer(finalLayer);

        for(int i = layersArray.length() - 2; i >= 0; --i){
            JSONObject layerObject = layersArray.getJSONObject(i);
            Layer l = new Layer();
            JSONArray nodesArray = layerObject.getJSONArray("Nodes");
            for(int j = 0; j < nodesArray.length(); ++j){
                JSONObject nodeObject = nodesArray.getJSONObject(j);
                Node n = new Node();
                JSONArray edgesArray = nodeObject.getJSONArray("Edges");
                for(int k = 0; k < nodesArray.length(); ++k){
                    JSONObject edgeObject;
                    try {
                        edgeObject = edgesArray.getJSONObject(k);
                    }catch (Exception e){
                        continue;
                    }
                    int endNodeIndex = edgeObject.getInt("EndNodeNum");
                    Layer nextLayer = layers.get(0);
                    Node endNode = nextLayer.nodes.get(endNodeIndex);
                    int weight = edgeObject.getInt("Weight");
                    Edge e = new Edge(n,endNode,weight);
                    n.appendEdge(e);
                }
                l.appendNode(n);
            }
            layers.add(0,l);
            numberOfLayers++;
        }
    }

    void wipeNetwork(){
        layers = new ArrayList<>();
        numberOfLayers = 0;
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
                    int endNodeNumInLayer = layers.get(nextLayer).nodes.indexOf(endNode);
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
