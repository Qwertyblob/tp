package seedu.address.logic;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.ConfirmableCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.util.CommandDisplayPermissionChecker;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.Person;
import seedu.address.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final AddressBookParser addressBookParser;

    private final ConfirmationManager confirmationManager = new ConfirmationManager();
    private CommandResult.DisplayType currentDisplayType = CommandResult.DisplayType.DEFAULT;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        addressBookParser = new AddressBookParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult = null;

        try {
            if (model.getPendingCommand() != null) {
                String commandWord = commandText.trim().split("\\s+")[0];
                if (CommandDisplayPermissionChecker.isNotAllowed(commandWord, currentDisplayType)) {
                    throw new CommandException("You cannot use this command in the current view.");
                }
                commandResult = confirmationManager.handleUserResponse(commandText, model);
            } else {
                String commandWord = commandText.trim().split("\\s+")[0];
                if (CommandDisplayPermissionChecker.isNotAllowed(commandWord, currentDisplayType)) {
                    throw new CommandException("You cannot use this command in the current view.");
                }
                commandResult = processNewCommand(commandText);
            }

            handleSaveResult();

            return commandResult;
        } catch (ParseException e) {
            throw e; // Re-throw ParseException as-is
        } catch (CommandException e) {
            throw e; // Re-throw CommandException as-is
        } catch (Exception e) {
            throw new CommandException("Error handling user response: " + e.getMessage());
        }
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return model.getAddressBook();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public ObservableList<Lesson> getFilteredLessonList() {
        return model.getFilteredLessonList();
    }

    @Override
    public Path getAddressBookFilePath() {
        return model.getAddressBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }

    @Override
    public CommandResult processNewCommand(String commandText) throws CommandException, ParseException {
        Command command = addressBookParser.parseCommand(commandText);
        CommandResult commandResult;

        if (command instanceof ConfirmableCommand confirmable && !confirmable.isForced()) {
            // Perform semantic validation of command before moving on to confirmation step
            confirmable.validate(model);
            commandResult = confirmationManager.requestConfirmation(confirmable, model);
        } else {
            // Not a ConfirmableCommand, move directly to execution
            commandResult = command.execute(model);
        }

        return commandResult;
    }

    @Override
    public void handleSaveResult() throws CommandException {
        try {
            storage.saveAddressBook(model.getAddressBook());
        } catch (AccessDeniedException e) {
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }
    }

    public CommandResult.DisplayType getCurrentDisplayType() {
        return currentDisplayType;
    }

}
