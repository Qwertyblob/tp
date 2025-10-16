package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Jackson-friendly version of a daily attendance record.
 */
class JsonAdaptedAttendance {

    private final String date;
    private final List<JsonAdaptedIdentificationNumber> presentStudents = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedAttendance} with the given details.
     */
    @JsonCreator
    public JsonAdaptedAttendance(@JsonProperty("date") String date,
                                 @JsonProperty("presentStudents") List<JsonAdaptedIdentificationNumber>
                                         presentStudents) {
        this.date = date;
        if (presentStudents != null) {
            this.presentStudents.addAll(presentStudents);
        }
    }

    public String getDate() {
        return date;
    }

    public List<JsonAdaptedIdentificationNumber> getPresentStudents() {
        return new ArrayList<>(presentStudents);
    }
}
