package seedu.address.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.EditLessonCommand.EditLessonDescriptor;
import seedu.address.model.lesson.ClassName;
import seedu.address.model.lesson.Day;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.lesson.Time;
import seedu.address.model.lesson.Tutor;
import seedu.address.model.tag.Tag;

/**
 * A utility class to help with building EditLessonDescriptor objects.
 */
public class EditLessonDescriptorBuilder {

    private EditLessonDescriptor descriptor;

    public EditLessonDescriptorBuilder() {
        descriptor = new EditLessonDescriptor();
    }

    public EditLessonDescriptorBuilder(EditLessonDescriptor descriptor) {
        this.descriptor = new EditLessonDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditLessonDescriptor} with fields containing {@code lesson}'s details.
     */
    public EditLessonDescriptorBuilder(Lesson lesson) {
        descriptor = new EditLessonDescriptor();
        descriptor.setClassName(lesson.getClassName());
        descriptor.setDay(lesson.getDay());
        descriptor.setTime(lesson.getTime());
        descriptor.setTutor(lesson.getTutor());
        descriptor.setTags(lesson.getTags());
    }

    /**
     * Sets the {@code ClassName} of the {@code EditLessonDescriptor} that we are building.
     */
    public EditLessonDescriptorBuilder withClassName(String className) {
        descriptor.setClassName(new ClassName(className));
        return this;
    }

    /**
     * Sets the {@code Day} of the {@code EditLessonDescriptor} that we are building.
     */
    public EditLessonDescriptorBuilder withDay(String day) {
        descriptor.setDay(new Day(day));
        return this;
    }

    /**
     * Sets the {@code Time} of the {@code EditLessonDescriptor} that we are building.
     */
    public EditLessonDescriptorBuilder withTime(String time) {
        descriptor.setTime(new Time(time));
        return this;
    }

    /**
     * Sets the {@code Tutor} of the {@code EditLessonDescriptor} that we are building.
     */
    public EditLessonDescriptorBuilder withTutor(String tutorId) {
        descriptor.setTutor(new Tutor(tutorId));
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditLessonDescriptor}.
     */
    public EditLessonDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditLessonDescriptor build() {
        return descriptor;
    }
}
