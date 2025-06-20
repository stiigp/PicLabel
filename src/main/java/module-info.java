module org.example.piclabel {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires metadata.extractor;
    requires java.desktop;
    requires jdk.compiler;


    opens org.example.piclabel to javafx.fxml;
    exports org.example.piclabel;
    exports org.example.piclabel.controllers;
    opens org.example.piclabel.controllers to javafx.fxml;
}