package ObscureJSON;

public class JSONbool implements JSONelement {

    private static final JSONbool cached_true = new JSONbool(true);
    private static final JSONbool cached_false = new JSONbool(false);
    private static final String cached_true_str = "true";
    private static final String cached_false_str = "false";

    private final boolean stored_value;

    private JSONbool(boolean new_value){
        stored_value = new_value;
    }

    public static JSONbool create(boolean value){
        if(value) return cached_true;
        return cached_false;
    }

    public boolean getValue(){
        return stored_value;
    }

    public String condensedPrint(){
        if(stored_value) return cached_true_str;
        return cached_false_str;
    }

    public String prettyPrint(){
        return condensedPrint();
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

}