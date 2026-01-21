package agh.ics.oop.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Objects;

public class AnimalBox {
    private static final HashMap<String, Image> imageCache = new HashMap<>();
    private final Animal animal;
    private final Image image;

    public AnimalBox(Animal animal) {
        this.animal = animal;
        String resourceName = animal.getResourceName();

        if (imageCache.get(resourceName) != null) {
            this.image = imageCache.get(resourceName);
        } else {
            this.image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/" + resourceName)));
            imageCache.put(resourceName, image);
        }
    }

    public void draw(GraphicsContext gc, double x, double y, double size, boolean selectedAnimal) {
        x = x - size / 2;
        y = y - size / 2;
        gc.save();
        gc.beginPath();
        gc.arc(x + size / 2, y + size / 2, size / 2, size / 2, 0, 360);
        gc.clip();
        gc.drawImage(image, x, y, size, size);
        gc.restore();

        gc.setStroke(selectedAnimal ? Color.RED : Color.BLACK);
        gc.setLineWidth(selectedAnimal ? 3 : 1);
        gc.strokeOval(x, y, size, size);

        gc.restore();

        double health = animal.getEnergyRatio();
        gc.setFill(Color.GRAY);
        gc.fillRect(x, y + size + 1, size, 3);
        gc.setFill(health > 0.3 ? Color.GREEN : Color.RED);
        gc.fillRect(x, y + size + 1, size * Math.min(health, 1.0), 3);
    }


}
