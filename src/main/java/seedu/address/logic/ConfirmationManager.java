package seedu.address.logic;

import seedu.address.logic.commands.ConfirmableCommand;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.Model;

/**
 * Handles confirmation flow for confirmable commands.
 */
public class ConfirmationManager {

    public static final String MESSAGE_NO_PENDING_ACTION = "No action pending confirmation.";
    public static final String MESSAGE_ACTION_CANCELLED = "Action cancelled.";
    public static final String MESSAGE_INVALID_CONFIRMATION_INPUT = "Please respond with Y or N.";

    private ConfirmableCommand pendingCommand;

    public CommandResult handleUserResponse(String input, Model model) throws Exception {
        if (pendingCommand == null) {
            return new CommandResult(MESSAGE_NO_PENDING_ACTION, CommandResult.DisplayType.RECENT);
        }

        String trimmed = input.trim().toLowerCase();
        switch (trimmed) {
        case "y":
            pendingCommand.confirm();
            CommandResult result = pendingCommand.execute(model);
            clearPendingCommand();
            return result;
        case "n":
            clearPendingCommand();
            return new CommandResult(MESSAGE_ACTION_CANCELLED, CommandResult.DisplayType.RECENT);
        default:
            return new CommandResult(MESSAGE_INVALID_CONFIRMATION_INPUT, CommandResult.DisplayType.RECENT);
        }
    }

    public CommandResult requestConfirmation(ConfirmableCommand command, Model model) {
        this.pendingCommand = command;
        return new CommandResult(command.getConfirmationMessage(model), CommandResult.DisplayType.RECENT);
    }

    public boolean hasPendingCommand() {
        return pendingCommand != null;
    }

    public void clearPendingCommand() {
        this.pendingCommand = null;
    }
}
