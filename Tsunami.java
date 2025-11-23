/**
 * Tsunami weather event.
 */
public class Tsunami implements Weather {
    @Override
    public String getName() { return "Tsunami"; }

    @Override
    public String getEventMessage() {
        return "A massive tsunami surges inland, towering walls of water rushing over the land.";
    }
}
