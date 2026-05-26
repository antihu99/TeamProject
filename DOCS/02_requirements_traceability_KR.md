# 요구사항 추적 매트릭스

## 1. 문서 개요
- 문서명: `요구사항 추적 매트릭스`
- 관련 제품: `Employee Management CLI`
- 상위 문서:
  - `requirement/Base.md`
  - `requirement/Further.md`
- 관련 하위 문서:
  - `DOCS/00_PRD_KR.md`
- 소스 기준 참조:
  - `src/main/java/com/sec/bestreviewer`
  - `src/test/java/com/sec/bestreviewer`

## 2. 목적
이 문서는 원문 요구사항을 PRD, 구현, 테스트 포인트와 연결해 설계, 구현, 리팩토링, QA 과정에서 어떤 요구도 빠지지 않도록 하기 위해 존재한다.

## 3. 추적 매트릭스
| Req ID | Source | Requirement Summary | PRD Mapping | Implementation / Test Focus |
| --- | --- | --- | --- | --- |
| B-01 | Base | 프로그램은 사원 데이터베이스 레코드를 관리한다 | PRD 2, 4, 18 | core product scope and data lifecycle |
| B-02 | Base | 레코드는 `employeeNum`, `name`, `cl`, `phoneNum`, `birthday`를 포함한다 | PRD 8 | data model validation |
| B-03 | Base | `employeeNum`은 입사년도 의미를 가진 8자리 숫자다 | PRD 8, 10, 11 | field validation, ordering, comparison rules |
| B-04 | Base | `name`은 대문자 영문이며 first/last 구조를 가진다 | PRD 8, 10 | field validation, partial name filtering |
| B-05 | Base | `cl`은 `CL1` ~ `CL4` 중 하나다 | PRD 8 | enum/value validation |
| B-06 | Base | `phoneNum`은 `010-xxxx-xxxx` 형식이다 | PRD 8, 10 | field validation, partial phone filtering |
| B-07 | Base | `birthday`는 `YYYYMMDD` 형식이다 | PRD 8, 10 | field validation, partial birthday filtering |
| B-08 | Base | command 형식은 고정되며 option-driven이다 | PRD 7, 13 | parser contract, command normalization |
| B-09 | Base | `ADD`는 새로운 사원을 추가한다 | PRD 9 FR-01, 12 | add flow, persistence behavior |
| B-10 | Base | `ADD`는 사원번호 외 중복을 허용하지만 필드는 non-null이다 | PRD 9 FR-01 | input validation, record creation |
| B-11 | Base | `DEL`은 조건 일치 레코드를 모두 삭제한다 | PRD 9 FR-02, 12 | bulk delete behavior |
| B-12 | Base | `SCH`는 조건 일치 레코드를 모두 검색한다 | PRD 9 FR-03, 12 | bulk search behavior |
| B-13 | Base | `-p`는 option1 위치에만 존재한다 | PRD 10 OR-01, 13 | parser enforcement |
| B-14 | Base | `-p`는 레코드를 줄 단위로 출력한다 | PRD 10 OR-01, 11 | printed output format |
| B-15 | Base | 출력은 더 이른 join year 기준으로 정렬된다 | PRD 10 OR-01, 11 | sorting logic, approval tests |
| B-16 | Base | 출력 건수는 최대 5건이다 | PRD 10 OR-01, 11 | max output constraint |
| B-17 | Base | `-p`가 없으면 영향 레코드 수를 출력한다 | PRD 11 | count output contract |
| B-18 | Base | 일치 레코드가 없으면 `COMMAND,NONE`을 출력한다 | PRD 11 | no-match behavior |
| B-19 | Base | `ADD`는 일반 출력 형식을 사용하지 않는다 | PRD 9 FR-01 | add output handling |
| B-20 | Base | 최소 100,000건을 처리할 수 있어야 한다 | PRD 4, 13, 16 | scalability and performance validation |
| B-21 | Base | 입력 txt를 읽고 출력 txt를 쓴다 | PRD 7, 16 | CLI execution contract |
| F-01 | Further | `MOD` 명령이 추가된다 | PRD 9 FR-04, 12 | update flow implementation |
| F-02 | Further | `MOD`는 조건 일치 레코드를 모두 수정한다 | PRD 9 FR-04, 12 | bulk modify behavior |
| F-03 | Further | `MOD`는 한 번에 한 필드만 변경 가능하다 | PRD 9 FR-04, 12 | parser and mutation constraint |
| F-04 | Further | `employeeNum`은 수정할 수 없다 | PRD 9 FR-04 | immutable field enforcement |
| F-05 | Further | `MOD -p`는 수정 전 레코드를 출력한다 | PRD 9 FR-04, 11, 16 | pre-change snapshot handling |
| F-06 | Further | `certi` 컬럼은 `ADV`, `PRO`, `EX`를 지원한다 | PRD 8, 9, 16 | extended data model and validation |
| F-07 | Further | `certi` 추가 이후 출력은 `certi`를 포함해야 한다 | PRD 11 | output schema evolution |
| F-08 | Further | `-f`는 first name 기준 필터다 | PRD 10 OR-02 | option parsing and condition evaluation |
| F-09 | Further | `-l`은 last name 기준 필터다 | PRD 10 OR-02 | option parsing and condition evaluation |
| F-10 | Further | `-m`은 phone middle digits 필터다 | PRD 10 OR-03 | partial phone search/update/delete |
| F-11 | Further | `-l`은 phone last digits 필터다 | PRD 10 OR-03 | partial phone search/update/delete |
| F-12 | Further | `-y`는 birthday year 필터다 | PRD 10 OR-04 | partial birthday handling |
| F-13 | Further | `-m`은 birthday month 필터다 | PRD 10 OR-04 | partial birthday handling |
| F-14 | Further | `-d`는 birthday day 필터다 | PRD 10 OR-04 | partial birthday handling |
| F-15 | Further | `SCH`는 `-g`, `-ge`, `-s`, `-se`를 지원한다 | PRD 10 OR-05 | comparison-based search |
| F-16 | Further | 비교 의미는 field ordering을 따른다 | PRD 10 OR-05, 15 | cross-field comparison consistency |
| F-17 | Further | phone prefix `010`은 비교에서 제외된다 | PRD 10 OR-05 | special comparison handling |
| F-18 | Further | `-a`는 AND 조건 조합이다 | PRD 10 OR-06 | dual-condition query evaluation |
| F-19 | Further | `-o`는 OR 조건 조합이다 | PRD 10 OR-06 | dual-condition query evaluation |
| F-20 | Further | OR 결과는 duplicate가 없어야 한다 | PRD 10 OR-06, 15 | deduplication behavior |
| F-21 | Further | `DEL`은 AND/OR 형식을 지원한다 | PRD 10 OR-06 | delete parser and condition composition |
| F-22 | Further | `MOD`는 AND/OR 형식을 지원한다 | PRD 10 OR-06 | modify parser and condition composition |
| F-23 | Further | `SCH`는 AND/OR 형식을 지원한다 | PRD 10 OR-06 | search parser and condition composition |

