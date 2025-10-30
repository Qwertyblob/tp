package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code RedoCommand}.
 */
public class RedoCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        // Set up a model with a VersionedAddressBook and some persons
        model = new ModelManager();
        model.addPerson(ALICE);
        model.commitAddressBook(ALICE.toString());

        model.addPerson(BENSON);
        model.commitAddressBook(BENSON.toString());

        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_singleRedo_success() {
        // Undo first to create a redoable state
        model.undoAddressBook();
        assertEquals(1, model.getAddressBook().getPersonList().size());

        // Expected model after redoing should match model before undo
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(BENSON);
        expectedModel.commitAddressBook(BENSON.toString());

        assertCommandSuccess(new RedoCommand(), model,
                String.format("Redo successful!\n(Redo: %s)", BENSON.toString()), expectedModel);
    }

    @Test
    public void execute_multipleRedo_success() {
        // Prepare a model with multiple states
        model.undoAddressBook();
        model.redoAddressBook(); // back to 2 persons
        model.undoAddressBook(); // back to 1 person

        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(BENSON);
        expectedModel.commitAddressBook(BENSON.toString());

        assertCommandSuccess(new RedoCommand(), model,
                String.format("Redo successful!\n(Redo: %s)", BENSON.toString()), expectedModel);
    }

    @Test
    public void execute_noRedoableState_failure() {
        // No redoable states initially
        assertCommandFailure(new RedoCommand(), model, RedoCommand.MESSAGE_FAILURE);

        // Undo once -> redo should now work once
        model.undoAddressBook();
        model.redoAddressBook();
        assertCommandFailure(new RedoCommand(), model, RedoCommand.MESSAGE_FAILURE);
    }

}
