# Employee Management CLI Documentation Hub

## 1. Overview
This folder contains the planning and delivery documents for the `Employee Management CLI` project.

The document set is organized to support the current team strategy:
- requirement-driven development,
- TDD-first execution,
- branch-based parallel collaboration,
- output-contract validation,
- final QA and presentation readiness.

This README is the entry point for understanding how the project documents connect and how they should be used during each branch stage.

## 2. Project Summary
The product is a text-file based employee database management CLI.

It must:
- read commands from an input file,
- process employee database operations,
- write deterministic results to an output file,
- support up to 100,000 records,
- satisfy both base and extended requirements.

Core commands:
- `ADD`
- `DEL`
- `SCH`
- `MOD`

Core option groups:
- output option: `-p`
- secondary field options: `-f`, `-l`, `-m`, `-y`, `-d`
- comparison options: `-g`, `-ge`, `-s`, `-se`
- logical composition: `-a`, `-o`

Extended data model:
- `certi`

## 3. Current Source Structure
The current Java source tree under `src/main/java/com/sec/bestreviewer` is organized as follows.

- top-level runtime flow:
  - `EmployeeManagement`
  - `CommandReader`
  - `CommandParser`
  - `CommandFactory`
  - `CommandExecutor`
  - `Printer`
  - `TokenGroup`
- command package:
  - `command/Command`
  - `command/AddCommand`
  - `command/DeleteCommand`
  - `command/SearchCommand`
  - `command/ModCommand`
  - `command/CountCommand`
  - `command/CombinationEnum`
- store package:
  - `store/EmployeeStore`
  - `store/EmployeeStoreImpl`
  - `store/Employee`
  - `store/FieldEnum`
  - `store/Injection`
- field package:
  - `field/Field`
  - `field/EmployeeNumber`
  - `field/Name`
  - `field/CareerLevel`
  - `field/PhoneNumber`
  - `field/Birthday`
  - `field/Certi`
- util package:
  - `util/OptionParser`
  - `util/PrimaryOptionEnum`
  - `util/SecondaryOptionEnum`
  - `util/TertiaryOptionEnum`
  - `util/ResultStringFormatter`
  - `util/Pair`
  - `util/CachedSupplier`

Current source note:
- `CountCommand` / `CNT` exists in source and tests as an auxiliary command path, but it is not part of the original requirement documents.

## 4. Current Test Status
The current test tree under `src/test/java/com/sec/bestreviewer` already contains broad unit and integration coverage.

Strengths:
- field parsing and comparison tests exist for core value objects
- store-level tests exist for name, birthday, career level, and modify behavior
- command-level tests already cover major `DEL` / `SCH` output cases

Open gaps:
- malformed-command output assertions in `EmployeeManagement`
- phone partial-field integration tests
- direct formatter regression tests
- active end-to-end approval coverage for currently disabled scenarios
- 100,000-record validation

## 5. Required Environment Note
Before running the project, apply the prerequisite noted in the root `README.md`.

- Copy `settings.xml` to the local Maven settings path.
- Remove the local Maven repository cache if required by the environment.
- Then launch IntelliJ or run the project build again.

This setup note remains important because the project may depend on an internal Maven repository configuration.

## 6. Delivery Strategy Summary
The team follows this stage flow:

1. `SPEC`
2. `RED`
3. `GREEN`
4. `REFACTORING`
5. `NEW_FEATURE`
6. `QA`

Each stage is created from `main` as a separate integration branch.

Each integration branch is then split into 4 personal work branches for parallel execution:
- `A_01_*`
- `A_02_*`
- `A_03_*`
- `A_04_*`

This structure supports:
- small reviewable changes,
- parallel team execution,
- TDD-oriented handoff between stages,
- cleaner QA and merge control.

## 7. Document Map
### Core Documents
- [`00_PRD.md`](./00_PRD.md)
  - Product definition, scope, goals, data model, command contracts, output rules, and acceptance criteria.

- [`01_epic.md`](./01_epic.md)
  - Product capability groups organized into epics with goals, stories, and acceptance signals.

- [`02_requirements_traceability.md`](./02_requirements_traceability.md)
  - Requirement-to-PRD traceability matrix with implementation and test focus.

