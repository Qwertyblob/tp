package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.Optional;

import seedu.address.model.Model;
import seedu.address.model.lesson.ClassName;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.Name;

public class MarkCommand extends Command {

    public static final String COMMAND_WORD = "mark";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks a student's attendance for a class. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_CLASS + "CLASS "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Alice Tan"
            + PREFIX_CLASS + "M2a";

    public static final String MESSAGE_SUCCESS = "Marked %1$s as present for class %2$s.";
    public static final String MESSAGE_LESSON_NOT_FOUND = "Class %1$s not found.";
    public static final String MESSAGE_PERSON_NOT_FOUND = "Student %1$s not found.";
    public static final String MESSAGE_ALREADY_MARKED = "%1$s has already been marked present for %2$s.";

    private final Name studentName;
    private final ClassName className;

    public MarkCommand(Name studentName, ClassName className) {
        this.studentName = requireNonNull(studentName);
        this.className = requireNonNull(className);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        Optional<Lesson> lessonOpt = model.getFilteredLessonList().stream()
                .filter(l -> l.getClassName().toString().equalsIgnoreCase(className))
                .findFirst();

        if (lessonOpt.isEmpty()) {
            return new CommandResult(String.format(MESSAGE_LESSON_NOT_FOUND, className));
        }

        Lesson lesson = lessonOpt.get();

        // Check if student exists
        boolean studentExists = model.getFilteredPersonList().stream()
                .anyMatch(p -> p.getName().equals(studentName));

        if (!studentExists) {
            return new CommandResult(String.format(MESSAGE_PERSON_NOT_FOUND, studentName));
        }

        if (lesson.hasStudent(studentName)) {
            return new CommandResult(String.format(MESSAGE_ALREADY_MARKED, studentName, className));
        }

        Lesson updatedLesson = lesson.markAttendance(studentName);
        model.setLesson(lesson, updatedLesson);

        return new CommandResult(String.format(MESSAGE_SUCCESS, studentName, className));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof MarkCommand
                && className.equals(((MarkCommand) other).className)
                && studentName.equals(((MarkCommand) other).studentName));
    }
}

