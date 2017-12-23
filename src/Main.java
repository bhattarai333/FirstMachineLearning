import java.util.ArrayList;
import java.util.Scanner;

class Main {
    void init(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter your word: ");
        String input = scan.next();

        Network network = new Network();
        ArrayList<Layer> layers = new ArrayList<>();
        Layer firstLayer = new Layer();

        for(int i = 0; i < input.length(); ++i){
            Node n = new Node();
            firstLayer.appendNode(n);
        }

    }
}
