package exceptions;

public class BBCServerAlreadyStarted extends RuntimeException {
    public BBCServerAlreadyStarted(String message) {
        super(message);
    }
}
