package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CLASS_MATH;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalLessons.getTypicalModelManager;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.util.Set;
import java.util.stream.Collectors;

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

public class UnenrolCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = getTypicalModelManager();
        expectedModel = getTypicalModelManager();

        // Ensure AMY is already enrolled in the math class for testing unenrolment
        ClassName className = new ClassName(VALID_CLASS_MATH);
        EnrolCommand enrolCommand = new EnrolCommand(AMY.getId(), className);
        try {
            enrolCommand.execute(model);
            enrolCommand.execute(expectedModel);
        } catch (CommandException e) {
            // Should not fail
        }
    }

    @Test
    public void execute_validStudentAndLesson_success() {
        // Models with typical data
        ClassName classNameToUnenrol = new ClassName(VALID_CLASS_MATH);
        IdentificationNumber studentIdToUnenrol = AMY.getId();
        UnenrolCommand unenrolCommand = new UnenrolCommand(studentIdToUnenrol, classNameToUnenrol);

        // Get the actual lesson in the model
        Lesson lessonInModel = model.getFilteredLessonList().stream()
                .filter(l -> l.getClassName().equals(classNameToUnenrol))
                .findFirst().get();

        // Build the expected lesson with one fewer student
        Set<IdentificationNumber> newStudentIds = lessonInModel.getStudents().stream()
                .filter(id -> !id.equals(studentIdToUnenrol))
                .collect(Collectors.toSet());
        Lesson expectedLesson = new LessonBuilder(lessonInModel)
                .withStudents(newStudentIds.stream()
                        .map(IdentificationNumber::toString)
                        .toArray(String[]::new))
                .build();
        expectedModel.setLesson(lessonInModel, expectedLesson);

        // --- Critical section: find matching person in expectedModel ---
        Person personInExpectedModel = expectedModel.getFilteredPersonList().stream()
                .filter(p -> p.getId().equals(studentIdToUnenrol))
                .findFirst()
                .orElseThrow();

        // Build updated person based on AMY’s lesson info
        Set<Lesson> newLessons = personInExpectedModel.getLessons().stream()
                .filter(l -> !l.getClassName().equals(classNameToUnenrol))
                .collect(Collectors.toSet());
        Person expectedPerson = new Person(
                personInExpectedModel.getId(),
                personInExpectedModel.getName(),
                personInExpectedModel.getRole(),
                newLessons,
                personInExpectedModel.getPhone(),
                personInExpectedModel.getEmail(),
                personInExpectedModel.getAddress(),
                personInExpectedModel.getTags()
        );
        expectedModel.setPerson(personInExpectedModel, expectedPerson);

        String expectedMessage = String.format(
                UnenrolCommand.MESSAGE_UNENROL_SUCCESS,
                Messages.shortenedFormatPerson(expectedPerson),
                Messages.shortenedFormatLesson(expectedLesson)
        );

        assertCommandSuccess(unenrolCommand, model, expectedMessage, expectedModel);
    }


    @Test
    public void execute_studentNotInLesson_throwsCommandException() {
        ClassName className = new ClassName(VALID_CLASS_MATH);
        IdentificationNumber studentId = BOB.getId(); // Not enrolled in math
        UnenrolCommand unenrolCommand = new UnenrolCommand(studentId, className);

        assertCommandFailure(unenrolCommand, model, UnenrolCommand.MESSAGE_STUDENT_NOT_ENROLLED);
    }

    @Test
    public void execute_lessonNotFound_throwsCommandException() {
        ClassName nonExistentClassName = new ClassName("Z9z");
        UnenrolCommand unenrolCommand = new UnenrolCommand(AMY.getId(), nonExistentClassName);

        assertCommandFailure(unenrolCommand, model, UnenrolCommand.MESSAGE_LESSON_NOT_FOUND);
    }

    @Test
    public void execute_studentNotFound_throwsCommandException() {
        ClassName className = new ClassName(VALID_CLASS_MATH);
        IdentificationNumber nonExistentStudentId = new IdentificationNumber("S9999999");
        UnenrolCommand unenrolCommand = new UnenrolCommand(nonExistentStudentId, className);

        assertCommandFailure(unenrolCommand, model, UnenrolCommand.MESSAGE_STUDENT_NOT_FOUND);
    }

    @Test
    public void equals() {
        final ClassName mathClass = new ClassName(VALID_CLASS_MATH);
        final ClassName scienceClass = new ClassName("B2b");
        final IdentificationNumber amyId = AMY.getId();
        final IdentificationNumber bobId = BOB.getId();

        final UnenrolCommand standardCommand = new UnenrolCommand(amyId, mathClass);

        // same values -> returns true
        UnenrolCommand commandWithSameValues = new UnenrolCommand(amyId, mathClass);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different class name -> returns false
        assertFalse(standardCommand.equals(new UnenrolCommand(amyId, scienceClass)));

        // different student id -> returns false
        assertFalse(standardCommand.equals(new UnenrolCommand(bobId, mathClass)));
    }
}
