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
    /** Locking Doors */
    private boolean isLocked;
    private String keyItemName;

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

    public Door(char direction, Location from, Location to, String keyItemName) {
        this.direction = Character.toLowerCase(direction);
        this.from = from;
        this.to = to;
        this.isLocked = true;
        this.keyItemName = keyItemName;
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

    /**
     * Gets status of locked door
     * @return true if door is currently locked
     */
    public boolean getIsLocked() { return isLocked; }
    /**
     * Sets locked door statys
     * @param locked true to lock door, false to unlock.
     */
    public void setIsLocked(boolean locked) { this.isLocked = locked; }
    /**
     * gets name of the key item required to unlock this door
     * @return key name or null
     */
    public String getKeyItemName () { return keyItemName; }

    /**
     * Check if item is correct key to unlock
     * @param itemName
     * @return true if door is unlocked and correct key used
     */
    public boolean canUnlock(String itemName) {
        if (!isLocked || keyItemName == null || itemName == null) {
            return false;
        }
        return keyItemName.equalsIgnoreCase(itemName);
    }
}
