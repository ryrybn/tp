package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonHasRolePredicateTest {

    @Test
    public void equals() {
        PersonHasRolePredicate playerPredicate = new PersonHasRolePredicate(Role.PLAYER);
        PersonHasRolePredicate staffPredicate = new PersonHasRolePredicate(Role.STAFF);

        // same object -> returns true
        assertTrue(playerPredicate.equals(playerPredicate));

        // same role -> returns true
        PersonHasRolePredicate playerPredicateCopy = new PersonHasRolePredicate(Role.PLAYER);
        assertTrue(playerPredicate.equals(playerPredicateCopy));

        // different types -> returns false
        assertFalse(playerPredicate.equals(1));

        // null -> returns false
        assertFalse(playerPredicate.equals(null));

        // different role -> returns false
        assertFalse(playerPredicate.equals(staffPredicate));
    }

    @Test
    public void test_personHasMatchingRole_returnsTrue() {
        // player predicate matches player
        PersonHasRolePredicate predicate = new PersonHasRolePredicate(Role.PLAYER);
        assertTrue(predicate.test(new PersonBuilder().withRole(Role.PLAYER).build()));

        // staff predicate matches staff
        predicate = new PersonHasRolePredicate(Role.STAFF);
        assertTrue(predicate.test(new PersonBuilder().withRole(Role.STAFF).build()));
    }

    @Test
    public void test_personHasNonMatchingRole_returnsFalse() {
        // player predicate does not match staff
        PersonHasRolePredicate predicate = new PersonHasRolePredicate(Role.PLAYER);
        assertFalse(predicate.test(new PersonBuilder().withRole(Role.STAFF).build()));

        // staff predicate does not match player
        predicate = new PersonHasRolePredicate(Role.STAFF);
        assertFalse(predicate.test(new PersonBuilder().withRole(Role.PLAYER).build()));
    }

    @Test
    public void test_personHasPlayerRole_returnsTrue() {
        PersonHasRolePredicate playerPredicate = new PersonHasRolePredicate(Role.PLAYER);
        assertTrue(playerPredicate.test(new PersonBuilder().withRole(Role.PLAYER).build()));
    }

    @Test
    public void test_personHasStaffRole_returnsTrue() {
        PersonHasRolePredicate staffPredicate = new PersonHasRolePredicate(Role.STAFF);
        assertTrue(staffPredicate.test(new PersonBuilder().withRole(Role.STAFF).build()));
    }
}

