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
 * Unenrols a student from a lesson using their ID and the lesson's class name.
 */
public class UnenrolCommand extends Command {

    public static final String COMMAND_WORD = "unenrol";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes a student from a class using student ID and class name.\n"
            + "Parameters: "
            + PREFIX_ID + "STUDENT_ID "
            + PREFIX_CLASS + "CLASS_NAME\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_ID + "S1234567 "
            + PREFIX_CLASS + "M2a ";

    public static final String MESSAGE_UNENROL_SUCCESS = "Removed student %1$s from class %2$s";
    public static final String MESSAGE_LESSON_NOT_FOUND = "This class does not exist in the address book.";
    public static final String MESSAGE_STUDENT_NOT_FOUND = "This student ID does not exist in the address book.";
    public static final String MESSAGE_STUDENT_NOT_ENROLLED = "This student is not enrolled in the specified class.";

    private final IdentificationNumber studentId;
    private final ClassName className;

    /**
     * @param studentId of the student to be removed
     * @param className of the lesson to remove the student from
     */
    public UnenrolCommand(IdentificationNumber studentId, ClassName className) {
        requireNonNull(studentId);
        requireNonNull(className);
        this.studentId = studentId;
        this.className = className;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Find the lesson by ClassName
        Optional<Lesson> lessonToEditOptional = model.getFilteredLessonList().stream()
                .filter(lesson -> lesson.getClassName().equals(className))
                .findFirst();

        if (lessonToEditOptional.isEmpty()) {
            throw new CommandException(MESSAGE_LESSON_NOT_FOUND);
        }
        Lesson lessonToEdit = lessonToEditOptional.get();

        // Find the student by IdentificationNumber
        Optional<Person> studentOptional = model.getAddressBook().getPersonList().stream()
                .filter(person -> person.getId().equals(studentId))
                .findFirst();

        if (studentOptional.isEmpty()) {
            throw new CommandException(MESSAGE_STUDENT_NOT_FOUND);
        }
        Person student = studentOptional.get();

        Set<IdentificationNumber> newStudentIdSet = new HashSet<>(lessonToEdit.getStudents());

        if (!newStudentIdSet.remove(student.getId())) {
            throw new CommandException(MESSAGE_STUDENT_NOT_ENROLLED);
        }

        Lesson newLesson = new Lesson(
                lessonToEdit.getClassName(),
                lessonToEdit.getDay(),
                lessonToEdit.getTime(),
                lessonToEdit.getTutor(),
                newStudentIdSet,
                lessonToEdit.getAttendance(),
                lessonToEdit.getTags()
        );

        model.setLesson(lessonToEdit, newLesson);

        // Also remove the lesson from the student’s lesson set
        Set<Lesson> newLessonSet = new HashSet<>(student.getLessons());
        newLessonSet.removeIf(l -> l.isSameLesson(lessonToEdit));

        Person updatedStudent = new Person(
                student.getId(),
                student.getName(),
                student.getRole(),
                newLessonSet,
                student.getPhone(),
                student.getEmail(),
                student.getAddress(),
                student.getTags()
        );

        model.setPerson(student, updatedStudent);
        // Update AddressBook state pointer
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_UNENROL_SUCCESS,
                Messages.shortenedFormatPerson(student), Messages.shortenedFormatLesson(newLesson)),
                CommandResult.DisplayType.DEFAULT);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UnenrolCommand)) {
            return false;
        }

        UnenrolCommand otherCommand = (UnenrolCommand) other;
        return className.equals(otherCommand.className)
                && studentId.equals(otherCommand.studentId);
    }
}
