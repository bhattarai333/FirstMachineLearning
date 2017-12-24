import java.io.*;
import java.util.ArrayList;
import org.json.*;

class Network {
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

    @Override
    public String toString(){
        String output = "";
        output = output + "Number of Layers: " + numberOfLayers + "\n";
        output = output + "Path: " + path + "\n";
        int i = 0;
        for (Layer l : layers) {
            ++i;
            output = output + "\nLayer " + i + ", " + l.getNumberOfNodes() + " node(s): " + "\n";
            int j = 0;
            for (Node n : l.nodes) {
                ++j;
                output = output + "Node " + j + ": " + n.getValue() + "\n";
            }
            output = output + "\n";
        }


        return output;
    }
}
