/**
 * Acid rain weather event.
 */
public class AcidRain implements Weather {
    @Override
    public String getName() { return "Acid rain"; }

    @Override
    public String getEventMessage() {
        return "A deluge of corrosive acid rain pours down, burning exposed skin and corroding materials.";
    }
}
