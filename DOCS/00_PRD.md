# PRD: Employee Management CLI

## 1. Document Overview
- Document name: `PRD`
- Product name: `Employee Management CLI`
- Source documents:
  - `requirement/Base.md`
  - `requirement/Further.md`
- Source-guided implementation references:
  - `src/main/java/com/sec/bestreviewer/EmployeeManagement.java`
  - `src/main/java/com/sec/bestreviewer/CommandParser.java`
  - `src/main/java/com/sec/bestreviewer/CommandFactory.java`
  - `src/main/java/com/sec/bestreviewer/util/ResultStringFormatter.java`
- Product type: Text-file based employee database management program

## 2. Product Summary
`Employee Management CLI` is a command-line based employee database management program that reads commands from an input text file and writes execution results to an output text file.

The product must support employee registration, search, deletion, and modification. It must also support detailed filtering options, comparison search, logical condition combinations, and output formatting rules defined in the requirement documents.

## 3. Problem Statement
Users need a lightweight employee data management tool that can process a large number of employee records through a fixed command format without relying on an interactive UI.

The product must:
- manage employee records consistently,
- process bulk command inputs,
- return deterministic output,
- support up to 100,000 records,
- preserve clear command contracts for automated verification.

## 4. Product Goals
1. Provide reliable employee record management through file-based commands.
2. Support exact and option-based search/delete/modify operations.
3. Guarantee deterministic output formatting for automated testing and review.
4. Handle at least 100,000 employee records.
5. Maintain extensibility for new columns and new command options.

## 5. Users
- Primary users:
  - Developers implementing the command processor
  - Test engineers validating command behavior
  - Reviewers checking output correctness against requirement examples

## 6. Scope
### In Scope
- Employee data registration with `ADD`
- Employee data deletion with `DEL`
- Employee data search with `SCH`
- Employee data modification with `MOD`
- Support for output option `-p`
- Support for detailed options on `name`, `phoneNum`, `birthday`
- Support for comparison options on `SCH`
- Support for logical combination options `-a`, `-o`
- Support for `certi` column
- Input file to output file execution flow

### Out of Scope
- Interactive UI
- Authentication/authorization
- External API integration
- Real database server requirements beyond allowed storage implementation
- Phone number prefix comparison against `010`

## 7. Product Flow
1. User executes the program with input and output file paths.
2. The program reads commands line by line from the input file.
3. Each command is parsed by command type and options.
4. The program applies the command to the in-memory or storage-backed employee database.
5. The program writes formatted results to the output file.

Current implementation flow in `src/main/java`:

1. `EmployeeManagement`
2. `CommandReader`
3. `CommandParser`
4. `CommandFactory`
5. `CommandExecutor`
6. `EmployeeStoreImpl`
7. `ResultStringFormatter`

Execution format:

```text
EmployeeManagement [input file] [output file]
```

Example:

```text
EmployeeManagement input.txt output.txt
```

## 8. Data Model
The final employee record model must support the following fields.

| Field | Type/Format | Description |
| --- | --- | --- |
| `employeeNum` | 8-digit number | First 2 digits represent join year, from `90XXXXXX` to `19XXXXXX` |
| `name` | uppercase English, max 15 chars | First name and last name separated by a space |
| `cl` | `CL1`, `CL2`, `CL3`, `CL4` | Career development level |
| `phoneNum` | `010-xxxx-xxxx` | Phone number with fixed prefix `010` |
| `birthday` | `YYYYMMDD` | Birth date |
| `certi` | `ADV`, `PRO`, `EX` | Certification level added in extended requirements |

Current source note:
- The current `Employee` constructor and `ADD` command path in `CommandFactory` already expect `certi` as part of the employee input payload.

## 9. Core Functional Requirements
### FR-01. Add Employee
- Command: `ADD`
- Format:

```text
ADD,옵션1,옵션2,옵션3,사원번호,성명,경력개발단계,전화번호,생년월일,certi
```

- The current source implementation already requires `certi` in the `ADD` path.
- A new employee record is added to the database.
- `employeeNum` must identify the employee record format correctly.
- Other fields may be duplicated, but all fields are non-null.
- `ADD` does not produce normal search/delete style output.

### FR-02. Delete Employee
- Command: `DEL`
- Format:

```text
DEL,옵션1,옵션2,옵션3,조건 Column명,조건 값
```

- All records matching the condition must be deleted.
- With `-p`, matching records are printed.
- Without `-p`, only the deleted record count is printed.

### FR-03. Search Employee
- Command: `SCH`
- Format:

```text
SCH,옵션1,옵션2,옵션3,조건 Column명,조건 값
```

- All matching records must be returned.
- With `-p`, matching records are printed.
- Without `-p`, only the matched record count is printed.

### FR-04. Modify Employee
- Command: `MOD`
- Format:

```text
MOD,옵션1,옵션2,옵션3,조건 Column명,조건 값,변경할 Column명,변경할 값
```

- All records matching the condition must be modified.
- Only one column may be changed per command.
- `employeeNum` cannot be modified.
- With `-p`, records must be printed in their pre-change state.
- Without `-p`, only the modified record count is printed.

