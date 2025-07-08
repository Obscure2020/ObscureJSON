package ObscureJSON;

public class JSONstandardsException extends RuntimeException {

    public JSONstandardsException(String errorMessage){
        super(errorMessage);
    }

    public JSONstandardsException(String errorMessage, Throwable err){
        super(errorMessage, err);
    }
}