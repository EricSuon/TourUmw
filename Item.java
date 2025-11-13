/**
 * Represents an item that can be picked up by the user.
 * Each item has a name and a one-line pickup message.
 */
public class Item {
    /** Item name (used for display and case-insensitive lookups). */
    private String name;
    /** One-line message displayed when the item is picked up. */
    private String message;

    /** Zero-argument constructor. */
    public Item() { }

    /**
     * Constructs an Item.
     * @param name item name (non-empty)
     * @param msg pickup message (single line)
     */
    public Item(String name, String msg) {
        this.name = name;
        this.message = msg;
    }

    /**
     * Gets the item name.
     * @return name
     */
    public String getName() { return name; }

    /**
     * Sets the item name.
     * @param n new name
     */
    public void setName(String n) { this.name = n; }

    /**
     * Gets the pickup message.
     * @return message
     */
    public String getMessage() { return message; }

    /**
     * Sets the pickup message.
     * @param msg new message
     */
    public void setMessage(String msg) { this.message = msg; }

    /**
     * Returns the item name for display.
     * @return item name
     */
    @Override public String toString() { return name; }

    /**
     * Case-insensitive equality by item name.
     * @param o other object
     * @return true if both Items have the same name ignoring case
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item that = (Item) o;
        if (this.name == null || that.name == null) return false;
        return this.name.equalsIgnoreCase(that.name);
    }

    /**
     * Hash code that matches equals (case-insensitive by name).
     * @return hash
     */
    @Override
    public int hashCode() {
        return (name == null) ? 0 : name.toLowerCase().hashCode();
    }
}
