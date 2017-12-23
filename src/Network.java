import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.json.*;

class Network {
    private ArrayList<Layer> layers;

    void appendLayer(Layer l){
        layers.add(l);
    }

    void loadNetwork(){
        String networkString = readNetwork();
    }
    private String readNetwork(){
        //add this tomo
        return "";
    }
    void saveNetwork(){
        JSONObject jsonObject = new JSONObject(layers);
        String jsonObjectString = jsonObject.toString();
        writeNetwork(jsonObjectString);
    }
    private void writeNetwork(String content){
        try {
            String[] lines = content.split("\n");
            File f = new File("./Network/");
            FileWriter fw = new FileWriter(f.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            String sep = System.getProperty("line.separator");
            for (String s : lines){
                fw.write(s);
                fw.write(sep);
            }
            bw.close();
        }catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}
