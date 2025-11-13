/**
 * Command to list backpack contents.
 */
public class BackpackCommand implements UserInputCommand {
    /**
     * Lists backpack items or shows that it's empty.
     * @return formatted backpack string
     */
    @Override
    public String carryOut() {
        return TourStatus.getInstance().listBackpackItems();
    }
}
