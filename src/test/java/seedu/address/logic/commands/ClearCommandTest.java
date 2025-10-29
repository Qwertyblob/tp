package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.assertConfirmableCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.assertConfirmationRequested;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.ConfirmationManager;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ClearCommandTest {
    @Test
    public void execute_nonEmptyAddressBook_force_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setAddressBook(new AddressBook());

        assertCommandSuccess(new ClearCommand(true), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyAddressBook_requestsConfirmation() {
        Model model = new ModelManager();
        ClearCommand clearCommand = new ClearCommand();
        assertConfirmationRequested(clearCommand, model);
    }

    @Test
    public void execute_nonEmptyAddressBook_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setAddressBook(new AddressBook());

        assertConfirmableCommandSuccess(new ClearCommand(), model,
                new CommandResult(ClearCommand.MESSAGE_SUCCESS, CommandResult.DisplayType.DEFAULT),
                expectedModel, new ConfirmationManager());
    }

}
