# SPEC Analysis Conversation

## 개요
본 문서는 `SPEC` 단계에서 수행한 코드베이스 분석, 요구사항 재정리, RED 테스트 대상 도출, 후속 산출물 저장 작업에 대한 주요 대화 흐름을 정리한 기록이다.

## 대화 기록
### 1. SPEC 프롬프트 확인
**User**
- `PCTF_PROMPT/01_SPEC_PCTF_prompt.md`의 완성형 프롬프트 구간을 가리키며 해당 SPEC 작업을 수행해달라고 요청

**Agent**
- SPEC 프롬프트의 목표를 기준으로 코드베이스 구조, 요구사항, 옵션 규칙, 출력 규칙, 테스트 대상, 4인 분업안을 분석 대상으로 설정

### 2. 코드베이스 및 문서 구조 분석
**User**
- 별도 추가 지시 없이 SPEC 분석 결과를 요청

**Agent**
- `src/main/java`, `requirement/`, `DOCS/`, `src/test/java`를 병렬 검토
- 코드 흐름을 `EmployeeManagement -> CommandParser -> CommandFactory -> CommandExecutor -> EmployeeStoreImpl -> ResultStringFormatter` 기준으로 정리
- `Base.md`, `Further.md`, `PRD`, `Traceability`, `Gherkin`, `Todo`, `README`를 교차 검토하여 기능/옵션/출력 규칙을 재확인

### 3. 핵심 리스크 및 RED 관점 정리
**User**
- 분석 결과를 이어서 확인

**Agent**
- 가장 큰 요구사항 공백으로 `certi` 입력 계약 불일치를 식별
- invalid command 처리 규칙, invalid option 조합 처리, `MOD employeeNum` 수정 금지의 외부 계약 미정 상태를 리스크로 정리
- RED 단계에서 우선 실패해야 하는 테스트를 `P0 / P1 / P2`로 구분

### 4. 분석 결과 시각화
**User**
- 별도 추가 요청 없음

**Agent**
- 채팅 옆에서 열어볼 수 있는 분석 캔버스 `canvases/spec-review.canvas.tsx` 생성
- 구조, 추적표, RED 우선순위, 분업안, 리스크를 한 화면에서 검토할 수 있도록 정리

### 5. 후속 문서 저장 요청
**User**
- `DOCS/06_spec_gap_log.md`, `DOCS/07_red_test_inventory.md` 초안 작성
- SPEC 단계의 결과 보고서와 PROMPT 저장 요청

**Agent**
- `report/`, `prompting/`의 기존 형식을 확인
- 문서 번호 체계를 맞춰 신규 산출물을 생성
- 필요한 경우 후속 RED 분업에 바로 연결될 수 있도록 각 문서의 역할을 분리

## 이번 작업에서 생성한 산출물
### DOCS
- `DOCS/06_spec_gap_log.md`
- `DOCS/07_red_test_inventory.md`

### REPORT
- `report/01_spec_analysis_report.md`

### PROMPTING
- `prompting/01_spec_analysis_conversation.md`

### CANVAS
- `C:\Users\user\.cursor\projects\d-Vs-workplace-Java-project-TeamProject-01\canvases\spec-review.canvas.tsx`

## 요약
이번 대화에서는 SPEC 단계의 목적에 맞춰 구현 전 요구사항과 코드 구조를 정리하고, RED 단계에서 바로 실패 테스트를 쓸 수 있도록 공백과 우선순위를 문서화했다.

핵심 결론은 현재 프로젝트가 문서와 기본 구조는 잘 갖춰져 있지만, `certi`, invalid input, 옵션 호환성, 정렬/비교 경계 계약은 RED 전에 추가 합의가 필요하다는 점이다.
