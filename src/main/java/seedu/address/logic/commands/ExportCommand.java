package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;

import seedu.address.commons.util.JsonUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.storage.JsonSerializableAddressBook;

/**
 * Exports the current address book data to a specified JSON file.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Exports the current contacts and classes to a specified JSON file.\n"
            + "Parameters: FILE_PATH.json\n"
            + "Example: " + COMMAND_WORD + " /Users/user/Desktop/backup.json";

    public static final String MESSAGE_SUCCESS = "Data exported successfully to: %1$s";
    public static final String MESSAGE_EXPORT_ERROR = "Error exporting data to %1$s: %2$s";

    private final Path filePath;

    /**
     * @param filePath Path to the JSON file to export data to.
     */
    public ExportCommand(Path filePath) {
        requireNonNull(filePath);
        this.filePath = filePath;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        ReadOnlyAddressBook addressBook = model.getAddressBook();
        JsonSerializableAddressBook serializableAddressBook = new JsonSerializableAddressBook(addressBook);

        try {
            JsonUtil.saveJsonFile(serializableAddressBook, filePath);
        } catch (IOException ioe) {
            throw new CommandException(String.format(MESSAGE_EXPORT_ERROR, filePath.getFileName(), ioe.getMessage()),
                    ioe);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, filePath.getFileName()),
                CommandResult.DisplayType.RECENT);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ExportCommand)) {
            return false;
        }

        ExportCommand otherExportCommand = (ExportCommand) other;
        return filePath.equals(otherExportCommand.filePath);
    }
}