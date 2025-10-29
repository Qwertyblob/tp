package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code UndoCommand}.
 */
public class UndoCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new AddressBookBuilder().withPerson(ALICE).build(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_undoSuccessful_success() throws Exception {
        // add BENSON and commit
        model.addPerson(BENSON);
        model.commitAddressBook();

        // make sure model actually changed
        assertEquals(2, model.getAddressBook().getPersonList().size());

        // execute undo
        CommandResult result = new UndoCommand().execute(model);

        // verify undo successful
        assertEquals("Undo successful!", result.getFeedbackToUser());
        assertEquals(1, model.getAddressBook().getPersonList().size());
    }

    @Test
    public void execute_noUndoAvailable_throwsCommandException() {
        UndoCommand undoCommand = new UndoCommand();
        assertThrows(CommandException.class, () -> undoCommand.execute(model));
    }

    @Test
    public void execute_multipleUndos_success() throws Exception {
        model.addPerson(BENSON);
        model.commitAddressBook();
        model.addPerson(new PersonBuilder().withName("Carl").build());
        model.commitAddressBook();

        // Undo twice
        new UndoCommand().execute(model);
        new UndoCommand().execute(model);

        assertEquals(1, model.getAddressBook().getPersonList().size());
    }

    @Test
    public void execute_afterRedoableHistoryCleared_failure() throws Exception {
        model.addPerson(BENSON);
        model.commitAddressBook();
        new UndoCommand().execute(model);

        // commit new change, clearing redo history
        model.addPerson(new PersonBuilder().withName("Carl").build());
        model.commitAddressBook();

        // undo should still work
        new UndoCommand().execute(model);
        assertEquals(1, model.getAddressBook().getPersonList().size());
    }
}
