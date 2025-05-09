module com.example.bbc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.compiler;
    requires java.sql;
    requires mysql.connector.j;


    opens com.example.bbc to javafx.fxml;
    exports com.example.bbc;
    exports classes;
    exports datas;
    exports server.debug;
    opens classes to javafx.fxml;
}