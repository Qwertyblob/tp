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
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.lesson.ClassName;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.IdentificationNumber;
import seedu.address.model.person.Person;
import seedu.address.testutil.LessonBuilder;

public class UnmarkCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = getTypicalModelManager();
        expectedModel = getTypicalModelManager();

        // Pre-condition: Enrol AMY in the MATH class ("A1a") for testing.
        try {
            ClassName className = new ClassName(VALID_CLASS_MATH);
            IdentificationNumber studentId = AMY.getId();
            new EnrolCommand(studentId, className).execute(model);
            new EnrolCommand(studentId, className).execute(expectedModel);
        } catch (CommandException e) {
            throw new AssertionError("Setup for UnmarkCommandTest should not fail.");
        }
    }

    @Test
    public void execute_validStudentAndLesson_success() throws CommandException {
        ClassName className = new ClassName(VALID_CLASS_MATH);
        IdentificationNumber studentId = AMY.getId();
        LocalDate today = LocalDate.now();

        // Setup: Mark the student in both models first
        new MarkCommand(studentId, className).execute(model);
        new MarkCommand(studentId, className).execute(expectedModel);

        UnmarkCommand unmarkCommand = new UnmarkCommand(studentId, className, Optional.of(today));

        Lesson lessonToUnmark = expectedModel.getFilteredLessonList().stream()
                .filter(l -> l.getClassName().equals(className)).findFirst().get();
        Person personToUpdate = expectedModel.getFilteredPersonList().stream()
                .filter(p -> p.getId().equals(studentId)).findFirst().get();

        Map<LocalDate, Set<IdentificationNumber>> unmarkedAttendance = new HashMap<>(lessonToUnmark.getAttendance());
        if (unmarkedAttendance.containsKey(today)) {
            unmarkedAttendance.get(today).remove(studentId);
        }
        Lesson updatedLesson = new LessonBuilder(lessonToUnmark).withAttendance(unmarkedAttendance).build();

        // Create the "updated" Person with the new lesson
        Set<Lesson> updatedLessonSet = new HashSet<>(personToUpdate.getLessons());
        updatedLessonSet.removeIf(l -> l.getClassName().equals(className)); // Remove old lesson version
        updatedLessonSet.add(updatedLesson); // Add new lesson version
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
        expectedModel.setLesson(lessonToUnmark, updatedLesson);
        expectedModel.setPerson(personToUpdate, updatedAmy); // This prevents PersonNotFoundException

        String expectedMessage = String.format(UnmarkCommand.MESSAGE_SUCCESS,
                personToUpdate.getName().fullName, className.fullClassName, today);

        assertCommandSuccess(unmarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_studentNotMarked_throwsCommandException() {
        // Amy is enrolled but not marked present
        ClassName className = new ClassName(VALID_CLASS_MATH);
        Person studentToUnmark = AMY;
        UnmarkCommand unmarkCommand = new UnmarkCommand(studentToUnmark.getId(), className, Optional.empty());

        String expectedMessage = String.format(UnmarkCommand.MESSAGE_NOT_MARKED,
                studentToUnmark.getName().fullName, className.fullClassName, LocalDate.now());

        assertCommandFailure(unmarkCommand, model, expectedMessage);
    }

    @Test
    public void execute_studentNotEnrolled_throwsCommandException() {
        // BOB is not enrolled in the Math class
        ClassName className = new ClassName(VALID_CLASS_MATH);
        Person studentNotEnrolled = BOB;
        UnmarkCommand command = new UnmarkCommand(studentNotEnrolled.getId(), className, Optional.empty());

        assertCommandFailure(command, model, UnmarkCommand.MESSAGE_STUDENT_NOT_ENROLLED);
    }

    @Test
    public void execute_lessonNotFound_throwsCommandException() {
        ClassName nonExistentClassName = new ClassName("Z9z");
        UnmarkCommand command = new UnmarkCommand(AMY.getId(), nonExistentClassName, Optional.empty());

        assertCommandFailure(command,
                model,
                String.format(UnmarkCommand.MESSAGE_LESSON_NOT_FOUND, nonExistentClassName));
    }

    @Test
    public void execute_studentNotFound_throwsCommandException() {
        ClassName className = new ClassName(VALID_CLASS_MATH);
        IdentificationNumber nonExistentStudentId = new IdentificationNumber("S9999999");
        UnmarkCommand command = new UnmarkCommand(nonExistentStudentId, className, Optional.empty());

        assertCommandFailure(command,
                model,
                String.format(UnmarkCommand.MESSAGE_PERSON_NOT_FOUND, nonExistentStudentId));
    }

    @Test
    public void equals() {
        final ClassName mathClass = new ClassName(VALID_CLASS_MATH);
        final ClassName scienceClass = new ClassName("B2b");
        final IdentificationNumber amyId = AMY.getId();
        final IdentificationNumber bobId = BOB.getId();
        final Optional<LocalDate> today = Optional.of(LocalDate.now());

        final UnmarkCommand standardCommand = new UnmarkCommand(amyId, mathClass, Optional.empty());

        // same values -> returns true
        UnmarkCommand commandWithSameValues = new UnmarkCommand(amyId, mathClass, Optional.empty());
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different class name -> returns false
        assertFalse(standardCommand.equals(new UnmarkCommand(amyId, scienceClass, Optional.empty())));

        // different student id -> returns false
        assertFalse(standardCommand.equals(new UnmarkCommand(bobId, mathClass, Optional.empty())));

        // different date -> returns false
        assertFalse(standardCommand.equals(new UnmarkCommand(amyId, mathClass, today)));
    }

}
