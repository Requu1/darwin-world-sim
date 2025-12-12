package agh.ics.oop;

import agh.ics.oop.model.ConsoleMapDisplay;
import agh.ics.oop.model.GrassField;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
    public static void main(String[] args) {
        System.out.println("System wystartował");

        int argsCount = args.length;
        int currCounter = 0;

        System.out.print("Podane argumenty:");
        while (currCounter < argsCount) {
            System.out.print(args[currCounter++]);

            if (currCounter < argsCount) {
                System.out.print(", ");
            }
        }
        System.out.println();

        ArrayList<MoveDirection> directions;
        try {
            directions = OptionsParser.parse(args);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return;
        }
        System.out.println();

        ConsoleMapDisplay listener = new ConsoleMapDisplay();
        ArrayList<Simulation> simulations = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            GrassField map = new GrassField(10);
            map.addListener(listener);
            List<Vector2d> positions = generatePositions();
            Simulation simulation = new Simulation(positions, directions, map);
            simulations.add(simulation);
        }

        SimulationEngine engine = new SimulationEngine(simulations);
        try {
            engine.runAsyncInThreadPool();
            engine.awaitSimulationsEnd();
        } catch (InterruptedException e) {
            System.err.println("Thread interrupted:" + e);
        }

        System.out.println("System zakończył działanie");
    }

    public static List<Vector2d> generatePositions() {
        Random randomPos = new Random();
        ArrayList<Vector2d> positions = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            positions.add(new Vector2d(randomPos.nextInt(10), randomPos.nextInt(10)));
        }
        return positions;
    }
}
