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
- Source-guided references:
  - `src/main/java/com/sec/bestreviewer/EmployeeManagement.java`
  - `src/main/java/com/sec/bestreviewer/CommandFactory.java`
  - `src/main/java/com/sec/bestreviewer/util/ResultStringFormatter.java`

## 2. Purpose
This document records the requirement ambiguities, contract conflicts, and architecture-sensitive decisions that must be locked before `RED` starts.

The goal is not to guess the answer, but to separate still-open contract gaps from decisions that the current source tree has already made.

## 3. Gap Log
| Gap ID | Status | Area | Current source-grounded state | Why it matters in RED |
| --- | --- | --- | --- | --- |
| G-01 | resolved in code, stale in docs | `certi` input contract | `CommandFactory` already builds `ADD` with `employeeNum,name,cl,phoneNum,birthday,certi`. | RED should test the implemented `ADD` shape, and docs must stop treating it as undecided. |
| G-02 | partially resolved | Invalid command behavior | `EmployeeManagement` writes `wrong command : <line>` for `IllegalArgumentException` paths, but malformed cases outside that path are still not fully normalized. | RED can assert current invalid-command output where the CLI already defines it. |
| G-03 | open | Invalid option-field combinations | Requirements define valid pairings, but dedicated tests and explicit docs for invalid pairings are still missing. | RED should add focused negative tests instead of guessing fallback behavior. |
| G-04 | resolved in current CLI path | `MOD employeeNum` rejection contract | Current `MOD employeeNum` attempts are treated as invalid command flows in the CLI path. | RED should document and test the current externally visible behavior. |
| G-05 | open | `employeeNum` ordering boundary | Join-year ordering exists in code and tests, but a direct formatter regression test for 1990s vs 2000s boundaries is still missing. | RED needs a stable assertion around century-boundary ordering. |
| G-06 | resolved in implementation shape | `OR` duplicate handling scope | Current matching behavior uses one deduplicated match set, so duplicates are not emitted or processed more than once. | RED should still add a regression test to lock this behavior. |
| G-07 | resolved in source, thinly documented | Comparison ordering for enumerated fields | `cl` and `certi` ordering are already implemented in field comparison logic and field tests. | Docs should reflect the current source ordering instead of treating it as undecided. |
| G-08 | open | 100,000-record acceptance definition | Scale support is a requirement, but no dedicated active test currently validates it. | RED and QA still need a measurable large-volume acceptance scenario. |

## 4. Recommended Resolution Order
1. Lock the remaining negative-contract behavior around `G-02` and `G-03`.
2. Add regression tests for `G-05` and `G-06` so currently implemented behavior is preserved.
3. Define a measurable acceptance strategy for `G-08` before `QA`.

## 5. Suggested RED Guardrails
- Use the current `ADD,...,birthday,certi` source shape in failing tests.
- Keep malformed-input tests focused on the currently implemented `IllegalArgumentException` output path unless the team expands the contract.
- Prefer contract tests at command/output level for sorting, `NONE`, max-5, and pre-change `MOD` output.
- Record every deferred ambiguity in the RED PR description so unowned gaps do not disappear.

## 6. Exit Criteria
- Every still-open ambiguity has an explicit team decision or an agreed temporary rule.
- The team can write `RED` tests without inventing hidden behavior.
- Base and Further requirements can be mapped to one unambiguous external contract.
