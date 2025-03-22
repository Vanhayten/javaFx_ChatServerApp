module com.ayoub_chaib.chatserverapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.ayoub_chaib.chatserverapp to javafx.fxml;
    exports com.ayoub_chaib.chatserverapp;
}