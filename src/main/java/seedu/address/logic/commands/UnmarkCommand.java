package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.lesson.ClassName;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.IdentificationNumber;
import seedu.address.model.person.Person;
import seedu.address.model.util.LessonCascadeUpdater;

/**
 * Unmarks a student's attendance for a specific class on the current day.
 */
public class UnmarkCommand extends Command {

    public static final String COMMAND_WORD = "unmark";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Unmarks a student's attendance for a class on the current day.\n"
            + "Parameters: "
            + PREFIX_ID + "STUDENT_ID "
            + PREFIX_CLASS + "CLASS_NAME "
            + "[" + PREFIX_DATE + "DATE]\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_ID + "S0000001 "
            + PREFIX_CLASS + "M2a "
            + PREFIX_DATE + "2023-10-28";

    public static final String MESSAGE_SUCCESS = "Unmarked %1$s as present for class %2$s on %3$s.";
    public static final String MESSAGE_LESSON_NOT_FOUND = "Class %1$s not found.";
    public static final String MESSAGE_PERSON_NOT_FOUND = "Student %1$s not found.";
    public static final String MESSAGE_NOT_MARKED = "%1$s has not been marked present for %2$s on %3$s.";
    public static final String MESSAGE_STUDENT_NOT_ENROLLED = "This student is not enrolled in this class.";

    private final IdentificationNumber studentId;
    private final ClassName className;
    private final Optional<LocalDate> date;

    /**
     * @param studentId of the student to be unmarked.
     * @param className of the target class.
     */
    public UnmarkCommand(IdentificationNumber studentId, ClassName className, Optional<LocalDate> date) {
        this.studentId = requireNonNull(studentId);
        this.className = requireNonNull(className);
        this.date = requireNonNull(date);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Optional<Lesson> lessonOptional = model.getAddressBook().getLessonList().stream()
                .filter(l -> l.getClassName().toString().equalsIgnoreCase(className.toString()))
                .findFirst();

        if (lessonOptional.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_LESSON_NOT_FOUND, className));
        }
        
        Lesson lessonToUnmark = lessonOptional.get();

        Optional<Person> studentToUnmarkOptional = model.getAddressBook().getPersonList().stream()
                .filter(p -> p.getId().equals(studentId))
                .findFirst();

        if (studentToUnmarkOptional.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_PERSON_NOT_FOUND, studentId));
        }
        Person studentToUnmark = studentToUnmarkOptional.get();

        if (!lessonToUnmark.getStudents().contains(studentId)) {
            throw new CommandException(MESSAGE_STUDENT_NOT_ENROLLED);
        }

        LocalDate attendanceDate = date.orElse(LocalDate.now());
        // Create a deep copy of the attendance map with new HashSet copies for each date
        Map<LocalDate, Set<IdentificationNumber>> newAttendanceMap = new HashMap<>();
        lessonToUnmark.getAttendance().forEach((date, studentSet) ->
                newAttendanceMap.put(date, new HashSet<>(studentSet)));

        Set<IdentificationNumber> presentStudentsToday = newAttendanceMap.getOrDefault(attendanceDate, new HashSet<>());

        if (!presentStudentsToday.remove(studentId)) {
            throw new CommandException(String.format(MESSAGE_NOT_MARKED,
                    studentToUnmark.getName().fullName,
                    className.fullClassName,
                    attendanceDate));
        }

        newAttendanceMap.put(attendanceDate, presentStudentsToday);

        Lesson updatedLesson = new Lesson(
                lessonToUnmark.getClassName(),
                lessonToUnmark.getDay(),
                lessonToUnmark.getTime(),
                lessonToUnmark.getTutor(),
                lessonToUnmark.getStudents(), newAttendanceMap, lessonToUnmark.getTags()
        );

        model.setLesson(lessonToUnmark, updatedLesson);

        LessonCascadeUpdater.updateStudentsWithEditedLesson(model, lessonToUnmark, updatedLesson);

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // Update AddressBook state pointer
        String output = String.format(MESSAGE_SUCCESS,
                studentToUnmark.getName().fullName, className.fullClassName, attendanceDate);
        model.commitAddressBook(output, CommandResult.DisplayType.DEFAULT);
        return new CommandResult(output, CommandResult.DisplayType.DEFAULT);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UnmarkCommand)) {
            return false;
        }
        UnmarkCommand otherCommand = (UnmarkCommand) other;
        return className.equals(otherCommand.className)
                && studentId.equals(otherCommand.studentId)
                && date.equals(otherCommand.date);
    }

}
