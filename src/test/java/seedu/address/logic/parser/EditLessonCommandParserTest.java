package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.getErrorMessageForDuplicatePrefixes;
import static seedu.address.logic.commands.CommandTestUtil.CLASS_DESC_MATH;
import static seedu.address.logic.commands.CommandTestUtil.CLASS_DESC_SCIENCE;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_CLASS_NAME_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_MATH;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_SCIENCE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CLASS_MATH;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_MATH;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_SCIENCE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_LESSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_LESSON;

import org.junit.jupiter.api.Test;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditLessonCommand;
import seedu.address.logic.commands.EditLessonCommand.EditLessonDescriptor;
import seedu.address.model.lesson.ClassName;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EditLessonDescriptorBuilder;

/**
 * Unit tests for {@code EditLessonCommandParser}.
 */
public class EditLessonCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;
    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditLessonCommand.MESSAGE_USAGE);

    private final EditLessonCommandParser parser = new EditLessonCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_CLASS_MATH, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditLessonCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + CLASS_DESC_MATH, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + CLASS_DESC_MATH, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1 c/" + INVALID_CLASS_NAME_FORMAT, ClassName.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS);

        // while parsing PREFIX_TAG alone will reset the tags of the lesson,
        // parsing it together with a valid tag results in error
        assertParseFailure(parser, "1" + TAG_DESC_MATH + TAG_DESC_SCIENCE + TAG_EMPTY,
                Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_DESC_MATH + TAG_EMPTY + TAG_DESC_SCIENCE,
                Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_EMPTY + TAG_DESC_MATH + TAG_DESC_SCIENCE,
                Tag.MESSAGE_CONSTRAINTS);

        // multiple invalid values, only first invalid is reported
        assertParseFailure(parser, "1 c/" + INVALID_CLASS_NAME_FORMAT + INVALID_TAG_DESC,
                ClassName.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_LESSON;
        String userInput = targetIndex.getOneBased() + CLASS_DESC_MATH + TAG_DESC_MATH + TAG_DESC_SCIENCE;

        EditLessonDescriptor descriptor = new EditLessonDescriptorBuilder()
                .withClassName(VALID_CLASS_MATH)
                .withTags(VALID_TAG_MATH, VALID_TAG_SCIENCE)
                .build();
        EditLessonCommand expectedCommand = new EditLessonCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_LESSON;
        String userInput = targetIndex.getOneBased() + TAG_DESC_MATH;

        EditLessonDescriptor descriptor = new EditLessonDescriptorBuilder()
                .withTags(VALID_TAG_MATH)
                .build();
        EditLessonCommand expectedCommand = new EditLessonCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // class only
        Index targetIndex = INDEX_FIRST_LESSON;
        String userInput = targetIndex.getOneBased() + CLASS_DESC_MATH;

        EditLessonDescriptor descriptor = new EditLessonDescriptorBuilder()
                .withClassName(VALID_CLASS_MATH)
                .build();
        EditLessonCommand expectedCommand = new EditLessonCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags only
        userInput = targetIndex.getOneBased() + TAG_DESC_SCIENCE;
        descriptor = new EditLessonDescriptorBuilder()
                .withTags(VALID_TAG_SCIENCE)
                .build();
        expectedCommand = new EditLessonCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        Index targetIndex = INDEX_FIRST_LESSON;
        String userInput = targetIndex.getOneBased() + CLASS_DESC_MATH + CLASS_DESC_SCIENCE;

        assertParseFailure(parser, userInput,
                getErrorMessageForDuplicatePrefixes(PREFIX_CLASS));
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_SECOND_LESSON;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        EditLessonDescriptor descriptor = new EditLessonDescriptorBuilder().withTags().build();
        EditLessonCommand expectedCommand = new EditLessonCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
