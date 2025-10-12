package seedu.address.logic;

import seedu.address.logic.commands.ConfirmableCommand;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.Model;

/**
 * Handles confirmation flow for confirmable commands.
 */
public class ConfirmationManager {

    private ConfirmableCommand pendingCommand;

    public CommandResult handleUserResponse(String input, Model model) throws Exception {
        if (pendingCommand == null) {
            return new CommandResult("No action pending confirmation.", CommandResult.DisplayType.RECENT);
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
            return new CommandResult("Action cancelled.", CommandResult.DisplayType.RECENT);
        default:
            return new CommandResult("Please respond with Y or N.", CommandResult.DisplayType.RECENT);
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
