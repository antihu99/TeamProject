# SPEC Gap Log

## 1. Document Overview
- Document name: `SPEC Gap Log`
- Related stage: `SPEC`
- Upstream documents:
  - `requirement/Base.md`
  - `requirement/Further.md`
  - `DOCS/00_PRD.md`
  - `DOCS/02_requirements_traceability.md`
- Related downstream stage:
  - `RED`

## 2. Purpose
This document records the requirement ambiguities, contract conflicts, and architecture-sensitive decisions that must be locked before `RED` starts.

The goal is not to guess the answer, but to make unresolved interpretation points explicit so that failing tests in `RED` do not encode the wrong contract.

## 3. Gap Log
| Gap ID | Area | Current gap or conflict | Why it blocks RED | Required decision |
| --- | --- | --- | --- | --- |
| G-01 | `certi` input contract | `Further.md` adds `certi` to the final data model and printed output, but the base `ADD` command format is not updated to include a `certi` input value. Current code already expects 6 employee data fields. | Failing tests cannot decide whether `ADD` should accept 5 or 6 employee data values. | Lock one rule: extend `ADD` format, define default `certi`, or stage `certi` separately until `NEW_FEATURE`. |
| G-02 | Invalid command behavior | Source docs require strict format, but they do not define the external behavior for malformed commands, missing tokens, unknown fields, or invalid option positions. | Parser and end-to-end failure tests need an observable output contract. | Decide whether invalid lines print an error message, print `NONE`, are ignored, or terminate execution. |
| G-03 | Invalid option-field combinations | Docs define valid combinations such as `name + -f/-l`, `phoneNum + -m/-l`, `birthday + -y/-m/-d`, but do not define behavior for invalid combinations. Current implementation may silently fall back to full-field behavior. | RED needs to know whether to reject or tolerate invalid combinations. | Decide whether invalid combinations must be rejected explicitly or treated as invalid input. |
| G-04 | `MOD employeeNum` rejection contract | Docs say `employeeNum` cannot be modified, but do not define the exact external behavior. | `MOD` failure tests need to know whether this is an exception, `MOD,NONE`, count `0`, or ignored mutation. | Lock the externally visible rule for forbidden modification attempts. |
| G-05 | `employeeNum` ordering boundary | Sorting rules explain join-year priority, but the boundary behavior across `90`~`99` and `00`~`19` needs explicit testable wording. | Formatter tests must encode the same year interpretation across all commands. | Define canonical ordering examples that cover 1990s vs 2000s and tie-break behavior inside the same join year. |
| G-06 | `OR` duplicate handling scope | Docs explicitly forbid duplicate search results for `SCH -o`, but do not state whether the same dedup rule must also apply to internal delete/modify match sets. | RED tests for `-o` need a consistent matching set definition. | Clarify whether deduplication is only an output rule for `SCH` or a shared matching rule for `DEL`/`MOD` as well. |
| G-07 | Comparison ordering for enumerated fields | `cl` and `certi` examples imply ordered comparison semantics, but the total order is not normalized in one source of truth. | Comparison tests need one stable ordering contract. | Confirm `cl: CL1 < CL2 < CL3 < CL4` and `certi: ADV < PRO < EX` as explicit comparison orders. |
| G-08 | 100,000-record acceptance definition | Docs require 100,000-record support, but do not define measurable acceptance criteria beyond “must work.” | RED/QA cannot turn scale requirements into executable acceptance checks. | Define minimum scale test shape: insert volume, required operations, and expected observable outcome. |

## 4. Recommended Resolution Order
1. Resolve `G-01` because `certi` affects command syntax, field validation, output format, and test fixtures.
2. Resolve `G-02`, `G-03`, and `G-04` because they directly control negative test expectations.
3. Resolve `G-05`, `G-06`, and `G-07` because they affect deterministic result assertions.
4. Resolve `G-08` before `QA` so scale requirements are testable and reviewable.

## 5. Suggested RED Guardrails
- Do not write failing tests that guess the `certi` input shape.
- Keep malformed-input tests pending until invalid-command output is explicitly decided.
- Prefer contract tests at command/output level for sorting, `NONE`, max-5, and pre-change `MOD` output.
- Record every deferred ambiguity in the RED PR description so unowned gaps do not disappear.

## 6. Exit Criteria
- Every high-risk ambiguity has an explicit team decision or an agreed temporary rule.
- The team can write `RED` tests without inventing hidden behavior.
- Base and Further requirements can be mapped to one unambiguous external contract.
