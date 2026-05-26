# Epic Definition: Employee Management CLI

## 1. Document Overview
- Document name: `Epic Definition`
- Related product: `Employee Management CLI`
- Upstream documents:
  - `DOCS/00_PRD.md`
  - `requirement/Base.md`
  - `requirement/Further.md`

## 2. Product Goal Summary
The product must provide a file-driven employee database management CLI that supports reliable add, search, delete, and modify operations with deterministic output, scalable processing, and extended option-based filtering.

## 3. Epic List
## Epic 1. Core Command Processing
### Goal
Implement the core command lifecycle so that the system can read commands from an input file, parse them, execute them, and write output to an output file.

### Why It Matters
Without a stable command-processing flow, none of the business commands can be executed consistently or tested reliably.

### Included Capabilities
- input/output file execution flow
- command parsing
- command dispatch
- employee record storage access
- result writing

### Key Stories
- As a user, I want to run the program with input and output file paths so that command execution can be automated.
- As a developer, I want each command to follow a strict format so that parsing remains deterministic.
- As a tester, I want output to be file-based so that I can compare expected and actual results automatically.

### Acceptance Signals
- Program reads the input file and writes the output file.
- Command execution order matches input order.
- Invalid command structures can be isolated and tested.


## Epic 2. Employee Record Management
### Goal
Support the core employee data operations: add, search, and delete.

### Why It Matters
These commands define the base product value and the minimum usable scope required by the base requirement.

### Included Capabilities
- `ADD`
- `DEL`
- `SCH`
- field-based exact matching
- non-null employee data handling

### Key Stories
- As an operator, I want to add a new employee record so that the database can grow over time.
- As an operator, I want to search employees by field so that I can inspect matching records.
- As an operator, I want to delete employees by condition so that obsolete records can be removed.

### Acceptance Signals
- `ADD` stores a new record.
- `DEL` removes all matching records.
- `SCH` returns all matching records.
- Employee fields follow the required format contract.


## Epic 3. Output Rules and Result Formatting
### Goal
Provide deterministic and requirement-compliant output for `DEL`, `SCH`, and `MOD`.

### Why It Matters
The project is heavily output-contract driven, so correctness depends not only on data changes but also on exact formatting behavior.

### Included Capabilities
- `-p` output mode
- count output mode
- `NONE` output behavior
- max 5 record output limit
- join-year-based ordering
- command-prefixed result lines

### Key Stories
- As a tester, I want printed output to follow one exact format so that expected-output approval tests are stable.
- As a user, I want a count result when `-p` is not used so that I can quickly understand impact size.
- As a reviewer, I want no-match cases to print `COMMAND,NONE` so that behavior stays explicit.

### Acceptance Signals
- With `-p`, records are printed in the correct order and at most 5 lines.
- Without `-p`, the command prints only the affected count.
- No-match cases print `COMMAND,NONE`.


## Epic 4. Extended Employee Update and Data Model
### Goal
Extend the base system with record modification support and the new `certi` field.

### Why It Matters
The extended requirement turns the system from simple storage/search into a fuller record management product.

### Included Capabilities
- `MOD`
- `certi` column support
- pre-change output for `MOD`
- one-field-per-command update rule
- immutable `employeeNum`

### Key Stories
- As an operator, I want to modify matching records so that employee data stays current.
- As an operator, I want only one column changed per command so that updates remain explicit and auditable.
- As a user, I want `MOD -p` to show the old record values so that I can verify what changed.

### Acceptance Signals
- `MOD` updates all matching records.
- `employeeNum` cannot be modified.
- Printed `MOD` output reflects the pre-change state.
- Printed final record shape supports `certi`.


## Epic 5. Advanced Querying and Condition Composition
### Goal
Support richer filtering semantics across names, phone numbers, birthdays, comparison searches, and logical condition combinations.

### Why It Matters
This epic expands the product from exact-match CRUD into a more expressive employee query tool.

### Included Capabilities
- name options: `-f`, `-l`
- phone options: `-m`, `-l`
- birthday options: `-y`, `-m`, `-d`
- comparison options: `-g`, `-ge`, `-s`, `-se`
- logical options: `-a`, `-o`

### Key Stories
- As an operator, I want to search or modify by first name, last name, phone segments, or birthday parts so that I can target subsets precisely.
- As an operator, I want comparison search for `SCH` so that I can find records above or below a threshold.
- As an operator, I want `AND` and `OR` combinations so that I can express more realistic conditions.

### Acceptance Signals
- Option positions are interpreted correctly.
- Comparison search works only for `SCH`.
- Duplicate records are not emitted in `OR` search results.
- Partial-field semantics follow requirement examples exactly.


## Epic 6. Scalability, Reliability, and Testability
### Goal
Ensure the product can handle large datasets and remain easy to test and validate.

### Why It Matters
The requirement explicitly expects 100,000-record handling and strongly implies automated verification through deterministic file outputs.

### Included Capabilities
- 100,000 record support
- deterministic behavior
- automated testability
- stable command contracts

### Key Stories
- As a tester, I want deterministic results so that regression testing is trustworthy.
- As a maintainer, I want command behavior to remain contract-driven so that refactoring does not break hidden assumptions.
- As a reviewer, I want clear feature boundaries so that code reviews remain manageable.

### Acceptance Signals
- Search/delete/modify still work after large-scale inserts.
- The same input always produces the same output.
- Functional behavior can be verified through unit and integration tests.

## 4. Suggested Delivery Order
1. Epic 1: Core Command Processing
2. Epic 2: Employee Record Management
3. Epic 3: Output Rules and Result Formatting
4. Epic 4: Extended Employee Update and Data Model
5. Epic 5: Advanced Querying and Condition Composition
6. Epic 6: Scalability, Reliability, and Testability

## 5. Branch Mapping Suggestion
- `SPEC`: Epic definitions, scope clarification, story breakdown
- `RED`: failing tests by epic and story
- `GREEN`: base epics implementation
- `REFACTORING`: internal quality improvement across epics
- `NEW_FEATURE`: extended epics implementation
- `QA`: epic-level acceptance validation
