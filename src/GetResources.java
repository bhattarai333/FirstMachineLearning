import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Write a description of class getResources here.
 *
 * @author Josh Bhattarai and Friends
 * @version 1?
 */
class GetResources
{
    public static boolean isInteger(String s, int radix) {
        Scanner sc = new Scanner(s.trim());
        if(!sc.hasNextInt(radix)) return false;
        // we know it starts with a valid int, now make sure
        // there's nothing left!
        sc.nextInt(radix);
        return !sc.hasNext();
    }

    BufferedImage getImg(String path){
        //returns BufferedImage of a png file with the file name path in sprites folder
        if(path.contains("bhattarai333.github.io")){
            try{
                return getImgFromGithub(path);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        BufferedImage img = null;
        try{
            img = ImageIO.read(this.getClass().getResource("/Resources/Sprites/" + path + ".png"));//gets image from file path
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    private BufferedImage getImgFromGithub(String url) throws MalformedURLException{
        URL obj = new URL(url);
        BufferedImage img = null;
        try {
            img = ImageIO.read(obj);
        }catch(Exception e){
            //https://bhattarai333.github.io/Websites/Resources/Sprites/1.png
            String[] parts = url.split("Sprites");
            String newPath = parts[0] + "Sprites972" + parts[1];
            URL newURL = new URL(newPath);
            try{
                img = ImageIO.read(newURL);
            }catch(Exception ex){
                ex.printStackTrace();
                e.printStackTrace();
            }
        }
        return img;
    }

    ArrayList<BufferedImage> cutSheet(String path, int cols, int rows){
        BufferedImage spriteSheet = getImg(path);
        double sheetHeight = spriteSheet.getHeight();
        double sheetWidth = spriteSheet.getWidth();
        ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
        double width = sheetWidth/cols;
        double height = sheetHeight/rows;
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                images.add ( spriteSheet.getSubimage(
                        (int)(j * width + 1),
                        (int)(i * height + 1),
                        (int)(width - 1),
                        (int)(height - 1)
                ));
            }
        }
        return images;
    }

    Font getFont(String filename, float fontSize) {
        if(filename.contains("bhattarai333.github.io")){
            try {
                return getFontFromGithub(filename, fontSize);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        Font myfont = null;
        Font myfontReal = null;
        try {
            InputStream is = new BufferedInputStream(this.getClass().getResourceAsStream("/Resources/Fonts/" + filename+".ttf"));

            myfont = Font.createFont(Font.TRUETYPE_FONT, is);
            myfontReal = myfont.deriveFont(fontSize);
        } catch (FontFormatException | IOException e) {
            System.out.println(e.getMessage());
        }
        return myfontReal;
    }

    private Font getFontFromGithub(String url, float fontsize) throws Exception{
        Font myfont;
        Font myfontReal;
        URL fonturl = new URL(url);

        myfont = Font.createFont(Font.TRUETYPE_FONT, fonturl.openStream());


        myfontReal = myfont.deriveFont(fontsize);
        return myfontReal;
    }

    BufferedImage getScreenShot(Component c) {
        //jframe.getContentPane()
        BufferedImage img = new BufferedImage(c.getWidth(),c.getHeight(),BufferedImage.TYPE_INT_RGB);
        c.paint( img.getGraphics() );
        return img;
    }

    void saveImg(BufferedImage image){
        saveImg(image,"./Screens/screenshot0t.png");
    }

    String saveImg(BufferedImage img,String path){
        String path2 = "";
        try{
            //noinspection ResultOfMethodCallIgnored
            new File("./Screens").mkdir();

            path2 = iterate(path,0);
            File outputfile = new File(path2);
            ImageIO.write(img, "png", outputfile);
        }catch(IOException e){
            e.printStackTrace();
        }
        return path2;
    }

    private String iterate(String path, int d){
        //part of saveImg
        File f = new File(path);
        String path2 = path;
        int i = d;
        if(f.exists() && !f.isDirectory()) {
            String[] tokens = path2.split("t");
            i=i+1;
            path2 = tokens[0]+"t" + i + "t"+tokens[2];
            return (iterate(path2, i));
        }else{
            return path2;
        }
    }

    void writeText(String content, String path){
        try {
            String[] lines = content.split("\n");
            File f = new File(path);
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

    public static ArrayList<Component> getAllComponents(final Container c) {
        Component[] comps = c.getComponents();
        ArrayList<Component> compList = new ArrayList<Component>();
        for (Component comp : comps) {
            compList.add(comp);
            if (comp instanceof Container)
                compList.addAll(getAllComponents((Container) comp));
        }
        return compList;
    }

    public static void deleteAllComponents(JFrame f){
        ArrayList<Component> components = getAllComponents(f);
        for(Component c : components){
            f.remove(c);
        }
    }

    int randomWithRange(int min, int max)
    {
        int range = Math.abs(max - min) + 1;
        return (int)(Math.random() * range) + (min <= max ? min : max);
    }

    BufferedImage getScaledImg(BufferedImage img, double percent){
        percent = percent/100.0;
        int newWidth = (int) (img.getWidth() * percent);
        int newHeight = (int) (img.getHeight() * percent);
        return getSizedImg(img,newWidth,newHeight);
    }

    BufferedImage getSizedImg(BufferedImage otherImage,int width,int height){
        BufferedImage outputImg = otherImage;
        try{
            outputImg = resizeUsingJavaAlgo(otherImage,width,height);
        }catch(IOException e){
            e.printStackTrace();
        }
        return outputImg;
    }

    private BufferedImage resizeUsingJavaAlgo(BufferedImage image, int width, int height) throws IOException {
        //part of getSizedImg thanks to StackOverflow user Mladen Adamovic
        double ratio = (double) image.getWidth()/ image.getHeight();
        if (width < 1) {
            width = (int) (height * ratio + 0.4);
        } else if (height < 1) {
            height = (int) (width /ratio + 0.4);
        }

        Image scaled = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage bufferedScaled = new BufferedImage(scaled.getWidth(null), scaled.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedScaled.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.drawImage(scaled, 0, 0, width, height, null);
        return bufferedScaled;
    }

    public String getTextFromFile(String path){
        File f = new File(path);
        return getTextFromFile(f);
    }

    String getTextFromFile(File f){
        BufferedReader br = null;
        String output = "";
        try {


            br = new BufferedReader(new FileReader(f.toPath().toString()));

            output = bufferedReaderToString(br);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return output;
    }

    String bufferedReaderToString(BufferedReader br){
        String sCurrentLine;
        String output = "";
        try {
            while ((sCurrentLine = br.readLine()) != null) {
                output = output + sCurrentLine;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return output;
    }

    String getProgramPath(){
        File f = new File("./Resources/Sprites/blank.png");
        String s = f.getAbsolutePath();
        String[] stringArr = s.split("Resources");
        String s2 = stringArr[0];
        s2 = s2.substring(0, s2.length() - 3);
        return s2;
    }

    void writeFile(String content, String path){
        try{
            File file = new File(path);
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    void makeDirectory(String path){
        File f = new File(path);
        if(!f.exists()){
            //noinspection ResultOfMethodCallIgnored
            f.mkdir();
        }
    }

    void deleteFile(String path){
        File f = new File(path);
        if(!f.isDirectory()){
            //noinspection ResultOfMethodCallIgnored
            f.delete();
        }
    }

    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})//please god be careful with this function
    private void purgeDirectory(String path){
        //BE VERY VERY VERY CAREFUL WITH THIS SHIT PLEASE DON'T DELETE EVERYTHING AAAAAAAA
        File dir = new File(path);
        for (File file: dir.listFiles()) {
            if (file.isDirectory()) purgeDirectory(file.toPath().toString());//recursively delete all folders in the folder
            file.delete();
        }
    }

    static String httpGet(String url,String userAgent){
        try{
            return(httpRest(url,userAgent,"GET"));
        }catch(Exception e){
            e.printStackTrace();
            return "Fail";
        }
    }

    static String httpPost(String url, String urlParameters, String userAgent){
        try {
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", userAgent);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            return response.toString();
        }catch(Exception e){
            e.printStackTrace();
            return "Fail";
        }
    }

    static private String httpRest(String url, String userAgent, String requestType) throws Exception{
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod(requestType);

        //add request header
        con.setRequestProperty("User-Agent", userAgent);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println("Complete");
        return(response.toString());
    }

    private static void openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void openWebpage(String urlString) {
        try {
            URL url = new URL(urlString);
            openWebpage(url.toURI());
        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    int bind(int min, int val, int max) {
        return Math.max(min,Math.min(max,val));
    }

    BufferedImage applyGaussianBlur (BufferedImage image, int kernelWidth, int kernelHeight){

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        int[][] pixels = new int[imageWidth][imageHeight];

        for(int i = 0; i < imageWidth; ++i){
            for(int j = 0; j < imageHeight; ++j){
                pixels[i][j] = image.getRGB(i, j);
            }
        }

        for(int i = 0; i < imageWidth; ++i){
            for(int j = 0; j < imageHeight; ++j){
                long redBucket = 0;
                long greenBucket = 0;
                long blueBucket = 0;
                int count = 0;

                for(int w = -kernelWidth; w < kernelWidth; ++w){
                    for(int h = -kernelHeight; h < kernelHeight; ++h){
                        if(i+w > 0 && j+h > 0 && i+w < imageWidth && j+h < imageHeight){
                            if(pixels[i+w][j+h] != 0) {
                                Color temp = new Color(pixels[i + w][j + h]);
                                redBucket += temp.getRed();
                                greenBucket += temp.getGreen();
                                blueBucket += temp.getBlue();
                                ++count;
                            }
                        }
                    }
                }
                System.out.println(i+":"+j);
                if (count != 0) {
                    Color blurredPixel = new Color(redBucket / count, greenBucket / count, blueBucket / count);
                    image.setRGB(i, j, blurredPixel.getRGB());
                }


            }
        }

        return image;
    }

    BufferedImage flipImage(BufferedImage image, char direction){
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage outputImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        Graphics g = outputImage.getGraphics();

        if(direction == 'h'){
            g.drawImage(image,width,0,-width,height,null);
        }else if (direction == 'v'){
            g.drawImage(image,0,height,width,-height,null);
        }else{
            outputImage = image;
        }
        return outputImage;
    }

    private BufferedImage getOutline(BufferedImage image, Color c){
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage outline = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for(int i = 0; i < width; ++i){
            for(int j = 0; j < height; ++j){
                if(!(image.getRGB(i,j) >> 24 == 0x00)){
                    outline.setRGB(i,j, c.getRGB());
                }
            }
        }
        return outline;
    }
    private void drawOutline(BufferedImage image, BufferedImage outline, Graphics g, int thickness, float opacity, char alignment){
        if(alignment == '1'){
            for(int i = 0; i < thickness; ++i){
                Graphics2D g2 = (Graphics2D)g;
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                g2.drawImage(outline,thickness-i,thickness-i,null);
                g2.drawImage(outline,thickness-i,thickness,null);
                g2.drawImage(outline,thickness,thickness-i,null);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
                g.drawImage(image,thickness,thickness,null);
            }

        }else if(alignment == 't'){

        }else if(alignment == '2'){
            for(int i = 0; i < thickness; ++i){
                Graphics2D g2 = (Graphics2D)g;
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                g2.drawImage(outline,thickness+i,thickness-i,null);
                g2.drawImage(outline,thickness+i,thickness,null);
                g2.drawImage(outline,thickness,thickness-i,null);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
                g.drawImage(image,thickness,thickness,null);
            }

        }else if(alignment == 'l'){

        }else if(alignment == 'r'){

        }else if(alignment == '3'){
            for(int i = 0; i < thickness; ++i){
                Graphics2D g2 = (Graphics2D)g;
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                g2.drawImage(outline,thickness-i,thickness+i,null);
                g2.drawImage(outline,thickness-i,thickness,null);
                g2.drawImage(outline,thickness,thickness+i,null);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
                g.drawImage(image,thickness,thickness,null);
            }

        }else if (alignment == 'b'){

        }else if (alignment == '4'){
            for(int i = 0; i < thickness; ++i){
                Graphics2D g2 = (Graphics2D)g;
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                g2.drawImage(outline,thickness+i,thickness+i,null);
                g2.drawImage(outline,thickness+i,thickness,null);
                g2.drawImage(outline,thickness,thickness+i,null);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
                g.drawImage(image,thickness,thickness,null);
            }

        }else{
            for(int i = 0; i < thickness; ++i){
                g.drawImage(outline,thickness+i,thickness+i,null);
                g.drawImage(outline,thickness+i,thickness,null);
                g.drawImage(outline,thickness,thickness+i,null);
                g.drawImage(outline,thickness-i,thickness-i,null);
                g.drawImage(outline,thickness-i,thickness,null);
                g.drawImage(outline,thickness,thickness-i,null);
                g.drawImage(image,thickness,thickness,null);
            }
        }
    }

    BufferedImage dropShadow(BufferedImage image, int thickness,Color c, char alignment){
        if(thickness <= 0){
            return image;
        }
        int width = image.getWidth();
        int height = image.getHeight();

        int newWidth = width + (thickness * 2);
        int newHeight = height + (thickness * 2);

        BufferedImage output = new BufferedImage(newWidth,newHeight,BufferedImage.TYPE_INT_ARGB);

        BufferedImage outline = getOutline(image,c);

        Graphics g = output.getGraphics();

        drawOutline(image,outline,g,thickness,.1f,alignment);


        return output;
    }

    //overloads
    BufferedImage giveBorder(BufferedImage image){
        return giveBorder(image,5);
    }
    BufferedImage giveBorder(BufferedImage image, int thickness){
        return giveBorder(image,thickness,Color.BLACK);//laaaamooo
    }
    BufferedImage giveBorder(BufferedImage image, int thickness, String color){
        return giveBorder(image,thickness,Color.decode(color));
    }

    BufferedImage giveBorder(BufferedImage image,int thickness, Color c){
        int width = image.getWidth();
        int height = image.getHeight();

        int newWidth = width + (thickness * 2);
        int newHeight = height + (thickness * 2);

        BufferedImage output = new BufferedImage(newWidth,newHeight,BufferedImage.TYPE_INT_ARGB);

        BufferedImage outline = getOutline(image,c);

        Graphics g = output.getGraphics();
        drawOutline(image,outline,g,thickness,1f,'c');

        return output;
    }

    //overloads
    void drawText(String stringVal, int x, int y, int width, int height,Graphics g){

        String stringColor ="#"+Integer.toHexString(g.getColor().getRGB()).substring(2);
        int thickness = 0;
        Font f = g.getFont();
        char alignment = 'c';

        drawText(stringVal,stringColor, stringColor,x,y,width,height,g,thickness,Integer.MAX_VALUE,0,f,alignment);

    }
    void drawText(String stringVal, int x, int y, int width, int height, Graphics g, char alignment) {
        String stringColor ="#"+Integer.toHexString(g.getColor().getRGB()).substring(2);
        drawText(stringVal,stringColor, stringColor,x,y,width,height,g,0,Integer.MAX_VALUE,0,g.getFont(),alignment);
    }
    void drawText(String stringVal, String fontColor, String outlineColor, int x, int y, int width, int height, Graphics g, int outlineThickness, int maxFontSize, int minFontSize, Font f) {
        drawText(stringVal,fontColor,outlineColor,x,y,width,height,g,outlineThickness,maxFontSize,minFontSize,f,'c');
    }
    void drawText(String stringVal, String fontColor, String outlineColor, int x, int y, int width, int height, Graphics g, int outlineThickness, Font cst) {
        drawText(stringVal,fontColor,outlineColor,x,y,width,height,g,outlineThickness,Integer.MAX_VALUE,0,cst,'c');
    }
    FontMetrics getSizedFont(FontMetrics fm, int maxWidth, int maxHeight, String str, Graphics g) {
        return getSizedFont(fm,maxWidth,maxHeight,str,g,0);
    }

    void drawTextToScreen(String stringVal, String stringColor, String outlineColor, int X, int Y, Graphics g, int thickness){
        g.setColor(Color.decode(outlineColor));
        for(int i = 1; i <= thickness; ++i) {
            //123
            //456
            //789

            g.drawString(stringVal,X-i,Y-i);//1
            g.drawString(stringVal,X,Y-i);//2
            g.drawString(stringVal,X+i,Y-i);//3
            g.drawString(stringVal,X-i,Y);//4
            g.drawString(stringVal,X+i,Y);//6
            g.drawString(stringVal,X-i,Y+i);//7
            g.drawString(stringVal,X,Y+i);//8
            g.drawString(stringVal,X+i,Y+i);//9
        }
        g.setColor(Color.decode(stringColor));
        g.drawString(stringVal,X,Y);//5


    }
    void drawText(String stringVal, String stringColor, String outlineColor, int x, int y, int width, int height, Graphics g, int thickness, int maxFontSize, int minFontSize, Font f, char alignment){
        if(minFontSize < 0){
            //make sure font can't turn upside down
            minFontSize = 0;
        }

        int fontSize = f.getSize();//get font size from parameter f
        fontSize = bind(minFontSize,fontSize,maxFontSize);//make sure it doesn't exceed the bounds
        f = f.deriveFont( (float) fontSize);//set the new font size to f

        FontMetrics fm = g.getFontMetrics(f);//make font metrics with resized f
        fm = getSizedFont(fm,width,height,stringVal,g,thickness);//recursively obtain the largest font size for bounds
        g.setFont(fm.getFont());//set the new font to graphics

        String[] lines = stringVal.split("  ");//split for double space, double space being new line

        height = height / lines.length;
        /*
            By splitting the height per line, we're making individual boxes for each line of text, stacking the boxes on
            top of each other and writing the text in each individual box, each line gets its own box
         */
        for(int i = 0; i < lines.length; ++i) {
            int finalY = y + height*i;//we split the lines to boxes so we need the y value for each box
            int stringWidth = fm.stringWidth(lines[i]);//the width for that line of text
            int stringHeight = fm.getHeight();//the height for that line of text
            int lineOffset = fm.getMaxAscent() + (thickness);//Java draws strings starting at the ascent line so you have to add ascent


            int centeredX = ((width / 2) - (stringWidth / 2)) + x;
            int rightX = width+x-stringWidth;
            int bottomY = (finalY+height) - 5 - fm.getMaxDescent();
            int topY = (finalY + 5);


            int centeredY = finalY + ( (height/2) - (stringHeight/2) );
            centeredY = centeredY + (int) (centeredY * .015);//there's a slightly upward bias so I tried correcting it


            if(alignment == 'l'){
                //left
                drawTextToScreen(lines[i], stringColor, outlineColor, x+5, centeredY + lineOffset, g, thickness);
            }else if(alignment == 'r'){
                //right
                drawTextToScreen(lines[i], stringColor, outlineColor, rightX-5, centeredY + lineOffset, g, thickness);
            }else if (alignment == 't'){
                //top center
                drawTextToScreen(lines[i],stringColor,outlineColor,centeredX,topY+lineOffset,g,thickness);
            }else if(alignment == 'b'){
                //bottom center
                drawTextToScreen(lines[i],stringColor,outlineColor,centeredX,bottomY,g,thickness);
            }else if (alignment == '1'){
                //top right
                drawTextToScreen(lines[i],stringColor,outlineColor,rightX,topY+lineOffset,g,thickness);
            }else if (alignment == '2'){
                //top left
                drawTextToScreen(lines[i],stringColor,outlineColor,x+5,topY + lineOffset,g,thickness);
            }else if (alignment == '3'){
                //bottom left
                drawTextToScreen(lines[i], stringColor, outlineColor, x+5, bottomY, g, thickness);
            }else if (alignment == '4'){
                //bottom right
                drawTextToScreen(lines[i], stringColor, outlineColor, rightX-5, bottomY, g, thickness);
            }else{
                //true center
                drawTextToScreen(lines[i], stringColor, outlineColor, centeredX, centeredY + lineOffset, g, thickness);
            }
        }
    }
    FontMetrics getSizedFont(FontMetrics fm, int maxWidth, int maxHeight, String str, Graphics g, int thickness){
        if(str.trim().equals("")){
            return fm;
        }


        maxWidth = maxWidth - thickness * 2;

        String[] lines = str.split("  ");
        int stringWidth = 0;
        int stringHeight = fm.getMaxAscent() + fm.getMaxDescent();
        if(lines.length > 1){
            maxHeight = maxHeight - ((thickness * 2)*lines.length);
            stringHeight = stringHeight * lines.length;
            stringHeight += (fm.getLeading() + thickness)* (lines.length - 1);
        }
        for (String line : lines) {
            stringWidth = Math.max(stringWidth, fm.stringWidth(line));
        }
        //System.out.println("X: " + maxWidth + " STRX: " + stringWidth + " Y: " + maxHeight + " STRY: " + stringHeight);
        if (stringWidth >= maxWidth || stringHeight >= maxHeight ) {
            FontMetrics fm2 = g.getFontMetrics(fm.getFont().deriveFont(fm.getFont().getSize2D()-1));
            return getSizedFont(fm2,maxWidth,maxHeight,str,g);
        }
        return fm;
    }

    void drawImageInCenter(BufferedImage image, int x, int y, int width, int height, Graphics g){
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int centeredX = (width /2 - imageWidth /2) + x;
        int centeredY = (height/2 - imageHeight/2) + y;
        g.drawImage(image,centeredX,centeredY,null);
    }

    public BufferedReader inputStreamToBufferedReader(InputStream in) {
        BufferedReader br = null;
        try{
            br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        }catch(Exception e){
            e.printStackTrace();
        }
        return br;
    }

    void drawImageSizedInCenter(BufferedImage image, int x, int y, int width, int height, Graphics g, Character smallerLargerBoth){
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        if(smallerLargerBoth == 'l') {
            if (imageWidth > width || imageHeight > height) {
                image = getSizedImgToScale(image, width, height);
            }
        }else if(smallerLargerBoth == 'b'){
            if (imageWidth > width || imageHeight > height) {
                image = getSizedImgToScale(image, width, height);
            }
        }else{
            image = getSizedImgToScale(image, width, height);
        }
        drawImageInCenter(image,x,y,width,height,g);
    }

    private BufferedImage getSizedImgToScale(BufferedImage image, int maxWidth, int maxHeight){
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        Dimension imageDimension = new Dimension(imageWidth,imageHeight);
        Dimension rectangleDImension = new Dimension(maxWidth,maxHeight);

        Dimension newImageDimension = getScaledDimension(imageDimension,rectangleDImension);

        image = getSizedImg(image,(int) newImageDimension.getWidth(),(int) newImageDimension.getHeight());
        return image;
    }

    private static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {
        //Thanks stackoverflow user "Ozzy"
        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }

}