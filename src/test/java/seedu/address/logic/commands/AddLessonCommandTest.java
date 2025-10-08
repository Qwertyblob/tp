package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.TypicalLessons.getTypicalLessons;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.lesson.Lesson;
import seedu.address.testutil.LessonBuilder;

public class AddLessonCommandTest {

    private Model model = new ModelManager();

    @Test
    public void constructor_nullLesson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddLessonCommand(null));
    }

    @Test
    public void execute_lessonAcceptedByModel_addSuccessful() throws Exception {
        Lesson validLesson = new LessonBuilder().build();
        CommandResult commandResult = new AddLessonCommand(validLesson).execute(model);

        assertEquals(String.format(AddLessonCommand.MESSAGE_SUCCESS, Messages.formatLesson(validLesson)),
                commandResult.getFeedbackToUser());
    }

    @Test
    public void execute_duplicateLesson_throwsCommandException() {
        Lesson lessonInList = getTypicalLessons().get(0);
        assertCommandFailure(new AddLessonCommand(lessonInList), model,
                AddLessonCommand.MESSAGE_DUPLICATE_CLASS);
    }

    @Test
    public void equals() {
        Lesson math = new LessonBuilder().withClassName("A1a").build();
        Lesson science = new LessonBuilder().withClassName("B2b").build();
        AddLessonCommand addMathCommand = new AddLessonCommand(math);
        AddLessonCommand addScienceCommand = new AddLessonCommand(science);

        // same object -> returns true
        assertTrue(addMathCommand.equals(addMathCommand));

        // same values -> returns true
        AddLessonCommand addMathCommandCopy = new AddLessonCommand(math);
        assertTrue(addMathCommand.equals(addMathCommandCopy));

        // different types -> returns false
        assertFalse(addMathCommand.equals(1));

        // null -> returns false
        assertFalse(addMathCommand.equals(null));

        // different lesson -> returns false
        assertFalse(addMathCommand.equals(addScienceCommand));
    }

    @Test
    public void toStringMethod() {
        Lesson lesson = new LessonBuilder().withClassName("A1a").build();
        AddLessonCommand addLessonCommand = new AddLessonCommand(lesson);
        String expected = AddLessonCommand.class.getCanonicalName() + "{toAdd=" + lesson + "}";
        assertEquals(expected, addLessonCommand.toString());
    }
}
