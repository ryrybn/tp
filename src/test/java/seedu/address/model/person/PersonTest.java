package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_PLAYER_BEN;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_PLAYER_BEN;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_PLAYER_BEN;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_PLAYER_BEN;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.PLAYER_AMY;
import static seedu.address.testutil.TypicalPersons.PLAYER_BEN;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(PLAYER_AMY.isSamePerson(PLAYER_AMY));

        // null -> returns false
        assertFalse(PLAYER_AMY.isSamePerson(null));

        // same name, all other attributes different -> returns true
        Person editedAmy =
                new PersonBuilder(PLAYER_AMY).withPhone(VALID_PHONE_PLAYER_BEN).withEmail(VALID_EMAIL_PLAYER_BEN)
                        .withAddress(VALID_ADDRESS_PLAYER_BEN).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(PLAYER_AMY.isSamePerson(editedAmy));

        // different name, all other attributes same -> returns false
        editedAmy = new PersonBuilder(PLAYER_AMY).withName(VALID_NAME_PLAYER_BEN).build();
        assertFalse(PLAYER_AMY.isSamePerson(editedAmy));

        // name differs in case, all other attributes same -> returns false
        Person editedBen = new PersonBuilder(PLAYER_BEN).withName(VALID_NAME_PLAYER_BEN.toLowerCase()).build();
        assertFalse(PLAYER_BEN.isSamePerson(editedBen));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_PLAYER_BEN + " ";
        editedBen = new PersonBuilder(PLAYER_BEN).withName(nameWithTrailingSpaces).build();
        assertFalse(PLAYER_BEN.isSamePerson(editedBen));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person amyCopy = new PersonBuilder(PLAYER_AMY).build();
        assertTrue(PLAYER_AMY.equals(amyCopy));

        // same object -> returns true
        assertTrue(PLAYER_AMY.equals(PLAYER_AMY));

        // null -> returns false
        assertFalse(PLAYER_AMY.equals(null));

        // different type -> returns false
        assertFalse(PLAYER_AMY.equals(5));

        // different person -> returns false
        assertFalse(PLAYER_AMY.equals(PLAYER_BEN));

        // different name -> returns false
        Person editedAmy = new PersonBuilder(PLAYER_AMY).withName(VALID_NAME_PLAYER_BEN).build();
        assertFalse(PLAYER_AMY.equals(editedAmy));

        // different phone -> returns false
        editedAmy = new PersonBuilder(PLAYER_AMY).withPhone(VALID_PHONE_PLAYER_BEN).build();
        assertFalse(PLAYER_AMY.equals(editedAmy));

        // different email -> returns false
        editedAmy = new PersonBuilder(PLAYER_AMY).withEmail(VALID_EMAIL_PLAYER_BEN).build();
        assertFalse(PLAYER_AMY.equals(editedAmy));

        // different address -> returns false
        editedAmy = new PersonBuilder(PLAYER_AMY).withAddress(VALID_ADDRESS_PLAYER_BEN).build();
        assertFalse(PLAYER_AMY.equals(editedAmy));

        // different tags -> returns false
        editedAmy = new PersonBuilder(PLAYER_AMY).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(PLAYER_AMY.equals(editedAmy));
    }

    @Test
    public void toStringMethod() {
        String expected = Player.class.getCanonicalName() + "{name=" + PLAYER_AMY.getName()
                + ", role=" + PLAYER_AMY.getRole() + ", phone="
                + PLAYER_AMY.getPhone() + ", email=" + PLAYER_AMY.getEmail() + ", address="
                + PLAYER_AMY.getAddress() + ", tags=" + PLAYER_AMY.getTags() + "}";
        assertEquals(expected, PLAYER_AMY.toString());
    }
}
