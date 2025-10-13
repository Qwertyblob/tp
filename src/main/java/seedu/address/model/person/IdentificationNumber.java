package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents the unique Identification Number of a person.
 */
public class IdentificationNumber {

    public static final String MESSAGE_CONSTRAINTS =
            "Identification number must start with 'T' or 'S' followed by an 8 digit number";
    public static final String VALIDATION_REGEX = "[TS]\\d{8}$";

    private final String value;
    private final String prefix;
    private final int number;

    /**
     * Constructor for Identification Number
     * @param prefix Prefix to determine type of person
     * @param number Number of ID
     */
    public IdentificationNumber(String prefix, int number) {
        requireNonNull(prefix);
        if (!prefix.equals("T") && !prefix.equals("S")) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        if (number < 0 || number > 99999999) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }

        this.prefix = prefix;
        this.number = number;
        this.value = String.format("%s%08d", prefix, number);
    }

    /**
     * Constructor for Identification Number
     * @param value String value of ID
     */
    public IdentificationNumber(String value) {
        requireNonNull(value);
        if (!value.matches(VALIDATION_REGEX)) {
            throw new IllegalArgumentException("Invalid identification number format");
        }

        this.value = value;
        this.prefix = value.substring(0, 1);
        this.number = Integer.parseInt(value.substring(1));
    }

    /**
     * Returns boolean if ID provided is valid
     * @param test String to be tested
     * @return boolean result
     */
    public static boolean isValidId(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns boolean if ID provided is valid
     * @param testPrefix Prefix to be tested
     * @param testNumber Number to be tested
     * @return boolean result
     */
    public static boolean isValidId(String testPrefix, int testNumber) {
        if (!testPrefix.equals("T") && !testPrefix.equals("S")) {
            return false;
        }
        return testNumber >= 0 && testNumber <= 99999999;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public int getNumericValue() {
        return this.number;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof IdentificationNumber)) {
            return false;
        }

        IdentificationNumber otherId = (IdentificationNumber) other;
        return value.equals(otherId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
