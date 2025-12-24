package agh.ics.oop.model;

import java.util.Objects;

public class Plant implements WorldElement {
    private final Vector2d posVector;

    public Plant(Vector2d posVector) {
        this.posVector = posVector;
    }

    @Override
    public String toString() {
        return "*";
    }

    @Override
    public boolean isAt(Vector2d position) {
        return Objects.equals(position, this.posVector);
    }

    @Override
    public Vector2d getPosition() {
        return this.posVector;
    }
}
