package org.example.piclabel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.piclabel.utils.AppCleaner;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main.fxml"));
        Path processedPath = Paths.get(System.getProperty("user.home"), "PicLabel", "processed");
        AppCleaner.setupShutdownHook(processedPath);
        Scene scene = new Scene(fxmlLoader.load(), 960, 720);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}