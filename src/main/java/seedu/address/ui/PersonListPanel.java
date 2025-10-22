package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.Person;

/**
 * Panel containing the list of persons.
 */
public class PersonListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);

    @FXML
    private ListView<Person> personListView;

    private ObservableList<Lesson> allLessons;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public PersonListPanel(ObservableList<Person> personList) {
        this(personList, null);
    }

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList} and lesson list for attendance checking.
     */
    public PersonListPanel(ObservableList<Person> personList, ObservableList<Lesson> allLessons) {
        super(FXML);
        this.allLessons = allLessons;
        personListView.setItems(personList);
        personListView.setCellFactory(listView -> new PersonListViewCell());

        // Add listener to refresh the person list when lessons change
        if (allLessons != null) {
            allLessons.addListener((javafx.collections.ListChangeListener.Change<? extends Lesson> change) -> {
                // Refresh the person list view to update attendance colors
                personListView.refresh();
            });
        }
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class PersonListViewCell extends ListCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PersonCard(person, getIndex() + 1, allLessons).getRoot());
            }
        }
    }

}
