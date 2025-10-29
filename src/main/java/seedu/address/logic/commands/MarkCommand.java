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
 * Marks a student to be present in the specified class.
 */
public class MarkCommand extends Command {

    public static final String COMMAND_WORD = "mark";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks a student's attendance for a class on the current day.\n"
            + "Parameters: "
            + PREFIX_ID + "STUDENT_ID "
            + PREFIX_CLASS + "CLASS\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_ID + "S0000001"
            + PREFIX_CLASS + "M2a";

    public static final String MESSAGE_SUCCESS = "Marked %1$s as present for class %2$s.";
    public static final String MESSAGE_LESSON_NOT_FOUND = "Class %1$s not found.";
    public static final String MESSAGE_PERSON_NOT_FOUND = "Student %1$s not found.";
    public static final String MESSAGE_ALREADY_MARKED = "%1$s has already been marked present for %2$s.";
    public static final String MESSAGE_STUDENT_NOT_ENROLLED = "This student is not enrolled in this lesson.";

    private final IdentificationNumber studentId;
    private final ClassName className;

    /**
     * @param studentId of the student to be arked present.
     * @param className of the target student.
     */
    public MarkCommand(IdentificationNumber studentId, ClassName className) {
        this.studentId = requireNonNull(studentId);
        this.className = requireNonNull(className);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Optional<Lesson> lessonOptional = model.getFilteredLessonList().stream()
                .filter(l -> l.getClassName().toString().equalsIgnoreCase(className.toString()))
                .findFirst();

        if (lessonOptional.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_LESSON_NOT_FOUND, className.toString()));
        }

        Lesson lessonToMark = lessonOptional.get();

        Optional<Person> studentToMarkOptional = model.getFilteredPersonList().stream()
                .filter(p -> p.getId().equals(studentId))
                .findFirst();

        if (studentToMarkOptional.isEmpty()) {
            throw new CommandException(MESSAGE_PERSON_NOT_FOUND);
        }
        Person studentToMark = studentToMarkOptional.get();

        // Check if the student is enrolled in the lesson
        if (!lessonToMark.getStudents().contains(studentId)) {
            throw new CommandException(MESSAGE_STUDENT_NOT_ENROLLED);
        }

        // --- Logic for HashMap approach ---
        LocalDate today = LocalDate.now();
        Map<LocalDate, Set<IdentificationNumber>> newAttendanceMap = new HashMap<>(lessonToMark.getAttendance());

        // Get the set of present students for today, or create a new set if it's the first record for today.
        Set<IdentificationNumber> presentStudentsToday = newAttendanceMap.getOrDefault(today, new HashSet<>());

        // Check for duplicates and add the student if not present
        if (!presentStudentsToday.add(studentId)) {
            throw new CommandException(String.format(MESSAGE_ALREADY_MARKED,
                    Messages.shortenedFormatPerson(studentToMark), Messages.shortenedFormatLesson(lessonToMark)));
        }

        // Update the map with the modified set for today's date
        newAttendanceMap.put(today, presentStudentsToday);

        // Create a new lesson with the updated attendance map
        Lesson updatedLesson = new Lesson(
                lessonToMark.getClassName(),
                lessonToMark.getDay(),
                lessonToMark.getTime(),
                lessonToMark.getTutor(),
                lessonToMark.getStudents(), newAttendanceMap, lessonToMark.getTags()
        );

        model.setLesson(lessonToMark, updatedLesson);
        // Update AddressBook state pointer
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_SUCCESS,
                Messages.shortenedFormatPerson(studentToMark), Messages.shortenedFormatLesson(lessonToMark)),
                CommandResult.DisplayType.RECENT);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof MarkCommand)) {
            return false;
        }
        MarkCommand otherCommand = (MarkCommand) other;
        return className.equals(otherCommand.className)
                && studentId.equals(otherCommand.studentId);
    }
}

