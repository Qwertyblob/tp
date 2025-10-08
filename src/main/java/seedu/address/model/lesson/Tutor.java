package seedu.address.model.lesson;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Lesson's tutor in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidTutor(String)}
 */
public class Tutor {

    public static final String MESSAGE_CONSTRAINTS =
            "Tutors should only contain alphanumeric characters, and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "^[a-z]?=.*\\d{8}$";

    public final String tutorName;

    /**
     * Constructs a {@code Tutor}.
     *
     * @param tutor A valid tutor name.
     */
    public Tutor(String tutor) {
        requireNonNull(tutor);
        checkArgument(isValidTutor(tutor), MESSAGE_CONSTRAINTS);
        tutorName = tutor;
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