## 4. 현재 구현 및 커버리지 스냅샷
| Feature group | 주요 소스 클래스 | 현재 테스트 근거 | Coverage status | Notes |
| --- | --- | --- | --- | --- |
| CLI execution and file IO | `EmployeeManagement`, `CommandReader`, `Printer` | `EmployeeManagementTest` | partially covered | argument handling은 있음, malformed-command output assertion은 부족 |
| Command parsing and dispatch | `CommandParser`, `TokenGroup`, `CommandFactory`, `CommandExecutor` | `CommandParserTest`, `CommandExecutorTest` | partially covered | valid path는 커버되지만 invalid option compatibility는 약함 |
| Base add/search/delete | `AddCommand`, `DeleteCommand`, `SearchCommand`, `EmployeeStoreImpl` | `AddCommandTest`, `CommandExecutorTest`, `EmployeeStoreImplTest` | partially covered | base path는 있으나 `ADD` validation depth는 얕음 |
| Output formatting | `ResultStringFormatter`, `CommandExecutor` | `CommandExecutorTest` | partially covered | count/`NONE`/max-5는 간접 커버, direct formatter test는 없음 |
| Modify behavior | `ModCommand`, `EmployeeStoreImpl.modify()` | `EmployeeStoreImplModifyCommandTest`, `CommandModTest` | partially covered | store-level pre-change behavior는 있으나 command-level output이 부족 |
| Name options | `SecondaryOptionEnum`, `CommandFactory`, `Name` | `EmployeeStoreImplNameTest` | covered | first/last name search와 comparison이 잘 잠김 |
| Birthday options | `SecondaryOptionEnum`, `CommandFactory`, `Birthday` | `EmployeeStoreImplBirthdayTest` | partially covered | year comparison은 강하지만 month/day integration은 약함 |
| Career level and certi comparisons | `CareerLevel`, `Certi`, `TertiaryOptionEnum` | `EmployeeStoreImplCareerLevelTest`, `CertiTest` | partially covered | field ordering은 있음, command-level certi search는 얇음 |
| Employee number ordering/comparison | `EmployeeNumber`, `ResultStringFormatter` | `EmployeeNumberTest`, `CommandExecutorTest` | partially covered | century-boundary formatter regression이 없음 |
| AND/OR composition | `CombinationEnum`, command classes, `EmployeeStoreImpl` | `AndOrParameterTest`, `CommandParserTest`, birthday/name integration cases | partially covered | active `-o` duplicate regression이 여전히 필요 |
| Phone partial-field handling | `PhoneNumber`, `SecondaryOptionEnum`, `CommandFactory` | `PhoneNumberTest` | partially covered | field unit test는 있으나 parser -> command -> store integration은 약함 |
| Scale requirement | `EmployeeStoreImpl`, full command path | none dedicated | not covered | 100,000건 acceptance는 문서에만 있고 활성 테스트는 없음 |

## 5. 기능 그룹별 커버리지 보기
### Base Requirement Coverage
- command execution: `B-08`, `B-21`
- data model: `B-02` ~ `B-07`
- add/search/delete: `B-09` ~ `B-12`
- output rules: `B-13` ~ `B-19`
- scale/performance: `B-20`

### Extended Requirement Coverage
- modify command: `F-01` ~ `F-05`
- certi model/output: `F-06`, `F-07`
- field-specific options: `F-08` ~ `F-14`
- comparison search: `F-15` ~ `F-17`
- logical composition: `F-18` ~ `F-23`

## 6. 권장 테스트 매핑
### Unit-Level Focus
- field parsing and validation
- option parsing by position
- employee number ordering/comparison
- condition matching helpers

### Integration-Level Focus
- end-to-end command parsing and execution
- output formatting by command type
- `MOD` pre-change output
- `AND` / `OR` dual-condition flow
- `NONE` 및 max-5 print behavior

### Performance-Level Focus
- 100,000 record insertion
- 대량 데이터 로드 후 search/delete/modify

## 7. 리뷰 체크리스트
- 모든 source requirement에 `Req ID`가 있는가
- 모든 `Req ID`가 PRD section에 매핑되는가
- 모든 high-risk rule에 test focus note가 있는가
- Base와 Further 요구사항이 모두 포함되는가
- output rule과 scale constraint가 명시적으로 추적되는가
