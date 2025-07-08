package ObscureJSON;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HexFormat;

final class InternalUtils {

    private static HexFormat hex = HexFormat.of().withUpperCase();

    public static boolean matchesAlphabet(String message, String alphabet){
        if(message.length() == 0) return true;
        if(alphabet.length() == 0) return false;
        int[] alphabet_codepoints = alphabet.codePoints().sorted().distinct().toArray();
        return message.codePoints().sorted().distinct().allMatch(i -> (Arrays.binarySearch(alphabet_codepoints, i) >= 0));
    }

    public static String indentLines(String text){
        String indent = "    ";
        if(text.indexOf('\n') < 0){
            return indent + text;
        }
        String[] lines = text.split("\n");
        int last = lines.length - 1;
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<last; i++){
            sb.append(indent);
            sb.append(lines[i]);
            sb.append('\n');
        }
        sb.append(indent);
        sb.append(lines[last]);
        return sb.toString();
    }

    private static String hexEscape(int codepoint){
        char[] pieces = Character.toChars(codepoint);
        StringBuilder sb = new StringBuilder();
        for(char c : pieces){
            sb.append("\\u");
            sb.append(hex.toHexDigits(c));
        }
        return sb.toString();
    }

    private static String escapeCodepoint(int codepoint){
        return switch(codepoint){
            case '"' -> "\\\"";
            case '\\' -> "\\\\";
            case '\b' -> "\\b";
            case '\f' -> "\\f";
            case '\n' -> "\\n";
            case '\r' -> "\\r";
            case '\t' -> "\\t";
            default -> hexEscape(codepoint);
        };
    }

    public static String JSONstringLiteral(String text){
        StringBuilder sb = new StringBuilder();
        sb.append('"');
        text.codePoints().forEachOrdered(c -> {
            if((c < ' ') || (c > '~') || (c == '"') || (c == '\\')){
                sb.append(escapeCodepoint(c));
            } else {
                sb.appendCodePoint(c);
            }
        });
        sb.append('"');
        return sb.toString();
    }

    public static boolean wouldCircular(JSONelement parent, JSONelement child){
        if(!(child.isObject() || child.isArray())) return false;
        ArrayDeque<JSONelement> queue = new ArrayDeque<>();
        queue.addLast(child);
        while(!queue.isEmpty()){
            JSONelement elem = queue.removeFirst();
            if(elem == parent) return true;
            if(elem.isObject()){
                JSONobject obj_elem = (JSONobject) elem;
                for(JSONelement deep : obj_elem.values()){
                    if(deep.isObject() || deep.isArray()) queue.addLast(deep);
                }
            } else {
                JSONarray arr_elem = (JSONarray) elem;
                for(JSONelement deep : arr_elem){
                    if(deep.isObject() || deep.isArray()) queue.addLast(deep);
                }
            }
        }
        return false;
    }

    public static final String CIRC_OBJECTION = "Cyclic inclusions are not allowed.";

}