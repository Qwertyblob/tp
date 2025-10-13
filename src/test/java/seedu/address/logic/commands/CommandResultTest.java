package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CommandResult.DisplayType;

public class CommandResultTest {

    @Test
    public void equals() {
        CommandResult commandResult = new CommandResult("feedback", DisplayType.RECENT);

        // same values -> returns true
        assertTrue(commandResult.equals(new CommandResult("feedback", DisplayType.RECENT)));
        assertTrue(commandResult.equals(new CommandResult("feedback", false, false, DisplayType.RECENT)));

        // same object -> returns true
        assertTrue(commandResult.equals(commandResult));

        // null -> returns false
        assertFalse(commandResult.equals(null));

        // different types -> returns false
        assertFalse(commandResult.equals(0.5f));

        // different feedbackToUser value -> returns false
        assertFalse(commandResult.equals(new CommandResult("different", DisplayType.RECENT)));

        // different showHelp value -> returns false
        assertFalse(commandResult.equals(new CommandResult("feedback", true, false, DisplayType.RECENT)));

        // different exit value -> returns false
        assertFalse(commandResult.equals(new CommandResult("feedback", false, true, DisplayType.RECENT)));

        // different displayType should NOT affect equality (based on equals implementation)
        assertTrue(commandResult.equals(new CommandResult("feedback", false, false, DisplayType.DEFAULT)));
    }

    @Test
    public void hashcode() {
        CommandResult commandResult = new CommandResult("feedback", DisplayType.RECENT);

        // same values -> returns same hashcode
        assertEquals(commandResult.hashCode(),
                new CommandResult("feedback", DisplayType.RECENT).hashCode());

        // different feedbackToUser value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(),
                new CommandResult("different", DisplayType.RECENT).hashCode());

        // different showHelp value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(),
                new CommandResult("feedback", true, false, DisplayType.RECENT).hashCode());

        // different exit value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(),
                new CommandResult("feedback", false, true, DisplayType.RECENT).hashCode());
    }

    @Test
    public void displayType_defaultsToRecentWhenNull() {
        CommandResult result = new CommandResult("feedback", false, false, null);
        assertEquals(DisplayType.RECENT, result.getDisplayType());
    }

    @Test
    public void toStringMethod() {
        CommandResult commandResult = new CommandResult("feedback", DisplayType.CLASS_LIST);
        String expected = CommandResult.class.getCanonicalName() + "{feedbackToUser="
                + commandResult.getFeedbackToUser() + ", showHelp=" + commandResult.isShowHelp()
                + ", exit=" + commandResult.isExit() + ", displayType=" + commandResult.getDisplayType() + "}";
        assertEquals(expected, commandResult.toString());
    }
}
