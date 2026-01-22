package agh.ics.oop.model;

import java.io.FileWriter;

public class FileMapDisplay implements MapChangeListener {
    @Override
    public void mapChanged(RectangularMap worldMap, String message) {
        String fileName = "map_" + worldMap.getId() + ".log";
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write("Ruch:" + message + "\n");
            writer.write(worldMap.toString());
            writer.write("\n--\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
