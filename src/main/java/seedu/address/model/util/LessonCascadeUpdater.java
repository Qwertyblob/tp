package seedu.address.model.util;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.collections.ObservableList;
import seedu.address.model.Model;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.Person;

/**
 * Handles synchronization between lessons and students enrolled in them.
 * Used to ensure consistency when a lesson is edited or deleted.
 */
public class LessonCascadeUpdater {

    /**
     * Updates all students who were enrolled in {@code oldLesson}
     * so they now reference {@code newLesson}.
     */
    public static void updateStudentsWithEditedLesson(Model model, Lesson oldLesson, Lesson newLesson) {
        requireNonNull(model);
        requireNonNull(oldLesson);
        requireNonNull(newLesson);

        ObservableList<Person> persons = model.getAddressBook().getPersonList();
        List<Person> personsToUpdate = new ArrayList<>();

        for (Person p : persons) {
            // check if this student was enrolled in oldLesson
            if (p.getLessons().contains(oldLesson)) {
                Set<Lesson> updatedLessons = new HashSet<>(p.getLessons());
                updatedLessons.remove(oldLesson);
                updatedLessons.add(newLesson);

                Person updatedPerson = new Person(
                        p.getId(),
                        p.getName(),
                        p.getRole(),
                        updatedLessons,
                        p.getPhone(),
                        p.getEmail(),
                        p.getAddress(),
                        p.getTags()
                );

                personsToUpdate.add(updatedPerson);
            }
        }

        // apply updates back to the model
        for (Person updated : personsToUpdate) {
            model.setPerson(findPersonById(model, updated.getId()), updated);
        }
    }

    /**
     * Finds a person in the model by ID.
     */
    private static Person findPersonById(Model model, seedu.address.model.person.IdentificationNumber id) {
        for (Person p : model.getAddressBook().getPersonList()) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }
}
