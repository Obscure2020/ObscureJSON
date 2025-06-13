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

    private String trimFloat(String numeric){
        if(numeric.endsWith(".0")){
            return numeric.substring(0, numeric.length()-2);
        }
        return numeric;
    }

    public String condensedPrint(){
        String original = Double.toString(stored_value).toLowerCase();
        int exp_sep = original.indexOf('e');
        if(exp_sep >= 0){
            String prefix = original.substring(0, exp_sep);
            String suffix = original.substring(exp_sep);
            return trimFloat(prefix) + suffix;
        }
        return trimFloat(original);
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

    @Override
    public boolean equals(Object other){
        if(!(other instanceof JSONelement)) return false;
        JSONelement j_other = (JSONelement) other;
        if(!j_other.isNumber()) return false;
        JSONnumber n_other = (JSONnumber) other;
        return stored_value == n_other.stored_value;
    }

}