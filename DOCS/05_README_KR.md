# Employee Management CLI 문서 허브

## 1. 개요
이 폴더는 `Employee Management CLI` 프로젝트의 기획, 테스트, 구현, 검증을 지원하는 문서 세트를 담고 있다.

문서 세트는 현재 팀 전략을 지원하도록 구성되어 있다.
- 요구사항 기반 개발
- TDD 우선 실행
- 브랜치 기반 병렬 협업
- 출력 계약 검증
- 최종 QA 및 발표 준비

이 README는 문서 간 연결 관계와 각 단계에서 어떤 문서를 어떻게 활용해야 하는지 이해하기 위한 진입점이다.

## 2. 프로젝트 요약
제품은 텍스트 파일 기반 사원 관리 CLI이다.

주요 목표:
- 입력 파일에서 명령을 읽는다.
- 사원 데이터 조작 명령을 처리한다.
- 결과를 출력 파일에 결정적으로 기록한다.
- 최대 100,000건까지 처리할 수 있어야 한다.
- 기본 요구사항과 추가 요구사항을 모두 만족해야 한다.

핵심 명령:
- `ADD`
- `DEL`
- `SCH`
- `MOD`

핵심 옵션 그룹:
- 출력 옵션: `-p`
- 세부 필드 옵션: `-f`, `-l`, `-m`, `-y`, `-d`
- 비교 옵션: `-g`, `-ge`, `-s`, `-se`
- 논리 조합 옵션: `-a`, `-o`

확장 데이터 모델:
- `certi`

## 3. 현재 소스 구조
현재 Java 소스 트리는 `src/main/java/com/sec/bestreviewer` 아래에서 다음과 같이 구성되어 있다.

- 상위 실행 흐름:
  - `EmployeeManagement`
  - `CommandReader`
  - `CommandParser`
  - `CommandFactory`
  - `CommandExecutor`
  - `Printer`
  - `TokenGroup`
- command 패키지:
  - `command/Command`
  - `command/AddCommand`
  - `command/DeleteCommand`
  - `command/SearchCommand`
  - `command/ModCommand`
  - `command/CountCommand`
  - `command/CombinationEnum`
- store 패키지:
  - `store/EmployeeStore`
  - `store/EmployeeStoreImpl`
  - `store/Employee`
  - `store/FieldEnum`
  - `store/Injection`
- field 패키지:
  - `field/Field`
  - `field/EmployeeNumber`
  - `field/Name`
  - `field/CareerLevel`
  - `field/PhoneNumber`
  - `field/Birthday`
  - `field/Certi`
- util 패키지:
  - `util/OptionParser`
  - `util/PrimaryOptionEnum`
  - `util/SecondaryOptionEnum`
  - `util/TertiaryOptionEnum`
  - `util/ResultStringFormatter`
  - `util/Pair`
  - `util/CachedSupplier`

현재 소스 참고 사항:
- `CountCommand` / `CNT`는 소스와 테스트에는 존재하지만, 원본 요구사항 문서의 핵심 요구 범위에는 포함되지 않는다.

## 4. 현재 테스트 상태
현재 테스트 트리 `src/test/java/com/sec/bestreviewer`에는 단위 테스트와 통합 테스트가 비교적 넓게 존재한다.

강점:
- 핵심 value object에 대한 파싱/비교 테스트가 있다.
- 이름, 생일, 경력, 수정 동작에 대한 store-level 테스트가 있다.
- `DEL` / `SCH` 출력 계약에 대한 command-level 테스트가 있다.

남은 공백:
- `EmployeeManagement`의 malformed command 출력 검증
- phone partial-field 통합 테스트
- `ResultStringFormatter` 직접 회귀 테스트
- 현재 비활성화된 end-to-end approval 테스트의 활성 커버리지
- 100,000건 검증

## 5. 실행 환경 참고
프로젝트 실행 전 루트 `README.md`에 적힌 사전 준비 사항을 먼저 적용해야 한다.

