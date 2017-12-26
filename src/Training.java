import java.util.Scanner;

class Training {
    Network network = new Network();
    GetResources get = new GetResources();
    int counter;
    void init() {
        network.loadNetwork();
        String[] words;
        String[] nonWords;

        String wordFile = get.getTextFromFile("./Network/words.txt");
        String nonWordFile = get.getTextFromFile("./Network/gibberish.txt");

        words = wordFile.split("\n");
        nonWords = nonWordFile.split("\n");
        float correctWords = doGuesses(words, 1);
        float correctNonWords = doGuesses(nonWords, 0);
       // System.out.println("Percent Correct Words: " + correctWords + "%\nPercent Correct Non-Words: " + correctNonWords + "%");
        float totalCorrect = (correctNonWords + correctWords) / 2;
       // System.out.println("Percent Correct: " + totalCorrect + "%");

        counter = 0;
        trainNetwork(totalCorrect,words,nonWords);
        Scanner scan = new Scanner(System.in);
        while(true) {
            System.out.println("Enter word that you want to guess: ");
            String word = scan.next();
            word = word.replaceAll(" ", "");
            if (word.length() > 45) {
                word = word.substring(0, 45);
            }
            word = word.toLowerCase();
            //System.out.println(string);
            Layer l = new Layer();
            for (int i = 0; i < word.length(); ++i) {
                Node n = new Node(word.charAt(i));
                l.appendNode(n);
            }
            network.newInputLayer(l);
            network.proliferate();
            int result = network.getResult();
            if (result == 0) {
                System.out.println("The neural network thinks that '" + word + "' is not a word");
            } else {
                System.out.println("The neural network thinks that '" + word + "' is a word");
            }
            System.out.println("Try another word? Y/N");
            String res = scan.next().trim().toLowerCase();
            if (res.contains("n")) {
                break;
            }
        }

    }

    private void trainNetwork(float startingPercent, String[] words, String[] nonWords){
        //do random change to network
        //if random change improves percent keep it
        //if not revert change
        Network n1 = network;
        n1.randomChange();
        float correctPercentage = getCorrectPercentage(startingPercent,words,nonWords);
        System.out.println("Iteration: "+counter+" Highest Percentage: " + startingPercent + "%");
        counter++;
        if(counter > 1000) {
            System.out.println("Continue? Y/N");
            Scanner scan = new Scanner(System.in);
            String s = scan.next().trim().toLowerCase();
            if (s.contains("n")) {
                return;
            }else{
                counter = 0;
            }
        }

        if(correctPercentage > startingPercent){
            network = n1;
            trainNetwork(correctPercentage,words,nonWords);
        }else{
            trainNetwork(startingPercent,words,nonWords);
        }
    }

    private float getCorrectPercentage(float startingPercent, String[] words, String[] nonWords){
        float correctWords = doGuesses(words,1);
        float correctNonWords = doGuesses(nonWords, 0);
        return (correctNonWords + correctWords)/2;
    }

    private float doGuesses(String[] array, int correctInteger){
        float arrayLength = array.length;
        float correctGuesses = 0;
        for(String string : array){
            string = string.replaceAll(" ","");
            if(string.length() > 45){
                string = string.substring(0,45);
            }
            string = string.toLowerCase();
            //System.out.println(string);
            Layer l = new Layer();
            for(int i = 0; i < string.length(); ++i){
                Node n = new Node(string.charAt(i));
                l.appendNode(n);
            }
            network.newInputLayer(l);
            network.proliferate();
            int result = network.getResult();
            //System.out.println(result);
            if(result == correctInteger){
                correctGuesses++;
            }
        }
        return (correctGuesses/arrayLength) * 100;
    }
}
