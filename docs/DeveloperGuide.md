---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# Rollcall Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

This project is based on the AddressBook-Level3 project created by the SE-EDU initiative.

Libraries used: JavaFX, Jackson, JUnit5

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2526S1-CS2103T-F14a-4/tp/blob/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2526S1-CS2103T-F14a-4/tp/blob/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
2. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is returned to the `LogicManager`.
3. If the command requires confirmation, the `ConfirmationManager` handles the user's Y/N response to either continue to execution or cancel the command.
3. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
4. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="800" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="800" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Add Feature

The `AddCommand` allows users to create and add a new Person entry into the address book.
Each person must have a **name**, **role**, **phone number**, **email**, and **address**. Optional **tags** may be included. During execution, the command validates input fields, automatically generates an `IdentificationNumber` based on the user’s role, and prevents duplicates entries.

Below is an example execution flow:

Step 1. The user enters a command such as:
```
add n/John Doe r/student p/98765432 e/johnd@example.com a/311, Clementi Ave 2, #02-25 t/friends t/owesMoney
```
Step 2. `AddCommandParser` parses the command:

* Tokenizes input using the `PREFIX_*` constants (e.g. `n/`, `r/`, `p/`).
* Ensures all required prefixes are present and no duplicates exist.
* Parses each field (`Name`, `Role`, `Phone`, etc.).
* Assigns a unique ID (e.g. S0000001 for students). 
* Creates a new `Person` object.

Step 3. The `AddCommand` object is created with the parsed `Person`.

Step 4. The `Person` is then added to the address book through the `Model`.

The following sequence diagram shows how the add operation goes through the Logic component:

<puml src="diagrams/AddSequenceDiagram-Logic.puml" alt="AddSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `AddCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an add operation goes through the `Model` component is shown below:

<puml src="diagrams/AddSequenceDiagram-Model.puml" alt="AddSequenceDiagram-Model" />

### Undo/redo feature

The undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* has a need to manage a significant number of contacts
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps
* tuition centre HR admin

**Value proposition**:
* General HR management of the students and staff in the centre
* Streamline enrollment of student into classes
* Students and tutors can be filtered by subject/class
* More intuitive and easier to use than software like excel
* Lightweight, offline address book which provide the essential HR features without the overhead of fullblown HR software


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                         | I want to …​                                                                            | So that I can…​                                                                                                                            |
|----------|---------------------------------|-----------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| `* * *`  | hr admin                        | check the classes taught by various staff                                               | better allocate different classes to different tutors                                                                                      |
| `* * *`  | new user                        | access an overview of the important features quickly                                    | first learn the essential commands quickly instead of being overwhelmed by many features                                                   |
| `* * *`  | user with many contacts         | create tags and add tags to contacts                                                    | track important additional info                                                                                                            |
| `* * *`  | user with many contacts         | filter the contact list view according to tags                                          | get an overview of the relevant contacts for any situation (e.g. students with unpaid fees, struggling students, students on thursday etc) |
| `* * *`  | user with many contacts         | find a specific contact with powerful search feature                                    | easily find one specific person if i need their contact                                                                                    |
| `* * *`  | hr admin                        | show class details for the day of the week                                              | quickly check the schedule                                                                                                                 |
| `* * *`  | hr admin                        | view attendance for each class                                                          | quickly check students attendance                                                                                                          |
| `* *`    | new user                        | import data from an existing csv / json file                                            | seamlessly transfer data from a previously used software                                                                                   |
| `* *`    | careless user                   | undo the most recent command                                                            | prevent making a permanent mistake                                                                                                         |
| `* *`    | user with many contacts         | Choose different list views for contacts (eg name only/ name and class/ all details...) | minimise visual clutter and see only what is required                                                                                      |
| `* *`    | user with many contacts         | apply tags or update state of many contacts at once                                     | prevent wasting time with repetitive similar commands                                                                                      |
| `* *`    | hr admin                        | quickly export the data to my boss                                                      | prevent wasting time extracting information one by one                                                                                     |
| `* *`    | owner                           | encrypt the data with a password                                                        | protect my staff and students' privacy                                                                                                     |
| `* *`    | more advanced user              | press TAB to autocomplete commands based on data of my most frequent commands           | enter commands faster                                                                                                                      |
| `* *`    | more advanced user              | quickly select my most recent command                                                   | prevent wasting time typing it out again                                                                                                   |
| `* *`    | user inheriting a used database | purge the current database                                                              | start from scratch                                                                                                                         |
| `* *`    | user with many contacts         | sort the contacts based on my preference                                                | view the contact list in a more orderly manner                                                                                             |
| `* *`    | forgetful user                  | view when a contacts details are last updated                                           | see how up to the date the current details are                                                                                             |
| `* *`    | sad user                        | view a greeting message everytime i start the program                                   | feel more happy to do work                                                                                                                 |
| `* *`    | sad user                        | view an exit message which includes the changes made everytime i log out of the program | feel satisfied about by work everyday                                                                                                      |
| `* *`    | more advanced user              | backup the database frequently at a scheduled time                                      | preserve old data off the app                                                                                                              |
| `* *`    | more advanced user              | chose what details to be show in the overview                                           | be shown only the important details                                                                                                        |
| `*`      | less tech savvy user            | understand the simple and intuitive UI                                                  | avoid giving up on using the app                                                                                                           |
| `*`      | new user                        | have a quick reference to possible suggested commands that appears as i type            | avoid having to remember everything at first                                                                                               |
| `*`      | more advanced user              | make my own aliases for commands                                                        | tailor the app towards my personal preferences and further improve efficiency                                                              |
| `*`      | user with limited vision        | i can change the font size/ UI scale                                                    | see better                                                                                                                                 |
| `*`      | user with limited vision        | I can change the theme for more contrast                                                | see better                                                                                                                                 |
| `*`      | hr admin                        | archive inactive students and staff                                                     | my active contacts stay uncluttered while the old data is remained                                                                         |
| `*`      | owner                           | generate statistics with a breakdown of salaries and payments                           | view and plan my finances at a glance                                                                                                      |
| `*`      | forgetful user                  | view my recent commands                                                                 | remember what I typed or changes i made previously                                                                                         |
| `*`      | more advanced user              | schedule frequently used commands                                                       | save on repetitive work                                                                                                                    |