- `settings.xml`을 로컬 Maven settings 경로에 복사한다.
- 환경에 따라 로컬 Maven repository 캐시를 정리한다.
- 이후 IntelliJ를 다시 실행하거나 프로젝트 빌드를 다시 수행한다.

이 설정은 내부 Maven 저장소 설정에 의존할 가능성이 있으므로 중요하다.

## 6. 전달 전략 요약
팀은 다음 단계 흐름을 따른다.

1. `SPEC`
2. `RED`
3. `GREEN`
4. `REFACTORING`
5. `NEW_FEATURE`
6. `QA`

각 단계는 `main`에서 별도 통합 브랜치로 생성된다.

각 통합 브랜치는 다시 4개의 개인 작업 브랜치로 나뉜다.
- `A_01_*`
- `A_02_*`
- `A_03_*`
- `A_04_*`

이 구조의 목적:
- 작고 리뷰 가능한 변경 단위 유지
- 병렬 팀 작업 지원
- 단계 간 TDD 중심 인계
- QA와 병합 제어 단순화

## 7. 문서 맵
### 핵심 문서
- [`00_PRD_KR.md`](./00_PRD_KR.md)
  - 제품 정의, 범위, 목표, 데이터 모델, 명령 계약, 출력 규칙, 수용 기준

- [`01_epic_KR.md`](./01_epic_KR.md)
  - 제품 기능을 epic 단위로 재구성한 문서

- [`02_requirements_traceability_KR.md`](./02_requirements_traceability_KR.md)
  - 요구사항과 PRD, 구현, 테스트를 연결하는 추적 문서

- [`03_gherkin_KR.md`](./03_gherkin_KR.md)
  - 요구사항을 테스트 가능한 Gherkin 시나리오로 정리한 문서

- [`04_todo_KR.md`](./04_todo_KR.md)
  - README 일정, 평가 기준, 브랜치 전략에 맞춘 실행 계획

- [`06_spec_gap_log_KR.md`](./06_spec_gap_log_KR.md)
  - RED 전에 정리해야 할 SPEC 단계의 계약 공백과 해석 차이

- [`07_red_test_inventory_KR.md`](./07_red_test_inventory_KR.md)
  - RED 단계 테스트 인벤토리와 브랜치 분담 기준

- [`08_red_test_strategy_KR.md`](./08_red_test_strategy_KR.md)
  - 현재 RED 단계 테스트 상태, 공백, 실패 테스트 전략, 우선순위 파일 정리

- [`09_red_test_plan_KR.md`](./09_red_test_plan_KR.md)
  - RED 단계의 실행 순서, 테스트 파일 계획, 브랜치 분담, 산출물 기준을 담은 테스트 계획서

## 8. 권장 읽기 순서
새 팀원이 들어왔을 때 권장 읽기 순서는 다음과 같다.

1. [`00_PRD_KR.md`](./00_PRD_KR.md)로 최종 제품 정의를 이해한다.
2. [`01_epic_KR.md`](./01_epic_KR.md)로 기능 묶음과 전달 순서를 이해한다.
3. [`02_requirements_traceability_KR.md`](./02_requirements_traceability_KR.md)로 요구사항과 구현의 연결을 본다.
4. [`03_gherkin_KR.md`](./03_gherkin_KR.md)로 실행 가능한 시나리오를 이해한다.
5. [`04_todo_KR.md`](./04_todo_KR.md)로 실제 작업 순서와 브랜치 분담을 확인한다.
6. [`06_spec_gap_log_KR.md`](./06_spec_gap_log_KR.md)로 RED 전 계약 공백을 확인한다.
7. [`07_red_test_inventory_KR.md`](./07_red_test_inventory_KR.md)로 RED 테스트 소유권과 순서를 정한다.
8. [`08_red_test_strategy_KR.md`](./08_red_test_strategy_KR.md)로 현재 테스트 트리와 RED 우선순위를 이해한다.
9. [`09_red_test_plan_KR.md`](./09_red_test_plan_KR.md)로 실제 작성 순서와 산출물 계획을 확인한다.

