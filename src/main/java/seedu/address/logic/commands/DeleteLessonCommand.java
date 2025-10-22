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
import seedu.address.model.lesson.ClassName;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.Person;

/**
 * Deletes a person identified using its displayed index from the address book.
 */
public class DeleteLessonCommand extends ConfirmableCommand {

    public static final String COMMAND_WORD = "deletec";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the class identified by the index number or class name used in the displayed class list.\n"
            + "Parameters: INDEX (must be a positive integer) or c/CLASSNAME\n"
            + "Example: " + COMMAND_WORD + " 1 or " + COMMAND_WORD + " c/M2a";

    public static final String MESSAGE_DELETE_LESSON_SUCCESS = "Deleted Class: %1$s";
    public static final String MESSAGE_CONFIRM_DELETE = "(Y/N) Would you like to delete this item? %1$s";
    private static final String MESSAGE_LESSON_NOT_FOUND = "No class found with the given name.";

    private final Index targetIndex;
    private final ClassName targetClassName;

    /**
     * @param targetIndex of the lesson to delete.
     */
    public DeleteLessonCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
        this.targetClassName = null;
    }

    /**
     * @param className of the lesson to delete.
     */
    public DeleteLessonCommand(ClassName className) {
        requireNonNull(className);
        this.targetClassName = className;
        this.targetIndex = null;
    }

    private Lesson getLessonToDelete(Model model) throws CommandException {
        List<Lesson> lastShownList = model.getFilteredLessonList();

        // If index is provided, get the lesson accordingly
        if (targetIndex != null) {
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_LESSON_DISPLAYED_INDEX);
            }
            return lastShownList.get(targetIndex.getZeroBased());
        }

        // Else search for matching name in list
        if (targetClassName != null) {
            for (Lesson lesson : lastShownList) {
                String trimmedName = lesson.getClassName().fullClassName.trim();
                String trimmedTargetName = targetClassName.fullClassName.trim();
                if (trimmedName.equals(trimmedTargetName)) {
                    return lesson;
                }
            }
        }

        throw new CommandException(MESSAGE_LESSON_NOT_FOUND);
    }

    @Override
    public String getConfirmationMessage(Model model) throws CommandException {
        Lesson lessonToDelete = getLessonToDelete(model);
        return String.format(MESSAGE_CONFIRM_DELETE, Messages.formatLesson(lessonToDelete));
    }

    @Override
    public CommandResult executeConfirmed(Model model) throws CommandException {
        requireNonNull(model);
        List<Lesson> lastShownList = model.getFilteredLessonList();

        if (targetIndex != null) {
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_LESSON_DISPLAYED_INDEX);
            }
        }

        Lesson lessonToDelete = getLessonToDelete(model);

        // Create a copy of the person list to iterate over, to avoid modification issues.
        List<Person> personsToUpdate = new java.util.ArrayList<>(model.getFilteredPersonList());

        for (Person person : personsToUpdate) {
            // Check if the person is enrolled in the lesson to be deleted.
            if (person.getLessons().contains(lessonToDelete)) {
                // Create a new set of lessons without the deleted lesson
                Set<Lesson> newLessonSet = new HashSet<>(person.getLessons());
                newLessonSet.remove(lessonToDelete);

                // Create a new person with the updated lesson list.
                Person updatedPerson = new Person(
                        person.getId(),
                        person.getName(),
                        person.getRole(),
                        newLessonSet,
                        person.getPhone(),
                        person.getEmail(),
                        person.getAddress(),
                        person.getTags()
                );
                // Update the lesson in the model.
                model.setPerson(person, updatedPerson);
            }
        }

        model.deleteLesson(lessonToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_LESSON_SUCCESS, Messages.formatLesson(lessonToDelete)),
                CommandResult.DisplayType.RECENT);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteLessonCommand)) {
            return false;
        }

        DeleteLessonCommand otherDeleteCommand = (DeleteLessonCommand) other;
        return targetIndex.equals(otherDeleteCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}

