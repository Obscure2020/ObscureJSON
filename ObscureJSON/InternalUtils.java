package ObscureJSON;

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
            case 34 -> "\\\"";
            case 92 -> "\\\\";
            case 8 -> "\\b";
            case 12 -> "\\f";
            case 10 -> "\\n";
            case 13 -> "\\r";
            case 9 -> "\\t";
            default -> hexEscape(codepoint);
        };
    }

    public static String JSONstringLiteral(String text){
        StringBuilder sb = new StringBuilder();
        sb.append('"');
        text.codePoints().forEachOrdered(c -> {
            if((c < 20) || (c > 126) || (c == 34) || (c == 92)){
                sb.append(escapeCodepoint(c));
            } else {
                sb.appendCodePoint(c);
            }
        });
        sb.append('"');
        return sb.toString();
    }

}