package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.lesson.ClassName;
import seedu.address.model.lesson.Day;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.lesson.Time;
import seedu.address.model.lesson.Tutor;
import seedu.address.model.tag.Tag;

/**
 * A utility class to help with building Lesson objects.
 */
public class LessonBuilder {

    public static final String DEFAULT_CLASS_NAME = "A1a";
    public static final String DEFAULT_DAY = "Monday";
    public static final String DEFAULT_TIME = "1200";
    public static final String DEFAULT_TUTOR = "A12345678";

    private ClassName className;
    private Day day;
    private Time time;
    private Tutor tutor;
    private Set<Tag> tags;

    /**
     * Creates a {@code LessonBuilder} with the default details.
     */
    public LessonBuilder() {
        className = new ClassName(DEFAULT_CLASS_NAME);
        day = new Day(DEFAULT_DAY);
        time = new Time(DEFAULT_TIME);
        tutor = new Tutor(DEFAULT_TUTOR);
        tags = new HashSet<>();
    }

    /**
     * Initializes the LessonBuilder with the data of {@code lessonToCopy}.
     */
    public LessonBuilder(Lesson lessonToCopy) {
        className = lessonToCopy.getClassName();
        day = lessonToCopy.getDay();
        time = lessonToCopy.getTime();
        tutor = lessonToCopy.getTutor();
        tags = new HashSet<>(lessonToCopy.getTags());
    }

    /**
     * Sets the {@code ClassName} of the {@code Lesson} that we are building.
     */
    public LessonBuilder withClassName(String className) {
        this.className = new ClassName(className);
        return this;
    }

    /**
     * Sets the {@code Day} of the {@code Lesson} that we are building.
     */
    public LessonBuilder withDay(String day) {
        this.day = new Day(day);
        return this;
    }

    /**
     * Sets the {@code Time} of the {@code Lesson} that we are building.
     */
    public LessonBuilder withTime(String time) {
        this.time = new Time(time);
        return this;
    }

    /**
     * Sets the {@code Tutor} of the {@code Lesson} that we are building.
     */
    public LessonBuilder withTutor(String tutor) {
        this.tutor = new Tutor(tutor);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Lesson} that we are building.
     */
    public LessonBuilder withTags(String... tags) {
        this.tags = new HashSet<>();
        for (String tag : tags) {
            this.tags.add(new Tag(tag));
        }
        return this;
    }

    public Lesson build() {
        return new Lesson(className, day, time, tutor, tags);
    }
}
