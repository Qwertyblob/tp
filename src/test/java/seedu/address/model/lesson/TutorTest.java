package seedu.address.model.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TutorTest {

    @Test
    public void constructor_nullTutor_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tutor(null));
    }

    @Test
    public void constructor_invalidTutor_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Tutor(""));
        assertThrows(IllegalArgumentException.class, () -> new Tutor(" "));
        assertThrows(IllegalArgumentException.class, () -> new Tutor("1234567")); // too short
        assertThrows(IllegalArgumentException.class, () -> new Tutor("123456789")); // too long
        assertThrows(IllegalArgumentException.class, () -> new Tutor("abc12345")); // letters before digits
        assertThrows(IllegalArgumentException.class, () -> new Tutor("1234567a")); // letters after digits
        assertThrows(IllegalArgumentException.class, () -> new Tutor("12345678")); // no letter prefix
    }

    @Test
    public void constructor_validTutor_success() {
        // Test valid tutor IDs
        Tutor tutor1 = new Tutor("A12345678");
        assertEquals("A12345678", tutor1.tutorName);

        Tutor tutor2 = new Tutor("B87654321");
        assertEquals("B87654321", tutor2.tutorName);

        Tutor tutor3 = new Tutor("Z00000000");
        assertEquals("Z00000000", tutor3.tutorName);
    }

    @Test
    public void isValidTutor_validInputs_returnsTrue() {
        assertTrue(Tutor.isValidTutor("A12345678"));
        assertTrue(Tutor.isValidTutor("B87654321"));
        assertTrue(Tutor.isValidTutor("Z00000000"));
        assertTrue(Tutor.isValidTutor("a12345678")); // lowercase letter
    }

    @Test
    public void isValidTutor_invalidInputs_returnsFalse() {
        // Empty or whitespace
        assertFalse(Tutor.isValidTutor(""));
        assertFalse(Tutor.isValidTutor(" "));
        assertFalse(Tutor.isValidTutor("  "));

        // Wrong length
        assertFalse(Tutor.isValidTutor("1234567")); // too short
        assertFalse(Tutor.isValidTutor("123456789")); // too long
        assertFalse(Tutor.isValidTutor("1234567890")); // too long

        // No letter prefix
        assertFalse(Tutor.isValidTutor("12345678"));

        // Wrong format
        assertFalse(Tutor.isValidTutor("abc12345")); // letters before digits
        assertFalse(Tutor.isValidTutor("1234567a")); // letters after digits
        assertFalse(Tutor.isValidTutor("A1234567a")); // letters after digits
        assertFalse(Tutor.isValidTutor("AB1234567")); // multiple letters

        // Special characters
        assertFalse(Tutor.isValidTutor("A1234567-"));
        assertFalse(Tutor.isValidTutor("A1234567_"));
        assertFalse(Tutor.isValidTutor("A1234567."));
    }

    @Test
    public void toString_returnsCorrectString() {
        Tutor tutor = new Tutor("A12345678");
        assertEquals("A12345678", tutor.toString());
    }

    @Test
    public void equals_sameTutor_returnsTrue() {
        Tutor tutor1 = new Tutor("A12345678");
        Tutor tutor2 = new Tutor("A12345678");

        assertTrue(tutor1.equals(tutor1)); // same object
        assertTrue(tutor1.equals(tutor2)); // same values
    }

    @Test
    public void equals_differentTutor_returnsFalse() {
        Tutor tutor1 = new Tutor("A12345678");
        Tutor tutor2 = new Tutor("B12345678");

        assertFalse(tutor1.equals(tutor2)); // different values
        assertFalse(tutor1.equals(null)); // null
        assertFalse(tutor1.equals("A12345678")); // different type
    }

    @Test
    public void hashCode_sameTutor_sameHashCode() {
        Tutor tutor1 = new Tutor("A12345678");
        Tutor tutor2 = new Tutor("A12345678");

        assertEquals(tutor1.hashCode(), tutor2.hashCode());
    }

    @Test
    public void hashCode_differentTutor_differentHashCode() {
        Tutor tutor1 = new Tutor("A12345678");
        Tutor tutor2 = new Tutor("B12345678");

        assertTrue(tutor1.hashCode() != tutor2.hashCode());
    }
}
