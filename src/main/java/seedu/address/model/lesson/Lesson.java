package seedu.address.model.lesson;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Name;
import seedu.address.model.tag.Tag;


/**
 * Represents a Lesson in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Lesson {

    // Identity fields
    private final ClassName className;
    private final Day day;
    private final Time time;

    // Data fields
    private final Tutor tutor;
    private final Set<Tag> tags = new HashSet<>();
    private final Set<Name> students = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Lesson(ClassName className, Day day, Time time, Tutor tutor, Set<Tag> tags, Set<Name> students) {
        requireAllNonNull(className, day, time, tutor, tags, students);
        this.className = className;
        this.day = day;
        this.time = time;
        this.tutor = tutor;
        this.tags.addAll(tags);
        this.students.addAll(students);
    }

    public Lesson(ClassName className, Day day, Time time, Tutor tutor, Set<Tag> tags) {
        this(className, day, time, tutor, tags, new HashSet<>());
    }

    public ClassName getClassName() {
        return className;
    }

    public Day getDay() {
        return day;
    }

    public Time getTime() {
        return time;
    }

    public Tutor getTutor() {
        return tutor;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public Set<Name> getAttendees() {
        return Collections.unmodifiableSet(students);
    }

    public boolean hasAttendee(Name studentName) {
        return students.contains(studentName);
    }

    public Lesson markAttendance(Name studentName) {
        Set<Name> updated = new HashSet<>(students);
        updated.add(studentName);
        return new Lesson(className, day, time, tutor, tags, updated);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSameLesson(Lesson otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getClassName().equals(getClassName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Lesson)) {
            return false;
        }

        Lesson otherLesson = (Lesson) other;
        return className.equals(otherLesson.className)
                && day.equals(otherLesson.day)
                && time.equals(otherLesson.time)
                && tutor.equals(otherLesson.tutor)
                && tags.equals(otherLesson.tags)
                && students.equals(otherLesson.students);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(className, day, time, tutor, tags, students);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("class", className)
                .add("day", day)
                .add("time", time)
                .add("tutor", tutor)
                .add("tags", tags)
                .add("students", students)
                .toString();
    }
}
