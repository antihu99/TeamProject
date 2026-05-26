# Epic 정의: Employee Management CLI

## 1. 문서 개요
- 문서명: `Epic 정의`
- 관련 제품: `Employee Management CLI`
- 상위 문서:
  - `DOCS/00_PRD_KR.md`
  - `requirement/Base.md`
  - `requirement/Further.md`
- 소스 기준 참조:
  - `src/main/java/com/sec/bestreviewer`
  - `src/test/java/com/sec/bestreviewer`

## 2. 제품 목표 요약
이 제품은 파일 기반 사원 데이터베이스 CLI로서, 안정적인 추가/검색/삭제/수정 기능과 결정적인 출력, 대량 처리, 세부 옵션 기반 필터링을 제공해야 한다.

## 3. Epic 목록
## Epic 1. 핵심 명령 처리 흐름
### Goal
시스템이 입력 파일에서 명령을 읽고, 파싱하고, 실행하고, 출력 파일에 결과를 쓰는 핵심 lifecycle을 구현한다.

### Why It Matters
안정적인 command-processing flow가 없으면 어떤 비즈니스 명령도 일관되게 실행하거나 테스트할 수 없다.

### Included Capabilities
- input/output file execution flow
- command parsing
- command dispatch
- employee record storage access
- result writing

### Key Stories
- 사용자로서 입력/출력 파일 경로로 프로그램을 실행해 자동화된 command execution을 하고 싶다.
- 개발자로서 모든 명령이 엄격한 형식을 따르길 원한다.
- 테스터로서 파일 기반 출력으로 expected/actual을 비교하고 싶다.

### Acceptance Signals
- 프로그램이 입력 파일을 읽고 출력 파일을 쓴다.
- 명령 실행 순서가 입력 순서와 일치한다.
- invalid command structure를 분리해서 테스트할 수 있다.
- 현재 CLI 구현에서는 `IllegalArgumentException` 기반 invalid line이 `wrong command : <line>`으로 출력된다.

## Epic 2. 사원 레코드 관리
### Goal
기본 사원 데이터 조작 기능인 add, search, delete를 지원한다.

### Why It Matters
이 명령들은 base requirement의 최소 유효 범위를 구성한다.

### Included Capabilities
- `ADD`
- `DEL`
- `SCH`
- field-based exact matching
- non-null employee data handling

### Key Stories
- 작업자로서 새로운 사원 레코드를 추가하고 싶다.
- 작업자로서 조건 기반으로 사원을 검색하고 싶다.
- 작업자로서 조건 기반으로 사원을 삭제하고 싶다.

### Acceptance Signals
- `ADD`가 새 레코드를 저장한다.
- `DEL`이 조건 일치 레코드를 모두 삭제한다.
- `SCH`가 조건 일치 레코드를 모두 반환한다.
- 사원 필드가 요구 형식을 따른다.

## Epic 3. 출력 규칙과 결과 포맷팅
### Goal
`DEL`, `SCH`, `MOD`에 대해 결정적이고 요구사항 준수적인 출력을 제공한다.

### Why It Matters
이 프로젝트는 output contract 의존도가 높기 때문에, 데이터 변경뿐 아니라 정확한 formatting이 correctness의 핵심이다.

### Included Capabilities
- `-p` output mode
- count output mode
- `NONE` output behavior
- 최대 5건 출력 제한
- join-year 기반 ordering
- command-prefixed result line

### Key Stories
- 테스터로서 printed output이 하나의 정확한 형식을 따르길 원한다.
- 사용자로서 `-p`가 없을 때 count 결과를 보고 싶다.
- 리뷰어로서 no-match case가 `COMMAND,NONE`으로 명확히 드러나길 원한다.

### Acceptance Signals
- `-p` 사용 시 올바른 순서로 최대 5줄이 출력된다.
- `-p`가 없으면 영향 count만 출력된다.
- no-match case는 `COMMAND,NONE`을 출력한다.

## Epic 4. 확장 데이터 모델과 수정 기능
### Goal
기본 시스템을 `MOD`와 `certi` 필드까지 확장한다.

### Why It Matters
이 확장은 단순 저장/검색 도구를 더 완전한 사원 관리 제품으로 바꾼다.

