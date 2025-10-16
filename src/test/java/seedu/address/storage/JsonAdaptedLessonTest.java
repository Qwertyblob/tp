package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedLesson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalLessons.MATH_A1A;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.lesson.ClassName;
import seedu.address.model.lesson.Day;
import seedu.address.model.lesson.Time;
import seedu.address.model.lesson.Tutor;

public class JsonAdaptedLessonTest {

    private static final String INVALID_CLASSNAME = "math3a";
    private static final String INVALID_DAY = "Today";
    private static final String INVALID_TIME = "4pm";
    private static final String INVALID_TUTOR = "S1234567";
    private static final String INVALID_IDS = "S123456";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_CLASSNAME = MATH_A1A.getClassName().toString();
    private static final String VALID_DAY = MATH_A1A.getDay().toString();
    private static final String VALID_TIME = MATH_A1A.getTime().toString();
    private static final String VALID_TUTOR = MATH_A1A.getTutor().toString();
    private static final List<JsonAdaptedIdentificationNumber> VALID_IDS = MATH_A1A.getStudents().stream()
            .map(JsonAdaptedIdentificationNumber::new)
            .collect(Collectors.toList());
    private static final List<JsonAdaptedTag> VALID_TAGS = MATH_A1A.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    @BeforeEach
    public void setUp() {
        // Reset the ID generator before each test
    }

    @Test
    public void toModelType_validLessonDetails_returnsLesson() throws Exception {
        JsonAdaptedLesson lesson = new JsonAdaptedLesson(VALID_CLASSNAME, VALID_DAY, VALID_TIME,
                VALID_TUTOR, VALID_IDS, VALID_TAGS);
        assertEquals(MATH_A1A, lesson.toModelType());
    }

    @Test
    public void toModelType_invalidClassName_throwsIllegalValueException() {
        JsonAdaptedLesson lesson =
                new JsonAdaptedLesson(INVALID_CLASSNAME, VALID_DAY, VALID_TIME,
                        VALID_TUTOR, VALID_IDS, VALID_TAGS);
        String expectedMessage = ClassName.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, lesson::toModelType);
    }

    @Test
    public void toModelType_nullClassName_throwsIllegalValueException() {
        JsonAdaptedLesson lesson =
                new JsonAdaptedLesson(null, VALID_DAY, VALID_TIME,
                        VALID_TUTOR, VALID_IDS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT,
                ClassName.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, lesson::toModelType);
    }

    @Test
    public void toModelType_invalidDay_throwsIllegalValueException() {
        JsonAdaptedLesson lesson =
                new JsonAdaptedLesson(VALID_CLASSNAME, INVALID_DAY, VALID_TIME,
                        VALID_TUTOR, VALID_IDS, VALID_TAGS);
        String expectedMessage = Day.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, lesson::toModelType);
    }

    @Test
    public void toModelType_nullDay_throwsIllegalValueException() {
        JsonAdaptedLesson lesson =
                new JsonAdaptedLesson(VALID_CLASSNAME, null, VALID_TIME,
                        VALID_TUTOR, VALID_IDS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT,
                Day.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, lesson::toModelType);
    }

    @Test
    public void toModelType_invalidTime_throwsIllegalValueException() {
        JsonAdaptedLesson lesson =
                new JsonAdaptedLesson(VALID_CLASSNAME, VALID_DAY, INVALID_TIME,
                        VALID_TUTOR, VALID_IDS, VALID_TAGS);
        String expectedMessage = Time.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, lesson::toModelType);
    }

    @Test
    public void toModelType_nullTime_throwsIllegalValueException() {
        JsonAdaptedLesson lesson =
                new JsonAdaptedLesson(VALID_CLASSNAME, VALID_DAY, null,
                        VALID_TUTOR, VALID_IDS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT,
                Time.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, lesson::toModelType);
    }

    @Test
    public void toModelType_invalidTutor_throwsIllegalValueException() {
        JsonAdaptedLesson lesson =
                new JsonAdaptedLesson(VALID_CLASSNAME, VALID_DAY, VALID_TIME,
                        INVALID_TUTOR, VALID_IDS, VALID_TAGS);
        String expectedMessage = Tutor.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, lesson::toModelType);
    }

    @Test
    public void toModelType_nullTutor_throwsIllegalValueException() {
        JsonAdaptedLesson lesson =
                new JsonAdaptedLesson(VALID_CLASSNAME, VALID_DAY, VALID_TIME,
                        null, VALID_IDS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT,
                Tutor.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, lesson::toModelType);
    }

    @Test
    public void toModelType_invalidStudentIds_throwsIllegalValueException() {
        List<JsonAdaptedIdentificationNumber> invalidIds = new ArrayList<>(VALID_IDS);
        invalidIds.add(new JsonAdaptedIdentificationNumber(INVALID_IDS));
        JsonAdaptedLesson lesson =
                new JsonAdaptedLesson(VALID_CLASSNAME, VALID_DAY, VALID_TIME,
                        VALID_TUTOR, invalidIds, VALID_TAGS);
        assertThrows(IllegalValueException.class, lesson::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedLesson lesson =
                new JsonAdaptedLesson(VALID_CLASSNAME, VALID_DAY, VALID_TIME,
                        VALID_TUTOR, VALID_IDS, invalidTags);
        assertThrows(IllegalValueException.class, lesson::toModelType);
    }
}
