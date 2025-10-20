package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTOR;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.ConfirmationManager;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.person.ContactMatchesPredicate;
import seedu.address.model.person.Person;
import seedu.address.testutil.EditLessonDescriptorBuilder;
import seedu.address.testutil.EditPersonDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_ID_AMY = "S0000001";
    public static final String VALID_ID_BOB = "T0000001";
    public static final String VALID_ID_JAMES = "S0000010";

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_NAME_JAMES = "James Tan";

    public static final String VALID_ROLE_AMY = "student";
    public static final String VALID_ROLE_BOB = "tutor";
    public static final String VALID_ROLE_JAMES = "student";

    public static final String VALID_PHONE_AMY = "11111111";
    public static final String VALID_PHONE_BOB = "22222222";
    public static final String VALID_PHONE_JAMES = "93334444";

    public static final String VALID_EMAIL_AMY = "amy@example.com";
    public static final String VALID_EMAIL_BOB = "bob@example.com";
    public static final String VALID_EMAIL_JAMES = "james@example.com";

    public static final String VALID_ADDRESS_AMY = "Block 312, Amy Street 1";
    public static final String VALID_ADDRESS_BOB = "Block 123, Bobby Street 3";
    public static final String VALID_ADDRESS_JAMES = "99, Clementi Street 12";

    public static final String VALID_TAG_HUSBAND = "husband";
    public static final String VALID_TAG_FRIEND = "friend";
    public static final String VALID_TAG_CLASSREP = "classrep";

    // Lesson test data
    public static final String VALID_CLASS_MATH = "A1a";
    public static final String VALID_DAY_MONDAY = "monday";
    public static final String VALID_TIME_1200 = "1200";
    public static final String VALID_TUTOR_T1234567 = "t1234567";
    public static final String VALID_TAG_MATH = "Math";
    public static final String VALID_TAG_SCIENCE = "Science";

    public static final String VALID_CLASS_SCIENCE = "B2b";
    public static final String VALID_DAY_TUESDAY = "tuesday";
    public static final String VALID_TIME_1400 = "1400";
    public static final String VALID_TUTOR_T7654321 = "t7654321";


    public static final String INVALID_CLASS_NAME_FORMAT = "Math101";
    public static final String INVALID_DAY_FORMAT = "Funday";
    public static final String INVALID_TIME_FORMAT = "25:00";
    public static final String INVALID_TUTOR_ID_FORMAT = "A1234567";


    // Descriptor-style constants
    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String NAME_DESC_JAMES = " " + PREFIX_NAME + VALID_NAME_JAMES;

    public static final String ROLE_DESC_AMY = " " + PREFIX_ROLE + VALID_ROLE_AMY;
    public static final String ROLE_DESC_BOB = " " + PREFIX_ROLE + VALID_ROLE_BOB;
    public static final String ROLE_DESC_JAMES = " " + PREFIX_ROLE + VALID_ROLE_JAMES;

    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String PHONE_DESC_JAMES = " " + PREFIX_PHONE + VALID_PHONE_JAMES;

    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;
    public static final String EMAIL_DESC_JAMES = " " + PREFIX_EMAIL + VALID_EMAIL_JAMES;

    public static final String ADDRESS_DESC_AMY = " " + PREFIX_ADDRESS + VALID_ADDRESS_AMY;
    public static final String ADDRESS_DESC_BOB = " " + PREFIX_ADDRESS + VALID_ADDRESS_BOB;
    public static final String ADDRESS_DESC_JAMES = " " + PREFIX_ADDRESS + VALID_ADDRESS_JAMES;

    public static final String TAG_DESC_FRIEND = " " + PREFIX_TAG + VALID_TAG_FRIEND;
    public static final String TAG_DESC_HUSBAND = " " + PREFIX_TAG + VALID_TAG_HUSBAND;
    public static final String STUDENT_ID_DESC_AMY = " " + PREFIX_ID + VALID_ID_AMY;
    public static final String TAG_DESC_CLASSREP = " " + PREFIX_TAG + VALID_TAG_CLASSREP;


    // Lesson command descriptions
    public static final String CLASS_DESC_MATH = " " + PREFIX_CLASS + VALID_CLASS_MATH;
    public static final String CLASS_DESC_SCIENCE = " " + PREFIX_CLASS + VALID_CLASS_SCIENCE;

    public static final String DAY_DESC_MONDAY = " " + PREFIX_DAY + VALID_DAY_MONDAY;
    public static final String DAY_DESC_TUESDAY = " " + PREFIX_DAY + VALID_DAY_TUESDAY;

    public static final String TIME_DESC_1200 = " " + PREFIX_TIME + VALID_TIME_1200;
    public static final String TIME_DESC_1400 = " " + PREFIX_TIME + VALID_TIME_1400;

    public static final String TUTOR_DESC_T1234567 = " " + PREFIX_TUTOR + VALID_TUTOR_T1234567;
    public static final String TUTOR_DESC_T7654321 = " " + PREFIX_TUTOR + VALID_TUTOR_T7654321;

    public static final String TAG_DESC_MATH = " " + PREFIX_TAG + VALID_TAG_MATH;
    public static final String TAG_DESC_SCIENCE = " " + PREFIX_TAG + VALID_TAG_SCIENCE;

    public static final String INVALID_STUDENT_ID_DESC = " " + PREFIX_ID + "a100";
    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_ROLE_DESC = " " + PREFIX_ROLE + "teacher"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS; // empty string not allowed for addresses
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "hubby*"; // '*' not allowed in tags

    public static final String INVALID_CLASS_NAME_DESC = " " + PREFIX_CLASS + "Math101";

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditPersonDescriptor DESC_AMY;
    public static final EditCommand.EditPersonDescriptor DESC_BOB;

    static {
        DESC_AMY = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY).withRole(VALID_ROLE_AMY)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        DESC_BOB = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).withRole(VALID_ROLE_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
    }

    public static final EditLessonCommand.EditLessonDescriptor DESC_MATH;
    public static final EditLessonCommand.EditLessonDescriptor DESC_SCIENCE;

    static {
        DESC_MATH = new EditLessonDescriptorBuilder().withTags(VALID_TAG_MATH).build();
        DESC_SCIENCE = new EditLessonDescriptorBuilder().withTags(VALID_TAG_SCIENCE).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel.getFilteredPersonList(), actualModel.getFilteredPersonList());
        } catch (Exception e) {
            throw new AssertionError("Execution of command should not fail.", e);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
                                            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, CommandResult.DisplayType.DEFAULT);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertConfirmableCommandSuccess(ConfirmableCommand command, Model actualModel,
                                                       CommandResult expectedCommandResult,
                                                       Model expectedModel, ConfirmationManager confirmationManager) {
        try {
            confirmationManager.requestConfirmation(command, actualModel);
            CommandResult result = confirmationManager.handleUserResponse(
                    ConfirmationManager.INPUT_CONFIRM, actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel.getFilteredPersonList(), actualModel.getFilteredPersonList());
        } catch (Exception e) {
            throw new AssertionError("Execution of command should not fail.", e);
        }
    }

    /**
     * Confirms that <br>
     * - The DisplayType is CONFIRMATION <br>
     * - The correct confirmation message is displayed
     */
    public static void assertConfirmationRequested(ConfirmableCommand command, Model actualModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(CommandResult.DisplayType.CONFIRMATION, result.getDisplayType());
            assertEquals(command.getConfirmationMessage(actualModel), result.getFeedbackToUser());
        } catch (CommandException e) {
            throw new AssertionError("Requesting confirmation should not fail", e);
        }
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book, filtered person list and selected person in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        AddressBook expectedAddressBook = new AddressBook(actualModel.getAddressBook());
        List<Person> expectedFilteredList = new ArrayList<>(actualModel.getFilteredPersonList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedAddressBook, actualModel.getAddressBook());
        assertEquals(expectedFilteredList, actualModel.getFilteredPersonList());
    }

    /**
     * Updates {@code model}'s filtered list to show only the person at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showPersonAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredPersonList().size());

        Person person = model.getFilteredPersonList().get(targetIndex.getZeroBased());

        model.updateFilteredPersonList(new ContactMatchesPredicate(
                person.getId().toString(),
                person.getName().fullName,
                "", "", "", "", "", ""));

        assertEquals(1, model.getFilteredPersonList().size());
    }

}
