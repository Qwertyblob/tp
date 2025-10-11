package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class ContactMatchesPredicateTest {

    @Test
    public void equals() {
        ContactMatchesPredicate firstPredicate =
                new ContactMatchesPredicate("Alice", "student", "12345", "alice@email.com", "Main Street", "friend");
        ContactMatchesPredicate secondPredicate =
                new ContactMatchesPredicate("Bob", "tutor", "67890", "bob@email.com", "Second Street", "");

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        ContactMatchesPredicate firstPredicateCopy =
                new ContactMatchesPredicate("Alice", "student", "12345", "alice@email.com", "Main Street", "friend");
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different values -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_allFieldsMatch_returnsTrue() {
        ContactMatchesPredicate predicate =
                new ContactMatchesPredicate("Alice", "student", "12345", "alice@email.com", "Main Street", "friend");
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice")
                .withRole("student")
                .withPhone("12345")
                .withEmail("alice@email.com")
                .withAddress("Main Street")
                .withTags("friend")
                .build()));
    }

    @Test
    public void test_partialFieldsMatch_returnsTrue() {
        // Only name and tag
        ContactMatchesPredicate predicate =
                new ContactMatchesPredicate("Alice", "", "", "", "", "friend");
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice")
                .withRole("tutor")
                .withPhone("67890")
                .withEmail("bob@email.com")
                .withAddress("Second Street")
                .withTags("friend", "colleague")
                .build()));

        // Only role
        predicate = new ContactMatchesPredicate("", "tutor", "", "", "", "");
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Bob")
                .withRole("tutor")
                .withPhone("67890")
                .withEmail("bob@email.com")
                .withAddress("Second Street")
                .withTags("colleague")
                .build()));
    }

    @Test
    public void test_noFieldsMatch_returnsFalse() {
        ContactMatchesPredicate predicate =
                new ContactMatchesPredicate("Carol", "student", "11111", "carol@email.com", "Third Street", "family");
        assertFalse(predicate.test(new PersonBuilder()
                .withName("Alice")
                .withRole("tutor")
                .withPhone("12345")
                .withEmail("alice@email.com")
                .withAddress("Main Street")
                .withTags("friend")
                .build()));
    }

    @Test
    public void toStringMethod() {
        ContactMatchesPredicate predicate =
                new ContactMatchesPredicate("Alice", "student", "12345", "alice@email.com", "Main Street", "friend");

        String expected = ContactMatchesPredicate.class.getCanonicalName()
                + "{name=Alice, role=student, phone=12345, email=alice@email.com, address=Main Street, tags=friend}";
        assertEquals(expected, predicate.toString());
    }
}
