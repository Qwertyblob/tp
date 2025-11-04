package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CLASS_MATH;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalLessons.getTypicalModelManager;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.lesson.ClassName;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.IdentificationNumber;
import seedu.address.model.person.Person;
import seedu.address.testutil.LessonBuilder;
import seedu.address.testutil.PersonBuilder;

public class MarkCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = getTypicalModelManager();
        expectedModel = getTypicalModelManager();

        // Set up a pre-condition: Enrol AMY in the MATH class ("A1a") for testing.
        try {
            ClassName className = new ClassName(VALID_CLASS_MATH);
            IdentificationNumber studentId = AMY.getId();
            new EnrolCommand(studentId, className).execute(model);
            new EnrolCommand(studentId, className).execute(expectedModel);
        } catch (CommandException e) {
            throw new AssertionError("Setup for MarkCommandTest should not fail.");
        }
    }

    @Test
    public void execute_validStudentAndLesson_success() {
        ClassName className = new ClassName(VALID_CLASS_MATH);
        IdentificationNumber studentId = AMY.getId(); // Use the ID
        MarkCommand markAttendanceCommand = new MarkCommand(studentId, className);

        Lesson lessonToUpdate = expectedModel.getFilteredLessonList().stream()
                .filter(l -> l.getClassName().equals(className)).findFirst().get();

        Person personToUpdate = expectedModel.getFilteredPersonList().stream()
                .filter(p -> p.getId().equals(studentId)).findFirst().get();

        // Create the updated Lesson
        Map<LocalDate, Set<IdentificationNumber>> updatedAttendance = new HashMap<>(lessonToUpdate.getAttendance());
        updatedAttendance.computeIfAbsent(LocalDate.now(), k -> new HashSet<>()).add(studentId);
        Lesson updatedLesson = new LessonBuilder(lessonToUpdate).withAttendance(updatedAttendance).build();

        // Create the updated Person
        Set<Lesson> updatedLessonSet = new HashSet<>(personToUpdate.getLessons());
        updatedLessonSet.removeIf(l -> l.getClassName().equals(className)); // Remove old
        updatedLessonSet.add(updatedLesson); // Add new

        Person updatedAmy = new Person(
                personToUpdate.getId(),
                personToUpdate.getName(),
                personToUpdate.getRole(),
                updatedLessonSet,
                personToUpdate.getPhone(),
                personToUpdate.getEmail(),
                personToUpdate.getAddress(),
                personToUpdate.getTags()
        );

        // Set both updated objects in the expected model
        expectedModel.setLesson(lessonToUpdate, updatedLesson);
        expectedModel.setPerson(personToUpdate, updatedAmy); // Use the person from the model as the target

        String expectedMessage = String.format(MarkCommand.MESSAGE_SUCCESS,
                Messages.shortenedFormatPerson(personToUpdate), Messages.shortenedFormatLesson(lessonToUpdate));

        assertCommandSuccess(markAttendanceCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateAttendance_throwsCommandException() throws CommandException {
        ClassName className = new ClassName(VALID_CLASS_MATH);
        Person studentToMark = AMY;

        // Mark attendance once to set up the state
        new MarkCommand(studentToMark.getId(), className).execute(model);

        // Try to mark it a second time
        MarkCommand duplicateCommand = new MarkCommand(studentToMark.getId(), className);
        assertCommandFailure(duplicateCommand, model, String.format(MarkCommand.MESSAGE_ALREADY_MARKED,
                Messages.shortenedFormatPerson(studentToMark), className.fullClassName));
    }

    @Test
    public void execute_studentNotEnrolled_throwsCommandException() {
        // BOB is not enrolled in the Math class
        ClassName className = new ClassName(VALID_CLASS_MATH);
        Person studentNotEnrolled = BOB;
        MarkCommand command = new MarkCommand(studentNotEnrolled.getId(), className);

        assertCommandFailure(command, model, MarkCommand.MESSAGE_STUDENT_NOT_ENROLLED);
    }

    @Test
    public void execute_lessonNotFound_throwsCommandException() {
        ClassName nonExistentClassName = new ClassName("Z9z");
        MarkCommand command = new MarkCommand(AMY.getId(), nonExistentClassName);

        assertCommandFailure(command, model, String.format(MarkCommand.MESSAGE_LESSON_NOT_FOUND, nonExistentClassName));
    }

    @Test
    public void execute_studentNotFound_throwsCommandException() {
        ClassName className = new ClassName(VALID_CLASS_MATH);
        IdentificationNumber nonExistentStudentId = new IdentificationNumber("S9999999");
        MarkCommand command = new MarkCommand(nonExistentStudentId, className);

        assertCommandFailure(command, model, String.format(MarkCommand.MESSAGE_PERSON_NOT_FOUND, nonExistentStudentId));
    }

    @Test
    public void equals() {
        final ClassName mathClass = new ClassName(VALID_CLASS_MATH);
        final ClassName scienceClass = new ClassName("B2b");
        final IdentificationNumber amyId = AMY.getId();
        final IdentificationNumber bobId = BOB.getId();

        final MarkCommand standardCommand = new MarkCommand(amyId, mathClass);

        // same values -> returns true
        MarkCommand commandWithSameValues = new MarkCommand(amyId, mathClass);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different class name -> returns false
        assertFalse(standardCommand.equals(new MarkCommand(amyId, scienceClass)));

        // different student id -> returns false
        assertFalse(standardCommand.equals(new MarkCommand(bobId, mathClass)));
    }
}
