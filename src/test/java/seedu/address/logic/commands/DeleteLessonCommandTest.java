package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.ConfirmationManager.MESSAGE_ACTION_CANCELLED;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertConfirmableCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.assertConfirmationRequested;
import static seedu.address.logic.commands.CommandTestUtil.showLessonAtIndex;
import static seedu.address.logic.commands.DeleteLessonCommand.MESSAGE_DELETE_LESSON_SUCCESS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_LESSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_LESSON;
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
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteLessonCommand}.
 */
public class DeleteLessonCommandTest {

    private Model model;
    private Model expectedModel;
    private ConfirmationManager confirmationManager;

    @BeforeEach
    public void setUp() {
        model = getTypicalModelManager();
        expectedModel = getTypicalModelManager();
        confirmationManager = new ConfirmationManager();
    }

    @Test
    public void execute_validIndexUnfilteredList_requestsConfirmation() {
        DeleteLessonCommand deleteCommand = new DeleteLessonCommand(INDEX_FIRST_LESSON);
        assertConfirmationRequested(deleteCommand, model);
    }

    @Test
    public void execute_validIndexUnfilteredList_updatesPendingCommand() throws CommandException {
        DeleteLessonCommand deleteCommand = new DeleteLessonCommand(INDEX_FIRST_LESSON);
        confirmationManager.requestConfirmation(deleteCommand, model);

        assertEquals(model.getPendingCommand(), deleteCommand);
    }

    @Test
    public void execute_validIndexUnfilteredList_cancelled() throws Exception {
        DeleteLessonCommand deleteCommand = new DeleteLessonCommand(INDEX_FIRST_LESSON);
        model.updatePendingCommand(deleteCommand);

        CommandResult expectedCommandResult = new CommandResult(
                MESSAGE_ACTION_CANCELLED, CommandResult.DisplayType.RECENT);

        assertEquals(confirmationManager.handleUserResponse(ConfirmationManager.INPUT_CANCEL, model),
                expectedCommandResult);
    }

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Lesson lessonToDelete = model.getFilteredLessonList().get(INDEX_FIRST_LESSON.getZeroBased());
        DeleteLessonCommand deleteCommand = new DeleteLessonCommand(INDEX_FIRST_LESSON);

        String expectedMessage = String.format(MESSAGE_DELETE_LESSON_SUCCESS,
                Messages.formatLesson(lessonToDelete));

        expectedModel.deleteLesson(lessonToDelete);

        assertConfirmableCommandSuccess(deleteCommand, model,
                new CommandResult(expectedMessage, CommandResult.DisplayType.DEFAULT),
                expectedModel, confirmationManager);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredLessonList().size() + 1);
        DeleteLessonCommand deleteCommand = new DeleteLessonCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_LESSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_requestsConfirmation() {
        showLessonAtIndex(model, INDEX_FIRST_LESSON);

        DeleteLessonCommand deleteCommand = new DeleteLessonCommand(INDEX_FIRST_LESSON);

        assertConfirmationRequested(deleteCommand, model);

        Model expectedModel = getTypicalModelManager();
        assertEquals(model.getAddressBook(), expectedModel.getAddressBook());
    }

    @Test
    public void execute_validIndexFilteredList_updatesPendingCommand() throws CommandException {
        showLessonAtIndex(model, INDEX_FIRST_LESSON);

        DeleteLessonCommand deleteCommand = new DeleteLessonCommand(INDEX_FIRST_LESSON);
        confirmationManager.requestConfirmation(deleteCommand, model);

        assertEquals(model.getPendingCommand(), deleteCommand);
    }

    @Test
    public void execute_validIndexFilteredList_cancelled() throws Exception {
        showLessonAtIndex(model, INDEX_FIRST_LESSON);

        DeleteLessonCommand deleteCommand = new DeleteLessonCommand(INDEX_FIRST_LESSON);
        model.updatePendingCommand(deleteCommand);

        CommandResult expectedCommandResult = new CommandResult(
                MESSAGE_ACTION_CANCELLED, CommandResult.DisplayType.RECENT);

        assertEquals(confirmationManager.handleUserResponse(ConfirmationManager.INPUT_CANCEL, model),
                expectedCommandResult);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showLessonAtIndex(model, INDEX_FIRST_LESSON);

        Lesson lessonToDelete = model.getFilteredLessonList().get(INDEX_FIRST_LESSON.getZeroBased());
        DeleteLessonCommand deleteCommand = new DeleteLessonCommand(INDEX_FIRST_LESSON);

        String expectedMessage = String.format(MESSAGE_DELETE_LESSON_SUCCESS,
                Messages.formatLesson(lessonToDelete));

        expectedModel.deleteLesson(lessonToDelete);
        showNoLesson(expectedModel);

        assertConfirmableCommandSuccess(deleteCommand, model,
                new CommandResult(expectedMessage, CommandResult.DisplayType.DEFAULT),
                expectedModel, confirmationManager);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showLessonAtIndex(model, INDEX_FIRST_LESSON);

        Index outOfBoundIndex = INDEX_SECOND_LESSON;
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getLessonList().size());

        DeleteLessonCommand deleteCommand = new DeleteLessonCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_LESSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_deleteLesson_unenrollsFromAssociatedPersons() {
        Lesson lessonToDelete = model.getFilteredLessonList().get(INDEX_SECOND_LESSON.getZeroBased());
        DeleteLessonCommand deleteCommand = new DeleteLessonCommand(INDEX_SECOND_LESSON);

        String expectedMessage = String.format(DeleteLessonCommand.MESSAGE_DELETE_LESSON_SUCCESS,
                Messages.formatLesson(lessonToDelete));

        expectedModel.deleteLesson(lessonToDelete);

        // Update affected persons to remove deleted lesson
        for (Person p : expectedModel.getFilteredPersonList()) {
            if (p.getLessons().contains(lessonToDelete)) {
                Person updated = new PersonBuilder(p).withoutLesson(lessonToDelete).build();
                expectedModel.setPerson(p, updated);
            }
        }

        assertConfirmableCommandSuccess(deleteCommand, model,
                new CommandResult(expectedMessage, CommandResult.DisplayType.DEFAULT),
                expectedModel, confirmationManager);
    }

    @Test
    public void equals() {
        DeleteLessonCommand deleteFirstCommand = new DeleteLessonCommand(INDEX_FIRST_LESSON);
        DeleteLessonCommand deleteSecondCommand = new DeleteLessonCommand(INDEX_SECOND_LESSON);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteLessonCommand deleteFirstCommandCopy = new DeleteLessonCommand(INDEX_FIRST_LESSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different lesson -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteLessonCommand deleteCommand = new DeleteLessonCommand(targetIndex);
        String expected = DeleteLessonCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no lessons.
     */
    private void showNoLesson(Model model) {
        model.updateFilteredLessonList(l -> false);
        assertTrue(model.getFilteredLessonList().isEmpty());
    }
}
