package seedu.address.logic.parser;

import java.util.Locale;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteCommand.DeletionDecision;

/**
 * Handles parser-level continuation input for the multi-step delete interaction flow.
 */
class DeleteInteractionFlow {
    private PendingDeleteContext pendingDeleteContext;

    String preprocessInput(String trimmedInput) {
        if (pendingDeleteContext == null) {
            return trimmedInput;
        }

        if (pendingDeleteContext.pendingDeleteBaseCommand != null && isYesNo(trimmedInput)) {
            return buildFollowUpCommand(trimmedInput.toLowerCase(Locale.ROOT));
        }

        if (pendingDeleteContext.pendingDeleteCriteria != null && isPositiveInteger(trimmedInput)) {
            return DeleteCommand.COMMAND_WORD + " " + pendingDeleteContext.pendingDeleteCriteria + " " + trimmedInput;
        }

        pendingDeleteContext = null;
        return trimmedInput;
    }

    void updateAfterParse(Command command) {
        if (!(command instanceof DeleteCommand)) {
            pendingDeleteContext = null;
            return;
        }

        DeleteCommand deleteCommand = (DeleteCommand) command;
        if (deleteCommand.getDeletionDecision() != DeletionDecision.UNDECIDED) {
            pendingDeleteContext = null;
            return;
        }

        if (deleteCommand.isCriteriaDelete()) {
            if (deleteCommand.getCriteriaMatchIndex() == null) {
                pendingDeleteContext = new PendingDeleteContext(
                        DeleteCommand.COMMAND_WORD + " " + deleteCommand.getCriteria(),
                        deleteCommand.getCriteria(), false);
                return;
            }

            pendingDeleteContext = new PendingDeleteContext(buildBaseDeleteCommand(deleteCommand), null, false);
            return;
        }

        pendingDeleteContext = new PendingDeleteContext(buildBaseDeleteCommand(deleteCommand), null, true);
    }

    private String buildBaseDeleteCommand(DeleteCommand deleteCommand) {
        if (!deleteCommand.isCriteriaDelete()) {
            return DeleteCommand.COMMAND_WORD + " " + deleteCommand.getTargetIndex().getOneBased();
        }

        StringBuilder commandBuilder = new StringBuilder(DeleteCommand.COMMAND_WORD)
                .append(" ")
                .append(deleteCommand.getCriteria());
        Index criteriaMatchIndex = deleteCommand.getCriteriaMatchIndex();
        if (criteriaMatchIndex != null) {
            commandBuilder.append(" ").append(criteriaMatchIndex.getOneBased());
        }
        return commandBuilder.toString();
    }

    private String buildFollowUpCommand(String normalizedInput) {
        if (pendingDeleteContext.isIndexBasedDelete && normalizedInput.equals(DeleteCommand.YES_KEYWORD)) {
            return pendingDeleteContext.pendingDeleteBaseCommand + " " + DeleteCommand.CONFIRM_KEYWORD;
        }

        return pendingDeleteContext.pendingDeleteBaseCommand + " " + normalizedInput;
    }

    private boolean isYesNo(String input) {
        return input.equalsIgnoreCase(DeleteCommand.YES_KEYWORD)
                || input.equalsIgnoreCase(DeleteCommand.NO_KEYWORD);
    }

    private boolean isPositiveInteger(String input) {
        return input.matches("[1-9]\\d*");
    }

    private static class PendingDeleteContext {
        private final String pendingDeleteBaseCommand;
        private final String pendingDeleteCriteria;
        private final boolean isIndexBasedDelete;

        private PendingDeleteContext(String pendingDeleteBaseCommand, String pendingDeleteCriteria,
                                     boolean isIndexBasedDelete) {
            this.pendingDeleteBaseCommand = pendingDeleteBaseCommand;
            this.pendingDeleteCriteria = pendingDeleteCriteria;
            this.isIndexBasedDelete = isIndexBasedDelete;
        }
    }
}
