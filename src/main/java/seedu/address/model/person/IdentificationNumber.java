package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents the unique Identification Number of a person.
 */
public class IdentificationNumber {

    public static final String MESSAGE_CONSTRAINTS =
            "Identification number must start with 'T' or 'S' followed by 8 digits.";
    public static final String VALIDATION_REGEX = "[TS]\\d{8}";

    private final String value;

    public IdentificationNumber(String value) {
        requireNonNull(value);
        if (!value.matches(VALIDATION_REGEX)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof IdentificationNumber
                && value.equals(((IdentificationNumber) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
