package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_PLAYER_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_PLAYER_BEN;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_PLAYER_BEN;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_PLAYER_BEN;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_PLAYER_BEN;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_PLAYER_BEN;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.testutil.EditPersonDescriptorBuilder;

public class EditPersonDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditPersonDescriptor descriptorWithSameValues = new EditPersonDescriptor(DESC_PLAYER_AMY);
        assertTrue(DESC_PLAYER_AMY.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_PLAYER_AMY.equals(DESC_PLAYER_AMY));

        // null -> returns false
        assertFalse(DESC_PLAYER_AMY.equals(null));

        // different types -> returns false
        assertFalse(DESC_PLAYER_AMY.equals(5));

        // different values -> returns false
        assertFalse(DESC_PLAYER_AMY.equals(DESC_PLAYER_BEN));

        // different name -> returns false
        EditPersonDescriptor editedAmy =
                new EditPersonDescriptorBuilder(DESC_PLAYER_AMY).withName(VALID_NAME_PLAYER_BEN).build();
        assertFalse(DESC_PLAYER_AMY.equals(editedAmy));

        // different phone -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_PLAYER_AMY).withPhone(VALID_PHONE_PLAYER_BEN).build();
        assertFalse(DESC_PLAYER_AMY.equals(editedAmy));

        // different email -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_PLAYER_AMY).withEmail(VALID_EMAIL_PLAYER_BEN).build();
        assertFalse(DESC_PLAYER_AMY.equals(editedAmy));

        // different address -> returns false
        editedAmy =
                new EditPersonDescriptorBuilder(DESC_PLAYER_AMY).withAddress(VALID_ADDRESS_PLAYER_BEN).build();
        assertFalse(DESC_PLAYER_AMY.equals(editedAmy));

        // different tags -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_PLAYER_AMY).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(DESC_PLAYER_AMY.equals(editedAmy));
    }

    @Test
    public void toStringMethod() {
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        String expected =
                EditPersonDescriptor.class.getCanonicalName() + "{name=" + editPersonDescriptor.getName().orElse(null)
                        + ", phone=" + editPersonDescriptor.getPhone().orElse(null) + ", email="
                        + editPersonDescriptor.getEmail().orElse(null) + ", address=" + editPersonDescriptor.getRole()
                        .orElse(null) + ", role=" + editPersonDescriptor.getAddress().orElse(null) + ", tags="
                        + editPersonDescriptor.getTags().orElse(null) + "}";
        assertEquals(expected, editPersonDescriptor.toString());
    }
}
