package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationApp;
import agh.ics.oop.World;
import agh.ics.oop.model.RectangularMap;
import agh.ics.oop.model.util.RectangularMapBuilder;
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
        RectangularMap map = new RectangularMapBuilder()
                .withHeight(mapHeightInput.getValue())
                .withWidth(mapWidthInput.getValue())
                .withStartingPlantsCount(startingPlantsCountInput.getValue())

                //do symulacji
                .withMaxMutationsCount(maxMutationsInput.getValue())
                .withMinMutationsCount(minMutationsInput.getValue())
                .withEnergyRestoredByPlant(plantEnergyRestoreInput.getValue())
                .withUsedEnergyForReproduction(reproductionEnergyInput.getValue())
                .withMinimalEnergyForReproduction(canReproduceEnergyForAnimalInput.getValue())
                .create();

        try {
            simApp.launchSimulation(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                .build();
        Thread thread = new Thread(simulation);
        thread.start();
    }

    private void checkSimulationParameters() {
        if (minMutationsInput.getValue() > maxMutationsInput.getValue()) {
            throw new IllegalArgumentException("Max mutations cannot be greater than minimal");
        }
    }

}
