package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationApp;
import agh.ics.oop.World;
import agh.ics.oop.model.RectangularMap;
import agh.ics.oop.model.util.SimulationBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;

import java.io.IOException;

public class SimulationStarterPresenter {
    @FXML
    private Spinner<Integer> mapWidthInput;
    @FXML
    private Spinner<Integer> mapHeightInput;
    @FXML
    private Spinner<Integer> startingPlantsCountInput;
    @FXML
    private Spinner<Integer> plantEnergyRestoreInput;
    @FXML
    private Spinner<Integer> dailyGrowingPlantsInput;
    @FXML
    private Spinner<Integer> startingAnimalCountInput;
    @FXML
    private Spinner<Integer> defaultEnergyInput;
    @FXML
    private Spinner<Integer> energyLossInput;
    @FXML
    private Spinner<Integer> reproductionEnergyInput;
    @FXML
    private Spinner<Integer> genomeLengthInput;
    @FXML
    private Spinner<Integer> minMutationsInput;
    @FXML
    private Spinner<Integer> maxMutationsInput;
    @FXML
    private Spinner<Integer> canReproduceEnergyForAnimalInput;
    @FXML
    private Spinner<Integer> minTemperatureInput;
    @FXML
    private Spinner<Integer> seasonDurationInput;
    @FXML
    private Spinner<Integer> warmDistanceInput;

    @FXML
    public void onSimulationStartClicked() {
        try {
            checkSimulationParameters();
        } catch (Exception e) {
            System.err.print(e);
        }

        SimulationApp simApp = new SimulationApp();
        RectangularMap map = new RectangularMap(mapWidthInput.getValue(), mapHeightInput.getValue(), startingPlantsCountInput.getValue());

        Simulation simulation = new SimulationBuilder()
                .withAnimalsPositions(World.generatePositions(
                        startingAnimalCountInput.getValue(),
                        mapWidthInput.getValue(),
                        mapHeightInput.getValue()))
                .withMap(map)
                .withDailyEnergyLoss(energyLossInput.getValue())
                .withMinTemperature(minTemperatureInput.getValue())
                .withPlantsGrowingDaily(dailyGrowingPlantsInput.getValue())
                .withSeasonDuration(seasonDurationInput.getValue())
                .withGenomeLength(genomeLengthInput.getValue())
                .withStartingAnimalEnergy(defaultEnergyInput.getValue())
                .withWarmDistance(warmDistanceInput.getValue())
                .withEnergyRestoredByPlant(plantEnergyRestoreInput.getValue())
                .withMinimalEnergyForReproduction(canReproduceEnergyForAnimalInput.getValue())
                .withUsedEnergyForReproduction(reproductionEnergyInput.getValue())
                .withMaxMutationsCount(maxMutationsInput.getValue())
                .withMinMutationsCount(minMutationsInput.getValue())
                .build();
        try {
            simApp.launchSimulation(map, simulation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread thread = Thread.ofVirtual().start(simulation);
        thread.start();
    }

    private void checkSimulationParameters() {
        if (minMutationsInput.getValue() > maxMutationsInput.getValue()) {
            throw new IllegalArgumentException("Max mutations cannot be greater than minimal");
        }
    }

}
