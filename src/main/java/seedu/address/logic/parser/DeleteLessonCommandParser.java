package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteLessonCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.lesson.ClassName;

/**
 * Parses input arguments and creates a new DeleteLessonCommand object
 */
public class DeleteLessonCommandParser implements Parser<DeleteLessonCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteLessonCommand
     * and returns a DeleteLessonCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteLessonCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        // Check if it's a class name-based delete (starts with c/)
        if (trimmedArgs.startsWith("c/")) {
            try {
                ClassName name = ParserUtil.parseClassName(trimmedArgs.substring(2));
                return new DeleteLessonCommand(name);
            } catch (ParseException pe) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteLessonCommand.MESSAGE_USAGE), pe);
            }
        }

        // Check if it's an index-based delete (numeric)
        if (trimmedArgs.matches("^[0-9]+$")) {
            try {
                Index index = ParserUtil.parseIndex(trimmedArgs);
                return new DeleteLessonCommand(index);
            } catch (ParseException pe) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteLessonCommand.MESSAGE_USAGE), pe);
            }
        }

        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteLessonCommand.MESSAGE_USAGE));
    }
}
