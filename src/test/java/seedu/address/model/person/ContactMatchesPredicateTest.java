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
    public void test_emptyFields_returnsFalse() {
        // All fields empty
        ContactMatchesPredicate predicate =
                new ContactMatchesPredicate("", "", "", "", "", "", "", "");
        assertFalse(predicate.test(new PersonBuilder().build()));
    }

    @Test
    public void test_idMatching() {
        // Exact ID match
        ContactMatchesPredicate predicate =
                new ContactMatchesPredicate("S0000001", "", "", "", "", "", "", "");
        assertTrue(predicate.test(new PersonBuilder().withId("S0000001").build()));

        // No ID match
        predicate = new ContactMatchesPredicate("T0000001", "", "", "", "", "", "", "");
        assertFalse(predicate.test(new PersonBuilder().withId("S0000001").build()));
    }

    @Test
    public void test_nameMatching() {
        // Exact name match
        ContactMatchesPredicate predicate =
                new ContactMatchesPredicate("", "Alice", "", "", "", "", "", "");
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").build()));

        // No name match
        predicate = new ContactMatchesPredicate("", "Carol", "", "", "", "", "", "");
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));
    }

    @Test
    public void test_roleMatching() {
        // Exact role match
        ContactMatchesPredicate predicate =
                new ContactMatchesPredicate("", "", "student", "", "", "", "", "");
        assertTrue(predicate.test(new PersonBuilder().withRole("student").build()));

        // No role match
        predicate = new ContactMatchesPredicate("", "", "tutor", "", "", "", "", "");
        assertFalse(predicate.test(new PersonBuilder().withRole("student").build()));
    }

    @Test
    public void test_lessonMatching() {
        // Exact lesson match
        ContactMatchesPredicate predicate =
                new ContactMatchesPredicate("", "", "", "M2a", "", "", "", "");
        assertTrue(predicate.test(new PersonBuilder().withLessons("M2a").build()));

        // Case insensitive lesson match
        predicate = new ContactMatchesPredicate("", "", "", "m2a", "", "", "", "");
        assertTrue(predicate.test(new PersonBuilder().withLessons("M2a").build()));

        // Multiple lessons match
        predicate = new ContactMatchesPredicate("", "", "", "M2a S3b", "", "", "", "");
        assertTrue(predicate.test(new PersonBuilder().withLessons("M2a", "S3b").build()));

        // Partial lesson match
        predicate = new ContactMatchesPredicate("", "", "", "M2a", "", "", "", "");
        assertTrue(predicate.test(new PersonBuilder().withLessons("M2a", "S3b").build()));

        // No lesson match
        predicate = new ContactMatchesPredicate("", "", "", "S3b", "", "", "", "");
        assertFalse(predicate.test(new PersonBuilder().withLessons("M2a").build()));
    }

    @Test
    public void test_phoneMatching() {
        // Exact phone match
        ContactMatchesPredicate predicate =
                new ContactMatchesPredicate("", "", "", "", "12345", "", "", "");
        assertTrue(predicate.test(new PersonBuilder().withPhone("12345").build()));

        // No phone match
        predicate = new ContactMatchesPredicate("", "", "", "", "67890", "", "", "");
        assertFalse(predicate.test(new PersonBuilder().withPhone("12345").build()));
    }

    @Test
    public void test_emailMatching() {
        // Exact email match
        ContactMatchesPredicate predicate =
                new ContactMatchesPredicate("", "", "", "", "", "alice@email.com", "", "");
        assertTrue(predicate.test(new PersonBuilder().withEmail("alice@email.com").build()));

        // No email match
        predicate = new ContactMatchesPredicate("", "", "", "", "", "bob@email.com", "", "");
        assertFalse(predicate.test(new PersonBuilder().withEmail("alice@email.com").build()));
    }

    @Test
    public void test_addressMatching() {
        // Exact address match
        ContactMatchesPredicate predicate =
                new ContactMatchesPredicate("", "", "", "", "", "", "Main Street", "");
        assertTrue(predicate.test(new PersonBuilder().withAddress("Main Street").build()));

        // Partial address match
        predicate = new ContactMatchesPredicate("", "", "", "", "", "", "Main", "");
        assertTrue(predicate.test(new PersonBuilder().withAddress("Main Street").build()));

        // Case insensitive address match
        predicate = new ContactMatchesPredicate("", "", "", "", "", "", "main street", "");
        assertTrue(predicate.test(new PersonBuilder().withAddress("Main Street").build()));

        // Multiple word address match
        predicate = new ContactMatchesPredicate("", "", "", "", "", "", "Main Street 123", "");
        assertTrue(predicate.test(new PersonBuilder().withAddress("Main Street 123").build()));

        // No address match
        predicate = new ContactMatchesPredicate("", "", "", "", "", "", "Second Street", "");
        assertFalse(predicate.test(new PersonBuilder().withAddress("Main Street").build()));
    }

    @Test
    public void test_tagMatching() {
        // Exact tag match
        ContactMatchesPredicate predicate =
                new ContactMatchesPredicate("", "", "", "", "", "", "", "friend");
        assertTrue(predicate.test(new PersonBuilder().withTags("friend").build()));

        // Case insensitive tag match
        predicate = new ContactMatchesPredicate("", "", "", "", "", "", "", "FRIEND");
        assertTrue(predicate.test(new PersonBuilder().withTags("friend").build()));

        // Multiple tags match
        predicate = new ContactMatchesPredicate("", "", "", "", "", "", "", "friend colleague");
        assertTrue(predicate.test(new PersonBuilder().withTags("friend", "colleague").build()));

        // Partial tag match
        predicate = new ContactMatchesPredicate("", "", "", "", "", "", "", "friend");
        assertTrue(predicate.test(new PersonBuilder().withTags("friend", "colleague").build()));

        // No tag match
        predicate = new ContactMatchesPredicate("", "", "", "", "", "", "", "family");
        assertFalse(predicate.test(new PersonBuilder().withTags("friend").build()));
    }

    @Test
    public void test_combinedMatching() {
        // Multiple field match
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

        // Partial field match (some fields match, some don't)
        predicate = new ContactMatchesPredicate("S0000001", "Bob", "student", "M2a", "12345",
                "alice@email.com", "Main Street", "friend");
        assertFalse(predicate.test(new PersonBuilder()
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
