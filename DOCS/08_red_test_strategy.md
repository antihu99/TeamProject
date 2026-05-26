# RED Test Strategy

## 1. Document Overview
- Document name: `RED Test Strategy`
- Related stage: `RED`
- Upstream documents:
  - `requirement/Base.md`
  - `requirement/Further.md`
  - `DOCS/00_PRD.md`
  - `DOCS/02_requirements_traceability.md`
  - `DOCS/03_gherkin.md`
  - `DOCS/04_todo.md`
  - `DOCS/07_red_test_inventory.md`
- Source-guided references:
  - `src/main/java/com/sec/bestreviewer`
  - `src/test/java/com/sec/bestreviewer`

## 2. Purpose
This document records the current RED-stage test status using the actual source tree as the primary grounding.

The purpose is to make the team align on:
- what is already covered,
- what is still missing,
- which failing tests should be written first,
- how to split the work across `A_01_RED ~ A_04_RED`.

## 3. Current Test State
### Stronger areas
- `SCH` is the strongest feature area overall.
  - `CommandExecutorTest`
  - `EmployeeStoreImplNameTest`
  - `EmployeeStoreImplBirthdayTest`
  - `EmployeeStoreImplCareerLevelTest`
- field parsing and comparison tests exist for:
  - `EmployeeNumber`
  - `Birthday`
  - `CareerLevel`
  - `Certi`
  - `Name`
  - `PhoneNumber`
- `DEL` / `SCH` output contracts already have coverage for:
  - `NONE`
  - max 5 rows
  - sorted print output
  - count output

### Partially covered areas
- `ADD`
  - `CommandExecutorTest` and store tests exist, but real `AddCommand.execute()` behavior and duplicate `employeeNum` policy are not strongly locked.
- `MOD`
  - store-level pre-change behavior exists in `EmployeeStoreImplModifyCommandTest`
  - command-level `MOD,-p` output contract is still weak
- `AND/OR`
  - parsing and some store behavior exist
  - asserted command-level `OR` behavior is still thin
- `certi`
  - field-level ordering exists
  - command-level search/modify integration is still weak

### Weak or missing areas
- malformed command output behavior in `EmployeeManagement`
- `phoneNum,-m/-l` parser -> command -> store integration
- invalid option-field compatibility tests
- direct `ResultStringFormatter` regression tests
- active end-to-end approval coverage
- 100,000-record validation

## 4. Missing Test List
### P0
- malformed command line should be written as `wrong command : <line>`
- `phoneNum,-m/-l` should work through real command paths
- invalid option-field combinations should be explicitly rejected or locked
- `MOD,-p` should assert pre-change row shape including `certi`
- `SCH,-o` duplicate suppression should be regression-locked

### P1
- `birthday,-m/-d` integration behavior
- `DEL` with secondary options through parser -> command -> store
- formatter boundary regression for `90xxxxxx` vs `00xxxxxx`
- `MOD` with `-a/-o` command-level integration
- duplicate `employeeNum` add policy

### P2
- disabled approval-style tests should be re-enabled or replaced
- 100,000-record acceptance test

## 5. Failure Test Strategy
### 1. Lock externally visible CLI behavior first
Start with tests that define what the user sees from the outside:
- malformed command output
- invalid option/field compatibility
- `NONE`
- max 5
- deterministic sorting

### 2. Add real parser -> command -> store integration tests
Avoid relying only on field-level or mocked tests.

Priority targets:
- `phoneNum,-m/-l`
- `birthday,-m/-d`
- `DEL` option flows
- `MOD` option flows
- `AND/OR` overlap behavior

### 3. Lock command-level `MOD` behavior
Even though store-level modify behavior exists, RED should still pin:
- `MOD,-p` pre-change output
- `MOD,NONE`
- printed row shape including `certi`

### 4. Finish with regression and non-functional risk
After contract and integration failures are written:
- restore or replace disabled approval tests
- add scale-oriented validation

## 6. Branch Split
### `A_01_RED`
- malformed command handling
- parser validation
- invalid option-field combinations
- `phoneNum,-m/-l`
- `birthday,-m/-d`

### `A_02_RED`
- `ADD` weak spots
- duplicate `employeeNum` add policy
- `DEL` option integration
- `SCH` base regression gaps

### `A_03_RED`
- `ResultStringFormatter`
- `NONE`
- max-5
- join-year sorting boundary
- disabled approval replacement
- scale fixture preparation

### `A_04_RED`
- `MOD`
- `certi`
- comparison search
- `AND/OR`
- OR duplicate suppression

## 7. Priority Test Files
| File | Why it should come early |
| --- | --- |
| `src/test/java/com/sec/bestreviewer/EmployeeManagementTest.java` | malformed command output and disabled E2E coverage |
| `src/test/java/com/sec/bestreviewer/store/EmployeeStoreImplPhoneNumberTest.java` | phone partial-field integration is currently a major gap |
| `src/test/java/com/sec/bestreviewer/OptionValidationTest.java` | invalid option-position and field-option compatibility need explicit contracts |
| `src/test/java/com/sec/bestreviewer/CommandModOutputTest.java` | command-level `MOD,-p` behavior is not locked strongly enough |
| `src/test/java/com/sec/bestreviewer/ResultStringFormatterTest.java` | direct formatting and ordering regression checks are missing |
| `src/test/java/com/sec/bestreviewer/AndOrCommandIntegrationTest.java` | asserted `-a/-o` integration and OR duplicate suppression |
| `src/test/java/com/sec/bestreviewer/EmployeeManagementScaleTest.java` | 100,000-record requirement currently has no active verification |

## 8. Coverage Risks
- The current raw test count can overstate confidence because some tests are mocked or field-level only.
- `MOD` and `AND/OR` look more mature than they are because store-level coverage is better than command-level coverage.
- disabled approval tests reduce true regression confidence.
- the 100,000-record requirement is still documented but not locked by an active automated test.

## 9. RED Exit Criteria
- Every P0 item exists as a failing test or an explicit tracked decision.
- No failing test contradicts `requirement/Base.md`, `requirement/Further.md`, or the current source-guided contract docs.
- Branch ownership is explicit for `A_01_RED ~ A_04_RED`.
- `GREEN` can start from a stable, test-defined contract rather than interpretation by memory.
