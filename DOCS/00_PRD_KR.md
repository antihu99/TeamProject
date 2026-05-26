# PRD: Employee Management CLI

## 1. 문서 개요
- 문서명: `PRD`
- 제품명: `Employee Management CLI`
- 원문 요구사항:
  - `requirement/Base.md`
  - `requirement/Further.md`
- 소스 기준 참조:
  - `src/main/java/com/sec/bestreviewer/EmployeeManagement.java`
  - `src/main/java/com/sec/bestreviewer/CommandParser.java`
  - `src/main/java/com/sec/bestreviewer/CommandFactory.java`
  - `src/main/java/com/sec/bestreviewer/util/ResultStringFormatter.java`
- 제품 유형: 텍스트 파일 기반 사원 데이터베이스 관리 프로그램

## 2. 제품 요약
`Employee Management CLI`는 입력 텍스트 파일에서 명령을 읽고, 실행 결과를 출력 텍스트 파일에 기록하는 command-line 기반 사원 데이터베이스 관리 프로그램이다.

이 제품은 사원 등록, 검색, 삭제, 수정 기능을 지원해야 하며, 세부 필터 옵션, 비교 검색, 논리 조건 조합, 출력 형식 규칙까지 포함해야 한다.

## 3. 문제 정의
사용자는 대화형 UI 없이도 고정된 명령 형식만으로 대량의 사원 데이터를 처리할 수 있는 경량 관리 도구가 필요하다.

제품은 다음을 만족해야 한다.
- 일관되게 사원 레코드를 관리한다.
- 대량 command input을 처리한다.
- 결정적인 출력을 반환한다.
- 최소 100,000건까지 지원한다.
- 자동 검증을 위한 명확한 command contract를 유지한다.

## 4. 제품 목표
1. 파일 기반 명령을 통해 신뢰 가능한 사원 레코드 관리를 제공한다.
2. exact match와 option-based search/delete/modify를 지원한다.
3. 자동 테스트와 리뷰를 위한 결정적인 출력 형식을 보장한다.
4. 최소 100,000건 이상의 사원 레코드를 처리한다.
5. 새 컬럼과 새 옵션을 확장 가능한 구조로 유지한다.

## 5. 사용자
- 주요 사용자:
  - command processor를 구현하는 개발자
  - command behavior를 검증하는 테스트 엔지니어
  - 요구사항 예시와 출력 결과를 대조하는 리뷰어

## 6. 범위
### In Scope
- `ADD`를 통한 사원 등록
- `DEL`을 통한 사원 삭제
- `SCH`를 통한 사원 검색
- `MOD`를 통한 사원 수정
- 출력 옵션 `-p`
- `name`, `phoneNum`, `birthday`의 세부 옵션
- `SCH` 전용 비교 옵션
- 논리 조합 옵션 `-a`, `-o`
- `certi` 컬럼 지원
- 입력 파일에서 출력 파일까지의 전체 실행 흐름

### Out of Scope
- 대화형 UI
- 인증/인가
- 외부 API 연동
- 허용된 저장 방식 범위를 넘는 별도 DB 서버 요구
- `010` prefix 자체에 대한 phone 비교

## 7. 제품 흐름
1. 사용자가 입력 파일과 출력 파일 경로로 프로그램을 실행한다.
2. 프로그램이 입력 파일을 줄 단위로 읽는다.
3. 각 명령을 command type과 option 기준으로 파싱한다.
4. 프로그램이 명령을 in-memory 또는 storage-backed employee database에 적용한다.
5. 프로그램이 포맷된 결과를 출력 파일에 쓴다.

실행 형식:

```text
EmployeeManagement [input file] [output file]
```

예시:

```text
EmployeeManagement input.txt output.txt
```

현재 구현 기준 실행 흐름:
1. `EmployeeManagement`
2. `CommandReader`
3. `CommandParser`
4. `CommandFactory`
5. `CommandExecutor`
6. `EmployeeStoreImpl`
7. `ResultStringFormatter`

## 8. 데이터 모델
최종 사원 레코드는 다음 필드를 지원해야 한다.

| Field | Type/Format | Description |
| --- | --- | --- |
| `employeeNum` | 8-digit number | 앞 2자리가 입사년도 의미를 가지며 `90XXXXXX` ~ `19XXXXXX` 범위 |
| `name` | uppercase English, max 15 chars | first name과 last name이 공백으로 구분됨 |
| `cl` | `CL1`, `CL2`, `CL3`, `CL4` | 경력 개발 단계 |
| `phoneNum` | `010-xxxx-xxxx` | `010` prefix를 갖는 전화번호 |
| `birthday` | `YYYYMMDD` | 생년월일 |
| `certi` | `ADV`, `PRO`, `EX` | 추가 요구사항에서 도입된 자격 수준 |

