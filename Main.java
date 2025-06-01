import java.io.*;
import java.util.*;
import ObscureJSON.*;

class Main {
    private static String readWholeFile(File input) throws FileNotFoundException{
        Scanner scan = new Scanner(input);
        StringBuilder sb = new StringBuilder();
        if(scan.hasNext()) sb.append(scan.nextLine());
        while(scan.hasNext()){
            sb.append('\n');
            sb.append(scan.nextLine());
        }
        scan.close();
        return sb.toString();
    }
    public static void main(String[] args) throws Exception {
        String test = readWholeFile(new File("TestData/MixedChars.txt"));
        JSONstring test_json = JSONstring.create(test);
        System.out.println(test_json.prettyPrint());
    }
}