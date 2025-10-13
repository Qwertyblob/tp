package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalLessons.getTypicalModelManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListClassCommand.
 */
public class ListLessonCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = getTypicalModelManager();
        expectedModel = getTypicalModelManager();
    }

    @Test
    public void execute_showsAllLessons_success() {
        ListLessonCommand command = new ListLessonCommand();
        CommandResult result = command.execute(model);

        assertEquals("Listed all classes", result.getFeedbackToUser());
        assertEquals(CommandResult.DisplayType.CLASS_LIST, result.getDisplayType());
    }

    @Test
    public void execute_doesNotModifyLessonList() {
        ListLessonCommand command = new ListLessonCommand();
        command.execute(model);

        // Verify lessons remain identical
        assertEquals(expectedModel.getFilteredLessonList(), model.getFilteredLessonList());
    }
}
