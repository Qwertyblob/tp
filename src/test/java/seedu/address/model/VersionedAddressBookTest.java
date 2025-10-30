package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.testutil.AddressBookBuilder;

/**
 * Tests VersionedAddressBook's version history behavior.
 */
public class VersionedAddressBookTest {

    private VersionedAddressBook versionedAddressBook;

    @BeforeEach
    public void setUp() {
        AddressBook addressBook = new AddressBook();
        versionedAddressBook = new VersionedAddressBook(addressBook);
    }

    @Test
    public void commit_addsNewVersion_success() {
        AddressBook initial = new AddressBookBuilder().withPerson(ALICE).build();
        versionedAddressBook.resetData(initial);
        versionedAddressBook.commit(ALICE.toString());

        AddressBook next = new AddressBookBuilder(initial).withPerson(BENSON).build();
        versionedAddressBook.resetData(next);
        versionedAddressBook.commit(BENSON.toString());

        assertTrue(versionedAddressBook.canUndo());
        assertFalse(versionedAddressBook.canRedo());
    }

    @Test
    public void undo_revertsToPreviousVersion_success() {
        AddressBook first = new AddressBookBuilder().withPerson(ALICE).build();
        AddressBook second = new AddressBookBuilder(first).withPerson(BENSON).build();

        versionedAddressBook.resetData(first);
        versionedAddressBook.commit(first.toString());
        versionedAddressBook.resetData(second);
        versionedAddressBook.commit(second.toString());

        versionedAddressBook.undo();
        assertEquals(first, new AddressBook(versionedAddressBook));
    }

    @Test
    public void redo_restoresNextVersion_success() {
        AddressBook first = new AddressBookBuilder().withPerson(ALICE).build();
        AddressBook second = new AddressBookBuilder(first).withPerson(BENSON).build();

        versionedAddressBook.resetData(first);
        versionedAddressBook.commit(first.toString());
        versionedAddressBook.resetData(second);
        versionedAddressBook.commit(second.toString());

        versionedAddressBook.undo();
        versionedAddressBook.redo();
        assertEquals(second, new AddressBook(versionedAddressBook));
    }

    @Test
    public void undo_withoutHistory_throwsException() {
        assertThrows(IllegalStateException.class, () -> versionedAddressBook.undo());
    }

    @Test
    public void redo_withoutFuture_throwsException() {
        assertThrows(IllegalStateException.class, () -> versionedAddressBook.redo());
    }

    @Test
    public void commit_afterUndo_clearsFutureVersions() {
        AddressBook first = new AddressBookBuilder().withPerson(ALICE).build();
        AddressBook second = new AddressBookBuilder(first).withPerson(BENSON).build();
        AddressBook third = new AddressBookBuilder(second).withPerson(CARL).build();

        versionedAddressBook.resetData(first);
        versionedAddressBook.commit(first.toString());
        versionedAddressBook.resetData(second);
        versionedAddressBook.commit(second.toString());
        versionedAddressBook.resetData(third);
        versionedAddressBook.commit(third.toString());

        versionedAddressBook.undo();
        versionedAddressBook.undo();

        // committing after undo should clear redo history
        AddressBook branched = new AddressBookBuilder().withPerson(ALICE).withPerson(CARL).build();
        versionedAddressBook.resetData(branched);
        versionedAddressBook.commit(branched.toString());

        assertFalse(versionedAddressBook.canRedo());
    }
}
