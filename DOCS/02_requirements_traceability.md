# Requirements Traceability Matrix

## 1. Document Overview
- Document name: `Requirements Traceability Matrix`
- Related product: `Employee Management CLI`
- Upstream documents:
  - `requirement/Base.md`
  - `requirement/Further.md`
- Related downstream document:
  - `DOCS/00_PRD.md`

## 2. Purpose
This document connects source requirements to PRD sections and implementation/test focus areas so that no requirement is lost during design, coding, refactoring, or QA.

## 3. Traceability Matrix
| Req ID | Source | Requirement Summary | PRD Mapping | Implementation / Test Focus |
| --- | --- | --- | --- | --- |
| B-01 | Base | Program manages employee database records | PRD 2, 4, 18 | Core product scope and data lifecycle |
| B-02 | Base | Employee record includes `employeeNum`, `name`, `cl`, `phoneNum`, `birthday` | PRD 8 | Data model validation |
| B-03 | Base | `employeeNum` is 8 digits with join-year semantics | PRD 8, 10, 11 | field validation, ordering, comparison rules |
| B-04 | Base | `name` is uppercase English with first/last name structure | PRD 8, 10 | field validation, partial name filtering |
| B-05 | Base | `cl` must be one of `CL1` to `CL4` | PRD 8 | enum/value validation |
| B-06 | Base | `phoneNum` must match `010-xxxx-xxxx` | PRD 8, 10 | field validation, partial phone filtering |
| B-07 | Base | `birthday` must match `YYYYMMDD` | PRD 8, 10 | field validation, partial birthday filtering |
| B-08 | Base | Command format is fixed and option-driven | PRD 7, 13 | parser contract and command normalization |
| B-09 | Base | `ADD` adds a new employee | PRD 9 FR-01, 12 | add flow and persistence behavior |
| B-10 | Base | `ADD` allows duplicates except employee number semantics, but fields cannot be null | PRD 9 FR-01 | input validation and record creation |
| B-11 | Base | `DEL` deletes all matching records by condition | PRD 9 FR-02, 12 | bulk delete behavior |
| B-12 | Base | `SCH` searches all matching records by condition | PRD 9 FR-03, 12 | bulk search behavior |
| B-13 | Base | `-p` exists only in option1 position | PRD 10 OR-01, 13 | parser enforcement |
| B-14 | Base | `-p` prints records line by line | PRD 10 OR-01, 11 | printed output format |
| B-15 | Base | Printed records are sorted by earlier join year first | PRD 10 OR-01, 11 | sorting logic and approval tests |
| B-16 | Base | Printed record count is capped at 5 | PRD 10 OR-01, 11 | max output constraint |
| B-17 | Base | Without `-p`, affected record count is printed | PRD 11 | count output contract |
| B-18 | Base | No matching record prints `COMMAND,NONE` | PRD 11 | no-match behavior |
| B-19 | Base | `ADD` does not use output printing behavior | PRD 9 FR-01 | add output handling |
| B-20 | Base | System must support at least 100,000 records | PRD 4, 13, 16 | scalability and performance validation |
| B-21 | Base | Program reads input txt and writes output txt | PRD 7, 16 | CLI execution contract |
| F-01 | Further | `MOD` command is added | PRD 9 FR-04, 12 | update flow implementation |
| F-02 | Further | `MOD` updates all matching records | PRD 9 FR-04, 12 | bulk modify behavior |
| F-03 | Further | Only one field may be changed per `MOD` command | PRD 9 FR-04, 12 | parser and mutation constraint |
| F-04 | Further | `employeeNum` cannot be modified | PRD 9 FR-04 | immutable field enforcement |
| F-05 | Further | `MOD -p` prints pre-change records | PRD 9 FR-04, 11, 16 | pre-change snapshot handling |
| F-06 | Further | New `certi` column supports `ADV`, `PRO`, `EX` | PRD 8, 9, 16 | extended data model and validation |
| F-07 | Further | Printed output after certi addition must include `certi` | PRD 11 | output schema evolution |
| F-08 | Further | Name option `-f` filters by first name | PRD 10 OR-02 | option parsing and condition evaluation |
| F-09 | Further | Name option `-l` filters by last name | PRD 10 OR-02 | option parsing and condition evaluation |
| F-10 | Further | Phone option `-m` filters middle digits | PRD 10 OR-03 | partial phone search/update/delete |
| F-11 | Further | Phone option `-l` filters last digits | PRD 10 OR-03 | partial phone search/update/delete |
| F-12 | Further | Birthday option `-y` filters by year | PRD 10 OR-04 | partial birthday handling |
| F-13 | Further | Birthday option `-m` filters by month | PRD 10 OR-04 | partial birthday handling |
| F-14 | Further | Birthday option `-d` filters by day | PRD 10 OR-04 | partial birthday handling |
| F-15 | Further | `SCH` supports comparison options `-g`, `-ge`, `-s`, `-se` | PRD 10 OR-05 | comparison-based search |
| F-16 | Further | Comparison semantics follow field ordering rules | PRD 10 OR-05, 15 | cross-field comparison consistency |
| F-17 | Further | Phone prefix `010` is excluded from comparison semantics | PRD 10 OR-05 | special comparison handling |
| F-18 | Further | `-a` adds AND condition composition | PRD 10 OR-06 | dual-condition query evaluation |
| F-19 | Further | `-o` adds OR condition composition | PRD 10 OR-06 | dual-condition query evaluation |
| F-20 | Further | OR results must not contain duplicate records | PRD 10 OR-06, 15 | deduplication behavior |
| F-21 | Further | `DEL` supports AND/OR condition forms | PRD 10 OR-06 | delete parser and condition composition |
| F-22 | Further | `MOD` supports AND/OR condition forms | PRD 10 OR-06 | modify parser and condition composition |
| F-23 | Further | `SCH` supports AND/OR condition forms | PRD 10 OR-06 | search parser and condition composition |

## 4. Coverage View by Feature Group
### Base Requirement Coverage
- command execution: `B-08`, `B-21`
- data model: `B-02` to `B-07`
- add/search/delete: `B-09` to `B-12`
- output rules: `B-13` to `B-19`
- scale/performance: `B-20`

### Extended Requirement Coverage
- modify command: `F-01` to `F-05`
- certi model/output: `F-06`, `F-07`
- field-specific options: `F-08` to `F-14`
- comparison search: `F-15` to `F-17`
- logical composition: `F-18` to `F-23`

## 5. Suggested Test Mapping
### Unit-Level Focus
- field parsing and validation
- option parsing by position
- employee number ordering/comparison
- condition matching helpers

### Integration-Level Focus
- end-to-end command parsing and execution
- output formatting by command type
- `MOD` pre-change output
- `AND` / `OR` dual-condition flows
- `NONE` and max-5 print behavior

### Performance-Level Focus
- 100,000 record insertion
- search/delete/modify after large data load

## 6. Review Checklist
- Every source requirement has a `Req ID`.
- Every `Req ID` is mapped to a PRD section.
- Every high-risk rule has a test focus note.
- Base and Further requirements are both covered.
- Output rules and scale constraints are explicitly tracked.
