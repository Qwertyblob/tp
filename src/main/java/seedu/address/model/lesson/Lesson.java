package seedu.address.model.lesson;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

public class Lesson {

    // Identity fields
    private final ClassName className;
    private final Day day;
    private final Time time;

    // Data fields
    private final Tutor tutor;
    private final Set<Tag> tags = new HashSet<>();

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

        Lesson otherPerson = (Lesson) other;
        return className.equals(otherPerson.className)
                && day.equals(otherPerson.day)
                && time.equals(otherPerson.time)
                && tutor.equals(otherPerson.tutor)
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(className, day, time, tutor, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("class", className)
                .add("day", day)
                .add("time", time)
                .add("tutor", tutor)
                .add("tags", tags)
                .toString();
    }
}