## 9. 단계별 문서 활용 방법
### SPEC
사용 문서:
- `00_PRD_KR.md`
- `01_epic_KR.md`
- `02_requirements_traceability_KR.md`
- `06_spec_gap_log_KR.md`
- `07_red_test_inventory_KR.md`

목적:
- 요구사항 해석 확정
- 범위 정의
- 안전한 작업 분해
- 테스트/코드 작성 전 공백 제거
- RED 테스트 우선순위와 분담 정리

### RED
사용 문서:
- `02_requirements_traceability_KR.md`
- `03_gherkin_KR.md`
- `04_todo_KR.md`
- `06_spec_gap_log_KR.md`
- `07_red_test_inventory_KR.md`
- `08_red_test_strategy_KR.md`
- `09_red_test_plan_KR.md`

목적:
- 요구사항에서 실패 테스트 도출
- 커버리지 목표 정의
- 테스트 작업 분담
- SPEC의 미해결 공백을 잘못 잠그지 않도록 방지
- 실제 현재 소스/테스트 트리에 맞춰 RED 우선순위 정렬
- 테스트 작성 순서와 산출물 기준을 실행 계획으로 확정

### GREEN
사용 문서:
- `00_PRD_KR.md`
- `03_gherkin_KR.md`
- `04_todo_KR.md`

목적:
- 기본 요구사항 테스트를 통과시키는 최소 구현
- 정확한 출력 계약 맞춤
- 핵심 명령 동작 안정화

### REFACTORING
사용 문서:
- `00_PRD_KR.md`
- `01_epic_KR.md`
- `04_todo_KR.md`

목적:
- 외부 계약 보존
- 가독성 및 유지보수성 개선
- 테스트를 green 상태로 유지한 채 구조 개선

### NEW_FEATURE
사용 문서:
- `00_PRD_KR.md`
- `02_requirements_traceability_KR.md`
- `03_gherkin_KR.md`
- `04_todo_KR.md`

목적:
- `MOD`, `certi`, 고급 옵션, 비교 검색, 논리 조합 구현
- 기본 기능 안정성 유지
- 기능과 테스트를 함께 이동

### QA
사용 문서:
- 이 폴더의 전체 문서

목적:
- 요구사항 충족 여부 검증
- 회귀 안정성 확인
- 품질 기준 확인
- 최종 보고서 및 발표 자료 준비

## 10. 품질 및 평가 초점
이 문서 세트는 루트 `README.md`의 평가 관점을 반영한다.

팀은 지속적으로 다음을 확인해야 한다.
- 주어진 요구사항을 모두 만족하는가
- production code의 가독성과 유지보수성이 개선되고 있는가
- 테스트 코드의 질이 적절한가
- 커버리지가 최소 90% 이상인가
- 커밋이 작고 리뷰 가능하게 유지되는가
- 리뷰가 Clean Code, Refactoring, TDD, Secure Coding 관점을 다루는가

## 11. 문서 흐름의 기대 산출물
이 문서들을 따라가면 팀은 다음을 만들 수 있어야 한다.
- 공유된 제품 정의
- 단계별 브랜치 실행 계획
- 테스트 가능한 acceptance 시나리오
- 요구사항 추적 체계
- 리뷰 가능한 구현 작업 단위
- QA 준비가 된 완료 기준

## 12. 최종 안내
이 폴더를 기획, 구현 정렬, 리뷰 준비를 위한 단일 문서 허브로 사용한다.

요구사항, 테스트, 구현 선택, 리뷰 코멘트가 애매할 때는 아래 순서로 추적한다.

1. source requirement file
2. `00_PRD_KR.md`
3. `02_requirements_traceability_KR.md`
4. `03_gherkin_KR.md`
5. `04_todo_KR.md`

이 원칙은 `SPEC -> RED -> GREEN -> REFACTORING -> NEW_FEATURE -> QA` 전체 흐름에서 팀 정렬을 유지하게 해준다.
