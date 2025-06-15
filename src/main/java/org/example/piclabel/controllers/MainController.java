package org.example.piclabel.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

public class MainController {
    @FXML
    private CarouselController carouselController;

    @FXML
    private MenuItem openFileItem;

    @FXML
    private void initialize() {
        openFileItem.setOnAction(event -> openFiles());
    }

    private void openFiles() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar imagens");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imagens", "*.jpg", "*.jpeg", "*.png", "*.bmp", "*.gif")
        );
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(carouselController.getPrimaryStage());

        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            List<File> imageFiles = carouselController.getImageFiles();
            imageFiles.clear();
            imageFiles.addAll(selectedFiles);
            carouselController.setCurrentIndex(0);
            carouselController.showImage(carouselController.getCurrentIndex());
        }
    }

}
