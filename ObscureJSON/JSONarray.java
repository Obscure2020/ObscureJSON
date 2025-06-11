package ObscureJSON;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public final class JSONarray implements JSONelement, List<JSONelement> {

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

    //The methods below all go toward implementing List<JSONelement>.

    public void add(int index, JSONelement element){
        contents.add(index, element);
    }

    public boolean add(JSONelement e){
        return contents.add(e);
    }

    public boolean addAll(int index, Collection<? extends JSONelement> c){
        return contents.addAll(index, c);
    }

    public boolean addAll(Collection<? extends JSONelement> c){
        return contents.addAll(c);
    }

    public void clear(){
        contents.clear();
    }

    public boolean contains(Object o){
        return contents.contains(o);
    }

    public boolean containsAll(Collection<?> c){
        return contents.containsAll(c);
    }

    public JSONelement get(int index){
        return contents.get(index);
    }

    public int indexOf(Object o){
        return contents.indexOf(o);
    }

    public boolean isEmpty(){
        return contents.isEmpty();
    }

    public Iterator<JSONelement> iterator(){
        return contents.iterator();
    }

    public int lastIndexOf(Object o){
        return contents.lastIndexOf(o);
    }

    public ListIterator<JSONelement> listIterator(){
        return contents.listIterator();
    }

    public ListIterator<JSONelement> listIterator(int index){
        return contents.listIterator(index);
    }

    public JSONelement remove(int index){
        return contents.remove(index);
    }

    public boolean remove(Object o){
        return contents.remove(o);
    }

    public boolean removeAll(Collection<?> c){
        return contents.removeAll(c);
    }

    public boolean retainAll(Collection<?> c){
        return contents.retainAll(c);
    }

    public JSONelement set(int index, JSONelement element){
        return contents.set(index, element);
    }

    public int size(){
        return contents.size();
    }

    public List<JSONelement> subList(int fromIndex, int toIndex){
        return contents.subList(fromIndex, toIndex);
    }

    public Object[] toArray(){
        return contents.toArray();
    }

    public <T> T[] toArray(T[] a){
        return contents.toArray(a);
    }

}