package ObscureJSON;

import java.util.ArrayList;

public final class JSONarray implements JSONelement {

    private final ArrayList<JSONelement> contents;

    private JSONarray(){
        contents = new ArrayList<>();
    }

    public static JSONarray create(){
        return new JSONarray();
    }

    public String condensedPrint(){
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        int len = contents.size();
        if(len > 0) sb.append(contents.getFirst().condensedPrint());
        if(len > 1){
            for(JSONelement item : contents.subList(1, len)){
                sb.append(',');
                sb.append(item.condensedPrint());
            }
        }
        sb.append(']');
        return sb.toString();
    }

    public String prettyPrint(){
        if(contents.isEmpty()) return "[]";
        boolean multi_line = false;
        int long_strings = 0;
        for(JSONelement item : contents){
            if(item.isString()){
                JSONstring cast = (JSONstring) item;
                int len = cast.prettyPrint().length();
                if(len >= 40) long_strings++;
            }
            if((long_strings >= 2) || item.isObject() || item.isArray()){
                multi_line = true;
                break;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        int len = contents.size();
        if(multi_line){
            sb.append('\n');
            sb.append(InternalUtils.indentLines(contents.getFirst().prettyPrint()));
            if(len > 1){
                for(JSONelement item : contents.subList(1, len)){
                    sb.append(",\n");
                    sb.append(InternalUtils.indentLines(item.prettyPrint()));
                }
            }
            sb.append('\n');
        } else {
            sb.append(contents.getFirst().prettyPrint());
            if(len > 1){
                for(JSONelement item : contents.subList(1, len)){
                    sb.append(", ");
                    sb.append(item.prettyPrint());
                }
            }
        }
        sb.append(']');
        return sb.toString();
    }

    public boolean isObject(){
        return false;
    }

    public boolean isArray(){
        return true;
    }

    public boolean isNumber(){
        return false;
    }

    public boolean isString(){
        return false;
    }

    public boolean isBool(){
        return false;
    }

    public boolean isNull(){
        return false;
    }

    //Usability Methods

    public JSONelement get(int index){
        return contents.get(index);
    }

    public void add(JSONelement item){
        contents.add(item);
    }

    public void insert(JSONelement item, int index){
        contents.add(index, item);
    }

    public void replace(JSONelement item, int index){
        contents.set(index, item);
    }

    public void remove(int index){
        contents.remove(index);
    }

    public void clear(){
        contents.clear();
    }

    public int length(){
        return contents.size();
    }

    public boolean isEmpty(){
        return contents.isEmpty();
    }

    //TODO: Attempt to add "implements Iterable<JSONelement>".

}