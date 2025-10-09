package seedu.address.model.util;

import seedu.address.model.person.IdentificationNumber;
import seedu.address.model.person.Role;

import java.util.concurrent.atomic.AtomicInteger;

public class IdentificationNumberGenerator {
    private final AtomicInteger tutorCounter;
    private final AtomicInteger studentCounter;

    public IdentificationNumberGenerator(int tutorStart, int studentStart) {
        this.tutorCounter = new AtomicInteger(tutorStart);
        this.studentCounter = new AtomicInteger(studentStart);
    }

    public IdentificationNumber generate(Role role) {
        if (role.isTutor()) {
            return new IdentificationNumber(String.format("T%08d", tutorCounter.incrementAndGet()));
        } else {
            return new IdentificationNumber(String.format("S%08d", studentCounter.incrementAndGet()));
        }
    }

    public void init(int tutorStart, int studentStart) {
        tutorCounter.set(tutorStart);
        studentCounter.set(studentStart);
    }
}
