package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CLASS_MATH;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalLessons.getTypicalModelManager;
import static seedu.address.testutil.TypicalPersons.AMY;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.lesson.ClassName;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.IdentificationNumber;
import seedu.address.model.person.Person;

public class MarkCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = getTypicalModelManager();
        expectedModel = getTypicalModelManager();

        // Set up a pre-condition: Enrol AMY in the MATH class ("A1a") for testing.
        try {
            ClassName className = new ClassName(VALID_CLASS_MATH);
            IdentificationNumber studentId = AMY.getId();
            new EnrolCommand(studentId, className).execute(model);
            new EnrolCommand(studentId, className).execute(expectedModel);
        } catch (CommandException e) {
            throw new AssertionError("Setup for MarkCommandTest should not fail.");
        }
    }

    @Test
    public void execute_validStudentAndLesson_success() {
        ClassName className = new ClassName(VALID_CLASS_MATH);
        Person studentToMark = AMY;
        MarkAttendanceCommand markAttendanceCommand = new MarkAttendanceCommand(studentToMark.getId(), className);

        // --- Build the expected state ---
        Lesson lessonToUpdate = expectedModel.getFilteredLessonList().stream()
                .filter(l -> l.getClassName().equals(className)).findFirst().get();

        Map<LocalDate, Set<IdentificationNumber>> updatedAttendance = new HashMap<>(lessonToUpdate.getAttendance());
        updatedAttendance.computeIfAbsent(LocalDate.now(), k -> new HashSet<>()).add