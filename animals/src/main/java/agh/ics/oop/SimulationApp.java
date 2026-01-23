package agh.ics.oop;

import agh.ics.oop.model.RectangularMap;
import agh.ics.oop.presenter.SimulationPresenter;
import agh.ics.oop.presenter.SimulationStatisticsPresenter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.UUID;

public class SimulationApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulationStarter.fxml"));
        BorderPane viewRoot = loader.load();
        configureStage(primaryStage, viewRoot);
        primaryStage.show();

        primaryStage.setOnCloseRequest(_ -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public void launchSimulation(RectangularMap map, Simulation simulation) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();
        SimulationPresenter presenter = loader.getController();
        presenter.setWorldMap(map);
        presenter.setSimulation(simulation);
        map.addListener(presenter);
        simulation.addListener(presenter);
        //map.addListener(new FileMapDisplay());
        Stage stage = new Stage();
        configureStage(stage, viewRoot);
        stage.setTitle("Simulation app with MapID: " + map.getId());
        stage.show();
        stage.setOnCloseRequest(_ -> {
            map.removeListener(presenter);
            simulation.removeListener(presenter);
        });

        launchSimulationStatistics(simulation, map.getId());
    }

    private void launchSimulationStatistics(Simulation simulation, UUID mapID) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulationStatistics.fxml"));
        BorderPane viewRoot = loader.load();
        SimulationStatisticsPresenter presenter = loader.getController();
        presenter.initializeChart(simulation);
        simulation.addListener(presenter);
        Stage stage = new Stage();
        configureStage(stage, viewRoot);
        stage.setTitle("Simulation stats with MapID: " + mapID);
        stage.show();
        stage.setOnCloseRequest(_ ->
                simulation.removeListener(presenter));

    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}
