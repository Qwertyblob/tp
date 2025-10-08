package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.ModelManager;
import seedu.address.model.lesson.Lesson;

/**
 * A utility class containing a list of {@code Lesson} objects to be used in tests.
 */
public class TypicalLessons {

    public static final Lesson MATH_A1A = new LessonBuilder().withClassName("A1a")
            .withDay("Monday")
            .withTime("1200")
            .withTutor("A12345678")
            .withTags("math").build();

    public static final Lesson SCIENCE_B2B = new LessonBuilder().withClassName("B2b")
            .withDay("Tuesday")
            .withTime("1300")
            .withTutor("B12345678")
            .withTags("science").build();

    public static final Lesson ENGLISH_C3C = new LessonBuilder().withClassName("C3c")
            .withDay("Wednesday")
            .withTime("1400")
            .withTutor("C12345678")
            .withTags("english").build();

    public static final Lesson PHYSICS_D4D = new LessonBuilder().withClassName("D4d")
            .withDay("Thursday")
            .withTime("1500")
            .withTutor("D12345678")
            .withTags("physics").build();

    public static final Lesson CHEMISTRY_E5E = new LessonBuilder().withClassName("E5e")
            .withDay("Friday")
            .withTime("1600")
            .withTutor("E12345678")
            .withTags("chemistry").build();

    private TypicalLessons() {} // prevents instantiation

    /**
     * Returns an {@code ModelManager} with all the typical lessons.
     */
    public static ModelManager getTypicalModelManager() {
        ModelManager model = new ModelManager();
        for (Lesson lesson : getTypicalLessons()) {
            model.addLesson(lesson);
        }
        return model;
    }

    public static List<Lesson> getTypicalLessons() {
        return new ArrayList<>(Arrays.asList(MATH_A1A, SCIENCE_B2B, ENGLISH_C3C, PHYSICS_D4D, CHEMISTRY_E5E));
    }
}
