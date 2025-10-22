package seedu.address.model.lesson;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests whether a {@code Lesson} matches all the specified search criteria.
 */
public class LessonMatchesPredicate implements Predicate<Lesson> {
    private final String className;
    private final String day;
    private final String time;
    private final String tutor;
    private final String tags;

    /**
     * Creates a LessonMatchesPredicate with the given information of a lesson.
     */
    public LessonMatchesPredicate(String className, String day, String time, String tutor, String tags) {
        this.className = className;
        this.day = day;
        this.time = time;
        this.tutor = tutor;
        this.tags = tags;
    }

    /**
     * Tests whether the given {@code Lesson} satisfies all specified criteria.
     * Any empty search fields are ignored.
     *
     * @param lesson The {@code Lesson} to test.
     * @return {@code true} if the lesson matches all non-empty criteria, {@code false} otherwise.
     */
    @Override
    public boolean test(Lesson lesson) {
        if (className.isEmpty() && day.isEmpty() && time.isEmpty() && tutor.isEmpty() && tags.isEmpty()) {
            return false;
        }

        boolean matches = true;

        if (!className.isEmpty()) {
            matches &= StringUtil.containsWordIgnoreCase(lesson.getClassName().fullClassName, className);
        }
        if (!day.isEmpty()) {
            matches &= StringUtil.containsWordIgnoreCase(lesson.getDay().fullDay, day);
        }
        if (!time.isEmpty()) {
            matches &= StringUtil.containsWordIgnoreCase(lesson.getTime().fullTime, time);
        }
        if (!tutor.isEmpty()) {
            matches &= StringUtil.containsWordIgnoreCase(lesson.getTutor().tutorName, tutor);
        }
        if (!tags.isEmpty()) {
            Set<String> personTags = lesson.getTags().stream()
                    .map(tag -> tag.tagName.toLowerCase())
                    .collect(Collectors.toSet());
            String[] searchTags = tags.toLowerCase().trim().split("\\s+");
            for (String searchTag : searchTags) {
                if (!personTags.contains(searchTag)) {
                    matches = false;
                    break;
                }
            }
        }
        return matches;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof LessonMatchesPredicate)) {
            return false;
        }

        LessonMatchesPredicate otherLessonMatchesPredicate = (LessonMatchesPredicate) other;
        return className.equals(otherLessonMatchesPredicate.className)
                && day.equals(otherLessonMatchesPredicate.day)
                && time.equals(otherLessonMatchesPredicate.time)
                && tutor.equals(otherLessonMatchesPredicate.tutor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("className", className)
                .add("day", day)
                .add("time", time)
                .add("tutor", tutor)
                .add("tags", tags)
                .toString();
    }
}
