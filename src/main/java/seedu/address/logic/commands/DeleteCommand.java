package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.Model;
import seedu.address.model.person.IdentificationNumber;
import seedu.address.model.person.Person;

/**
 * Deletes a person identified using its displayed index from the address book.
 */
public class DeleteCommand extends ConfirmableCommand {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number or name used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer) or n/NAME\n"
            + "Example: " + COMMAND_WORD + " 1 or " + COMMAND_WORD + " n/Alice";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";
    public static final String MESSAGE_CONFIRM_DELETE = "(Y/N) Would you like to delete this item? %1$s";
    private static final String MESSAGE_PERSON_NOT_FOUND = "No person found with the given name.";

    private final Index targetIndex;
    private final Name targetName;

    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
        this.targetName = null;
    }

    public DeleteCommand(Name name) {
        requireNonNull(name);
        this.targetName = name;
        this.targetIndex = null;
    }

    private Person getPersonToDelete(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        // If index is provided, get the person accordingly
        if (targetIndex != null) {
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            return lastShownList.get(targetIndex.getZeroBased());
        }

        // Else search for matching name in list
        if (targetName != null) {
            for (Person person : lastShownList) {
                String trimmedName = person.getName().fullName.toLowerCase().trim();
                String trimmedTargetName = targetName.fullName.toLowerCase().trim();
                if (trimmedName.equals(trimmedTargetName)) {
                    return person;
                }
            }
        }

        throw new CommandException(MESSAGE_PERSON_NOT_FOUND);
    }

    @Override
    public String getConfirmationMessage(Model model) throws CommandException {
        Person personToDelete = getPersonToDelete(model);
        return String.format(MESSAGE_CONFIRM_DELETE, Messages.formatPerson(personToDelete));
    }

    @Override
    public CommandResult executeConfirmed(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex != null) {
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }

        Person personToDelete = getPersonToDelete(model);
        IdentificationNumber personIdToDelete = personToDelete.getId();

        // Create a copy of the lesson list to iterate over, to avoid modification issues.
        List<Lesson> lessonsToUpdate = new java.util.ArrayList<>(model.getFilteredLessonList());

        for (Lesson lesson : lessonsToUpdate) {
            // Check if the person to be deleted is enrolled in this lesson.
            if (lesson.getStudents().contains(personIdToDelete)) {
                // Create a new set of student IDs without the deleted person's ID.
                Set<IdentificationNumber> newStudentIdSet = new HashSet<>(lesson.getStudents());
                newStudentIdSet.remove(personIdToDelete);

                // Create a new lesson with the updated student list.
                Lesson updatedLesson = new Lesson(
                        lesson.getClassName(),
                        lesson.getDay(),
                        lesson.getTime(),
                        lesson.getTutor(),
                        newStudentIdSet, lesson.getAttendance(), lesson.getTags()
                );
                // Update the lesson in the model.
                model.setLesson(lesson, updatedLesson);
            }
        }

        model.deletePerson(personToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.formatPerson(personToDelete)),
                CommandResult.DisplayType.RECENT);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetIndex.equals(otherDeleteCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
