package exceptions;

public class BBCSQLError extends Exception {
    public BBCSQLError() {
    super("Database Error");
  }
    public BBCSQLError(String message) {
        super(message);
    }
}
