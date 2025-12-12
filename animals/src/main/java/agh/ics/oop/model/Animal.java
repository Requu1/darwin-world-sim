package agh.ics.oop.model;

public class Animal implements WorldElement {
    private final static Vector2d DEFAULT_POS = new Vector2d(2, 2);

    private MapDirection facingDirection;
    private Vector2d posVector;

    public Animal() {
        this(DEFAULT_POS);
    }


    public Animal(Vector2d posVector) {
        this.posVector = posVector;
        this.facingDirection = MapDirection.NORTH;
    }

    public MapDirection getFacingDirection() {
        return this.facingDirection;
    }

    @Override
    public Vector2d getPosition() {
        return this.posVector;
    }

    @Override
    public String toString() {
        return switch (this.facingDirection) {
            case NORTH -> "N";
            case WEST -> "W";
            case EAST -> "E";
            case SOUTH -> "S";
        };
    }

    @Override
    public boolean isAt(Vector2d position) {
        return this.posVector.equals(position);
    }

    boolean move(MoveDirection direction, MoveValidator validator) {
        boolean animalHasMoved = false;
        switch (direction) {
            case MoveDirection.RIGHT -> {
                this.facingDirection = MapDirection.next(facingDirection);
                animalHasMoved = true;
            }
            case MoveDirection.LEFT -> {
                this.facingDirection = MapDirection.previous(facingDirection);
                animalHasMoved = true;
            }
            case MoveDirection.FORWARD -> {
                Vector2d newPosVector = this.posVector.add(facingDirection.toUnitVector());
                if (validator.canMoveTo(newPosVector)) {
                    this.posVector = newPosVector;
                    animalHasMoved = true;
                }
            }
            case MoveDirection.BACKWARD -> {
                Vector2d newPosVector = this.posVector.add(facingDirection.toUnitVector().opposite());
                if (validator.canMoveTo(newPosVector)) {
                    this.posVector = newPosVector;
                    animalHasMoved = true;
                }
            }
        }
        return animalHasMoved;
    }

}
