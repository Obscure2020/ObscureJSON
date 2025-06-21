package ObscureJSON;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class JSONdecode {

    private static List<TaggedString> chunkify(String document) throws JSONstandardsException {
        ArrayList<TaggedString> bareChunks = new ArrayList<>();
        int[] chars = document.strip().codePoints().toArray();
        if(chars.length == 0){
            if(document.length() == 0){
                throw new JSONstandardsException("Was expecting a non-empty document.");
            } else {
                throw new JSONstandardsException("Was expecting a document that was not entirely composed of whitespace.");
            }
        }
        boolean mode = false;
        StringBuilder sb = new StringBuilder();
        int prev = -1;
        for(int i=0; i<chars.length; i++){
            int c = chars[i];
            if(mode){
                //Inside a String
                if((c == 34) && (prev != 92)){
                    mode = false;
                    bareChunks.add(new TaggedString(sb.toString(), true));
                    sb.setLength(0);
                } else {
                    sb.appendCodePoint(c);
                }
            } else {
                //Outside a String
                if(c == 92){
                    throw new JSONstandardsException("Illegal usage of the escape character \"\\\" outside of a string.");
                    //TODO: Show where this error occurs visually.
                } else if(c == 34){
                    mode = true;
                    bareChunks.add(new TaggedString(sb.toString(), false));
                    sb.setLength(0);
                } else {
                    sb.appendCodePoint(c);
                }
            }
            prev = c;
        }
        if(mode){
            throw new JSONstandardsException("Document ended while still inside a yet-to-terminate string.");
            //TODO: Show where this error occurs visually.
        }
        bareChunks.add(new TaggedString(sb.toString(), mode));
        ArrayList<TaggedString> processedChunks = new ArrayList<>(bareChunks.size());
        for(TaggedString item : bareChunks){
            if(item.check){
                processedChunks.add(item);
            } else {
                String[] pieces = item.text.splitWithDelimiters("\\s+|[\\[\\{\\]\\}:,]", 0);
                for(String piece : pieces){
                    String polished = piece.strip();
                    if(polished.length() > 0) processedChunks.add(new TaggedString(polished, false));
                }
            }
        }
        processedChunks.trimToSize();
        return Collections.unmodifiableList(processedChunks);
    }

    public static JSONelement document(String d) throws JSONstandardsException {
        List<TaggedString> chunks = chunkify(d);
        for(TaggedString item : chunks){
            System.out.println(item.check + " -> " + item.text);
        }
        return JSONobject.create(); //TODO
    }

}