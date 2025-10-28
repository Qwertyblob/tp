package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.storage.JsonAddressBookStorage;

/**
 * Imports address book data from a specified JSON file.
 * This will replace all existing data in the application.
 */
public class ImportCommand extends Command {

    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Imports contacts and classes from a specified JSON file. "
            + "This will replace all current data in the app.\n"
            + "Parameters: FILE_PATH.json\n"
            + "Example: " + COMMAND_WORD + " /Users/user/Desktop/backup.json";

    public static final String MESSAGE_SUCCESS = "Data imported successfully from: %1$s";
    public static final String MESSAGE_IMPORT_ERROR = "Could not load data from file %1$s: %2$s";
    public static final String MESSAGE_FILE_NOT_FOUND = "File not found: %1$s";
    public static final String MESSAGE_FILE_EMPTY = "There is no data in the file";

    private final Path filePath;

    /**
     * @param filePath Path to the JSON file to import data from.
     */
    public ImportCommand(Path filePath) {
        requireNonNull(filePath);
        this.filePath = filePath;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!Files.exists(filePath)) {
            throw new CommandException(String.format(MESSAGE_FILE_NOT_FOUND, filePath.getFileName()));
        }

        // Use the existing JsonAddressBookStorage to read the file
        JsonAddressBookStorage importStorage = new JsonAddressBookStorage(filePath);
        Optional<ReadOnlyAddressBook> addressBookOptional;
        ReadOnlyAddressBook importedData;

        try {
            addressBookOptional = importStorage.readAddressBook();

            importedData = addressBookOptional
                    .orElseThrow(() -> new DataLoadingException(new Exception(MESSAGE_FILE_EMPTY)));
        } catch (DataLoadingException e) {
            throw new CommandException(
                    String.format(MESSAGE_IMPORT_ERROR, filePath.getFileName(), e.getMessage()), e);
        }

        // Replace the current data in the model with the imported data
        model.setAddressBook(importedData);

        return new CommandResult(String.format(MESSAGE_SUCCESS, filePath.getFileName()),
                CommandResult.DisplayType.DEFAULT);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ImportCommand)) {
            return false;
        }
        ImportCommand otherImportCommand = (ImportCommand) other;
        return filePath.equals(otherImportCommand.filePath);
    }

}
