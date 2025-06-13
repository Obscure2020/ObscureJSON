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

    public String condensedPrint(){
        return InternalUtils.JSONstringLiteral(stored_value);
    }

    public String prettyPrint(){
        return InternalUtils.JSONstringLiteral(stored_value);
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

    @Override
    public boolean equals(Object other){
        if(!(other instanceof JSONelement)) return false;
        JSONelement j_other = (JSONelement) other;
        if(!j_other.isString()) return false;
        JSONstring s_other = (JSONstring) other;
        return stored_value.equals(s_other.stored_value);
    }

}