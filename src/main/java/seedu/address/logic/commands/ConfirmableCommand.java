package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Abstract class for commands that require user confirmation.
 */
public abstract class ConfirmableCommand extends Command implements Confirmable {

    private boolean isConfirmed = false;
    private final boolean isForced;

    protected ConfirmableCommand() {
        this(false);
    }

    protected ConfirmableCommand(boolean isForced) {
        this.isForced = isForced;
    }

    public void confirm() {
        isConfirmed = true;
    }

    /**
     * Validates the command using the given model, ensuring that all references
     * (e.g. indexes, names) are valid before requesting confirmation.
     *
     * @throws CommandException if validation fails.
     */
    public void validate(Model model) throws CommandException {
    }

    protected abstract CommandResult executeConfirmed(Model model) throws CommandException;

    @Override
    public final CommandResult execute(Model model) throws CommandException {
        if (isForced) {
            // Skip confirmation
            return executeConfirmed(model);
        }
        if (!isConfirmed) {
            return new CommandResult(getConfirmationMessage(model), CommandResult.DisplayType.CONFIRMATION);
        }
        return executeConfirmed(model);
    }

    @Override
    public boolean isForced() {
        return this.isForced;
    }
}
