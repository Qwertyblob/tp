package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import seedu.address.logic.commands.UnmarkCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.lesson.ClassName;
import seedu.address.model.person.IdentificationNumber;

/**
 * Parses input arguments and creates a new UnmarkCommand object.
 */
public class UnmarkCommandParser implements Parser<UnmarkCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the UnmarkCommand
     * and returns an UnmarkCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public UnmarkCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ID, PREFIX_CLASS, PREFIX_DATE);

        if (!arePrefixesPresent(argMultimap, PREFIX_ID, PREFIX_CLASS)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    UnmarkCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_ID, PREFIX_CLASS, PREFIX_DATE);

        IdentificationNumber studentId = ParserUtil.parseIdentificationNumber(argMultimap.getValue(PREFIX_ID).get());
        ClassName className = ParserUtil.parseClassName(argMultimap.getValue(PREFIX_CLASS).get());

        Optional<LocalDate> date = Optional.empty();
        if (argMultimap.getValue(PREFIX_DATE).isPresent()) {
            date = Optional.of(ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).get()));
        }

        return new UnmarkCommand(studentId, className, date);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
