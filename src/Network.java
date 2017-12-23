import java.io.*;
import java.util.ArrayList;
import org.json.*;

class Network {
    private ArrayList<Layer> layers;
    private String path = "./Network/network.json";

    Network(){

        layers = new ArrayList<>();
    }

    void appendLayer(Layer l){
        layers.add(l);
    }

    void saveNetwork(){
        String content = "";
        for (Layer layer : layers) {
            JSONObject o = new JSONObject(layer);
            content = content + "_" + o;
        }
        System.out.println(content);

    }
}
