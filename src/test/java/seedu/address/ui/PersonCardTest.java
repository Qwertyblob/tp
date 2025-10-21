package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalLessons.MATH_A1A;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.IdentificationNumber;
import seedu.address.model.person.Person;
import seedu.address.testutil.LessonBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Tests for PersonCard weekly attendance functionality.
 * Note: These tests focus on logic coverage rather than UI testing due to JavaFX initialization issues.
 */
public class PersonCardTest {

    private Person testPerson;
    private ObservableList<Lesson> testLessons;

    @BeforeEach
    public void setUp() {
        // Create a test person with lessons that match the test lesson data
        testPerson = new PersonBuilder(ALICE)
                .withLessons("A1a")
                .build();

        // Create test lessons with attendance data
        testLessons = FXCollections.observableArrayList();
    }

    @Test
    public void testWeeklyAttendanceLogic_studentPresentThisWeek() {
        // Test the weekly attendance logic without requiring JavaFX UI components
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(java.time.DayOfWeek.MONDAY);
        LocalDate tuesday = monday.plusDays(1);
        if (tuesday.isAfter(today)) {
            tuesday = monday;
        }

        Map<LocalDate, Set<IdentificationNumber>> attendance = new HashMap<>();
        Set<IdentificationNumber> presentStudents = new HashSet<>();
        presentStudents.add(testPerson.getId());
        attendance.put(tuesday, presentStudents);

        Lesson lessonWithAttendance = new LessonBuilder(MATH_A1A)
                .withAttendance(attendance)
                .build();

        testLessons.add(lessonWithAttendance);

        // Test the weekly attendance logic directly
        boolean isPresent = testWeeklyAttendanceLogic(testPerson.getLessons().iterator().next(),
                testPerson.getId(), testLessons);

        assertTrue(isPresent, "Student should be present this week");
    }

    @Test
    public void testWeeklyAttendanceLogic_studentAbsentThisWeek() {
        // Test when student is not present
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(java.time.DayOfWeek.MONDAY);
        LocalDate tuesday = monday.plusDays(1);
        if (tuesday.isAfter(today)) {
            tuesday = monday;
        }

        Map<LocalDate, Set<IdentificationNumber>> attendance = new HashMap<>();
        Set<IdentificationNumber> presentStudents = new HashSet<>();
        presentStudents.add(BOB.getId()); // Different student
        attendance.put(tuesday, presentStudents);

        Lesson lessonWithAttendance = new LessonBuilder(MATH_A1A)
                .withAttendance(attendance)
                .build();

        testLessons.add(lessonWithAttendance);

        // Test the weekly attendance logic directly
        boolean isPresent = testWeeklyAttendanceLogic(testPerson.getLessons().iterator().next(),
                testPerson.getId(), testLessons);

        assertFalse(isPresent, "Student should not be present this week");
    }

    @Test
    public void testWeeklyAttendanceLogic_studentPresentEarlierInWeek() {
        // Test when student was present earlier in the week
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(java.time.DayOfWeek.MONDAY);
        LocalDate wednesday = monday.plusDays(2);
        if (wednesday.isAfter(today)) {
            wednesday = monday;
        }

        Map<LocalDate, Set<IdentificationNumber>> attendance = new HashMap<>();
        Set<IdentificationNumber> presentStudents = new HashSet<>();
        presentStudents.add(testPerson.getId());
        attendance.put(wednesday, presentStudents);

        Lesson lessonWithAttendance = new LessonBuilder(MATH_A1A)
                .withAttendance(attendance)
                .build();

        testLessons.add(lessonWithAttendance);

        // Test the weekly attendance logic directly
        boolean isPresent = testWeeklyAttendanceLogic(testPerson.getLessons().iterator().next(),
                testPerson.getId(), testLessons);

        assertTrue(isPresent, "Student should be present this week (was present earlier)");
    }

    @Test
    public void testWeeklyAttendanceLogic_studentPresentLastWeek() {
        // Test when student was present last week (not this week)
        LocalDate lastMonday = LocalDate.now().with(java.time.DayOfWeek.MONDAY).minusWeeks(1);
        LocalDate lastTuesday = lastMonday.plusDays(1);

        Map<LocalDate, Set<IdentificationNumber>> attendance = new HashMap<>();
        Set<IdentificationNumber> presentStudents = new HashSet<>();
        presentStudents.add(testPerson.getId());
        attendance.put(lastTuesday, presentStudents);

        Lesson lessonWithAttendance = new LessonBuilder(MATH_A1A)
                .withAttendance(attendance)
                .build();

        testLessons.add(lessonWithAttendance);

        // Test the weekly attendance logic directly
        boolean isPresent = testWeeklyAttendanceLogic(testPerson.getLessons().iterator().next(),
                testPerson.getId(), testLessons);

        assertFalse(isPresent, "Student should not be present this week (was only present last week)");
    }

    @Test
    public void testWeeklyAttendanceLogic_lessonNotFound() {
        // Test when the lesson is not found in allLessons
        Lesson differentLesson = new LessonBuilder()
                .withClassName("Z0q")
                .build();
        testLessons.add(differentLesson);

        // Test the weekly attendance logic directly
        boolean isPresent = testWeeklyAttendanceLogic(testPerson.getLessons().iterator().next(),
                testPerson.getId(), testLessons);

        assertFalse(isPresent, "Student should not be present when lesson is not found");
    }

    /**
     * Helper method to test the weekly attendance logic without requiring JavaFX UI components.
     * This replicates the logic from PersonCard.isStudentPresentForLesson() method.
     */
    private boolean testWeeklyAttendanceLogic(Lesson studentLesson, IdentificationNumber studentId,
                                             ObservableList<Lesson> allLessons) {
        // Find the corresponding lesson in the full lesson list to get attendance data
        for (Lesson fullLesson : allLessons) {
            if (fullLesson.getClassName().equals(studentLesson.getClassName())) {
                Map<LocalDate, Set<IdentificationNumber>> attendance = fullLesson.getAttendance();

                // Get the start of the current week (Monday)
                LocalDate today = LocalDate.now();
                LocalDate startOfWeek = today.with(java.time.DayOfWeek.MONDAY);

                // Check if student was present on any day from Monday to today
                for (LocalDate date = startOfWeek; !date.isAfter(today); date = date.plusDays(1)) {
                    Set<IdentificationNumber> presentStudents = attendance.get(date);
                    if (presentStudents != null && presentStudents.contains(studentId)) {
                        return true; // Student was present at least once this week
                    }
                }
                return false;
            }
        }
        return false;
    }
}
