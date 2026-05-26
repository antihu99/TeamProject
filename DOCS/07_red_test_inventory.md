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

## 2. Purpose
This document converts the `SPEC` analysis into an execution-ready failing-test backlog.

The focus is to answer three questions before `RED` begins:
1. Which contracts must be locked first?
2. Which test layer should own each contract?
3. How should the work be split across `A_01_RED ~ A_04_RED`?

## 3. Priority View
### P0: Must be written first
- `MOD -p` pre-change output contract
- `NONE` vs count output contract for `MOD`
- real parser-path coverage for `phoneNum -m/-l`
- real parser-path coverage for `birthday -m/-d`
- `SCH -o` duplicate suppression
- join-year ordering boundary around `90xxxxxx` vs `00xxxxxx`
- invalid option-field pair rejection behavior

### P1: Must follow immediately after P0
- `certi` comparison behavior
- `employeeNum` comparison behavior
- `DEL` and `MOD` real-path option combinations
- malformed command handling
- `MOD` with `-a` / `-o`

### P2: Hardening and regression coverage
- `ADD` duplicate `employeeNum` policy
- full field validation matrix
- disabled approval-style scenarios replaced by active tests
- scale fixture scaffolding for 100,000-record validation

## 4. Inventory Table
| Test ID | Priority | Req IDs | Scenario | Suggested layer | Target class/file | Recommended owner |
| --- | --- | --- | --- | --- | --- | --- |
| RED-01 | P0 | F-05, F-07 | `MOD,-p` prints pre-change rows and includes final row shape with `certi` | command/integration | `CommandExecutorTest` or new `CommandModOutputTest` | `A_04_RED` |
| RED-02 | P0 | F-01, F-02, F-05 | `MOD` without `-p` returns `MOD,<count>` and no match returns `MOD,NONE` | command/integration | new `CommandModOutputTest` | `A_04_RED` |
| RED-03 | P0 | F-10, F-11 | `phoneNum,-m/-l` parsing and matching through real command lines | integration | new `EmployeeStoreImplPhoneNumberTest` | `A_01_RED` |
| RED-04 | P0 | F-13, F-14 | `birthday,-m/-d` parsing and matching through real command lines | integration | extend `EmployeeStoreImplBirthdayTest` | `A_01_RED` |
| RED-05 | P0 | F-19, F-20, F-23 | `SCH,-o` returns union without duplicates | integration | new `AndOrCommandIntegrationTest` | `A_04_RED` |
| RED-06 | P0 | B-15, F-16 | join-year ordering boundary is deterministic across 1990s and 2000s | unit/formatter | `ResultStringFormatterTest` | `A_03_RED` |
| RED-07 | P0 | B-08, OR-02..OR-05 | invalid option-field combinations are rejected consistently | parser/command | new `OptionValidationTest` | `A_01_RED` |
| RED-08 | P1 | F-06, F-15, F-16 | `certi` comparison order for `-g/-ge/-s/-se` | integration | new `EmployeeStoreImplCertiTest` | `A_04_RED` |
| RED-09 | P1 | B-03, F-15, F-16 | `employeeNum` comparison order follows join-year-first semantics | integration | new `EmployeeStoreImplEmployeeNumberTest` | `A_03_RED` |
| RED-10 | P1 | F-21 | `DEL` with `-f/-l/-m/-y/-d` works through parser -> command -> store path | integration | extend `CommandExecutorTest` or new `DeleteCommandOptionTest` | `A_02_RED` |
| RED-11 | P1 | F-22 | `MOD` with `-a/-o` uses two conditions and one target mutation correctly | integration | new `ModCommandAndOrTest` | `A_04_RED` |
| RED-12 | P1 | B-08, B-13, F-03 | malformed command shape and bad option placement behavior | parser/end-to-end | `CommandParserTest`, `EmployeeManagementTest` | `A_01_RED` |
| RED-13 | P2 | B-10 | duplicate `employeeNum` add behavior is explicitly tested | domain/command | `AddCommandTest` or new `AddCommandValidationTest` | `A_02_RED` |
| RED-14 | P2 | B-03..B-07, F-06 | field validation matrix for all employee attributes | domain/unit | field tests under `field/` | `A_01_RED` |
| RED-15 | P2 | B-21, B-14..B-19, F-05 | existing disabled approval-style scenarios are converted into active assertions | end-to-end | `EmployeeManagementTest` | `A_03_RED` |
| RED-16 | P2 | B-20 | large-volume fixture and smoke assertion for 100,000 records | integration/performance | new `EmployeeManagementScaleTest` | `A_03_RED` |

## 5. Branch Split Suggestion
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

## 6. Suggested Test File Plan
| File | Purpose |
| --- | --- |
| `src/test/java/com/sec/bestreviewer/ResultStringFormatterTest.java` | sort, max-5, and count/`NONE` formatting |
| `src/test/java/com/sec/bestreviewer/OptionValidationTest.java` | invalid option position and field-option compatibility |
| `src/test/java/com/sec/bestreviewer/AndOrCommandIntegrationTest.java` | `-a` / `-o` integration coverage |
| `src/test/java/com/sec/bestreviewer/CommandModOutputTest.java` | `MOD` count vs print vs pre-change output |
| `src/test/java/com/sec/bestreviewer/store/EmployeeStoreImplPhoneNumberTest.java` | phone partial search behavior |
| `src/test/java/com/sec/bestreviewer/store/EmployeeStoreImplCertiTest.java` | `certi` comparison behavior |
| `src/test/java/com/sec/bestreviewer/store/EmployeeStoreImplEmployeeNumberTest.java` | `employeeNum` comparison and ordering semantics |

## 7. RED Exit Criteria
- Every P0 test exists and fails for a requirement-based reason.
- No failing test depends on guessed behavior from unresolved gaps in `DOCS/06_spec_gap_log.md`.
- Branch ownership is explicit for `A_01_RED ~ A_04_RED`.
- The team can hand `RED` outputs directly to `GREEN` without reinterpreting the contract.
