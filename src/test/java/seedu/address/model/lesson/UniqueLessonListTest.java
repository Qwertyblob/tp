package seedu.address.model.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.lesson.exceptions.DuplicateLessonException;
import seedu.address.model.lesson.exceptions.LessonNotFoundException;
import seedu.address.testutil.LessonBuilder;

/**
 * Unit tests for UniqueLessonList.
 */
public class UniqueLessonListTest {

    private UniqueLessonList uniqueLessonList;
    private Lesson mathLesson;
    private Lesson physicsLesson;

    @BeforeEach
    public void setUp() {
        uniqueLessonList = new UniqueLessonList();
        mathLesson = new LessonBuilder().withClassName("A1a").withDay("monday").build();
        physicsLesson = new LessonBuilder().withClassName("B2b").withDay("tuesday").build();
    }

    @Test
    public void add_newLesson_success() {
        uniqueLessonList.add(mathLesson);
        assertTrue(uniqueLessonList.contains(mathLesson));
    }

    @Test
    public void add_duplicateLesson_throwsDuplicateLessonException() {
        uniqueLessonList.add(mathLesson);
        assertThrows(DuplicateLessonException.class, () -> uniqueLessonList.add(mathLesson));
    }

    @Test
    public void remove_existingLesson_success() {
        uniqueLessonList.add(mathLesson);
        uniqueLessonList.remove(mathLesson);
        assertFalse(uniqueLessonList.contains(mathLesson));
    }

    @Test
    public void remove_nonExistingLesson_throwsLessonNotFoundException() {
        assertThrows(LessonNotFoundException.class, () -> uniqueLessonList.remove(mathLesson));
    }

    @Test
    public void setLessons_withValidList_replacesData() {
        uniqueLessonList.add(mathLesson);
        uniqueLessonList.setLessons(List.of(physicsLesson));

        assertFalse(uniqueLessonList.contains(mathLesson));
        assertTrue(uniqueLessonList.contains(physicsLesson));
    }

    @Test
    public void setLessons_withDuplicateLessons_throwsDuplicateLessonException() {
        List<Lesson> duplicates = List.of(mathLesson, mathLesson);
        assertThrows(DuplicateLessonException.class, () -> uniqueLessonList.setLessons(duplicates));
    }

    @Test
    public void equals_sameContents_returnsTrue() {
        UniqueLessonList anotherList = new UniqueLessonList();
        uniqueLessonList.add(mathLesson);
        anotherList.add(mathLesson);
        assertEquals(uniqueLessonList, anotherList);
    }
}
