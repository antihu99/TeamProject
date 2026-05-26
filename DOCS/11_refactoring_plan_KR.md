# REFACTORING 단계 구조 개선 계획서

## 1. 리팩토링 대상 요약
이번 `REFACTORING` 단계에서 우선적으로 손봐야 할 대상은 `CommandFactory`, `CommandParser`-`TokenGroup`-`OptionParser` 조합, `command/*` 계층, `EmployeeStoreImpl`, `ResultStringFormatter` 및 `EmployeeManagement` 경계부다.

핵심 이유는 현재 구현이 기능적으로는 동작하지만, 명령 파싱, 조건 정규화, 출력 포맷, 저장소 수정 정책이 서로 강하게 결합되어 있어 구조 변경 시 회귀 위험이 크기 때문이다. 특히 부분 필드 검색(`-f`, `-l`, `-m`, `-y`, `-d`)과 `AND`/`OR` 조합은 여러 클래스에 암묵 규약이 흩어져 있어 유지보수성이 낮다.

## 2. 우선순위 높은 구조 문제
### 2.1 `CommandFactory`의 과도한 책임
- `CommandFactory`는 명령 분기만 담당하지 않고, 필드명 매핑, 부분 필드용 값 가공, `MOD` 수정 조건 조립, `AND`/`OR` 경로 분기까지 함께 처리한다.
- `"HOHAN "`, `"010-0000-"`, `"9999"` 같은 보정 문자열과 `1~5` 타입 숫자에 의존해 부분 검색을 성립시키고 있어 SRP와 OCP 모두 취약하다.
- `MOD`의 `AND`/`OR` 경로에는 아직 `TODO`가 남아 있어 리팩토링 우선순위가 높다.

### 2.2 파서 계층의 위치 기반 규약 의존
- `CommandParser`는 `List<String>`와 인덱스 계산에 강하게 의존하며, 최소 토큰 수 검증 코드가 주석 처리되어 있다.
- `TokenGroup`과 `OptionParser`가 타입 안전한 명령 모델을 제공하지 못해 후단 로직이 문자열/빈 칸 규약에 의존한다.

### 2.3 `command/*` 계층의 중복과 불완전한 템플릿 구조
- `Command`는 템플릿 메서드처럼 보이지만 기본 구현이 빈 리스트 반환이다.
- `AddCommand`는 직접 `execute()`를 사용하고, `DeleteCommand`/`SearchCommand`/`ModCommand`는 단일 조건과 조합 조건을 각각 별도로 다뤄 구조가 일관되지 않다.
- 출력 포맷 선택, 필드 인덱스 계산, 저장소 호출 패턴이 유사하게 반복된다.

### 2.4 `EmployeeStoreImpl`의 책임 혼재
- `EmployeeStoreImpl`은 저장소이면서 검색 엔진, 조합 조건 평가기, 수정기, 복사기 역할을 모두 맡고 있다.
- `modify()` 오버로드가 거의 같은 흐름을 중복 구현하고, `modifyFieldOfList()`는 긴 `if/else` 체인으로 사원 객체를 재구성한다.

### 2.5 출력 정책의 매직 넘버 및 경계부 결합
- `ResultStringFormatter`는 사번 정렬을 위해 연도 보정용 숫자 상수를 직접 계산한다.
- `EmployeeManagement`는 실행 흐름 제어와 malformed command 처리까지 함께 맡고 있으나, 이 경계 동작에 대한 활성 통합 테스트가 약하다.

## 3. 개선 전략
### 3.1 외부 계약 고정
- 명령 문자열(`ADD`, `DEL`, `SCH`, `MOD`)
- 출력 형식(`CMD,NONE`, `CMD,count`, `CMD,employee...`)
- `-p` 최대 5건 제한
- 사번 정렬 규칙
- `MOD`가 수정 전 레코드를 반환하는 현재 계약

