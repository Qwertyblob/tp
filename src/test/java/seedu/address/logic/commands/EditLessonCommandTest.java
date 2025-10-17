package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_MATH;
import static seedu.address.logic.commands.CommandTestUtil.DESC_SCIENCE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_SCIENCE;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_LESSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_LESSON;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditLessonCommand.EditLessonDescriptor;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.IdentificationNumber;
import seedu.address.testutil.EditLessonDescriptorBuilder;
import seedu.address.testutil.LessonBuilder;
import seedu.address.testutil.TypicalLessons;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditLessonCommand.
 */
public class EditLessonCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = TypicalLessons.getTypicalModelManager();
    }

    @Test
    public void execute_allFieldsSpecified_success() {
        Lesson editedLesson = new LessonBuilder().withTags("Math", "Science").build();
        EditLessonDescriptor descriptor = new EditLessonDescriptorBuilder(editedLesson).build();
        EditLessonCommand editLessonCommand = new EditLessonCommand(INDEX_FIRST_LESSON, descriptor);

        String expectedMessage = String.format(EditLessonCommand.MESSAGE_EDIT_LESSON_SUCCESS, editedLesson);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setLesson(model.getFilteredLessonList().get(0), editedLesson);

        assertCommandSuccess(editLessonCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecified_success() {
        Index indexLastLesson = Index.fromOneBased(model.getFilteredLessonList().size());
        Lesson lastLesson = model.getFilteredLessonList().get(indexLastLesson.getZeroBased());

        LessonBuilder lessonInList = new LessonBuilder(lastLesson);
        Lesson editedLesson = lessonInList.withTags("Science").build();

        EditLessonDescriptor descriptor = new EditLessonDescriptorBuilder()
                .withTags("Science")
                .build();
        EditLessonCommand editLessonCommand = new EditLessonCommand(indexLastLesson, descriptor);

        String expectedMessage = String.format(EditLessonCommand.MESSAGE_EDIT_LESSON_SUCCESS, editedLesson);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setLesson(lastLesson, editedLesson);

        assertCommandSuccess(editLessonCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecified_success() {
        EditLessonCommand editLessonCommand =
                new EditLessonCommand(INDEX_FIRST_LESSON, new EditLessonDescriptor());
        Lesson editedLesson = model.getFilteredLessonList().get(INDEX_FIRST_LESSON.getZeroBased());

        String expectedMessage = String.format(EditLessonCommand.MESSAGE_EDIT_LESSON_SUCCESS, editedLesson);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(editLessonCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateLesson_failure() {
        Lesson firstLesson = model.getFilteredLessonList().get(INDEX_FIRST_LESSON.getZeroBased());
        EditLessonDescriptor descriptor = new EditLessonDescriptorBuilder(firstLesson).build();
        EditLessonCommand editLessonCommand = new EditLessonCommand(INDEX_SECOND_LESSON, descriptor);

        assertCommandFailure(editLessonCommand, model, EditLessonCommand.MESSAGE_DUPLICATE_LESSON);
    }

    @Test
    public void execute_invalidLessonIndex_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredLessonList().size() + 1);
        EditLessonDescriptor descriptor = new EditLessonDescriptorBuilder()
                .withTags(VALID_TAG_SCIENCE)
                .build();
        EditLessonCommand editLessonCommand = new EditLessonCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editLessonCommand, model, "The class index provided is invalid");
    }

    @Test
    public void execute_preservesStudentIdsAndAttendance() {
        // Arrange: pick the first lesson and store its students and attendance
        Lesson lessonToEdit = model.getFilteredLessonList().get(INDEX_FIRST_LESSON.getZeroBased());
        Set<IdentificationNumber> originalStudents = new HashSet<>(lessonToEdit.getStudents());
        Map<LocalDate, Set<IdentificationNumber>> originalAttendance = new HashMap<>();
        lessonToEdit.getAttendance().forEach((date, ids) ->
                originalAttendance.put(date, new HashSet<>(ids))
        );

        // Build a descriptor that edits only the tags
        EditLessonDescriptor descriptor = new EditLessonDescriptorBuilder()
                .withTags("EditedTag")
                .build();
        EditLessonCommand editLessonCommand = new EditLessonCommand(INDEX_FIRST_LESSON, descriptor);

        // Act
        String expectedMessage = String.format(EditLessonCommand.MESSAGE_EDIT_LESSON_SUCCESS,
                new LessonBuilder(lessonToEdit).withTags("EditedTag").build());
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        Lesson editedLesson = new LessonBuilder(lessonToEdit)
                .withTags("EditedTag")
                .build();
        expectedModel.setLesson(lessonToEdit, editedLesson);

        // Assert: command succeeds
        assertCommandSuccess(editLessonCommand, model, expectedMessage, expectedModel);

        // Assert: student IDs are unchanged
        Lesson updatedLesson = model.getFilteredLessonList().get(INDEX_FIRST_LESSON.getZeroBased());
        assertEquals(originalStudents, updatedLesson.getStudents(), "Student IDs should be preserved");

        // Assert: attendance map is unchanged
        assertEquals(originalAttendance, updatedLesson.getAttendance(), "Attendance should be preserved");
    }


    @Test
    public void equals() {
        final EditLessonCommand standardCommand = new EditLessonCommand(INDEX_FIRST_LESSON, DESC_MATH);

        // same values -> returns true
        EditLessonDescriptor copyDescriptor = new EditLessonDescriptor(DESC_MATH);
        EditLessonCommand commandWithSameValues = new EditLessonCommand(INDEX_FIRST_LESSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new Object()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditLessonCommand(INDEX_SECOND_LESSON, DESC_MATH)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditLessonCommand(INDEX_FIRST_LESSON, DESC_SCIENCE)));
    }
}
