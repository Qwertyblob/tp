package seedu.address.model.lesson;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Lesson's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidClassName(String)}
 */
public class ClassName {

    public static final String MESSAGE_CONSTRAINTS =
            "Class names should only contain alphanumeric characters in the formatPerson <A1a>, and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "^[A-Z]//d[a-z]";

    public final String fullClassName;

    /**
     * Constructs a {@code ClassName}.
     *
     * @param name A valid name.
     */
    public ClassName(String name) {
        requireNonNull(name);
        checkArgument(isValidClassName(name), MESSAGE_CONSTRAINTS);
        fullClassName = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidClassName(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullClassName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ClassName)) {
            return false;
        }

        ClassName otherName = (ClassName) other;
        return fullClassName.equals(otherName.fullClassName);
    }

    @Override
    public int hashCode() {
        return fullClassName.hashCode();
    }

}
