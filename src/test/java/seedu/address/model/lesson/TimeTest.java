package seedu.address.model.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TimeTest {

    @Test
    public void constructor_nullTime_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Time(null));
    }

    @Test
    public void constructor_invalidTime_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Time(""));
        assertThrows(IllegalArgumentException.class, () -> new Time(" "));
        assertFalse(Time.isValidTime("abc")); // letters
        assertFalse(Time.isValidTime("12:00")); // colon
        assertFalse(Time.isValidTime("12-00")); // hyphen
        assertFalse(Time.isValidTime("12.00")); // dot
        assertFalse(Time.isValidTime("12 00")); // space
        assertFalse(Time.isValidTime("+1200")); // plus sign
        assertFalse(Time.isValidTime("-1200")); // minus sign
    }

    @Test
    public void constructor_validTime_success() {
        // Test valid times
        Time time1 = new Time("1200");
        assertEquals("1200", time1.fullTime);

        Time time2 = new Time("0900");
        assertEquals("0900", time2.fullTime);

        Time time3 = new Time("2359");
        assertEquals("2359", time3.fullTime);

        Time time4 = new Time("0000");
        assertEquals("0000", time4.fullTime);
    }

    @Test
    public void isValidTime_validInputs_returnsTrue() {
        assertTrue(Time.isValidTime("1200"));
        assertTrue(Time.isValidTime("0900"));
        assertTrue(Time.isValidTime("2359"));
        assertTrue(Time.isValidTime("0000"));
        assertTrue(Time.isValidTime("1"));
        assertTrue(Time.isValidTime("12"));
        assertTrue(Time.isValidTime("123"));
        assertTrue(Time.isValidTime("12345"));
    }

    @Test
    public void isValidTime_invalidInputs_returnsFalse() {
        // Empty or whitespace
        assertFalse(Time.isValidTime(""));
        assertFalse(Time.isValidTime(" "));
        assertFalse(Time.isValidTime("  "));

        // Non-numeric characters
        assertFalse(Time.isValidTime("abc"));
        assertFalse(Time.isValidTime("12a"));
        assertFalse(Time.isValidTime("a12"));

        // Special characters
        assertFalse(Time.isValidTime("12:00"));
        assertFalse(Time.isValidTime("12-00"));
        assertFalse(Time.isValidTime("12.00"));
        assertFalse(Time.isValidTime("12 00"));
        assertFalse(Time.isValidTime("+1200"));
        assertFalse(Time.isValidTime("-1200"));

        // Mixed valid and invalid
        assertFalse(Time.isValidTime("12a0"));
        assertFalse(Time.isValidTime("1a00"));
    }

    @Test
    public void toString_returnsCorrectString() {
        Time time = new Time("1200");
        assertEquals("1200", time.toString());
    }

    @Test
    public void equals_sameTime_returnsTrue() {
        Time time1 = new Time("1200");
        Time time2 = new Time("1200");

        assertTrue(time1.equals(time1)); // same object
        assertTrue(time1.equals(time2)); // same values
    }

    @Test
    public void equals_differentTime_returnsFalse() {
        Time time1 = new Time("1200");
        Time time2 = new Time("1300");

        assertFalse(time1.equals(time2)); // different values
        assertFalse(time1.equals(null)); // null
        assertFalse(time1.equals("1200")); // different type
    }

    @Test
    public void hashCode_sameTime_sameHashCode() {
        Time time1 = new Time("1200");
        Time time2 = new Time("1200");

        assertEquals(time1.hashCode(), time2.hashCode());
    }

    @Test
    public void hashCode_differentTime_differentHashCode() {
        Time time1 = new Time("1200");
        Time time2 = new Time("1300");

        assertTrue(time1.hashCode() != time2.hashCode());
    }
}
