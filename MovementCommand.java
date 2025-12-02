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
        StringBuilder sb = new StringBuilder(next.describeLocation(dir));
        java.util.List<Person> ppl = ts.getCampus().getPeopleAtLocation(next.getName());
        if (!ppl.isEmpty()) {
            sb.append(System.lineSeparator());
            sb.append("People here: ");
            for (int i = 0; i < ppl.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(ppl.get(i).getName());
            }
        }
        return sb.toString();
    }
}
