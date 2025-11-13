/**
 * Movement command handling n/s/e/w.
 */
public class MovementCommand implements UserInputCommand {
    private final String dir;

    /**
     * Constructs a MovementCommand.
     * @param dir direction string (first char used)
     */
    public MovementCommand(String dir) { this.dir = dir; }

    /**
     * Attempts to move and prints the new location or an error.
     * @return description or error message
     */
    @Override
    public String carryOut() {
        TourStatus ts = TourStatus.getInstance();
        Location next = ts.updateTourLocation(dir);
        if (next == null) return "You can't go that way.";
        return next.describeLocation(dir);
    }
}