- [`03_gherkin.md`](./03_gherkin.md)
  - Gherkin scenarios that turn requirements into acceptance and test-ready behavior descriptions.

- [`04_todo.md`](./04_todo.md)
  - Execution-ready team todo plan aligned to README schedule, evaluation criteria, and branch flow.

- [`06_spec_gap_log.md`](./06_spec_gap_log.md)
  - SPEC-stage ambiguity and contract gap log that must be resolved before RED writes failing tests.

- [`07_red_test_inventory.md`](./07_red_test_inventory.md)
  - Priority-based RED test backlog with branch ownership and suggested target test files.

## 8. Recommended Reading Order
For a new team member:

1. Read [`00_PRD.md`](./00_PRD.md) to understand the final product definition.
2. Read [`01_epic.md`](./01_epic.md) to understand feature grouping and delivery order.
3. Read [`02_requirements_traceability.md`](./02_requirements_traceability.md) to see how source requirements map into implementation.
4. Read [`03_gherkin.md`](./03_gherkin.md) to understand executable business scenarios.
5. Read [`04_todo.md`](./04_todo.md) to understand actual team execution order and branch responsibilities.
6. Read [`06_spec_gap_log.md`](./06_spec_gap_log.md) to confirm unresolved contracts before RED.
7. Read [`07_red_test_inventory.md`](./07_red_test_inventory.md) to assign RED ownership and test order.

## 9. How to Use These Documents by Stage
### SPEC
Use:
- `00_PRD.md`
- `01_epic.md`
- `02_requirements_traceability.md`
- `06_spec_gap_log.md`
- `07_red_test_inventory.md`

Purpose:
- lock requirement interpretation,
- define scope,
- split work safely,
- remove ambiguity before tests and code changes,
- prepare RED test ownership and priority.

### RED
Use:
- `02_requirements_traceability.md`
- `03_gherkin.md`
- `04_todo.md`
- `06_spec_gap_log.md`
- `07_red_test_inventory.md`

Purpose:
- derive failing tests from requirements,
- define coverage targets,
- split testing work across branches,
- avoid encoding unresolved SPEC ambiguities into tests.

### GREEN
Use:
- `00_PRD.md`
- `03_gherkin.md`
- `04_todo.md`

Purpose:
- implement the minimum behavior needed to satisfy base requirement tests,
- match exact output contracts,
- stabilize core command behavior.

### REFACTORING
Use:
- `00_PRD.md`
- `01_epic.md`
- `04_todo.md`

Purpose:
- preserve external contracts,
- improve readability and maintainability,
- keep tests green while improving structure.

### NEW_FEATURE
Use:
- `00_PRD.md`
- `02_requirements_traceability.md`
- `03_gherkin.md`
- `04_todo.md`

Purpose:
- implement `MOD`, `certi`, advanced options, comparison search, and logical composition,
- keep base features stable,
- move tests with features.

### QA
Use:
- all documents in this folder

Purpose:
- verify requirement coverage,
- confirm regression safety,
- validate quality criteria,
- prepare final report and presentation materials.

## 10. Quality and Evaluation Focus
This document set reflects the evaluation priorities from the root `README.md`.

The team should continuously verify:
- all given requirements are satisfied,
- production code readability and maintainability are improving,
- test code quality is appropriate,
- coverage is at least 90%,
- commits stay small and reviewable,
- reviews discuss Clean Code, Refactoring, TDD, and Secure Coding perspectives.

## 11. Expected Outputs from the Documentation Flow
By following these documents, the team should be able to produce:
- a shared product definition,
- a staged branch execution plan,
- testable acceptance scenarios,
- requirement traceability,
- review-ready implementation tasks,
- QA-ready completion criteria.

## 12. Final Guidance
Use this folder as the single documentation hub for planning, implementation alignment, and review preparation.

If a requirement, test, implementation choice, or review comment is unclear, trace it in this order:

1. source requirement file
2. `00_PRD.md`
3. `02_requirements_traceability.md`
4. `03_gherkin.md`
5. `04_todo.md`

This keeps the team aligned to one strategy throughout `SPEC -> RED -> GREEN -> REFACTORING -> NEW_FEATURE -> QA`.
