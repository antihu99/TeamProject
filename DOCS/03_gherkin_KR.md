# Gherkin 시나리오: Employee Management CLI

## 1. 문서 개요
- 문서명: `Gherkin 시나리오`
- 관련 제품: `Employee Management CLI`
- 상위 문서:
  - `DOCS/00_PRD_KR.md`
  - `DOCS/01_epic_KR.md`
  - `DOCS/02_requirements_traceability_KR.md`
  - `requirement/Base.md`
  - `requirement/Further.md`
- 소스 기준 참조:
  - `src/main/java/com/sec/bestreviewer/EmployeeManagement.java`
  - `src/main/java/com/sec/bestreviewer/CommandFactory.java`
  - `src/main/java/com/sec/bestreviewer/util/ResultStringFormatter.java`

## 2. 목적
이 문서는 제품 요구사항을 Gherkin 형식의 실행 가능한 비즈니스 시나리오로 변환한 것이다.

이 시나리오는 다음 단계에서 활용된다.
- `SPEC`에서 acceptance 기준 검토
- `RED`에서 failing test 설계
- `GREEN`과 `NEW_FEATURE`에서 기능 검증
- `QA`에서 최종 회귀 검토

## 3. 기능 맵
- Feature 1: 입력/출력 파일 실행 흐름
- Feature 2: 사원 추가
- Feature 3: 사원 삭제
- Feature 4: 사원 검색
- Feature 5: 출력 형식
- Feature 6: 사원 수정
- Feature 7: `certi` 필드 지원
- Feature 8: 세부 필드 옵션
- Feature 9: 비교 검색
- Feature 10: 논리 조건 조합
- Feature 11: 대량 데이터 처리

