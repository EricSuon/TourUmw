/**
 * Command for "drop <itemName>".
 */
public class DropCommand implements UserInputCommand {
    /** Item name to drop. */
    private final String itemName;

    /**
     * Constructs a DropCommand.
     * @param itemName item name (may be null/blank; validated in carryOut)
     */
    public DropCommand(String itemName) { this.itemName = itemName; }

    /**
     * Drops the item in the current location.
     * @return confirmation or an error if not owned
     */
    @Override
    public String carryOut() {
        if (itemName == null || itemName.isBlank())
            return "Please specify which item to drop (e.g., \"drop hat\").";
        Item dropped = TourStatus.getInstance().dropItemFromBackpack(itemName);
        if (dropped == null) return "You don't have \"" + itemName + "\" in your backpack.";
        return "The " + dropped.getName() + " has been dropped in " +
                TourStatus.getInstance().getCurrentLocation().getName() + ".";
    }
}
