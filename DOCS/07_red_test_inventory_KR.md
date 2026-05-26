# RED 테스트 인벤토리

## 1. 문서 개요
- 문서명: `RED 테스트 인벤토리`
- 관련 단계: `RED`
- 상위 문서:
  - `DOCS/00_PRD_KR.md`
  - `DOCS/02_requirements_traceability_KR.md`
  - `DOCS/03_gherkin_KR.md`
  - `DOCS/04_todo_KR.md`
  - `DOCS/06_spec_gap_log_KR.md`
  - `requirement/Base.md`
  - `requirement/Further.md`
- 소스 기준 참조:
  - `src/main/java/com/sec/bestreviewer`
  - `src/test/java/com/sec/bestreviewer`

## 2. 목적
이 문서는 현재 소스 트리와 요구사항 분석을 바탕으로 RED 단계의 실행 가능한 테스트 backlog를 정리한다.

이 문서가 답해야 할 질문은 세 가지다.
1. 어떤 계약을 먼저 잠가야 하는가?
2. 현재 트리에 이미 어떤 테스트가 존재하는가?
3. 남은 작업을 `A_01_RED ~ A_04_RED`로 어떻게 나눌 것인가?

## 3. 현재 커버리지 기준선
### 이미 커버되었거나 상당 부분 커버된 항목
- `EmployeeNumber`, `Birthday`, `CareerLevel`, `Certi`, `Name`, `PhoneNumber`의 field parsing/comparison
- `CommandExecutorTest`를 통한 `DEL` / `SCH` count output, print output, `NONE`, max-5, sorted output
- `EmployeeStoreImplNameTest`를 통한 name option behavior
- `EmployeeStoreImplBirthdayTest`를 통한 birthday year comparison
- `EmployeeStoreImplModifyCommandTest`를 통한 store-level `MOD` pre-change behavior
- `CommandParserTest`를 통한 기본 command parsing

### 존재하지만 아직 불완전한 항목
- command-level `MOD` output coverage
- command-level `AND` / `OR` integration coverage
- `birthday,-m/-d` integration coverage
- duplicate `employeeNum` add policy
- 활성 end-to-end approval coverage

### 아직 의미 있게 비어 있는 항목
- `EmployeeManagement`의 malformed-command output assertion
- `phoneNum,-m/-l` parser -> command -> store integration test
- explicit invalid option-field compatibility test
- direct `ResultStringFormatter` regression test
- 100,000건 validation

## 4. 남은 RED 인벤토리
| Test ID | 우선순위 | Req IDs | 시나리오 | 현재 상태 | 제안 대상 클래스/파일 | 권장 담당 |
| --- | --- | --- | --- | --- | --- | --- |
| RED-01 | P0 | B-08, B-21 | invalid command line이 `wrong command : <line>`으로 출력된다 | 없음 | `EmployeeManagementTest` | `A_01_RED` |
| RED-02 | P0 | F-10, F-11 | `phoneNum,-m/-l`가 parser -> command -> store 경로로 동작한다 | 없음 | 새 `EmployeeStoreImplPhoneNumberTest` | `A_01_RED` |
| RED-03 | P0 | B-15, F-16 | `90xxxxxx` vs `00xxxxxx` 정렬 경계가 정확하다 | direct test 없음 | 새 `ResultStringFormatterTest` | `A_03_RED` |
| RED-04 | P0 | B-08, F-08..F-17 | invalid option-field combinations를 명시적으로 거절한다 | 없음 | 새 `OptionValidationTest` | `A_01_RED` |
| RED-05 | P0 | F-05, F-07 | command-level `MOD,-p`가 pre-change row shape와 `certi`를 포함한다 | store-level partial | 새 `CommandModOutputTest` | `A_04_RED` |
| RED-06 | P0 | F-19, F-20, F-23 | `SCH,-o` duplicate suppression이 유지된다 | 간접 partial | 새 또는 확장 `AndOrCommandIntegrationTest` | `A_04_RED` |
| RED-07 | P1 | F-13, F-14 | `birthday,-m/-d` integration behavior를 real command로 검증한다 | partial | 확장 `EmployeeStoreImplBirthdayTest` | `A_01_RED` |
| RED-08 | P1 | F-21 | `DEL` + secondary option이 parser -> command -> store 경로로 동작한다 | partial | 확장 `CommandExecutorTest` 또는 새 `DeleteCommandOptionTest` | `A_02_RED` |
| RED-09 | P1 | F-22 | `MOD` + `-a/-o`가 command-level 통합으로 동작한다 | partial | 새 `ModCommandAndOrTest` | `A_04_RED` |
| RED-10 | P1 | B-10 | duplicate `employeeNum` add policy를 failing test로 고정한다 | weak | 새 `AddCommandValidationTest` | `A_02_RED` |
| RED-11 | P2 | B-21, B-14..B-19 | disabled approval 테스트를 대체하거나 다시 활성화한다 | 현재 disabled | `EmployeeManagementTest` | `A_03_RED` |
| RED-12 | P2 | B-20 | 100,000건 acceptance 시나리오가 존재한다 | 없음 | 새 `EmployeeManagementScaleTest` | `A_03_RED` |

