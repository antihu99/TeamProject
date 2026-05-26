# Gherkin Scenarios: Employee Management CLI

## 1. Document Overview
- Document name: `Gherkin Scenarios`
- Related product: `Employee Management CLI`
- Upstream documents:
  - `DOCS/00_PRD.md`
  - `DOCS/01_epic.md`
  - `DOCS/02_requirements_traceability.md`
  - `requirement/Base.md`
  - `requirement/Further.md`
- Source-guided references:
  - `src/main/java/com/sec/bestreviewer/EmployeeManagement.java`
  - `src/main/java/com/sec/bestreviewer/CommandFactory.java`
  - `src/main/java/com/sec/bestreviewer/util/ResultStringFormatter.java`

## 2. Purpose
This document translates the product requirements into executable business scenarios written in Gherkin style.

The scenarios are intended to support:
- acceptance criteria review,
- failing test design in `RED`,
- feature validation in `GREEN` and `NEW_FEATURE`,
- final regression review in `QA`.

## 3. Feature Map
- Feature 1: Command execution flow
- Feature 2: Add employee
- Feature 3: Delete employee
- Feature 4: Search employee
- Feature 5: Output formatting
- Feature 6: Modify employee
- Feature 7: `certi` field support
- Feature 8: Secondary field options
- Feature 9: Comparison search
- Feature 10: Logical condition composition
- Feature 11: Large-volume handling

