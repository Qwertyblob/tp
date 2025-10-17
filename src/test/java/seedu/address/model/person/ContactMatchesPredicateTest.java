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
    public void test_allFieldsEmpty_returnsFalse() {
        // Test the condition: all fields empty
        ContactMatchesPredicate predicate =
                new ContactMatchesPredicate("", "", "", "", "", "", "", "");
        assertFalse(predicate.test(new PersonBuilder().build()));
    }

    @Test
    public void test_equals_comprehensive() {
        ContactMatchesPredicate predicate1 =
                new ContactMatchesPredicate("S0000001", "Alice", "student", "M2a", "12345",
                        "alice@email.com", "Main Street", "friend");
        ContactMatchesPredicate predicate2 =
                new ContactMatchesPredicate("S0000001", "Alice", "student", "M2a", "12345",
                        "alice@email.com", "Main Street", "friend");
        ContactMatchesPredicate predicate3 =
                new ContactMatchesPredicate("T0000001", "Alice", "student", "M2a", "12345",
                        "alice@email.com", "Main Street", "friend");
        ContactMatchesPredicate predicate4 =
                new ContactMatchesPredicate("S0000001", "Bob", "student", "M2a", "12345",
                        "alice@email.com", "Main Street", "friend");
        ContactMatchesPredicate predicate5 =
                new ContactMatchesPredicate("S0000001", "Alice", "tutor", "M2a", "12345",
                        "alice@email.com", "Main Street", "friend");
        ContactMatchesPredicate predicate6 =
                new ContactMatchesPredicate("S0000001", "Alice", "student", "S3b", "12345",
                        "alice@email.com", "Main Street", "friend");
        ContactMatchesPredicate predicate7 =
                new ContactMatchesPredicate("S0000001", "Alice", "student", "M2a", "67890",
                        "alice@email.com", "Main Street", "friend");
        ContactMatchesPredicate predicate8 =
                new ContactMatchesPredicate("S0000001", "Alice", "student", "M2a", "12345",
                        "bob@email.com", "Main Street", "friend");
        ContactMatchesPredicate predicate9 =
                new ContactMatchesPredicate("S0000001", "Alice", "student", "M2a", "12345",
                        "alice@email.com", "Second Street", "friend");
        ContactMatchesPredicate predicate10 =
                new ContactMatchesPredicate("S0000001", "Alice", "student", "M2a", "12345",
                        "alice@email.com", "Main Street", "colleague");

        // Test all field comparisons in equals method
        assertTrue(predicate1.equals(predicate2)); // Same values
        assertFalse(predicate1.equals(predicate3)); // Different id
        assertFalse(predicate1.equals(predicate4)); // Different name
        assertFalse(predicate1.equals(predicate5)); // Different role
        assertFalse(predicate1.equals(predicate6)); // Different lesson
        assertFalse(predicate1.equals(predicate7)); // Different phone
        assertFalse(predicate1.equals(predicate8)); // Different email
        assertFalse(predicate1.equals(predicate9)); // Different address
        assertFalse(predicate1.equals(predicate10)); // Different tags
    }

    @Test
    public void test_matchingLogic_edgeCases() {
        // Test case sensitivity in matching
        ContactMatchesPredicate predicate =
                new ContactMatchesPredicate("", "alice", "", "", "", "", "", "");
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").build()));

        // Test partial matching
        predicate = new ContactMatchesPredicate("", "Ali", "", "", "", "", "", "");
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));

        // Test multiple word matching
        predicate = new ContactMatchesPredicate("", "Alice Bob", "", "", "", "", "", "");
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Test lesson matching with multiple lessons
        predicate = new ContactMatchesPredicate("", "", "", "M2a", "", "", "", "");
        assertTrue(predicate.test(new PersonBuilder().withLessons("M2a", "S3b").build()));

        // Test tag matching with multiple tags
        predicate = new ContactMatchesPredicate("", "", "", "", "", "", "", "friend");
        assertTrue(predicate.test(new PersonBuilder().withTags("friend", "colleague").build()));
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
