# SPEC Analysis Report

## 1. 작업 개요
본 작업에서는 `SPEC` 단계의 목적에 맞춰 코드베이스 구조, 요구사항 문서, 기존 테스트를 함께 검토하고, 이후 `RED` 단계에서 바로 실패 테스트를 작성할 수 있도록 분석 결과를 정리했다.

이번 작업은 production code 수정 없이 분석 문서와 보고서 산출물 중심으로 진행했다.

## 2. 수행 결과 요약
### 코드베이스 구조 분석
- 실행 흐름을 `EmployeeManagement -> CommandParser -> CommandFactory -> CommandExecutor -> EmployeeStoreImpl -> ResultStringFormatter` 기준으로 정리
- 패키지 역할을 orchestration, command, field/domain, store, util 관점으로 분류
- 주요 아키텍처 리스크를 파서 검증, 옵션 결합도, 저장소 구조, 정렬 규칙 위치 측면에서 식별

### 요구사항 재정리
- `Base.md`, `Further.md`, `DOCS/00_PRD.md`, `DOCS/02_requirements_traceability.md`, `DOCS/03_gherkin.md`를 기준으로 `ADD`, `DEL`, `SCH`, `MOD` 계약을 재확인
- `-p`, `-f`, `-l`, `-m`, `-y`, `-d`, `-g`, `-ge`, `-s`, `-se`, `-a`, `-o`의 위치와 의미를 재정리
- `NONE`, 최대 5건, 입사년도 우선 정렬, `MOD` 변경 전 출력 규칙을 RED 전용 테스트 관점으로 해석

### 테스트 관점 분석
- 기존 테스트 중 `DEL` / `SCH` 출력 규칙과 일부 비교 검색 커버리지를 확인
- `MOD` command-level 출력 계약, `-o` 중복 제거, invalid option 조합, malformed command 입력은 취약 구간으로 분류
- `RED` 우선순위를 `P0 / P1 / P2`로 나누어 실패 테스트 후보를 정리

## 3. 핵심 판단
### 가장 큰 요구사항 리스크
- `certi`는 최종 데이터 모델과 출력 예시에 포함되지만, 원문 `ADD` 형식은 여전히 `certi` 입력을 정의하지 않는다.
- 이 불일치는 RED 테스트가 5개 필드 입력을 기준으로 써야 하는지, 6개 필드 입력을 기준으로 써야 하는지 결정하지 못하게 만든다.

### 가장 큰 구현 리스크
- `CommandParser`의 최소 토큰 검증이 비활성화되어 있고, malformed input의 외부 계약이 문서상 명확하지 않다.
- `CommandFactory`가 옵션별 부분 검색을 위해 placeholder 값을 조립하는 구조라서 확장과 검증에 취약하다.
- `EmployeeStoreImpl`는 `List` 기반 전체 탐색 구조이므로 10만 건 요구에 대한 성능 여유는 아직 문서로만 존재한다.

### RED 착수 전 확정이 필요한 항목
- `certi` 입력 계약
- invalid command 처리 규칙
- invalid option-field 조합 처리 규칙
- `MOD employeeNum` 수정 금지 시 외부 출력 계약
- `employeeNum` 정렬 경계와 비교 의미

## 4. 이번 작업의 신규 산출물
### DOCS
- `DOCS/06_spec_gap_log.md`
  - RED 전에 확정해야 할 요구사항 공백과 계약 충돌 목록
- `DOCS/07_red_test_inventory.md`
  - 우선순위와 브랜치 소유권까지 포함한 RED 테스트 인벤토리

### 보고서
- `report/01_spec_analysis_report.md`

### 프롬프트/대화 기록
- `prompting/01_spec_analysis_conversation.md`

### 분석 캔버스
- `C:\Users\user\.cursor\projects\d-Vs-workplace-Java-project-TeamProject-01\canvases\spec-review.canvas.tsx`

## 5. 산출물별 활용 방식
- `DOCS/06_spec_gap_log.md`
  - 팀이 해석 충돌을 먼저 해소하고 RED 테스트가 잘못된 계약을 잠그지 않도록 하는 용도
- `DOCS/07_red_test_inventory.md`
  - `A_01_RED ~ A_04_RED` 분업과 테스트 우선순위 확정 용도
- `report/01_spec_analysis_report.md`
  - SPEC 단계 결과 요약 및 후속 단계 인계 문서
- `prompting/01_spec_analysis_conversation.md`
  - 이번 SPEC 분석 작업 대화 흐름 보관

## 6. 후속 작업 제안
1. `DOCS/06_spec_gap_log.md`의 `G-01 ~ G-05`를 우선 합의한다.
2. `DOCS/07_red_test_inventory.md` 기준으로 `A_01_RED ~ A_04_RED` 담당 범위를 확정한다.
3. P0 테스트부터 실패 테스트를 작성해 `GREEN`에서 고칠 계약을 명확하게 드러낸다.

## 7. 결론
이번 SPEC 분석을 통해 현재 프로젝트는 문서 구조와 기능 범주는 비교적 정리되어 있으나, `certi`, invalid input, option compatibility, 정렬/비교 경계 같은 계약성 규칙은 아직 팀 합의가 더 필요하다는 점이 확인되었다.

따라서 다음 단계의 핵심은 구현이 아니라, RED 테스트가 잘못된 해석을 고정하지 않도록 남은 공백을 먼저 잠그는 것이다.
