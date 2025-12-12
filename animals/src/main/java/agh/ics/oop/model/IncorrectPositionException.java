package agh.ics.oop.model;

public class IncorrectPositionException extends Exception {
    public IncorrectPositionException(Vector2d vector) {
        super(String.format("Position  (%d,%d) is not correct", vector.getX(), vector.getY()));
    }
}
