package ObscureJSON;

public final class JSONnull implements JSONelement {

    private static final JSONnull cached = new JSONnull();
    private static final String cached_str = "null";

    private JSONnull(){
    }

    public static JSONnull create(){
        return cached;
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
        return false;
    }

    public boolean isNull(){
        return true;
    }

}