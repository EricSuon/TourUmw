import java.io.*;
import java.util.*;

/**
 * @author Mohammad Eraj Danish
 * CPSC 240
 * Date: 10/6/2025
 *
 * Description:
 * Represents the entire UMW campus, containing multiple locations.
 * Tracks the campus name, list of all Location objects, and the
 * starting location where the virtual tour begins.
 */

public class Campus {
    /** Case-insensitive key -> Location. */
    private final Map<String, Location> locations = new LinkedHashMap<>();
    /** Case-insensitive key -> Item definition (may exist but not be placed in any location). */
    private final Map<String, Item> itemDefinitions = new LinkedHashMap<>();
    /** Display name (optional). */
    private final String name;
    /** Starting location. */
    private Location startingLocation;

    public Campus(String name) {
        this.name = (name == null || name.isBlank()) ? "UMW" : name.trim();
    }

    public String getName() { return name; }
    public Location getStartingLocation() { return startingLocation; }
    public void setStartingLocation(Location loc) { this.startingLocation = loc; }

    /** Normalize a location key: trim + lowercase. */
    private static String keyFor(String s) { return s == null ? "" : s.trim().toLowerCase(); }

    /** Adds a location (case-insensitive key). */
    public void addLocation(Location loc) {
        locations.put(keyFor(loc.getName()), loc);
    }

    /** Returns the locations map. */
    public Map<String, Location> getLocations() { return locations; }

    /** Looks up a location by (trimmed, case-insensitive) name. */
    public Location getLocation(String name) {
        Location loc = locations.get(keyFor(name));
        if (loc != null) return loc;
        // If not found, try stripping any bracketed qualifiers such as "[building]"
        if (name != null) {
            String stripped = name.replaceAll("(?i)\\s*\\[.*?\\]\\s*", "").trim();
            if (!stripped.isEmpty() && !stripped.equals(name.trim())) {
                loc = locations.get(keyFor(stripped));
            }
        }
        return loc;
    }

    /** Adds a door; throws with a helpful message if endpoints are missing. */
    public void addDoor(char dir, String fromName, String toName) {
        Location from = getLocation(fromName);
        Location to = getLocation(toName);
        if (from == null || to == null) {
            StringBuilder known = new StringBuilder();
            boolean first = true;
            for (Location l : locations.values()) {
                if (!first) known.append(", ");
                known.append(l.getName());
                first = false;
            }
            throw new IllegalArgumentException(
                    "Invalid door endpoints. from=\"" + fromName + "\" -> to=\"" + toName + "\" dir=" + dir +
                            ". Missing: " + (from == null ? "[from]" : "") + (from == null && to == null ? " & " : "") +
                            (to == null ? "[to]" : "") +
                            ". Known locations: " + known
            );
        }
        from.addDoor(new Door(dir, from, to));
    }


