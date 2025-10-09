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
        assertThrows(IllegalArgumentException.class, () -> new Tutor("123456")); // too short
        assertThrows(IllegalArgumentException.class, () -> new Tutor("12345678")); // too long
        assertThrows(IllegalArgumentException.class, () -> new Tutor("abc12345")); // letters before digits
        assertThrows(IllegalArgumentException.class, () -> new Tutor("1234567a")); // letters after digits
        assertThrows(IllegalArgumentException.class, () -> new Tutor("1234567")); // no letter prefix
    }

    @Test
    public void constructor_validTutor_success() {
        // Test valid tutor IDs
        Tutor tutor1 = new Tutor("T1234567");
        assertEquals("T1234567", tutor1.tutorName);

        Tutor tutor2 = new Tutor("T7654321");
        assertEquals("T7654321", tutor2.tutorName);

        Tutor tutor3 = new Tutor("T0000000");
        assertEquals("T0000000", tutor3.tutorName);
    }

    @Test
    public void isValidTutor_validInputs_returnsTrue() {
        assertTrue(Tutor.isValidTutor("T1234567"));
        assertTrue(Tutor.isValidTutor("T7654321"));
        assertTrue(Tutor.isValidTutor("T0000000"));
        assertTrue(Tutor.isValidTutor("t1234567")); // lowercase letter
    }

    @Test
    public void isValidTutor_invalidInputs_returnsFalse() {
        // Empty or whitespace
        assertFalse(Tutor.isValidTutor(""));
        assertFalse(Tutor.isValidTutor(" "));
        assertFalse(Tutor.isValidTutor("  "));

        // Wrong length
        assertFalse(Tutor.isValidTutor("t123456")); // too short
        assertFalse(Tutor.isValidTutor("t12345678")); // too long
        assertFalse(Tutor.isValidTutor("t123456789")); // too long

        // No letter prefix
        assertFalse(Tutor.isValidTutor("1234567"));

        // Wrong format
        assertFalse(Tutor.isValidTutor("abc12345")); // letters before digits
        assertFalse(Tutor.isValidTutor("1234567a")); // letters after digits
        assertFalse(Tutor.isValidTutor("A123456a")); // letters after digits
        assertFalse(Tutor.isValidTutor("AB123456")); // multiple letters

        // Special characters
        assertFalse(Tutor.isValidTutor("A1234567-"));
        assertFalse(Tutor.isValidTutor("A1234567_"));
        assertFalse(Tutor.isValidTutor("A1234567."));
    }

    @Test
    public void toString_returnsCorrectString() {
        Tutor tutor = new Tutor("A1234567");
        assertEquals("A1234567", tutor.toString());
    }

    @Test
    public void equals_sameTutor_returnsTrue() {
        Tutor tutor1 = new Tutor("T1234567");
        Tutor tutor2 = new Tutor("T1234567");

        assertTrue(tutor1.equals(tutor1)); // same object
        assertTrue(tutor1.equals(tutor2)); // same values
    }

    @Test
    public void equals_differentTutor_returnsFalse() {
        Tutor tutor1 = new Tutor("T1234567");
        Tutor tutor2 = new Tutor("T1234568");

        assertFalse(tutor1.equals(tutor2)); // different values
        assertFalse(tutor1.equals(null)); // null
        assertFalse(tutor1.equals("T1234567")); // different type
    }

    @Test
    public void hashCode_sameTutor_sameHashCode() {
        Tutor tutor1 = new Tutor("T1234567");
        Tutor tutor2 = new Tutor("T1234567");

        assertEquals(tutor1.hashCode(), tutor2.hashCode());
    }

    @Test
    public void hashCode_differentTutor_differentHashCode() {
        Tutor tutor1 = new Tutor("T1234567");
        Tutor tutor2 = new Tutor("T1234568");

        assertTrue(tutor1.hashCode() != tutor2.hashCode());
    }
}
