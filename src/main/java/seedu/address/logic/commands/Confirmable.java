package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Represents a Confirmable that is able to requires user input to execute a command.
 */
public interface Confirmable {
    String getConfirmationMessage(Model model) throws CommandException;
}
