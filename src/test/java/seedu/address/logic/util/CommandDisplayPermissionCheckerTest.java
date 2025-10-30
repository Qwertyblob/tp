package seedu.address.logic.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CommandResult.DisplayType;

public class CommandDisplayPermissionCheckerTest {

    // Neutral Commands (Always Allowed)

    @Test
    // Neutral commands are always allowed in any display
    public void isNotAllowed_neutralCommands_allowedEverywhere() {
        String[] neutralCommands = {"help", "exit", "clear", "enrol", "mark", "unmark", "list", "listc"};
        for (String cmd : neutralCommands) {
            assertFalse(CommandDisplayPermissionChecker.isNotAllowed(cmd, DisplayType.DEFAULT),
                    cmd + " should be allowed in person view");
            assertFalse(CommandDisplayPermissionChecker.isNotAllowed(cmd, DisplayType.CLASS_LIST),
                    cmd + " should be allowed in class view");
        }
    }

    // Person Commands

    @Test
    // Person commands allowed in DEFAULT (person) view
    public void isNotAllowed_personCommands_allowedInPersonView() {
        String[] personCommands = {"add", "edit", "delete", "find"};
        for (String cmd : personCommands) {
            assertFalse(CommandDisplayPermissionChecker.isNotAllowed(cmd, DisplayType.DEFAULT),
                    cmd + " should be allowed in person view");
        }
    }

    @Test
    // Person commands disallowed in CLASS_LIST view
    public void isNotAllowed_personCommands_disallowedInClassView() {
        String[] personCommands = {"add", "edit", "delete", "find"};
        for (String cmd : personCommands) {
            assertTrue(CommandDisplayPermissionChecker.isNotAllowed(cmd, DisplayType.CLASS_LIST),
                    cmd + " should be disallowed in class view");
        }
    }

    // Class Commands

    @Test
    // Class commands allowed in CLASS_LIST view
    public void isNotAllowed_classCommands_allowedInClassView() {
        String[] classCommands = {"addc", "editc", "deletec", "findc"};
        for (String cmd : classCommands) {
            assertFalse(CommandDisplayPermissionChecker.isNotAllowed(cmd, DisplayType.CLASS_LIST),
                    cmd + " should be allowed in class view");
        }
    }

    @Test
    // Class commands disallowed in DEFAULT (person) view
    public void isNotAllowed_classCommands_disallowedInPersonView() {
        String[] classCommands = {"addc", "editc", "deletec", "findc"};
        for (String cmd : classCommands) {
            assertTrue(CommandDisplayPermissionChecker.isNotAllowed(cmd, DisplayType.DEFAULT),
                    cmd + " should be disallowed in person view");
        }
    }

    // Unknown or future commands (should be allowed)

    @Test
    // Unknown commands should be allowed in all views
    public void isNotAllowed_unknownCommands_allowedEverywhere() {
        String[] unknownCommands = {"stats", "backup", "reload", "xyz"};
        for (String cmd : unknownCommands) {
            assertFalse(CommandDisplayPermissionChecker.isNotAllowed(cmd, DisplayType.DEFAULT),
                    cmd + " should be allowed in person view");
            assertFalse(CommandDisplayPermissionChecker.isNotAllowed(cmd, DisplayType.CLASS_LIST),
                    cmd + " should be allowed in class view");
        }
    }

    // For edge cases

    @Test
    // Blank or null commands should be disallowed
    public void isNotAllowed_blankOrNullCommands_disallowed() {
        assertTrue(CommandDisplayPermissionChecker.isNotAllowed("", DisplayType.DEFAULT));
        assertTrue(CommandDisplayPermissionChecker.isNotAllowed("   ", DisplayType.CLASS_LIST));
        assertTrue(CommandDisplayPermissionChecker.isNotAllowed(null, DisplayType.DEFAULT));
    }
}
