package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.person.Role;
import seedu.address.model.util.IdentificationNumberGenerator;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    static {
        // Initialize ID counters so that typical persons have predictable IDs
        IdentificationNumberGenerator.init(new ArrayList<>());
    }

    public static final Person ALICE = new PersonBuilder().withName("Alice Pauline")
            .withRole("student")
            .withAddress("123, Jurong West Ave 6, #08-111")
            .withEmail("alice@example.com")
            .withPhone("94351253")
            .withTags("friends")
            .withId(IdentificationNumberGenerator.generate(new Role("student")))
            .build();

    public static final Person BENSON = new PersonBuilder().withName("Benson Meier")
            .withRole("student")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withEmail("johnd@example.com")
            .withPhone("98765432")
            .withTags("owesMoney", "friends")
            .withId(IdentificationNumberGenerator.generate(new Role("student")))
            .build();

    public static final Person CARL = new PersonBuilder().withName("Carl Kurz")
            .withRole("tutor")
            .withPhone("95352563")
            .withEmail("heinz@example.com")
            .withAddress("wall street")
            .withId(IdentificationNumberGenerator.generate(new Role("tutor")))
            .build();

    public static final Person DANIEL = new PersonBuilder().withName("Daniel Meier")
            .withRole("tutor")
            .withPhone("87652533")
            .withEmail("cornelia@example.com")
            .withAddress("10th street")
            .withTags("friends")
            .withId(IdentificationNumberGenerator.generate(new Role("tutor")))
            .build();

    public static final Person ELLE = new PersonBuilder().withName("Elle Meyer")
            .withRole("student")
            .withPhone("9482224")
            .withEmail("werner@example.com")
            .withAddress("michegan ave")
            .withId(IdentificationNumberGenerator.generate(new Role("student")))
            .build();

    public static final Person FIONA = new PersonBuilder().withName("Fiona Kunz")
            .withRole("tutor")
            .withPhone("9482427")
            .withEmail("lydia@example.com")
            .withAddress("little tokyo")
            .withId(IdentificationNumberGenerator.generate(new Role("tutor")))
            .build();

    public static final Person GEORGE = new PersonBuilder().withName("George Best")
            .withRole("student")
            .withPhone("9482442")
            .withEmail("anna@example.com")
            .withAddress("4th street")
            .withId(IdentificationNumberGenerator.generate(new Role("student")))
            .build();

    // Manually added
    public static final Person HOON = new PersonBuilder().withName("Hoon Meier")
            .withRole("student")
            .withPhone("8482424")
            .withEmail("stefan@example.com")
            .withAddress("little india")
            .withId(IdentificationNumberGenerator.generate(new Role("student")))
            .build();

    public static final Person IDA = new PersonBuilder().withName("Ida Mueller")
            .withRole("tutor")
            .withPhone("8482131")
            .withEmail("hans@example.com")
            .withAddress("chicago ave")
            .withId(IdentificationNumberGenerator.generate(new Role("tutor")))
            .build();

    // Persons from CommandTestUtil
    public static final Person AMY = new PersonBuilder().withName(VALID_NAME_AMY)
            .withRole(VALID_ROLE_AMY)
            .withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY)
            .withAddress(VALID_ADDRESS_AMY)
            .withTags(VALID_TAG_FRIEND)
            .withId(IdentificationNumberGenerator.generate(new Role(VALID_ROLE_AMY)))
            .build();

    public static final Person BOB = new PersonBuilder().withName(VALID_NAME_BOB)
            .withRole(VALID_ROLE_BOB)
            .withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB)
            .withAddress(VALID_ADDRESS_BOB)
            .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .withId(IdentificationNumberGenerator.generate(new Role(VALID_ROLE_BOB)))
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
