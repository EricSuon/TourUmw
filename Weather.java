/**
 * Weather interface for random events.
 */
public interface Weather {
    /**
     * Name of the weather event.
     */
    String getName();

    /**
     * Descriptive event message to show when the weather occurs.
     */
    String getEventMessage();
}
