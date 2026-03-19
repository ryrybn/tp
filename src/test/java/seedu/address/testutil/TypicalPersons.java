package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.person.Role;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    // Persons with roles
    public static final Person PLAYER_AMY =
            new PersonBuilder().withName("Amy Tan").withPhone("91234567").withEmail("amytan@example.com")
                    .withAddress("5th street").withTags("goalkeeper").withRole(Role.PLAYER).build();
    public static final Person PLAYER_BEN =
            new PersonBuilder().withName("Ben Lim").withPhone("92345678").withEmail("benlim@example.com")
                    .withAddress("6th street").withRole(Role.PLAYER).build();
    public static final Person PLAYER_CHARLIE =
            new PersonBuilder().withName("Charlie Ng").withPhone("93456789").withEmail("charlieng@example.com")
                    .withAddress("7th street").withRole(Role.PLAYER).build();
    public static final Person STAFF_DIVA =
            new PersonBuilder().withName("Diva Meier").withPhone("94433443").withEmail("divameier@example.com")
                    .withAddress("chinatown").withRole(Role.STAFF).build();
    public static final Person STAFF_HOON =
            new PersonBuilder().withName("Hoon Meier").withPhone("84824243").withEmail("hoonmeier@example.com")
                    .withAddress("little india").withRole(Role.STAFF).build();
    public static final Person STAFF_IDA =
            new PersonBuilder().withName("Ida Mueller").withPhone("84821311").withEmail("idamueller@example.com")
                    .withAddress("chicago ave").withRole(Role.STAFF).build();

    // Extra persons for testing
    public static final Person PLAYER_TYRONE =
            new PersonBuilder().withName("Tyrone Williams").withPhone("91273731")
                    .withEmail("tyronewilliams@example.com")
                    .withAddress("texas street").withRole(Role.PLAYER).build();

    public static final Person STAFF_JEFFREY =
            new PersonBuilder().withName("Jeffrey Smith").withPhone("91233555")
                    .withEmail("mynameisjeff@example.com")
                    .withAddress("hougang avenue 3").withRole(Role.STAFF).build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {
    } // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    /**
     * Returns a list of typical persons including persons with roles assigned.
     * Used for testing role-based filtering.
     */
    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(
                Arrays.asList(PLAYER_AMY, PLAYER_BEN, PLAYER_CHARLIE, STAFF_DIVA, STAFF_HOON, STAFF_IDA));
    }
}
