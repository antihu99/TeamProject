# SPEC 공백 로그

## 1. 문서 개요
- 문서명: `SPEC 공백 로그`
- 관련 단계: `SPEC`
- 상위 문서:
  - `requirement/Base.md`
  - `requirement/Further.md`
  - `DOCS/00_PRD_KR.md`
  - `DOCS/02_requirements_traceability_KR.md`
- 하위 관련 단계:
  - `RED`
- 소스 기준 참조:
  - `src/main/java/com/sec/bestreviewer/EmployeeManagement.java`
  - `src/main/java/com/sec/bestreviewer/CommandFactory.java`
  - `src/main/java/com/sec/bestreviewer/util/ResultStringFormatter.java`

## 2. 목적
이 문서는 `RED` 시작 전에 정리되어야 하는 요구사항 해석 공백, 계약 충돌, 아키텍처 민감 의사결정을 기록한다.

핵심 목적은 “아직 열려 있는 계약 공백”과 “현재 소스가 이미 결정해버린 사항”을 분리하는 것이다.

## 3. 공백 로그
| Gap ID | 상태 | 영역 | 현재 소스 기준 상태 | RED에서 중요한 이유 |
| --- | --- | --- | --- | --- |
| G-01 | 코드 기준 해결, 문서 기준 낡음 | `certi` 입력 계약 | `CommandFactory`는 이미 `ADD`를 `employeeNum,name,cl,phoneNum,birthday,certi` 형태로 조립한다. | RED는 현재 구현된 `ADD` 형태를 기준으로 테스트해야 하며, 문서도 더 이상 미정처럼 다루면 안 된다. |
| G-02 | 부분 해결 | invalid command 동작 | `EmployeeManagement`는 `IllegalArgumentException` 경로에 대해 `wrong command : <line>`을 출력한다. 다만 그 밖의 malformed case는 아직 완전히 정규화되지 않았다. | RED는 CLI가 이미 정의한 invalid-command 출력은 바로 assertion할 수 있다. |
| G-03 | 미해결 | invalid option-field 조합 | 요구사항에는 유효 조합이 정의돼 있지만, invalid 조합에 대한 전용 테스트와 명시 문서가 부족하다. | RED는 fallback을 추정하지 말고 negative test로 고정해야 한다. |
| G-04 | 현재 CLI 경로 기준 해결 | `MOD employeeNum` 거절 계약 | 현재 `MOD employeeNum` 시도는 invalid command 흐름으로 처리된다. | RED는 현재 외부 동작을 문서화하고 테스트해야 한다. |
| G-05 | 미해결 | `employeeNum` 정렬 경계 | join-year ordering은 코드와 일부 테스트에 존재하지만, 1990년대 vs 2000년대 경계에 대한 direct formatter regression이 없다. | RED는 세기 경계 정렬을 안정적으로 고정해야 한다. |
| G-06 | 구현 기준 해결 | `OR` duplicate 처리 범위 | 현재 matching behavior는 deduplicated match set을 사용하므로 중복 row가 다시 출력/처리되지 않는다. | RED는 이 동작이 깨지지 않도록 regression test를 추가해야 한다. |
| G-07 | 소스 기준 해결, 문서화는 얕음 | enum 비교 순서 | `cl`과 `certi`의 ordering은 field 비교 로직과 field test에 이미 구현돼 있다. | 문서는 더 이상 이를 미정처럼 다루지 말아야 한다. |
| G-08 | 미해결 | 100,000건 acceptance 정의 | 대량 처리 지원은 요구사항이지만, 이를 검증하는 활성 테스트가 없다. | RED와 QA는 측정 가능한 large-volume acceptance 시나리오를 정의해야 한다. |

## 4. 권장 해결 순서
1. `G-02`, `G-03`의 negative-contract behavior를 먼저 잠근다.
2. `G-05`, `G-06`에 대한 regression test를 추가해 현재 구현 동작을 보존한다.
3. `G-08`의 scale acceptance 전략을 `QA` 전에 확정한다.

## 5. RED 가드레일 제안
- failing test는 현재 소스의 `ADD,...,birthday,certi` 형태를 기준으로 작성한다.
- malformed-input 테스트는 팀이 계약을 넓히지 않는 한, 현재 구현된 `IllegalArgumentException` 출력 경로 중심으로 둔다.
- sorting, `NONE`, max-5, pre-change `MOD` 출력은 command/output-level contract test로 우선 고정한다.
- 미뤄진 공백은 RED PR 설명에 남겨서 소유권이 사라지지 않게 한다.

## 6. 종료 기준
- 아직 열려 있는 공백마다 명시적 팀 결정 또는 임시 합의가 있다.
- 팀이 숨은 동작을 추정하지 않고도 `RED` 테스트를 작성할 수 있다.
- Base/Further 요구사항이 하나의 외부 계약으로 매핑된다.