## 4. Gherkin 시나리오
```gherkin
Feature: 입력 파일과 출력 파일 실행
  As a user
  I want to run the program with an input file and an output file
  So that command processing can be automated

  Scenario: 입력 파일에서 명령을 실행한다
    Given 유효한 사원 명령이 들어 있는 입력 파일이 있다
    When 프로그램이 입력 파일과 출력 파일 인자로 실행된다
    Then 프로그램은 입력 파일을 줄 단위로 읽어야 한다
    And 프로그램은 실행 결과를 출력 파일에 기록해야 한다

  Scenario: 잘못된 명령 줄을 출력에 다시 남긴다
    Given invalid-command 경로에 도달하는 명령 줄이 포함된 입력 파일이 있다
    When 프로그램이 입력 파일과 출력 파일 인자로 실행된다
    Then 출력에는 "wrong command : <original line>"가 포함되어야 한다


Feature: 사원 추가
  As an operator
  I want to add employee records
  So that the employee database can be built and maintained

  Scenario: 유효한 사원 레코드를 추가한다
    Given 비어 있는 사원 데이터베이스가 있다
    When the command "ADD, , , ,14000301,YUJIN KIM,CL2,010-0977-0000,19981206,ADV" is executed
    Then 해당 사원 레코드는 데이터베이스에 저장되어야 한다

  Scenario: non-key 값이 중복되는 사원을 추가한다
    Given 기존 사원 이름과 전화 패턴이 이미 존재하는 데이터베이스가 있다
    When 다른 유효한 ADD 명령이 employee identity context를 제외한 중복 값을 사용한다
    Then 레코드는 여전히 추가되어야 한다


Feature: 사원 삭제
  As an operator
  I want to delete all records matching a condition
  So that obsolete employee data can be removed

  Scenario: count output으로 삭제한다
    Given career level이 "CL3"인 사원 레코드들이 있다
    When the command "DEL, , , ,cl,CL3" is executed
    Then 조건에 맞는 모든 레코드는 삭제되어야 한다
    And 출력은 "DEL,<count>"여야 한다

  Scenario: print output으로 삭제한다
    Given name이 "YUJIN KIM"인 사원 레코드들이 있다
    When the command "DEL,-p, , ,name,YUJIN KIM" is executed
    Then 조건에 맞는 레코드는 "DEL" prefix와 함께 출력되어야 한다
    And 최대 5건까지만 출력되어야 한다

  Scenario: 일치 레코드 없이 삭제한다
    Given birthday가 "19901225"인 레코드가 없는 데이터베이스가 있다
    When the command "DEL,-p, , ,birthday,19901225" is executed
    Then 출력은 "DEL,NONE"이어야 한다


Feature: 사원 검색
  As an operator
  I want to search employee records by condition
  So that I can inspect matched employees

  Scenario: count output으로 검색한다
    Given birthday가 "19810630"인 사원 레코드들이 있다
    When the command "SCH, , , ,birthday,19810630" is executed
    Then 출력은 "SCH,<count>"여야 한다

  Scenario: print output으로 검색한다
    Given birthday가 "19810630"인 사원 레코드들이 있다
    When the command "SCH,-p, , ,birthday,19810630" is executed
    Then 일치하는 레코드는 "SCH" prefix와 함께 출력되어야 한다
    And 레코드는 입사년도가 빠른 순으로 정렬되어야 한다
    And 최대 5건까지만 출력되어야 한다

  Scenario: 일치 레코드 없이 검색한다
    Given name이 "UNKNOWN USER"인 사원이 없는 데이터베이스가 있다
    When the command "SCH, , , ,name,UNKNOWN USER" is executed
    Then 출력은 "SCH,NONE"이어야 한다


Feature: 출력 형식 규칙
  As a tester
  I want command output to be deterministic
  So that automated validation remains stable

  Scenario: print output은 5건으로 제한된다
    Given 검색 조건에 5건을 초과하는 사원 레코드가 일치한다
    When "-p"가 포함된 명령이 실행된다
    Then 오직 5건만 출력되어야 한다

  Scenario: printed output은 employee number 의미 기준으로 정렬된다
    Given 서로 다른 입사년도를 가진 일치 레코드들이 있다
    When "-p"가 포함된 명령이 실행된다
    Then 더 이른 입사년도의 레코드가 먼저 출력되어야 한다

  Scenario: ADD 명령은 레코드 출력이 없다
    Given 유효한 ADD 명령이 있다
    When 명령이 실행된다
    Then DEL/SCH/MOD 형태의 출력이 생성되지 않아야 한다


Feature: 사원 수정
  As an operator
  I want to modify matched employee records
  So that employee data stays current

  Scenario: count output으로 수정한다
    Given career level이 "CL3"인 사원 레코드들이 있다
    When the command "MOD, , , ,cl,CL3,phoneNum,010-0970-0055" is executed
    Then 일치하는 모든 레코드는 수정되어야 한다
    And 출력은 "MOD,<count>"여야 한다

  Scenario: print output으로 수정한다
    Given employee number가 "91351446"인 사원 레코드가 있다
    When the command "MOD,-p, , ,employeeNum,91351446,phoneNum,010-0970-0055" is executed
    Then printed output은 수정 전 레코드 상태를 사용해야 한다

  Scenario: employee number 수정 시도를 거절한다
    Given 기존 사원 레코드가 있다
    When MOD 명령이 "employeeNum"을 수정하려고 한다
    Then 출력은 "wrong command : <original line>"를 포함해야 한다


Feature: certi 필드 지원
  As an operator
  I want the system to support the certi field
  So that extended employee information can be stored and displayed

  Scenario: printed command output은 certi를 포함한다
    Given certi 값을 가진 사원 레코드들이 있다
    When the command "SCH,-p, , ,birthday,19810408" is executed
    Then printed record는 certi 필드를 포함해야 한다
    And 동일한 printed row shape는 DEL과 MOD 출력에도 적용되어야 한다


Feature: 세부 필드 옵션
  As an operator
  I want to target partial fields using secondary options
  So that I can search, delete, and modify more precisely

  Scenario: first name으로 필터링한다
    Given first name이 "YUJIN"인 사원 레코드들이 있다
    When the command "DEL,-p,-f, ,name,YUJIN" is executed
    Then first name이 "YUJIN"인 레코드만 일치해야 한다

  Scenario: last name으로 필터링한다
    Given last name이 "KIM"인 사원 레코드들이 있다
    When the command "MOD, ,-l, ,name,KIM,phoneNum,010-0970-0055" is executed
    Then last name이 "KIM"인 레코드만 수정되어야 한다

  Scenario: phone middle digits로 필터링한다
    Given middle phone digits가 "0970"인 사원 레코드들이 있다
    When the command "DEL,-p,-m, ,phoneNum,0970" is executed
    Then 조건에 맞는 레코드만 삭제되어야 한다

  Scenario: birthday year로 필터링한다
    Given 출생 연도가 "1990"인 사원 레코드들이 있다
    When the command "MOD,-p,-y, ,birthday,1990,name,YUJIN LEE" is executed
    Then birthday year가 "1990"인 레코드만 수정되어야 한다


Feature: 비교 검색 옵션
  As an operator
  I want to compare values in search conditions
  So that threshold-based search is possible

  Scenario: first name을 greater-than-or-equal로 검색한다
    Given 다양한 first name을 가진 사원 레코드들이 있다
    When the command "SCH,-p,-f,-ge,name,YUJIN" is executed
    Then first name이 "YUJIN"보다 사전 순으로 뒤이거나 같은 레코드만 일치해야 한다

  Scenario: career level을 smaller-than로 검색한다
    Given 서로 다른 career level을 가진 사원 레코드들이 있다
    When the command "SCH,-p, ,-s,cl,CL3" is executed
    Then career level이 "CL3"보다 작은 레코드만 일치해야 한다

  Scenario: certification을 greater-than-or-equal로 검색한다
    Given 서로 다른 certi 값을 가진 사원 레코드들이 있다
    When the command "SCH,-p, ,-ge,certi,PRO" is executed
    Then certi 값이 "PRO"보다 크거나 같은 레코드만 일치해야 한다


Feature: 논리 조건 조합
  As an operator
  I want to combine two conditions with AND or OR
  So that I can express richer business queries

  Scenario: AND 조건으로 검색한다
    Given 서로 다른 last name과 career level을 가진 사원 레코드들이 있다
    When the command "SCH,-p,-l, ,name,KIM,-a, , ,cl,CL4" is executed
    Then 두 조건을 모두 만족하는 레코드만 반환되어야 한다

  Scenario: OR 조건으로 검색한다
    Given 서로 다른 phone middle digits와 birthday year를 가진 사원 레코드들이 있다
    When the command "SCH, ,-m, ,phoneNum,0970,-o,-y, ,birthday,1990" is executed
    Then 둘 중 하나의 조건이라도 만족하는 레코드가 반환되어야 한다
    And duplicate record는 한 번만 출력되어야 한다

  Scenario: AND 조건으로 수정한다
    Given career level이 "CL2"이고 birthday month가 "01"인 사원 레코드들이 있다
    When the command "MOD,-p, , ,cl,CL2,-a,-m, ,birthday,01,name,YUJIN LEE" is executed
    Then 두 조건을 모두 만족하는 레코드만 수정되어야 한다


Feature: 대량 데이터 처리
  As a maintainer
  I want the system to support large datasets
  So that the requirement scale is satisfied

  Scenario: 100000건 삽입 후에도 정상 동작한다
    Given 최소 100000개의 사원 레코드가 들어 있는 데이터베이스가 있다
    When search, delete, modify 명령이 실행된다
    Then 각 명령은 정의된 계약에 따라 계속 동작해야 한다
```

## 5. 활용 제안
- `SPEC`에서는 acceptance 기준 참고 자료로 사용한다.
- `RED`에서는 시나리오를 failing test로 변환한다.
- `GREEN`과 `NEW_FEATURE`에서는 구현 범위와 검증 기준으로 사용한다.
- `QA`에서는 회귀 검증 시나리오로 재사용한다.