### Included Capabilities
- `MOD`
- `certi` column support
- pre-change output for `MOD`
- one-field-per-command update rule
- immutable `employeeNum`

### Key Stories
- 작업자로서 조건 일치 레코드를 수정하고 싶다.
- 한 명령에서 하나의 컬럼만 바꿔 변경 이력을 명확히 하고 싶다.
- 사용자로서 `MOD -p`가 변경 전 값을 보여주길 원한다.

### Acceptance Signals
- `MOD`가 조건 일치 레코드를 모두 수정한다.
- `employeeNum`은 수정할 수 없다.
- printed `MOD` output은 변경 전 상태를 반영한다.
- printed final record shape는 `certi`를 지원한다.
- 현재 소스는 `ADD` 경로에서도 이미 `certi`를 요구한다.

## Epic 5. 고급 조회와 조건 조합
### Goal
이름, 전화번호, 생일, 비교 검색, 논리 조건 조합을 포함한 풍부한 필터링 semantics를 지원한다.

### Why It Matters
이 epic은 제품을 exact-match CRUD에서 더 표현력 있는 employee query tool로 확장한다.

### Included Capabilities
- name options: `-f`, `-l`
- phone options: `-m`, `-l`
- birthday options: `-y`, `-m`, `-d`
- comparison options: `-g`, `-ge`, `-s`, `-se`
- logical options: `-a`, `-o`

### Key Stories
- 작업자로서 first name, last name, phone segment, birthday part로 정밀하게 조건을 걸고 싶다.
- 작업자로서 `SCH`에서 threshold-based comparison search를 하고 싶다.
- 작업자로서 `AND` / `OR` 조합으로 더 현실적인 조건을 표현하고 싶다.

### Acceptance Signals
- option position이 올바르게 해석된다.
- comparison search는 `SCH`에서만 동작한다.
- duplicate record는 `OR` 기반 match set에서 한 번만 처리된다.
- partial-field semantics가 요구사항 예시와 일치한다.

## Epic 6. 확장성, 신뢰성, 테스트 가능성
### Goal
대량 데이터도 처리 가능하고 테스트/검증이 쉬운 제품을 보장한다.

### Why It Matters
요구사항은 명시적으로 100,000건 처리를 요구하고, 자동 검증 가능한 결정적 파일 출력을 강하게 시사한다.

### Included Capabilities
- 100,000 record support
- deterministic behavior
- automated testability
- stable command contracts

### Key Stories
- 테스터로서 결정적인 결과를 바탕으로 신뢰 가능한 regression testing을 하고 싶다.
- 유지보수 담당자로서 command behavior가 contract-driven 상태로 유지되길 원한다.
- 리뷰어로서 feature boundary가 명확하길 원한다.

### Acceptance Signals
- 대량 삽입 후에도 search/delete/modify가 동작한다.
- 같은 입력은 항상 같은 출력을 만든다.
- unit/integration test로 기능을 검증할 수 있다.

## 4. 현재 소스 스냅샷
- 핵심 runtime path는 `EmployeeManagement -> CommandReader -> CommandParser -> CommandFactory -> CommandExecutor -> EmployeeStoreImpl -> ResultStringFormatter`이다.
- 현재 자동화 테스트는 field/store/command layer에서 강한 편이다.
- active gap은 malformed-command output assertion, phone partial-field integration, direct formatter regression, active end-to-end approval coverage, 100,000-record validation이다.

## 5. 권장 전달 순서
1. Epic 1: 핵심 명령 처리 흐름
2. Epic 2: 사원 레코드 관리
3. Epic 3: 출력 규칙과 결과 포맷팅
4. Epic 4: 확장 데이터 모델과 수정 기능
5. Epic 5: 고급 조회와 조건 조합
6. Epic 6: 확장성, 신뢰성, 테스트 가능성

## 6. 브랜치 매핑 제안
- `SPEC`: Epic 정의, 범위 정리, story breakdown
- `RED`: epic/story 기준 failing test
- `GREEN`: base epic 구현
- `REFACTORING`: internal quality improvement
- `NEW_FEATURE`: extended epic 구현
- `QA`: epic-level acceptance validation
