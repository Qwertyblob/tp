---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# AB-3 User Guide

AddressBook Level 3 (AB3) is a **desktop app for managing contacts, optimized for use via a  Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, AB3 can get your contact management tasks done faster than traditional GUI apps.

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/se-edu/addressbook-level3/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your AddressBook.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar addressbook.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01` : Adds a contact named `John Doe` to the Address Book.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</box>

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a person: `add`

Adds a person to the address book.

Format: `add n/NAME r/ROLE p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​`

* `ROLE` is only limited to `Student` or `Tutor` (case-insensitive)

<box type="tip" seamless>

**Tip:** A person can have any number of tags (including 0)
</box>

Examples:
* `add n/John Doe r/Tutor p/98765432 e/johnd@example.com a/John street, block 123, #01-01`
* `add n/Betsy Crowe r/Student t/friend e/betsycrowe@example.com a/Newgate Prison p/1234567 t/criminal`

### Adding a class: `addc`

Adds a class to the address book.

Format: `addc c/CLASS_NAME d/DAY tm/TIME tt/TUTOR [t/TAG]…​`

* `DAY` can only be the days of the week (e.g. Monday, Tuesday, etc.)
* `DAY` is case-insensitive.
* `TUTOR` must exist in the address book.

<box type="tip" seamless>

**Tip:** A class can have any number of tags (including 0)
</box>

Examples:
* `addc c/M2a d/Monday tm/1200 tt/T1234567` 
* `addc c/S3b d/Monday tm/1200 tt/T1234567 t/temporary class`

### Listing all persons: `list`

Shows a list of all persons in the address book.

Format: `list`

### Listing all classes: `listc`

Shows a list of all classes in the address book.

Format: `listc`

### Editing a person: `edit`

Edits an existing person in the address book.

