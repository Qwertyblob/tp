package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTOR;

import seedu.address.logic.commands.FindLessonCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.lesson.LessonMatchesPredicate;
import seedu.address.model.lesson.Tutor;

/**
 * Parses input arguments and creates a new FindLessonCommand object
 */
public class FindLessonCommandParser implements Parser<FindLessonCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindLessonCommand
     * and returns a FindLessonCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindLessonCommand parse(String args) throws ParseException {
        requireNonNull(args);
        args = args.replaceAll("\\s+", " ");

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_CLASS, PREFIX_DAY, PREFIX_TIME, PREFIX_TUTOR, PREFIX_TAG);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CLASS, PREFIX_DAY, PREFIX_TIME, PREFIX_TUTOR, PREFIX_TAG);

        String className = argMultimap.getValue(PREFIX_CLASS).orElse("").trim();
        String day = argMultimap.getValue(PREFIX_DAY).orElse("").trim();
        String time = argMultimap.getValue(PREFIX_TIME).orElse("").trim();
        String tutor = argMultimap.getValue(PREFIX_TUTOR).orElse("").trim();
        String tags = argMultimap.getValue(PREFIX_TAG).orElse("").trim();

        if (!tutor.isEmpty() && !tutor.matches(Tutor.VALIDATION_REGEX)) {
            throw new ParseException(Tutor.MESSAGE_CONSTRAINTS);
        }

        if (className.isEmpty() && day.isEmpty() && time.isEmpty() && tutor.isEmpty() && tags.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindLessonCommand.MESSAGE_USAGE));
        }

        LessonMatchesPredicate predicate = new LessonMatchesPredicate(className, day, time, tutor, tags);
        return new FindLessonCommand(predicate);
    }
}
