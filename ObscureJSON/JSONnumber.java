package ObscureJSON;

public final class JSONnumber implements JSONelement {

    private final double stored_value;

    private JSONnumber(double new_value){
        stored_value = new_value;
    }

    public static JSONnumber create(double value) throws JSONstandardsException {
        if(Double.isInfinite(value)){
            throw new JSONstandardsException("Infinite numbers are not allowed in JSON.");
        }
        if(Double.isNaN(value)){
            throw new JSONstandardsException("NaN values are not allowed in JSON.");
        }
        return new JSONnumber(value);
    }

    public double getValue(){
        return stored_value;
    }

    public String condensedPrint(){
        return Double.toString(stored_value);
    }

    public String prettyPrint(){
        return Double.toString(stored_value);
    }

    public boolean isObject(){
        return false;
    }

    public boolean isArray(){
        return false;
    }

    public boolean isNumber(){
        return true;
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