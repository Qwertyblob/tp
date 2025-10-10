package seedu.address.model.person;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

public class ContactMatchesPredicate implements Predicate<Person> {
    private final String name;
    private final String role;
    private final String phone;
    private final String email;
    private final String address;
    private final String tags;

    public ContactMatchesPredicate(String name, String role, String phone, String email, String address, String tags) {
        this.name = name;
        this.role = role;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags = tags;
    }

    @Override
    public boolean test(Person person) {
        boolean matches = true;

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
        return name.equals(otherContactMatchesPredicate.name)
                && role.equals(otherContactMatchesPredicate.role)
                && phone.equals(otherContactMatchesPredicate.phone)
                && email.equals(otherContactMatchesPredicate.email)
                && address.equals(otherContactMatchesPredicate.address)
                && tags.equals(otherContactMatchesPredicate.tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("role", role)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("tags", tags)
                .toString();
    }
}
