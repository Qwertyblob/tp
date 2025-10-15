package seedu.address.model.util;


import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import seedu.address.model.person.IdentificationNumber;
import seedu.address.model.person.Person;
import seedu.address.model.person.Role;


/**
 * Generator class to create unique Identity Number Objects
 */
public class IdentificationNumberGenerator {
    private static final AtomicInteger tutorCounter = new AtomicInteger(0);
    private static final AtomicInteger studentCounter = new AtomicInteger(0);

    /**
     * Function to initiate generator using a List of persons
     * @param persons List check existing IDs from
     */
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

    /**
     * Function to generate ID based on role argument
     * @param role Role of person to be generated
     * @return Unique ID
     */
    public static IdentificationNumber generate(Role role) {
        if (role.isTutor()) {
            return new IdentificationNumber("T", tutorCounter.incrementAndGet());
        } else {
            return new IdentificationNumber("S", studentCounter.incrementAndGet());
        }
    }
}
