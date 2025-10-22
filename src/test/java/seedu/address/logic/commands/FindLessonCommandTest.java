package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_LESSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalLessons.MATH_A1A;
import static seedu.address.testutil.TypicalLessons.getTypicalModelManager;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.lesson.LessonMatchesPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindLessonCommand}.
 */
public class FindLessonCommandTest {
    private final Model model = getTypicalModelManager();
    private final Model expectedModel = getTypicalModelManager();

    @Test
    public void equals() {
        LessonMatchesPredicate firstPredicate =
                new LessonMatchesPredicate("A1a", "", "", "", "");
        LessonMatchesPredicate secondPredicate =
                new LessonMatchesPredicate("B2b", "", "", "", "");

        FindLessonCommand findFirstCommand = new FindLessonCommand(firstPredicate);
        FindLessonCommand findSecondCommand = new FindLessonCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindLessonCommand findFirstCommandCopy = new FindLessonCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different predicate -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noLessonFound() {
        String expectedMessage = String.format(MESSAGE_LESSONS_LISTED_OVERVIEW, 0);
        LessonMatchesPredicate predicate = preparePredicate("");
        FindLessonCommand command = new FindLessonCommand(predicate);
        expectedModel.updateFilteredLessonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredLessonList());
    }

    @Test
    public void execute_oneKeyword_oneLessonFound() {
        String expectedMessage = String.format(MESSAGE_LESSONS_LISTED_OVERVIEW, 1);
        LessonMatchesPredicate predicate = preparePredicate("A1a");
        FindLessonCommand command = new FindLessonCommand(predicate);
        expectedModel.updateFilteredLessonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(MATH_A1A), model.getFilteredLessonList());
    }

    @Test
    public void toStringMethod() {
        LessonMatchesPredicate predicate = new LessonMatchesPredicate("A1a", "", "", "", "");
        FindLessonCommand findLessonCommand = new FindLessonCommand(predicate);
        String expected = FindLessonCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findLessonCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code LessonMatchesPredicate}.
     */
    private LessonMatchesPredicate preparePredicate(String userInput) {
        // only className is set for simplicity, others are empty
        return new LessonMatchesPredicate(userInput.trim(), "", "", "", "");
    }
}
