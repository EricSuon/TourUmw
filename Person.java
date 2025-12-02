/**
 * Represents a person that can be met on campus.
 */
public class Person {
    private String name;
    private String location;
    private String dialogue;

    public Person(String name, String location, String dialogue) {
        this.name = name;
        this.location = location;
        this.dialogue = dialogue;
    }

    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getDialogue() { return dialogue; }

    @Override
    public String toString() { return name; }
}
