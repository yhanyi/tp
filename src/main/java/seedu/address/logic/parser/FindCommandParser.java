package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.student.CourseContainsKeywordsPredicate;
import seedu.address.model.student.NameContainsKeywordsPredicate;
import seedu.address.model.student.Student;

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
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_COURSE);

        if (argMultimap.getPreamble().isEmpty()
                && (!arePrefixesPresent(argMultimap, PREFIX_NAME)
                && !arePrefixesPresent(argMultimap, PREFIX_COURSE))) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        List<Predicate<Student>> predicates = new ArrayList<>();

        if (arePrefixesPresent(argMultimap, PREFIX_NAME)) {
            NameContainsKeywordsPredicate namePredicate =
                    new NameContainsKeywordsPredicate(argMultimap.getAllValues(PREFIX_NAME));
            predicates.add(namePredicate);
        }

        if (arePrefixesPresent(argMultimap, PREFIX_COURSE)) {
            CourseContainsKeywordsPredicate coursePredicate =
                    new CourseContainsKeywordsPredicate(argMultimap.getAllValues(PREFIX_COURSE));
            predicates.add(coursePredicate);
        }

        Predicate<Student> finalPredicate = predicates.stream().reduce(x -> true, Predicate::and);
        return new FindCommand(finalPredicate);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
