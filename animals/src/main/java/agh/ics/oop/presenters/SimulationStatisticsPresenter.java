package agh.ics.oop.presenters;

import agh.ics.oop.simulation.Simulation;
import agh.ics.oop.simulation.SimulationChangeListener;
import agh.ics.oop.simulation.SimulationStats;
import agh.ics.oop.simulation.StatisticsType;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;

import java.util.List;

public class SimulationStatisticsPresenter implements SimulationChangeListener {
    @FXML
    private LineChart<Number, Number> simulationChart;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private ComboBox<StatisticsType> statisticsComboBox;

    private Simulation simulation;
    private XYChart.Series<Number, Number> displayedSeries;

    public void initializeChart(Simulation simulation) {
        setSimulation(simulation);
        setupChart();
        setupComboBox();
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
        reloadChartData();
    }

    private void setupChart() {
        simulationChart.setAnimated(false);
        simulationChart.setCreateSymbols(false);

        configureAxisX();
        configureAxisY();

        displayedSeries = new XYChart.Series<>();
        simulationChart.getData().add(displayedSeries);

    }

    private void configureAxisY() {
        yAxis.setAutoRanging(true);
        yAxis.setTickUnit(1);
        yAxis.setMinorTickVisible(false);
    }

    private void configureAxisX() {
        xAxis.setAutoRanging(false);
        xAxis.setForceZeroInRange(false);
        xAxis.setLowerBound(1);
        xAxis.setUpperBound(simulation.getLastSimulationStats().getSimulationDay());
        xAxis.setTickUnit(1);
        xAxis.setMinorTickVisible(false);
    }

    private void setupComboBox() {
        statisticsComboBox.getItems().addAll(StatisticsType.values());
        statisticsComboBox.setValue(StatisticsType.ANIMAL_COUNT);
        statisticsComboBox.setOnAction(_ -> reloadChartData());
    }

    private void reloadChartData() {
        Platform.runLater(() -> {
            displayedSeries.getData().clear();

            StatisticsType selectedStat = statisticsComboBox.getValue();
            if (simulation == null || selectedStat == null) {
                return;
            }

            updateChartHeader();
            List<SimulationStats> history = simulation.getStatsHistory();

            for (SimulationStats stats : history) {
                int day = stats.getSimulationDay();
                Number value = selectedStat.apply(stats);
                displayedSeries.getData().add(new XYChart.Data<>(day, value));
            }
        });

    }

    private void updateChartHeader() {
        StatisticsType selected = statisticsComboBox.getValue();
        if (selected != null) {
            String label = selected.toString();
            yAxis.setLabel(label);
            displayedSeries.setName(label);
        }
    }


    @Override
    public void simulationChanged(Simulation simulation) {
        Platform.runLater(() -> {
            StatisticsType selectedStat = statisticsComboBox.getValue();
            if (simulation == null || selectedStat == null) {
                return;
            }
            xAxis.setUpperBound(simulation.getLastSimulationStats().getSimulationDay());
            SimulationStats lastStats = simulation.getLastSimulationStats();
            if (lastStats != null) {
                int day = lastStats.getSimulationDay();
                Number value = selectedStat.apply(lastStats);
                displayedSeries.getData().add(new XYChart.Data<>(day, value));
            }
        });
    }
}
