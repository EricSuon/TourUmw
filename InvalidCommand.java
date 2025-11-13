/**
 * Catch-all for invalid user input.
 */
public class InvalidCommand implements UserInputCommand {
    /** The unrecognized input. */
    private final String invalidCommand;

    /**
     * Constructs an InvalidCommand.
     * @param invalidCommand raw input
     */
    public InvalidCommand(String invalidCommand) {
        this.invalidCommand = invalidCommand;
    }

    /**
     * Returns an error and hints at valid commands.
     * @return error/help text
     */
    @Override
    public String carryOut() {
        return "Invalid command: \"" + invalidCommand +
                "\". Valid: n,s,e,w, pickup <item>, drop <item>, backpack, or q to quit.";
    }
}
