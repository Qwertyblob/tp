package seedu.address.model;

import java.util.ArrayList;
import java.util.List;

/**
 * An AddressBook that keeps track of its own history so that it can undo/redo previous states.
 */
public class VersionedAddressBook extends AddressBook {

    private final List<ReadOnlyAddressBook> addressBookStateList;
    private List<String> commandHistory = new ArrayList<>();
    private int currentStatePointer;

    /**
     * Creates a VersionedAddressBook with the initial state of the provided {@code initialState}.
     */
    public VersionedAddressBook(ReadOnlyAddressBook initialState) {
        super(initialState);
        addressBookStateList = new ArrayList<>();
        addressBookStateList.add(new AddressBook(initialState));
        currentStatePointer = 0;
    }

    /**
     * Saves the current address book state in its history.
     * Removes all states after the current pointer.
     */
    public void commit(String commandDescription) {
        // Remove all states after current pointer (if any)
        removeStatesAfterPointer();
        // Add a copy of the current state
        addressBookStateList.add(new AddressBook(this));
        commandHistory.add(commandDescription == null ? "Unknown change" : commandDescription);
        currentStatePointer++;
    }

    /**
     * Saves the current address book state in its history.
     * Removes all states after the current pointer.
     */
    public void commit() {
        removeStatesAfterPointer();
        addressBookStateList.add(new AddressBook(this));
        commandHistory.add("Unknown change");
        currentStatePointer++;
    }

    private void removeStatesAfterPointer() {
        int statesToRemove = addressBookStateList.size() - (currentStatePointer + 1);
        if (statesToRemove > 0) {
            addressBookStateList.subList(currentStatePointer + 1, addressBookStateList.size()).clear();
            if (currentStatePointer + 1 < commandHistory.size()) {
                commandHistory.subList(currentStatePointer + 1, commandHistory.size()).clear();
            }
            while (commandHistory.size() > addressBookStateList.size() - 1) {
                commandHistory.remove(commandHistory.size() - 1);
            }
        }
    }

    /**
     * Restores the previous address book state from its history.
     */
    public void undo() {
        if (!canUndo()) {
            throw new IllegalStateException("No more states to undo.");
        }
        currentStatePointer--;
        resetData(addressBookStateList.get(currentStatePointer));
    }

    /**
     * Restores a previously undone address book state from its history.
     */
    public void redo() {
        if (!canRedo()) {
            throw new IllegalStateException("No more states to redo.");
        }
        currentStatePointer++;
        resetData(addressBookStateList.get(currentStatePointer));
    }

    /**
     * Returns true if there are states to undo.
     */
    public boolean canUndo() {
        return currentStatePointer > 0;
    }

    /**
     * Returns true if there are states to redo.
     */
    public boolean canRedo() {
        return currentStatePointer < addressBookStateList.size() - 1;
    }

    public int getCurrentStatePointer() {
        return this.currentStatePointer;
    }

    public List<ReadOnlyAddressBook> getAddressBookStateList() {
        return this.addressBookStateList;
    }

    /**
     * Returns the description of the most recently committed command.
     */
    public String getLastCommandDescription() {
        if (currentStatePointer <= 0) {
            return "No previous command";
        }
        // Ensure commandHistory has the entry we need
        // commandHistory.size() should be at least currentStatePointer
        if (currentStatePointer - 1 >= commandHistory.size()) {
            return "No previous command";
        }
        return commandHistory.get(currentStatePointer - 1);
    }

    /**
     * Returns the description of the command being redone.
     */
    public String getRedoCommandDescription() {
        if (currentStatePointer < 0 || currentStatePointer >= commandHistory.size()) {
            return "No command to redo";
        }
        return commandHistory.get(currentStatePointer);
    }

    @Override
    public boolean equals(Object other) {
        // Ignore history; compare current AddressBook state only
        return super.equals(other);
    }

}
