package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Abstract class for commands that require user confirmation.
 */
public abstract class ConfirmableCommand extends Command implements Confirmable {

    private boolean isConfirmed = false;

    public void confirm() {
        isConfirmed = true;
    }

    protected abstract CommandResult executeConfirmed(Model model) throws CommandException;

    @Override
    public final CommandResult execute(Model model) throws CommandException {
        if (!isConfirmed) {
            throw new CommandException("Command not confirmed yet.");
        }
        return executeConfirmed(model);
    }
}
