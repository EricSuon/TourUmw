/**
 * Command for "pickup <itemName>".
 */
public class PickupCommand implements UserInputCommand {
    /** Item name to pick up. */
    private final String itemName;

    /**
     * Constructs a PickupCommand.
     * @param itemName item name (may be null/blank; validated in carryOut)
     */
    public PickupCommand(String itemName) { this.itemName = itemName; }

    /**
     * Picks up the item into the backpack.
     * @return pickup message or an error
     */
    @Override
    public String carryOut() {
        if (itemName == null || itemName.isBlank())
            return "Please specify which item to pick up (e.g., \"pickup hat\").";
        Item got = TourStatus.getInstance().pickupItemFromLocation(itemName);
        if (got == null) return "There is no \"" + itemName + "\" here.";
        return got.getMessage() + " You have 5 turns to use this item before it disappears from your backpack.";
    }
}
