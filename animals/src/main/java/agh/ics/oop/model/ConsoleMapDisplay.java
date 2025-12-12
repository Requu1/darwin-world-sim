package agh.ics.oop.model;

public class ConsoleMapDisplay implements MapChangeListener {
    private int countUpdates = 0;

    @Override
    public synchronized void mapChanged(WorldMap worldMap, String message) {
        System.out.println(String.format("%s: %s", worldMap.getId(), message));
        System.out.println(String.format("Map updates:%d", ++countUpdates));
        System.out.println(worldMap);
    }

    public int getCountUpdates() {
        return this.countUpdates;
    }
}
