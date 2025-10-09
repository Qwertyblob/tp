package seedu.address.model.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.Tag;

public class LessonTest {

    @Test
    public void constructor_nullParameters_throwsNullPointerException() {
        ClassName className = new ClassName("A1a");
        Day day = new Day("Monday");
        Time time = new Time("1200");
        Tutor tutor = new Tutor("A1234567");
        Set<Tag> tags = new HashSet<>();

        assertThrows(NullPointerException.class, () -> new Lesson(null, day, time, tutor, tags));
        assertThrows(NullPointerException.class, () -> new Lesson(className, null, time, tutor, tags));
        assertThrows(NullPointerException.class, () -> new Lesson(className, day, null, tutor, tags));
        assertThrows(NullPointerException.class, () -> new Lesson(className, day, time, null, tags));
        assertThrows(NullPointerException.class, () -> new Lesson(className, day, time, tutor, null));
    }

    @Test
    public void constructor_validParameters_success() {
        ClassName className = new ClassName("A1a");
        Day day = new Day("Monday");
        Time time = new Time("1200");
        Tutor tutor = new Tutor("A1234567");
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("math"));

        Lesson lesson = new Lesson(className, day, time, tutor, tags);

        assertEquals(className, lesson.getClassName());
        assertEquals(day, lesson.getDay());
        assertEquals(time, lesson.getTime());
        assertEquals(tutor, lesson.getTutor());
        assertEquals(tags, lesson.getTags());
    }

    @Test
    public void constructor_emptyTags_success() {
        ClassName className = new ClassName("A1a");
        Day day = new Day("Monday");
        Time time = new Time("1200");
        Tutor tutor = new Tutor("A1234567");
        Set<Tag> tags = new HashSet<>();

        Lesson lesson = new Lesson(className, day, time, tutor, tags);

        assertTrue(lesson.getTags().isEmpty());
    }

    @Test
    public void constructor_multipleTags_success() {
        ClassName className = new ClassName("A1a");
        Day day = new Day("Monday");
        Time time = new Time("1200");
        Tutor tutor = new Tutor("A1234567");
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("math"));
        tags.add(new Tag("algebra"));

        Lesson lesson = new Lesson(className, day, time, tutor, tags);

        assertEquals(2, lesson.getTags().size());
        assertTrue(lesson.getTags().contains(new Tag("math")));
        assertTrue(lesson.getTags().contains(new Tag("algebra")));
    }

    @Test
    public void getTags_returnsImmutableSet() {
        ClassName className = new ClassName("A1a");
        Day day = new Day("Monday");
        Time time = new Time("1200");
        Tutor tutor = new Tutor("A1234567");
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("math"));

        Lesson lesson = new Lesson(className, day, time, tutor, tags);
        Set<Tag> returnedTags = lesson.getTags();

        assertThrows(UnsupportedOperationException.class, () -> returnedTags.add(new Tag("science")));
        assertThrows(UnsupportedOperationException.class, () -> returnedTags.remove(new Tag("math")));
        assertThrows(UnsupportedOperationException.class, () -> returnedTags.clear());
    }

    @Test
    public void isSameLesson_sameLesson_returnsTrue() {
        ClassName className = new ClassName("A1a");
        Day day = new Day("Monday");
        Time time = new Time("1200");
        Tutor tutor = new Tutor("T1234567");
        Set<Tag> tags = new HashSet<>();

        Lesson lesson1 = new Lesson(className, day, time, tutor, tags);
        Lesson lesson2 = new Lesson(className, new Day("Tuesday"), new Time("1300"),
                new Tutor("T1234568"), new HashSet<>());

        assertTrue(lesson1.isSameLesson(lesson1)); // same object
        assertTrue(lesson1.isSameLesson(lesson2)); // same class name
    }

    @Test
    public void isSameLesson_differentLesson_returnsFalse() {
        ClassName className1 = new ClassName("A1a");
        ClassName className2 = new ClassName("B2b");
        Day day = new Day("Monday");
        Time time = new Time("1200");
        Tutor tutor = new Tutor("T1234567");
        Set<Tag> tags = new HashSet<>();

        Lesson lesson1 = new Lesson(className1, day, time, tutor, tags);
        Lesson lesson2 = new Lesson(className2, day, time, tutor, tags);

        assertFalse(lesson1.isSameLesson(lesson2)); // different class name
        assertFalse(lesson1.isSameLesson(null)); // null
    }

    @Test
    public void equals_sameLesson_returnsTrue() {
        ClassName className = new ClassName("A1a");
        Day day = new Day("Monday");
        Time time = new Time("1200");
        Tutor tutor = new Tutor("T1234567");
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("math"));

        Lesson lesson1 = new Lesson(className, day, time, tutor, tags);
        Lesson lesson2 = new Lesson(className, day, time, tutor, tags);

        assertTrue(lesson1.equals(lesson1)); // same object
        assertTrue(lesson1.equals(lesson2)); // same values
    }

    @Test
    public void equals_differentLesson_returnsFalse() {
        ClassName className1 = new ClassName("A1a");
        ClassName className2 = new ClassName("B2b");
        Day day1 = new Day("Monday");
        Day day2 = new Day("Tuesday");
        Time time1 = new Time("1200");
        Time time2 = new Time("1300");
        Tutor tutor1 = new Tutor("T1234567");
        Tutor tutor2 = new Tutor("T1234568");
        Set<Tag> tags = new HashSet<>();

        Lesson lesson1 = new Lesson(className1, day1, time1, tutor1, tags);
        Lesson lesson2 = new Lesson(className2, day1, time1, tutor1, tags);
        Lesson lesson3 = new Lesson(className1, day2, time1, tutor1, tags);
        Lesson lesson4 = new Lesson(className1, day1, time2, tutor1, tags);
        Lesson lesson5 = new Lesson(className1, day1, time1, tutor2, tags);

        assertFalse(lesson1.equals(lesson2)); // different class name
        assertFalse(lesson1.equals(lesson3)); // different day
        assertFalse(lesson1.equals(lesson4)); // different time
        assertFalse(lesson1.equals(lesson5)); // different tutor
        assertFalse(lesson1.equals(null)); // null
        assertFalse(lesson1.equals("not a lesson")); // different type
    }

    @Test
    public void equals_differentTags_returnsFalse() {
        ClassName className = new ClassName("A1a");
        Day day = new Day("Monday");
        Time time = new Time("1200");
        Tutor tutor = new Tutor("T1234567");
        Set<Tag> tags1 = new HashSet<>();
        tags1.add(new Tag("math"));
        Set<Tag> tags2 = new HashSet<>();
        tags2.add(new Tag("science"));

        Lesson lesson1 = new Lesson(className, day, time, tutor, tags1);
        Lesson lesson2 = new Lesson(className, day, time, tutor, tags2);

        assertFalse(lesson1.equals(lesson2)); // different tags
    }

    @Test
    public void toString_containsAllFields() {
        ClassName className = new ClassName("A1a");
        Day day = new Day("Monday");
        Time time = new Time("1200");
        Tutor tutor = new Tutor("T1234567");
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("math"));

        Lesson lesson = new Lesson(className, day, time, tutor, tags);
        String toString = lesson.toString();

        assertTrue(toString.contains("A1a"));
        assertTrue(toString.contains("Monday"));
        assertTrue(toString.contains("1200"));
        assertTrue(toString.contains("T1234567"));
        assertTrue(toString.contains("math"));
    }
}
