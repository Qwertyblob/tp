package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final IdentificationNumber id;
    private final Name name;
    private final Role role;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<Lesson> lessons = new HashSet<>();
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Person(IdentificationNumber id, Name name, Role role, Set<Lesson> lessons, Phone phone,
                  Email email, Address address, Set<Tag> tags) {
        requireAllNonNull(name, role, phone, email, address, tags);
        this.id = id;
        this.name = name;
        this.role = role;
        this.lessons.addAll(lessons);
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
    }

    public Person(IdentificationNumber id, Name name, Role role, Phone phone,
                  Email email, Address address, Set<Tag> tags) {
        this(id, name, role, Collections.emptySet(), phone, email, address, tags);
    }

    public Name getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public IdentificationNumber getId() {
        return id;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an immutable lesson set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Lesson> getLessons() {
        return Collections.unmodifiableSet(lessons);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean hasSameName(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity fields.
     * This defines identity equality between two persons.
     */
    public boolean hasSameIdentity(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && name.equals(otherPerson.name)
                && role.equals(otherPerson.role)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email);
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return id.equals(otherPerson.id)
                && name.equals(otherPerson.name)
                && role.equals(otherPerson.role)
                && lessons.equals(otherPerson.lessons)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(id, name, role, lessons, phone, email, address, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("id", id)
                .add("name", name)
                .add("role", role)
                .add("lessons", lessons)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("tags", tags)
                .toString();
    }

}
