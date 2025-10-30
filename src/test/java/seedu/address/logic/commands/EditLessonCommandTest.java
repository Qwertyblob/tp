package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_LESSON_DISPLAYED_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.DESC_MATH;
import static seedu.address.logic.commands.CommandTestUtil.DESC_SCIENCE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_SCIENCE;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_LESSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_LESSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_LESSON;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditLessonCommand.EditLessonDescriptor;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.IdentificationNumber;
import seedu.address.model.util.LessonCascadeUpdater;
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

        String expectedMessage = String.format(EditLessonCommand.MESSAGE_EDIT_LESSON_SUCCESS,
                Messages.formatLesson(editedLesson));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        Lesson lessonToEdit = model.getFilteredLessonList().get(INDEX_FIRST_LESSON.getZeroBased());
        expectedModel.setLesson(lessonToEdit, editedLesson);

        LessonCascadeUpdater.updateStudentsWithEditedLesson(expectedModel, lessonToEdit, editedLesson);

        assertCommandSuccess(editLessonCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecified_success() {
        Index indexLastLesson = Index.fromOneBased(model.getFilteredLessonList().size());
        Lesson lastLesson = model.getFilteredLessonList().get(indexLastLesson.getZeroBased());

        Lesson editedLesson = new LessonBuilder(lastLesson)
                .withTags("Science")
                .build();

        EditLessonDescriptor descriptor = new EditLessonDescriptorBuilder()
                .withTags("Science")
                .build();
        EditLessonCommand editLessonCommand = new EditLessonCommand(indexLastLesson, descriptor);

        String expectedMessage = String.format(EditLessonCommand.MESSAGE_EDIT_LESSON_SUCCESS,
                Messages.formatLesson(editedLesson));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setLesson(lastLesson, editedLesson);

        LessonCascadeUpdater.updateStudentsWithEditedLesson(expectedModel, lastLesson, editedLesson);

        assertCommandSuccess(editLessonCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecified_success() {
        EditLessonCommand editLessonCommand =
                new EditLessonCommand(INDEX_THIRD_LESSON, new EditLessonDescriptor());
        Lesson editedLesson = model.getFilteredLessonList().get(INDEX_THIRD_LESSON.getZeroBased());

        String expectedMessage = String.format(EditLessonCommand.MESSAGE_EDIT_LESSON_SUCCESS,
                Messages.formatLesson(editedLesson));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        Lesson lessonToEdit = expectedModel.getFilteredLessonList().get(INDEX_THIRD_LESSON.getZeroBased());

        // simulate same cascade if your command does it unconditionally
        LessonCascadeUpdater.updateStudentsWithEditedLesson(expectedModel, lessonToEdit, editedLesson);

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

        assertCommandFailure(editLessonCommand, model, MESSAGE_INVALID_LESSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_tutorClash_failure() {
        Lesson firstLesson = model.getFilteredLessonList().get(INDEX_FIRST_LESSON.getZeroBased());
        Lesson secondLesson = model.getFilteredLessonList().get(INDEX_SECOND_LESSON.getZeroBased());

        // Make edited lesson share the same tutor and overlapping time as another lesson
        EditLessonDescriptor descriptor = new EditLessonDescriptorBuilder(secondLesson)
                .withTutor(firstLesson.getTutor().toString()) // same tutor
                .withDay(firstLesson.getDay().toString()) // same day
                .withTime(firstLesson.getTime().toString()) // overlapping time
                .build();

        EditLessonCommand editLessonCommand = new EditLessonCommand(INDEX_SECOND_LESSON, descriptor);

        assertCommandFailure(editLessonCommand, model, EditLessonCommand.MESSAGE_TUTOR_TIME_CLASH);
    }

    @Test
    public void execute_studentClash_failure() {
        Lesson firstLesson = model.getFilteredLessonList().get(INDEX_FIRST_LESSON.getZeroBased());
        Lesson secondLesson = model.getFilteredLessonList().get(INDEX_THIRD_LESSON.getZeroBased());

        // Create a shared student between the two lessons
        IdentificationNumber sharedStudent = new IdentificationNumber("S6767676");

        // Update first and second lessons to include the shared student
        Lesson updatedFirstLesson = new LessonBuilder(firstLesson)
                .withStudents(new HashSet<>(firstLesson.getStudents()) {{ add(sharedStudent); }}).build();
        model.setLesson(firstLesson, updatedFirstLesson);

        Lesson updatedSecondLesson = new LessonBuilder(secondLesson)
                .withStudents(new HashSet<>(secondLesson.getStudents()) {{ add(sharedStudent); }}).build();
        model.setLesson(secondLesson, updatedSecondLesson);

        // Edit second lesson to overlap with first lesson
        EditLessonDescriptor descriptor = new EditLessonDescriptorBuilder(updatedSecondLesson)
                .withDay(updatedFirstLesson.getDay().toString())
                .withTime(updatedFirstLesson.getTime().toString())
                .build();

        EditLessonCommand editLessonCommand = new EditLessonCommand(INDEX_THIRD_LESSON, descriptor);

        // Expected message
        String expectedMessage = "Student clash detected for: " + sharedStudent.getValue();

        // Verify failure
        assertCommandFailure(editLessonCommand, model, expectedMessage);
    }



    @Test
    public void execute_preservesStudentIdsAndAttendance() {
        Lesson lessonToEdit = model.getFilteredLessonList().get(INDEX_THIRD_LESSON.getZeroBased());
        Set<IdentificationNumber> originalStudents = new HashSet<>(lessonToEdit.getStudents());
        Map<LocalDate, Set<IdentificationNumber>> originalAttendance = new HashMap<>();
        lessonToEdit.getAttendance().forEach((date, ids) ->
                originalAttendance.put(date, new HashSet<>(ids))
        );

        EditLessonDescriptor descriptor = new EditLessonDescriptorBuilder()
                .withTags("EditedTag")
                .build();
        EditLessonCommand editLessonCommand = new EditLessonCommand(INDEX_THIRD_LESSON, descriptor);

        Lesson editedLesson = new LessonBuilder(lessonToEdit)
                .withTags("EditedTag")
                .build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setLesson(lessonToEdit, editedLesson);
        LessonCascadeUpdater.updateStudentsWithEditedLesson(expectedModel, lessonToEdit, editedLesson);

        String expectedMessage = String.format(EditLessonCommand.MESSAGE_EDIT_LESSON_SUCCESS,
                Messages.formatLesson(editedLesson));
        assertCommandSuccess(editLessonCommand, model, expectedMessage, expectedModel);

        // Verify student IDs and attendance are unchanged
        Lesson updatedLesson = model.getFilteredLessonList().get(INDEX_THIRD_LESSON.getZeroBased());
        assertEquals(originalStudents, updatedLesson.getStudents(), "Student IDs should be preserved");
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
