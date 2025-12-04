import java.io.File;
import java.io.PrintWriter;
import java.util.List;

/**
 * Command to save the current tour state to a file.
 * Usage: "save" (uses default filename "tour_save.txt").
 */
public class SaveCommand implements UserInputCommand {
    private final String filename;

    public SaveCommand() {
        this("tour_save.txt");
    }

    public SaveCommand(String filename) {
        this.filename = filename;
    }

    @Override
    public String carryOut() {
        TourStatus status = TourStatus.getInstance();
        Campus campus = status.getCampus();
        if (campus == null) {
            return "Cannot save: campus is not initialized.";
        }
        File outFile = new File(filename);
        try (PrintWriter pw = new PrintWriter(outFile)) {
            pw.println("UMW_SAVE_V1");
            pw.println("DATA_FILE: umw_campus_scavenger.txt");
            pw.println("CURRENT_LOCATION: " + status.getCurrentLocation().getName());
            pw.println("DISTANCE: " + status.getInstance());
            pw.println("BACKPACK:");
                List<Item> backpack = status.getBackpack();
            for (Item item : backpack) {
                pw.println(item.getName());
            }
            pw.println("END_BACKPACK");
        } catch (Exception e) {
            return "Error saving game: " + e.getMessage();
        }
        return "Game saved to " + filename + ".";
    }
}
