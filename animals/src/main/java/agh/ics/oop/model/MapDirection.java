package agh.ics.oop.model;


public enum MapDirection {
    NORTH(new Vector2d(0, 1)),
    EAST(new Vector2d(1, 0)),
    SOUTH(new Vector2d(0, -1)),
    WEST(new Vector2d(-1, 0));

    private final Vector2d unitVector;

    MapDirection(Vector2d unitVector) {
        this.unitVector = unitVector;
    }

    public static String toString(MapDirection direction) {
        return switch (direction) {
            case NORTH -> "Północ";
            case EAST -> "Wschód";
            case WEST -> "Zachód";
            case SOUTH -> "Południe";
        };
    }

    public static MapDirection next(MapDirection direction) {
        return switch (direction) {
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
    }

    public static MapDirection previous(MapDirection direction) {
        return switch (direction) {
            case NORTH -> WEST;
            case EAST -> NORTH;
            case SOUTH -> EAST;
            case WEST -> SOUTH;
        };
    }

    public Vector2d toUnitVector() {
        return this.unitVector;
    }
}
