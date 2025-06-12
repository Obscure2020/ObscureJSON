package ObscureJSON;

final class InternalUtils {

    public static String leftPad(String body, char pad, int length){
        if(body.length() >= length) return body;
        StringBuilder sb = new StringBuilder(body);
        while(sb.length() < length) sb.insert(0, pad);
        return sb.toString();
    }

    public static String rightPad(String body, char pad, int length){
        if(body.length() >= length) return body;
        StringBuilder sb = new StringBuilder(body);
        while(sb.length() < length) sb.append(pad);
        return sb.toString();
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
        int[] int_pieces = new int[pieces.length];
        for(int i=0; i<pieces.length; i++){
            int_pieces[i] = (int) pieces[i];
        }
        StringBuilder sb = new StringBuilder();
        for(int item : int_pieces){
            sb.append("\\u");
            sb.append(InternalUtils.leftPad(Integer.toHexString(item).toUpperCase(), '0', 4));
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
        final int[] codepoints = text.codePoints().toArray();
        StringBuilder sb = new StringBuilder();
        sb.append('"');
        for(int c : codepoints){
            if((c < 20) || (c > 126) || (c == 34) || (c == 92)){
                sb.append(escapeCodepoint(c));
            } else {
                sb.appendCodePoint(c);
            }
        }
        sb.append('"');
        return sb.toString();
    }

}