public class Training {
    GetResources get = new GetResources();
    void randomChars(){
        String s = "";
        int sentinel = 0;
        int end = 0;
        while(end < 1000) {
            while (sentinel < 90) {
                int nextChar = get.randomWithRange(32, 126);
                char c = (char) nextChar;
                s = s + c;
                sentinel = get.randomWithRange(0, 100);
            }
            sentinel = 0;
            System.out.println(s);
            s = "";
            end++;
        }
    }
}
