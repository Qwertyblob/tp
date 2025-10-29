package seedu.address.model.lesson;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Lesson's tutor in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidTutor(String)}
 */
public class Tutor {

    public static final String MESSAGE_CONSTRAINTS =
            "Tutors should start with the character 'T', followed by 7 digits, and should not be left blank.";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "^[Tt]\\d{7}$";

    public final String tutorName;

    /**
     * Constructs a {@code Tutor}.
     *
     * @param tutor A valid tutor name.
     */
    public Tutor(String tutor) {
        requireNonNull(tutor);
        checkArgument(isValidTutor(tutor), MESSAGE_CONSTRAINTS);
        String i = tutor.substring(0, 1).toUpperCase();
        this.tutorName = i + tutor.substring(1);
    }

    /**
     * Returns true if a given string is a valid tutor name.
     */
    public static boolean isValidTutor(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return tutorName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Tutor)) {
            return false;
        }

        Tutor otherDay = (Tutor) other;
        return tutorName.equals(otherDay.tutorName);
    }

    @Override
    public int hashCode() {
        return tutorName.hashCode();
    }

}
