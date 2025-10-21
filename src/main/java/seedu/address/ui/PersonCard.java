package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.lesson.Lesson;
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
        super(FXML);
        this.person = person;
        index.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        id.setText(person.getId().toString());

        Label roleLabel = new Label(person.getRole().role);
        roleLabel.getStyleClass().add("role_label"); // define style in CSS
        roleAndLessons.getChildren().add(roleLabel);

        // Lessons (assuming person has a getLessons() method returning a list)
        for (Lesson lesson : person.getLessons()) {
            Label lessonLabel = new Label(lesson.getClassName().fullClassName);
            lessonLabel.getStyleClass().add("lesson_label");
            roleAndLessons.getChildren().add(lessonLabel);
        }

        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }
}
