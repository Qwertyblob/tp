package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.IdentificationNumber;
import seedu.address.model.person.Person;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

    private final Index targetIndex;

    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
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
                        lesson.getTags(),
                        newStudentIdSet
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
