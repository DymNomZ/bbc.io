package exceptions;

public class BBCServerTimeout extends RuntimeException {
    public BBCServerTimeout(String message) {
        super(message);
    }
}
