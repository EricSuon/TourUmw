import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

/**
 * Command to load a saved tour state from a file.
 * Usage: "load" (uses default filename "tour_save.txt").
 */
public class LoadCommand implements UserInputCommand {

    private final String filename;
    private int distance;

    public LoadCommand() {
        this("tour_save.txt");
    }

    /**
     * Constructs a LoadCommand with a given filename.
     * @param filename name of the save file
     */
    public LoadCommand(String filename) {
        this.filename = filename;
    }

    /**
     * Loads tour state from the save file and updates TourStatus.
     * @return message indicating success or failure
     */
    @Override
    public String carryOut() {
        TourStatus status = TourStatus.getInstance();
        Campus campus = status.getCampus();

        if (campus == null) {
            return "Cannot load: campus is not initialized.";
        }

        File inFile = new File(filename);
        if (!inFile.exists()) {
            return "Save file \"" + filename + "\" not found.";
        }

        String locationName = null;
        int distance = 0;
        List<String> backpackNames = new ArrayList<>();

        try (Scanner s = new Scanner(inFile)) {
            while (s.hasNextLine()) {
                String line = s.nextLine().trim();
                if (line.isEmpty()) continue;

                if (line.startsWith("UMW_SAVE_V1")) {
                    // header, ignore
                } else if (line.startsWith("DATA_FILE:")) {
                    // not used directly here
                } else if (line.startsWith("CURRENT_LOCATION:")) {
                    locationName = line.substring("CURRENT_LOCATION:".length()).trim();
                } else if (line.startsWith("DISTANCE:")) {
                    String num = line.substring("DISTANCE:".length()).trim();
                    try {
                        distance = Integer.parseInt(num);
                    } catch (NumberFormatException e) {
                        distance = 0;
                    }
                } else if (line.equals("BACKPACK:")) {
                    // read lines until END_BACKPACK
                    while (s.hasNextLine()) {
                        String itemLine = s.nextLine().trim();
                        if (itemLine.equals("END_BACKPACK")) break;
                        if (!itemLine.isEmpty()) {
                            backpackNames.add(itemLine);
                        }
                    }
                }
            }
        } catch (Exception e) {
            return "Error loading game: " + e.getMessage();
        }

        if (locationName == null) {
            return "Save file is missing CURRENT_LOCATION.";
        }

        Location loc = campus.getLocation(locationName);
        if (loc == null) {
            return "Location \"" + locationName + "\" in save file not found on campus.";
        }

        // Apply loaded state.
        status.setCurrentLocation(loc);

        // Distance – adjust setter name if yours is different.
        status.setDistance(distance);

        // Restore backpack contents:
        status.clearBackpack();
        for (String itemName : backpackNames) {
            Item it = null;
            // Search all locations for the item
            for (String locName : ((Collection<String>) campus.getLocations()).toArray(new String[0])) {
                Location location = campus.getLocation(locName);
                it = location.getItemNamed(itemName);
                if (it != null) {
                    location.removeItem(it);
                    break;
                }
            }
            if (it == null) {
                // If not found, create a basic item so the user still has it.
                it = new Item(itemName, "An item from your previous tour.");
            }
            status.addToBackpack(it);
        }

        return "Game loaded. You are now at " + loc.getName() + ".";
    }

    public int getDistance() {
        return getDistance();
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
