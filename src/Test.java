import java.util.Scanner;

public class Test {
    Network network = new Network();
    void init(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter a word or gibberish with less than 45 characters: ");
        String input = scan.next();
        network.loadNetwork();
        newWord(input);
        network.proliferate();
        int guess = network.getResult();
        if(guess == 1){
            System.out.println("The algorithm thinks that " + input + " is an English word");
        }else{
            System.out.println("The algorithm thinks that " + input + " is not an English word");
        }
    }

    private void newWord(String string){
        string = string.replaceAll(" ","");
        if(string.length() > 45){
            string = string.substring(0,45);
        }
        string = string.toLowerCase();
        System.out.println(string);
        Layer l = new Layer();
        for(int i = 0; i < string.length(); ++i){
            Node n = new Node(string.charAt(i));
            l.appendNode(n);
        }
        network.newInputLayer(l);
    }
}
