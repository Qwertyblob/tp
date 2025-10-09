package seedu.address.model.lesson;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Lesson's day in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDay(String)}
 */
public class Day {

    public static final String MESSAGE_CONSTRAINTS =
            "Days should only contain alphabetical characters, and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "^(?i)(monday|tuesday|wednesday|thursday|friday|saturday|sunday)$";

    public final String fullDay;

    /**
     * Constructs a {@code Day}.
     *
     * @param day A valid day.
     */
    public Day(String day) {
        requireNonNull(day);
        checkArgument(isValidDay(day), MESSAGE_CONSTRAINTS);
        fullDay = day;
    }

    /**
     * Returns true if a given string is a valid day.
     */
    public static boolean isValidDay(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullDay;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Day)) {
            return false;
        }

        Day otherDay = (Day) other;
        return fullDay.equals(otherDay.fullDay);
    }

    @Override
    public int hashCode() {
        return fullDay.hashCode();
    }

}
