package seedu.address.logic;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.ConfirmableCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Handles confirmation flow for confirmable commands.
 */
public class ConfirmationManager {

    public static final String MESSAGE_NO_PENDING_ACTION = "No action pending confirmation.";
    public static final String MESSAGE_ACTION_CANCELLED = "Action cancelled.";
    public static final String MESSAGE_INVALID_CONFIRMATION_INPUT = "Please respond with Y or N.";
    public static final String INPUT_CONFIRM = "Y";
    public static final String INPUT_CANCEL = "N";

    /**
     * Handles the user's response to a confirmation prompt.
     *
     * @param input The user's response to the confirmation prompt
     * @param model The model containing the pending command and application state
     * @return A CommandResult indicating the outcome of the confirmation process
     * @throws Exception If there is an error executing the confirmed command
     */
    public CommandResult handleUserResponse(String input, Model model) throws Exception {
        if (model.getPendingCommand() == null) {
            return new CommandResult(MESSAGE_NO_PENDING_ACTION, CommandResult.DisplayType.RECENT);
        }

        String trimmed = input.trim().toLowerCase();
        switch (trimmed) {
        case "y":
            model.getPendingCommand().confirm();
            CommandResult result = model.getPendingCommand().execute(model);
            model.clearPendingCommand();
            return result;
        case "n":
            model.clearPendingCommand();
            return new CommandResult(MESSAGE_ACTION_CANCELLED, CommandResult.DisplayType.RECENT);
        default:
            return new CommandResult(MESSAGE_INVALID_CONFIRMATION_INPUT, CommandResult.DisplayType.RECENT);
        }
    }

    /**
     * Requests confirmation from the user for a potentially dangerous command.
     *
     * @param command The command that requires confirmation before execution
     * @param model The model to store the pending command and retrieve confirmation message
     * @return A CommandResult containing the confirmation message to display to the user
     * @throws CommandException If there is an error generating the confirmation message
     */
    public CommandResult requestConfirmation(ConfirmableCommand command, Model model) throws CommandException {
        model.updatePendingCommand(command);
        return new CommandResult(command.getConfirmationMessage(model), CommandResult.DisplayType.RECENT);
    }
}
