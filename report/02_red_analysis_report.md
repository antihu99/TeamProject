# RED Analysis Report

## 1. 작업 개요
본 작업에서는 `RED` 단계의 목적에 맞춰 현재 `src/main/java`와 `src/test/java`를 실제 기준으로 다시 분석하고, 요구사항 문서와 구현 상태를 비교하여 테스트 공백과 우선순위를 정리했다.

이번 작업은 production code 수정 없이 `DOCS`, 분석 캔버스, 결과 보고서 중심으로 진행했다.

## 2. 수행 결과 요약
### 현재 테스트 구조 재분석
- `src/test/java`의 실제 테스트 파일과 테스트 개수를 기준으로 현재 커버리지 강약을 재정리
- `SCH`가 상대적으로 강하고, `ADD`, command-level `MOD`, malformed command, phone partial-field, scale 검증은 약한 영역으로 분류
- disabled approval 테스트 2건이 남아 있어 회귀 감지 신뢰도가 낮다는 점을 확인

### 소스 기준 RED 관점 정리
- `EmployeeManagement`의 invalid command 출력 형식 `wrong command : <line>`을 RED에서 먼저 잠가야 할 외부 계약으로 식별
- `CommandParser`, `OptionParser`, `SecondaryOptionEnum`, `DeleteCommand`, `ModCommand`, `ResultStringFormatter`를 기준으로 실패 테스트 대상 경로를 재확인
- `requirement/Base.md`, `requirement/Further.md`와 현재 구현 사이의 정합성 관점에서 테스트 우선순위를 재배치

### 신규 문서 및 분석 산출물 작성
- `DOCS/08_red_test_strategy.md` 작성
  - 현재 테스트 상태
  - 누락 테스트 목록
  - 기능별 실패 테스트 전략
  - `A_01_RED ~ A_04_RED` 분담안
  - 우선순위 높은 테스트 파일 제안
  - 커버리지 리스크
- `DOCS/05_README.md` 업데이트
  - `08_red_test_strategy.md`를 문서 허브와 RED 읽기 순서에 반영
- RED 분석 캔버스 `canvases/red-test-review.canvas.tsx` 생성

## 3. 핵심 판단
### 현재 테스트 강점
- `CommandExecutorTest`가 `DEL` / `SCH`의 `NONE`, max-5, count, 정렬 등 기본 출력 계약을 상당 부분 잡고 있다.
- `EmployeeStoreImplNameTest`, `EmployeeStoreImplBirthdayTest`, `EmployeeStoreImplCareerLevelTest`로 이름/생일/경력 비교 규칙이 비교적 잘 잠겨 있다.
- `EmployeeStoreImplModifyCommandTest`로 store-level `MOD` pre-change 동작이 확인된다.

### 현재 테스트 약점
- malformed command가 실제로 어떻게 출력되는지에 대한 활성 테스트가 없다.
- `phoneNum,-m/-l` 실경로 통합 테스트가 없다.
- invalid option-field 조합을 명시적으로 거절하는 테스트가 없다.
- command-level `MOD,-p` 출력 계약이 약하다.
- `SCH,-o` 중복 제거에 대한 명시적 회귀 테스트가 약하다.
- 100,000건 요구사항을 잠그는 테스트가 없다.

### RED에서 먼저 실패시켜야 할 항목
1. malformed command output
2. `phoneNum,-m/-l` parser -> command -> store integration
3. invalid option-field compatibility
4. command-level `MOD,-p` output
5. `SCH,-o` duplicate suppression

## 4. 이번 작업의 신규 산출물
### DOCS
- `DOCS/08_red_test_strategy.md`

### 보고서
- `report/02_red_analysis_report.md`

### 프롬프트/대화 기록
- `prompting/02_red_analysis_conversation.md`

### 분석 캔버스
- `C:\Users\user\.cursor\projects\d-Vs-workplace-Java-project-TeamProject-01\canvases\red-test-review.canvas.tsx`

## 5. 산출물별 활용 방식
- `DOCS/08_red_test_strategy.md`
  - RED 단계 테스트 작성 순서와 분업 기준 문서
- `report/02_red_analysis_report.md`
  - 이번 RED 분석 작업 결과 보고용 문서
- `prompting/02_red_analysis_conversation.md`
  - RED 단계 분석 및 문서화 대화 흐름 보관
- `canvases/red-test-review.canvas.tsx`
  - 현재 테스트 상태와 P0/P1/P2 공백을 시각적으로 검토하는 분석 캔버스

## 6. 후속 작업 제안
1. `EmployeeManagementTest`에 malformed command 출력 테스트를 추가한다.
2. `EmployeeStoreImplPhoneNumberTest`와 `OptionValidationTest`를 우선 작성한다.
3. `CommandModOutputTest`, `ResultStringFormatterTest`, `AndOrCommandIntegrationTest` 순으로 P0/P1 테스트를 확장한다.

## 7. 결론
이번 RED 분석을 통해 현재 프로젝트는 테스트 수 자체는 적지 않지만, 실제로 구현 계약을 잠그는 command-level 통합 테스트와 비기능 검증은 아직 부족하다는 점이 확인되었다.

따라서 RED 단계의 핵심은 테스트 개수를 늘리는 것이 아니라, 외부 계약과 취약 경계를 먼저 실패 테스트로 고정하는 것이다.
