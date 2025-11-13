import java.util.*;

/**
 * @author Mohammad Eraj Danish
 * CPSC 240
 * Date: 10/6/2025
 *
 * Description:
 * Keeps track of the user’s current state in the tour,
 * including the campus being explored and the user’s
 * current location. Handles updates when the user moves
 * in a given direction.
 */



public class TourStatus {
    private static TourStatus tourInstance;
    private Campus campus;
    private Location currentLocation;
    private final ArrayList<Item> backpack = new ArrayList<>();

    private TourStatus() { }

    /**
     * Gets the singleton instance.
     * @return TourStatus
     */
    public static TourStatus getInstance() {
        if (tourInstance == null) tourInstance = new TourStatus();
        return tourInstance;
    }

    /**
     * Sets the campus.
     * @param campus campus
     */
    public void setCampus(Campus campus) { this.campus = campus; }

    /**
     * Gets the campus.
     * @return campus
     */
    public Campus getCampus() { return campus; }

    /**
     * Sets the current location.
     * @param loc location to set
     */
    public void setCurrentLocation(Location loc) { this.currentLocation = loc; }

    /**
     * Gets the current location.
     * @return current location
     */
    public Location getCurrentLocation() { return currentLocation; }

    /**
     * Attempts to move in a direction and updates current location on success.
     * @param dir "n","s","e","w" (case-insensitive; only first char used)
     * @return new location if moved; null if blocked
     */
    public Location updateTourLocation(String dir) {
        if (currentLocation == null) return null;
        Location next = currentLocation.leaveLocation(dir);
        if (next != null) {
            this.currentLocation = next;
            this.currentLocation.setHaveVisited(true);
        }
        return next;
    }

    /**
     * Adds an item to the backpack.
     * @param item item to add
     * @return the same item
     */
    public Item addToBackpack(Item item) {
        if (item != null) backpack.add(item);
        return item;
    }

    /**
     * Drops an item from the backpack by name (case-insensitive) into the current location.
     * @param name item name
     * @return the dropped item, or null if not owned
     */
    public Item dropItemFromBackpack(String name) {
        if (name == null) return null;
        for (int i = 0; i < backpack.size(); i++) {
            Item it = backpack.get(i);
            if (it.getName().equalsIgnoreCase(name)) {
                backpack.remove(i);
                if (currentLocation != null) currentLocation.addItem(it);
                return it;
            }
        }
        return null;
    }

    /**
     * Picks up an item by name from the current location into the backpack.
     * @param name item name
     * @return the picked up item, or null if not present
     */
    public Item pickupItemFromLocation(String name) {
        if (currentLocation == null || name == null) return null;
        Item found = currentLocation.getItemNamed(name);
        if (found != null) {
            currentLocation.removeItem(found);
            backpack.add(found);
        }
        return found;
    }

    /**
     * Lists backpack item names as a string.
     * @return "Backpack: (empty)" or "Backpack: a, b, c"
     */
    public String listBackpackItems() {
        if (backpack.isEmpty()) return "Backpack: (empty)";
        StringBuilder sb = new StringBuilder("Backpack: ");
        for (int i = 0; i < backpack.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(backpack.get(i).getName());
        }
        return sb.toString();
    }
}
