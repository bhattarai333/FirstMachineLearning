import java.util.ArrayList;
import java.util.Scanner;

class Main {
    Network network;
    String input;
    Layer lastLayer;

    void init(){
        getWord();
        createFirstLayer();
        createLastLayer();
    }

    private void createLastLayer(){
        lastLayer = new Layer();
        Node n0 = new Node();
        lastLayer.appendNode(n0);
        Node n1 = new Node();
        lastLayer.appendNode(n1);
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
