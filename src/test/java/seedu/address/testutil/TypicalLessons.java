package seedu.address.testutil;

import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.ModelManager;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.Person;

/**
 * A utility class containing a list of {@code Lesson} objects to be used in tests.
 */
public class TypicalLessons {

    public static final Lesson MATH_A1A = new LessonBuilder().withClassName("A1a")
            .withDay("monday")
            .withTime("1200")
            .withTutor("t1234567")
            .withTags("math").build();

    public static final Lesson SCIENCE_B2B = new LessonBuilder().withClassName("B2b")
            .withDay("tuesday")
            .withTime("1300")
            .withTutor("t1234567")
            .withTags("science").build();

    public static final Lesson ENGLISH_C3C = new LessonBuilder().withClassName("C3c")
            .withDay("wednesday")
            .withTime("1400")
            .withTutor("t1234567")
            .withTags("english").build();

    public static final Lesson PHYSICS_D4D = new LessonBuilder().withClassName("D4d")
            .withDay("thursday")
            .withTime("1500")
            .withTutor("t1234567")
            .withTags("physics").build();

    public static final Lesson CHEMISTRY_E5E = new LessonBuilder().withClassName("E5e")
            .withDay("friday")
            .withTime("1600")
            .withTutor("t1234567")
            .withTags("chemistry").build();

    public static final Lesson CHINESE_D6F = new LessonBuilder().withClassName("D6f")
            .withDay("saturday")
            .withTime("1700")
            .withTutor("t1234567")
            .withStudents(BENSON.getId().toString())
            .withTags("chinese").build();

    private TypicalLessons() {} // prevents instantiation

    /**
     * Returns an {@code ModelManager} with all the typical lessons.
     */
    public static ModelManager getTypicalModelManager() {
        ModelManager model = new ModelManager();

        for (Person person : TypicalPersons.getTypicalPersons()) {
            model.addPerson(person);
        }

        for (Lesson lesson : getTypicalLessons()) {
            model.addLesson(lesson);
        }
        return model;
    }

    public static List<Lesson> getTypicalLessons() {
        return new ArrayList<>(Arrays.asList(MATH_A1A, SCIENCE_B2B, ENGLISH_C3C, PHYSICS_D4D, CHEMISTRY_E5E, CHINESE_D6F));
    }
}