## 4. Gherkin Scenarios
```gherkin
Feature: Input and output file execution
  As a user
  I want to run the program with an input file and an output file
  So that command processing can be automated

  Scenario: Execute commands from an input file
    Given an input file containing valid employee commands
    When the program is executed with input and output file arguments
    Then the program should read the input file line by line
    And the program should write execution results to the output file

  Scenario: Write invalid command lines back to the output
    Given an input file containing a command line that reaches an invalid-command path
    When the program is executed with input and output file arguments
    Then the output should contain "wrong command : <original line>"


Feature: Add employee records
  As an operator
  I want to add employee records
  So that the employee database can be built and maintained

  Scenario: Add a valid employee record
    Given an empty employee database
    When the command "ADD, , , ,14000301,YUJIN KIM,CL2,010-0977-0000,19981206,ADV" is executed
    Then the employee record should be stored in the database

  Scenario: Add employee records with duplicated non-key values
    Given an employee database with an existing employee name and phone pattern
    When another valid ADD command uses duplicated values except for employee identity context
    Then the record should still be added


Feature: Delete employee records
  As an operator
  I want to delete all records matching a condition
  So that obsolete employee data can be removed

  Scenario: Delete matching records with count output
    Given employee records with career level "CL3"
    When the command "DEL, , , ,cl,CL3" is executed
    Then all matching records should be deleted
    And the output should be "DEL,<count>"

  Scenario: Delete matching records with print output
    Given employee records with name "YUJIN KIM"
    When the command "DEL,-p, , ,name,YUJIN KIM" is executed
    Then matching records should be printed with the "DEL" prefix
    And at most 5 records should be printed

  Scenario: Delete with no matching record
    Given an employee database with no matching birthday "19901225"
    When the command "DEL,-p, , ,birthday,19901225" is executed
    Then the output should be "DEL,NONE"


Feature: Search employee records
  As an operator
  I want to search employee records by condition
  So that I can inspect matched employees

  Scenario: Search matching records with count output
    Given employee records with birthday "19810630"
    When the command "SCH, , , ,birthday,19810630" is executed
    Then the output should be "SCH,<count>"

  Scenario: Search matching records with print output
    Given employee records with birthday "19810630"
    When the command "SCH,-p, , ,birthday,19810630" is executed
    Then matching records should be printed with the "SCH" prefix
    And records should be ordered by earlier join year first
    And at most 5 records should be printed

  Scenario: Search with no matching record
    Given an employee database with no employee named "UNKNOWN USER"
    When the command "SCH, , , ,name,UNKNOWN USER" is executed
    Then the output should be "SCH,NONE"


Feature: Output formatting rules
  As a tester
  I want command output to be deterministic
  So that automated validation remains stable

  Scenario: Print output is limited to five records
    Given more than 5 employee records match the search condition
    When a command with "-p" is executed
    Then only 5 records should be printed

  Scenario: Printed output is sorted by employee number semantics
    Given multiple matching employee records with different join years
    When a command with "-p" is executed
    Then records with earlier join years should appear first

  Scenario: Add command does not print record output
    Given a valid ADD command
    When the command is executed
    Then no DEL/SCH/MOD style output should be produced for ADD


Feature: Modify employee records
  As an operator
  I want to modify matched employee records
  So that employee data stays current

  Scenario: Modify matching records with count output
    Given employee records with career level "CL3"
    When the command "MOD, , , ,cl,CL3,phoneNum,010-0970-0055" is executed
    Then all matching records should be updated
    And the output should be "MOD,<count>"

  Scenario: Modify matching records with print output
    Given an employee record with employee number "91351446"
    When the command "MOD,-p, , ,employeeNum,91351446,phoneNum,010-0970-0055" is executed
    Then the printed output should use the record state before modification

  Scenario: Reject modification of employee number
    Given an existing employee record
    When a MOD command tries to update "employeeNum"
    Then the output should contain "wrong command : <original line>"


Feature: Certification field support
  As an operator
  I want the system to support the certi field
  So that extended employee information can be stored and displayed

  Scenario: Printed command output includes certi in final record shape
    Given employee records containing certi values
    When the command "SCH,-p, , ,birthday,19810408" is executed
    Then printed records should include the certi field
    And the same printed row shape should apply to DEL and MOD outputs


Feature: Secondary field options
  As an operator
  I want to target partial fields using secondary options
  So that I can search, delete, and modify more precisely

  Scenario: Filter by first name
    Given employee records with first name "YUJIN"
    When the command "DEL,-p,-f, ,name,YUJIN" is executed
    Then only records whose first name is "YUJIN" should match

  Scenario: Filter by last name
    Given employee records with last name "KIM"
    When the command "MOD, ,-l, ,name,KIM,phoneNum,010-0970-0055" is executed
    Then only records whose last name is "KIM" should be modified

  Scenario: Filter by phone middle digits
    Given employee records with middle phone digits "0970"
    When the command "DEL,-p,-m, ,phoneNum,0970" is executed
    Then only matching records should be deleted

  Scenario: Filter by birthday year
    Given employee records born in year "1990"
    When the command "MOD,-p,-y, ,birthday,1990,name,YUJIN LEE" is executed
    Then only records with birthday year "1990" should be modified


Feature: Comparison search options
  As an operator
  I want to compare values in search conditions
  So that threshold-based search is possible

  Scenario: Search by greater-than-or-equal first name
    Given employee records with varying first names
    When the command "SCH,-p,-f,-ge,name,YUJIN" is executed
    Then only records whose first name is lexically greater than or equal to "YUJIN" should match

  Scenario: Search by smaller-than career level
    Given employee records with different career levels
    When the command "SCH,-p, ,-s,cl,CL3" is executed
    Then only records whose career level is smaller than "CL3" should match

  Scenario: Search by greater-than-or-equal certification
    Given employee records with different certi values
    When the command "SCH,-p, ,-ge,certi,PRO" is executed
    Then only records whose certi value is greater than or equal to "PRO" should match


Feature: Logical condition composition
  As an operator
  I want to combine two conditions with AND or OR
  So that I can express richer business queries

  Scenario: Search with AND condition
    Given employee records with different last names and career levels
    When the command "SCH,-p,-l, ,name,KIM,-a, , ,cl,CL4" is executed
    Then only records matching both conditions should be returned

  Scenario: Search with OR condition
    Given employee records with different phone middle digits and birthday years
    When the command "SCH, ,-m, ,phoneNum,0970,-o,-y, ,birthday,1990" is executed
    Then records matching either condition should be returned
    And duplicate records should not appear more than once

  Scenario: Modify with AND condition
    Given employee records with career level "CL2" and birthday month "01"
    When the command "MOD,-p, , ,cl,CL2,-a,-m, ,birthday,01,name,YUJIN LEE" is executed
    Then only records matching both conditions should be modified


Feature: Large-volume handling
  As a maintainer
  I want the system to support large datasets
  So that the requirement scale is satisfied

  Scenario: Operate correctly after 100000 record insertions
    Given a database containing at least 100000 employee records
    When search, delete, and modify commands are executed
    Then the commands should still behave according to the defined contracts
```

## 5. Suggested Usage
- Use the scenarios as acceptance references in `SPEC`.
- Convert selected scenarios into failing tests in `RED`.
- Map each scenario to implementation scope in `GREEN` and `NEW_FEATURE`.
- Reuse the same scenarios for regression confirmation in `QA`.
