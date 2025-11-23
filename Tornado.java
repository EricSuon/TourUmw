/**
 * Tornado weather event.
 */
public class Tornado implements Weather {
    @Override
    public String getName() { return "Tornado"; }

    @Override
    public String getEventMessage() {
        return "A swirling tornado touches down nearby, tearing up everything in its path.";
    }
}
