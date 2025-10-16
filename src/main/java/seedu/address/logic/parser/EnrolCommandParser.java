package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;

import java.util.stream.Stream;

import seedu.address.logic.commands.EnrolCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.lesson.ClassName;
import seedu.address.model.person.IdentificationNumber;

/**
 * Parses input arguments and creates a new EnrolCommand object
 */
public class EnrolCommandParser implements Parser<EnrolCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EnrolCommand
     * and returns an EnrolCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EnrolCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ID, PREFIX_CLASS);

        if (!arePrefixesPresent(argMultimap, PREFIX_ID, PREFIX_CLASS)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EnrolCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_ID, PREFIX_CLASS);

        IdentificationNumber studentId = ParserUtil.parseIdentificationNumber(argMultimap.getValue(PREFIX_ID).get());
        ClassName className = ParserUtil.parseClassName(argMultimap.getValue(PREFIX_CLASS).get());

        return new EnrolCommand(studentId, className);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
