/**
 * Command to meet a person at a location.
 * Usage: meet <person-name>
 */
public class MeetPersonCommand implements UserInputCommand {

    private final String personName;

    public MeetPersonCommand(String input) {
        if (input == null) {
            this.personName = null;
            return;
        }
        String[] parts = input.split("\\s+", 2);
        if (parts.length >= 2) {
            this.personName = parts[1].trim();
        } else {
            this.personName = null;
        }
    }

    @Override
    public String carryOut() {
        if (personName == null || personName.isBlank()) {
            return "Please specify which person you want to meet.";
        }
        
        TourStatus ts = TourStatus.getInstance();
        Campus campus = ts.getCampus();
        Location here = ts.getCurrentLocation();
        
        if (here == null) return "You're nowhere.";
        if (campus == null) return "Campus not loaded.";

        Person person = campus.getPersonAtLocation(personName, here.getName());
        if (person == null) {
            return "There's no one named \"" + personName + "\" here.";
        }

        return person.getName() + " says: " + person.getDialogue();
    }
}
