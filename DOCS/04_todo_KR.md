# 전달 TODO 계획

## 1. 문서 개요
- 문서명: `전달 TODO 계획`
- 관련 제품: `Employee Management CLI`
- 상위 문서:
  - `README.md`
  - `DOCS/00_PRD_KR.md`
  - `DOCS/01_epic_KR.md`
  - `DOCS/02_requirements_traceability_KR.md`
  - `DOCS/03_gherkin_KR.md`
- 소스 기준 참조:
  - `src/main/java/com/sec/bestreviewer`
  - `src/test/java/com/sec/bestreviewer`

## 2. 목적
이 문서는 프로젝트 TODO를 실제 실행 가능한 계획으로 재구성한다.

정렬 기준:
- `README.md`의 프로젝트 일정
- `README.md`의 품질 및 평가 기준
- 저장소의 브랜치 전략
- 이미 작성된 PRD, Epic, Traceability, Gherkin 문서 흐름

## 3. README 기반 작업 원칙
- 팀 코드 리뷰는 필수이다.
- TDD 실천은 필수이다.
- 테스트 커버리지는 90% 이상을 목표로 한다.
- 커밋은 작게 유지하는 것을 권장한다.
- 리뷰는 Clean Code, Refactoring, TDD, Secure Coding 관점을 포함해야 한다.
- 최종 제출은 release-oriented 브랜치 흐름으로 준비한다.

### 현재 소스 기준 참고 사항
- 현재 소스에는 이미 `MOD`, `certi`, name option, birthday option, comparison search, `AND/OR` support가 포함되어 있다.
- 현재 테스트 트리에도 field parsing/comparison, name option, birthday comparison, modify store behavior, command output 기본 계약 테스트가 존재한다.
- 현재 RED에서 실제로 남아 있는 핵심 공백은 malformed command output, phone partial-field integration, invalid option-field compatibility, direct formatter regression, disabled end-to-end approval coverage, 100,000건 검증이다.

## 4. 일정 기준 전달 계획
### Day 1
- 저장소와 팀 작업 규칙 정리
- base code와 requirement 문서 분석
- `SPEC` 개인 브랜치 분배
- PRD, Epic, Traceability, Gherkin 정리

### Day 2 to Day 4
- `RED`에서 failing test 작성
- `GREEN`에서 base requirement 구현
- `REFACTORING`에서 구조 개선
- `NEW_FEATURE`에서 추가 요구사항 정렬

### Day 5
- `QA`에서 품질 검증
- 최종 보고서와 발표 자료 준비
- 학습 포인트, 리뷰 결과, AI 활용 회고 정리

## 5. 브랜치 기반 TODO 계획
## Stage 1. SPEC
### Goal
기능 개발 시작 전 요구사항 해석을 고정하고 안전하게 작업을 분할한다.

### Todo
- [ ] `Base.md`, `Further.md` 기반 PRD 확정
- [ ] Epic 문서 확정
- [ ] Requirements Traceability Matrix 확정
- [ ] Gherkin 시나리오 확정
- [ ] 필드 정의, 옵션 위치, 출력 규칙 확인
- [ ] `A_01_spec ~ A_04_spec` 담당자 확정

### Exit Criteria
- 팀이 하나의 요구사항 해석을 공유한다.
- RED가 문서 기준으로 바로 시작될 수 있다.

## Stage 2. RED
### Goal
요구사항을 failing test와 coverage target으로 전환한다.

### Todo
- [ ] `EmployeeManagement`의 malformed command output assertion 추가
- [ ] `phoneNum,-m/-l` parser -> command -> store 통합 테스트 추가
- [ ] invalid option-field compatibility 테스트 추가
- [ ] `NONE`, max-5, join-year ordering boundary를 직접 formatter 테스트로 고정
- [ ] disabled approval-style 테스트 재활성화 또는 대체
- [ ] command-level `MOD` 출력 assertion 보강
- [ ] explicit `OR` duplicate regression 추가
- [ ] 100,000건 validation 전략 정의 및 테스트 추가
- [ ] 커버리지를 90%+로 끌어올릴 기준 정리

### 현재 저장소에 이미 존재하는 주요 테스트 자산
- `EmployeeManagementTest`
- `CommandParserTest`
- `CommandExecutorTest`
- `CommandModTest`
- `AndOrParameterTest`
- `store/EmployeeStoreImplNameTest`
- `store/EmployeeStoreImplBirthdayTest`
- `store/EmployeeStoreImplCareerLevelTest`
- `store/EmployeeStoreImplModifyCommandTest`
- `field/EmployeeNumberTest`
- `field/CertiTest`
- `field/BirthdayTest`
- `field/PhoneNumberTest`

### README Mapping
이 단계는 다음과 직접 연결된다.
- TDD practice
- test code appropriateness
- code coverage 90%+

## Stage 3. GREEN
### Goal
기본 요구사항 테스트를 최소 구현으로 통과시킨다.

### Todo
- [ ] `ADD` 동작 보완 또는 수정
- [ ] `DEL` 동작 보완 또는 수정
- [ ] `SCH` 동작 보완 또는 수정
- [ ] `-p` 출력 동작 회귀 보완
- [ ] `-p` 미사용 시 count output 보장
- [ ] max 5 printed records 보장
- [ ] no result 시 `NONE` 보장
- [ ] input/output file 실행 경로 검증

