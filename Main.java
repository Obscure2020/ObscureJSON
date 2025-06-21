import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.*;
import ObscureJSON.*;

class Main {
    private static String readWholeFile(Path input) throws IOException{
        List<String> lines = Files.readAllLines(input, StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        if(!lines.isEmpty()) sb.append(lines.getFirst());
        if(lines.size() > 1){
            for(String s : lines.subList(1, lines.size())){
                sb.append('\n');
                sb.append(s);
            }
        }
        return sb.toString();
    }

    private static void properPrint(String message){
        String[] lines = message.split("\n");
        for(String l : lines) System.out.println(l);
    }

    public static void main(String[] args) throws Exception {
        String document = readWholeFile(Paths.get("TestData/Sample001.txt"));
        JSONelement elem = JSONdecode.document(document);
        if(!elem.isObject()){
            System.out.println("Something went wrong.");
            System.out.println("We were expecting to see a JSON \"object\" inside the file.");
            System.exit(1);
        }
        properPrint(elem.prettyPrint());

        System.out.println();
        JSONstring str = JSONstring.create(readWholeFile(Paths.get("TestData/MixedChars.txt")));
        System.out.println(str.prettyPrint());
    }
}