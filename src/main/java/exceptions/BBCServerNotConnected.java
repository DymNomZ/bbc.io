package exceptions;

public class BBCServerNotConnected extends RuntimeException {
    public BBCServerNotConnected() {
        super("Not connected to the server");
    }
}
