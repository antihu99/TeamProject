# RED 테스트 계획서

## 1. 문서 개요
- 문서명: `RED 테스트 계획서`
- 관련 단계: `RED`
- 목적: 현재 소스와 요구사항을 기준으로 실패 테스트 작성 순서, 범위, 분담, 산출물을 실행 계획 형태로 고정한다.
- 상위 문서:
  - `requirement/Base.md`
  - `requirement/Further.md`
  - `DOCS/00_PRD_KR.md`
  - `DOCS/02_requirements_traceability_KR.md`
  - `DOCS/03_gherkin_KR.md`
  - `DOCS/07_red_test_inventory_KR.md`
  - `DOCS/08_red_test_strategy_KR.md`
- 소스 기준 참조:
  - `src/main/java/com/sec/bestreviewer`
  - `src/test/java/com/sec/bestreviewer`

## 2. 목표
RED 단계의 목표는 구현을 먼저 고치는 것이 아니라, 현재 구현이 요구사항을 어디서 만족하지 못하는지 실패 테스트로 명확하게 드러내는 것이다.

이번 테스트 계획의 목표는 다음과 같다.
- 외부 계약을 먼저 고정한다.
- parser -> command -> store 실경로 테스트를 늘린다.
- mocked test와 field-level test에 치우친 현재 커버리지를 command-level 통합 테스트로 보완한다.
- 4개 개인 브랜치가 병렬로 작업할 수 있도록 충돌이 적은 작업 단위로 나눈다.
- `GREEN` 단계가 기억이 아닌 failing test 기준으로 시작되게 한다.

## 3. 현재 기준선
### 강한 영역
- `SCH` 관련 비교/필터링 테스트
- `DEL` / `SCH`의 `NONE`, count, max-5, 정렬 기본 출력
- field parsing/comparison
- store-level `MOD` pre-change behavior

### 약한 영역
- malformed command 출력
- `phoneNum,-m/-l` integration
- invalid option-field compatibility
- command-level `MOD,-p`
- explicit `OR` duplicate suppression
- direct `ResultStringFormatter` regression
- 100,000건 validation

## 4. 테스트 범위
### In Scope
- `ADD`, `DEL`, `SCH`, `MOD`
- `certi`
- 세부 옵션: `-f`, `-l`, `-m`, `-y`, `-d`
- 비교 옵션: `-g`, `-ge`, `-s`, `-se`
- 논리 옵션: `-a`, `-o`
- `NONE`
- 최대 5건 출력
- join-year 기반 정렬
- invalid command 처리

### Out of Scope
- production code 리팩토링
- GREEN 구현 수정
- 성능 최적화 자체
- 발표자료 작성

## 5. 테스트 전략
### 5.1 외부 계약 우선
가장 먼저 사용자가 직접 관측하는 결과를 고정한다.

우선 테스트:
- invalid command line -> `wrong command : <line>`
- `NONE`
- count output
- max-5 output
- join-year ordering boundary

### 5.2 실경로 통합 테스트 우선
field-level / mocked coverage가 아닌 실제 입력 경로를 늘린다.

우선 테스트:
- `phoneNum,-m/-l`
- `birthday,-m/-d`
- `DEL` option path
- `MOD` option path
- `AND/OR` overlap path

### 5.3 수정 계약 분리 고정
`MOD`는 store-level과 command-level을 분리해서 본다.

고정 대상:
- `MOD,-p` pre-change output
- `MOD,NONE`
- printed row shape with `certi`
- `employeeNum` modification rejection

### 5.4 회귀/비기능 보완
계약과 통합 테스트가 잠긴 뒤 다음을 수행한다.
- disabled approval 테스트 대체/복원
- scale validation 추가

## 6. 우선순위 계획
### P0: 먼저 작성할 실패 테스트
1. malformed command output
2. `phoneNum,-m/-l` integration
3. invalid option-field compatibility
4. command-level `MOD,-p` output
5. `SCH,-o` duplicate suppression

