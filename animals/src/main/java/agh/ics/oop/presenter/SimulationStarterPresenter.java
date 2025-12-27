package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationApp;
import agh.ics.oop.World;
import agh.ics.oop.model.RectangularMap;
import agh.ics.oop.model.util.RectangularMapBuilder;
import agh.ics.oop.model.util.SimulationBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class SimulationStarterPresenter {
    @FXML
    private TextField mapWidthInput;
    @FXML
    private TextField mapHeightInput;
    @FXML
    private TextField startingPlantsCountInput;
    @FXML
    private TextField plantEnergyRestoreInput;
    @FXML
    private TextField dailyGrowingPlantsInput;
    @FXML
    private TextField startingAnimalCountInput;
    @FXML
    private TextField defaultEnergyInput;
    @FXML
    private TextField energyLossInput;
    @FXML
    private TextField reproductionEnergyInput;
    @FXML
    private TextField genomeLengthInput;
    @FXML
    private TextField minMutationsInput;
    @FXML
    private TextField maxMutationsInput;
    @FXML
    private TextField canReproduceEnergyForAnimalInput;

    @FXML
    public void onSimulationStartClicked() {
        SimulationApp simApp = new SimulationApp();
        RectangularMap map = new RectangularMapBuilder()
                .withHeight(getIntFromTextField(mapHeightInput))
                .withWidth(getIntFromTextField(mapWidthInput))
                .withStartingPlantsCount(getIntFromTextField(startingPlantsCountInput))
                .withMaxMutationsCount(getIntFromTextField(maxMutationsInput))
                .withMinMutationsCount(getIntFromTextField(minMutationsInput))
                .withEnergyRestoredByPlant(getIntFromTextField(plantEnergyRestoreInput))
                .withUsedEnergyForReproduction(getIntFromTextField(reproductionEnergyInput))
                .withMinimalEnergyForReproduction(getIntFromTextField(canReproduceEnergyForAnimalInput))
                .create();
        try {
            simApp.launchSimulation(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Simulation simulation = new SimulationBuilder()
                .withAnimalsPositions(World.generatePositions(getIntFromTextField(startingAnimalCountInput)))
                .withMap(map)
                .withGenomeLength(getIntFromTextField(genomeLengthInput))
                .withStartingAnimalEnergy((getIntFromTextField(defaultEnergyInput)))
                .build();
        Thread thread = new Thread(simulation);
        thread.start();

    }

    private int getIntFromTextField(TextField text) {
        return Integer.parseInt(text.getText());
    }
}
