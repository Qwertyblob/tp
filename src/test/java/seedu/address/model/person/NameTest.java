package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class NameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ClassName(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, () -> new ClassName(invalidName));
    }

    @Test
    public void isValidName() {
        // null name
        assertThrows(NullPointerException.class, () -> ClassName.isValidName(null));

        // invalid name
        assertFalse(ClassName.isValidName("")); // empty string
        assertFalse(ClassName.isValidName(" ")); // spaces only
        assertFalse(ClassName.isValidName("^")); // only non-alphanumeric characters
        assertFalse(ClassName.isValidName("peter*")); // contains non-alphanumeric characters

        // valid name
        assertTrue(ClassName.isValidName("peter jack")); // alphabets only
        assertTrue(ClassName.isValidName("12345")); // numbers only
        assertTrue(ClassName.isValidName("peter the 2nd")); // alphanumeric characters
        assertTrue(ClassName.isValidName("Capital Tan")); // with capital letters
        assertTrue(ClassName.isValidName("David Roger Jackson Ray Jr 2nd")); // long names
    }

    @Test
    public void equals() {
        ClassName name = new ClassName("Valid ClassName");

        // same values -> returns true
        assertTrue(name.equals(new ClassName("Valid ClassName")));

        // same object -> returns true
        assertTrue(name.equals(name));

        // null -> returns false
        assertFalse(name.equals(null));

        // different types -> returns false
        assertFalse(name.equals(5.0f));

        // different values -> returns false
        assertFalse(name.equals(new ClassName("Other Valid ClassName")));
    }
}
