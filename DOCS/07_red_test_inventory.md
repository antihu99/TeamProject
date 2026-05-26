# RED Test Inventory

## 1. Document Overview
- Document name: `RED Test Inventory`
- Related stage: `RED`
- Upstream documents:
  - `DOCS/00_PRD.md`
  - `DOCS/02_requirements_traceability.md`
  - `DOCS/03_gherkin.md`
  - `DOCS/04_todo.md`
  - `DOCS/06_spec_gap_log.md`
  - `requirement/Base.md`
  - `requirement/Further.md`
- Source-guided references:
  - `src/main/java/com/sec/bestreviewer`
  - `src/test/java/com/sec/bestreviewer`

## 2. Purpose
This document converts the current source tree and requirement analysis into an execution-ready RED backlog.

The focus is to answer three questions before `RED` begins:
1. Which contracts must be locked first?
2. Which tests are already present in the tree?
3. How should the remaining work be split across `A_01_RED ~ A_04_RED`?

## 3. Current Coverage Baseline
### Already covered or largely covered
- field parsing and comparison for `EmployeeNumber`, `Birthday`, `CareerLevel`, `Certi`, `Name`, `PhoneNumber`
- `DEL` / `SCH` count output, print output, `NONE`, max-5, and sorted output through `CommandExecutorTest`
- name option behavior through `EmployeeStoreImplNameTest`
- birthday year comparison behavior through `EmployeeStoreImplBirthdayTest`
- store-level `MOD` pre-change behavior through `EmployeeStoreImplModifyCommandTest`
- basic command parsing through `CommandParserTest`

### Present but still incomplete
- `MOD` command-level output coverage
- `AND` / `OR` command integration coverage
- birthday month/day option integration coverage
- duplicate `employeeNum` add policy
- active end-to-end approval coverage

### Still meaningfully missing
- malformed-command output assertions in `EmployeeManagement`
- phone `-m/-l` parser -> command -> store integration tests
- explicit invalid option-field compatibility tests
- direct `ResultStringFormatter` regression tests
- 100,000-record validation

## 4. Remaining RED Inventory
| Test ID | Priority | Req IDs | Scenario | Current state | Suggested target class/file | Recommended owner |
| --- | --- | --- | --- | --- | --- | --- |
| RED-01 | P0 | B-08, B-21 | invalid command lines are written as `wrong command : <line>` | missing | `EmployeeManagementTest` | `A_01_RED` |
| RED-02 | P0 | F-10, F-11 | `phoneNum,-m/-l` works through parser -> command -> store path | missing | new `EmployeeStoreImplPhoneNumberTest` | `A_01_RED` |
| RED-03 | P0 | B-15, F-16 | formatter ordering boundary is correct for `90xxxxxx` vs `00xxxxxx` | missing direct test | new `ResultStringFormatterTest` | `A_03_RED` |
| RED-04 | P0 | B-08, F-08..F-17 | invalid option-field combinations are rejected explicitly | missing | new `OptionValidationTest` | `A_01_RED` |
| RED-05 | P0 | F-05, F-07 | command-level `MOD,-p` output includes pre-change row shape and `certi` | partially covered at store level | new `CommandModOutputTest` | `A_04_RED` |
| RED-06 | P0 | F-19, F-20, F-23 | `SCH,-o` duplicate suppression stays stable | partially covered indirectly | new or extend `AndOrCommandIntegrationTest` | `A_04_RED` |
| RED-07 | P1 | F-13, F-14 | `birthday,-m/-d` integration behavior is verified through real commands | partially covered | extend `EmployeeStoreImplBirthdayTest` | `A_01_RED` |
| RED-08 | P1 | F-21 | `DEL` with secondary options works through parser -> command -> store path | partially covered | extend `CommandExecutorTest` or add `DeleteCommandOptionTest` | `A_02_RED` |
| RED-09 | P1 | F-22 | `MOD` with `-a/-o` works through command-level integration | partially covered | new `ModCommandAndOrTest` | `A_04_RED` |
| RED-10 | P1 | B-10 | duplicate `employeeNum` add behavior is locked by failing test | weakly covered | new `AddCommandValidationTest` | `A_02_RED` |
| RED-11 | P2 | B-21, B-14..B-19 | disabled approval-style tests are replaced or re-enabled | disabled now | `EmployeeManagementTest` | `A_03_RED` |
| RED-12 | P2 | B-20 | 100,000-record acceptance scenario exists | missing | new `EmployeeManagementScaleTest` | `A_03_RED` |

## 5. Existing High-Value Test Files
| File | What it already gives us |
| --- | --- |
| `src/test/java/com/sec/bestreviewer/CommandExecutorTest.java` | `DEL` / `SCH` output basics, `NONE`, max-5, sorting, add no-output behavior |
| `src/test/java/com/sec/bestreviewer/CommandParserTest.java` | valid command parsing and basic `-a` parsing |
| `src/test/java/com/sec/bestreviewer/EmployeeManagementTest.java` | argument validation and disabled end-to-end approval hooks |
| `src/test/java/com/sec/bestreviewer/store/EmployeeStoreImplModifyCommandTest.java` | pre-change modify behavior and employee-number modification rejection |
| `src/test/java/com/sec/bestreviewer/store/EmployeeStoreImplNameTest.java` | first/last-name filtering and comparison behavior |
| `src/test/java/com/sec/bestreviewer/store/EmployeeStoreImplBirthdayTest.java` | birthday year comparisons and one `AND` case |
| `src/test/java/com/sec/bestreviewer/field/EmployeeNumberTest.java` | employee-number comparison semantics |
| `src/test/java/com/sec/bestreviewer/field/CertiTest.java` | certi ordering semantics |

## 6. Branch Split Suggestion
### `A_01_RED`
- parser validation
- option compatibility
- field-level and partial-field tests
- malformed input cases

### `A_02_RED`
- `ADD`, `DEL`, `SCH` base command failing tests
- bulk behavior and duplicate-value scenarios
- delete-path option coverage

### `A_03_RED`
- output formatting
- `NONE`
- max-5
- join-year sorting
- large-volume fixture preparation

### `A_04_RED`
- `MOD`
- `certi`
- comparison search
- `AND` / `OR`
- advanced integration coverage

## 7. Suggested Test File Plan
| File | Purpose |
| --- | --- |
| `src/test/java/com/sec/bestreviewer/ResultStringFormatterTest.java` | sort, max-5, and count/`NONE` formatting |
| `src/test/java/com/sec/bestreviewer/OptionValidationTest.java` | invalid option position and field-option compatibility |
| `src/test/java/com/sec/bestreviewer/AndOrCommandIntegrationTest.java` | `-a` / `-o` integration coverage |
| `src/test/java/com/sec/bestreviewer/CommandModOutputTest.java` | `MOD` count vs print vs pre-change output |
| `src/test/java/com/sec/bestreviewer/store/EmployeeStoreImplPhoneNumberTest.java` | phone partial search behavior |
| `src/test/java/com/sec/bestreviewer/store/EmployeeStoreImplCertiTest.java` | `certi` comparison behavior |
| `src/test/java/com/sec/bestreviewer/store/EmployeeStoreImplEmployeeNumberTest.java` | `employeeNum` comparison and ordering semantics |

## 8. RED Exit Criteria
- Every P0 test exists and fails for a requirement-based reason.
- No failing test depends on guessed behavior that contradicts current source-guided contracts in `DOCS/06_spec_gap_log.md`.
- Branch ownership is explicit for `A_01_RED ~ A_04_RED`.
- The team can hand `RED` outputs directly to `GREEN` without reinterpreting the contract.