    /**
     * Loads a Campus from a file with sections separated by "*****" and blocks by "+++".
     * Accepts:
     * - Door triplets as either (from, to, dir) OR (from, dir, to)
     * - Stray label lines "Locations:", "Doors:", "Items:" inside sections/blocks
     * - Case-insensitive, trimmed location names
     */
    public static Campus fromFile(File f) throws IOException {
        List<String> lines = readAll(f);
        // Split into sections by "*****"
        List<List<String>> sections = splitOn(lines, "*****");
        if (sections.size() < 3)
            throw new IllegalArgumentException("Expected at least 3 sections: Title, Locations, Doors; Items optional.");

        // Title
        String title = firstNonBlank(sections.get(0));
        Campus campus = new Campus(title);

        // LOCATIONS
        List<String> locSection = new ArrayList<>(sections.get(1));
        removeLabelLines(locSection, "Locations:");
        List<List<String>> locBlocks = splitOn(locSection, "+++");
        Location firstLoc = null;
        for (List<String> rawBlock : locBlocks) {
            List<String> nb = nonBlank(rawBlock);
            if (nb.isEmpty()) continue;
            // name = first line, description = remainder joined with newlines
            String name = nb.get(0).trim();
            String desc = join(nb.subList(1, nb.size()), System.lineSeparator()).trim();

            boolean indoors = false;
            // allow a marker token [building] in either the name or the description to mark indoor locations
            if (name.toLowerCase().contains("[building]")) {
                indoors = true;
                name = name.replaceAll("(?i)\\[building\\]", "").trim();
            }
            if (desc.toLowerCase().contains("[building]")) {
                indoors = true;
                desc = desc.replaceAll("(?i)\\[building\\]", "").trim();
            }
            Location loc = new Location(name, desc, indoors);
            campus.addLocation(loc);
            if (firstLoc == null) firstLoc = loc;
        }
        if (firstLoc == null) throw new IllegalArgumentException("No locations found.");
        campus.setStartingLocation(firstLoc);

        // DOORS
        List<String> doorSection = new ArrayList<>(sections.get(2));
        removeLabelLines(doorSection, "Doors:");
        List<List<String>> doorBlocks = splitOn(doorSection, "+++");
        for (List<String> rawBlock : doorBlocks) {
            List<String> nb = nonBlank(rawBlock);
            if (nb.size() < 3) continue;

            String a = nb.get(0).trim();
            String b = nb.get(1).trim();
            String c = nb.get(2).trim();

            String from, to; char dir;
            // Accept either (from, dir, to) OR (from, to, dir)
            if (isDirToken(b)) {
                from = a; dir = toDir(b); to = c;
            } else if (isDirToken(c)) {
                from = a; to = b; dir = toDir(c);
            } else {
                // ignore malformed block instead of crashing
                continue;
            }
            campus.addDoor(dir, from, to);
        }

        // ITEMS (optional)
        if (sections.size() >= 4) {
            List<String> itemSection = new ArrayList<>(sections.get(3));
            removeLabelLines(itemSection, "Items:");
            List<List<String>> itemBlocks = splitOn(itemSection, "+++");
            for (List<String> rawBlock : itemBlocks) {
                List<String> nb = nonBlank(rawBlock);
                if (nb.size() < 3) continue;
                String itemNameRaw = nb.get(0).trim();
                String locName  = nb.get(1).trim();
                String message  = nb.get(2).trim();

                // Detect optional transform target in parentheses within the item name: "Cookie (Crumbs)"
                String itemName = itemNameRaw;
                String transformTarget = null;
                java.util.regex.Matcher m = java.util.regex.Pattern.compile("^(.*?)\\s*\\(([^)]+)\\)\\s*$").matcher(itemNameRaw);
                if (m.find()) {
                    itemName = m.group(1).trim();
                    transformTarget = m.group(2).trim();
                }

                // Create definition and register it
                Item def = new Item(itemName, message);
                if (transformTarget != null && !transformTarget.isEmpty()) def.setTransformTarget(transformTarget);
                campus.registerItemDefinition(def);

                // If location is "none", do not place the item anywhere; otherwise place a copy at the location
                if (!locName.equalsIgnoreCase("none")) {
                    Location where = campus.getLocation(locName);
                    if (where == null) {
                        throw new IllegalArgumentException("Item location not found: \"" + locName + "\" for item \"" + itemName + "\"");
                    }
                    Item placed = new Item(def.getName(), def.getMessage());
                    placed.setActionTwo(def.getActionTwo());
                    placed.setTransformTarget(def.getTransformTarget());
                    where.addItem(placed);
                }
            }
        }

        return campus;
    }

    public Item getItemFromList(String itemName) {
        if (startingLocation != null) {
            return startingLocation.getItemNamed(itemName);
        }
        return null;
    }

    /** Registers an item definition for later lookup. */
    public void registerItemDefinition(Item it) {
        if (it == null || it.getName() == null) return;
        itemDefinitions.put(keyFor(it.getName()), it);
    }

    /** Looks up a registered item definition by name (case-insensitive). */
    public Item getItemDefinition(String name) {
        if (name == null) return null;
        return itemDefinitions.get(keyFor(name));
    }


    private static boolean isDirToken(String s) {
        if (s == null || s.isBlank()) return false;
        String t = s.trim().toLowerCase();
        return t.length() == 1 && "nsew".indexOf(t.charAt(0)) >= 0;
    }
    private static char toDir(String s) { return s.trim().toLowerCase().charAt(0); }

    private static void removeLabelLines(List<String> lines, String label) {
        for (ListIterator<String> it = lines.listIterator(); it.hasNext(); ) {
            String ln = it.next();
            if (ln.trim().equalsIgnoreCase(label)) it.set(""); // blank it; will be removed by nonBlank later
        }
    }

    private static List<String> readAll(File f) throws IOException {
        ArrayList<String> out = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) out.add(line);
        }
        return out;
    }

    private static List<List<String>> splitOn(List<String> lines, String delim) {
        ArrayList<List<String>> res = new ArrayList<>();
        ArrayList<String> cur = new ArrayList<>();
        for (String s : lines) {
            if (s.trim().equals(delim)) {
                res.add(cur);
                cur = new ArrayList<>();
            } else {
                cur.add(s);
            }
        }
        res.add(cur);
        return res;
    }

    private static String firstNonBlank(List<String> lines) {
        for (String s : lines) if (!s.trim().isEmpty()) return s;
        return null;
    }

    private static List<String> nonBlank(List<String> lines) {
        ArrayList<String> out = new ArrayList<>();
        for (String s : lines) if (!s.trim().isEmpty()) out.add(s);
        return out;
    }

    private static String join(List<String> lines, String sep) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            if (i > 0) sb.append(sep);
            sb.append(lines.get(i));
        }
        return sb.toString();
    }
}
