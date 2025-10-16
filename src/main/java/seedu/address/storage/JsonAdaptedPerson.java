package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.IdentificationNumber;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String id;
    private final String name;
    private final String role;
    private final List<String> lessons = new ArrayList<>();
    private final String phone;
    private final String email;
    private final String address;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("id") String id,
                             @JsonProperty("name") String name,
                             @JsonProperty("role") String role,
                             @JsonProperty("lessons") List<String> lessons,
                             @JsonProperty("phone") String phone,
                             @JsonProperty("email") String email,
                             @JsonProperty("address") String address,
                             @JsonProperty("tags") List<JsonAdaptedTag> tags) {
        this.id = id;
        this.name = name;
        this.role = role;
        if (lessons != null) {
            this.lessons.addAll(lessons);
        }
        this.phone = phone;
        this.email = email;
        this.address = address;
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    /**
     * Backwards-compatible constructor for code/tests that do not provide lessons.
     */
    public JsonAdaptedPerson(String id, String name, String role, String phone,
                              String email, String address, List<JsonAdaptedTag> tags) {
        this(id, name, role, null, phone, email, address, tags);
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        id = source.getId().toString();
        name = source.getName().fullName;
        role = source.getRole().role;
        lessons.addAll(source.getLessons().stream()
                .map(lesson -> lesson.getClassName().toString())
                .collect(Collectors.toList()));
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        // default behaviour when no lessons list from addressbook is provided: create person with no lessons
        return toModelType(new ArrayList<>());
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object
     * using the provided list of available lessons (from the address book). Only lessons whose
     * class names match entries in this object's `lessons` list will be linked to the returned Person.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person or
     *                               if a referenced lesson class name does not exist in availableLessons.
     */
    public Person toModelType(List<Lesson> availableLessons) throws IllegalValueException {
        final List<Lesson> personLessons = new ArrayList<>();
        for (String classNameStr : lessons) {
            if (classNameStr == null) {
                throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Lesson class name"));
            }
            Lesson matched = null;
            for (Lesson l : availableLessons) {
                if (l.getClassName().toString().equals(classNameStr)) {
                    matched = l;
                    break;
                }
            }
            if (matched == null) {
                throw new IllegalValueException("Lesson with className '" + classNameStr + "' not found in lessons list.");
            }
            personLessons.add(matched);
        }

        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        if (id == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    "IdentificationNumber"));
        }
        if (!IdentificationNumber.isValidId(id)) {
            throw new IllegalValueException(IdentificationNumber.MESSAGE_CONSTRAINTS);
        }
        final IdentificationNumber modelId = new IdentificationNumber(id);

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (role == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Role.class.getSimpleName()));
        }
        if (!Role.isValidRole(role)) {
            throw new IllegalValueException(Role.MESSAGE_CONSTRAINTS);
        }
        final Role modelRole = new Role(role);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        final Set<Tag> modelTags = new HashSet<>(personTags);
        final Set<Lesson> modelLessons = new HashSet<>(personLessons);
        return new Person(modelId, modelName, modelRole, modelLessons, modelPhone, modelEmail, modelAddress, modelTags);
    }

}