## 10. Option Requirements
### OR-01. Output Option
- Option: `-p`
- Position: only `옵션1`
- Supported commands: `DEL`, `SCH`, `MOD`
- Behavior:
  - prints matching records line by line,
  - sorts by earlier join year first based on `employeeNum`,
  - outputs at most 5 records,
  - outputs `NONE` when no record matches.

### OR-02. Name Filter Option
- Position: `옵션2`
- Supported options:
  - `-f`: first name
  - `-l`: last name

### OR-03. Phone Number Filter Option
- Position: `옵션2`
- Supported options:
  - `-m`: middle digits
  - `-l`: last digits

### OR-04. Birthday Filter Option
- Position: `옵션2`
- Supported options:
  - `-y`: year
  - `-m`: month
  - `-d`: day

### OR-05. Comparison Search Option
- Position: `옵션3`
- Supported command: `SCH` only
- Supported options:
  - `-g`: greater than
  - `-ge`: greater than or equal
  - `-s`: smaller than
  - `-se`: smaller than or equal

Notes:
- Comparison on `phoneNum` does not apply to prefix `010`.
- `employeeNum` comparison follows join-year-first ordering semantics.

### OR-06. Logical Combination Option
- Combination operators:
  - `-a`: AND
  - `-o`: OR
- Supported commands: `DEL`, `SCH`, `MOD`
- Two conditions can be specified.
- Duplicate records must not appear more than once in search results.

## 11. Output Requirements
### General Rules
- Output is written to the output file.
- When `-p` is enabled, output must include the command name prefix:
  - `DEL,...`
  - `SCH,...`
  - `MOD,...`
- When there is no matching record:
  - `DEL,NONE`
  - `SCH,NONE`
  - `MOD,NONE`
- When an input line reaches an `IllegalArgumentException` path during parse/build/execute in the current CLI implementation:
  - `wrong command : <original line>`

### Count Output
- When `-p` is not applied:
  - `DEL,<count>`
  - `SCH,<count>`
  - `MOD,<count>`

### Printed Record Output
- At most 5 records are printed.
- Records are sorted by employee number order interpreted by join-year priority.
- `MOD` must print the record before modification.
- In the current formatter implementation, printed `DEL`, `SCH`, and `MOD` rows include the `certi` field.

### Current Source Notes
- `EmployeeManagement` catches `IllegalArgumentException` and writes `wrong command : <line>` to the output file.
- `CommandFactory` already builds `ADD` with `employeeNum,name,cl,phoneNum,birthday,certi`.
- `ResultStringFormatter` appends `certi` to printed employee rows.
- `CountCommand` / `CNT` exists in source and tests as an auxiliary command path, but it is not part of the original source requirement documents.

## 12. Command Contracts
### ADD Contract
- Adds one employee record.
- Record remains stored until deleted.

### DEL Contract
- Deletes all records matching the given condition.

### SCH Contract
- Finds all records matching the given condition.
- In `OR` condition flows, current implementation behavior uses one deduplicated employee match set.

### MOD Contract
- Updates all records matching the given condition.
- Only one target column can be changed.
- In the current CLI path, attempts to modify `employeeNum` are treated as invalid command flows.

## 13. Non-Functional Requirements
### NFR-01. Performance
- The system must support at least 100,000 employee records.
- After inserting 100,000 records, search, delete, and modify operations must still function correctly.

### NFR-02. Deterministic Output
- The same input must always produce the same output.
- Output ordering and formatting must follow requirement-defined rules exactly.

### NFR-03. Input Contract Stability
- Commands must be interpreted strictly according to the specified CSV-like format.
- Option positions must be respected.

### NFR-04. Testability
- The system should be easy to validate with text input/output comparison.
- Behavior must support automated unit and integration tests.

## 14. Constraints
- External modules related to DB may be used.
- Other implementation choices are flexible as long as the external behavior matches the requirement.
- Command input and result output are text-file based.

## 15. Risks and Edge Cases
- Ambiguity between base and extended final record shape must be resolved consistently in implementation.
- `MOD` output must use pre-change record state, which is easy to break during refactoring.
- `OR` search must avoid duplicate output.
- Join-year ordering derived from `employeeNum` must be implemented consistently across commands.
- Comparison rules for partial fields (`name`, `birthday`, `phoneNum`, `certi`) must match requirement semantics exactly.

## 16. Acceptance Criteria
### Core Acceptance
- The program reads an input text file and writes a valid output text file.
- `ADD`, `DEL`, `SCH`, and `MOD` behave according to their command contracts.
- All option positions and option semantics are supported correctly.

### Output Acceptance
- `-p` prints up to 5 records only.
- No-match behavior prints `COMMAND,NONE`.
- Non-`-p` behavior prints `COMMAND,count`.
- `MOD` printed records reflect pre-change state.

### Data Acceptance
- Employee fields follow required formats.
- Extended `certi` field is supported in the final product.
- `ADD` accepts the `certi` field in the current implementation.

### Scale Acceptance
- 100,000 record processing is supported.

## 17. Suggested Release View
### Phase 1: Base Features
- `ADD`
- `DEL`
- `SCH`
- `-p`
- ordering / `NONE` / count output

### Phase 2: Extended Features
- `MOD`
- `certi`
- detailed field options
- comparison search
- logical combination conditions

## 18. Final Product Definition
The final product is a file-driven employee database management CLI that supports high-volume employee record operations with strict command contracts, deterministic output rules, and extended filtering/modification capabilities.
