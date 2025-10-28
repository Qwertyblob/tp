package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalLessons.getTypicalLessons;
import static seedu.address.testutil.TypicalPersons.getTypicalPersons;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.Person;
import seedu.address.storage.JsonAddressBookStorage;

public class ExportCommandTest {

    @TempDir
    public Path tempFolder;

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        // Initialize the model with typical data
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        for (Lesson lesson : getTypicalLessons()) {
            ab.addLesson(lesson);
        }
        model = new ModelManager(ab, new UserPrefs());
        expectedModel = new ModelManager(ab, new UserPrefs());
    }

    @Test
    public void execute_validFilePath_success() throws Exception {
        Path filePath = tempFolder.resolve("testExport.json");
        ExportCommand exportCommand = new ExportCommand(filePath);

        // Execute the command
        CommandResult commandResult = exportCommand.execute(model);

        // Check success message
        assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, filePath.getFileName()),
                commandResult.getFeedbackToUser());

        // Check that file was created
        assertTrue(Files.exists(filePath));

        // Check that the data in the file is correct
        JsonAddressBookStorage storage = new JsonAddressBookStorage(filePath);
        ReadOnlyAddressBook exportedData = storage.readAddressBook()
                .orElseThrow(() -> new DataLoadingException(new Exception("Error reading exported file")));

        assertEquals(expectedModel.getAddressBook(), exportedData);
    }

    @Test
    public void execute_fileExists_throwsCommandException() throws Exception {
        // Create an existing file
        Path filePath = tempFolder.resolve("existingFile.json");
        Files.createFile(filePath);
        assertTrue(Files.exists(filePath));

        ExportCommand exportCommand = new ExportCommand(filePath);

        // Try to execute, expect an error
        String expectedMessage = String.format(ExportCommand.MESSAGE_FILE_ALREADY_EXISTS,
                filePath.getFileName());
        assertThrows(CommandException.class, expectedMessage, () -> exportCommand.execute(model));
    }

    @Test
    public void execute_invalidPath_throwsCommandException() {
        // Use a path that is invalid (e.g., points to a directory)
        // Note: Writing to a directory will cause an IOException, which is caught and re-thrown
        Path invalidFilePath = tempFolder.resolve("nonexistent/test.json"); // Path is a directory, not a file
        ExportCommand exportCommand = new ExportCommand(invalidFilePath);

        assertThrows(CommandException.class,
                String.format(ExportCommand.MESSAGE_EXPORT_ERROR,
                        invalidFilePath.getFileName(), invalidFilePath), () -> exportCommand.execute(model));
    }

    @Test
    public void equals() {
        Path path1 = Paths.get("a.json");
        Path path2 = Paths.get("b.json");

        ExportCommand exportA = new ExportCommand(path1);
        ExportCommand exportAcopy = new ExportCommand(path1);
        ExportCommand exportB = new ExportCommand(path2);

        // same object -> returns true
        assertTrue(exportA.equals(exportA));

        // same values -> returns true
        assertTrue(exportA.equals(exportAcopy));

        // different types -> returns false
        assertFalse(exportA.equals(1));

        // null -> returns false
        assertFalse(exportA.equals(null));

        // different person -> returns false
        assertFalse(exportA.equals(exportB));
    }

}
