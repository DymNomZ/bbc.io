package server;

import java.util.Random;

public class DeathMessageGenerator {
    public static String getRandomDeathMessage(String player, String killer) {
        Random random = new Random();
        // Pick a random message (single string from a one-element array)
        String[] messageArray = classes.Dialogues.DEATH_MESSAGES[random.nextInt(classes.Dialogues.DEATH_MESSAGES.length)];
        String message = messageArray[0];

        // Replace placeholders
        message = message.replace("<Player>", player);
        message = message.replace("<Killer>", killer);

        return message;
    }
}
