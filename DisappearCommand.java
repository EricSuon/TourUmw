/**
 * This class makes a backpack item disappear from the Tour.
 *
 * @Author Madi Kracke
 */
public class DisappearCommand implements UserInputCommand {

    private String goneItem;
    private boolean isValidCommand = true;


    public DisappearCommand(String input) {
        String[] parts = input.split("\\s+", 2);

        if (parts.length >= 2) {
            goneItem = parts[1];
        } else {
            goneItem = null;
            isValidCommand = false;
        }
    }

    public String carryOut() {
        if(!isValidCommand) {
            return "What do you want to disappear? Specify the item you want.";
        }

        TourStatus tour = TourStatus.getInstance();

        Item disappear = tour.getItemFromBackpack(goneItem);


        if (disappear == null) {
            return goneItem + " is not here, you cannot make it disappear.";
        }

        String goneMessage = disappear.getActionTwo();
        if (goneMessage == null) {
            tour.removeFromBackpack(disappear);
            return goneItem + "disappears without a trace!";
        }

        String[] parts = goneMessage.split(":", 2);
        tour.removeFromBackpack(disappear);

        if (parts.length >= 2) {
            return parts [1].trim();
        } else {
            return goneItem + " disappears: " + goneMessage;
        }
    }
}
