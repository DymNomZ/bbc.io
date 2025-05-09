package exceptions;

public class BBCSQLAlreadyInLobby extends BBCSQLError {
    public BBCSQLAlreadyInLobby(String player_name) {
        super("Player: " + player_name + " already in-lobby");
    }
}
