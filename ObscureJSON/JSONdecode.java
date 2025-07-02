package ObscureJSON;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class JSONdecode {

    private static final int ERR_CONTEXT = 40;

    private static void codepointPositionException(int[] document, int position, String reason) throws JSONstandardsException {
        ArrayList<Integer> leftPartList = new ArrayList<>();
        int limit = Math.max(position - ERR_CONTEXT, 0);
        for(int i=position-1; i>=limit; i--){
            int codepoint = document[i];
            if((codepoint == '\n') || (codepoint == '\r')) break;
            if(codepoint != '\t') leftPartList.add(codepoint);
        }
        Collections.reverse(leftPartList);
        StringBuilder leftPart = new StringBuilder();
        if(limit != 0) leftPart.append("...");
        for(int c : leftPartList) leftPart.appendCodePoint(c);
        String markerPadding = "-".repeat((int) leftPart.codePoints().count());
        String centerPart = Character.toString(document[position]);
        limit = Math.min(position + ERR_CONTEXT, document.length - 1);
        StringBuilder rightPart = new StringBuilder();
        for(int i=position+1; i<=limit; i++){
            int codepoint = document[i];
            if((codepoint == '\n') || (codepoint == '\r')) break;
            if(codepoint != '\t') rightPart.appendCodePoint(codepoint);
        }
        if(limit != (document.length - 1)) rightPart.append("...");
        StringBuilder result = new StringBuilder(reason);
        String lineSep = System.lineSeparator();
        result.append(lineSep);
        result.append(lineSep);
        result.append("      ");
        result.append(leftPart);
        result.append(centerPart);
        result.append(rightPart);
        result.append(lineSep);
        result.append("HERE: ");
        result.append(markerPadding);
        result.append('^');
        result.append(lineSep);
        throw new JSONstandardsException(result.toString());
    }

    private static void codepointEndingException(int[] document, String reason) throws JSONstandardsException {
        ArrayList<Integer> excerptList = new ArrayList<>();
        int limit = Math.max(document.length - 1 - ERR_CONTEXT, 0);
        for(int i=document.length-1; i>=limit; i--){
            int codepoint = document[i];
            if((codepoint == '\n') || (codepoint == '\r')) break;
            if(codepoint != '\t') excerptList.add(codepoint);
        }
        Collections.reverse(excerptList);
        StringBuilder excerpt = new StringBuilder();
        if(limit != 0) excerpt.append("...");
        for(int c : excerptList) excerpt.appendCodePoint(c);
        String markerPadding = "-".repeat((int) excerpt.codePoints().count());
        StringBuilder result = new StringBuilder(reason);
        String lineSep = System.lineSeparator();
        result.append(lineSep);
        result.append(lineSep);
        result.append("      ");
        result.append(excerpt);
        result.append(lineSep);
        result.append("HERE: ");
        result.append(markerPadding);
        result.append('^');
        result.append(lineSep);
        throw new JSONstandardsException(result.toString());
    }

    private static String reprint(TaggedString ts){
        if(ts.check){
            return '"' + ts.text + '"';
        }
        return ts.text;
    }

    private static void chunkException(List<TaggedString> chunks, int position, String reason) throws JSONstandardsException {
        ArrayList<Integer> partList = new ArrayList<>();
        int index = position - 1;
        while(partList.size() < ERR_CONTEXT){
            if(index < 0) break;
            int[] codepoints = reprint(chunks.get(index)).codePoints().toArray();
            for(int i=codepoints.length-1; i>=0; i--) partList.add(codepoints[i]);
            index--;
        }
        int leftGrowth = partList.size();
        while(partList.size() > ERR_CONTEXT) partList.removeLast();
        Collections.reverse(partList);
        StringBuilder leftPart = new StringBuilder();
        if((index >= 0) || (leftGrowth > ERR_CONTEXT)) leftPart.append("...");
        for(int c : partList) leftPart.appendCodePoint(c);
        String markerPadding = "-".repeat((int) leftPart.codePoints().count());
        String centerPart = reprint(chunks.get(position));
        String marker = "^".repeat((int) centerPart.codePoints().count());
        partList.clear();
        index = position + 1;
        while(partList.size() < ERR_CONTEXT){
            if(index >= chunks.size()) break;
            reprint(chunks.get(index)).codePoints().forEachOrdered(partList::add);
            index++;
        }
        int rightGrowth = partList.size();
        while(partList.size() > ERR_CONTEXT) partList.removeLast();
        StringBuilder rightPart = new StringBuilder();
        for(int c : partList) rightPart.appendCodePoint(c);
        if((index < chunks.size()) || (rightGrowth > ERR_CONTEXT)) rightPart.append("...");
        StringBuilder result = new StringBuilder(reason);
        String lineSep = System.lineSeparator();
        result.append(lineSep);
        result.append(lineSep);
        result.append("      ");
        result.append(leftPart);
        result.append(centerPart);
        result.append(rightPart);
        result.append(lineSep);
        result.append("HERE: ");
        result.append(markerPadding);
        result.append(marker);
        result.append(lineSep);
        throw new JSONstandardsException(result.toString());
    }

    private static void chunkEndingException(List<TaggedString> chunks, String reason) throws JSONstandardsException {
        ArrayList<Integer> partList = new ArrayList<>();
        int index = chunks.size() - 1;
        while(partList.size() < ERR_CONTEXT){
            if(index < 0) break;
            int[] codepoints = reprint(chunks.get(index)).codePoints().toArray();
            for(int i=codepoints.length-1; i>=0; i--) partList.add(codepoints[i]);
            index--;
        }
        int growth = partList.size();
        while(partList.size() > ERR_CONTEXT) partList.removeLast();
        Collections.reverse(partList);
        StringBuilder excerpt = new StringBuilder();
        if((index >= 0) || (growth > ERR_CONTEXT)) excerpt.append("...");
        for(int c : partList) excerpt.appendCodePoint(c);
        String markerPadding = "-".repeat((int) excerpt.codePoints().count());
        StringBuilder result = new StringBuilder(reason);
        String lineSep = System.lineSeparator();
        result.append(lineSep);
        result.append(lineSep);
        result.append("      ");
        result.append(excerpt);
        result.append(lineSep);
        result.append("HERE: ");
        result.append(markerPadding);
        result.append('^');
        result.append(lineSep);
        throw new JSONstandardsException(result.toString());
    }

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
                    codepointPositionException(chars, i, "Illegal usage of the escape character \"\\\" outside of a string.");
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
        if(mode) codepointEndingException(chars, "Document ended while still inside a yet-to-terminate string.");
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