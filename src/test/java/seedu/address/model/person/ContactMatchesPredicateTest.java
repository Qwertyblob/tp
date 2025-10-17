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
                new ContactMatchesPredicate("S0000001", "Alice", "student", "M2a", "12345",
                        "alice@email.com", "Main Street", "friend");
        ContactMatchesPredicate secondPredicate =
                new ContactMatchesPredicate("T0000001", "Bob", "tutor", "M2a", "67890",
                        "bob@email.com", "Second Street", "");

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        ContactMatchesPredicate firstPredicateCopy =
                new ContactMatchesPredicate("S0000001", "Alice", "student", "M2a", "12345",
                        "alice@email.com", "Main Street", "friend");
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
                new ContactMatchesPredicate("S0000001", "Alice", "student", "M2a", "12345",
                        "alice@email.com", "Main Street", "friend");
        assertTrue(predicate.test(new PersonBuilder()
                .withId("S0000001")
                .withName("Alice")
                .withRole("student")
                .withLessons("M2a")
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
                new ContactMatchesPredicate("", "Alice", "", "", "", "", "", "friend");
        assertTrue(predicate.test(new PersonBuilder()
                .withId("S0000001")
                .withName("Alice")
                .withRole("tutor")
                .withLessons("M2a")
                .withPhone("67890")
                .withEmail("bob@email.com")
                .withAddress("Second Street")
                .withTags("friend", "colleague")
                .build()));

        // Only role
        predicate = new ContactMatchesPredicate("", "", "tutor", "", "", "", "", "");
        assertTrue(predicate.test(new PersonBuilder()
                .withId("T0000001")
                .withName("Bob")
                .withRole("tutor")
                .withLessons("M2a")
                .withPhone("67890")
                .withEmail("bob@email.com")
                .withAddress("Second Street")
                .withTags("colleague")
                .build()));
    }

    @Test
    public void test_noFieldsMatch_returnsFalse() {
        ContactMatchesPredicate predicate =
                new ContactMatchesPredicate("S1234567", "Carol", "student", "S3b", "11111",
                        "carol@email.com", "Third Street", "family");
        assertFalse(predicate.test(new PersonBuilder()
                .withId("S0000001")
                .withName("Alice")
                .withRole("tutor")
                .withLessons("M2a")
                .withPhone("12345")
                .withEmail("alice@email.com")
                .withAddress("Main Street")
                .withTags("friend")
                .build()));
    }

    @Test
    public void toStringMethod() {
        ContactMatchesPredicate predicate =
                new ContactMatchesPredicate("S0000001", "Alice", "student", "M2a", "12345",
                        "alice@email.com", "Main Street", "friend");

        String expected = ContactMatchesPredicate.class.getCanonicalName()
                + "{id=S0000001, name=Alice, role=student, lessons=M2a, phone=12345, email=alice@email.com, "
                + "address=Main Street, tags=friend}";
        assertEquals(expected, predicate.toString());
    }
}
