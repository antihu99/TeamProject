# Delivery Todo Plan

## 1. Document Overview
- Document name: `Delivery Todo Plan`
- Related product: `Employee Management CLI`
- Upstream documents:
  - `README.md`
  - `DOCS/00_PRD.md`
  - `DOCS/01_epic.md`
  - `DOCS/02_requirements_traceability.md`
  - `DOCS/03_gherkin.md`
- Source-guided references:
  - `src/main/java/com/sec/bestreviewer`
  - `src/test/java/com/sec/bestreviewer`

## 2. Purpose
This document restructures the project TODO list into an execution-ready plan aligned with:
- the README project schedule,
- the README quality and evaluation criteria,
- the branch strategy in this repository,
- the previously defined PRD, Epic, and traceability flow.

## 3. Working Principles from README
- Team code review is mandatory.
- TDD practice is mandatory.
- Test coverage target is 90% or higher.
- Small commits are recommended.
- Review quality matters from the perspectives of Clean Code, Refactoring, TDD, and Secure Coding.
- The final submission should be prepared through a release-oriented branch flow.

### Current Source Snapshot
- Current source already includes `MOD`, `certi`, name options, birthday options, comparison search, and `AND/OR` support.
- Current test tree already contains active tests for field parsing/comparison, name-option behavior, birthday comparisons, modify store behavior, and command output basics.
- The remaining meaningful RED gaps are malformed-command output, phone partial-field integration, invalid option-field compatibility, direct formatter regression, disabled end-to-end approval coverage, and 100,000-record validation.

## 4. Schedule-Aligned Delivery Plan
### Day 1
- Set up repository and team working rules.
- Analyze base code and requirement documents.
- Split work across `SPEC` individual branches.
- Finalize PRD, epic definition, traceability, and Gherkin scenarios.

### Day 2 to Day 4
- Build failing tests in `RED`.
- Implement base requirements in `GREEN`.
- Improve structure in `REFACTORING`.
- Implement extended requirements in `NEW_FEATURE`.

### Day 5
- Validate quality in `QA`.
- Prepare final report and presentation materials.
- Summarize learning points, review outcomes, and AI usage reflections.

## 5. Branch-Based Todo Plan
## Stage 1. SPEC
### Goal
Lock the requirement interpretation and split the work safely before feature development starts.

### Todo
- [ ] Finalize PRD from `Base.md` and `Further.md`
- [ ] Finalize Epic document
- [ ] Finalize Requirements Traceability Matrix
- [ ] Finalize Gherkin scenarios
- [ ] Confirm field definitions, option positions, and output rules
- [ ] Confirm branch ownership for `A_01_spec ~ A_04_spec`

### Exit Criteria
- The team shares one requirement interpretation.
- RED can begin from documented scenarios without ambiguity.


## Stage 2. RED
### Goal
Turn requirements into failing tests and coverage targets.

### Todo
- [ ] Add malformed-command output assertions for `EmployeeManagement` (`wrong command : <line>`)
- [ ] Add parser -> command -> store integration tests for phone options `-m`, `-l`
- [ ] Add explicit invalid option-field compatibility tests
- [ ] Add direct formatter regression tests for `NONE`, max-5, and join-year ordering boundaries
- [ ] Re-enable or replace disabled approval-style end-to-end tests
- [ ] Add missing command-level `MOD` output assertions
- [ ] Add explicit `OR` duplicate regression coverage
- [ ] Define and add a 100,000-record validation strategy
- [ ] Define a coverage improvement target toward 90%+

### Current Test Assets Already In Repo
- `EmployeeManagementTest`
- `CommandParserTest`
- `CommandExecutorTest`
- `CommandModTest`
- `AndOrParameterTest`
- `store/EmployeeStoreImplNameTest`
- `store/EmployeeStoreImplBirthdayTest`
- `store/EmployeeStoreImplCareerLevelTest`
- `store/EmployeeStoreImplModifyCommandTest`
- `field/EmployeeNumberTest`
- `field/CertiTest`
- `field/BirthdayTest`
- `field/PhoneNumberTest`

### README Mapping
This stage directly supports:
- TDD practice
- test code appropriateness
- code coverage 90%+


## Stage 3. GREEN
### Goal
Make the base requirement tests pass with minimal, requirement-accurate implementation.

### Todo
- [ ] Implement or fix `ADD`
- [ ] Implement or fix `DEL`
- [ ] Implement or fix `SCH`
- [ ] Implement changed `-p` behavior
- [ ] Ensure pre-defined count output behavior works when `-p` is not used
- [ ] Enforce printed record limit of 5
- [ ] Ensure `NONE` is printed when no result exists
- [ ] Validate input/output file execution flow

