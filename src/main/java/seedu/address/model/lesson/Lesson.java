package seedu.address.model.lesson;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.IdentificationNumber;
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
    private final Set<IdentificationNumber> studentIds = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Lesson(ClassName className, Day day, Time time, Tutor tutor, Set<Tag> tags) {
        requireAllNonNull(className, day, time, tutor, tags);
        this.className = className;
        this.day = day;
        this.time = time;
        this.tutor = tutor;
        this.tags.addAll(tags);
    }

    /**
     * Every field must be present and not null.
     */
    public Lesson(ClassName className, Day day, Time time, Tutor tutor, Set<Tag> tags, Set<IdentificationNumber> studentIds) {
        requireAllNonNull(className, day, time, tutor, tags, studentIds);
        this.className = className;
        this.day = day;
        this.time = time;
        this.tutor = tutor;
        this.tags.addAll(tags);
        this.studentIds.addAll(studentIds);
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

    public Lesson markAttendance(IdentificationNumber id) {
        Set<IdentificationNumber> updated = new HashSet<>(studentIds);
        updated.add(id);
        return new Lesson(className, day, time, tutor, tags, updated);
    }

    /**
     * Returns an immutable set of student IDs, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<IdentificationNumber> getStudents() {
        return Collections.unmodifiableSet(studentIds);
    }

    /**
     * Returns true if both lessons have the same name.
     * This defines a weaker notion of equality between two lessons.
     */
    public boolean isSameLesson(Lesson otherLesson) {
        if (otherLesson == this) {
            return true;
        }

        return otherLesson != null
                && otherLesson.getClassName().equals(getClassName());
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
                && studentIds.equals(otherLesson.studentIds);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(className, day, time, tutor, tags, studentIds);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("class", className)
                .add("day", day)
                .add("time", time)
                .add("tutor", tutor)
                .add("tags", tags)
                .add("students", studentIds)
                .toString();
    }
}