현재 소스 기준 참고:
- `Employee` 생성자와 `CommandFactory`의 `ADD` 경로는 이미 `certi`까지 포함한 employee input payload를 기대한다.

## 9. 핵심 기능 요구사항
### FR-01. Add Employee
- Command: `ADD`
- Format:

```text
ADD,옵션1,옵션2,옵션3,사원번호,성명,경력개발단계,전화번호,생년월일,certi
```

- 현재 소스 구현은 이미 `certi`를 포함한 `ADD` 형식을 요구한다.
- 새로운 사원 레코드를 데이터베이스에 추가한다.
- `employeeNum`은 레코드 식별 형식에 맞아야 한다.
- 다른 필드는 중복될 수 있지만 모든 필드는 non-null이다.
- `ADD`는 일반적인 search/delete style 출력이 없다.

### FR-02. Delete Employee
- Command: `DEL`
- Format:

```text
DEL,옵션1,옵션2,옵션3,조건 Column명,조건 값
```

- 조건에 맞는 모든 레코드를 삭제한다.
- `-p`가 있으면 일치 레코드를 출력한다.
- `-p`가 없으면 삭제 건수만 출력한다.

### FR-03. Search Employee
- Command: `SCH`
- Format:

```text
SCH,옵션1,옵션2,옵션3,조건 Column명,조건 값
```

- 조건에 맞는 모든 레코드를 반환한다.
- `-p`가 있으면 일치 레코드를 출력한다.
- `-p`가 없으면 검색 건수만 출력한다.

### FR-04. Modify Employee
- Command: `MOD`
- Format:

```text
MOD,옵션1,옵션2,옵션3,조건 Column명,조건 값,변경할 Column명,변경할 값
```

- 조건에 맞는 모든 레코드를 수정한다.
- 한 명령에서 한 컬럼만 변경할 수 있다.
- `employeeNum`은 수정할 수 없다.
- `-p`가 있으면 수정 전 레코드를 출력한다.
- `-p`가 없으면 수정 건수만 출력한다.

## 10. 옵션 요구사항
### OR-01. 출력 옵션
- Option: `-p`
- 위치: `옵션1`만 허용
- 지원 명령: `DEL`, `SCH`, `MOD`
- 동작:
  - 일치 레코드를 줄 단위로 출력한다.
  - `employeeNum` 기반 join-year priority로 정렬한다.
  - 최대 5건만 출력한다.
  - 일치가 없으면 `NONE`을 출력한다.

### OR-02. 이름 필터 옵션
- 위치: `옵션2`
- 지원 옵션:
  - `-f`: first name
  - `-l`: last name

### OR-03. 전화번호 필터 옵션
- 위치: `옵션2`
- 지원 옵션:
  - `-m`: middle digits
  - `-l`: last digits

### OR-04. 생일 필터 옵션
- 위치: `옵션2`
- 지원 옵션:
  - `-y`: year
  - `-m`: month
  - `-d`: day

### OR-05. 비교 검색 옵션
- 위치: `옵션3`
- 지원 명령: `SCH`만
- 지원 옵션:
  - `-g`: greater than
  - `-ge`: greater than or equal
  - `-s`: smaller than
  - `-se`: smaller than or equal

참고:
- `phoneNum` 비교는 prefix `010`에는 적용되지 않는다.
- `employeeNum` 비교는 join-year-first ordering을 따른다.

### OR-06. 논리 조합 옵션
- 조합 연산자:
  - `-a`: AND
  - `-o`: OR
- 지원 명령: `DEL`, `SCH`, `MOD`
- 두 개의 조건을 지정할 수 있다.
- duplicate record는 한 번만 나타나야 한다.

## 11. 출력 요구사항
### General Rules
- 출력은 output file에 기록된다.
- `-p`가 적용되면 출력은 command name prefix를 포함해야 한다.
  - `DEL,...`
  - `SCH,...`
  - `MOD,...`
- 일치 레코드가 없으면:
  - `DEL,NONE`
  - `SCH,NONE`
  - `MOD,NONE`
- 현재 CLI 구현에서 parse/build/execute 중 `IllegalArgumentException`이 발생하면:
  - `wrong command : <original line>`

### Count Output
- `-p`가 없으면:
  - `DEL,<count>`
  - `SCH,<count>`
  - `MOD,<count>`

