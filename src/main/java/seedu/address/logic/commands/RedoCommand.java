package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Reverts the address book to the previous state.
 */
public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";
    public static final String MESSAGE_SUCCESS = "Redo successful!\n(Redo: %s)";
    public static final String MESSAGE_FAILURE = "No actions to redo.";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (!model.canRedoAddressBook()) {
            throw new CommandException(MESSAGE_FAILURE);
        }
        String output = String.format(MESSAGE_SUCCESS, model.getRedoCommandDescription());
        CommandResult.DisplayType displayType = model.getRedoCommandDisplayType();
        model.redoAddressBook();
        return new CommandResult(output, displayType);
    }
}
