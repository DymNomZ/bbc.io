module com.example.bbc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.bbc to javafx.fxml;
    exports com.example.bbc;
    exports classes;
    opens classes to javafx.fxml;
}