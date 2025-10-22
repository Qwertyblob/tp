package seedu.address.ui;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.IdentificationNumber;
import seedu.address.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label index;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private FlowPane roleAndLessons;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        this(person, displayedIndex, null);
    }

    /**
     * Creates a {@code PersonCode} with the given {@code Person}, index, and lesson list for attendance checking.
     */
    public PersonCard(Person person, int displayedIndex, ObservableList<Lesson> allLessons) {
        super(FXML);
        this.person = person;
        index.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        id.setText(person.getId().toString());

        Label roleLabel = new Label(person.getRole().role);
        roleLabel.getStyleClass().add("role_label"); // define style in CSS
        roleAndLessons.getChildren().add(roleLabel);

        // Lessons with weekly attendance-based coloring
        for (Lesson lesson : person.getLessons()) {
            Label lessonLabel = new Label(lesson.getClassName().fullClassName);

            // Check if student is present for this lesson this week (resets every Monday)
            if (allLessons != null && isStudentPresentForLesson(lesson, person.getId(), allLessons)) {
                lessonLabel.getStyleClass().add("lesson_label_present");
            } else {
                lessonLabel.getStyleClass().add("lesson_label");
            }

            roleAndLessons.getChildren().add(lessonLabel);
        }

        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    /**
     * Checks if the student is present for the given lesson this week.
     * Attendance resets every Monday at 00:00.
     */
    private boolean isStudentPresentForLesson(Lesson studentLesson, IdentificationNumber studentId,
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
