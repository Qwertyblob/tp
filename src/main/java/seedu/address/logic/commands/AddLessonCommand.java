package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTOR;

import java.util.Optional;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.Person;

/**
 * Adds a Lesson to the address book.
 */
public class AddLessonCommand extends Command {
    public static final String COMMAND_WORD = "addc";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a class to the address book. "
            + "Parameters: "
            + PREFIX_CLASS + "CLASS_NAME "
            + PREFIX_DAY + "DAY "
            + PREFIX_TIME + "TIME "
            + PREFIX_TUTOR + "TUTOR "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_CLASS + "M2a "
            + PREFIX_DAY + "Monday "
            + PREFIX_TIME + "1200-1400 "
            + PREFIX_TUTOR + "T1234567";

    public static final String MESSAGE_SUCCESS = "New class added: %1$s";
    public static final String MESSAGE_DUPLICATE_CLASS = "This class already exists in the address book";
    public static final String MESSAGE_TUTOR_NOT_FOUND = "This tutor ID does not exist in the address book.";
    public static final String MESSAGE_TUTOR_TIME_CLASH =
            "This tutor already has a class that overlaps with the specified time.";

    private final Lesson toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddLessonCommand(Lesson lesson) {
        requireNonNull(lesson);
        toAdd = lesson;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasLesson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_CLASS);
        }

        // Find the tutor by IdentificationNumber from the full person list
        Optional<Person> tutorOfLessonOptional = model.getAddressBook().getPersonList().stream()
                .filter(person -> person.getId().toString().equals(toAdd.getTutor().tutorName))
                .findFirst();

        if (tutorOfLessonOptional.isEmpty()) {
            throw new CommandException(MESSAGE_TUTOR_NOT_FOUND);
        }

        boolean hasClash = model.getAddressBook().getLessonList().stream()
                .filter(existingLesson -> existingLesson.getTutor().equals(toAdd.getTutor()))
                .anyMatch(existingLesson -> existingLesson.hasOverlapsWith(toAdd));

        if (hasClash) {
            throw new CommandException(MESSAGE_TUTOR_TIME_CLASH);
        }

        model.addLesson(toAdd);
        // Update AddressBook state pointer
        String output = String.format(MESSAGE_SUCCESS, Messages.formatLesson(toAdd));
        model.commitAddressBook(output, CommandResult.DisplayType.CLASS_LIST);
        return new CommandResult(output, CommandResult.DisplayType.CLASS_LIST);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddLessonCommand)) {
            return false;
        }

        AddLessonCommand otherAddLessonCommand = (AddLessonCommand) other;
        return toAdd.equals(otherAddLessonCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
