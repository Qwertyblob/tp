package seedu.address.model.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ClassNameTest {

    @Test
    public void constructor_nullClassName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ClassName(null));
    }

    @Test
    public void constructor_invalidClassName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ClassName(""));
        assertThrows(IllegalArgumentException.class, () -> new ClassName(" "));
        assertThrows(IllegalArgumentException.class, () -> new ClassName("abc")); // lowercase
        assertThrows(IllegalArgumentException.class, () -> new ClassName("A1")); // missing lowercase
        assertThrows(IllegalArgumentException.class, () -> new ClassName("Aa")); // missing digit
        assertThrows(IllegalArgumentException.class, () -> new ClassName("A12a")); // too many digits
        assertThrows(IllegalArgumentException.class, () -> new ClassName("A1aa")); // too many lowercase
        assertThrows(IllegalArgumentException.class, () -> new ClassName("1Aa")); // starts with digit
        assertThrows(IllegalArgumentException.class, () -> new ClassName("A1a1")); // ends with digit
    }

    @Test
    public void constructor_validClassName_success() {
        // Test valid class names
        ClassName className1 = new ClassName("A1a");
        assertEquals("A1a", className1.fullClassName);

        ClassName className2 = new ClassName("B2b");
        assertEquals("B2b", className2.fullClassName);

        ClassName className3 = new ClassName("Z9z");
        assertEquals("Z9z", className3.fullClassName);
    }

    @Test
    public void isValidClassName_validInputs_returnsTrue() {
        assertTrue(ClassName.isValidClassName("A1a"));
        assertTrue(ClassName.isValidClassName("B2b"));
        assertTrue(ClassName.isValidClassName("C3c"));
        assertTrue(ClassName.isValidClassName("Z9z"));
    }

    @Test
    public void isValidClassName_invalidInputs_returnsFalse() {
        // Empty or whitespace
        assertFalse(ClassName.isValidClassName(""));
        assertFalse(ClassName.isValidClassName(" "));
        assertFalse(ClassName.isValidClassName("  "));

        // Wrong case
        assertFalse(ClassName.isValidClassName("a1a")); // all lowercase
        assertFalse(ClassName.isValidClassName("A1A")); // uppercase at end
        assertFalse(ClassName.isValidClassName("abc")); // all lowercase
        assertFalse(ClassName.isValidClassName("ABC")); // all uppercase

        // Wrong format
        assertFalse(ClassName.isValidClassName("A1")); // missing lowercase
        assertFalse(ClassName.isValidClassName("Aa")); // missing digit
        assertFalse(ClassName.isValidClassName("A12a")); // too many digits
        assertFalse(ClassName.isValidClassName("A1aa")); // too many lowercase
        assertFalse(ClassName.isValidClassName("1Aa")); // starts with digit
        assertFalse(ClassName.isValidClassName("A1a1")); // ends with digit

        // Special characters
        assertFalse(ClassName.isValidClassName("A1-a"));
        assertFalse(ClassName.isValidClassName("A1_a"));
        assertFalse(ClassName.isValidClassName("A1.a"));
    }

    @Test
    public void toString_returnsCorrectString() {
        ClassName className = new ClassName("A1a");
        assertEquals("A1a", className.toString());
    }

    @Test
    public void equals_sameClassName_returnsTrue() {
        ClassName className1 = new ClassName("A1a");
        ClassName className2 = new ClassName("A1a");

        assertTrue(className1.equals(className1)); // same object
        assertTrue(className1.equals(className2)); // same values
    }

    @Test
    public void equals_differentClassName_returnsFalse() {
        ClassName className1 = new ClassName("A1a");
        ClassName className2 = new ClassName("B2b");

        assertFalse(className1.equals(className2)); // different values
        assertFalse(className1.equals(null)); // null
        assertFalse(className1.equals("A1a")); // different type
    }

    @Test
    public void hashCode_sameClassName_sameHashCode() {
        ClassName className1 = new ClassName("A1a");
        ClassName className2 = new ClassName("A1a");

        assertEquals(className1.hashCode(), className2.hashCode());
    }

    @Test
    public void hashCode_differentClassName_differentHashCode() {
        ClassName className1 = new ClassName("A1a");
        ClassName className2 = new ClassName("B2b");

        assertTrue(className1.hashCode() != className2.hashCode());
    }
}
