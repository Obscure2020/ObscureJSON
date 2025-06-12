package ObscureJSON;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public final class JSONobject implements JSONelement, Map<String, JSONelement> {

    private final LinkedHashMap<String,JSONelement> contents;

    private JSONobject(){
        contents = new LinkedHashMap<>();
    }

    public static JSONobject create(){
        return new JSONobject();
    }

    public String condensedPrint(){
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        boolean more = false;
        for(Map.Entry<String, JSONelement> entry : contents.entrySet()){
            if(more) sb.append(',');
            sb.append(InternalUtils.JSONstringLiteral(entry.getKey()));
            sb.append(':');
            sb.append(entry.getValue().condensedPrint());
            more = true;
        }
        sb.append('}');
        return sb.toString();
    }

    public String prettyPrint(){
        if(contents.isEmpty()) return "{}";
        StringBuilder overall = new StringBuilder();
        overall.append("{\n");
        boolean more = false;
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, JSONelement> entry : contents.entrySet()){
            if(more) overall.append(",\n");
            sb.setLength(0);
            sb.append(InternalUtils.JSONstringLiteral(entry.getKey()));
            sb.append(": ");
            sb.append(entry.getValue().prettyPrint());
            overall.append(InternalUtils.indentLines(sb.toString()));
            more = true;
        }
        overall.append("\n}");
        return overall.toString();
    }

    public boolean isObject(){
        return true;
    }

    public boolean isArray(){
        return false;
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

    //The methods below all go toward implementing Map<String, JSONelement>.

    public void clear(){
        contents.clear();
    }

    public boolean containsKey(Object key){
        return contents.containsKey(key);
    }

    public boolean containsValue(Object value){
        return contents.containsValue(value);
    }

    public Set<Map.Entry<String,JSONelement>> entrySet(){
        return contents.entrySet();
    }

    public JSONelement get(Object key){
        return contents.get(key);
    }

    public boolean isEmpty(){
        return contents.isEmpty();
    }

    public Set<String> keySet(){
        return contents.keySet();
    }

    public JSONelement put(String key, JSONelement value){
        return contents.put(key, value);
    }

    public void putAll(Map<? extends String, ? extends JSONelement> m){
        contents.putAll(m);
    }

    public JSONelement remove(Object key){
        return contents.remove(key);
    }

    public int size(){
        return contents.size();
    }

    public Collection<JSONelement> values(){
        return contents.values();
    }

}
