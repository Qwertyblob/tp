package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalLessons.getTypicalModelManager;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.ContactMatchesPredicate;
import seedu.address.model.person.Person;
import seedu.address.testutil.AddressBookBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new AddressBook(), new AddressBook(modelManager.getAddressBook()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setAddressBookFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setAddressBookFilePath(null));
    }

    @Test
    public void setAddressBookFilePath_validPath_setsAddressBookFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setAddressBookFilePath(path);
        assertEquals(path, modelManager.getAddressBookFilePath());
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.hasPerson(ALICE));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredPersonList().remove(0));
    }

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs)));

        // different filteredList -> returns false
        modelManager.updateFilteredPersonList(
                new ContactMatchesPredicate("", ALICE.getName().fullName, "", "", "", "", "", ""));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(addressBook, differentUserPrefs)));
    }

    @Test
    public void commitAddressBook_success() {
        ModelManager modelManager = new ModelManager();
        modelManager.addPerson(ALICE);
        modelManager.commitAddressBook();
        assertTrue(modelManager.canUndoAddressBook());
    }

    @Test
    public void undoAddressBook_success() {
        ModelManager modelManager = new ModelManager();
        modelManager.addPerson(ALICE);
        modelManager.commitAddressBook();

        modelManager.addPerson(BENSON);
        modelManager.commitAddressBook();

        assertEquals(2, modelManager.getAddressBook().getPersonList().size());

        modelManager.undoAddressBook();

        // Undo should revert to state with only ALICE
        assertEquals(1, modelManager.getAddressBook().getPersonList().size());
        assertTrue(modelManager.getAddressBook().getPersonList().contains(ALICE));
        assertFalse(modelManager.getAddressBook().getPersonList().contains(BENSON));
    }

    @Test
    public void redoAddressBook_success() {
        ModelManager modelManager = new ModelManager();
        modelManager.addPerson(ALICE);
        modelManager.commitAddressBook();

        modelManager.addPerson(BENSON);
        modelManager.commitAddressBook();

        modelManager.undoAddressBook();
        assertEquals(1, modelManager.getAddressBook().getPersonList().size());

        modelManager.redoAddressBook();
        assertEquals(2, modelManager.getAddressBook().getPersonList().size());
    }

    @Test
    public void canUndoRedoAddressBook_checksCorrectly() {
        ModelManager modelManager = new ModelManager();
        assertFalse(modelManager.canUndoAddressBook());
        assertFalse(modelManager.canRedoAddressBook());

        modelManager.addPerson(ALICE);
        modelManager.commitAddressBook();
        assertTrue(modelManager.canUndoAddressBook());
        assertFalse(modelManager.canRedoAddressBook());

        modelManager.undoAddressBook();
        assertFalse(modelManager.canUndoAddressBook());
        assertTrue(modelManager.canRedoAddressBook());
    }

    @Test
    public void getLessonsAssignedToTutor_tutorAssignedToLessons_returnsLessonList() {
        // Use a typical model that includes tutors and lessons
        Model model = getTypicalModelManager();

        // Get a tutor known to be assigned to a lesson (based on your TypicalLessons setup)
        Person tutor = model.getFilteredPersonList().stream()
                .filter(p -> p.getRole().isTutor())
                .findFirst()
                .orElseThrow();

        // Should return at least one lesson assigned to this tutor
        List<Lesson> lessons = model.getLessonsAssignedToTutor(tutor);

        assertFalse(lessons.isEmpty());
        for (Lesson lesson : lessons) {
            assertEquals(tutor.getId().getValue(), lesson.getTutor().toString());
        }
    }

    @Test
    public void getLessonsAssignedToTutor_tutorNotAssignedToAnyLessons_returnsEmptyList() {
        Model model = getTypicalModelManager();

        // Create a new tutor who is not assigned to any lessons
        Person unassignedTutor = new Person(
                new seedu.address.model.person.IdentificationNumber("T0009999"),
                new seedu.address.model.person.Name("Unassigned Tutor"),
                new seedu.address.model.person.Role("Tutor"),
                new seedu.address.model.person.Phone("99999999"),
                new seedu.address.model.person.Email("unassigned@tutor.com"),
                new seedu.address.model.person.Address("No lessons street"),
                Set.of()
        );

        model.addPerson(unassignedTutor);
        List<Lesson> lessons = model.getLessonsAssignedToTutor(unassignedTutor);

        assertTrue(lessons.isEmpty());
    }

    @Test
    public void getLessonsAssignedToTutor_nonTutor_returnsEmptyList() {
        Model model = getTypicalModelManager();

        // Pick a student (non-tutor)
        Person student = model.getFilteredPersonList().stream()
                .filter(p -> !p.getRole().isTutor())
                .findFirst()
                .orElseThrow();

        List<Lesson> lessons = model.getLessonsAssignedToTutor(student);

        assertTrue(lessons.isEmpty());
    }

    @Test
    public void getLessonsAssignedToTutor_nullTutor_throwsNullPointerException() {
        Model model = getTypicalModelManager();
        assertThrows(NullPointerException.class, () -> model.getLessonsAssignedToTutor(null));
    }

}
