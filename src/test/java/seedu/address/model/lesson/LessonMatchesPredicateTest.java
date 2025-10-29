package seedu.address.model.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.LessonBuilder;

public class LessonMatchesPredicateTest {

    @Test
    public void equals() {
        LessonMatchesPredicate firstPredicate =
                new LessonMatchesPredicate("A1a", "Monday", "1000-1200", "T1234567", "Math");
        LessonMatchesPredicate secondPredicate =
                new LessonMatchesPredicate("B2b", "Tuesday", "1400-1600", "T1234567", "Science");

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        LessonMatchesPredicate firstPredicateCopy =
                new LessonMatchesPredicate("A1a", "Monday", "1000-1200", "T1234567", "Math");
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
        LessonMatchesPredicate predicate =
                new LessonMatchesPredicate("A1a", "Monday", "1000-1200", "T1234567", "Math");
        assertTrue(predicate.test(new LessonBuilder()
                .withClassName("A1a")
                .withDay("Monday")
                .withTime("1000-1200")
                .withTutor("T1234567")
                .withTags("Math")
                .build()));
    }

    @Test
    public void test_partialFieldsMatch_returnsTrue() {
        // Only class name and tag
        LessonMatchesPredicate predicate =
                new LessonMatchesPredicate("A1a", "", "", "", "Math");
        assertTrue(predicate.test(new LessonBuilder()
                .withClassName("A1a")
                .withDay("Friday")
                .withTime("1400-1600")
                .withTutor("T1234567")
                .withTags("Math", "Calculus")
                .build()));

        // Only tutor
        predicate = new LessonMatchesPredicate("", "", "", "T1234567", "");
        assertTrue(predicate.test(new LessonBuilder()
                .withClassName("D4d")
                .withDay("Wednesday")
                .withTime("0900-1100")
                .withTutor("T1234567")
                .build()));
    }

    @Test
    public void test_noFieldsMatch_returnsFalse() {
        LessonMatchesPredicate predicate =
                new LessonMatchesPredicate("B2b", "Monday", "1200-1400", "T1234567", "Science");
        assertFalse(predicate.test(new LessonBuilder()
                .withClassName("A1a")
                .withDay("Friday")
                .withTime("1000-1200")
                .withTutor("T7654321")
                .withTags("Math")
                .build()));
    }

    @Test
    public void test_allFieldsEmpty_returnsFalse() {
        LessonMatchesPredicate predicate =
                new LessonMatchesPredicate("", "", "", "", "");
        assertFalse(predicate.test(new LessonBuilder().build()));
    }

    @Test
    public void test_tagMatching_logic() {
        // Single matching tag
        LessonMatchesPredicate predicate =
                new LessonMatchesPredicate("", "", "", "", "tag1");
        assertTrue(predicate.test(new LessonBuilder().withTags("tag1", "tag2").build()));

        // Non-matching tag
        predicate = new LessonMatchesPredicate("", "", "", "", "random");
        assertFalse(predicate.test(new LessonBuilder().withTags("tag1", "tag2").build()));

        // Multiple search tags (all must match)
        predicate = new LessonMatchesPredicate("", "", "", "", "tag1 tag2");
        assertTrue(predicate.test(new LessonBuilder().withTags("tag1", "tag2", "tag3").build()));

        predicate = new LessonMatchesPredicate("", "", "", "", "tag1 tag4");
        assertFalse(predicate.test(new LessonBuilder().withTags("tag1", "tag2").build()));
    }

    @Test
    public void test_caseInsensitiveMatching_returnsTrue() {
        LessonMatchesPredicate predicate =
                new LessonMatchesPredicate("a1a", "monday", "1000-1200", "t1234567", "Math");
        assertTrue(predicate.test(new LessonBuilder()
                .withClassName("A1a")
                .withDay("Monday")
                .withTime("1000-1200")
                .withTutor("T1234567")
                .withTags("MATH")
                .build()));
    }

    @Test
    public void toStringMethod() {
        LessonMatchesPredicate predicate =
                new LessonMatchesPredicate("A1a", "Monday", "1000-1200", "T1234567", "Math");
        String expected = LessonMatchesPredicate.class.getCanonicalName()
                + "{className=A1a, day=Monday, time=1000-1200, tutor=T1234567, tags=Math}";
        assertEquals(expected, predicate.toString());
    }
}
