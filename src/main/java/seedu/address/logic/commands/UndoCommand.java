package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Reverts the address book to the previous state.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Undo successful!";
    public static final String MESSAGE_FAILURE = "No actions to undo.";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (!model.canUndoAddressBook()) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        model.undoAddressBook();
        return new CommandResult(MESSAGE_SUCCESS, CommandResult.DisplayType.DEFAULT);
    }
}
