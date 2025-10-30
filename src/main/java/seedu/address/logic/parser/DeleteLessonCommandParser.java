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
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteLessonCommand.MESSAGE_USAGE));
        }

        // Handle optional "-f" flag
        ParseResult result = getRemainingArgs(trimmedArgs);
        boolean isForced = result.isForced;
        String remainingArgs = result.remainingArgs;

        // Check if it's a class name-based delete (starts with c/)
        if (remainingArgs.startsWith("c/")) {
            return handleDeleteByClassName(remainingArgs, isForced);
        }

        // Check if it's an index-based delete (numeric)
        if (remainingArgs.matches("^[0-9]+$")) {
            return handleDeleteByIndex(remainingArgs, isForced);
        }

        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteLessonCommand.MESSAGE_USAGE));
    }

    /**
     * Extracts the "-f" flag (if present) and returns the remaining arguments.
     */
    private static ParseResult getRemainingArgs(String trimmedArgs) throws ParseException {
        String[] parts = trimmedArgs.split("\\s+", 2);
        boolean isForced = false;
        String remainingArgs;

        if (parts[0].equals("-f")) {
            isForced = true;

            if (parts.length < 2) {
                throw new ParseException(String.format(
                        MESSAGE_INVALID_COMMAND_FORMAT, DeleteLessonCommand.MESSAGE_USAGE));
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

    private DeleteLessonCommand handleDeleteByClassName(String remainingArgs, boolean isForced) throws ParseException {
        try {
            ClassName className = ParserUtil.parseClassName(remainingArgs.substring(2));
            return new DeleteLessonCommand(className, isForced);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteLessonCommand.MESSAGE_USAGE), pe);
        }
    }

    private DeleteLessonCommand handleDeleteByIndex(String remainingArgs, boolean isForced) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(remainingArgs);
            return new DeleteLessonCommand(index, isForced);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteLessonCommand.MESSAGE_USAGE), pe);
        }
    }
}
