import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

class Network {
    GetResources get = new GetResources();
    private ArrayList<Layer> layers;
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
        for(int i = 0; i < firstLayer.getNodes().size(); ++i){
            try {
                firstLayer.getNodes().get(i).setValue(l.getNodes().get(i).getValue());
            }catch (Exception e){
                firstLayer.getNodes().get(i).setValue(0);
            }
        }
        layers.set(0,firstLayer);
    }
    void proliferate(){
        for(int i = 0; i < layers.size() - 1; ++i){
            Layer l = layers.get(i);
            for(int j = 0; j < l.getNodes().size(); ++j){
                Node n = l.getNodes().get(j);
                for(int k = 0; k < n.getEdges().size(); ++k){
                    Edge e = n.getEdges().get(k);
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
        for(Node n : lastLayer.getNodes()){
            if(n.getValue() > maxVal){
                maxVal = n.getValue();
                indexOfHighestNode = lastLayer.getNodes().indexOf(n);
            }
        }
        return indexOfHighestNode;
    }
    private Layer getRandomLayer(){
        int layerNum = get.randomWithRange(0,layers.size()-1);
        return layers.get(layerNum);
    }
    private Layer getRandomHiddenLayer(){
        int layerNum = get.randomWithRange(1,layers.size()-2);
        return layers.get(layerNum);
    }
    private Node getRandomNode(){
        Layer l = getRandomLayer();
        int nodeNum = get.randomWithRange(0, l.getNodes().size()-1);
        return l.getNodes().get(nodeNum);
    }
    private Node getRandomHiddenNode(){

        Layer l = getRandomHiddenLayer();
        int nodeNum = get.randomWithRange(0, l.getNodes().size()-1);
        return l.getNodes().get(nodeNum);
    }
    private Edge getRandomEdge(){
        Node n = getRandomNode();
        int edgeNum = get.randomWithRange(0, n.getEdges().size()-1);
        try {
            return n.getEdges().get(edgeNum);
        }catch (Exception e){
            return getRandomEdge();
        }
    }
    private Edge getRandomHiddenEdge(){

        Node n = getRandomHiddenNode();
        int edgeNum = get.randomWithRange(0, n.getEdges().size()-1);
        return n.getEdges().get(edgeNum);
    }
    ArrayList<Layer> getLayers(){
        return layers;
    }

    void randomChange(){
        int typeOfChange = get.randomWithRange(0,4);
        if(typeOfChange == 0){
            changeEdgeWeight();
        }else if(typeOfChange == 1){
            deleteEdge();
        }else if(typeOfChange == 2){
            addEdge();
        }else if(typeOfChange == 3){
            deleteNode();
        }else{
            addNode();
        }
    }
    private void changeEdgeWeight(){
        Edge e = getRandomEdge();
        e.setWeight(get.randomWithRange(0,2));
    }
    private void deleteEdge(){
        Node n = getRandomNode();
        int edgeNum = get.randomWithRange(0,n.getEdges().size()-1);
        Edge e;
        try {
            e = n.getEdges().get(edgeNum);
        }catch(Exception ex){
            return;
        }
        n.deleteEdge(e);
    }
    private void addEdge(){
        Node n = getRandomHiddenNode();
        Node n2 = getRandomHiddenNode();
        Edge e = new Edge(n,n2,get.randomWithRange(0,2));
        n.appendEdge(e);
    }
    private void deleteNode(){
        //implement later
    }
    private void addNode(){
        //implement later
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
            for (Node n : l.getNodes()) {
                ++nodeCounter;
                output = output + "Node " + nodeCounter + ": " + n.getValue() + "  |  Connections: ";
                for(Edge e : n.getEdges()){
                    int nodeNum;
                    Node endNode = e.getEndNode();
                    nodeNum = layers.get(i + 1).getNodes().indexOf(endNode);
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
                    Node endNode = nextLayer.getNodes().get(endNodeIndex);
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
            for(Node n : l.getNodes()){
                JSONObject nodeObject =  new JSONObject();
                nodeObject.accumulate("NumEdges",n.getNumberOfEdges());
                JSONArray edgesArray = new JSONArray();
                for(Edge e : n.getEdges()){
                    JSONObject edgeObject = new JSONObject();
                    edgeObject.accumulate("Weight",e.getWeight());
                    Node endNode = e.getEndNode();
                    int nextLayer = layers.indexOf(l) + 1;
                    int endNodeNumInLayer = layers.get(nextLayer).getNodes().indexOf(endNode);
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
