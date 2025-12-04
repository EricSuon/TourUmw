import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Mohammad Eraj Danish
 * CPSC 240
 * Date: 10/6/2025
 *
 * Description:
 * Represents a physical place on campus that we can visit.
 * Stores the location’s name, description, list of door objects,
 * and whether or not we have visited it.
 */


public class Location {
    private String name;
    private String description;
    private boolean haveVisited;
    // true when this location is an indoor building (protects from weather)
    private boolean indoors;

    private final ArrayList<Door> doors = new ArrayList<>();
    private final ArrayList<Item> items = new ArrayList<>();
    private final ArrayList<Person> people = new ArrayList<>();

    public Location() { }

    /**
     * Constructs a Location.
     * @param name location name
     * @param desc description text
     */
    public Location(String name, String desc) {
        this(name, desc, false);
    }

    /**
     * Constructs a Location with indoor flag.
     * @param name location name
     * @param desc description text
     * @param indoors true if this location is indoors/a building
     */
    public Location(String name, String desc, boolean indoors) {
        this.name = name;
        this.description = (desc == null) ? "" : desc;
        this.indoors = indoors;
    }

    /**
     * Gets the location name.
     * @return name
     */
    public String getName() { return name; }

    /**
     * Gets the description text.
     * @return description
     */
    public String getDescription() { return description; }

    /**
     * Returns whether this location has been visited.
     * @return true if visited
     */
    public boolean getHaveVisited() { return haveVisited; }

    /**
     * Sets the visited flag.
     * @param v new visited value
     */
    public void setHaveVisited(boolean v) { this.haveVisited = v; }

    /**
     * Adds a door that leaves from this location.
     * @param door door to add
     */
    public void addDoor(Door door) {
        if (door != null) doors.add(door);
    }

    /**
     * Returns a read-only collection of doors.
     * @return doors
     */
    public Collection<Door> getDoors() {
        return java.util.Collections.unmodifiableList(doors);
    }

    /**
     * Formats the list of doors as: "Doors: N -> Name, E -> Name, ..."
     * @return doors description line
     */
    public String describeDoors() {
        if (doors.isEmpty()) return "Doors: (none)";
        StringBuilder sb = new StringBuilder("Doors: ");
        for (int i = 0; i < doors.size(); i++) {
            Door d = doors.get(i);
            if (i > 0) sb.append(", ");
            sb.append(Character.toUpperCase(d.getDirection()))
                    .append(" -> ").append(d.getTo().getName());
        }
        return sb.toString();
    }

    /**
     * Builds a multi-line description (name, description, items, doors).
     * The dir parameter is accepted for UML compatibility and is not required
     * for formatting the text.
     * @param dir direction entered (ignored)
     * @return full description
     */
    public String describeLocation(String dir) {
        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append(System.lineSeparator());
        if (description != null && !description.isBlank()) {
            sb.append(description.trim()).append(System.lineSeparator());
        }
        sb.append(getItemsInLocation()).append(System.lineSeparator());
        sb.append(describePeople()).append(System.lineSeparator());
        sb.append(describeDoors());
        return sb.toString();
    }

    /**
     * Attempts to leave this location in a given direction.
     * @param dir a string like "n","s","e","w" (first character is used)
     * @return the destination Location, or null if no door that way
     */
    public Location leaveLocation(String dir) {
        if (dir == null || dir.isEmpty()) return null;
        char c = Character.toLowerCase(dir.charAt(0));
        for (Door d : doors) {
            if (d.getDirection() == c) return d.getTo();
        }
        return null;
    }

    /**
     * Adds an item to this location.
     * @param item item to add
     */
    public void addItem(Item item) {
        if (item != null) items.add(item);
    }

    /**
     * Removes the first equal item instance from this location.
     * @param item item to remove
     * @return the removed item, or null if not present
     */
    public Item removeItem(Item item) {
        if (item == null) return null;
        return items.remove(item) ? item : null;
        // note: equality uses Item.equals (case-insensitive by name)
    }

    /**
     * Finds an item by case-insensitive name (without removing).
     * @param name item name
     * @return the matching item, or null if none
     */
    public Item getItemNamed(String name) {
        if (name == null) return null;
        for (Item it : items) {
            if (name.equalsIgnoreCase(it.getName())) return it;
        }
        return null;
    }

    /**
     * Returns a formatted items line for this location.
     * @return "Items: (none)" or "Items: a, b, c"
     */
    public String getItemsInLocation() {
        if (items.isEmpty()) return "Items: (none)";
        StringBuilder sb = new StringBuilder("Items: ");
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(items.get(i).getName());
        }
        return sb.toString();
    }
    /**
     * Adds a person to this location.
     *
     * @param p the person to add
     */
    public void addPerson(Person p) {
        if (p != null) {
            people.add(p);
        }
    }

    /**
     * Returns a read-only collection of people at this location.
     *
     * @return collection of people in this location
     */
    public java.util.Collection<Person> getPeople() {
        return java.util.Collections.unmodifiableCollection(people);
    }

    /**
     * Finds a person in this location by name (case-insensitive).
     *
     * @param name the name to search for
     * @return matching Person, or null if not found
     */
    public Person getPerson(String name) {
        if (name == null) {
            return null;
        }
        for (Person p : people) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Returns a description of all people in this location.
     *
     * @return text listing people here, or saying none
     */
    public String describePeople() {
        if (people.isEmpty()) {
            return "People here: none.";
        }
        StringBuilder sb = new StringBuilder("People here: ");
        for (int i = 0; i < people.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            Person p = people.get(i);
            sb.append(p.getName());
        }
        return sb.toString();
    }


    /**
     * Returns true if this location is considered indoors (a building).
     * @return indoors flag
     */
    public boolean isIndoors() { return indoors; }

    /**
     * Sets whether this location is indoors.
     * @param v true if indoors
     */
    public void setIndoors(boolean v) { this.indoors = v; }
}
