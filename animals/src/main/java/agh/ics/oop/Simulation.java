package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {
    private final ArrayList<MoveDirection> directions;
    private final ArrayList<Animal> animals = new ArrayList<>();
    private final WorldMap map;

    public Simulation(List<Vector2d> animalsPositions, ArrayList<MoveDirection> directions, WorldMap map) {
        this.directions = directions;
        this.map = map;
        initializeAnimals(animalsPositions);
    }

    private void initializeAnimals(List<Vector2d> animalsPositions) {
        for (Vector2d pos : animalsPositions) {
            try {
                map.place(new Animal(pos));
                this.animals.add(new Animal(pos));
            } catch (IncorrectPositionException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        int animalsCount = animals.size();

        if (animalsCount > 0) {
            int currAnimalIndex = 0;
            for (MoveDirection nextMove : this.directions) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                map.move(this.animals.get(currAnimalIndex), nextMove);
                currAnimalIndex = (currAnimalIndex + 1) % animalsCount;
            }
        } else {
            System.out.println("No animals to move");
        }
    }

    public ArrayList<Animal> getAnimals() {
        return this.animals;
    }
}
