package org.example.piclabel.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.example.piclabel.utils.MetadataUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ControlpanelController {

    @FXML
    private Button addDateButton;

    @FXML
    private Button addAllDatesButton;

    @FXML
    private CarouselController carouselController;

    public static final String TOP_RIGHT = "top_right";
    public static final String TOP_LEFT = "top_left";
    public static final String BOTTOM_RIGHT = "bottom_right";
    public static final String BOTTOM_LEFT = "bottom_left";

    private String corner = BOTTOM_RIGHT;

    @FXML
    public void addDate() {
        addDate(corner, carouselController.getCurrentIndex());
    }

    @FXML
    public void addAllDates() {
        for (int i = 0; i < carouselController.getOriginalImages().size(); i++) {
            addDate(corner, i);
        }

        carouselController.showImage(0);
        carouselController.setCurrentIndex(0);
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
                    x = (int) (20 * coefficient);
                    y = (int) (65 * coefficient);
                    break;
                case TOP_RIGHT:
                    x = (int) (width - (310 * coefficient));
                    y = (int) (65 * coefficient);
                    break;
                case BOTTOM_LEFT:
                    x = (int) (20 * coefficient);
                    y = (int) (height - (20 * coefficient));
                    break;
                case BOTTOM_RIGHT:
                    x = (int) (width - (310 * coefficient));
                    y = (int) (height - (20 * coefficient));
                    break;
                default:
                    x = (int) (width - (310 * coefficient));
                    y = (int) (height - (20 * coefficient));
                    break;
            }

            Graphics2D g2d = bimg.createGraphics();
            Font font = new Font("Monospaced", Font.BOLD, (int)(50 * coefficient));
            g2d.setFont(font);
            g2d.setColor(Color.RED);
            g2d.drawString(dateString, x, y);
            g2d.dispose();

            file = new File("C:\\Users\\Usuario\\Desktop\\CODING\\FERRO3\\PicLabel\\src\\main\\resources\\pics\\" + file.getName());

            ImageIO.write(bimg, "jpg", file);

            carouselController.getImageFiles().set(index, file);
            carouselController.showImage(index);

        } catch (IOException ioe) {
            ioe.printStackTrace();
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
}