### 3.2 단계별 내부 구조 분리
1. 테스트 안전망을 먼저 보강한다.
2. `CommandFactory`에서 조건 해석 로직을 분리해 typed condition 모델로 이동한다.
3. `CommandParser`-`TokenGroup`-`OptionParser`를 명시적 명령 DTO 중심으로 정리한다.
4. `DeleteCommand`/`SearchCommand`/`ModCommand`의 공통 실행 흐름을 추출한다.
5. `EmployeeStoreImpl` 내부의 검색/수정/복사 경로를 하나의 일관된 경로로 축소한다.
6. `ResultStringFormatter`와 앱 경계부의 출력 정책을 명명된 정책 객체 또는 헬퍼로 분리한다.

### 3.3 리팩토링 원칙
- 한 번에 한 계층만 수정한다.
- 동작 변화가 보이면 즉시 범위를 줄인다.
- 테스트 이름이 외부 계약을 설명하도록 유지한다.
- public API와 파일 입출력 계약은 그대로 둔다.

## 4. 파일/클래스별 리팩토링 계획
### 4.1 `CommandFactory.java`
- 필드명 매핑과 부분 필드 변환 로직을 전담 클래스로 이동한다.
- `getConditionMapFromParams()`의 placeholder 문자열 생성 규칙을 제거하고, `field + subfield + operator + value` 형태의 명시적 조건 객체로 치환한다.
- `buildSingleCommand()`와 `buildAndOrCommand()`에서 중복되는 분기 구조를 공통 빌더 메서드로 정리한다.

### 4.2 `CommandParser.java`, `TokenGroup.java`, `OptionParser.java`
- raw `List<String>` 중심 모델을 immutable parsed-command 구조로 정리한다.
- 최소 토큰 수, `AND`/`OR` 위치, 수정 명령의 파라미터 길이 규칙을 명시적 검증으로 올린다.
- 빈 문자열/공백 문자열 sentinel 사용을 제거하거나 최소화한다.

### 4.3 `command/Command.java`, `DeleteCommand.java`, `SearchCommand.java`, `ModCommand.java`, `AddCommand.java`
- 단일 조건/조합 조건 공통 실행 흐름을 helper 또는 abstract base 메서드로 추출한다.
- 디버그 출력 메서드는 제거하거나 테스트 유틸로 이동한다.
- 각 명령 클래스는 "저장소에 어떤 질의를 보낼지"까지만 책임지게 하고, 결과 포맷 책임은 공통 계층으로 이동한다.

### 4.4 `EmployeeStoreImpl.java`, `Employee.java`, `FieldEnum.java`, `field/*`
- 검색 경로를 단일 조건과 조합 조건으로 나누기보다 공통 predicate 생성 방식으로 통합한다.
- 수정 시 Employee 복제를 생성자/팩토리/`withUpdatedField(...)` 스타일로 단순화한다.
- `modifyFieldOfList()`의 긴 분기문을 필드별 updater map 또는 enum dispatch로 바꾼다.
- `field/*`가 부분 필드 비교 규칙을 직접 표현하도록 하여 상위 계층의 보정 문자열 생성을 제거한다.

### 4.5 `ResultStringFormatter.java`, `CommandExecutor.java`, `EmployeeManagement.java`, `Printer.java`
- 사번 정렬 기준을 의미 있는 이름의 정책으로 추출한다.
- 출력 문자열 조립과 결과 개수 제한을 테스트 가능한 작은 메서드로 분리한다.
- malformed command 처리와 파일 입출력 예외 경계를 `EmployeeManagement`에서 더 명시적으로 고정한다.

### 4.6 우선 보강할 테스트 파일
- `CommandExecutorTest`
- `EmployeeStoreImplNameTest`
- `EmployeeStoreImplBirthdayTest`
- `EmployeeStoreImplModifyCommandTest`
- `CommandModTest`
- `PhoneNumberTest`와 연결되는 실제 통합 경로 테스트 신설
- `AndOrParameterTest` 대체 또는 보강
- `EmployeeManagementTest`

## 5. 4인 분담안
### A_01_REFACTORING
- 대상: `CommandParser`, `TokenGroup`, `OptionParser`, `CommandFactory`
- 목표: 문자열/인덱스 기반 조건 해석을 명시적 모델로 분리
- 선행 테스트: `CommandParserTest`, `EmployeeStoreImplNameTest`, `EmployeeStoreImplBirthdayTest`

