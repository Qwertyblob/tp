package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.CLASS_DESC_MATH;
import static seedu.address.logic.commands.CommandTestUtil.STUDENT_ID_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_CLASS_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_STUDENT_ID_DESC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CLASS_MATH;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ID_AMY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.UnmarkCommand;
import seedu.address.model.lesson.ClassName;
import seedu.address.model.person.IdentificationNumber;

public class UnmarkCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE);

    private UnmarkCommandParser parser = new UnmarkCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no student ID specified
        assertParseFailure(parser, CLASS_DESC_MATH, MESSAGE_INVALID_FORMAT);

        // no class name specified
        assertParseFailure(parser, STUDENT_ID_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // no student ID and no class name specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "some random string" + STUDENT_ID_DESC_AMY + CLASS_DESC_MATH,
                MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "i/ string" + STUDENT_ID_DESC_AMY + CLASS_DESC_MATH,
                MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid student ID
        assertParseFailure(parser, INVALID_STUDENT_ID_DESC + CLASS_DESC_MATH,
                IdentificationNumber.MESSAGE_CONSTRAINTS);

        // invalid class name
        assertParseFailure(parser, STUDENT_ID_DESC_AMY + INVALID_CLASS_NAME_DESC,
                ClassName.MESSAGE_CONSTRAINTS);

        // both invalid values, but only the first invalid value is captured
        assertParseFailure(parser, INVALID_STUDENT_ID_DESC + INVALID_CLASS_NAME_DESC,
                IdentificationNumber.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsPresent_success() {
        // with date
        String userInput = STUDENT_ID_DESC_AMY + CLASS_DESC_MATH + " " + PREFIX_DATE + "2023-10-28";
        UnmarkCommand expectedCommand = new UnmarkCommand(new IdentificationNumber(VALID_ID_AMY),
                new ClassName(VALID_CLASS_MATH), Optional.of(LocalDate.of(2023, 10, 28)));
        assertParseSuccess(parser, userInput, expectedCommand);

        // without date
        userInput = STUDENT_ID_DESC_AMY + CLASS_DESC_MATH;
        expectedCommand = new UnmarkCommand(new IdentificationNumber(VALID_ID_AMY),
                new ClassName(VALID_CLASS_MATH), Optional.empty());
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_duplicatePrefixes_failure() {
        String userInput = STUDENT_ID_DESC_AMY + STUDENT_ID_DESC_AMY + CLASS_DESC_MATH;
        assertParseFailure(parser, userInput, "Multiple values specified for the following single-valued field(s): " + PREFIX_ID);
    }

}