import java.util.Scanner;

class Training {
    Network network = new Network();
    GetResources get = new GetResources();
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
        System.out.println("Percent Correct Words: " + correctWords + "%\nPercent Correct Non-Words: " + correctNonWords + "%");
        float totalCorrect = (correctNonWords + correctWords) / 2;
        System.out.println("Percent Correct: " + totalCorrect + "%");

        trainNetwork(totalCorrect,words,nonWords);
    }

    private void trainNetwork(float startingPercent, String[] words, String[] nonWords){
        //do random change to network
        //if random change improves percent keep it
        //if not revert change
        Network n1 = network;
        n1.randomChange();
        float correctPercentage = getCorrectPercentage(startingPercent,words,nonWords);
        System.out.println(correctPercentage + "% Continue? Y/N");
        Scanner scan = new Scanner(System.in);
        String s = scan.next().trim().toLowerCase();
        if(s.contains("n")){
            return;
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
            System.out.println(string);
            Layer l = new Layer();
            for(int i = 0; i < string.length(); ++i){
                Node n = new Node(string.charAt(i));
                l.appendNode(n);
            }
            network.newInputLayer(l);
            network.proliferate();
            int result = network.getResult();
            System.out.println(result);
            if(result == correctInteger){
                correctGuesses++;
            }
        }
        return (correctGuesses/arrayLength) * 100;
    }
}