### Detailed `-p` Todo
- [ ] Print matching records when `-p` is applied
- [ ] Print only record count when `-p` is not applied
- [ ] Limit output to 5 printed records
- [ ] Print `COMMAND,NONE` when no record matches

### Exit Criteria
- Base requirement acceptance scenarios pass.
- Core command behavior is stable enough for refactoring.


## Stage 4. REFACTORING
### Goal
Improve readability and maintainability without changing external behavior.

### Todo
- [ ] Extract magic numbers into named constants
- [ ] Rename ambiguous variables and methods using domain language
- [ ] Split methods that are too long
- [ ] Remove duplicated logic
- [ ] Review SRP opportunities across major classes
- [ ] Preserve GREEN test status during all refactoring

### README Mapping
This stage directly supports:
- production code readability and maintainability
- review quality in Clean Code and Refactoring terms


## Stage 5. NEW_FEATURE
### Goal
Align extended requirements from `Further.md` with the current source tree and close any remaining implementation gaps.

### Todo
- [ ] Verify current `certi` implementation matches requirement and output contracts
- [ ] Verify `MOD` command behavior matches current RED assertions
- [ ] Close any remaining gaps in phone options `-m`, `-l`
- [ ] Close any remaining gaps in birthday month/day option handling
- [ ] Close any remaining gaps in comparison-search behavior
- [ ] Close any remaining gaps in `and/or` behavior and deduplication
- [ ] Add or update tests together with each behavior adjustment

### Recommended Order
1. requirement-vs-source gap confirmation
2. `certi` and printed row shape validation
3. `MOD` and command-level output alignment
4. phone/birthday option gap closure
5. comparison and `and/or` logic gap closure


## Stage 6. QA
### Goal
Validate the product against requirement, quality, and review expectations before final merge.

### Todo
- [ ] Verify all base requirements are satisfied
- [ ] Verify all extended requirements are satisfied
- [ ] Re-run regression tests for output formatting
- [ ] Re-check coverage target against README expectations
- [ ] Review code complexity and class/method size
- [ ] Review commit size and whether tests accompany changes
- [ ] Prepare final quality report
- [ ] Prepare 발표자료 and retrospective notes

### README Mapping
This stage directly supports:
- requirement evaluation
- software quality evaluation
- commit and review evaluation
- communication and final presentation readiness

## 6. README Todo Mapping
The following items reinterpret the original `README.md` todo list against the current source tree.

| README Todo Item | Planned Stage |
| --- | --- |
| certi column 추가에 따른 전체 TC 검토 및 보완 | `RED` |
| certi column 구현 정합성 검증 및 잔여 gap 보완 | `NEW_FEATURE` |
| -p 출력 옵션 변경에 대한 TC 검토 및 보완 | `RED` |
| -p 출력 구현 회귀 보완 | `GREEN` |
| MOD 기능 TC 구현 | `RED` |
| MOD 기능 명령 레벨 gap 보완 | `NEW_FEATURE` |
| 2옵션 이름, 성(-f, -l) TC 구현 | `RED` |
| 2옵션 이름, 성(-f, -l) 구현 정합성 검증 | `NEW_FEATURE` |
| 2옵션 전화번호(-m, -l) TC 구현 | `RED` |
| 2옵션 전화번호(-m, -l) gap 보완 | `NEW_FEATURE` |
| 2옵션 생년월일(-y, -m, -d) TC 구현 | `RED` |
| 2옵션 생년월일(-y, -m, -d) gap 보완 | `NEW_FEATURE` |
| 3옵션 부등호(SCH만 -g, -ge, -s, -se) TC 구현 | `RED` |
| 3옵션 부등호 구현 회귀 보완 | `NEW_FEATURE` |
| and, or 연산 옵션 (2옵션, 3옵션) TC 구현 | `RED` |
| and, or 연산 옵션 구현 회귀 보완 | `NEW_FEATURE` |
| refactoring | `REFACTORING` |

## 7. Team Split Suggestion
- `A_01_*`: command parsing and data model
- `A_02_*`: core command behavior
- `A_03_*`: output rules and test scenarios
- `A_04_*`: advanced options and integration validation

## 8. Final Readiness Checklist
- [ ] PRD, epic, traceability, Gherkin, and todo documents are aligned
- [ ] Base features pass requirement checks
- [ ] Extended features pass requirement checks
- [ ] Coverage is at or above target
- [ ] Review comments are reflected
- [ ] Final report and 발표자료 are ready