## 5. 현재 가치가 높은 기존 테스트 파일
| 파일 | 현재 제공하는 가치 |
| --- | --- |
| `src/test/java/com/sec/bestreviewer/CommandExecutorTest.java` | `DEL` / `SCH` 출력 기본 계약, `NONE`, max-5, sorting, add no-output |
| `src/test/java/com/sec/bestreviewer/CommandParserTest.java` | valid command parsing, 기본 `-a` parsing |
| `src/test/java/com/sec/bestreviewer/EmployeeManagementTest.java` | 인자 검증과 disabled end-to-end approval hook |
| `src/test/java/com/sec/bestreviewer/store/EmployeeStoreImplModifyCommandTest.java` | pre-change modify behavior, employee-number modification rejection |
| `src/test/java/com/sec/bestreviewer/store/EmployeeStoreImplNameTest.java` | first/last-name filtering과 comparison behavior |
| `src/test/java/com/sec/bestreviewer/store/EmployeeStoreImplBirthdayTest.java` | birthday year comparison과 한 개의 `AND` case |
| `src/test/java/com/sec/bestreviewer/field/EmployeeNumberTest.java` | employee-number comparison semantics |
| `src/test/java/com/sec/bestreviewer/field/CertiTest.java` | certi ordering semantics |

## 6. 브랜치 분담 제안
### `A_01_RED`
- parser validation
- option compatibility
- field-level / partial-field test
- malformed input case

### `A_02_RED`
- `ADD`, `DEL`, `SCH` base command 취약 구간
- bulk behavior와 duplicate-value scenario
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

## 7. 제안 테스트 파일 계획
| 파일 | 목적 |
| --- | --- |
| `src/test/java/com/sec/bestreviewer/ResultStringFormatterTest.java` | sorting, max-5, count/`NONE` formatting |
| `src/test/java/com/sec/bestreviewer/OptionValidationTest.java` | invalid option position, field-option compatibility |
| `src/test/java/com/sec/bestreviewer/AndOrCommandIntegrationTest.java` | `-a` / `-o` integration coverage |
| `src/test/java/com/sec/bestreviewer/CommandModOutputTest.java` | `MOD` count vs print vs pre-change output |
| `src/test/java/com/sec/bestreviewer/store/EmployeeStoreImplPhoneNumberTest.java` | phone partial search behavior |
| `src/test/java/com/sec/bestreviewer/store/EmployeeStoreImplCertiTest.java` | `certi` comparison behavior |
| `src/test/java/com/sec/bestreviewer/store/EmployeeStoreImplEmployeeNumberTest.java` | `employeeNum` comparison과 ordering semantics |

## 8. RED 종료 기준
- 모든 P0 항목이 requirement-based failing test로 존재한다.
- failing test가 `DOCS/06_spec_gap_log_KR.md`의 source-guided contract와 충돌하지 않는다.
- `A_01_RED ~ A_04_RED`의 소유권이 명확하다.
- `GREEN` 단계가 기억이 아니라 테스트로 잠긴 계약에서 시작될 수 있다.
