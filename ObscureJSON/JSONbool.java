package ObscureJSON;

public final class JSONbool implements JSONelement {

    private static final JSONbool cached_true = new JSONbool(true);
    private static final JSONbool cached_false = new JSONbool(false);

    private final boolean stored_value;
    private final String cached_str;

    private JSONbool(boolean new_value){
        stored_value = new_value;
        cached_str = stored_value ? "true" : "false";
    }

    public static JSONbool create(boolean value){
        if(value) return cached_true;
        return cached_false;
    }

    public boolean getValue(){
        return stored_value;
    }

    public String condensedPrint(){
        return cached_str;
    }

    public String prettyPrint(){
        return cached_str;
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
        return false;
    }

    public boolean isBool(){
        return true;
    }

    public boolean isNull(){
        return false;
    }

    @Override
    public boolean equals(Object other){
        if(!(other instanceof JSONelement)) return false;
        JSONelement j_other = (JSONelement) other;
        if(!j_other.isBool()) return false;
        JSONbool b_other = (JSONbool) other;
        return stored_value == b_other.stored_value;
    }

}