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

    //TODO: get, add, insert, remove.
    //TODO: clear. length. isEmpty. replace.

    //TODO: Attempt to add "implements Iterable<JSONelement>".

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
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        int len = contents.size();
        if(len > 0) sb.append(contents.getFirst().prettyPrint());
        if(len > 1){
            for(JSONelement item : contents.subList(1, len)){
                sb.append(", ");
                sb.append(item.prettyPrint());
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

}