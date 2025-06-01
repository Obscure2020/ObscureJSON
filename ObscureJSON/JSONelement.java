package ObscureJSON;

public interface JSONelement {
    //Subtype checking methods
    public boolean isObject();
    public boolean isArray();
    public boolean isNumber();
    public boolean isString();
    public boolean isBool();
    public boolean isNull();

    //Encoding methods
    public String condensedPrint();
    public String prettyPrint();
}