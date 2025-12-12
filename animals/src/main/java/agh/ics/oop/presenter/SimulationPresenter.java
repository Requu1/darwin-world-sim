package agh.ics.oop.presenter;

import agh.ics.oop.OptionsParser;
import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationApp;
import agh.ics.oop.World;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class SimulationPresenter implements MapChangeListener {
    private static final int CELL_WIDTH = 30;
    private static final int CELL_HEIGHT = 30;

    private static final int WIDTH_OFFSET = CELL_WIDTH + CELL_WIDTH / 2;
    private static final int HEIGHT_OFFSET = CELL_HEIGHT + CELL_HEIGHT / 2;

    @FXML
    private TextField inputTextField;
    @FXML
    private Label moveInfoLabel;
    @FXML
    private Canvas mapGrid;

    private AbstractWorldMap map;


    public void setWorldMap(AbstractWorldMap map) {
        this.map = map;
        map.addListener(this);
        this.drawMap();
    }

    private void setGridSize() {
        Boundary bounds = this.map.getCurrentBounds();

        int mapWidth = bounds.upperRightCorner().getX() - bounds.lowerLeftCorner().getX() + 1;
        int mapHeight = bounds.upperRightCorner().getY() - bounds.lowerLeftCorner().getY() + 1;

        mapGrid.setHeight(mapHeight * CELL_HEIGHT + CELL_HEIGHT);
        mapGrid.setWidth(mapWidth * CELL_WIDTH + CELL_WIDTH);
    }

    private void drawMap() {
        this.clearGrid();
        this.setGridSize();

        GraphicsContext graphics = mapGrid.getGraphicsContext2D();
        graphics.setStroke(Color.BLACK);
        graphics.setLineWidth(2.0);

        for (int x = 0; x < mapGrid.getWidth() + 1 + CELL_WIDTH; x += CELL_WIDTH) {
            graphics.strokeLine(x, 0, x, mapGrid.getHeight());
        }

        for (int y = 0; y < mapGrid.getHeight() + 1 + CELL_HEIGHT; y += CELL_HEIGHT) {
            graphics.strokeLine(0, y, mapGrid.getWidth(), y);
        }
        this.drawAxes(graphics);
        this.drawElements(graphics);
    }

    private void drawElements(GraphicsContext graphics) {
        this.configureFont(graphics, 15, Color.RED);
        ArrayList<WorldElement> elements = map.getElements();
        Vector2d elementPosOnTheMap;
        Vector2d elementsPosOnTheGrid;

        HashMap<Vector2d, WorldElement> drawnElements = new HashMap<>();

        for (WorldElement element : elements) {
            if (canDraw(element, drawnElements)) {
                elementPosOnTheMap = element.getPosition();
                elementsPosOnTheGrid = new Vector2d(
                        (elementPosOnTheMap.getX() - map.getCurrentBounds().lowerLeftCorner().getX()) * CELL_WIDTH + WIDTH_OFFSET
                        , (map.getCurrentBounds().upperRightCorner().getY() - elementPosOnTheMap.getY()) * CELL_HEIGHT + HEIGHT_OFFSET);
                graphics.clearRect(elementsPosOnTheGrid.getX(), elementsPosOnTheGrid.getY(), (double) CELL_WIDTH / 2 - 1, (double) CELL_HEIGHT / 2 - 1);
                graphics.fillText(element.toString(), elementsPosOnTheGrid.getX(), elementsPosOnTheGrid.getY());
                drawnElements.put(element.getPosition(), element);
            }
        }
    }

    private boolean canDraw(WorldElement element, HashMap<Vector2d, WorldElement> drawnElements) {
        if (element instanceof Animal) {
            return true;
        } else return !(element instanceof Grass) || drawnElements.get(element.getPosition()) == null;
    }

    private void drawAxes(GraphicsContext graphics) {
        this.configureFont(graphics, 15, Color.BLACK);

        int WIDTH_OFFSET = CELL_WIDTH / 2;
        int HEIGHT_OFFSET = CELL_HEIGHT / 2;

        graphics.fillText("y/x", WIDTH_OFFSET, HEIGHT_OFFSET);

        int xCounter = map.getCurrentBounds().lowerLeftCorner().getX();
        for (int x = CELL_WIDTH + WIDTH_OFFSET; x < mapGrid.getWidth() + 1; x += CELL_WIDTH) {
            graphics.fillText(String.format("%d", xCounter++), x, WIDTH_OFFSET);
        }

        int yCounter = map.getCurrentBounds().upperRightCorner().getY();
        for (int y = CELL_HEIGHT + HEIGHT_OFFSET; y < mapGrid.getHeight() + 1; y += CELL_HEIGHT) {
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

    @FXML
    public void onSimulationStartClicked() {
        ArrayList<MoveDirection> directions = OptionsParser.parse(inputTextField.getText().split(" "));

        SimulationApp simApp = new SimulationApp();
        GrassField map = new GrassField(10);
        try {
            simApp.launchSimulation(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Simulation simulation = new Simulation(World.generatePositions(), directions, map);
        Thread thread = new Thread(simulation);
        thread.start();

    }

    @Override
    public void mapChanged(WorldMap map, String message) {
        Platform.runLater(() -> {
            this.drawMap();
            moveInfoLabel.setText(message);
        });
    }
}
