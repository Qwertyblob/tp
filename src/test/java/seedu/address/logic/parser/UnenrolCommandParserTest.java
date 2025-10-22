package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.CLASS_DESC_MATH;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CLASS_MATH;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ID_AMY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.UnenrolCommand;
import seedu.address.model.lesson.ClassName;
import seedu.address.model.person.IdentificationNumber;

public class UnenrolCommandParserTest {

    private final UnenrolCommandParser parser = new UnenrolCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        ClassName expectedClassName = new ClassName(VALID_CLASS_MATH);
        IdentificationNumber expectedStudentId = new IdentificationNumber(VALID_ID_AMY);

        // Standard case
        assertParseSuccess(parser, CLASS_DESC_MATH + " " + PREFIX_ID + VALID_ID_AMY,
                new UnenrolCommand(expectedStudentId, expectedClassName));

        // With whitespace preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + CLASS_DESC_MATH + " " + PREFIX_ID + VALID_ID_AMY,
                new UnenrolCommand(expectedStudentId, expectedClassName));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnenrolCommand.MESSAGE_USAGE);

        // Missing class prefix
        assertParseFailure(parser, VALID_CLASS_MATH + " " + PREFIX_ID + VALID_ID_AMY, expectedMessage);

        // Missing student ID prefix
        assertParseFailure(parser, CLASS_DESC_MATH + " " + VALID_ID_AMY, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // Invalid class name format
        assertParseFailure(parser, " " + PREFIX_CLASS + "Math101" + " " + PREFIX_ID + VALID_ID_AMY,
                ClassName.MESSAGE_CONSTRAINTS);

        // Invalid student ID format
        assertParseFailure(parser, CLASS_DESC_MATH + " " + PREFIX_ID + "A1234567",
                IdentificationNumber.MESSAGE_CONSTRAINTS);
    }
}
