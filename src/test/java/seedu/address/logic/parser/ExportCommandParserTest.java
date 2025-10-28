package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ExportCommand;

public class ExportCommandParserTest {

    private ExportCommandParser parser = new ExportCommandParser();

    @Test
    public void parse_validArgs_returnsExportCommand() {
        String fileName = "my_backup.json";
        ExportCommand expectedExportCommand = new ExportCommand(Paths.get(fileName));

        // Standard case
        assertParseSuccess(parser, fileName, expectedExportCommand);

        // With whitespace
        assertParseSuccess(parser, "   " + fileName + "   ", expectedExportCommand);

        // With absolute path (simple version)
        String absPath = "/data/my_backup.json";
        assertParseSuccess(parser, absPath, new ExportCommand(Paths.get(absPath)));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // Empty string
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));

        // Blank string
        assertParseFailure(parser, "  ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));

        // Invalid file extension
        assertParseFailure(parser, "test.txt", "File must be a .json file.");

        // No file extension
        assertParseFailure(parser, "test", "File must be a .json file.");

        // Invalid path (contains null character)
        assertParseFailure(parser, "test\0.json", "Invalid file path provided: test\0.json");
    }
}