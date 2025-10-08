package seedu.address.model.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class DayTest {

    @Test
    public void constructor_nullDay_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Day(null));
    }

    @Test
    public void constructor_invalidDay_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Day(""));
        assertThrows(IllegalArgumentException.class, () -> new Day(" "));
        assertThrows(IllegalArgumentException.class, () -> new Day("monday")); // lowercase
        assertThrows(IllegalArgumentException.class, () -> new Day("MONDAY")); // uppercase
        assertThrows(IllegalArgumentException.class, () -> new Day("Monday ")); // trailing space
        assertThrows(IllegalArgumentException.class, () -> new Day(" Monday")); // leading space
        assertThrows(IllegalArgumentException.class, () -> new Day("Mon")); // abbreviation
        assertThrows(IllegalArgumentException.class, () -> new Day("Monday1")); // with number
        assertThrows(IllegalArgumentException.class, () -> new Day("Monday-Tuesday")); // with hyphen
        assertThrows(IllegalArgumentException.class, () -> new Day("invalid")); // invalid day
    }

    @Test
    public void constructor_validDay_success() {
        // Test all valid days
        Day monday = new Day("Monday");
        assertEquals("Monday", monday.fullDay);

        Day tuesday = new Day("Tuesday");
        assertEquals("Tuesday", tuesday.fullDay);

        Day wednesday = new Day("Wednesday");
        assertEquals("Wednesday", wednesday.fullDay);

        Day thursday = new Day("Thursday");
        assertEquals("Thursday", thursday.fullDay);

        Day friday = new Day("Friday");
        assertEquals("Friday", friday.fullDay);

        Day saturday = new Day("Saturday");
        assertEquals("Saturday", saturday.fullDay);

        Day sunday = new Day("Sunday");
        assertEquals("Sunday", sunday.fullDay);
    }

    @Test
    public void isValidDay_validInputs_returnsTrue() {
        assertTrue(Day.isValidDay("Monday"));
        assertTrue(Day.isValidDay("Tuesday"));
        assertTrue(Day.isValidDay("Wednesday"));
        assertTrue(Day.isValidDay("Thursday"));
        assertTrue(Day.isValidDay("Friday"));
        assertTrue(Day.isValidDay("Saturday"));
        assertTrue(Day.isValidDay("Sunday"));
    }

    @Test
    public void isValidDay_invalidInputs_returnsFalse() {
        // Empty or whitespace
        assertFalse(Day.isValidDay(""));
        assertFalse(Day.isValidDay(" "));
        assertFalse(Day.isValidDay("  "));

        // Wrong case
        assertFalse(Day.isValidDay("monday")); // lowercase
        assertFalse(Day.isValidDay("MONDAY")); // uppercase
        assertFalse(Day.isValidDay("Monday ")); // trailing space
        assertFalse(Day.isValidDay(" Monday")); // leading space

        // Abbreviations
        assertFalse(Day.isValidDay("Mon"));
        assertFalse(Day.isValidDay("Tue"));
        assertFalse(Day.isValidDay("Wed"));

        // Invalid days
        assertFalse(Day.isValidDay("invalid"));
        assertFalse(Day.isValidDay("Monday1"));
        assertFalse(Day.isValidDay("Monday-Tuesday"));

        // Special characters
        assertFalse(Day.isValidDay("Monday!"));
        assertFalse(Day.isValidDay("Monday@"));
    }

    @Test
    public void toString_returnsCorrectString() {
        Day day = new Day("Monday");
        assertEquals("Monday", day.toString());
    }

    @Test
    public void equals_sameDay_returnsTrue() {
        Day day1 = new Day("Monday");
        Day day2 = new Day("Monday");

        assertTrue(day1.equals(day1)); // same object
        assertTrue(day1.equals(day2)); // same values
    }

    @Test
    public void equals_differentDay_returnsFalse() {
        Day day1 = new Day("Monday");
        Day day2 = new Day("Tuesday");

        assertFalse(day1.equals(day2)); // different values
        assertFalse(day1.equals(null)); // null
        assertFalse(day1.equals("Monday")); // different type
    }

    @Test
    public void hashCode_sameDay_sameHashCode() {
        Day day1 = new Day("Monday");
        Day day2 = new Day("Monday");

        assertEquals(day1.hashCode(), day2.hashCode());
    }

    @Test
    public void hashCode_differentDay_differentHashCode() {
        Day day1 = new Day("Monday");
        Day day2 = new Day("Tuesday");

        assertTrue(day1.hashCode() != day2.hashCode());
    }
}
