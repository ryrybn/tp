package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteCommand.DeletionDecision;

public class DeleteInteractionFlowTest {

    @Test
    public void preprocessInput_indexDelete_yesMapsToConfirm() {
        DeleteInteractionFlow flow = new DeleteInteractionFlow();
        flow.updateAfterParse(new DeleteCommand(INDEX_FIRST_PERSON));

        assertEquals("delete 1 confirm", flow.preprocessInput("y"));
    }

    @Test
    public void preprocessInput_criteriaDelete_supportsIndexThenYes() {
        DeleteInteractionFlow flow = new DeleteInteractionFlow();
        flow.updateAfterParse(new DeleteCommand("meier", null, DeletionDecision.UNDECIDED));

        assertEquals("delete meier 2", flow.preprocessInput("2"));

        flow.updateAfterParse(new DeleteCommand("meier", INDEX_SECOND_PERSON, DeletionDecision.UNDECIDED));
        assertEquals("delete meier 2 y", flow.preprocessInput("y"));
    }

    @Test
    public void preprocessInput_invalidFollowUp_clearsPendingContext() {
        DeleteInteractionFlow flow = new DeleteInteractionFlow();
        flow.updateAfterParse(new DeleteCommand(INDEX_FIRST_PERSON));

        assertEquals("gg", flow.preprocessInput("gg"));
        assertEquals("y", flow.preprocessInput("y"));
    }
}
