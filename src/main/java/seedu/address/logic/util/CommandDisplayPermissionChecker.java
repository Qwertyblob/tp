package seedu.address.logic.util;

import seedu.address.logic.commands.CommandResult.DisplayType;

/**
 * Checks if a given command string can be executed in the current display context.
 */
public class CommandDisplayPermissionChecker {

    /**
     * Determines if a command is allowed in the given display context.
     *
     * @param commandText     The raw command text entered by the user.
     * @param currentDisplay  The current display context (e.g. person list or class list).
     * @return true if the command is permitted; false otherwise.
     */
    public static boolean isNotAllowed(String commandText, DisplayType currentDisplay) {
        if (commandText == null || commandText.isBlank()) {
            return true;
        }

        String lower = commandText.trim().toLowerCase();

        String[] parts = lower.split("\\s+", 2);
        String commandWord = parts[0];

        if (isNeutralCommand(commandWord)) {
            return false;
        }


        switch (currentDisplay) {
        case DEFAULT:
            if (isClassCommand(commandWord)) {
                return true;
            }
            break;

        case CLASS_LIST:
            if (isPersonCommand(commandWord)) {
                return true;
            }
            break;

        default:
        }

        return false;
    }

    private static boolean isNeutralCommand(String cmd) {
        return switch (cmd) {
        case "help", "exit", "clear", "enrol", "mark", "unmark", "list", "listc" -> true;
        default -> false;
        };
    }

    private static boolean isPersonCommand(String cmd) {
        return switch (cmd) {
        case "add", "edit", "delete", "find" -> true;
        default -> false;
        };
    }

    private static boolean isClassCommand(String cmd) {
        return switch (cmd) {
        case "addc", "editc", "deletec", "findc" -> true;
        default -> false;
        };
    }
}
