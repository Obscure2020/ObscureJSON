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

}