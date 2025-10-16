package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.IdentificationNumber;

/**
 * Jackson-friendly version of {@link IdentificationNumber}.
 */
class JsonAdaptedIdentificationNumber {

    private final String studentId;

    /**
     * Constructs a {@code JsonAdaptedIdentificationNumber} with the given {@code studentId}.
     */
    @JsonCreator
    public JsonAdaptedIdentificationNumber(String studentId) {
        this.studentId = studentId;
    }

    /**
     * Converts a given {@code IdentificationNumber} into this class for Jackson use.
     */
    public JsonAdaptedIdentificationNumber(IdentificationNumber source) {
        studentId = source.toString();
    }

    @JsonValue
    public String getStudentId() {
        return studentId;
    }

    /**
     * Converts this Jackson-friendly adapted tag object into the model's {@code IdentificationNumber} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted studentId.
     */
    public IdentificationNumber toModelType() throws IllegalValueException {
        if (!IdentificationNumber.isValidId(studentId)) {
            throw new IllegalValueException(IdentificationNumber.MESSAGE_CONSTRAINTS);
        }
        return new IdentificationNumber(studentId);
    }

}
