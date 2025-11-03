package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTOR;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_LESSONS;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.lesson.ClassName;
import seedu.address.model.lesson.Day;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.lesson.Time;
import seedu.address.model.lesson.Tutor;
import seedu.address.model.person.IdentificationNumber;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.LessonCascadeUpdater;

/**
 * Edits the details of an existing lesson in the address book.
 */
public class EditLessonCommand extends Command {

    public static final String COMMAND_WORD = "editc";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the class identified "
            + "by the index number used in the displayed class list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: "
            + "INDEX (must be a positive integer) "
            + "[" + PREFIX_CLASS + "CLASS_NAME] "
            + "[" + PREFIX_DAY + "DAY] "
            + "[" + PREFIX_TIME + "TIME] "
            + "[" + PREFIX_TUTOR + "TUTOR] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_CLASS + "M2a "
            + PREFIX_DAY + "monday "
            + PREFIX_TIME + "1400-1600 "
            + PREFIX_TUTOR + "T7654321";

    public static final String MESSAGE_EDIT_LESSON_SUCCESS = "Edited class: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_LESSON = "This class already exists in the address book.";
    public static final String MESSAGE_TUTOR_TIME_CLASH =
            "This tutor already has a class that overlaps with the specified time.";
    public static final String MESSAGE_STUDENT_TIME_CLASH_START =
            "Student clash detected for: ";

    private final Index index;
    private final EditLessonDescriptor editLessonDescriptor;

    /**
     * @param index of the lesson in the filtered person list to edit
     * @param editLessonDescriptor details to edit the lesson with
     */
    public EditLessonCommand(Index index, EditLessonDescriptor editLessonDescriptor) {
        requireNonNull(index);
        requireNonNull(editLessonDescriptor);

        this.index = index;
        this.editLessonDescriptor = new EditLessonDescriptor(editLessonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Lesson> lastShownList = model.getFilteredLessonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_LESSON_DISPLAYED_INDEX);
        }

        Lesson lessonToEdit = lastShownList.get(index.getZeroBased());
        Lesson editedLesson = createEditedLesson(lessonToEdit, editLessonDescriptor);

        if (!lessonToEdit.isSameLesson(editedLesson) && model.hasLesson(editedLesson)) {
            throw new CommandException(MESSAGE_DUPLICATE_LESSON);
        }

        boolean tutorHasClash = model.getAddressBook().getLessonList().stream()
                .filter(existing -> !existing.equals(lessonToEdit)) // exclude current lesson being edited
                .filter(existing -> existing.getTutor().equals(editedLesson.getTutor()))
                .anyMatch(existing -> existing.hasOverlapsWith(editedLesson));

        if (tutorHasClash) {
            throw new CommandException(MESSAGE_TUTOR_TIME_CLASH);
        }

        Set<String> clashingIds = new HashSet<>();
        for (Lesson existing : model.getAddressBook().getLessonList()) {
            if (!existing.equals(lessonToEdit) && existing.hasOverlapsWith(editedLesson)) {
                for (IdentificationNumber id : existing.getStudents()) {
                    if (editedLesson.getStudents().contains(id)) {
                        clashingIds.add(id.getValue());
                    }
                }
            }
        }
        if (!clashingIds.isEmpty()) {
            throw new CommandException(MESSAGE_STUDENT_TIME_CLASH_START + String.join(", ", clashingIds));
        }

        model.setLesson(lessonToEdit, editedLesson);
        LessonCascadeUpdater.updateStudentsWithEditedLesson(model, lessonToEdit, editedLesson);

        model.updateFilteredLessonList(PREDICATE_SHOW_ALL_LESSONS);
        // Update AddressBook state pointer
        String output = String.format(MESSAGE_EDIT_LESSON_SUCCESS, Messages.formatLesson(editedLesson));
        model.commitAddressBook(output, CommandResult.DisplayType.CLASS_LIST);
        return new CommandResult(output, CommandResult.DisplayType.CLASS_LIST);
    }

    /**
     * Creates and returns a {@code Lesson} with the details of {@code lessonToEdit}
     * edited with {@code editLessonDescriptor}.
     *
     * Preserves student IDs and attendance in an immutable and defensive manner.
     */
    private static Lesson createEditedLesson(Lesson lessonToEdit, EditLessonDescriptor descriptor) {
        assert lessonToEdit != null;

        ClassName updatedClassName = descriptor.getClassName().orElse(lessonToEdit.getClassName());
        Day updatedDay = descriptor.getDay().orElse(lessonToEdit.getDay());
        Time updatedTime = descriptor.getTime().orElse(lessonToEdit.getTime());
        Tutor updatedTutor = descriptor.getTutor().orElse(lessonToEdit.getTutor());
        Set<Tag> updatedTags = descriptor.getTags().orElse(lessonToEdit.getTags());

        // Defensive copies for immutability
        Set<IdentificationNumber> preservedStudents = new HashSet<>(lessonToEdit.getStudents());
        Map<java.time.LocalDate, Set<IdentificationNumber>> preservedAttendance = new HashMap<>();

        lessonToEdit.getAttendance().forEach((date, ids) ->
                preservedAttendance.put(date, new HashSet<>(ids))
        );

        return new Lesson(updatedClassName, updatedDay, updatedTime, updatedTutor,
                preservedStudents, preservedAttendance, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof EditLessonCommand)) {
            return false;
        }
        EditLessonCommand otherCommand = (EditLessonCommand) other;
        return index.equals(otherCommand.index)
                && editLessonDescriptor.equals(otherCommand.editLessonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editLessonDescriptor", editLessonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the lesson with. Each non-empty field value will replace
     * the corresponding value of the lesson.
     */
    public static class EditLessonDescriptor {
        private ClassName className;
        private Day day;
        private Time time;
        private Tutor tutor;
        private Set<Tag> tags;

        public EditLessonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditLessonDescriptor(EditLessonDescriptor toCopy) {
            setClassName(toCopy.className);
            setDay(toCopy.day);
            setTime(toCopy.time);
            setTutor(toCopy.tutor);
            setTags(toCopy.tags);
        }

        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(className, day, time, tutor, tags);
        }

        public void setClassName(ClassName className) {
            this.className = className;
        }
        public Optional<ClassName> getClassName() {
            return Optional.ofNullable(className);
        }

        public void setDay(Day day) {
            this.day = day;
        }
        public Optional<Day> getDay() {
            return Optional.ofNullable(day);
        }

        public void setTime(Time time) {
            this.time = time;
        }
        public Optional<Time> getTime() {
            return Optional.ofNullable(time);
        }

        public void setTutor(Tutor tutor) {
            this.tutor = tutor;
        }
        public Optional<Tutor> getTutor() {
            return Optional.ofNullable(tutor);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null)
                    ? Optional.of(Collections.unmodifiableSet(tags))
                    : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }
            if (!(other instanceof EditLessonDescriptor)) {
                return false;
            }
            EditLessonDescriptor otherDesc = (EditLessonDescriptor) other;
            return Objects.equals(className, otherDesc.className)
                    && Objects.equals(day, otherDesc.day)
                    && Objects.equals(time, otherDesc.time)
                    && Objects.equals(tutor, otherDesc.tutor)
                    && Objects.equals(tags, otherDesc.tags);
        }
    }
}
