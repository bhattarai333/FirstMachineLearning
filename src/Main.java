import java.util.ArrayList;
import java.util.Scanner;

class Main {
    private void result(){
        if(network.layers.get(network.layers.size()-1).nodes.get(0).getValue()>network.layers.get(network.layers.size()-1).nodes.get(1).getValue()){
            System.out.println("Is word");
        }else{
            System.out.println("Is not word");
        }
    }
    private GetResources get = new GetResources();
    private Network network;
    private String input;
    private int alphaVal = 2;
    void init(){
        getWord();
        createFirstLayer();
        createHiddenLayers();
        createLastLayer();
        linkNodes();
        network.proliferate();
        System.out.println(network);
        network.saveNetwork();
        network.wipeNetwork();
        network.loadNetwork();
        System.out.println(network);



        result();
    }


    private void linkNodes(){
        //create edges between nodes throughout the network

        for(int i = 0; i < network.layers.size()-1; ++i){
            Layer l = network.layers.get(i);
            Layer nextLayer = network.layers.get(i+1);
            for(int j = 0; j < l.nodes.size(); ++j){
                Node n = l.nodes.get(j);
                for(int k = 0; k < nextLayer.nodes.size(); ++k){
                    int rand = get.randomWithRange(0,5);
                    if(rand > alphaVal){
                        Edge e = new Edge(n,nextLayer.nodes.get(k),get.randomWithRange(0,alphaVal));
                        n.appendEdge(e);
                    }
                }

            }
        }

    }
    private void createLastLayer(){
        Layer lastLayer = new Layer();

        //Node for false
        Node n0 = new Node();
        lastLayer.appendNode(n0);

        //Node for true
        Node n1 = new Node();
        lastLayer.appendNode(n1);

        //Add output layer to network
        network.appendLayer(lastLayer);

    }
    private void createHiddenLayers() {
        int hiddenLayersNumber = get.randomWithRange(1, input.length()*alphaVal);                           // decide how many hidden layers there will be
        for(int i = 0; i < hiddenLayersNumber; ++i){
            int numberOfNodes = get.randomWithRange(input.length()/alphaVal,input.length()*alphaVal);       // determine how many nodes will be within each layer
            Layer l = new Layer();
            for(int j = 0; j < numberOfNodes; ++j){
                Node n = new Node();
                l.appendNode(n);                                                                            // add node to layer
            }
            network.appendLayer(l);                                                                         // add layer to network
        }

    }
    private void createFirstLayer() {
        network = new Network();
        ArrayList<Layer> layers = new ArrayList<>();    // arraylist of layers to store in network
        Layer firstLayer = new Layer();                 // create input layer

        //create input layer where each node corresponds to a character in the string
        for(int i = 0; i < input.length(); ++i){
            Node n = new Node();
            int nodeVal = input.charAt(i);
            n.setValueWithoutSigmoid((double) nodeVal);
            firstLayer.appendNode(n);
        }

        network.appendLayer(firstLayer);                // add input layer to network
    }
    private void getWord(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter your word: ");
        input = scan.next();
    }
}
