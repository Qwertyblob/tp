package seedu.address.model.lesson;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

public class Tutor {

    public static final String MESSAGE_CONSTRAINTS =
            "Tutors should only contain alphabet characters, and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "^(Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday)$";

    public final String tutorName;

    /**
     * Constructs a {@code Day}.
     *
     * @param tutor A valid name.
     */
    public Tutor(String tutor) {
        requireNonNull(tutor);
        checkArgument(isValidTutor(tutor), MESSAGE_CONSTRAINTS);
        tutorName = tutor;
    }

    /**
     * Returns true if a given string is a valid day.
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
