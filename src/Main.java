import java.util.ArrayList;
import java.util.Scanner;

class Main {
    private GetResources get = new GetResources();
    private Network network;
    private String input;

    void init(){
        getWord();
        createFirstLayer();
        createHiddenLayers();
        createLastLayer();
        linkNodes();
        System.out.println(network.toString());
    }

    private void linkNodes(){
        //create edges between nodes throughout the network

        //implement this soon

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
        int hiddenLayersNumber = get.randomWithRange(1, input.length()*2);                  // decide how many hidden layers there will be
        for(int i = 0; i < hiddenLayersNumber; ++i){
            int numberOfNodes = get.randomWithRange(input.length()/2,input.length()*2);     // determine how many nodes will be within each layer
            Layer l = new Layer();
            for(int j = 0; j < numberOfNodes; ++j){
                Node n = new Node();
                l.appendNode(n);                                                            // add node to layer
            }
            network.appendLayer(l);                                                         // add layer to network
        }

    }
    private void createFirstLayer() {
        network = new Network();
        ArrayList<Layer> layers = new ArrayList<>();    // arraylist of layers to store in network
        Layer firstLayer = new Layer();                 // create input layer

        //create input layer where each node corresponds to a character in the string
        for(int i = 0; i < input.length(); ++i){
            Node n = new Node();
            n.setValue(input.charAt(i));
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
