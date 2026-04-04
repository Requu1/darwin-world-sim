package agh.ics.oop.presenters;

import agh.ics.oop.animal.Animal;
import agh.ics.oop.animal.AnimalBox;
import agh.ics.oop.map.Boundary;
import agh.ics.oop.map.MapChangeListener;
import agh.ics.oop.map.RectangularMap;
import agh.ics.oop.other.Plant;
import agh.ics.oop.other.Vector2d;
import agh.ics.oop.simulation.Simulation;
import agh.ics.oop.simulation.SimulationChangeListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class SimulationPresenter implements MapChangeListener, SimulationChangeListener {
    private static final int CELL_SIZE = 30;
    private static final int DEFAULT_FONT_SIZE = 15;
    private static final int WIDTH_OFFSET = CELL_SIZE + CELL_SIZE / 2;
    private static final int HEIGHT_OFFSET = CELL_SIZE + CELL_SIZE / 2;

    private final HashMap<String, Image> imageCache = new HashMap<>();


    @FXML
    private Label simulationDayLabel;
    @FXML
    private Label worldTemperatureLabel;
    @FXML
    private ImageView seasonImage;

    @FXML
    private Label freePositionsLabel;
    @FXML
    private Label plantsCountLabel;
    @FXML
    private Label averageEnergyLevelLabel;
    @FXML
    private Label averageLifeSpanLabel;
    @FXML
    private Label mostPopularGenomeLabel;
    @FXML
    private Label averageChildrenCountLabel;
    @FXML
    private Label animalsCountLabel;

    @FXML
    private Label activeGeneLabel;
    @FXML
    private Label plantsEatenLabel;
    @FXML
    private Label energyLabel;
    @FXML
    private Label childrenCountLabel;
    @FXML
    private Label dayOfDeathLabel;
    @FXML
    private Label genomeSequenceLabel;
    @FXML
    private Label daysLivedLabel;
    @FXML
    private Canvas mapGrid;

    @FXML
    private Button pauseButton;
    @FXML
    private Label simulationStatusLabel;


    private RectangularMap map;
    private Simulation simulation;
    private Animal selectedAnimal = null;

    public void setWorldMap(RectangularMap map) {
        this.map = map;
        map.addListener(this);
        mapGrid.setOnMouseClicked(event -> handleCanvasClick(event.getX(), event.getY()));
        this.drawMap();
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    private void setGridSize() {
        Boundary bounds = this.map.getCurrentBounds();

        int mapWidth = bounds.upperRightCorner().x() - bounds.lowerLeftCorner().x() + 1;
        int mapHeight = bounds.upperRightCorner().y() - bounds.lowerLeftCorner().y() + 1;

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
        this.configureFont(graphics, Color.RED);

        drawPlants(graphics);

        drawAnimals(graphics);
    }

    private void drawAnimals(GraphicsContext graphics) {
        Vector2d elementPosOnTheMap;
        Vector2d elementsPosOnTheGrid;
        for (List<Animal> animalsOnTheSamePos : map.getPositionsWithAnimals()) {
            elementPosOnTheMap = animalsOnTheSamePos.getFirst().getPosition();
            elementsPosOnTheGrid = new Vector2d(
                    (elementPosOnTheMap.x() - map.getCurrentBounds().lowerLeftCorner().x()) * CELL_SIZE + WIDTH_OFFSET
                    , (map.getCurrentBounds().upperRightCorner().y() - elementPosOnTheMap.y()) * CELL_SIZE + HEIGHT_OFFSET);
            drawAnimalsInCell(graphics, elementsPosOnTheGrid.x(), elementsPosOnTheGrid.y(), animalsOnTheSamePos);
        }
    }

    private void drawPlants(GraphicsContext graphics) {
        Vector2d elementsPosOnTheGrid;
        Vector2d elementPosOnTheMap;
        for (Plant plant : map.getPlants()) {
            elementPosOnTheMap = plant.getPosition();
            elementsPosOnTheGrid = new Vector2d(
                    (elementPosOnTheMap.x() - map.getCurrentBounds().lowerLeftCorner().x()) * CELL_SIZE + WIDTH_OFFSET
                    , (map.getCurrentBounds().upperRightCorner().y() - elementPosOnTheMap.y()) * CELL_SIZE + HEIGHT_OFFSET);
            graphics.clearRect(elementsPosOnTheGrid.x(), elementsPosOnTheGrid.y(), (double) CELL_SIZE / 2 - 1, (double) CELL_SIZE / 2 - 1);
            graphics.setFill(Color.GREEN);
            graphics.fillRect(elementsPosOnTheGrid.x() - ((double) CELL_SIZE / 2 - 1), elementsPosOnTheGrid.y() - ((double) CELL_SIZE / 2 - 1),
                    CELL_SIZE - 2, CELL_SIZE - 2);
        }
    }

    private void drawAnimalsInCell(GraphicsContext gc, int x, int y, List<Animal> animals) {
        int animalsCount = animals.size();
        if (animalsCount == 0) return;

        double animalSize = (animalsCount > 1) ? CELL_SIZE / 2.5 : CELL_SIZE / 1.5;

        for (int i = 0; i < animalsCount; i++) {
            double offsetX = (i % 2) * (CELL_SIZE / 2.0);
            double offsetY = ((double) i / 2) * (CELL_SIZE / 2.0);

            if (i >= 4) {
                offsetX = Math.random() * (CELL_SIZE - animalSize);
                offsetY = Math.random() * (CELL_SIZE - animalSize);
            }

            double drawX = x + offsetX;
            double drawY = y + offsetY;

            new AnimalBox(animals.get(i), imageCache).draw(gc, drawX, drawY, animalSize, Objects.equals(animals.get(i), selectedAnimal));
        }
    }


    private void drawAxes(GraphicsContext graphics) {
        this.configureFont(graphics, Color.BLACK);

        int WIDTH_OFFSET = CELL_SIZE / 2;
        int HEIGHT_OFFSET = CELL_SIZE / 2;

        graphics.fillText("y/x", WIDTH_OFFSET, HEIGHT_OFFSET);

        int xCounter = map.getCurrentBounds().lowerLeftCorner().x();
        for (int x = CELL_SIZE + WIDTH_OFFSET; x < mapGrid.getWidth() + 1; x += CELL_SIZE) {
            graphics.fillText(String.format("%d", xCounter++), x, WIDTH_OFFSET);
        }

        int yCounter = map.getCurrentBounds().upperRightCorner().y();
        for (int y = CELL_SIZE + HEIGHT_OFFSET; y < mapGrid.getHeight() + 1; y += CELL_SIZE) {
            graphics.fillText(String.format("%d", yCounter--), HEIGHT_OFFSET, y);
        }
    }


    private void clearGrid() {
        GraphicsContext graphics = mapGrid.getGraphicsContext2D();
        graphics.setFill(Color.WHITE);
        graphics.fillRect(0, 0, mapGrid.getWidth(), mapGrid.getHeight());
    }

    private void configureFont(GraphicsContext graphics, Color color) {
        graphics.setTextAlign(TextAlignment.CENTER);
        graphics.setTextBaseline(VPos.CENTER);
        graphics.setFont(new Font("Arial", DEFAULT_FONT_SIZE));
        graphics.setFill(color);
    }

    private void handleCanvasClick(double mouseX, double mouseY) {
        Boundary bounds = map.getCurrentBounds();

        int x = (int) Math.floor((mouseX - WIDTH_OFFSET + (double) CELL_SIZE / 2) / CELL_SIZE) + bounds.lowerLeftCorner().x();
        int y = bounds.upperRightCorner().y() - (int) Math.floor((mouseY - HEIGHT_OFFSET + (double) CELL_SIZE / 2) / CELL_SIZE);

        Vector2d clickedPos = new Vector2d(x, y);
        List<Animal> animalsAtPos = map.animalsAtPos(clickedPos);

        if (animalsAtPos != null && !animalsAtPos.isEmpty()) {
            int currentIndex = -1;

            if (selectedAnimal != null) {
                currentIndex = animalsAtPos.indexOf(selectedAnimal);
            }

            if (currentIndex == -1) {
                selectedAnimal = animalsAtPos.getFirst();
            } else {
                selectedAnimal = animalsAtPos.get((currentIndex + 1) % animalsAtPos.size());
            }
        } else {
            selectedAnimal = null;
        }

        this.drawMap();
        updateAnimalStats();

    }

    private void updateAnimalStats() {
        if (selectedAnimal != null) {
            activeGeneLabel.setText("Active gene: " + selectedAnimal.getStats().getActiveGene());
            plantsEatenLabel.setText("Plants eaten: " + selectedAnimal.getStats().getPlantsEaten());
            energyLabel.setText("Energy: " + selectedAnimal.getStats().getEnergy());
            childrenCountLabel.setText("Children count: " + selectedAnimal.getStats().getChildrenCount());
            genomeSequenceLabel.setText("Genome: " + selectedAnimal.getStats().getGenomeSequence());
            daysLivedLabel.setText("Days lived: " + selectedAnimal.getStats().getDaysLived());

            if (selectedAnimal.getStats().getDayOfDeath() == -1) {
                dayOfDeathLabel.setText("Day of death: ");
            } else {
                dayOfDeathLabel.setText("Day of death: " + selectedAnimal.getStats().getDayOfDeath());
            }
        } else {
            activeGeneLabel.setText("Active Gene: ");
            plantsEatenLabel.setText("Plants eaten: ");
            energyLabel.setText("Energy: ");
            childrenCountLabel.setText("Children count: ");
            genomeSequenceLabel.setText("Genome: ");
            daysLivedLabel.setText("Days lived: ");
            dayOfDeathLabel.setText("Day of death: ");
        }
    }

    private void updateSimulationStats() {
        if (simulation != null) {
            freePositionsLabel.setText("Free positions: " + simulation.getLastSimulationStats().getFreePositionsCount());
            plantsCountLabel.setText("Plants count: " + simulation.getLastSimulationStats().getPlantsCount());
            averageEnergyLevelLabel.setText("Average energy level: " + simulation.getLastSimulationStats().getAvgEnergyLevel());
            mostPopularGenomeLabel.setText("Most popular genome: " + simulation.getLastSimulationStats().getMostPopularGenome());
            averageChildrenCountLabel.setText("Average children count: " + simulation.getLastSimulationStats().getAvgChildrenCount());
            animalsCountLabel.setText("Animals count: " + simulation.getLastSimulationStats().getAnimalsCount());

            if (simulation.getLastSimulationStats().getAvgAnimalsLifeSpan() == -1) {
                averageLifeSpanLabel.setText("Average life span: ");
            } else {
                averageLifeSpanLabel.setText("Average life span: " + simulation.getLastSimulationStats().getAvgAnimalsLifeSpan());
            }
        }
    }

    private void updateWeatherStats() {
        if (simulation != null) {
            Image image;
            if (simulation.getSeason().isWinter()) {
                image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/winter.png")));
            } else {
                image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/summer.png")));
            }
            seasonImage.setImage(image);
            worldTemperatureLabel.setText("World temperature: " + simulation.getSeason().getCurrentTemperature());
            simulationDayLabel.setText("Simulation day: " + simulation.getSeason().getCurrentDay());
        }
    }

    @FXML
    private void onPauseResumeClicked() {
        if (simulation != null) {
            if (pauseButton.getText().equals("Pause")) {
                simulationStatusLabel.setText("Simulation is stopped");
                pauseButton.setText("Resume");
            } else {
                simulationStatusLabel.setText("Simulation is running");
                pauseButton.setText("Pause");
            }
            simulation.togglePause();
        }
    }

    @Override
    public void mapChanged(RectangularMap map, String message) {
        Platform.runLater(() -> {
            this.drawMap();
            updateAnimalStats();
        });
    }

    @Override
    public void simulationChanged(Simulation simulation) {
        Platform.runLater(() -> {
            updateSimulationStats();
            updateWeatherStats();
        });
    }
}
