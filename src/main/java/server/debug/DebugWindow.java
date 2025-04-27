package server.debug;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DebugWindow extends Application {
    private static TextArea textArea;
    private static TextArea players;

    @Override
    public void start(Stage stage) {
        textArea = new TextArea();
        textArea.setEditable(false);


        players = new TextArea();
        players.setEditable(false);

        VBox root = new VBox(textArea,players);
        Scene scene = new Scene(root, 400, 300);

        stage.setTitle("Server Debug Output");
        stage.setScene(scene);
        stage.show();
    }
    public static void log(String message) {
        if (textArea != null) {
            Platform.runLater(() -> textArea.setText(message + "\n"));
        }
    }
    public static void logPlayers(String message) {
        if (players != null) {
            Platform.runLater(() -> players.setText(message + "\n"));
        }
    }

    public static void initDebugWindow() {
        new Thread(() -> Application.launch(DebugWindow.class)).start();
    }
}
