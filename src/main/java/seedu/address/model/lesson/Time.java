package seedu.address.model.lesson;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a Lesson's time in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidTime(String)}
 */
public class Time {

    public static final String MESSAGE_CONSTRAINTS =
            "Time should only contain numerical characters in 24 hour time format, "
                    + "with start and end times separated by a '-'";
    public static final String TIME_CONSTRAINTS = "Start time cannot be later than or equal to end time.";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX =
            "^(?<start>(?:[01]\\d|2[0-3])[0-5]\\d)\\s*-\\s*(?<end>(?:[01]\\d|2[0-3])[0-5]\\d)$";
    private static final Pattern TIME_PATTERN = Pattern.compile(VALIDATION_REGEX);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmm");

    public final String fullTime;
    public final LocalTime startTime;
    public final LocalTime endTime;

    /**
     * Constructs a {@code Time}.
     *
     * @param time A valid time.
     */
    public Time(String time) {
        requireNonNull(time);
        checkArgument(isValidDuration(time), TIME_CONSTRAINTS);
        checkArgument(isValidTime(time), MESSAGE_CONSTRAINTS);
        Matcher matcher = TIME_PATTERN.matcher(time);
        matcher.matches(); // Safe because isValidTime already checked

        this.startTime = LocalTime.parse(matcher.group("start"), TIME_FORMATTER);
        this.endTime = LocalTime.parse(matcher.group("end"), TIME_FORMATTER);
        this.fullTime = String.format("%s-%s", matcher.group("start"), matcher.group("end"));

    }

    /**
     * Returns true if a given string is a valid time.
     */
    public static boolean isValidTime(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if the end time is later than the start time.
     */
    public static boolean isValidDuration(String test) {
        Matcher matcher = TIME_PATTERN.matcher(test);

        if (matcher.matches()) {
            int start = Integer.parseInt(matcher.group("start"));
            int end = Integer.parseInt(matcher.group("end"));
            return end > start;
        }
        return false;
    }

    public LocalTime getStartTime() {
        return this.startTime;
    }

    public LocalTime getEndTime() {
        return this.endTime;
    }

    @Override
    public String toString() {
        String[] strings = fullTime.split("-");
        return strings[0] + " - " + strings[1];
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Time)) {
            return false;
        }

        Time otherDay = (Time) other;
        return fullTime.equals(otherDay.fullTime);
    }

    @Override
    public int hashCode() {
        return fullTime.hashCode();
    }

}
