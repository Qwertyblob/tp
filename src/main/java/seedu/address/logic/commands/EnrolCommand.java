package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
            + ": Enrols a student into a class using student ID and class name.\n"
            + "Parameters: "
            + PREFIX_ID + "STUDENT_ID "
            + PREFIX_CLASS + "CLASS_NAME\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_ID + "S1234567 "
            + PREFIX_CLASS + "M2a ";

    public static final String MESSAGE_ENROL_SUCCESS = "Enrolled student %1$s to class %2$s";
    public static final String MESSAGE_DUPLICATE_STUDENT = "This student is already enrolled in the class.";
    public static final String MESSAGE_LESSON_NOT_FOUND = "This class does not exist in the address book.";
    public static final String MESSAGE_STUDENT_NOT_FOUND = "This student ID does not exist in the address book.";
    public static final String MESSAGE_PERSON_NOT_STUDENT = "Only students can be enrolled.";
    public static final String MESSAGE_TIMING_CLASH = "This class clashes with one of the student's classes: %1$s";

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
        Optional<Lesson> lessonToEnrolInOptional = model.getAddressBook().getLessonList().stream()
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

        if (!studentToEnrol.getRole().isStudent()) {
            throw new CommandException(MESSAGE_PERSON_NOT_STUDENT);
        }

        Set<IdentificationNumber> newStudentIdSet = new HashSet<>(lessonToEnrolIn.getStudents());

        // HashSet::add returns false if the element is already present.
        if (!newStudentIdSet.add(studentToEnrol.getId())) {
            throw new CommandException(MESSAGE_DUPLICATE_STUDENT);
        }

        for (Lesson existingLesson : studentToEnrol.getLessons()) {
            if (lessonToEnrolIn.hasOverlapsWith(existingLesson)) {
                throw new CommandException(String.format(MESSAGE_TIMING_CLASH,
                        Messages.shortenedFormatLesson(existingLesson)));
            }
        }

        // Create a deep copy of the attendance map with new HashSet copies for each date
        Map<LocalDate, Set<IdentificationNumber>> newAttendanceMap = new HashMap<>();
        lessonToEnrolIn.getAttendance().forEach((date, studentSet) ->
                newAttendanceMap.put(date, new HashSet<>(studentSet)));

        Lesson newLesson = new Lesson(
                lessonToEnrolIn.getClassName(),
                lessonToEnrolIn.getDay(),
                lessonToEnrolIn.getTime(),
                lessonToEnrolIn.getTutor(),
                newStudentIdSet, newAttendanceMap, lessonToEnrolIn.getTags()
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
        String output = String.format(MESSAGE_ENROL_SUCCESS,
                Messages.shortenedFormatPerson(studentToEnrol), Messages.shortenedFormatLesson(newLesson));
        model.commitAddressBook(output, CommandResult.DisplayType.DEFAULT);

        return new CommandResult(output, CommandResult.DisplayType.DEFAULT);
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
