package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;

/**
 * Clears all persons and lessons from the address book.
 */
public class ClearCommand extends ConfirmableCommand {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";
    public static final String MESSAGE_CONFIRM_CLEAR = "(Y/N) Are you sure you want to delete ALL people and lessons?";

    /**
     * Default constructor
     */
    public ClearCommand() {
        this(false);
    }

    /**
     * Alternative constructor for forced command
     */
    public ClearCommand(boolean isForced) {
        super(isForced);
    }

    @Override
    public CommandResult executeConfirmed(Model model) {
        requireNonNull(model);
        model.setAddressBook(new AddressBook());
        // Update AddressBook state pointer
        model.commitAddressBook(MESSAGE_SUCCESS);
        return new CommandResult(MESSAGE_SUCCESS, CommandResult.DisplayType.DEFAULT);
    }

    @Override
    public String getConfirmationMessage(Model model) throws CommandException {
        return MESSAGE_CONFIRM_CLEAR;
    }
}
