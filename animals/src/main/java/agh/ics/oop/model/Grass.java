package agh.ics.oop.model;

import java.util.Objects;

public record Grass(Vector2d position) implements WorldElement {
    @Override
    public String toString() {
        return "*";
    }

    @Override
    public boolean isAt(Vector2d position) {
        return Objects.equals(position, this.position);
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }
}
