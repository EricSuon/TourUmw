import java.util.Scanner;
import java.io.File;
import java.util.Locale;

/**
 * File format expected:
 * Sections are delimited by a line containing exactly "*****".
 * Within the Locations section, individual locations are delimited by a line containing exactly "+++".
 * Within the Doors section, individual doors are delimited by a line containing exactly "+++".
 *
 * @author Mohammad Eraj Danish
 * CPSC 240
 * Date: 10/30/2025
 * version 2
 *
 * Description:
 * The main driver class for the virtual tour program.
 * Reads campus data from a file, sets up all locations
 * and doors, and allows the user to navigate through
 * the campus interactively using text input.
 * This version allows for items to be found around campus
 * and picked up by the user.
 * These items can now be stored withing the backpack and
 * can be dropped in different parts of the campus
 */

public class TourUMW {

    /**
     * Main entry.
     * @param args optional path to data file
     * @throws Exception on unexpected error
     */
    public static void main(String[] args) throws Exception {
        Scanner s = new Scanner(System.in);

        Campus campus = setUpCampus(s);
        TourStatus ts = TourStatus.getInstance();
        ts.setCampus(campus);
        ts.setCurrentLocation(campus.getStartingLocation());
        ts.getCurrentLocation().setHaveVisited(true);

        System.out.println(ts.getCurrentLocation().describeLocation(""));

        while (true) {
            System.out.print("\n> ");
            String input = s.nextLine();
            if (input == null) continue;
            if (input.equalsIgnoreCase("q") || input.equalsIgnoreCase("quit")) {
                System.out.println("Goodbye!");
                break;
            }
            UserInputCommand cmd = parseInput(input);
            System.out.println(cmd.carryOut());
        }
    }

    /**
     * @param input raw user input
     * @return a UserInputCommand
     */
    public static UserInputCommand parseInput(String input) {
        if (input == null) return new InvalidCommand("");
        String lower = input.toLowerCase(Locale.ROOT).trim();

        // Movement (single letter n/s/e/w)
        if (lower.length() == 1 && "nsew".contains(lower)) {
            return new MovementCommand(lower);
        }

        // Backpack
        if (lower.equals("b") || lower.equals("backpack")) {
            return new BackpackCommand();
        }

        // Pickup
        if (lower.startsWith("pickup") || lower.startsWith("p ")) {
            String arg = extractArg(lower, "pickup");
            if (arg == null && lower.startsWith("p ")) arg = lower.substring(2).trim();
            return new PickupCommand(arg);
        }

        // Drop
        if (lower.startsWith("drop") || lower.startsWith("d ")) {
            String arg = extractArg(lower, "drop");
            if (arg == null && lower.startsWith("d ")) arg = lower.substring(2).trim();
            return new DropCommand(arg);
        }

        return new InvalidCommand(input);
    }

    /**
     * Sets up the campus by asking for a file path (default sample_umw.txt).
     * @param s scanner for input
     * @return loaded campus
     * @throws Exception if load fails
     */
    public static Campus setUpCampus(Scanner s) throws Exception {
        System.out.println("Welcome to the UMW Virtual Tour!");
        System.out.println("(Commands: n/s/e/w, pickup <item>, drop <item>, backpack, q to quit.)");
        System.out.print("Enter data file path (or press Enter for umw_campus_scavenger.txt): ");
        String path = s.nextLine().trim();
        File f = path.isEmpty() ? new File("umw_campus_scavenger.txt") : new File(path);
        return Campus.fromFile(f);
    }

    /**
     * Extracts an argument after a verb (e.g., "pickup hat" -> "hat").
     * @param lower lowercase, trimmed input
     * @param verb verb token
     * @return arg or null if missing
     */
    private static String extractArg(String lower, String verb) {
        if (lower.equals(verb)) return null;
        if (lower.startsWith(verb + " ")) return lower.substring(verb.length() + 1).trim();
        return null;
    }
}