*{More to be added}*

### Use cases

(For all use cases below, the **System** is `Rollcall` and the **Actor** is the `user`, unless specified otherwise)

**Use case: UC01 - Add an item (a student, tutor or class)**

**MSS**

1.  User requests to add the desired item with its necessary information.
2.  Rollcall shows a success message and the new item.

    Use case ends.

**Extensions**

* 1a. The user does not provide the required information in the proper format.
    * 1a1. Rollcall shows an error message.

      Use case resumes at step 1.

* 1b. The given information matches an existing item exactly.
    * 1b1. Rollcall shows an error message.

      Use case resumes at step 1.

**Use case: UC02 - Edit an item (a student, tutor or class)**

**MSS**

1.  User requests to edit the desired item, providing its new information.
2.  Rollcall shows a success message and the edited item.

    Use case ends.

**Extensions**

* 1a. The user does not provide the required information in the proper format.
  * 1a1. Rollcall shows an error message.

    Use case resumes at step 1.

* 1b. The given information matches an existing item exactly.
  * 1b1. Rollcall shows an error message.

    Use case resumes at step 1.

**Use case: UC03 - Mark a student's attendance**

**MSS**

1.  User requests to mark a student's attendance.
2.  Rollcall shows a success message.

    Use case ends.

**Extensions**

* 1a. The user does not provide the required information in the proper format.
    * 1a1. Rollcall shows an error message.

      Use case resumes at step 1.

**Use case: UC04 - List persons or classes**

**MSS**

1. User requests to list students, tutors, or classes.
2. Rollcall shows a list of all students, tutors or classes respectively.

    Use case ends.

**Extensions**

* 1a. The list is empty.
  * 1a1. Rollcall shows a message indicating the list is empty.

    Use case ends.

**Use case: UC05 - Search persons or classes**

**MSS**

1. User requests to list items with specific parameters.
2. Rollcall shows a list of all items matching the requested parameters.

    Use case ends.

**Extensions**

* 1a. The list is empty.
    * 1a1. Rollcall shows a message indicating the list is empty (i.e. no matching items).

      Use case ends.

**Use case: UC06 - Delete an item**

**MSS**

1. User requests a <u>certain list of persons or classes (UC05)</u>.
2. User requests to delete a specific item in the list.
3. Rollcall requests confirmation.
4. User confirms the deletion.
5. Rollcall deletes the item.

    Use case ends.

**Extensions**

* 1a. The list is empty.

  Use case ends.


* 2a. The user gives an item that is not in the list.

  * 2a1. Rollcall shows an error message.

    Use case resumes at step 2.


* 2b. User requested a forced deletion.

  Use case resumes at step 5.


* 2c. There are multiple possible matches for the requested item.
  * 2c1. Rollcall requests clarification.
  * 2c2. User clarifies the item to be deleted.

    Use case resumes at step 3.

**Use case: UC07 - Enroll a new student into a class**

**MSS**

1. User <u>adds a new student (UC01)</u>.
2. User requests to enroll the student into a class.
3. Rollcall shows a success message and a list of current students in the class.

   Use case ends.

**Extensions**

* 2a. User provided incorrect format.
  * 2a1. Rollcall shows an error message.

    Use case resumes at step 2.

* 2b. Enrollment is not possible (clashes with existing class/ class is fully enrolled/ student is already enrolled).
  * 2b1. Rollcall shows an error message.

    Use case resumes at step 2.

### Non-Functional Requirements

##### Environment

* The application should work on any _mainstream OS_ (Windows, macOS, Linux) as long as it has Java `17` or above installed.
* The application should run as a _single_ executable JAR file without any installation.

