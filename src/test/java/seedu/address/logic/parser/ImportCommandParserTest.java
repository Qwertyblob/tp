package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ImportCommand;

public class ImportCommandParserTest {

    private ImportCommandParser parser = new ImportCommandParser();

    @Test
    public void parse_validArgs_returnsImportCommand() {
        String fileName = "my_backup.json";
        ImportCommand expectedImportCommand = new ImportCommand(Paths.get(fileName));

        // Standard case
        assertParseSuccess(parser, fileName, expectedImportCommand);

        // With whitespace
        assertParseSuccess(parser, "   " + fileName + "   ", expectedImportCommand);

        // With absolute path
        String absPath = "/data/my_backup.json";
        assertParseSuccess(parser, absPath, new ImportCommand(Paths.get(absPath)));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // Empty string
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));

        // Blank string
        assertParseFailure(parser, "  ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));

        // Invalid file extension
        assertParseFailure(parser, "test.txt", "File must be a .json file.");

        // No file extension
        assertParseFailure(parser, "test", "File must be a .json file.");

        // Invalid path (contains null character)
        assertParseFailure(parser, "test\0.json", "Invalid file path provided: test\0.json");
    }

}
