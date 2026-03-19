package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_PLAYER_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_PLAYER_BEN;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_PLAYER_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_PLAYER_BEN;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ROLE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_PLAYER_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_PLAYER_BEN;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_PLAYER_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_PLAYER_BEN;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.ROLE_DESC_PLAYER;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_PLAYER_BEN;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_PLAYER_BEN;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_PLAYER_BEN;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_PLAYER_BEN;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_PLAYER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalPersons.PLAYER_AMY;
import static seedu.address.testutil.TypicalPersons.PLAYER_BEN;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddCommand;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = new PersonBuilder(PLAYER_BEN).withTags(VALID_TAG_FRIEND).build();

        // whitespace only preamble
        assertParseSuccess(parser,
                PREAMBLE_WHITESPACE + NAME_DESC_PLAYER_BEN + PHONE_DESC_PLAYER_BEN + EMAIL_DESC_PLAYER_BEN
                        + ADDRESS_DESC_PLAYER_BEN + ROLE_DESC_PLAYER + TAG_DESC_FRIEND, new AddCommand(expectedPerson));

        // allow player as preamble
        assertParseSuccess(parser,
                ROLE_DESC_PLAYER + NAME_DESC_PLAYER_BEN + PHONE_DESC_PLAYER_BEN
                + EMAIL_DESC_PLAYER_BEN + ADDRESS_DESC_PLAYER_BEN + TAG_DESC_FRIEND, new AddCommand(expectedPerson));

        // multiple tags - all accepted
        Person expectedPersonMultipleTags =
                new PersonBuilder(PLAYER_BEN).withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND).build();
        assertParseSuccess(parser,
                NAME_DESC_PLAYER_BEN + PHONE_DESC_PLAYER_BEN + EMAIL_DESC_PLAYER_BEN + ADDRESS_DESC_PLAYER_BEN
                        + ROLE_DESC_PLAYER + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                new AddCommand(expectedPersonMultipleTags));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedPersonString =
                NAME_DESC_PLAYER_BEN + PHONE_DESC_PLAYER_BEN + EMAIL_DESC_PLAYER_BEN + ADDRESS_DESC_PLAYER_BEN
                        + ROLE_DESC_PLAYER + TAG_DESC_FRIEND;

        // multiple names
        assertParseFailure(parser, NAME_DESC_PLAYER_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple phones
        assertParseFailure(parser, PHONE_DESC_PLAYER_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple emails
        assertParseFailure(parser, EMAIL_DESC_PLAYER_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // multiple addresses
        assertParseFailure(parser, ADDRESS_DESC_PLAYER_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // multiple roles
        assertParseFailure(parser, ROLE_DESC_PLAYER + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ROLE));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedPersonString + PHONE_DESC_PLAYER_AMY + EMAIL_DESC_PLAYER_AMY + NAME_DESC_PLAYER_AMY
                        + ADDRESS_DESC_PLAYER_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_ADDRESS, PREFIX_EMAIL,
                        PREFIX_PHONE, PREFIX_ROLE));

        // invalid value followed by valid value

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, INVALID_EMAIL_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, INVALID_PHONE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, INVALID_ADDRESS_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // invalid role
        assertParseFailure(parser, INVALID_ROLE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ROLE));

        // valid value followed by invalid value

        // invalid name
        assertParseFailure(parser, validExpectedPersonString + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, validExpectedPersonString + INVALID_EMAIL_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, validExpectedPersonString + INVALID_PHONE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, validExpectedPersonString + INVALID_ADDRESS_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // invalid role
        assertParseFailure(parser, validExpectedPersonString + INVALID_ROLE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ROLE));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Person expectedPerson = new PersonBuilder(PLAYER_AMY).withTags().build();
        assertParseSuccess(parser,
                NAME_DESC_PLAYER_AMY + PHONE_DESC_PLAYER_AMY + EMAIL_DESC_PLAYER_AMY + ADDRESS_DESC_PLAYER_AMY
                        + ROLE_DESC_PLAYER,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser,
                VALID_NAME_PLAYER_BEN + PHONE_DESC_PLAYER_BEN + EMAIL_DESC_PLAYER_BEN + ADDRESS_DESC_PLAYER_BEN
                        + ROLE_DESC_PLAYER,
                expectedMessage);

        // missing phone prefix
        assertParseFailure(parser,
                NAME_DESC_PLAYER_BEN + VALID_PHONE_PLAYER_BEN + EMAIL_DESC_PLAYER_BEN + ADDRESS_DESC_PLAYER_BEN
                        + ROLE_DESC_PLAYER,
                expectedMessage);

        // missing email prefix
        assertParseFailure(parser,
                NAME_DESC_PLAYER_BEN + PHONE_DESC_PLAYER_BEN + VALID_EMAIL_PLAYER_BEN + ADDRESS_DESC_PLAYER_BEN
                        + ROLE_DESC_PLAYER,
                expectedMessage);

        // missing address prefix
        assertParseFailure(parser,
                NAME_DESC_PLAYER_BEN + PHONE_DESC_PLAYER_BEN + EMAIL_DESC_PLAYER_BEN + VALID_ADDRESS_PLAYER_BEN
                        + ROLE_DESC_PLAYER,
                expectedMessage);

        // missing role prefix
        assertParseFailure(parser,
                NAME_DESC_PLAYER_BEN + PHONE_DESC_PLAYER_BEN + EMAIL_DESC_PLAYER_BEN + ADDRESS_DESC_PLAYER_BEN
                        + VALID_ROLE_PLAYER,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser,
                VALID_NAME_PLAYER_BEN + VALID_PHONE_PLAYER_BEN + VALID_EMAIL_PLAYER_BEN + VALID_ADDRESS_PLAYER_BEN
                        + VALID_ROLE_PLAYER,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser,
                INVALID_NAME_DESC + PHONE_DESC_PLAYER_BEN + EMAIL_DESC_PLAYER_BEN + ADDRESS_DESC_PLAYER_BEN
                        + ROLE_DESC_PLAYER + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser,
                NAME_DESC_PLAYER_BEN + INVALID_PHONE_DESC + EMAIL_DESC_PLAYER_BEN + ADDRESS_DESC_PLAYER_BEN
                        + ROLE_DESC_PLAYER + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser,
                NAME_DESC_PLAYER_BEN + PHONE_DESC_PLAYER_BEN + INVALID_EMAIL_DESC + ADDRESS_DESC_PLAYER_BEN
                        + ROLE_DESC_PLAYER + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Email.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser,
                NAME_DESC_PLAYER_BEN + PHONE_DESC_PLAYER_BEN + EMAIL_DESC_PLAYER_BEN + INVALID_ADDRESS_DESC
                        + ROLE_DESC_PLAYER + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Address.MESSAGE_CONSTRAINTS);

        // invalid role
        assertParseFailure(parser,
                NAME_DESC_PLAYER_BEN + PHONE_DESC_PLAYER_BEN + EMAIL_DESC_PLAYER_BEN + INVALID_ADDRESS_DESC
                        + INVALID_ROLE_DESC + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Address.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser,
                NAME_DESC_PLAYER_BEN + PHONE_DESC_PLAYER_BEN + EMAIL_DESC_PLAYER_BEN + ADDRESS_DESC_PLAYER_BEN
                        + ROLE_DESC_PLAYER + INVALID_TAG_DESC + VALID_TAG_FRIEND, Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser,
                INVALID_NAME_DESC + PHONE_DESC_PLAYER_BEN + EMAIL_DESC_PLAYER_BEN + ROLE_DESC_PLAYER
                        + INVALID_ADDRESS_DESC,
                Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser,
                PREAMBLE_NON_EMPTY + NAME_DESC_PLAYER_BEN + PHONE_DESC_PLAYER_BEN + EMAIL_DESC_PLAYER_BEN
                        + ADDRESS_DESC_PLAYER_BEN + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_staff_createsStaff() throws Exception {
        String testInput = " n/John Doe " + "r/staff " + "p/98765432 " + "e/john@email.com " + "a/123 Clementi Rd";
        AddCommand command = parser.parse(testInput);
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        command.execute(modelStub);
        assertTrue(modelStub.personsAdded.get(0).getRole() == Role.STAFF);
    }

    /**
     * A Model stub that always accept the person being added.
     */
    private static class ModelStubAcceptingPersonAdded implements Model {
        final ArrayList<Person> personsAdded = new ArrayList<>();

        @Override
        public boolean hasPerson(Person person) {
            return false;
        }

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            personsAdded.add(person);
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }
}
