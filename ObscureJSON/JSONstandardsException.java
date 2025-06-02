package ObscureJSON;

public class JSONstandardsException extends Exception {

    public JSONstandardsException(String errorMessage){
        super(errorMessage);
    }

    public JSONstandardsException(String errorMessage, Throwable err){
        super(errorMessage, err);
    }
}