package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.ConfirmationManager.MESSAGE_ACTION_CANCELLED;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.assertConfirmableCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.assertConfirmationRequested;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.logic.commands.DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS;
import static seedu.address.logic.commands.DeleteCommand.MESSAGE_MULTIPLE_MATCHING_NAMES;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_FOURTH_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalLessons.getTypicalModelManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.ConfirmationManager;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.IdentificationNumber;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.testutil.LessonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model;
    private Model expectedModel;
    private ConfirmationManager confirmationManager;

    @BeforeEach
    public void setUp() {
        // Use getTypicalModelManager for a fully initialized model
        model = getTypicalModelManager();
        expectedModel = getTypicalModelManager();
        confirmationManager = new ConfirmationManager();
    }

    @Test
    public void execute_validIndexUnfilteredList_requestsConfirmation() {
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        assertConfirmationRequested(deleteCommand, model);
    }

    @Test
    public void execute_validIndexUnfilteredList_updatesPendingCommand() throws CommandException {
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        confirmationManager.requestConfirmation(deleteCommand, model);

        assertEquals(model.getPendingCommand(), deleteCommand);
    }

    @Test
    public void execute_validIndexUnfilteredList_cancelled() throws Exception {
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        model.updatePendingCommand(deleteCommand);

        CommandResult expectedCommandResult = new CommandResult(
                MESSAGE_ACTION_CANCELLED, CommandResult.DisplayType.RECENT);

        assertEquals(confirmationManager.handleUserResponse(ConfirmationManager.INPUT_CANCEL, model),
                expectedCommandResult);
    }

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.formatPerson(personToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertConfirmableCommandSuccess(deleteCommand, model,
                new CommandResult(expectedMessage, CommandResult.DisplayType.DEFAULT),
                expectedModel, confirmationManager);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_multipleMatchingNames_throwsCommandException() {
        // Create a different person with the same name
        Person originalPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person otherPerson = new Person(new IdentificationNumber("T9999999"), originalPerson.getName(),
                originalPerson.getRole(), originalPerson.getPhone(), new Email("differentperson@gmail.com"),
                new Address("differentaddress"), originalPerson.getTags());

        model.addPerson(otherPerson);
        Name duplicateName = originalPerson.getName();
        DeleteCommand deleteCommand = new DeleteCommand(duplicateName);

        assertCommandFailure(deleteCommand, model,
                MESSAGE_MULTIPLE_MATCHING_NAMES);
    }


    @Test
    public void execute_validIndexFilteredList_requestsConfirmation() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        assertConfirmationRequested(deleteCommand, model);

        // AddressBook remains unchanged until confirmation
        Model expectedModel = getTypicalModelManager();
        assertEquals(model.getAddressBook(), expectedModel.getAddressBook());
    }

    @Test
    public void execute_validIndexFilteredList_updatesPendingCommand() throws CommandException {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        confirmationManager.requestConfirmation(deleteCommand, model);

        assertEquals(model.getPendingCommand(), deleteCommand);
    }

    @Test
    public void execute_validIndexFilteredList_cancelled() throws Exception {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        model.updatePendingCommand(deleteCommand);

        CommandResult expectedCommandResult = new CommandResult(
                MESSAGE_ACTION_CANCELLED, CommandResult.DisplayType.RECENT);

        assertEquals(confirmationManager.handleUserResponse(ConfirmationManager.INPUT_CANCEL, model),
                expectedCommandResult);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.formatPerson(personToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        showNoPerson(expectedModel);

        assertConfirmableCommandSuccess(deleteCommand, model,
                new CommandResult(expectedMessage, CommandResult.DisplayType.DEFAULT),
                expectedModel, confirmationManager);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_forcedDeleteByIndex_successWithoutConfirmation() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand forcedDeleteCommand = new DeleteCommand(INDEX_FIRST_PERSON, true);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.formatPerson(personToDelete));

        expectedModel.deletePerson(personToDelete);

        CommandResult expectedResult = new CommandResult(expectedMessage, CommandResult.DisplayType.DEFAULT);
        assertCommandSuccess(forcedDeleteCommand, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_forcedDeleteByName_successWithoutConfirmation() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Name name = personToDelete.getName();

        DeleteCommand forcedDeleteCommand = new DeleteCommand(name, true);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.formatPerson(personToDelete));
        expectedModel.deletePerson(personToDelete);

        CommandResult expectedResult = new CommandResult(expectedMessage, CommandResult.DisplayType.DEFAULT);
        assertCommandSuccess(forcedDeleteCommand, model, expectedResult, expectedModel);
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        DeleteCommand deleteSecondCommand = new DeleteCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void execute_deletePerson_unenrollsFromAssociatedLessons() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_SECOND_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.formatPerson(personToDelete));

        expectedModel.deletePerson(personToDelete);
        Lesson lessonToUpdate = expectedModel.getFilteredLessonList().stream()
                .filter(l -> l.getClassName().toString().equals("D6f"))
                .findFirst().get();
        Lesson updatedLesson = new LessonBuilder(lessonToUpdate).withStudents().build(); // Lesson is now empty
        expectedModel.setLesson(lessonToUpdate, updatedLesson);

        assertConfirmableCommandSuccess(deleteCommand, model, new CommandResult(expectedMessage,
                CommandResult.DisplayType.DEFAULT), expectedModel, confirmationManager);

    }

    @Test
    public void validate_personNotAssignedToAnyLessons_doesNotThrow() {
        // Pick a person who isn't a tutor in any class
        Person person = model.getFilteredPersonList().get(INDEX_FOURTH_PERSON.getOneBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FOURTH_PERSON);

        assertDoesNotThrow(() -> deleteCommand.validate(model));
    }

    @Test
    public void validate_tutorAssignedToLessons_throwsCommandException() {
        // Pick a tutor who is assigned to at least one class
        Person tutor = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_SECOND_PERSON);

        CommandException thrown = assertThrows(CommandException.class, () -> deleteCommand.validate(model));

        // Check the message contains tutor name and at least one class name
        String expectedName = tutor.getName().toString();
        assertTrue(thrown.getMessage().contains(expectedName));
        assertTrue(thrown.getMessage().contains("class")); // rough check
    }

    @Test
    public void validate_personNotFound_throwsCommandException() {
        // Name that does not exist in model
        Name nonexistent = new Name("Ghost Person");
        DeleteCommand deleteCommand = new DeleteCommand(nonexistent);

        assertThrows(CommandException.class, () -> deleteCommand.validate(model));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteCommand deleteCommand = new DeleteCommand(targetIndex);
        String expected = DeleteCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }

}
