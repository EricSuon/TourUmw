/**
 * Command to use an item from the backpack.
 * Usage: use <item-name>
 * Items with transform targets will transform when used.
 */
public class UseCommand implements UserInputCommand {

    private final String itemName;

    public UseCommand(String input) {
        if (input == null) {
            this.itemName = null;
            return;
        }
        String[] parts = input.split("\\s+", 2);
        if (parts.length >= 2) {
            this.itemName = parts[1].trim();
        } else {
            this.itemName = null;
        }
    }

    @Override
    public String carryOut() {
        if (itemName == null || itemName.isBlank()) {
            return "Please specify which item to use (e.g., \"use coffee\").";
        }
        return TourStatus.getInstance().useItemFromBackpack(itemName);
    }
}
