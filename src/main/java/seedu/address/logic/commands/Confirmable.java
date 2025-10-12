package seedu.address.logic.commands;

import seedu.address.model.Model;

public interface Confirmable {
    String getConfirmationMessage(Model model);
}
