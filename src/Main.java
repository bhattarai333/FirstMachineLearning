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
        System.out.println(network.toString());
    }

    private void createLastLayer(){
        Layer lastLayer = new Layer();
        Node n0 = new Node();
        lastLayer.appendNode(n0);
        Node n1 = new Node();
        lastLayer.appendNode(n1);
        network.appendLayer(lastLayer);

    }
    private void createHiddenLayers() {
        int hiddenLayersNumber = get.randomWithRange(1, input.length()*2);
        for(int i = 0; i < hiddenLayersNumber; ++i){
            int numberOfNodes = get.randomWithRange(input.length()/2,input.length()*2);
            Layer l = new Layer();
            for(int j = 0; j < numberOfNodes; ++j){
                Node n = new Node();
                l.appendNode(n);
            }
            network.appendLayer(l);
        }

    }
    private void createFirstLayer() {
        network = new Network();
        ArrayList<Layer> layers = new ArrayList<>();
        Layer firstLayer = new Layer();

        for(int i = 0; i < input.length(); ++i){
            Node n = new Node();
            n.setValue(input.charAt(i));
            firstLayer.appendNode(n);
        }

        network.appendLayer(firstLayer);
    }
    private void getWord(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter your word: ");
        input = scan.next();
    }
}
