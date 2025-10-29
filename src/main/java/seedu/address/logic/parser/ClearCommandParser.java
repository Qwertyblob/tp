package seedu.address.logic.parser;

import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class ClearCommandParser implements Parser<ClearCommand> {
    public ClearCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.equals("-f")) {
            return new ClearCommand(true);
        }
        return new ClearCommand();
    }
}
