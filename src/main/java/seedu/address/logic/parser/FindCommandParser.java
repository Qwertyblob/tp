package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.ContactMatchesPredicate;
import seedu.address.model.person.IdentificationNumber;
import seedu.address.model.person.Role;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        requireNonNull(args);
        args = args.replaceAll("\\s+", " ");

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ID, PREFIX_NAME, PREFIX_ROLE, PREFIX_CLASS,
                        PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_ID, PREFIX_NAME, PREFIX_ROLE, PREFIX_CLASS,
                PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);

        String id = argMultimap.getValue(PREFIX_ID).orElse("").trim();
        String name = argMultimap.getValue(PREFIX_NAME).orElse("").trim();
        String role = argMultimap.getValue(PREFIX_ROLE).orElse("").trim();
        String lesson = argMultimap.getValue(PREFIX_CLASS).orElse("").trim();
        String phone = argMultimap.getValue(PREFIX_PHONE).orElse("").trim();
        String email = argMultimap.getValue(PREFIX_EMAIL).orElse("").trim();
        String address = argMultimap.getValue(PREFIX_ADDRESS).orElse("").trim();
        String tags = argMultimap.getValue(PREFIX_TAG).orElse("").trim();

        // Id must be a valid ID
        if (!id.isEmpty() && !id.matches(IdentificationNumber.VALIDATION_REGEX)) {
            throw new ParseException(IdentificationNumber.MESSAGE_CONSTRAINTS);
        }

        // Roles should only be student or tutor
        if (!role.isEmpty() && !role.matches(Role.VALIDATION_REGEX)) {
            throw new ParseException(Role.MESSAGE_CONSTRAINTS);
        }

        if (id.isEmpty() && name.isEmpty() && role.isEmpty() && lesson.isEmpty()
                && phone.isEmpty() && email.isEmpty() && address.isEmpty() && tags.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        ContactMatchesPredicate predicate =
                new ContactMatchesPredicate(id, name, role, lesson, phone, email, address, tags);
        return new FindCommand(predicate);
    }
}
