package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ImportCommand object
 */
public class ImportCommandParser implements Parser<ImportCommand> {

    @Override
    public ImportCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
        }

        try {
            Path filePath = Paths.get(trimmedArgs);

            // Enforce that the file must end with .json
            String fileName = filePath.getFileName().toString().toLowerCase();
            if (!fileName.endsWith(".json")) {
                throw new ParseException("File must be a .json file.");
            }

            return new ImportCommand(filePath);

        } catch (InvalidPathException ipe) {
            throw new ParseException("Invalid file path provided: " + trimmedArgs, ipe);
        }
    }

}
