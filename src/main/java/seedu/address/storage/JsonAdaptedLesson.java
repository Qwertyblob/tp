package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.lesson.ClassName;
import seedu.address.model.lesson.Day;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.lesson.Time;
import seedu.address.model.lesson.Tutor;
import seedu.address.model.person.IdentificationNumber;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Lesson}.
 */
class JsonAdaptedLesson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Lesson's %s field is missing!";

    private final String className;
    private final String day;
    private final String time;
    private final String tutor;
    private final List<JsonAdaptedIdentificationNumber> students = new ArrayList<>();
    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedLesson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedLesson(@JsonProperty("className") String className,
                             @JsonProperty("day") String day,
                             @JsonProperty("time") String time,
                             @JsonProperty("tutor") String tutor,
                             @JsonProperty("students") List<JsonAdaptedIdentificationNumber> students,
                             @JsonProperty("tags") List<JsonAdaptedTag> tags) {
        this.className = className;
        this.day = day;
        this.time = time;
        this.tutor = tutor;
        if (students != null) {
            this.students.addAll(students);
        }
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    /**
     * Converts a given {@code Lesson} into this class for Jackson use.
     */
    public JsonAdaptedLesson(Lesson source) {
        className = source.getClassName().toString();
        day = source.getDay().fullDay;
        time = source.getTime().fullTime;
        tutor = source.getTutor().tutorName;
        students.addAll(source.getStudents().stream()
                .map(JsonAdaptedIdentificationNumber::new)
                .collect(Collectors.toList()));
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Lesson} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted lesson.
     */
    public Lesson toModelType() throws IllegalValueException {
        final List<IdentificationNumber> lessonStudents = new ArrayList<>();
        final List<Tag> lessonTags = new ArrayList<>();
        for (JsonAdaptedIdentificationNumber student : students) {
            lessonStudents.add(student.toModelType());
        }
        for (JsonAdaptedTag tag : tags) {
            lessonTags.add(tag.toModelType());
        }

        if (className == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    ClassName.class.getSimpleName()));
        }
        if (!ClassName.isValidClassName(className)) {
            throw new IllegalValueException(ClassName.MESSAGE_CONSTRAINTS);
        }
        final ClassName modelClassName = new ClassName(className);

        if (day == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Day.class.getSimpleName()));
        }
        if (!Day.isValidDay(day)) {
            throw new IllegalValueException(Day.MESSAGE_CONSTRAINTS);
        }
        final Day modelDay = new Day(day);

        if (time == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Time.class.getSimpleName()));
        }
        if (!Time.isValidTime(time)) {
            throw new IllegalValueException(Time.MESSAGE_CONSTRAINTS);
        }
        final Time modelTime = new Time(time);

        if (tutor == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Tutor.class.getSimpleName()));
        }
        if (!Tutor.isValidTutor(tutor)) {
            throw new IllegalValueException(Tutor.MESSAGE_CONSTRAINTS);
        }
        final Tutor modelTutor = new Tutor(tutor);

        final Set<IdentificationNumber> modelStudents = new HashSet<>(lessonStudents);

        final Set<Tag> modelTags = new HashSet<>(lessonTags);
        return new Lesson(modelClassName, modelDay, modelTime, modelTutor, modelTags, modelStudents);
    }

}
