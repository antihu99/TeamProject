# RED 테스트 전략

## 1. 문서 개요
- 문서명: `RED 테스트 전략`
- 관련 단계: `RED`
- 상위 문서:
  - `requirement/Base.md`
  - `requirement/Further.md`
  - `DOCS/00_PRD_KR.md`
  - `DOCS/02_requirements_traceability_KR.md`
  - `DOCS/03_gherkin_KR.md`
  - `DOCS/04_todo_KR.md`
  - `DOCS/07_red_test_inventory_KR.md`
- 소스 기준 참조:
  - `src/main/java/com/sec/bestreviewer`
  - `src/test/java/com/sec/bestreviewer`

## 2. 목적
이 문서는 실제 현재 소스 트리를 기준으로 RED 단계의 테스트 상태를 기록한다.

핵심 목적은 팀이 다음 사항에 대해 같은 이해를 갖도록 만드는 것이다.
- 이미 커버된 것은 무엇인가
- 아직 비어 있는 것은 무엇인가
- 어떤 실패 테스트를 먼저 써야 하는가
- `A_01_RED ~ A_04_RED`로 어떻게 나눌 것인가

## 3. 현재 테스트 상태
### 상대적으로 강한 영역
- 전체적으로 `SCH`가 가장 강한 기능 영역이다.
  - `CommandExecutorTest`
  - `EmployeeStoreImplNameTest`
  - `EmployeeStoreImplBirthdayTest`
  - `EmployeeStoreImplCareerLevelTest`
- 다음 value object에 대한 파싱/비교 테스트가 존재한다.
  - `EmployeeNumber`
  - `Birthday`
  - `CareerLevel`
  - `Certi`
  - `Name`
  - `PhoneNumber`
- `DEL` / `SCH` 출력 계약은 이미 다음을 상당 부분 커버한다.
  - `NONE`
  - 최대 5건 출력
  - 정렬된 출력
  - count 출력

### 부분적으로만 커버된 영역
- `ADD`
  - `CommandExecutorTest`와 store 테스트는 있지만, 실제 `AddCommand.execute()`와 duplicate `employeeNum` 정책은 강하게 잠겨 있지 않다.
- `MOD`
  - `EmployeeStoreImplModifyCommandTest`로 store-level pre-change 동작은 존재한다.
  - command-level `MOD,-p` 출력 계약은 여전히 약하다.
- `AND/OR`
  - 파싱과 일부 store 동작은 존재한다.
  - command-level `OR` 동작에 대한 명시적 assertion은 약하다.
- `certi`
  - field-level ordering은 존재한다.
  - command-level search/modify 통합은 약하다.

### 약하거나 비어 있는 영역
- `EmployeeManagement`의 malformed command 출력 동작
- `phoneNum,-m/-l`의 parser -> command -> store 통합 테스트
- invalid option-field compatibility 테스트
- `ResultStringFormatter` 직접 회귀 테스트
- 활성화된 end-to-end approval 테스트
- 100,000건 검증

## 4. 누락 테스트 목록
### P0
- malformed command line이 `wrong command : <line>`으로 출력되는지
- `phoneNum,-m/-l`가 실제 명령 경로 전체에서 동작하는지
- invalid option-field 조합이 명시적으로 거절되거나 고정되는지
- `MOD,-p`가 `certi`를 포함한 변경 전 row shape를 보장하는지
- `SCH,-o`의 duplicate suppression이 회귀 없이 유지되는지

### P1
- `birthday,-m/-d` 통합 동작
- `DEL` + secondary option의 parser -> command -> store 경로
- `90xxxxxx` vs `00xxxxxx` 정렬 경계 회귀
- `MOD` + `-a/-o`의 command-level 통합
- duplicate `employeeNum` add 정책

### P2
- disabled approval 테스트 재활성화 또는 대체
- 100,000건 acceptance 테스트

## 5. 실패 테스트 전략
### 1. 외부 CLI 동작부터 먼저 잠근다
사용자가 실제로 보는 외부 계약을 먼저 테스트로 고정한다.

