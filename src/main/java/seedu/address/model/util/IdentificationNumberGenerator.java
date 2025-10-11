package seedu.address.model.util;

import seedu.address.model.person.IdentificationNumber;
import seedu.address.model.person.Person;
import seedu.address.model.person.Role;
import seedu.address.model.person.UniquePersonList;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class IdentificationNumberGenerator {
    private final static AtomicInteger tutorCounter = new AtomicInteger(0);
    private final static AtomicInteger studentCounter = new AtomicInteger(0);

    public static void init(List<Person> persons) {
        int maxTutor = persons.stream()
                .filter(p -> p.getRole().isTutor())
                .mapToInt(p -> p.getId().getNumericValue())
                .max()
                .orElse(0);

        int maxStudent = persons.stream()
                .filter(p -> p.getRole().isStudent())
                .mapToInt(p -> p.getId().getNumericValue())
                .max()
                .orElse(0);

        tutorCounter.set(maxTutor);
        studentCounter.set(maxStudent);
    }

    public static IdentificationNumber generate(Role role) {
        if (role.isTutor()) {
            return new IdentificationNumber("T", tutorCounter.incrementAndGet());
        } else {
            return new IdentificationNumber("S", studentCounter.incrementAndGet());
        }
    }
}