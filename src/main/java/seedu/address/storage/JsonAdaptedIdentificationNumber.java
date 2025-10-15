package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.IdentificationNumber;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Tag}.
 */
class JsonAdaptedIdentificationNumber {

    private final String studentId;

    /**
     * Constructs a {@code JsonAdaptedTag} with the given {@code tagName}.
     */
    @JsonCreator
    public JsonAdaptedIdentificationNumber(String studentId) {
        this.studentId = studentId;
    }

    /**
     * Converts a given {@code Tag} into this class for Jackson use.
     */
    public JsonAdaptedIdentificationNumber(IdentificationNumber source) {
        studentId = source.toString();
    }

    @JsonValue
    public String getStudentId() {
        return studentId;
    }

    /**
     * Converts this Jackson-friendly adapted tag object into the model's {@code Tag} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted tag.
     */
    public IdentificationNumber toModelType() throws IllegalValueException {
        if (!IdentificationNumber.isValidId(studentId)) {
            throw new IllegalValueException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new IdentificationNumber(studentId);
    }

}
