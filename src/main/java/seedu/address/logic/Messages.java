package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_INVALID_LESSON_DISPLAYED_INDEX = "The class index provided is invalid";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_LESSONS_LISTED_OVERVIEW = "%1$d classes listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String formatPerson(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Name: ")
                .append(person.getName())
                .append("; Role: ")
                .append(person.getRole())
                .append("; ID: ")
                .append(person.getId())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Email: ")
                .append(person.getEmail())
                .append("; Address: ")
                .append(person.getAddress())
                .append("; Tags: ");
        person.getTags().forEach(builder::append);
        return builder.toString();
    }

    /**
     * Formats the {@code person} in the shortened format for display to the user.
     */
    public static String shortenedFormatPerson(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; ID: ")
                .append(person.getId());
        return builder.toString();
    }

    /**
     * Formats the {@code lesson} for display to the user.
     */
    public static String formatLesson(Lesson lesson) {
        final StringBuilder builder = new StringBuilder();
        builder.append(lesson.getClassName())
                .append("; Day: ")
                .append(lesson.getDay())
                .append("; Time: ")
                .append(lesson.getTime())
                .append("; Tutor: ")
                .append(lesson.getTutor())
                .append("; Tags: ");
        lesson.getTags().forEach(builder::append);
        return builder.toString();
    }

    /**
     * Formats the {@code lesson} in the shortened format for display to the user.
     */
    public static String shortenedFormatLesson(Lesson lesson) {
        final StringBuilder builder = new StringBuilder();
        builder.append(lesson.getClassName());
        return builder.toString();
    }

}
