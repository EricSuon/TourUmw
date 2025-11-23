/**
 * Hurricane weather event.
 */
public class Hurricane implements Weather {
    @Override
    public String getName() { return "Hurricane"; }

    @Override
    public String getEventMessage() {
        return "A violent hurricane sweeps across the area, with howling winds and flying debris.";
    }
}
