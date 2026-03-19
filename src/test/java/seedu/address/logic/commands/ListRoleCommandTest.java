package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.PersonHasRolePredicate;
import seedu.address.model.person.Role;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListRoleCommand.
 */
public class ListRoleCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_listPlayers_success() {
        PersonHasRolePredicate predicate = new PersonHasRolePredicate(Role.PLAYER);
        ListRoleCommand command = new ListRoleCommand(predicate, "players");
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, String.format(ListRoleCommand.MESSAGE_SUCCESS, "players"), expectedModel);
    }

    @Test
    public void execute_listStaff_success() {
        PersonHasRolePredicate predicate = new PersonHasRolePredicate(Role.STAFF);
        ListRoleCommand command = new ListRoleCommand(predicate, "staff");
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, String.format(ListRoleCommand.MESSAGE_SUCCESS, "staff"), expectedModel);
    }

    @Test
    public void equals() {
        PersonHasRolePredicate playerPredicate = new PersonHasRolePredicate(Role.PLAYER);
        PersonHasRolePredicate staffPredicate = new PersonHasRolePredicate(Role.STAFF);

        ListRoleCommand listPlayersCommand = new ListRoleCommand(playerPredicate, "players");
        ListRoleCommand listStaffCommand = new ListRoleCommand(staffPredicate, "staff");

        // same object -> returns true
        assertTrue(listPlayersCommand.equals(listPlayersCommand));

        // same predicate -> returns true
        ListRoleCommand listPlayersCommandCopy = new ListRoleCommand(playerPredicate, "players");
        assertTrue(listPlayersCommand.equals(listPlayersCommandCopy));

        // different types -> returns false
        assertFalse(listPlayersCommand.equals(1));

        // null -> returns false
        assertFalse(listPlayersCommand.equals(null));

        // different role -> returns false
        assertFalse(listPlayersCommand.equals(listStaffCommand));
    }

    @Test
    public void execute_listPlayers_correctCount() {
        PersonHasRolePredicate predicate = new PersonHasRolePredicate(Role.PLAYER);
        new ListRoleCommand(predicate, "players").execute(model);
        assertEquals(model.getFilteredPersonList().size(),
                (int) getTypicalAddressBook().getPersonList().stream()
                        .filter(predicate)
                        .count());
    }
}

