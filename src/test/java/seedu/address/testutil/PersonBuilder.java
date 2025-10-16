package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.IdentificationNumber;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;
import seedu.address.model.tag.Tag;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_ID = "S0000001";
    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_ROLE = "student";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";

    private IdentificationNumber id;
    private Name name;
    private Role role;
    private Set<Lesson> lessons;
    private Phone phone;
    private Email email;
    private Address address;
    private Set<Tag> tags;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        id = new IdentificationNumber(DEFAULT_ID);
        name = new Name(DEFAULT_NAME);
        role = new Role(DEFAULT_ROLE);
        lessons = new HashSet<>();
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        tags = new HashSet<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        id = personToCopy.getId();
        name = personToCopy.getName();
        role = personToCopy.getRole();
        lessons = new HashSet<>(personToCopy.getLessons());
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        address = personToCopy.getAddress();
        tags = new HashSet<>(personToCopy.getTags());
    }


    /**
     * Sets the {@code id} of the {@code Person} that we are building.
     */
    public PersonBuilder withId(IdentificationNumber id) {
        this.id = new IdentificationNumber(id.toString());
        return this;
    }


    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Sets the {@code Role} of the {@code Person} that we are building.
     */
    public PersonBuilder withRole(String role) {
        this.role = new Role(role);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code lessons} of the {@code Person} that we are building.
     */
    public PersonBuilder withLessons(Set<Lesson> lessons) {
        this.lessons = new HashSet<>(lessons);
        return this;
    }

    public Person build() {
        return new Person(id, name, role, lessons, phone, email, address, tags);
    }

}
