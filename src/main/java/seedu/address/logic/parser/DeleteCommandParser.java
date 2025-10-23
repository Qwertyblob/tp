package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Name;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        // Check if it's a person name-based delete (starts with n/)
        if (trimmedArgs.startsWith("n/")) {
            try {
                Name name = ParserUtil.parseName(trimmedArgs.substring(2));
                return new DeleteCommand(name);
            } catch (ParseException pe) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE), pe);
            }
        }

        // Check if it's an index-based delete (numeric)
        if (trimmedArgs.matches("^[0-9]+$")) {
            try {
                Index index = ParserUtil.parseIndex(trimmedArgs);
                return new DeleteCommand(index);
            } catch (ParseException pe) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE), pe);
            }
        }

        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }
}