Format: `edit INDEX [n/NAME] [r/ROLE] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags of the person will be removed i.e adding of tags is not cumulative.
* You can remove all the person’s tags by typing `t/` without
    specifying any tags after it.

Examples:
*  `edit 1 p/91234567 e/johndoe@example.com` Edits the phone number and email address of the 1st person to be `91234567` and `johndoe@example.com` respectively.
*  `edit 2 n/Betsy Crower t/` Edits the name of the 2nd person to be `Betsy Crower` and clears all existing tags.

### Editing a class: `editc`

Edits an existing person in the address book.

Format: `editc INDEX [c/CLASS_NAME] [d/DAY] [tm/TIME] [tt/TUTOR_ID] [t/TAG]…​`

* Edits the class at the specified `INDEX`. The index refers to the index number shown in the displayed class list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags of the class will be removed i.e adding of tags is not cumulative.
* You can remove all the class’ tags by typing `t/` without
  specifying any tags after it.

Examples:
*  `edit 1 d/Tuesday tm/1600` Edits the day and time of the 1st class to be `Tuesday` and `1600` respectively.
*  `edit 2 c/S3b t/` Edits the name of the 2nd class to be `S3b` and clears all existing tags.

### Enrolling a person to a class: `enrol`

Enrols an existing person in the address book to an existing class.

Format: `enrol id/STUDENT_ID class/CLASS_NAME`

* The `STUDENT_ID` and `CLASS_NAME` must exist in the address book.

Examples:
*  `enrol id/S0000001 c/M2a` Enrols the student with the student ID `S0000001` into the class `M2a`.

### Marking a person's attendance in a class: `mark`

Marks an existing person's attendance in the address book in an existing class on the current day.

Format: `mark id/STUDENT_ID class/CLASS_NAME`

* The `STUDENT_ID` and `CLASS_NAME` must exist in the address book.
* The `STUDENT_ID` must be currently enrolled in `CLASS_NAME`.
* The student will be marked present only for the current day.

Examples:
*  `mark id/S0000001 c/M2a` Marks the student with the student ID `S0000001` present in the class `M2a` on the current day.

### Unmarking a person's attendance in a class: `unmark`

Unmarks an existing person's attendance in the address book in an existing class on a particular day.

Format: `unmark id/STUDENT_ID class/CLASS_NAME [dt/DATE]`

* The `STUDENT_ID` and `CLASS_NAME` must exist in the address book.
* The `STUDENT_ID` must be currently marked present in `CLASS_NAME`.
* `DATE` format must be `yyyy-MM-dd`.
* If `DATE` is not specified, the current date will be unmarked if applicable, otherwise, the student will be unmarked only for the specified day.

Examples:
*  `unmark id/S0000001 c/M2a dt/2025-11-11` Unmarks the student with the student ID `S0000001`'s attendance in the class `M2a` on `2025-11-11`.

### Locating persons by name: `find`

Finds persons whose names contain any of the given keywords.

Format: `find [id/ID] [n/NAME] [r/ROLE] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`

* At least one of the optional fields must be provided.
* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only full words will be matched e.g. `Han` will not match `Hans`
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

### Deleting a person : `delete`

Deletes the specified person from the address book.

Format: `delete INDEX [n/NAME]`

* Deletes the person at the specified `INDEX` or `NAME`.
* The index refers to the index number shown in the displayed person list.
* The `NAME` must be the full name of the person.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd person in the address book.
* `find Betsy` followed by `delete 1` deletes the 1st person in the results of the `find` command.

### Clearing all entries : `clear`

Clears all entries from the address book.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

AddressBook data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

AddressBook data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<box type="warning" seamless>

**Caution:**
If your changes to the data file makes its format invalid, AddressBook will discard all data and start with an empty data file at the next run.  Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the AddressBook to behave in unexpected ways (e.g., if a value entered is outside the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</box>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous AddressBook home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.
3. **When using delete**, if you try to delete an invalid index or name, any following command will trigger an infinite cycle of prompting for confirmation and effectively crash the app. This issue will be fixed in the next release, so the current remedy is to avoid deleting an invalid index.
4. **When using commands with index (edit)**, the index accesses the list of contacts the command is meant to interact with regardless of the current display view (i.e. when executing edit on the class list view, the index accesses the person list). This makes it possible to edit persons on the class list view and vice versa which could be unintended behaviour. This issue will be fixed in the next release, so the current remedy is to only execute such commands on the correct view.
5. **When deleting using name**, if there are duplicate names, the first entry will always be deleted. This issue will be fixed in the next release, so the current remedy is to delete by index in such a case.

--------------------------------------------------------------------------------------------------------------------

## Command summary

| Action                | Format, Examples                                                                                                                                                                      |
|-----------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Add person**        | `add n/NAME r/ ROLE p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​` <br> e.g., `add n/James Ho r/tutor p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 t/friend t/colleague` |
| **Add class**         | `addc c/CLASS_NAME d/DAY tm/TIME tt/TUTOR_ID [t/TAG]…​` <br> e.g., `addc c/M2a d/Monday tm/1200 tt/T1234567`                                                                          |
| **Clear**             | `clear`                                                                                                                                                                               |
| **Delete**            | `delete INDEX [n/NAME]`<br> e.g., `delete 3`, `delete n/John`                                                                                                                         |
| **Edit**              | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…​` <br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`                                                          |
| **Edit class**        | `editc INDEX [c/CLASS_NAME] [d/DAY] [tm/TIME] [tt/TUTOR_ID] [t/TAG]…​` <br> e.g., `edit 3 d/Tuesday tt/T7654321`                                                                      |
| **Enrol**             | `enrol id/STUDENT_ID c/CLASS_NAME` <br> e.g., `enrol id/S0000001 c/M2a`                                                                                                               |
| **Mark attendance**   | `mark id/STUDENT_ID c/CLASS_NAME` <br> e.g., `mark id/S0000001 c/M2a`                                                                                                                 |
| **Unmark attendance** | `unmark id/STUDENT_ID c/CLASS_NAME dt/DATE` <br> e.g., `unmark id/S0000001 c/M2a dt/2025-11-11`                                                                                       |
| **Find**              | `find [id/ID] [n/NAME] [r/ROLE] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`<br> e.g., `find n/James Jake r/student`                                                             |
| **List**              | `list`                                                                                                                                                                                |
| **List classes**      | `listc`                                                                                                                                                                               |
| **Help**              | `help`                                                                                                                                                                                |
