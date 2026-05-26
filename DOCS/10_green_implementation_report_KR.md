# GREEN 구현 보고서

## 1. 실패 테스트 원인 요약
- `CombinationEnum.fromCombination()`이 잘못된 조합 옵션을 예외로 막지 않고 `NONE`으로 흡수해서 RED 의도와 입력 검증 규칙을 깨고 있었다.
- `OptionParser`는 옵션 목록 길이가 정확히 2 또는 3일 때만 동작했다. 그 결과 `buildSingleCommand()`를 직접 쓰는 테스트와 축약 옵션 경로에서 `-p`가 인식되지 않아 `DEL`/`SCH`가 `NONE`으로 떨어졌다.
- `DEL`/`SCH`/`MOD` 단일 조건 경로가 서브필드 인덱스가 없어도 항상 인덱스 오버로드만 호출했다. 기존 테스트 더블은 기본 오버로드를 스텁하고 있어 결과가 빈 리스트로 처리되었다.
- `EmployeeManagement [input file] [output file]` 실행 경로와 `ResultStringFormatter`의 정렬, 최대 5건, `NONE` 규칙은 현재 구현으로 이미 Base 요구사항과 일치했다.

## 2. 구현 대상 목록
- `-p` 출력 옵션 인식 안정화
- 기본 `DEL`/`SCH` count 출력과 `NONE` 처리 정상화
- 기본 `DEL`/`SCH`의 `-p` 출력 경로 정상화
- 잘못된 조합 옵션 입력 검증 강화
- Base 범위 외 기능 추가 없이 기존 확장 경로 회귀 방지

## 3. 수정 전략
- 실제 CLI 입력의 3칸 옵션 구조는 그대로 유지하고, 테스트/축약 호출에서만 compact 옵션 해석을 허용했다.
- 서브필드가 없는 기본 조건은 기본 오버로드를 우선 호출해 Base 동작을 맞추고, 필요할 때만 인덱스 오버로드로 보완했다.
- 정렬, 최대 5건, `NONE`, 입출력 파일 실행 경로는 이미 구현되어 있으므로 변경하지 않고 회귀 테스트로만 검증했다.
- `Further.md` 기능을 새로 구현하지 않고, Base 동작을 깨지 않도록 최소 수정만 적용했다.

## 4. 파일별 변경 계획
- `src/main/java/com/sec/bestreviewer/util/OptionParser.java`
  - 3칸 옵션은 위치 기반으로 해석하고, 축약 옵션 목록은 타입 기반으로 보완하도록 수정
- `src/main/java/com/sec/bestreviewer/command/DeleteCommand.java`
  - 단일 조건 기본 삭제 경로에서 기본 오버로드 우선 사용
- `src/main/java/com/sec/bestreviewer/command/SearchCommand.java`
  - 단일 조건 기본 검색 경로에서 기본 오버로드 우선 사용
- `src/main/java/com/sec/bestreviewer/command/ModCommand.java`
  - Base 구현과 직접 관련된 기능 추가는 아니지만, 동일한 기본 오버로드 호환 전략으로 기존 확장 회귀 방지
- `src/main/java/com/sec/bestreviewer/command/CombinationEnum.java`
  - 잘못된 조합 옵션에 대해 즉시 `IllegalArgumentException` 발생
- 변경 없이 검증만 수행한 파일
  - `src/main/java/com/sec/bestreviewer/EmployeeManagement.java`
  - `src/main/java/com/sec/bestreviewer/util/ResultStringFormatter.java`
  - `src/main/java/com/sec/bestreviewer/CommandReader.java`

## 5. 4인 분담안
- `A_01_GREEN`
  - `OptionParser`, `CombinationEnum` 중심 입력 검증과 옵션 해석
- `A_02_GREEN`
  - `DEL` 기본 경로, count 출력, `NONE`, `-p` 출력 확인
- `A_03_GREEN`
  - `SCH` 기본 경로, count 출력, `NONE`, 정렬/최대 5건 회귀 확인
- `A_04_GREEN`
  - 실행 경로 검증, 문서화, 전체 회귀 테스트와 산출물 정리

## 6. 검증
- 집중 회귀 테스트
  - `mvn "-Dtest=CommandExecutorTest,CombinationEnumTest,CommandModTest" test`
  - 결과: 성공
- 전체 회귀 테스트
  - `mvn test`
  - 결과: `Tests run: 121, Failures: 0, Errors: 0, Skipped: 2`
- 정적 확인
  - 수정 파일 대상 lint 오류 없음
- Base 요구사항 관점 확인
  - `CommandExecutorTest` 기준 `DEL`/`SCH`의 `-p`, count, `NONE`, 정렬, 최대 5건 출력 통과
  - `EmployeeManagement`는 입력/출력 파일 인자를 받아 결과 파일을 생성하는 경로 유지

## 7. 남은 리스크
- `CommandParser.getValidList()`는 여전히 공백 `" "` 입력 형식에 의존하므로, 향후 옵션 위치 검증을 더 명시적으로 다듬는 것이 좋다.
- `CommandFactory.getConditionMapFromParams()`에는 추가 기능 대응용 하드코딩 분기들이 남아 있어 REFACTORING 단계에서 정리가 필요하다.
- `EmployeeManagementTest`의 승인형 통합 테스트 2건은 아직 `@Disabled` 상태라 CLI 종단 회귀 신뢰도는 제한적이다.
- Maven 빌드 경고(compiler plugin version, encoding)는 현재 GREEN 범위 밖이지만 QA 전에는 정리하는 편이 안전하다.
