package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTOR;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.lesson.LessonMatchesPredicate;

/**
 * Finds and lists all lessons in address book that matches the given criteria.
 * Keyword matching is case-insensitive.
 */
public class FindLessonCommand extends Command {

    public static final String COMMAND_WORD = "findc";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all classes matching the given criteria. "
            + "All parameters are case-insensitive and it displays them as a list with index numbers.\n"
            + "Parameters: " + "[" + PREFIX_CLASS + "CLASS] "
            + "[" + PREFIX_DAY + "DAY] "
            + "[" + PREFIX_TIME + "TIME] "
            + "[" + PREFIX_TUTOR + "TUTOR] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_DAY + "Monday "
            + PREFIX_TIME + "1200";

    private final LessonMatchesPredicate predicate;

    public FindLessonCommand(LessonMatchesPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredLessonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_LESSONS_LISTED_OVERVIEW, model.getFilteredLessonList().size()),
                CommandResult.DisplayType.CLASS_LIST);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindLessonCommand)) {
            return false;
        }

        FindLessonCommand otherFindLessonCommand = (FindLessonCommand) other;
        return predicate.equals(otherFindLessonCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
