package exceptions;

public class BBCServerNotConnected extends RuntimeException {
    public BBCServerNotConnected(String message) {
        super(message);
    }
}
