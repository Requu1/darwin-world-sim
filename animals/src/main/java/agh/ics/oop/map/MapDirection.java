package agh.ics.oop.map;


import agh.ics.oop.other.Vector2d;

public enum MapDirection {
    NORTH(new Vector2d(0, 1)),
    NORTH_WEST(new Vector2d(-1, 1)),
    NORTH_EAST(new Vector2d(1, 1)),
    EAST(new Vector2d(1, 0)),
    WEST(new Vector2d(-1, 0)),
    SOUTH(new Vector2d(0, -1)),
    SOUTH_WEST(new Vector2d(-1, -1)),
    SOUTH_EAST(new Vector2d(1, -1));

    private final static int DIRECTIONS_COUNT = 8;
    private final Vector2d unitVector;

    MapDirection(Vector2d unitVector) {
        this.unitVector = unitVector;
    }

    public static String toString(MapDirection direction) {
        return switch (direction) {
            case NORTH -> "Północ";
            case NORTH_EAST -> "Północny-Wschód";
            case NORTH_WEST -> "Północny-Zachód";
            case EAST -> "Wschód";
            case WEST -> "Zachód";
            case SOUTH -> "Południe";
            case SOUTH_EAST -> "Południowy-Wschód";
            case SOUTH_WEST -> "Południowy-Zachód";
        };
    }

    public static MapDirection next(MapDirection direction) {
        return switch (direction) {
            case NORTH -> NORTH_EAST;
            case NORTH_EAST -> EAST;
            case EAST -> SOUTH_EAST;
            case SOUTH_EAST -> SOUTH;
            case SOUTH -> SOUTH_WEST;
            case SOUTH_WEST -> WEST;
            case WEST -> NORTH_WEST;
            case NORTH_WEST -> NORTH;
        };
    }

    public static MapDirection previous(MapDirection direction) {
        return switch (direction) {
            case NORTH -> NORTH_WEST;
            case NORTH_WEST -> WEST;
            case WEST -> SOUTH_WEST;
            case SOUTH_WEST -> SOUTH;
            case SOUTH -> SOUTH_EAST;
            case SOUTH_EAST -> EAST;
            case EAST -> NORTH_EAST;
            case NORTH_EAST -> NORTH;
        };
    }

    public static MapDirection opposite(MapDirection direction) {
        MapDirection newDirection = direction;
        for (int i = 0; i < DIRECTIONS_COUNT / 2; i++) {
            newDirection = MapDirection.next(newDirection);
        }
        return newDirection;
    }

    public Vector2d toUnitVector() {
        return this.unitVector;
    }
}