##### Performance

* The system should be able to hold up to 1000 persons (students and tutors) **without exceeding 1 second** for any basic command (e.g. `add`, `delete`, `find`).
* The application should have a startup time of under 5 seconds.

##### Usability

* The primary input method should be **text-based commands**.
* A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
* The system should not crash when invalid commands are entered.
* The system should be **fully usable offline**.
* The GUI should be usable on screen resolutions of 1280x720 and higher, and for screen scales 150%.
* The GUI should be used primarily for visual feedback and not as the main input method.

##### Maintainability

* The codebase should follow OOP design principles.
* The system should be developed incrementally with commits that reflect feature milestones.

##### Scalability

* The system should support small to medium tuition centres (around 100-200 students, 10-20 tutors).
* The system is not intended for large institutions with thousands of records.

##### Data Management

* All data must be stored locally in a **human-editable text file**.
* The system must not use a DBMS (database management system).

##### File Size

* The final JAR file should not exceed **100MB**.
* The UG and DG documents should not exceed **15MB each** and remain PDF-friendly.

### Glossary

**Role/Actors**
* **HR Admin**: The staff member responsible for managing tutors, students, classes, and related administrative tasks.
* **Owner**: The person who owns or manages the system, usually responsible for finances and overall data security.
* **New User**: A first-time user of the system, who may need onboarding and quick access to essential features.
* **Advanced User**: A user familiar with the system who leverages customization, automation, and efficiency features.
* **User with Limited Vision**: A user who requires accessibility features such as font scaling and high-contrast themes.

**Core System Concepts**
* **Contact**: A person or entity stored in the system, typically including details such as name, class, role, and tags.
* **Class**: A scheduled session or group taught by a tutor that students are assigned to.
* **Attendance**: A record of whether students attended a given class.
* **Database**: The underlying data store containing all contacts, classes, attendance, and related information.
* **Tag**: A customizable label attached to contacts (e.g., “unpaid fees,” “struggling,” “Thursday class”) for filtering and organization.
* **List View**: A way of displaying contacts, with different levels of detail (e.g., name only, name + class, full details).
* **Command**: A typed instruction given by the user to interact with the system.
* **Alias**: A user-defined shortcut for a command.
* **Autocomplete**: A feature that suggests or completes commands as the user types.
* **Recent Commands**: A history of the most recent instructions executed in the system.

**System Features / Functions**
* **Import**: Bringing in data from external sources (CSV, JSON).
* **Export**: Sending system data to an external format (e.g., for reports).
* **Archive**: Moving inactive data (students, staff, or contacts) out of active use without deleting it.
* **Purge**: Completely removing all data from the system.
* **Backup**: Creating a copy of the database for recovery or preservation.
* **Statistics**: Summarized financial or operational reports generated by the system.
* **Overview**: A summarized view of important system information (e.g., contacts, classes, schedule).
* **Undo / Redo**: Reverting or reapplying the most recent action.
* **Schedule (Commands)**: Setting commands to run automatically at specified times.

**User Experience & Accessibility**
* **UI Scale**: Adjustment of font size and layout for accessibility.
* **Theme**: A color and contrast scheme for the interface.
* **Quick Reference / Suggestions**: Inline guidance showing possible commands as the user types.

**Misc**
* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private contact detail**: A contact detail that is not meant to be shared with others.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder.

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. Exiting

   1. Type exit into the command bar and hit enter.<br>
      Expected: Program should exit

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: Confirmation shows up and if user is to type in 'Y', first contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. Deleting a person while using find
    1. Prerequisites: Using `find n/yeoh` to list 1 person.

    1. Test case: `delete 1`<br>
       Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

### Deleting a class

1. Delete a class while all classes are shown

   1. Prerequisites: List all classes using the `listc` command. Multiple classes in the list.

   1. Test case: `delete 1`<br>
       Expected: Confirmation shows up and if user is to type in 'Y', first class is deleted from the list. 
           Details of the deleted class shown in the status message. Timestamp in the status bar is updated.

### Undoing a Command

1. Undoing a delete

    1. Prerequisites: List all persons using the `list` command. Multiple persons in the list. Type `delete 1` and then type `Y` to confirm.

    1. Test case: `undo`<br>
         Expected: Undo is successful and person is no longer deleted and should be on the list.
   
    1. Other commmands to try undoing: `editc`, `edit`, `deletec` - Refer to Usage upon typing for command format

### Editing a Person

1. Editing a person while all persons are shown

    1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

    1. Test case: `edit 1 p/91234567 e/johndoe@example.com`<br>
       Expected: Edit is successful and person 1 should have a new phone number and email.

    1. Test case: `edit 0 n/bob bobson`
        Expected: Edit is unsuccessful and error message is shown

    1. Other incorrect delete commands to try: `edit`, `edit x n/test`, `edit 1 /test`, `edit 1 p/1`  (where x is larger than the list size)<br>
       Expected: Similar to previous.