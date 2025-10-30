package seedu.address.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.ConfirmableCommand;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.Person;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /** {@code Predicate} that always evaluate to true */
    Predicate<Lesson> PREDICATE_SHOW_ALL_LESSONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setPerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasLesson(Lesson aLesson);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deleteLesson(Lesson target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addLesson(Lesson person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setLesson(Lesson target, Lesson editedPerson);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Lesson> getFilteredLessonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredLessonList(Predicate<Lesson> predicate);

    /** Returns the command that is awaiting confirmation */
    public ConfirmableCommand getPendingCommand();

    /** Updates the model to contain a new command that is awaiting confirmation */
    public void updatePendingCommand(ConfirmableCommand command);

    public void clearPendingCommand();

    /** Saves the current AddressBook state for undo/redo. */
    void commitAddressBook(String command);

    void commitAddressBook();

    /** Returns the last undoable command executed */
    String getLastCommandDescription();

    /** Returns the undoable command to be redone */
    String getRedoCommandDescription();

    /** Restores the previous AddressBook state. */
    void undoAddressBook();

    /** Restores the next AddressBook state. */
    void redoAddressBook();

    /** Returns true if the model has previous AddressBook states to restore. */
    boolean canUndoAddressBook();

    /** Returns true if the model has undone AddressBook states to restore. */
    boolean canRedoAddressBook();

}
