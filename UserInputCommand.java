/**
 * Command interface used by all user commands.
 */
public interface UserInputCommand {
    /**
     * Executes the command and returns a message to print.
     * @return output string for the player
     */
    String carryOut();
}
