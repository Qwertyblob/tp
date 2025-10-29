package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.lesson.ClassName;
import seedu.address.model.lesson.Day;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.lesson.Time;
import seedu.address.model.lesson.Tutor;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.IdentificationNumber;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new IdentificationNumber("S0000000"), new Name("Alex Yeoh"),
                new Role("student"), new Phone("87438807"),
                new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"),
                getTagSet()),
            new Person(new IdentificationNumber("T0000000"),
                new Name("Bernice Yu"), new Role("tutor"), new Phone("99272758"),
                new Email("berniceyu@example.com"),
                new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                getTagSet("newcomer")),
            new Person(new IdentificationNumber("T0000001"),
                new Name("Charlotte Oliveiro"), new Role("tutor"), new Phone("93210283"),
                new Email("charlotte@example.com"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                getTagSet("math")),
            new Person(new IdentificationNumber("S0000001"),
                new Name("David Li"), new Role("student"), new Phone("91031282"),
                new Email("lidavid@example.com"),
                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                getTagSet("newcomer")),
            new Person(new IdentificationNumber("S0000002"),
                new Name("Irfan Ibrahim"), new Role("student"), new Phone("92492021"),
                new Email("irfan@example.com"),
                new Address("Blk 47 Tampines Street 20, #17-35"),
                getTagSet()),
            new Person(new IdentificationNumber("T0000002"),
                new Name("Roy Balakrishnan"), new Role("tutor"), new Phone("92624417"),
                new Email("royb@example.com"),
                new Address("Blk 45 Aljunied Street 85, #11-31"),
                getTagSet("science"))
        };
    }

    public static Lesson[] getSampleLessons() {
        return new Lesson[] {
                new Lesson(new ClassName("M2a"), new Day("monday"), new Time("1200-1400"), new Tutor("T0000001"),
                        getTagSet()),
                new Lesson(new ClassName("S2b"), new Day("tuesday"), new Time("1400-1600"), new Tutor("T0000002"),
                        getTagSet()),
                new Lesson(new ClassName("E2c"), new Day("thursday"), new Time("1500-1700"), new Tutor("T0000000"),
                        getTagSet("temporary")),
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        for (Lesson sampleLesson : getSampleLessons()) {
            sampleAb.addLesson(sampleLesson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Lesson> getLessonSet(String... strings) {
        return Arrays.stream(strings)
                .map(Lesson::makeLessonTest)
                .collect(Collectors.toSet());
    }

}
