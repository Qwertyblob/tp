package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.lesson.ClassName;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.IdentificationNumber;
import seedu.address.model.person.Person;

/**
 * Enrols a student to a lesson using their ID and the lesson's class name.
 */
public class EnrolCommand extends Command {

    public static final String COMMAND_WORD = "enrol";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Enrols a student into a lesson using student ID and class name.\n"
            + "Parameters: "
            + PREFIX_ID + "STUDENT_ID "
            + PREFIX_CLASS + "CLASS_NAME\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_ID + "S1234567"
            + PREFIX_CLASS + "M2a ";

    public static final String MESSAGE_ENROL_SUCCESS = "Enrolled student %1$s to lesson %2$s";
    public static final String MESSAGE_DUPLICATE_STUDENT = "This student is already enrolled in the lesson.";
    public static final String MESSAGE_LESSON_NOT_FOUND = "This lesson does not exist in the address book.";
    public static final String MESSAGE_STUDENT_NOT_FOUND = "This student ID does not exist in the address book.";

    private final IdentificationNumber studentId;
    private final ClassName className;

    /**
     * @param className of the lesson to enrol the student to
     * @param studentId of the student to be enrolled
     */
    public EnrolCommand(IdentificationNumber studentId, ClassName className) {
        requireNonNull(studentId);
        requireNonNull(className);
        this.studentId = studentId;
        this.className = className;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Find the lesson by ClassName from the full lesson list
        Optional<Lesson> lessonToEnrolInOptional = model.getFilteredLessonList().stream()
                .filter(lesson -> lesson.getClassName().equals(className))
                .findFirst();

        if (lessonToEnrolInOptional.isEmpty()) {
            throw new CommandException(MESSAGE_LESSON_NOT_FOUND);
        }
        Lesson lessonToEnrolIn = lessonToEnrolInOptional.get();

        // Find the student by IdentificationNumber from the full person list
        Optional<Person> studentToEnrolOptional = model.getAddressBook().getPersonList().stream()
                .filter(person -> person.getId().equals(studentId))
                .findFirst();

        if (studentToEnrolOptional.isEmpty()) {
            throw new CommandException(MESSAGE_STUDENT_NOT_FOUND);
        }
        Person studentToEnrol = studentToEnrolOptional.get();

        Set<IdentificationNumber> newStudentIdSet = new HashSet<>(lessonToEnrolIn.getStudents());

        // HashSet::add returns false if the element is already present.
        if (!newStudentIdSet.add(studentToEnrol.getId())) {
            throw new CommandException(MESSAGE_DUPLICATE_STUDENT);
        }

        Lesson newLesson = new Lesson(
                lessonToEnrolIn.getClassName(),
                lessonToEnrolIn.getDay(),
                lessonToEnrolIn.getTime(),
                lessonToEnrolIn.getTutor(),
                newStudentIdSet, lessonToEnrolIn.getAttendance(), lessonToEnrolIn.getTags()
        );

        model.setLesson(lessonToEnrolIn, newLesson);

        Set<Lesson> newLessonSet = new HashSet<>(studentToEnrol.getLessons());
        newLessonSet.add(newLesson);
        Person enrolledStudent = new Person(
                studentToEnrol.getId(),
                studentToEnrol.getName(),
                studentToEnrol.getRole(),
                newLessonSet,
                studentToEnrol.getPhone(),
                studentToEnrol.getEmail(),
                studentToEnrol.getAddress(),
                studentToEnrol.getTags()
        );

        model.setPerson(studentToEnrol, enrolledStudent);
        // Update AddressBook state pointer
        model.commitAddressBook();

        return new CommandResult(String.format(MESSAGE_ENROL_SUCCESS,
                Messages.shortenedFormatPerson(studentToEnrol), Messages.shortenedFormatLesson(newLesson)),
                CommandResult.DisplayType.DEFAULT);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EnrolCommand)) {
            return false;
        }

        EnrolCommand otherEnrolCommand = (EnrolCommand) other;
        return className.equals(otherEnrolCommand.className)
                && studentId.equals(otherEnrolCommand.studentId);
    }

}
