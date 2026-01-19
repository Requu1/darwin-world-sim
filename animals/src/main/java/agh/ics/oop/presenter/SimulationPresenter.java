package agh.ics.oop.presenter;

import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.List;
import java.util.Objects;


public class SimulationPresenter implements MapChangeListener {
    private static final int CELL_SIZE = 30;

    private static final int WIDTH_OFFSET = CELL_SIZE + CELL_SIZE / 2;
    private static final int HEIGHT_OFFSET = CELL_SIZE + CELL_SIZE / 2;
    @FXML
    private Label animalLifeLabel;
    @FXML
    private Canvas mapGrid;

    private AbstractWorldMap map;
    private Animal selectedAnimal = null;


    public void setWorldMap(AbstractWorldMap map) {
        this.map = map;
        map.addListener(this);
        mapGrid.setOnMouseClicked(event -> handleCanvasClick(event.getX(), event.getY()));
        this.drawMap();
    }

    private void setGridSize() {
        Boundary bounds = this.map.getCurrentBounds();

        int mapWidth = bounds.upperRightCorner().getX() - bounds.lowerLeftCorner().getX() + 1;
        int mapHeight = bounds.upperRightCorner().getY() - bounds.lowerLeftCorner().getY() + 1;

        mapGrid.setHeight(mapHeight * CELL_SIZE + CELL_SIZE);
        mapGrid.setWidth(mapWidth * CELL_SIZE + CELL_SIZE);
    }

    private void drawMap() {
        this.clearGrid();
        this.setGridSize();

        GraphicsContext graphics = mapGrid.getGraphicsContext2D();
        graphics.setStroke(Color.BLACK);
        graphics.setLineWidth(2.0);

        for (int x = 0; x < mapGrid.getWidth() + 1 + CELL_SIZE; x += CELL_SIZE) {
            graphics.strokeLine(x, 0, x, mapGrid.getHeight());
        }

        for (int y = 0; y < mapGrid.getHeight() + 1 + CELL_SIZE; y += CELL_SIZE) {
            graphics.strokeLine(0, y, mapGrid.getWidth(), y);
        }
        this.drawAxes(graphics);
        this.drawElements(graphics);
    }

    private void drawElements(GraphicsContext graphics) {
        this.configureFont(graphics, 15, Color.RED);
        Vector2d elementPosOnTheMap;
        Vector2d elementsPosOnTheGrid;

        for (Plant plant : map.getPlants()) {
            elementPosOnTheMap = plant.getPosition();
            elementsPosOnTheGrid = new Vector2d(
                    (elementPosOnTheMap.getX() - map.getCurrentBounds().lowerLeftCorner().getX()) * CELL_SIZE + WIDTH_OFFSET
                    , (map.getCurrentBounds().upperRightCorner().getY() - elementPosOnTheMap.getY()) * CELL_SIZE + HEIGHT_OFFSET);
            graphics.clearRect(elementsPosOnTheGrid.getX(), elementsPosOnTheGrid.getY(), (double) CELL_SIZE / 2 - 1, (double) CELL_SIZE / 2 - 1);
            graphics.setFill(Color.GREEN);
            graphics.fillRect(elementsPosOnTheGrid.getX() - ((double) CELL_SIZE / 2 - 1), elementsPosOnTheGrid.getY() - ((double) CELL_SIZE / 2 - 1),
                    CELL_SIZE - 1, CELL_SIZE - 1);
        }

        for (List<Animal> animalsOnTheSamePos : map.getPositionsWithAnimals()) {
            elementPosOnTheMap = animalsOnTheSamePos.getFirst().getPosition();
            elementsPosOnTheGrid = new Vector2d(
                    (elementPosOnTheMap.getX() - map.getCurrentBounds().lowerLeftCorner().getX()) * CELL_SIZE + WIDTH_OFFSET
                    , (map.getCurrentBounds().upperRightCorner().getY() - elementPosOnTheMap.getY()) * CELL_SIZE + HEIGHT_OFFSET);
            drawAnimalsInCell(graphics, elementsPosOnTheGrid.getX(), elementsPosOnTheGrid.getY(), animalsOnTheSamePos);
            //new WorldElementBox(animal).draw(graphics, elementsPosOnTheGrid.getX(), elementsPosOnTheGrid.getY(), ELEMENT_SIZE);}
        }
    }

