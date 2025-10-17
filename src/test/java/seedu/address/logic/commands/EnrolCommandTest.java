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
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.lesson.ClassName;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.IdentificationNumber;
import seedu.address.model.person.Person;
import seedu.address.testutil.LessonBuilder;

public class EnrolCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = getTypicalModelManager();
        expectedModel = getTypicalModelManager();
    }

    @Test
    public void execute_validStudentAndLesson_success() {
        ClassName classNameToEnrol = new ClassName(VALID_CLASS_MATH);
        IdentificationNumber studentIdToEnrol = AMY.getId();
        EnrolCommand enrolCommand = new EnrolCommand(studentIdToEnrol, classNameToEnrol);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        Lesson lessonInModel = expectedModel.getFilteredLessonList().stream()
                .filter(l -> l.getClassName().equals(classNameToEnrol)).findFirst().get();

        Set<IdentificationNumber> newStudentIds = Stream.concat(
                lessonInModel.getStudents().stream(),
                Stream.of(studentIdToEnrol)
        ).collect(Collectors.toSet());

        Lesson expectedLesson = new LessonBuilder(lessonInModel)
                .withStudents(newStudentIds.stream()
                .map(IdentificationNumber::toString).toArray(String[]::new)).build();
        expectedModel.setLesson(lessonInModel, expectedLesson);

        // Update the person's lessons as well
        Set<Lesson> newLessons = Stream.concat(
                AMY.getLessons().stream(),
                Stream.of(expectedLesson)
        ).collect(Collectors.toSet());
        Person expectedPerson = new Person(AMY.getId(), AMY.getName(), AMY.getRole(),
                newLessons, AMY.getPhone(), AMY.getEmail(), AMY.getAddress(), AMY.getTags());
        expectedModel.setPerson(AMY, expectedPerson);

        String expectedMessage = String.format(EnrolCommand.MESSAGE_ENROL_SUCCESS,
                Messages.formatPerson(AMY), Messages.formatLesson(expectedLesson));

        assertCommandSuccess(enrolCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateStudent_throwsCommandException() {
        ClassName className = new ClassName(VALID_CLASS_MATH);
        IdentificationNumber studentId = AMY.getId();
        EnrolCommand initialEnrolCommand = new EnrolCommand(studentId, className);

        Model tempModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        try {
            initialEnrolCommand.execute(tempModel);
        } catch (Exception e) {
            // Should not fail
        }

        EnrolCommand duplicateEnrolCommand = new EnrolCommand(studentId, className);
        assertCommandFailure(duplicateEnrolCommand, tempModel, EnrolCommand.MESSAGE_DUPLICATE_STUDENT);
    }

    @Test
    public void execute_lessonNotFound_throwsCommandException() {
        ClassName nonExistentClassName = new ClassName("Z9z");
        EnrolCommand enrolCommand = new EnrolCommand(AMY.getId(), nonExistentClassName);

        assertCommandFailure(enrolCommand, model, EnrolCommand.MESSAGE_LESSON_NOT_FOUND);
    }

    @Test
    public void execute_studentNotFound_throwsCommandException() {
        ClassName className = new ClassName(VALID_CLASS_MATH);
        IdentificationNumber nonExistentStudentId = new IdentificationNumber("S9999999");
        EnrolCommand enrolCommand = new EnrolCommand(nonExistentStudentId, className);

        assertCommandFailure(enrolCommand, model, EnrolCommand.MESSAGE_STUDENT_NOT_FOUND);
    }

    @Test
    public void equals() {
        final ClassName mathClass = new ClassName(VALID_CLASS_MATH);
        final ClassName scienceClass = new ClassName("B2b"); // Assuming a second class
        final IdentificationNumber amyId = AMY.getId();
        final IdentificationNumber bobId = BOB.getId();

        final EnrolCommand standardCommand = new EnrolCommand(amyId, mathClass);

        // same values -> returns true
        EnrolCommand commandWithSameValues = new EnrolCommand(amyId, mathClass);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different class name -> returns false
        assertFalse(standardCommand.equals(new EnrolCommand(amyId, scienceClass)));

        // different student id -> returns false
        assertFalse(standardCommand.equals(new EnrolCommand(bobId, mathClass)));
    }

}
