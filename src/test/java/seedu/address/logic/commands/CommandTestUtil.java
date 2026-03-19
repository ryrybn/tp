package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.testutil.EditPersonDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_PLAYER_BEN = "Ben Tan";
    public static final String VALID_NAME_JOHN = "JOHN";
    public static final String VALID_PHONE_PLAYER_BEN = "99999999";
    public static final String VALID_EMAIL_PLAYER_BEN = "Player_Ben@example.com";
    public static final String VALID_ADDRESS_PLAYER_BEN = "Block 123, Street 3";
    public static final String VALID_NAME_PLAYER_AMY = "Amy Bee";
    public static final String VALID_PHONE_PLAYER_AMY = "98888888";
    public static final String VALID_EMAIL_PLAYER_AMY = "Player_Amy@example.com";
    public static final String VALID_ADDRESS_PLAYER_AMY = "Block 33, Street 34";
    public static final String VALID_TAG_HUSBAND = "husband";
    public static final String VALID_TAG_FRIEND = "friend";
    public static final String VALID_ROLE_PLAYER = "PLAYER";
    public static final String VALID_ROLE_STAFF = "STAFF";

    public static final String NAME_DESC_PLAYER_BEN = " " + PREFIX_NAME + VALID_NAME_PLAYER_BEN;
    public static final String PHONE_DESC_PLAYER_BEN = " " + PREFIX_PHONE + VALID_PHONE_PLAYER_BEN;
    public static final String EMAIL_DESC_PLAYER_BEN = " " + PREFIX_EMAIL + VALID_EMAIL_PLAYER_BEN;
    public static final String ADDRESS_DESC_PLAYER_BEN = " " + PREFIX_ADDRESS + VALID_ADDRESS_PLAYER_BEN;
    public static final String NAME_DESC_PLAYER_AMY = " " + PREFIX_NAME + VALID_NAME_PLAYER_AMY;
    public static final String PHONE_DESC_PLAYER_AMY = " " + PREFIX_PHONE + VALID_PHONE_PLAYER_AMY;
    public static final String EMAIL_DESC_PLAYER_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_PLAYER_AMY;
    public static final String ADDRESS_DESC_PLAYER_AMY = " " + PREFIX_ADDRESS + VALID_ADDRESS_PLAYER_AMY;
    public static final String TAG_DESC_FRIEND = " " + PREFIX_TAG + VALID_TAG_FRIEND;
    public static final String TAG_DESC_HUSBAND = " " + PREFIX_TAG + VALID_TAG_HUSBAND;
    public static final String ROLE_DESC_PLAYER = " " + PREFIX_ROLE + VALID_ROLE_PLAYER;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "PLAYER_BOB!yahoo"; // missing '@' symbol
    public static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS; // empty string not allowed for addresses
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "hubby*"; // '*' not allowed in tags
    public static final String INVALID_ROLE_DESC = " " + PREFIX_ROLE + "invalidRole";

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditPersonDescriptor DESC_PLAYER_AMY;
    public static final EditCommand.EditPersonDescriptor DESC_PLAYER_BEN;

    static {
        DESC_PLAYER_AMY =
                new EditPersonDescriptorBuilder().withName(VALID_NAME_PLAYER_AMY).withPhone(VALID_PHONE_PLAYER_AMY)
                        .withEmail(VALID_EMAIL_PLAYER_AMY).withAddress(VALID_ADDRESS_PLAYER_AMY)
                        .withTags(VALID_TAG_FRIEND).withRole(VALID_ROLE_PLAYER).build();
        DESC_PLAYER_BEN =
                new EditPersonDescriptorBuilder().withName(VALID_NAME_PLAYER_BEN).withPhone(VALID_PHONE_PLAYER_BEN)
                        .withEmail(VALID_EMAIL_PLAYER_BEN).withAddress(VALID_ADDRESS_PLAYER_BEN)
                        .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).withRole(VALID_ROLE_PLAYER).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
                                            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
                                            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book, filtered person list and selected person in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        AddressBook expectedAddressBook = new AddressBook(actualModel.getAddressBook());
        List<Person> expectedFilteredList = new ArrayList<>(actualModel.getFilteredPersonList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedAddressBook, actualModel.getAddressBook());
        assertEquals(expectedFilteredList, actualModel.getFilteredPersonList());
    }

    /**
     * Updates {@code model}'s filtered list to show only the person at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showPersonAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredPersonList().size());

        Person person = model.getFilteredPersonList().get(targetIndex.getZeroBased());
        final String[] splitName = person.getName().fullName.split("\\s+");
        model.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredPersonList().size());
    }

}