    private void drawAnimalsInCell(GraphicsContext gc, int x, int y, List<Animal> animals) {
        int animalsCount = animals.size();
        if (animalsCount == 0) return;

        double animalSize = (animalsCount > 1) ? CELL_SIZE / 2.5 : CELL_SIZE / 1.5;

        for (int i = 0; i < animalsCount; i++) {
            double offsetX = (i % 2) * (CELL_SIZE / 2.0);
            double offsetY = (i / 2) * (CELL_SIZE / 2.0);

            if (i >= 4) {
                offsetX = Math.random() * (CELL_SIZE - animalSize);
                offsetY = Math.random() * (CELL_SIZE - animalSize);
            }

            double drawX = x + offsetX;
            double drawY = y + offsetY;

            new AnimalBox(animals.get(i)).draw(gc, drawX, drawY, animalSize, Objects.equals(animals.get(i), selectedAnimal));
        }
    }


    private void drawAxes(GraphicsContext graphics) {
        this.configureFont(graphics, 15, Color.BLACK);

        int WIDTH_OFFSET = CELL_SIZE / 2;
        int HEIGHT_OFFSET = CELL_SIZE / 2;

        graphics.fillText("y/x", WIDTH_OFFSET, HEIGHT_OFFSET);

        int xCounter = map.getCurrentBounds().lowerLeftCorner().getX();
        for (int x = CELL_SIZE + WIDTH_OFFSET; x < mapGrid.getWidth() + 1; x += CELL_SIZE) {
            graphics.fillText(String.format("%d", xCounter++), x, WIDTH_OFFSET);
        }

        int yCounter = map.getCurrentBounds().upperRightCorner().getY();
        for (int y = CELL_SIZE + HEIGHT_OFFSET; y < mapGrid.getHeight() + 1; y += CELL_SIZE) {
            graphics.fillText(String.format("%d", yCounter--), HEIGHT_OFFSET, y);
        }
    }


    private void clearGrid() {
        GraphicsContext graphics = mapGrid.getGraphicsContext2D();
        graphics.setFill(Color.WHITE);
        graphics.fillRect(0, 0, mapGrid.getWidth(), mapGrid.getHeight());
    }

    private void configureFont(GraphicsContext graphics, int size, Color color) {
        graphics.setTextAlign(TextAlignment.CENTER);
        graphics.setTextBaseline(VPos.CENTER);
        graphics.setFont(new Font("Arial", size));
        graphics.setFill(color);
    }

    private void handleCanvasClick(double mouseX, double mouseY) {
        Boundary bounds = map.getCurrentBounds();

        int x = (int) Math.floor((mouseX - WIDTH_OFFSET + (double) CELL_SIZE / 2) / CELL_SIZE) + bounds.lowerLeftCorner().getX();
        int y = bounds.upperRightCorner().getY() - (int) Math.floor((mouseY - HEIGHT_OFFSET + (double) CELL_SIZE / 2) / CELL_SIZE);

        Vector2d clickedPos = new Vector2d(x, y);
        List<Animal> animalsAtPos = map.animalsAtPos(clickedPos);

        if (animalsAtPos != null && !animalsAtPos.isEmpty()) {
            int currentIndex = -1;

            if (selectedAnimal != null) {
                currentIndex = animalsAtPos.indexOf(selectedAnimal);
            }

            if (currentIndex == -1) {
                selectedAnimal = animalsAtPos.get(0);
            } else {
                selectedAnimal = animalsAtPos.get((currentIndex + 1) % animalsAtPos.size());
            }
        } else {
            selectedAnimal = null;
        }

        Platform.runLater(() -> {
            drawMap();
            updateAnimalStats();
        });
    }

    private void updateAnimalStats() {
        if (selectedAnimal != null) {
            animalLifeLabel.setText("Energia: " + selectedAnimal.getEnergy() +
                    " | Geny: " + selectedAnimal.getGenome().getSequence() + " | Pozycja: " + selectedAnimal.getPosition());
        } else {
            animalLifeLabel.setText("Nie wybrano żadnego zwierzaka");
        }
    }


    @Override
    public void mapChanged(WorldMap map, String message) {
        Platform.runLater(() -> {
            this.drawMap();
            updateAnimalStats();
        });
    }

}
