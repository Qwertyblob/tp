package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.model.Model;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.Person;
import seedu.address.testutil.LessonBuilder;
import seedu.address.testutil.TypicalLessons;

public class LessonCascadeUpdaterTest {

    private Model model;
    private Lesson oldLesson;
    private Lesson newLesson;
    private Person studentWithLesson;

    @BeforeEach
    public void setUp() {
        // Get a ModelManager with typical persons and lessons
        model = TypicalLessons.getTypicalModelManager();

        // Pick a lesson with at least one enrolled student
        oldLesson = TypicalLessons.CHINESE_D6F;

        // Create a new lesson with a new class name but same student IDs
        newLesson = new LessonBuilder()
                .withClassName("D6c") // new class name
                .withDay(oldLesson.getDay().toString())
                .withTime(oldLesson.getTime().toString())
                .withTutor(oldLesson.getTutor().toString())
                .withStudents(oldLesson.getStudents().stream().map(Object::toString).toArray(String[]::new))
                .withTags(oldLesson.getTags().stream().map(tag -> tag.tagName).toArray(String[]::new))
                .build();

        // Grab a student that is enrolled in oldLesson
        ObservableList<Person> persons = model.getAddressBook().getPersonList();
        studentWithLesson = persons.stream()
                .filter(p -> p.getLessons().contains(oldLesson))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No student found enrolled in oldLesson"));
    }

    @Test
    public void updateStudentsWithEditedLesson_success() {
        // Update students
        LessonCascadeUpdater.updateStudentsWithEditedLesson(model, oldLesson, newLesson);

        // Verify student now references newLesson
        ObservableList<Person> updatedPersons = model.getAddressBook().getPersonList();
        for (Person p : updatedPersons) {
            if (p.getId().equals(studentWithLesson.getId())) {
                assertTrue(p.getLessons().contains(newLesson), "Student should now reference new lesson");
                assertEquals(1, p.getLessons().size(), "Student should have exactly one lesson after update");
            }
        }
    }

    @Test
    public void updateStudentsWithEditedLesson_noChangeForUnenrolledStudents() {
        // Create a lesson that no one is enrolled in
        Lesson unrelatedLesson = new LessonBuilder()
                .withClassName("X1z")
                .withDay("sunday")
                .withTime("0900-1100")
                .withTutor("t9999999")
                .withTags("dummy")
                .build();

        // Run update — should not affect any students
        LessonCascadeUpdater.updateStudentsWithEditedLesson(model, unrelatedLesson, newLesson);

        ObservableList<Person> updatedPersons = model.getAddressBook().getPersonList();
        for (Person p : updatedPersons) {
            // Previously enrolled students should still reference oldLesson
            if (p.getId().equals(studentWithLesson.getId())) {
                assertTrue(p.getLessons().contains(oldLesson), "Student should still reference old lesson");
            }
        }
    }
}
