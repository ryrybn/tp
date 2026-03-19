package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;

/**
 * Deletes a person by displayed index or by matching name keywords.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";
    public static final String CONFIRM_KEYWORD = "confirm";
    public static final String YES_KEYWORD = "y";
    public static final String NO_KEYWORD = "n";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes a player by list index or name keywords.\n"
            + "You can confirm with Y/N after selecting a player.\n"
            + "Parameters:\n"
            + "  1) INDEX\n"
            + "  2) NAME [MATCH_INDEX]\n"
            + "Examples:\n"
            + "  " + COMMAND_WORD + " 3\n"
            + "  " + COMMAND_WORD + " Ryan\n";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted player: %1$s";
    public static final String MESSAGE_DELETE_PERSON_CANCELLED = "Deletion cancelled for player: %1$s";
    public static final String MESSAGE_NO_MATCHING_PERSON = "No players found matching: %1$s";
    public static final String MESSAGE_INVALID_MATCH_INDEX = "The match index provided is invalid";

    public static final String MESSAGE_DELETE_PERSON_CONFIRMATION = "Selected player for deletion: %1$s\n"
            + "Confirm deletion? (%2$s/%3$s)\n"
            + "Type '%4$s' to delete or '%5$s' to cancel.";
    public static final String MESSAGE_DELETE_PERSON_CLASH = "Multiple matching players found for \"%1$s\":\n%2$s\n"
            + "Type the index corresponding to the player you wish to delete.";

    private final Index targetIndex;
    private final String criteria;
    private final Index criteriaMatchIndex;
    private final DeletionDecision deletionDecision;

    /**
     * Describes whether the user confirmed, cancelled, or has not yet decided on a deletion.
     */
    public enum DeletionDecision {
        UNDECIDED,
        CONFIRM,
        CANCEL
    }

    /**
     * Creates a {@code DeleteCommand} that requests confirmation for deletion by index.
     */
    public DeleteCommand(Index targetIndex) {
        this(targetIndex, DeletionDecision.UNDECIDED);
    }

    /**
     * Creates a {@code DeleteCommand} for deletion by index.
     *
     * @param targetIndex index of the player in the filtered list.
     * @param isConfirmed whether the deletion has been explicitly confirmed.
     */
    public DeleteCommand(Index targetIndex, boolean isConfirmed) {
        this(targetIndex, isConfirmed ? DeletionDecision.CONFIRM : DeletionDecision.UNDECIDED);
    }

    /**
     * Creates a {@code DeleteCommand} for deletion by index with an explicit decision state.
     *
     * @param targetIndex index of the player in the filtered list.
     * @param deletionDecision user confirmation state.
     */
    public DeleteCommand(Index targetIndex, DeletionDecision deletionDecision) {
        this.targetIndex = targetIndex;
        this.criteria = null;
        this.criteriaMatchIndex = null;
        this.deletionDecision = deletionDecision;
    }

    /**
     * Creates a {@code DeleteCommand} based on matching criteria.
     *
     * @param criteria keywords used to find matching players.
     * @param criteriaMatchIndex optional index for selecting a player when there are clashes.
     * @param deletionDecision user confirmation state.
     */
    public DeleteCommand(String criteria, Index criteriaMatchIndex, DeletionDecision deletionDecision) {
        this.targetIndex = null;
        this.criteria = criteria.trim();
        this.criteriaMatchIndex = criteriaMatchIndex;
        this.deletionDecision = deletionDecision;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        return targetIndex != null ? executeIndexDelete(model) : executeCriteriaDelete(model);
    }

    private CommandResult executeIndexDelete(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
        return executeDecision(model, personToDelete);
    }

    private CommandResult executeCriteriaDelete(Model model) throws CommandException {
        NameContainsKeywordsPredicate predicate = buildNamePredicate(criteria);
        model.updateFilteredPersonList(predicate);
        List<Person> matches = model.getFilteredPersonList();
        if (matches.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_NO_MATCHING_PERSON, criteria));
        }

        if (matches.size() > 1 && criteriaMatchIndex == null) {
            String formattedMatches = formatMatches(matches);
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_CLASH, criteria, formattedMatches));
        }

        Person personToDelete = selectPersonFromMatches(matches);
        return executeDecision(model, personToDelete);
    }

    private Person selectPersonFromMatches(List<Person> matches) throws CommandException {
        if (criteriaMatchIndex == null) {
            return matches.get(0);
        }

        if (criteriaMatchIndex.getZeroBased() >= matches.size()) {
            throw new CommandException(MESSAGE_INVALID_MATCH_INDEX);
        }

        return matches.get(criteriaMatchIndex.getZeroBased());
    }

    private CommandResult executeDecision(Model model, Person personToDelete) {
        if (deletionDecision == DeletionDecision.CONFIRM) {
            model.deletePerson(personToDelete);
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
        }

        if (deletionDecision == DeletionDecision.CANCEL) {
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_CANCELLED, Messages.format(personToDelete)));
        }

        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_CONFIRMATION,
                Messages.format(personToDelete), YES_KEYWORD.toUpperCase(Locale.ROOT),
                NO_KEYWORD.toUpperCase(Locale.ROOT),
                YES_KEYWORD.toUpperCase(Locale.ROOT),
                NO_KEYWORD.toUpperCase(Locale.ROOT)));
    }

    private NameContainsKeywordsPredicate buildNamePredicate(String rawCriteria) {
        List<String> keywords = Arrays.asList(rawCriteria.trim().split("\\s+"));
        return new NameContainsKeywordsPredicate(keywords);
    }

    private String formatMatches(List<Person> matches) {
        return IntStream.range(0, matches.size())
                .mapToObj(i -> (i + 1) + ". " + Messages.format(matches.get(i)))
                .collect(Collectors.joining("\n"));
    }

    /**
     * Returns the target index for index-based delete commands.
     */
    public Index getTargetIndex() {
        return targetIndex;
    }

    /**
     * Returns the raw criteria for criteria-based delete commands.
     */
    public String getCriteria() {
        return criteria;
    }

    /**
     * Returns the clash-selection index for criteria-based delete commands, if present.
     */
    public Index getCriteriaMatchIndex() {
        return criteriaMatchIndex;
    }

    /**
     * Returns the current deletion decision state.
     */
    public DeletionDecision getDeletionDecision() {
        return deletionDecision;
    }

    /**
     * Returns true if this command deletes by criteria instead of list index.
     */
    public boolean isCriteriaDelete() {
        return criteria != null;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return Objects.equals(targetIndex, otherDeleteCommand.targetIndex)
                && Objects.equals(criteria, otherDeleteCommand.criteria)
                && Objects.equals(criteriaMatchIndex, otherDeleteCommand.criteriaMatchIndex)
                && deletionDecision == otherDeleteCommand.deletionDecision;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("criteria", criteria)
                .add("criteriaMatchIndex", criteriaMatchIndex)
                .add("deletionDecision", deletionDecision)
                .toString();
    }
}
