package ObscureJSON;

public final class JSONstring implements JSONelement {

    private final String stored_value;

    private JSONstring(String new_value){
        stored_value = new_value;
    }

    public static JSONstring create(String value){
        return new JSONstring(value);
    }

    public String getValue(){
        return stored_value;
    }

    private String hexEscape(int codepoint){
        char[] pieces = Character.toChars(codepoint);
        int[] int_pieces = new int[pieces.length];
        for(int i=0; i<pieces.length; i++){
            int_pieces[i] = (int) pieces[i];
        }
        StringBuilder sb = new StringBuilder();
        for(int item : int_pieces){
            sb.append("\\u");
            sb.append(ObscureLib.leftPad(Integer.toHexString(item).toUpperCase(), '0', 4));
        }
        return sb.toString();
    }

    private String escapeCodepoint(int codepoint){
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

    public String condensedPrint(){
        final int[] codepoints = stored_value.codePoints().toArray();
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

    public String prettyPrint(){
        return condensedPrint();
    }

    public boolean isObject(){
        return false;
    }

    public boolean isArray(){
        return false;
    }

    public boolean isNumber(){
        return false;
    }

    public boolean isString(){
        return true;
    }

    public boolean isBool(){
        return false;
    }

    public boolean isNull(){
        return false;
    }

}