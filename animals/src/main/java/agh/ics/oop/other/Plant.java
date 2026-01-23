package agh.ics.oop.other;

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
    public Vector2d getPosition() {
        return this.posVector;
    }
}
