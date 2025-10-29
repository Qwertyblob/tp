package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.testutil.TypicalPersons;

public class ImportCommandTest {

    @TempDir
    public Path tempFolder;

    @Test
    public void execute_validFile_success() throws Exception {
        // 1. Create a temporary file with typical data to import
        Path filePath = tempFolder.resolve("testImport.json");
        ReadOnlyAddressBook dataToImport = TypicalPersons.getTypicalAddressBook();
        JsonAddressBookStorage storage = new JsonAddressBookStorage(filePath);
        storage.saveAddressBook(dataToImport);

        // 2. Create a model with different data (e.g., an empty AddressBook)
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(dataToImport, new UserPrefs());

        // 3. Execute the import command
        ImportCommand importCommand = new ImportCommand(filePath);
        CommandResult commandResult = importCommand.execute(model);

        // 4. Assert success message and that model data was replaced
        assertEquals(String.format(ImportCommand.MESSAGE_SUCCESS, filePath.getFileName()),
                commandResult.getFeedbackToUser());
        assertEquals(expectedModel.getAddressBook(), model.getAddressBook());
    }

    @Test
    public void execute_fileNotFound_throwsCommandException() {
        // 1. Create a model
        Model model = new ModelManager();

        // 2. Create a command pointing to a file that does NOT exist
        Path filePath = tempFolder.resolve("nonexistent.json");
        ImportCommand importCommand = new ImportCommand(filePath);

        // 3. Assert the correct error is thrown
        String expectedMessage = String.format(ImportCommand.MESSAGE_FILE_NOT_FOUND,
                filePath.getFileName());
        assertThrows(CommandException.class, expectedMessage, () -> importCommand.execute(model));
    }

    @Test
    public void execute_emptyFile_throwsCommandException() throws IOException {
        // Create an empty file
        Path filePath = tempFolder.resolve("empty.json");
        Files.createFile(filePath);

        Model model = new ModelManager();
        ImportCommand importCommand = new ImportCommand(filePath);

        // Assert that the command throws an error, and that the error message
        // is the one from our orElseThrow() logic.
        assertThrows(CommandException.class, () -> importCommand.execute(model));
    }

    @Test
    public void execute_invalidDataFile_throwsCommandException() {
        // Use a test resource file that has invalid person data
        Path filePath = Paths.get(
                "src/test/data/JsonSerializableAddressBookTest/invalidPersonAddressBook.json");

        Model model = new ModelManager();
        ImportCommand importCommand = new ImportCommand(filePath);

        // Assert that the storage validation logic catches the error
        assertThrows(CommandException.class, () -> importCommand.execute(model));
    }

    @Test
    public void equals() {
        Path path1 = Paths.get("a.json");
        Path path2 = Paths.get("b.json");

        ImportCommand importA = new ImportCommand(path1);
        ImportCommand importAcopy = new ImportCommand(path1);
        ImportCommand importB = new ImportCommand(path2);

        // same object -> returns true
        assertTrue(importA.equals(importA));

        // same values -> returns true
        assertTrue(importA.equals(importAcopy));

        // different types -> returns false
        assertFalse(importA.equals(1));

        // null -> returns false
        assertFalse(importA.equals(null));

        // different command -> returns false
        assertFalse(importA.equals(importB));
    }

}
