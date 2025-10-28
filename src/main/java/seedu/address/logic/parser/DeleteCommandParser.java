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
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        // Handle optional "-f" flag
        ParseResult result = getRemainingArgs(trimmedArgs);
        boolean isForced = result.isForced;
        String remainingArgs = result.remainingArgs;


        // Check if it's a person name-based delete (starts with n/)
        if (remainingArgs.startsWith("n/")) {
            try {
                Name name = ParserUtil.parseName(remainingArgs.substring(2));
                return new DeleteCommand(name, isForced);
            } catch (ParseException pe) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE), pe);
            }
        }

        // Check if it's an index-based delete (numeric)
        if (remainingArgs.matches("^[0-9]+$")) {
            try {
                Index index = ParserUtil.parseIndex(remainingArgs);
                return new DeleteCommand(index, isForced);
            } catch (ParseException pe) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE), pe);
            }
        }

        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    /**
     * Extracts the "-f" flag (if present) and returns the remaining arguments.
     */
    private static ParseResult getRemainingArgs(String trimmedArgs) throws ParseException {
        // Split arguments by whitespace
        String[] parts = trimmedArgs.split("\\s+", 2);
        boolean isForced = false;
        String remainingArgs;

        if (parts[0].equals("-f")) {
            isForced = true;

            if (parts.length < 2) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
            }
            remainingArgs = parts[1].trim();
        } else {
            remainingArgs = trimmedArgs;
        }

        return new ParseResult(isForced, remainingArgs);
    }

    private static class ParseResult {
        final boolean isForced;
        final String remainingArgs;

        ParseResult(boolean isForced, String remainingArgs) {
            this.isForced = isForced;
            this.remainingArgs = remainingArgs;
        }
    }
}
