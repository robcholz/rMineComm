package robcholz.exception;

public class CannotSaveException extends Exception {
    public CannotSaveException(){
        super();
    }
    public CannotSaveException(String reason){
        super(reason);
    }
}
