package ObscureJSON;

import java.util.List;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HexFormat;

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
        int longer_context = 2 * ERR_CONTEXT;
        ArrayList<Integer> partList = new ArrayList<>();
        int index = position - 1;
        while(partList.size() < ERR_CONTEXT){
            if(index < 0) break;
            partList.add((int) ' ');
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
        int centerGrowth = (int) centerPart.codePoints().count();
        int rightPartLimit = Math.min(Math.max(0, longer_context-centerGrowth), ERR_CONTEXT);
        if(rightPartLimit == 0){
            StringBuilder centerTrim = new StringBuilder();
            centerPart.codePoints().limit(longer_context).forEachOrdered(centerTrim::appendCodePoint);
            if((centerGrowth > longer_context) || (position < chunks.size()-1)) centerTrim.append("...");
            String marker = "^".repeat(longer_context);
            StringBuilder result = new StringBuilder(reason);
            String lineSep = System.lineSeparator();
            result.append(lineSep);
            result.append(lineSep);
            result.append("      ");
            result.append(leftPart);
            result.append(centerTrim);
            result.append(lineSep);
            result.append("HERE: ");
            result.append(markerPadding);
            result.append(marker);
            result.append(lineSep);
            throw new JSONstandardsException(result.toString());
        }
        String marker = "^".repeat(centerGrowth);
        partList.clear();
        index = position + 1;
        while(partList.size() < rightPartLimit){
            if(index >= chunks.size()) break;
            partList.add((int) ' ');
            reprint(chunks.get(index)).codePoints().forEachOrdered(partList::add);
            index++;
        }
        int rightGrowth = partList.size();
        while(partList.size() > rightPartLimit) partList.removeLast();
        StringBuilder rightPart = new StringBuilder();
        for(int c : partList) rightPart.appendCodePoint(c);
        if((index < chunks.size()) || (rightGrowth > rightPartLimit)) rightPart.append("...");
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

    private static String unescape(String original) throws JSONstandardsException {
        StringBuilder result = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        int[] codepoints = original.codePoints().toArray();
        int len = codepoints.length;
        int i=0;
        while(i < len){
            int c = codepoints[i];
            if(c == '\\'){
                if(i+1 >= len) codepointEndingException(codepoints, "We were expecting an escape signifier here.");
                int escape_signifier = codepoints[i+1];
                if(escape_signifier == 'u'){
                    temp.setLength(0);
                    if(i+2 >= len) codepointEndingException(codepoints, "We were expecting the first character of a Unicode hexadecimal specifier here.");
                    temp.appendCodePoint(codepoints[i+2]);
                    if(i+3 >= len) codepointEndingException(codepoints, "We were expecting the second character of a Unicode hexadecimal specifier here.");
                    temp.appendCodePoint(codepoints[i+3]);
                    if(i+4 >= len) codepointEndingException(codepoints, "We were expecting the third character of a Unicode hexadecimal specifier here.");
                    temp.appendCodePoint(codepoints[i+4]);
                    if(i+5 >= len) codepointEndingException(codepoints, "We were expecting the fourth character of a Unicode hexadecimal specifier here.");
                    temp.appendCodePoint(codepoints[i+5]);
                    String hex = temp.toString();
                    if(!InternalUtils.matchesAlphabet(hex, "0123456789abcdefABCDEF")) codepointPositionException(codepoints, i+2, "We were expecting the four-character piece that begins here to contain only hexadecimal characters.");
                    result.append((char) HexFormat.fromHexDigits(hex));
                    i += 6;
                } else {
                    switch(escape_signifier){
                        case '"' -> result.append('"');
                        case '\\' -> result.append('\\');
                        case '/' -> result.append('/');
                        case 'b' -> result.append('\b');
                        case 'f' -> result.append('\f');
                        case 'n' -> result.append('\n');
                        case 'r' -> result.append('\r');
                        case 't' -> result.append('\t');
                        default -> {
                            temp.setLength(0);
                            temp.append("The escape sequence \"\\");
                            temp.appendCodePoint(escape_signifier);
                            temp.append("\" is not recognized by JSON standards.");
                            codepointPositionException(codepoints, i+1, temp.toString());
                        }
                    }
                    i += 2;
                }
            } else {
                result.appendCodePoint(c);
                i++;
            }
        }
        return result.toString();
    }

    private enum State {
        EXPECT_GLOBAL("We were expecting to see... anything... here."),
        EXPECT_ITEM_OR_ARR_END("We were expecting to see an array item or the end of the array here."),
        EXPECT_COMMA_OR_ARR_END("We were expecting to see a comma or the end of the array here."),
        EXPECT_ARR_ITEM("We were expecting to see an array item here."),
        EXPECT_KEY_OR_OBJ_END("We were expecting to see an object member key string or the end of the object here."),
        EXPECT_COLON("We were expecting to see a colon here."),
        EXPECT_OBJ_VAL("We were expecting to see an object member value here."),
        EXPECT_COMMA_OR_OBJ_END("We were expecting to see a comma or the end of the object here."),
        EXPECT_OBJ_KEY("We were expecting to see an object member key here.");

        public final String description;

        State(String description){
            this.description = description;
        }
    }

    private static final String cantTellWhat = "We can't tell what JSON type this is supposed to be.";

    public static JSONelement document(String d) throws JSONstandardsException {
        List<TaggedString> chunks = chunkify(d);
        ArrayDeque<State> stateStack = new ArrayDeque<>();
        stateStack.addLast(State.EXPECT_GLOBAL);
        JSONelement globalElement = null;
        ArrayDeque<JSONelement> containerStack = new ArrayDeque<>();
        String lastObjKey = null;
        for(int i=0; i<chunks.size(); i++){
            if(stateStack.isEmpty()) chunkException(chunks, i, "We were not expecting to find any more data here, as the document seemed to have already concluded.");
            TaggedString chunk = chunks.get(i);
            String text = chunk.text;
            boolean check = chunk.check;
            State state = stateStack.removeLast();
            switch(state){
                case EXPECT_GLOBAL -> {
                    if(check){
                        globalElement = JSONstring.create(unescape(text));
                    } else if(text.equals("[")){
                        globalElement = JSONarray.create();
                        containerStack.addLast(globalElement);
                        stateStack.addLast(State.EXPECT_ITEM_OR_ARR_END);
                    } else if(text.equals("{")){
                        globalElement = JSONobject.create();
                        containerStack.addLast(globalElement);
                        stateStack.addLast(State.EXPECT_KEY_OR_OBJ_END);
                    } else if(text.equals("null")){
                        globalElement = JSONnull.create();
                    } else if(text.equals("true")){
                        globalElement = JSONbool.create(true);
                    } else if(text.equals("false")){
                        globalElement = JSONbool.create(false);
                    } else {
                        double result = 0;
                        try{
                            result = Double.parseDouble(text);
                        } catch(NumberFormatException e) {
                            chunkException(chunks, i, cantTellWhat + " " + state.description);
                        }
                        globalElement = JSONnumber.create(result);
                    }
                }
                case EXPECT_ITEM_OR_ARR_END -> {
                    JSONelement unchecked_container = containerStack.getLast();
                    if(!unchecked_container.isArray()){
                        throw new AssertionError("We thought we would be inside an array at this point? Debugging needed.");
                    }
                    JSONarray container = (JSONarray) unchecked_container;
                    if(check){
                        container.add(JSONstring.create(unescape(text)));
                        stateStack.addLast(State.EXPECT_COMMA_OR_ARR_END);
                    } else if(text.equals("]")){
                        containerStack.removeLast();
                    } else if(text.equals("[")){
                        JSONarray new_item = JSONarray.create();
                        container.add(new_item);
                        containerStack.addLast(new_item);
                        stateStack.addLast(State.EXPECT_COMMA_OR_ARR_END);
                        stateStack.addLast(State.EXPECT_ITEM_OR_ARR_END);
                    } else if(text.equals("{")){
                        JSONobject new_item = JSONobject.create();
                        container.add(new_item);
                        containerStack.addLast(new_item);
                        stateStack.addLast(State.EXPECT_COMMA_OR_ARR_END);
                        stateStack.addLast(State.EXPECT_KEY_OR_OBJ_END);
                    } else if(text.equals("null")){
                        container.add(JSONnull.create());
                        stateStack.addLast(State.EXPECT_COMMA_OR_ARR_END);
                    } else if(text.equals("true")){
                        container.add(JSONbool.create(true));
                        stateStack.addLast(State.EXPECT_COMMA_OR_ARR_END);
                    } else if(text.equals("false")){
                        container.add(JSONbool.create(false));
                        stateStack.addLast(State.EXPECT_COMMA_OR_ARR_END);
                    } else {
                        double result = 0;
                        try{
                            result = Double.parseDouble(text);
                        } catch(NumberFormatException e) {
                            chunkException(chunks, i, cantTellWhat + " " + state.description);
                        }
                        container.add(JSONnumber.create(result));
                        stateStack.addLast(State.EXPECT_COMMA_OR_ARR_END);
                    }
                }
                case EXPECT_COMMA_OR_ARR_END -> {
                    if(check){
                        chunkException(chunks, i, state.description);
                    } else if(text.equals(",")){
                        stateStack.addLast(State.EXPECT_COMMA_OR_ARR_END);
                        stateStack.addLast(State.EXPECT_ARR_ITEM);
                    } else if(text.equals("]")){
                        containerStack.removeLast();
                    } else {
                        chunkException(chunks, i, state.description);
                    }
                }
                case EXPECT_ARR_ITEM -> {
                    JSONelement unchecked_container = containerStack.getLast();
                    if(!unchecked_container.isArray()){
                        throw new AssertionError("We thought we would be inside an array at this point? Debugging needed.");
                    }
                    JSONarray container = (JSONarray) unchecked_container;
                    if(check){
                        container.add(JSONstring.create(unescape(text)));
                    } else if(text.equals("]")){
                        TaggedString prevChunk = chunks.get(i-1);
                        boolean prevCheck = prevChunk.check;
                        String prevText = prevChunk.text;
                        if(!prevCheck && prevText.equals(",")){
                            chunkException(chunks, i-1, "Illegal trailing comma in array.");
                        }
                    } else if(text.equals("[")){
                        JSONarray new_item = JSONarray.create();
                        container.add(new_item);
                        containerStack.addLast(new_item);
                        stateStack.addLast(State.EXPECT_ITEM_OR_ARR_END);
                    } else if(text.equals("{")){
                        JSONobject new_item = JSONobject.create();
                        container.add(new_item);
                        containerStack.addLast(new_item);
                        stateStack.addLast(State.EXPECT_KEY_OR_OBJ_END);
                    } else if(text.equals("null")){
                        container.add(JSONnull.create());
                    } else if(text.equals("true")){
                        container.add(JSONbool.create(true));
                    } else if(text.equals("false")){
                        container.add(JSONbool.create(false));
                    } else {
                        double result = 0;
                        try{
                            result = Double.parseDouble(text);
                        } catch(NumberFormatException e) {
                            chunkException(chunks, i, cantTellWhat + " " + state.description);
                        }
                        container.add(JSONnumber.create(result));
                    }
                }
                case EXPECT_KEY_OR_OBJ_END -> {
                    JSONelement unchecked_container = containerStack.getLast();
                    if(!unchecked_container.isObject()){
                        throw new AssertionError("We thought we would be inside an object at this point? Debugging needed.");
                    }
                    JSONobject container = (JSONobject) unchecked_container;
                    if(check){
                        lastObjKey = unescape(text);
                        stateStack.addLast(State.EXPECT_COMMA_OR_OBJ_END);
                        stateStack.addLast(State.EXPECT_OBJ_VAL);
                        stateStack.addLast(State.EXPECT_COLON);
                        if(container.containsKey(lastObjKey)){
                            chunkException(chunks, i, "The current object already contains the key \"" + lastObjKey + "\" and we cannot currently support duplicate object keys.");
                        }
                    } else if(text.equals("}")) {
                        containerStack.removeLast();
                    } else {
                        chunkException(chunks, i, state.description);
                    }
                }
                case EXPECT_COLON -> {
                    if(check || !text.equals(":")){
                        chunkException(chunks, i, state.description);
                    }
                }
                case EXPECT_OBJ_VAL -> {
                    JSONelement unchecked_container = containerStack.getLast();
                    if(!unchecked_container.isObject()){
                        throw new AssertionError("We thought we would be inside an object at this point? Debugging needed.");
                    }
                    JSONobject container = (JSONobject) unchecked_container;
                    if(lastObjKey == null){
                        throw new AssertionError("We were expecting that lastObjKey would be necessarily non-null here.");
                    }
                    if(check){
                        container.put(lastObjKey, JSONstring.create(unescape(text)));
                    } else if(text.equals("[")){
                        JSONarray new_item = JSONarray.create();
                        container.put(lastObjKey, new_item);
                        containerStack.addLast(new_item);
                        stateStack.addLast(State.EXPECT_ITEM_OR_ARR_END);
                    } else if(text.equals("{")){
                        JSONobject new_item = JSONobject.create();
                        container.put(lastObjKey, new_item);
                        containerStack.addLast(new_item);
                        stateStack.addLast(State.EXPECT_KEY_OR_OBJ_END);
                    } else if(text.equals("null")){
                        container.put(lastObjKey, JSONnull.create());
                    } else if(text.equals("true")){
                        container.put(lastObjKey, JSONbool.create(true));
                    } else if(text.equals("false")){
                        container.put(lastObjKey, JSONbool.create(false));
                    } else {
                        double result = 0;
                        try{
                            result = Double.parseDouble(text);
                        } catch(NumberFormatException e) {
                            chunkException(chunks, i, cantTellWhat + " " + state.description);
                        }
                        container.put(lastObjKey, JSONnumber.create(result));
                    }
                    lastObjKey = null;
                }
                case EXPECT_COMMA_OR_OBJ_END -> {
                    if(check){
                        chunkException(chunks, i, state.description);
                    } else if(text.equals(",")){
                        stateStack.addLast(State.EXPECT_COMMA_OR_OBJ_END);
                        stateStack.addLast(State.EXPECT_OBJ_VAL);
                        stateStack.addLast(State.EXPECT_COLON);
                        stateStack.addLast(State.EXPECT_OBJ_KEY);
                    } else if(text.equals("}")){
                        containerStack.removeLast();
                    } else {
                        chunkException(chunks, i, state.description);
                    }
                }
                case EXPECT_OBJ_KEY -> {
                    JSONelement unchecked_container = containerStack.getLast();
                    if(!unchecked_container.isObject()){
                        throw new AssertionError("We thought we would be inside an object at this point? Debugging needed.");
                    }
                    JSONobject container = (JSONobject) unchecked_container;
                    if(check){
                        lastObjKey = unescape(text);
                        if(container.containsKey(lastObjKey)){
                            chunkException(chunks, i, "The current object already contains the key \"" + lastObjKey + "\" and we cannot currently support duplicate object keys.");
                        }
                    } else {
                        if(text.equals("}")){
                            TaggedString prevChunk = chunks.get(i-1);
                            boolean prevCheck = prevChunk.check;
                            String prevText = prevChunk.text;
                            if(!prevCheck && prevText.equals(",")){
                                chunkException(chunks, i-1, "Illegal trailing comma in object.");
                            }
                        }
                        chunkException(chunks, i, state.description);
                    }
                }
            }
        }
        if(!stateStack.isEmpty()){
            String reason = "The document appears to have ended earlier than expected. ";
            String details = stateStack.removeLast().description;
            chunkEndingException(chunks, reason + details);
        }
        return globalElement;
    }

}