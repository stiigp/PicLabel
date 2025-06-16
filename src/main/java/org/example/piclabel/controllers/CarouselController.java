package org.example.piclabel.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.piclabel.utils.MetadataUtil;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class CarouselController {
    @FXML
    private MenuItem openFileItem;

    @FXML
    private ImageView imageView;

    @FXML
    private Button backwardButton;

    @FXML
    private Button forwardButton;

    @FXML
    private Label indexLabel;

    private Stage primaryStage;

    private List<File> imageFiles = new ArrayList<>();
    private int currentIndex = 0;

    public void showImage(int index) {
        if (index >= 0 && index < imageFiles.size()) {
            File file = imageFiles.get(index);
            Image image = new Image(file.toURI().toString());
            indexLabel.setText((index + 1) + "/" + imageFiles.size());
            imageView.setImage(image);
        }
    }

    @FXML
    private void goForward() {
        if (currentIndex < imageFiles.size() - 1) {
            currentIndex++;
            showImage(currentIndex);
        }
    }

    @FXML
    private void goBackward() {
        if (currentIndex > 0) {
            currentIndex--;
            showImage(currentIndex);
        }
    }

    public MenuItem getOpenFileItem() {
        return openFileItem;
    }

    public void setOpenFileItem(MenuItem openFileItem) {
        this.openFileItem = openFileItem;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public Button getBackwardButton() {
        return backwardButton;
    }

    public void setBackwardButton(Button backwardButton) {
        this.backwardButton = backwardButton;
    }

    public Button getForwardButton() {
        return forwardButton;
    }

    public void setForwardButton(Button forwardButton) {
        this.forwardButton = forwardButton;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public List<File> getImageFiles() {
        return imageFiles;
    }

    public void setImageFiles(List<File> imageFiles) {
        this.imageFiles = imageFiles;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }
}