package businesslayer.exceptions;

public class UnauthorizedUserOperationException extends Exception{

    public UnauthorizedUserOperationException() {
        super("Only team owners can add other users as team owners.");
    }

    public UnauthorizedUserOperationException(String message){
        super(message);
    }
}