### Detailed `-p` Todo
- [ ] `-p` 적용 시 matching records 출력
- [ ] `-p` 미적용 시 count만 출력
- [ ] 출력 건수 5건 제한
- [ ] no match 시 `COMMAND,NONE` 출력

### Exit Criteria
- Base requirement acceptance 시나리오가 통과한다.
- core command behavior가 refactoring 가능한 수준으로 안정화된다.

## Stage 4. REFACTORING
### Goal
외부 동작을 바꾸지 않고 가독성과 유지보수성을 개선한다.

### Todo
- [ ] magic number를 이름 있는 상수로 추출
- [ ] 모호한 변수/메서드명을 도메인 용어로 변경
- [ ] 긴 메서드 분리
- [ ] 중복 로직 제거
- [ ] 주요 클래스의 SRP 개선 검토
- [ ] 모든 refactoring 동안 GREEN 테스트 유지

### README Mapping
이 단계는 다음과 직접 연결된다.
- production code readability and maintainability
- review quality in Clean Code and Refactoring terms

## Stage 5. NEW_FEATURE
### Goal
`Further.md` 기반 확장 요구사항을 현재 소스와 일치하도록 정합화하고 남은 구현 gap을 닫는다.

### Todo
- [ ] 현재 `certi` 구현이 requirement/output contract와 일치하는지 검증
- [ ] `MOD` 명령이 RED assertion과 일치하도록 보완
- [ ] phone option `-m`, `-l`의 남은 gap 보완
- [ ] birthday month/day option의 남은 gap 보완
- [ ] comparison search behavior의 잔여 gap 보완
- [ ] `and/or` behavior와 deduplication gap 보완
- [ ] 각 보완과 함께 테스트도 같이 갱신

### Recommended Order
1. requirement-vs-source gap 확인
2. `certi`와 printed row shape 검증
3. `MOD`와 command-level output 정렬
4. phone/birthday option gap 정리
5. comparison / `and/or` gap 정리

## Stage 6. QA
### Goal
최종 병합 전 요구사항, 품질, 리뷰 기대치를 기준으로 제품을 검증한다.

### Todo
- [ ] 모든 base requirement 만족 여부 확인
- [ ] 모든 extended requirement 만족 여부 확인
- [ ] output formatting 회귀 테스트 재실행
- [ ] coverage 목표 재확인
- [ ] 코드 복잡도 및 클래스/메서드 크기 검토
- [ ] 커밋 크기와 테스트 동반 여부 검토
- [ ] 최종 품질 보고서 준비
- [ ] 발표 자료 및 회고 정리

### README Mapping
이 단계는 다음과 직접 연결된다.
- requirement evaluation
- software quality evaluation
- commit and review evaluation
- communication and final presentation readiness

## 6. README Todo Mapping
다음 표는 원래 `README.md`의 TODO를 현재 소스 트리 기준으로 다시 해석한 것이다.

| README Todo Item | Planned Stage |
| --- | --- |
| certi column 추가에 따른 전체 TC 검토 및 보완 | `RED` |
| certi column 구현 정합성 검증 및 잔여 gap 보완 | `NEW_FEATURE` |
| -p 출력 옵션 변경에 대한 TC 검토 및 보완 | `RED` |
| -p 출력 구현 회귀 보완 | `GREEN` |
| MOD 기능 TC 구현 | `RED` |
| MOD 기능 명령 레벨 gap 보완 | `NEW_FEATURE` |
| 2옵션 이름, 성(-f, -l) TC 구현 | `RED` |
| 2옵션 이름, 성(-f, -l) 구현 정합성 검증 | `NEW_FEATURE` |
| 2옵션 전화번호(-m, -l) TC 구현 | `RED` |
| 2옵션 전화번호(-m, -l) gap 보완 | `NEW_FEATURE` |
| 2옵션 생년월일(-y, -m, -d) TC 구현 | `RED` |
| 2옵션 생년월일(-y, -m, -d) gap 보완 | `NEW_FEATURE` |
| 3옵션 부등호(SCH만 -g, -ge, -s, -se) TC 구현 | `RED` |
| 3옵션 부등호 구현 회귀 보완 | `NEW_FEATURE` |
| and, or 연산 옵션 (2옵션, 3옵션) TC 구현 | `RED` |
| and, or 연산 옵션 구현 회귀 보완 | `NEW_FEATURE` |
| refactoring | `REFACTORING` |

## 7. 팀 분담 제안
- `A_01_*`: command parsing, data model, negative contract test
- `A_02_*`: core command behavior
- `A_03_*`: output rules, formatter, scale and approval coverage
- `A_04_*`: advanced options, `MOD`, `AND/OR`, integration validation

## 8. 최종 준비 체크리스트
- [ ] PRD, Epic, Traceability, Gherkin, Todo 문서가 서로 정렬되어 있는가
- [ ] Base feature가 requirement check를 통과하는가
- [ ] Extended feature가 requirement check를 통과하는가
- [ ] Coverage가 목표치 이상인가
- [ ] 리뷰 의견이 반영되었는가
- [ ] 최종 보고서와 발표 자료가 준비되었는가
