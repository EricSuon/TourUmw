/**
 * A directed connection between two Locations by a cardinal direction.
 */
public class Door {
    /** Direction: 'n','s','e','w' (stored lowercase). */
    private final char direction;
    /** Origin location. */
    private final Location from;
    /** Destination location. */
    private final Location to;

    /**
     * Constructs a Door.
     * @param direction one of 'n','s','e','w' (case-insensitive)
     * @param from origin location
     * @param to destination location
     */
    public Door(char direction, Location from, Location to) {
        this.direction = Character.toLowerCase(direction);
        this.from = from;
        this.to = to;
    }

    /**
     * Gets the door direction.
     * @return direction char
     */
    public char getDirection() { return direction; }

    /**
     * Gets the origin location.
     * @return from location
     */
    public Location getFrom() { return from; }

    /**
     * Gets the destination location.
     * @return to location
     */
    public Location getTo() { return to; }
}
