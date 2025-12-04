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
        // guard
        if (dir == null || dir.isEmpty()) return "Cannot move.";
        Location here = ts.getCurrentLocation();
        if (here == null) return "Cannot move.";

        char directionChar = Character.toLowerCase(dir.charAt(0));
        Door doorToUse = null;
        for (Door d : here.getDoors()) {
            if (d.getDirection() == directionChar) {
                doorToUse = d;
                break;
            }
        }
        if (doorToUse == null) return "You can't go that way.";

        // Always check locked status for GWH, Monroe Hall, For Five Coffee
        final String MASTER_KEY_NAME = "Master Key";
        Location dest = doorToUse.getTo();
        boolean needsMasterKey = false;
        if (dest != null) {
            String destName = dest.getName().toLowerCase();
            if (destName.contains("george washington hall") || destName.contains("monroe hall") || destName.contains("for five coffee")) {
                needsMasterKey = true;
            }
        }
        if (needsMasterKey) {
            Item masterKey = ts.getItemFromBackpack(MASTER_KEY_NAME);
            if (masterKey == null) {
                StringBuilder msg = new StringBuilder();
                msg.append("The door leading ")
                   .append(Character.toUpperCase(directionChar))
                   .append(" is locked! You need the Master Key to enter ")
                   .append(dest != null ? dest.getName() : "this building")
                   .append(".");
                msg.append(System.lineSeparator());
                msg.append(here.describeDoors());
                return msg.toString();
            }
            // Do NOT unlock the door, just allow movement
        } else if (doorToUse.getIsLocked()) {
            String requiredKeyName = doorToUse.getKeyItemName();
            Item requiredKeyItem = (requiredKeyName == null) ? null : ts.getItemFromBackpack(requiredKeyName);
            if (requiredKeyItem != null) {
                doorToUse.setIsLocked(false);
            } else {
                StringBuilder msg = new StringBuilder();
                msg.append("The door leading ")
                   .append(Character.toUpperCase(directionChar))
                   .append(" is locked!");
                if (requiredKeyName != null && !requiredKeyName.isEmpty()) msg.append(" It requires the ").append(requiredKeyName).append(".");
                msg.append(System.lineSeparator());
                msg.append(here.describeDoors());
                return msg.toString();
            }
        }

        // perform the move
        Location next = doorToUse.getTo();
        if (next == null) return "You can't go that way.";
        ts.setCurrentLocation(next);
        next.setHaveVisited(true);
        ts.recordMove(dir);

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
        // Append distance summary after a movement
        sb.append(System.lineSeparator()).append(ts.getDistanceSummary());
        return sb.toString();
    }
}