### Printed Record Output
- printed row는 최대 5건이다.
- printed row는 join-year priority 기준의 employee number ordering으로 정렬된다.
- `MOD`는 변경 전 레코드를 출력해야 한다.
- 현재 formatter 구현은 `DEL`, `SCH`, `MOD` printed row에 `certi`를 포함한다.

### 현재 소스 참고 사항
- `EmployeeManagement`는 `IllegalArgumentException`을 잡아 `wrong command : <line>`을 출력 파일에 기록한다.
- `CommandFactory`는 이미 `ADD`를 `employeeNum,name,cl,phoneNum,birthday,certi`로 구성한다.
- `ResultStringFormatter`는 printed employee row 끝에 `certi`를 붙인다.
- `CountCommand` / `CNT`는 소스와 테스트에는 존재하지만 원문 요구사항의 핵심 command는 아니다.

## 12. 명령 계약
### ADD Contract
- 한 명의 사원 레코드를 추가한다.
- 레코드는 삭제되기 전까지 저장된다.

### DEL Contract
- 주어진 조건에 맞는 모든 레코드를 삭제한다.

### SCH Contract
- 주어진 조건에 맞는 모든 레코드를 찾는다.
- `OR` 조건 흐름에서는 deduplicated employee match set이 사용된다.

### MOD Contract
- 주어진 조건에 맞는 모든 레코드를 수정한다.
- 한 번에 하나의 target column만 바꾼다.
- 현재 CLI 경로에서는 `employeeNum` 수정 시도가 invalid command flow로 처리된다.

## 13. 비기능 요구사항
### NFR-01. Performance
- 시스템은 최소 100,000건의 employee record를 지원해야 한다.
- 100,000건 삽입 후에도 search, delete, modify가 계속 동작해야 한다.

### NFR-02. Deterministic Output
- 같은 입력은 항상 같은 출력을 만들어야 한다.
- 출력 순서와 형식은 요구사항 정의를 정확히 따라야 한다.

### NFR-03. Input Contract Stability
- command는 지정된 CSV-like format에 따라 엄격하게 해석되어야 한다.
- option position은 반드시 지켜져야 한다.

### NFR-04. Testability
- text input/output 비교만으로 검증하기 쉬워야 한다.
- 동작은 unit/integration test로 자동 검증 가능해야 한다.

## 14. 제약사항
- DB 관련 외부 모듈은 사용할 수 있다.
- 그 외 구현 선택은 외부 동작만 요구사항과 일치하면 자유롭다.
- command input과 output은 텍스트 파일 기반이다.

## 15. 리스크와 경계 조건
- base와 extended record shape 사이의 차이는 구현 전반에서 일관되게 처리돼야 한다.
- `MOD` 출력은 pre-change 상태를 사용해야 하므로 리팩토링 시 깨지기 쉽다.
- `OR` 검색은 중복을 출력하지 않아야 한다.
- `employeeNum` 기반 join-year ordering은 모든 명령에서 일관돼야 한다.
- partial field (`name`, `birthday`, `phoneNum`, `certi`) 비교 규칙은 요구사항 semantics와 정확히 일치해야 한다.

## 16. 수용 기준
### Core Acceptance
- 프로그램이 입력 파일을 읽고 유효한 출력 파일을 생성한다.
- `ADD`, `DEL`, `SCH`, `MOD`가 각 command contract를 따른다.
- option position과 option semantics가 정확히 지원된다.

### Output Acceptance
- `-p`는 최대 5건만 출력한다.
- no-match는 `COMMAND,NONE`을 출력한다.
- non-`-p`는 `COMMAND,count`를 출력한다.
- `MOD` printed row는 변경 전 상태를 반영한다.

### Data Acceptance
- employee field가 요구 형식을 따른다.
- extended `certi` field가 최종 제품에서 지원된다.
- 현재 구현 기준으로 `ADD`는 `certi`를 입력받는다.

### Scale Acceptance
- 100,000건 처리를 지원한다.

## 17. 권장 릴리스 관점
### Phase 1: Base Features
- `ADD`
- `DEL`
- `SCH`
- `-p`
- ordering / `NONE` / count output

### Phase 2: Extended Features
- `MOD`
- `certi`
- detailed field options
- comparison search
- logical combination conditions

## 18. 최종 제품 정의
최종 제품은 고정된 command contract, 결정적인 출력 규칙, 확장된 필터링/수정 기능을 지원하는 파일 기반 employee database management CLI이다.
