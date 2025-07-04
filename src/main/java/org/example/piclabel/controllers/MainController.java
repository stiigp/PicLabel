package org.example.piclabel.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainController {
    @FXML
    private CarouselController carouselController;

    @FXML
    private ControlpanelController controlpanelController;


    @FXML
    private AnchorPane carousel; // fx:id do <fx:include>

    @FXML
    private AnchorPane controlpanel; // idem

    @FXML
    private MenuItem openFileItem;

    @FXML
    private void initialize() {
        openFileItem.setOnAction(event -> openFiles());

        // manually loading carousel.fxml
        FXMLLoader carouselLoader = new FXMLLoader(getClass().getResource("/org/example/piclabel/components/carousel.fxml"));
        VBox carouselNode = null;
        try {
            carouselNode = carouselLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        carouselController = carouselLoader.getController();

        // manually loading controlpanel.fxml
        FXMLLoader panelLoader = new FXMLLoader(getClass().getResource("/org/example/piclabel/components/controlpanel.fxml"));
        AnchorPane panelNode = null;
        try {
            panelNode = panelLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        controlpanelController = panelLoader.getController();

        // injects the controller
        controlpanelController.setCarouselController(carouselController);

        // swaps the included nodes
        carousel.getChildren().setAll(carouselNode);
        controlpanel.getChildren().setAll(panelNode);
    }

    private void openFiles() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select images");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png", "*.bmp", "*.gif")
        );
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(carouselController.getPrimaryStage());

        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            List<File> imageFiles = carouselController.getImageFiles();
            List<File> originalImages = carouselController.getOriginalImages();
            imageFiles.clear();
            imageFiles.addAll(selectedFiles);
            originalImages.clear();
            originalImages.addAll(selectedFiles);
            carouselController.setCurrentIndex(0);
            carouselController.showImage(carouselController.getCurrentIndex());
            carouselController.enableButtons();
            controlpanelController.enableButtons();
        }
    }

}
