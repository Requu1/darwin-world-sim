package agh.ics.oop.model;

public class SeasonManager {
    private final static int SUMMER_TEMPERATURE = 20;
    private final static double BODY_TEMP_CHANGE_FACTOR = 0.1;

    private final int seasonDuration;
    private final double minTemperature;
    private int currentDay = 0;

    public SeasonManager(int seasonDuration, double minTemperature) {
        this.seasonDuration = seasonDuration;
        this.minTemperature = minTemperature;
    }

    public boolean isWinter() {
        return (currentDay / seasonDuration) % 2 != 0;
    }

    public void nextDay() {
        this.currentDay += 1;
    }

    public double getCurrentTemperature() {
        if (!isWinter()) {
            return SUMMER_TEMPERATURE;
        }

        int dayInWinter = currentDay % seasonDuration;
        int halfWinter = seasonDuration / 2;

        if (dayInWinter <= halfWinter) {
            return (minTemperature / halfWinter) * dayInWinter;
        } else {
            return minTemperature - (minTemperature / halfWinter) * (dayInWinter - halfWinter);
        }
    }

    public int calcBodyTempChange(int animalBodyTemperature) {
        double currentTemp = this.getCurrentTemperature();

        int bodyTempChange = (int) Math.round((currentTemp - animalBodyTemperature) * BODY_TEMP_CHANGE_FACTOR);

        if (animalBodyTemperature > currentTemp) {
            bodyTempChange /= 2;
        }

        return bodyTempChange;
    }

}
