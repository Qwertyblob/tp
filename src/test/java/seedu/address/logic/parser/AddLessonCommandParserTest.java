package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.CLASS_DESC_MATH;
import static seedu.address.logic.commands.CommandTestUtil.DAY_DESC_MONDAY;
import static seedu.address.logic.commands.CommandTestUtil.TIME_DESC_1200;
import static seedu.address.logic.commands.CommandTestUtil.TUTOR_DESC_A12345678;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CLASS_MATH;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DAY_MONDAY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TIME_1200;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TUTOR_A12345678;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTOR;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddLessonCommand;
import seedu.address.model.lesson.ClassName;
import seedu.address.model.lesson.Day;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.lesson.Time;
import seedu.address.model.lesson.Tutor;
import seedu.address.testutil.LessonBuilder;

public class AddLessonCommandParserTest {
    private AddLessonCommandParser parser = new AddLessonCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Lesson expectedLesson = new LessonBuilder()
                .withClassName(VALID_CLASS_MATH)
                .withDay(VALID_DAY_MONDAY)
                .withTime(VALID_TIME_1200)
                .withTutor(VALID_TUTOR_A12345678)
                .build();

        // whitespace only preamble
        assertParseSuccess(parser, " " + CLASS_DESC_MATH + DAY_DESC_MONDAY + TIME_DESC_1200
                        + TUTOR_DESC_A12345678, new AddLessonCommand(expectedLesson));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedLessonString = CLASS_DESC_MATH + DAY_DESC_MONDAY + TIME_DESC_1200 + TUTOR_DESC_A12345678;

        // multiple class names
        assertParseFailure(parser, CLASS_DESC_MATH + validExpectedLessonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_CLASS));

        // multiple days
        assertParseFailure(parser, DAY_DESC_MONDAY + validExpectedLessonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DAY));

        // multiple times
        assertParseFailure(parser, TIME_DESC_1200 + validExpectedLessonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_TIME));

        // multiple tutors
        assertParseFailure(parser, TUTOR_DESC_A12345678 + validExpectedLessonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_TUTOR));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedLessonString + CLASS_DESC_MATH + DAY_DESC_MONDAY + TIME_DESC_1200 + TUTOR_DESC_A12345678,
                Messages.getErrorMessageForDuplicatePrefixes(
                        PREFIX_CLASS, PREFIX_DAY, PREFIX_TIME, PREFIX_TUTOR));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddLessonCommand.MESSAGE_USAGE);

        // missing class prefix
        assertParseFailure(parser, VALID_CLASS_MATH + DAY_DESC_MONDAY + TIME_DESC_1200 + TUTOR_DESC_A12345678,
                expectedMessage);

        // missing day prefix
        assertParseFailure(parser, CLASS_DESC_MATH + VALID_DAY_MONDAY + TIME_DESC_1200 + TUTOR_DESC_A12345678,
                expectedMessage);

        // missing time prefix
        assertParseFailure(parser, CLASS_DESC_MATH + DAY_DESC_MONDAY + VALID_TIME_1200 + TUTOR_DESC_A12345678,
                expectedMessage);

        // missing tutor prefix
        assertParseFailure(parser, CLASS_DESC_MATH + DAY_DESC_MONDAY + TIME_DESC_1200 + VALID_TUTOR_A12345678,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_CLASS_MATH + VALID_DAY_MONDAY + VALID_TIME_1200 + VALID_TUTOR_A12345678,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid class name
        assertParseFailure(parser, " c/invalid" + DAY_DESC_MONDAY + TIME_DESC_1200 + TUTOR_DESC_A12345678,
                ClassName.MESSAGE_CONSTRAINTS);

        // invalid day
        assertParseFailure(parser, CLASS_DESC_MATH + " d/invalid" + TIME_DESC_1200 + TUTOR_DESC_A12345678,
                Day.MESSAGE_CONSTRAINTS);

        // invalid time
        assertParseFailure(parser, CLASS_DESC_MATH + DAY_DESC_MONDAY + " t/invalid" + TUTOR_DESC_A12345678,
                Time.MESSAGE_CONSTRAINTS);

        // invalid tutor
        assertParseFailure(parser, CLASS_DESC_MATH + DAY_DESC_MONDAY + TIME_DESC_1200 + " u/invalid",
                Tutor.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, " c/invalid" + DAY_DESC_MONDAY + " t/invalid" + TUTOR_DESC_A12345678,
                ClassName.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, " preamble" + CLASS_DESC_MATH + DAY_DESC_MONDAY + TIME_DESC_1200
                + TUTOR_DESC_A12345678, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddLessonCommand.MESSAGE_USAGE));
    }
}
