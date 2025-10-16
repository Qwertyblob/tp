package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.CLASS_DESC_MATH;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_CLASS_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.STUDENT_ID_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CLASS_MATH;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ID_AMY;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.MarkCommand;
import seedu.address.model.lesson.ClassName;
import seedu.address.model.person.IdentificationNumber;

public class MarkCommandParserTest {

    private final MarkCommandParser parser = new MarkCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        ClassName expectedClassName = new ClassName(VALID_CLASS_MATH);
        IdentificationNumber expectedStudentId = new IdentificationNumber(VALID_ID_AMY);

        assertParseSuccess(parser, CLASS_DESC_MATH + STUDENT_ID_DESC_AMY,
                new MarkCommand(expectedStudentId, expectedClassName));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE);

        // Missing class prefix
        assertParseFailure(parser, VALID_CLASS_MATH + STUDENT_ID_DESC_AMY, expectedMessage);

        // Missing student ID prefix
        assertParseFailure(parser, CLASS_DESC_MATH + VALID_ID_AMY, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // Invalid class name
        assertParseFailure(parser, INVALID_CLASS_NAME_DESC + STUDENT_ID_DESC_AMY, ClassName.MESSAGE_CONSTRAINTS);
    }
}
