package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindLessonCommand;
import seedu.address.model.lesson.LessonMatchesPredicate;

/**
 * Unit tests for {@code FindLessonCommandParser}.
 */
public class FindLessonCommandParserTest {

    private final FindLessonCommandParser parser = new FindLessonCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindLessonCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindLessonCommand() {
        // no leading and trailing whitespaces
        FindLessonCommand expectedCommand = new FindLessonCommand(
                new LessonMatchesPredicate("A1a", "Monday", "1000", "T1234567", "Math"));

        assertParseSuccess(parser,
                " c/A1a d/Monday tm/1000 tt/T1234567 t/Math",
                expectedCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser,
                " \n c/A1a \n d/Monday \t tm/1000 \n tt/T1234567 \t t/Math",
                expectedCommand);
    }

    @Test
    public void parse_invalidTutorFormat_throwsParseException() {
        assertParseFailure(parser,
                " tt/Alice",
                "Tutors should only contain alphanumeric characters, and it should not be blank");
    }
}
