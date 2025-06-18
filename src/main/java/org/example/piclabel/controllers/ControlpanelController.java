package org.example.piclabel.controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import org.example.piclabel.utils.MetadataUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import javafx.scene.control.Label;

import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ControlpanelController {

    @FXML
    private Button addDateButton;

    @FXML
    private Button addAllDatesButton;

    @FXML
    private Button exportButton;

    @FXML
    private ProgressBar loadingProgress;

    @FXML
    private Label loadingLabel;

    @FXML
    private CarouselController carouselController;

    public static final String TOP_RIGHT = "top_right";
    public static final String TOP_LEFT = "top_left";
    public static final String BOTTOM_RIGHT = "bottom_right";
    public static final String BOTTOM_LEFT = "bottom_left";

    private static final int CONST = 35;

    private String corner = BOTTOM_RIGHT;

    @FXML
    public void addDate() {
        addDate(corner, carouselController.getCurrentIndex());
    }

    @FXML
    public void addAllDates() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {

                Platform.runLater(() -> {

                    GaussianBlur blur = new GaussianBlur(15);

                    ColorAdjust darken = new ColorAdjust();
                    darken.setBrightness(-0.4); // escurece

                    // Encadeando os efeitos
                    darken.setInput(blur); // aplica blur primeiro, depois escurece

                    // Aplicando ao ImageView
                    carouselController.getImageView().setEffect(darken);

                    carouselController.getBackwardButton().setDisable(true);
                    carouselController.getForwardButton().setDisable(true);
                    loadingProgress.setMinHeight(15.0);
                });

                int total = carouselController.getOriginalImages().size();

                for (int i = 0; i < total; i++) {
                    updateProgress(i, total);
                    addDate(corner, i);
                }

                Platform.runLater(() -> {
                    carouselController.showImage(0);
                    carouselController.setCurrentIndex(0);
                    carouselController.getImageView().setEffect(null);
                    carouselController.getBackwardButton().setDisable(false);
                    carouselController.getForwardButton().setDisable(false);
                    loadingProgress.setMinHeight(0.0);
                });

                return null;
            }
        };

        loadingProgress.progressProperty().bind(task.progressProperty());

        new Thread(task).start();
    }

    @FXML
    public void exportImages() {
        List<File> imageFiles = carouselController.getImageFiles();

        String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
        File baseDir = new File(desktopPath, "PicLabelExports");

        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String timestamp = LocalDateTime.now().format(formatter);
        File exportDir = new File(baseDir, "export_" + timestamp);

        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        for (File image : imageFiles) {
            Path sourcePath = image.toPath();
            Path targetPath = exportDir.toPath().resolve(image.getName());

            try {
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.err.println("Erro ao exportar " + image.getName());
                e.printStackTrace();
            }
        }

        System.out.println("Exportação concluída em: " + exportDir.getAbsolutePath());
    }

    public void addDate(String corner, int index) {
        File file = carouselController.getOriginalImages().get(index);

        try {
            BufferedImage bimg = ImageIO.read(file);
            String dateString = MetadataUtil.getImageDate(file);

            double height = bimg.getHeight(), width = bimg.getWidth();

            // coefficient is used to make the size of the date proportional to the image resolution
            double coefficient = width > height ? width / 1000 : height / 1000;

            int x, y;

            switch (corner) {
                case TOP_LEFT:
                    x = (int) ((CONST - 32) * coefficient);
                    y = (int) ((CONST - 5) * coefficient);
                    break;
                case TOP_RIGHT:
                    x = (int) (width - ((CONST + 180) * coefficient));
                    y = (int) ((CONST - 5) * coefficient);
                    break;
                case BOTTOM_LEFT:
                    x = (int) ((CONST - 32) * coefficient);
                    y = (int) (height - ((CONST - 30) * coefficient));
                    break;
                case BOTTOM_RIGHT:
                    x = (int) (width - ((CONST + 180) * coefficient));
                    y = (int) (height - ((CONST - 30) * coefficient));
                    break;
                default:
                    x = (int) (width - ((CONST + 180) * coefficient));
                    y = (int) (height - ((CONST - 30) * coefficient));
                    break;
            }

            Graphics2D g2d = bimg.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Cor retrô e transparência
            Color retroColor = new Color(255, 50, 50);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));

            Font font = new Font("Courier New", Font.BOLD, (int)(CONST * coefficient));
            g2d.setFont(font);

            // Outline (contorno)
            FontRenderContext frc = g2d.getFontRenderContext();
            TextLayout textLayout = new TextLayout(dateString, font, frc);
            AffineTransform transform = AffineTransform.getTranslateInstance(x, y);
            Shape outline = textLayout.getOutline(transform);

            // Contorno preto
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2f));
            g2d.draw(outline);

            // Texto principal
            g2d.setColor(retroColor);
            g2d.fill(outline);

            g2d.dispose();


            String userHome = System.getProperty("user.home");
            Path appImagesPath = Paths.get(userHome, "PicLabel", "processed");

            Files.createDirectories(appImagesPath);

            file = new File(appImagesPath.toFile(), file.getName());

            String ext = getFileExtension(file);

            ImageIO.write(bimg, ext, file);

            // esse platform.runlater é feito pois essa func é chamada dentro de uma task separada
            // e as alterações na UI tem que ser feitas por meio de platform.runlater para serem feitas na thread main
            File finalFile = file;
            Platform.runLater(() -> {
                carouselController.getImageFiles().set(index, finalFile);
                carouselController.showImage(index);
            });
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static String getFileExtension(File file) {
        String name = file.getName();
        int lastDot = name.lastIndexOf('.');
        if (lastDot > 0 && lastDot < name.length() - 1) {
            return name.substring(lastDot + 1).toLowerCase();
        } else {
            return ""; // sem extensão
        }
    }

    public CarouselController getCarouselController() {
        return carouselController;
    }

    public void setCarouselController(CarouselController carouselController) {
        this.carouselController = carouselController;
    }

    public String getCorner() {
        return corner;
    }

    public void setCorner(String corner) {
        this.corner = corner;
    }

    public void setCornerTopLeft() {
        this.corner = TOP_LEFT;
    }

    public void setCornerTopRight() {
        this.corner = TOP_RIGHT;
    }

    public void setCornerBottomLeft() {
        this.corner = BOTTOM_LEFT;
    }

    public void setCornerBottomRight() {
        this.corner = BOTTOM_RIGHT;
    }

    public Label getLoadingLabel() {
        return loadingLabel;
    }

    public void setLoadingLabel(Label loadingLabel) {
        this.loadingLabel = loadingLabel;
    }
}
