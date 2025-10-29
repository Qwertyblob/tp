package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_LESSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_LESSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_LESSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteLessonCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * White-box tests for DeleteLessonCommandParser.
 * Mirrors DeleteCommandParserTest for consistency.
 */
public class DeleteLessonCommandParserTest {

    private final DeleteLessonCommandParser parser = new DeleteLessonCommandParser();

    @Test
    public void parse_validIndexArgs_returnsDeleteLessonCommand() {
        // Single-digit index
        assertParseSuccess(parser, "1", new DeleteLessonCommand(INDEX_FIRST_LESSON));

        // Multi-digit index
        assertParseSuccess(parser, "2", new DeleteLessonCommand(INDEX_SECOND_LESSON));
        assertParseSuccess(parser, "3", new DeleteLessonCommand(INDEX_THIRD_LESSON));

        // Large index
        assertParseSuccess(parser, "999", new DeleteLessonCommand(Index.fromOneBased(999)));
    }

    @Test
    public void parse_validClassNameArgs_returnsDeleteLessonCommand() throws ParseException {
        // Test valid class names
        DeleteLessonCommand result1 = parser.parse("c/M1a");
        assertNotNull(result1);

        DeleteLessonCommand result2 = parser.parse("c/S2b");
        assertNotNull(result2);

        DeleteLessonCommand result3 = parser.parse("c/C3c");
        assertNotNull(result3);
    }

    @Test
    public void parse_forcedDeleteByIndex_returnsDeleteLessonCommandWithForcedTrue() {
        assertParseSuccess(parser, "-f 1", new DeleteLessonCommand(INDEX_FIRST_LESSON, true));
        assertParseSuccess(parser, "-f 2", new DeleteLessonCommand(INDEX_SECOND_LESSON, true));
    }

    @Test
    public void parse_forcedFlagWithoutArgument_throwsParseException() {
        assertParseFailure(parser, "-f",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteLessonCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "-f   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteLessonCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidClassNameArgs_throwsParseException() {
        // Empty class name
        assertParseFailure(parser, "c/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteLessonCommand.MESSAGE_USAGE));

        // Only spaces
        assertParseFailure(parser, "c/   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteLessonCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidIndexArgs_throwsParseException() {
        // 0 index
        assertParseFailure(parser, "0",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteLessonCommand.MESSAGE_USAGE));

        // Negative index
        assertParseFailure(parser, "-1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteLessonCommand.MESSAGE_USAGE));

        // Non-integer / overflow
        assertParseFailure(parser, "999999999999999999999",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteLessonCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // Non-numeric input
        assertParseFailure(parser, "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteLessonCommand.MESSAGE_USAGE));

        // Mixed alphanumeric
        assertParseFailure(parser, "1a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteLessonCommand.MESSAGE_USAGE));

        // Decimal number
        assertParseFailure(parser, "1.5",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteLessonCommand.MESSAGE_USAGE));

        // Empty string
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteLessonCommand.MESSAGE_USAGE));

        // Whitespace only
        assertParseFailure(parser, "   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteLessonCommand.MESSAGE_USAGE));

        // Missing prefix
        assertParseFailure(parser, "M1a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteLessonCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_edgeCases_throwsParseException() {
        // Very large number
        assertParseFailure(parser, "999999999999999999999",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteLessonCommand.MESSAGE_USAGE));

        // Class name with invalid symbols
        assertParseFailure(parser, "c/$$$",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteLessonCommand.MESSAGE_USAGE));
    }
}