우선 대상:
- malformed command output
- invalid option/field compatibility
- `NONE`
- 최대 5건
- 결정적 정렬

### 2. parser -> command -> store 실경로 통합 테스트를 추가한다
field-level 테스트나 mocked 테스트만으로 만족하지 않는다.

우선 타깃:
- `phoneNum,-m/-l`
- `birthday,-m/-d`
- `DEL` option flow
- `MOD` option flow
- `AND/OR` overlap behavior

### 3. command-level `MOD`를 잠근다
store-level modify 동작이 있어도 RED에서는 아래를 별도로 고정해야 한다.
- `MOD,-p` 변경 전 출력
- `MOD,NONE`
- `certi`를 포함한 printed row shape

### 4. 회귀 및 비기능 리스크를 마무리한다
계약/통합 실패 테스트 작성 후에는 아래를 이어서 수행한다.
- disabled approval 테스트 복원 또는 대체
- scale 검증 추가

## 6. 브랜치 분담
### `A_01_RED`
- malformed command 처리
- parser validation
- invalid option-field 조합
- `phoneNum,-m/-l`
- `birthday,-m/-d`

### `A_02_RED`
- `ADD` 취약 구간
- duplicate `employeeNum` add 정책
- `DEL` option integration
- `SCH` base regression gap

### `A_03_RED`
- `ResultStringFormatter`
- `NONE`
- 최대 5건
- join-year sorting boundary
- disabled approval replacement
- scale fixture preparation

### `A_04_RED`
- `MOD`
- `certi`
- comparison search
- `AND/OR`
- OR duplicate suppression

## 7. 우선 테스트 파일
| 파일 | 먼저 필요한 이유 |
| --- | --- |
| `src/test/java/com/sec/bestreviewer/EmployeeManagementTest.java` | malformed command output과 disabled E2E 커버리지 |
| `src/test/java/com/sec/bestreviewer/store/EmployeeStoreImplPhoneNumberTest.java` | phone partial-field integration이 현재 가장 큰 공백 중 하나 |
| `src/test/java/com/sec/bestreviewer/OptionValidationTest.java` | invalid option-position과 field-option compatibility를 명시적으로 고정해야 함 |
| `src/test/java/com/sec/bestreviewer/CommandModOutputTest.java` | command-level `MOD,-p` 계약이 아직 약함 |
| `src/test/java/com/sec/bestreviewer/ResultStringFormatterTest.java` | 직접적인 formatting/ordering 회귀 검증이 없음 |
| `src/test/java/com/sec/bestreviewer/AndOrCommandIntegrationTest.java` | `-a/-o` 통합과 OR duplicate suppression 고정 |
| `src/test/java/com/sec/bestreviewer/EmployeeManagementScaleTest.java` | 100,000건 요구사항에 대한 활성 검증이 없음 |

## 8. 커버리지 리스크
- 현재 테스트 개수만 보면 신뢰도가 높아 보일 수 있지만, 일부는 mocked test 또는 field-level test라 실제 통합 신뢰도는 과대평가될 수 있다.
- `MOD`와 `AND/OR`는 store-level 커버리지가 command-level보다 좋아서 실제보다 더 성숙해 보일 수 있다.
- disabled approval 테스트는 실제 회귀 감지 신뢰도를 낮춘다.
- 100,000건 요구사항은 문서에는 있으나 활성 자동화 테스트로 잠겨 있지 않다.

## 9. RED 종료 기준
- 모든 P0 항목이 실패 테스트 또는 명시적 추적 결정으로 존재한다.
- 실패 테스트가 `requirement/Base.md`, `requirement/Further.md`, 현재 source-guided 계약 문서와 충돌하지 않는다.
- `A_01_RED ~ A_04_RED`의 소유권이 명확하다.
- `GREEN` 단계가 기억이 아니라 테스트 정의 계약 기준으로 시작될 수 있다.
