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

    /*
    public static void main(String[] args) throws Exception {
        String document = readWholeFile(Paths.get("TestData/Sample005.txt"));
        JSONelement elem = JSONdecode.document(document);
        if(!elem.isObject()){
            System.out.println("Something went wrong.");
            System.out.println("We were expecting to see a JSON \"object\" inside the file.");
            System.exit(1);
        }
        properPrint(elem.prettyPrint());
    }
    */

    public static void main(String[] args) throws Exception {
        unclearMessages();
    }

    public static void circularReference() {
        JSONobject o1 = JSONobject.create();
        JSONobject o2 = JSONobject.create();
        o1.put("key", o2);
        o2.put("key", o1);

        o1.prettyPrint(); // makes ugly StackOverflowError
    }

    public static void unclearMessages() {
		String[] tests = {
                "{ \"key\": [1 234 5] }", // whitespace gets removed, so it looks like valid JSON in the error message
                "{ \"key\": value }", // JSONdecode.cantTellWhat is only used in one place, but not the other two where Double.parseDouble is used
                "{ key: \"value\" }", // unquoted keys are pretty easy to spot, a separate error message would be nice
                "{ \"key\": \"value\", }", "{ \"key\": [\"value\",] }", // likewise with trailing commas
                "\u00A0" // non-breaking space, gives funny message
        };

        for (String test : tests) {
            try {
                JSONdecode.document(test);
            } catch (Exception e) {
                System.out.println(e.getMessage() + "\n");
            }
        }
    }

    public static void unescapedControlCharacters() throws Throwable {
		String json = "{ \"key\": \"val\nue\" }"; // unescaped newline
        JSONelement element = JSONdecode.document(json);
        System.out.println(element.prettyPrint());
    }

    public static void illegalNumbers() throws Throwable {
        // because you use Double.parseDouble, and only special-case Infinity and NaN
        String json = """
                {
                  "leadingZero": 0123,
                  "trailingD": 1.2d,
                  "trailingF": 1.2f,
                  "leadingPlus": +123,
                  "leadingDot": .123,
                  "trailingDot": [123., 1.e3],
                  "hex": [0x1.0p0, 0X.8P+2, 0x1.fffffep127]
                }
                """;

        System.out.println(JSONdecode.document(json).prettyPrint());
    }

    public static void unicodeHandling() {
        // invalid surrogate pairs should be rejected
        String[] unicodeTests = {
                "{\"brokenHighSurrogate\": \"\\uD800\"}", // Lone high surrogate
                "{\"brokenLowSurrogate\": \"\\uDC00\"}", // Lone low surrogate
                "{\"reversedSurrogatePair\": \"\\uDC00\\uD800\"}" // Reversed pair
        };

        for (String test : unicodeTests) {
            try {
                JSONobject o = (JSONobject) JSONdecode.document(test);
                System.out.println("Accepted invalid Unicode: " + test);
                JSONstring str = (JSONstring) o.values().iterator().next();
                System.out.println("String value: " + str.getValue());
            } catch (Exception e) {
                System.out.println("Rejected: " + test);
            }
        }
    }
}