### P1: P0 직후 작성
1. `birthday,-m/-d` integration
2. `DEL` + secondary option integration
3. formatter boundary regression
4. `MOD` + `-a/-o`
5. duplicate `employeeNum` add policy

### P2: 회귀 및 비기능
1. disabled approval 테스트 재활성화/대체
2. 100,000건 acceptance 테스트

## 7. 테스트 파일 계획
| 우선순위 | 파일 | 목적 |
| --- | --- | --- |
| P0 | `src/test/java/com/sec/bestreviewer/EmployeeManagementTest.java` | malformed command output, disabled E2E replacement anchor |
| P0 | `src/test/java/com/sec/bestreviewer/store/EmployeeStoreImplPhoneNumberTest.java` | phone partial-field integration |
| P0 | `src/test/java/com/sec/bestreviewer/OptionValidationTest.java` | invalid option position / field-option compatibility |
| P0 | `src/test/java/com/sec/bestreviewer/CommandModOutputTest.java` | command-level `MOD,-p`, `MOD,NONE`, row-shape assertion |
| P0/P1 | `src/test/java/com/sec/bestreviewer/AndOrCommandIntegrationTest.java` | `-a/-o` integration, OR duplicate suppression |
| P1 | `src/test/java/com/sec/bestreviewer/ResultStringFormatterTest.java` | max-5, count/`NONE`, join-year ordering boundary |
| P2 | `src/test/java/com/sec/bestreviewer/EmployeeManagementScaleTest.java` | 100,000건 acceptance smoke test |

## 8. 브랜치 분담 계획
### `A_01_RED`
- malformed command handling
- parser validation
- invalid option-field compatibility
- `phoneNum,-m/-l`
- `birthday,-m/-d`

### `A_02_RED`
- `ADD` weak spot
- duplicate `employeeNum`
- `DEL` option integration
- `SCH` base regression gap

### `A_03_RED`
- `ResultStringFormatter`
- `NONE`
- max-5
- join-year sorting boundary
- approval-style coverage 대체
- scale fixture 준비

### `A_04_RED`
- `MOD`
- `certi`
- comparison search
- `AND/OR`
- OR duplicate suppression

## 9. 실행 순서
1. `EmployeeManagementTest`에 malformed command output 실패 테스트 추가
2. `EmployeeStoreImplPhoneNumberTest` 작성
3. `OptionValidationTest` 작성
4. `CommandModOutputTest` 작성
5. `AndOrCommandIntegrationTest` 작성 또는 확장
6. `ResultStringFormatterTest` 작성
7. approval-style / scale 테스트 정리

## 10. 산출물 계획
### 필수 산출물
- failing test code
- 테스트 데이터 파일
- 테스트 우선순위 문서
- 브랜치별 작업 계획

### 문서 산출물
- 본 문서 `DOCS/09_red_test_plan_KR.md`
- 필요 시 테스트 실행 결과 요약 보고서 `report/`
- 필요 시 RED 테스트 작성 대화 기록 `prompting/`

## 11. 완료 기준
- P0 실패 테스트가 모두 작성되어 있다.
- 테스트 이름이 의도를 드러낸다.
- 요구사항과 소스 기준 계약이 충돌하지 않는다.
- `A_01_RED ~ A_04_RED` 작업 경계가 명확하다.
- `GREEN` 단계에서 어떤 구현을 먼저 고쳐야 하는지 테스트만 보고 알 수 있다.

## 12. 리스크
- 현재 테스트 수만 보고 충분하다고 판단하면 command-level 공백을 놓칠 수 있다.
- `MOD`와 `AND/OR`는 store-level 테스트 때문에 실제보다 커버가 좋아 보일 수 있다.
- disabled approval 테스트를 그대로 두면 회귀 신뢰도가 낮다.
- 100,000건 요구사항은 문서상 중요도에 비해 자동화 검증이 아직 없다.