### A_02_REFACTORING
- 대상: `command/*`, `CommandExecutor`, `ResultStringFormatter`
- 목표: 명령 실행 흐름과 출력 포맷 중복 제거
- 선행 테스트: `CommandExecutorTest`, `CommandModTest`

### A_03_REFACTORING
- 대상: `EmployeeStoreImpl`, `Employee`, `FieldEnum`, `field/*`
- 목표: 검색/수정/복사 책임 분리와 내부 경로 단순화
- 선행 테스트: `EmployeeStoreImplTest`, `EmployeeStoreImplCareerLevelTest`, `EmployeeStoreImplModifyCommandTest`, `field/*Test`

### A_04_REFACTORING
- 대상: `EmployeeManagement`, `CommandReader`, `Printer`, 누락 회귀 테스트
- 목표: 앱 경계 안정화와 테스트 안전망 확장
- 선행 테스트: `EmployeeManagementTest`, `CommandReaderTest`, `AndOrParameterTest` 보강본

### 권장 병합 순서
1. `A_04_REFACTORING`에서 회귀 테스트를 먼저 보강
2. `A_01_REFACTORING`과 `A_03_REFACTORING`을 병렬 진행
3. `A_02_REFACTORING`에서 실행/출력 계층 정리를 마지막에 수행

## 6. 테스트 유지 전략
### 6.1 먼저 고정할 회귀 포인트
- `-p` 출력 시 최대 5건만 반환되는지
- 결과가 없을 때 `NONE`이 유지되는지
- 사번 기준 정렬 규칙이 유지되는지
- `MOD`가 수정 전 레코드를 반환하는지
- malformed command가 기존 메시지 형식을 유지하는지

### 6.2 우선 추가가 필요한 테스트
- `phoneNum -m`, `phoneNum -l` 실경로 통합 테스트
- `AND`/`OR` 결과를 실제 assert로 고정하는 테스트
- `ResultStringFormatter` 직접 회귀 테스트
- disabled approval 테스트의 재활성화 또는 동등한 회귀 테스트 대체

### 6.3 실행 전략
- 각 개인 브랜치는 자신이 건드리는 패키지의 집중 테스트를 먼저 통과시킨다.
- 병합 전에는 관련 집중 테스트 + `mvn test` 전체 회귀를 수행한다.
- 출력 계약이 얽힌 변경은 snapshot/approval 또는 문자열 비교 테스트를 반드시 남긴다.

## 7. 전/후 비교 포인트
- Before: `CommandFactory`가 조건 해석과 명령 생성까지 모두 수행
- After: 조건 해석기와 명령 빌더가 분리되어 SRP가 명확해짐

- Before: 부분 필드 검색이 placeholder 문자열과 숫자 타입 규약에 의존
- After: 필드/서브필드/연산자/value가 명시적 구조로 표현됨

- Before: `DeleteCommand`/`SearchCommand`/`ModCommand` 실행 흐름이 중복
- After: 공통 실행 경로를 공유하고 명령별 차이만 남김

- Before: `EmployeeStoreImpl`이 복사/수정/검색/조합 처리 책임을 모두 가짐
- After: 내부 query/mutation 책임이 분리되어 변경 영향 범위가 작아짐

- Before: 출력 정렬 정책이 매직 넘버로 암묵 표현됨
- After: 정렬 정책이 이름 있는 규칙과 테스트로 고정됨

## 8. 권장 추가 산출물
- `report/04_refactoring_analysis_report.md`: 이번 분석과 브랜치 준비 결과를 기록하는 단계 보고서
- `prompting/04_refactoring_analysis_conversation.md`: 이번 REFACTORING 단계 대화와 입력 프롬프트 보관
- QA 단계에서 사용할 전후 비교용 체크리스트 초안 문서

## 9. 메모
이번 문서는 실제 소스와 현재 테스트 자산을 기준으로 작성한 구조 개선 계획서다. 아직 production code 리팩토링은 시작하지 않았으며, 본 단계의 목적은 안전한 분해 순서와 회귀 방지 기준을 먼저 고정하는 것이다.
