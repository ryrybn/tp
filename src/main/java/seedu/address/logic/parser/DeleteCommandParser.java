package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        try {
            String trimmedArgs = args.trim();
            String[] tokens = trimmedArgs.split("\\s+");

            if (tokens.length == 1) {
                Index index = ParserUtil.parseIndex(tokens[0]);
                return new DeleteCommand(index, false);
            }

            if (tokens.length == 2 && tokens[1].equals(DeleteCommand.CONFIRM_KEYWORD)) {
                Index index = ParserUtil.parseIndex(tokens[0]);
                return new DeleteCommand(index, true);
            }

            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE), pe);
        }
    }

}
