package seedu.address.model.person;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests whether a {@code Person} matches all the specified search criteria.
 */
public class ContactMatchesPredicate implements Predicate<Person> {
    private final String id;
    private final String name;
    private final String role;
    private final String lesson;
    private final String phone;
    private final String email;
    private final String address;
    private final String tags;

    /**
     * Creates a ContactMatchesPredicate with the given information of a person.
     */
    public ContactMatchesPredicate(String id, String name, String role, String lesson,
                                   String phone, String email, String address, String tags) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.lesson = lesson;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags = tags;
    }

    /**
     * Tests whether the given {@code Person} satisfies all specified criteria.
     * Any empty search fields are ignored.
     *
     * @param person The {@code Person} to test.
     * @return {@code true} if the person matches all non-empty criteria, {@code false} otherwise.
     */
    @Override
    public boolean test(Person person) {
        if (id.isEmpty() && name.isEmpty() && role.isEmpty() && lesson.isEmpty() && phone.isEmpty()
                && email.isEmpty() && address.isEmpty() && tags.isEmpty()) {
            return false;
        }

        boolean matches = true;

        if (!id.isEmpty()) {
            matches &= StringUtil.containsWordIgnoreCase(person.getId().getValue(), id);
        }
        if (!name.isEmpty()) {
            String[] keywords = name.trim().split("\\s+");
            String personName = person.getName().fullName;
            for (String keyword : keywords) {
                if (!StringUtil.containsWordIgnoreCase(personName, keyword)) {
                    matches = false;
                    break;
                }
            }
        }
        if (!role.isEmpty()) {
            matches &= StringUtil.containsWordIgnoreCase(person.getRole().role, role);
        }
        if (!lesson.isEmpty()) {
            Set<String> personLessons = person.getLessons().stream()
                    .map(lesson -> lesson.getClassName().toString().toLowerCase())
                    .collect(Collectors.toSet());
            String[] searchLessons = lesson.toLowerCase().trim().split("\\s+");
            for (String searchLesson : searchLessons) {
                if (!personLessons.contains(searchLesson)) {
                    matches = false;
                    break;
                }
            }
        }
        if (!phone.isEmpty()) {
            matches &= StringUtil.containsWordIgnoreCase(person.getPhone().value, phone);
        }
        if (!email.isEmpty()) {
            matches &= StringUtil.containsWordIgnoreCase(person.getEmail().value, email);
        }
        if (!address.isEmpty()) {
            String[] keywords = address.trim().split("\\s+");
            String personAddress = person.getAddress().value;
            for (String keyword : keywords) {
                if (!StringUtil.containsWordIgnoreCase(personAddress, keyword)) {
                    matches = false;
                    break;
                }
            }
        }
        if (!tags.isEmpty()) {
            Set<String> personTags = person.getTags().stream()
                    .map(tag -> tag.tagName.toLowerCase())
                    .collect(Collectors.toSet());
            String[] searchTags = tags.toLowerCase().trim().split("\\s+");
            for (String searchTag : searchTags) {
                if (!personTags.contains(searchTag)) {
                    matches = false;
                    break;
                }
            }
        }

        return matches;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ContactMatchesPredicate)) {
            return false;
        }

        ContactMatchesPredicate otherContactMatchesPredicate = (ContactMatchesPredicate) other;
        return id.equals(otherContactMatchesPredicate.id)
                && name.equals(otherContactMatchesPredicate.name)
                && role.equals(otherContactMatchesPredicate.role)
                && phone.equals(otherContactMatchesPredicate.phone)
                && email.equals(otherContactMatchesPredicate.email)
                && address.equals(otherContactMatchesPredicate.address)
                && lesson.equals(otherContactMatchesPredicate.lesson)
                && tags.equals(otherContactMatchesPredicate.tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("id", id)
                .add("name", name)
                .add("role", role)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("lessons", lesson)
                .add("tags", tags)
                .toString();
    }
}
