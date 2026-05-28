# 요구사항 구현·커버리지 보고서

## 1. 문서 개요
| 항목 | 내용 |
| --- | --- |
| 문서명 | 요구사항 구현·커버리지 보고서 |
| 작성 기준일 | 2026-05-27 |
| 분석 브랜치 | `REFACTORING` (`9317c55` 기준) |
| 참조 요구사항 | `requirement/Base.md`, `requirement/Further.md` |
| 참조 제품 문서 | `DOCS/00_PRD_KR.md`, `DOCS/05_README_KR.md` |
| 분석 소스 | `src/main/java/com/sec/bestreviewer` |
| 분석 테스트 | `src/test/java/com/sec/bestreviewer` |

## 2. 분석 목적
본 문서는 원문 요구사항과 PRD를 기준으로, 현재 Java 소스가 **기능적으로 얼마나 구현되었는지**와 **자동 테스트로 얼마나 검증되었는지**를 한눈에 파악할 수 있도록 정리한다.

분석 관점은 다음 두 축이다.
- **구현 커버리지**: 요구사항에 대응하는 코드 경로가 존재하고 동작 계약을 따르는가
- **검증 커버리지**: 단위/통합 테스트가 해당 요구사항을 의미 있게 고정하고 있는가

## 3. 분석 방법
1. `Base.md` / `Further.md` / `00_PRD_KR.md`의 기능·옵션·출력·비기능 요구를 `Req ID` 단위로 분해했다.
2. `02_requirements_traceability_KR.md`의 추적 매트릭스를 기준으로 구현 클래스와 테스트 클래스를 대조했다.
3. `src/main/java`의 실행 경로(`EmployeeManagement` → `CommandParser` → `CommandFactory` → `CommandExecutor` → `EmployeeStoreImpl` → `ResultStringFormatter`)를 따라 실제 동작 여부를 확인했다.
4. `mvn test` 전체 회귀(125건, 실패 0건) 결과와 테스트 파일별 책임 범위를 함께 반영했다.

### 상태 기준
| 구현 상태 | 의미 |
| --- | --- |
| 완료 | 요구사항의 핵심 동작이 코드에 존재하고, 알려진 큰 결함 없이 동작한다 |
| 부분 | 기능은 있으나 placeholder·약한 검증·기술 부채·경계 케이스 미흡이 남아 있다 |
| 미구현 | 요구사항을 만족하는 코드 경로가 없다 |

| 검증 상태 | 의미 |
| --- | --- |
| 충분 | store/command/CLI 경로 중 최소 한 곳에서 의미 있는 assert가 있다 |
| 부분 | field 단위 또는 mock 기반 검증만 있고, 실경로 검증이 약하다 |
| 없음 | 자동 테스트 근거가 거의 없다 |

## 4. 종합 요약
### 4.1 핵심 지표
| 구분 | 완료 | 부분 | 미구현 | 환산 점수 |
| --- | ---: | ---: | ---: | ---: |
| 기본 요구사항 (`B-01` ~ `B-21`) | 17 | 4 | 0 | **90.5%** |
| 추가 요구사항 (`F-01` ~ `F-23`) | 18 | 5 | 0 | **89.1%** |
| 비기능 요구사항 (`NFR-01` ~ `NFR-04`) | 2 | 2 | 0 | **75.0%** |
| **전체 기능 요구사항 (44건)** | **35** | **9** | **0** | **약 89.8%** |

| 검증 축 | 충분 | 부분 | 없음 | 환산 점수 |
| --- | ---: | ---: | ---: | ---: |
| 기본 요구사항 검증 | 18 | 3 | 0 | **92.9%** |
| 추가 요구사항 검증 | 18 | 5 | 0 | **89.1%** |
| 비기능 요구사항 검증 | 2 | 1 | 1 | **62.5%** |
| **전체 검증 (44건)** | **38** | **9** | **1** | **약 90.9%** |

### 4.2 한 줄 결론
현재 코드베이스는 **기본·추가 요구사항의 핵심 기능은 대부분 구현되어 있으며**, `ADD` / `DEL` / `SCH` / `MOD`, `-p`, 부분 필드, 비교 검색, `AND`/`OR`, `certi` 출력까지 실사용 가능한 수준이다.  
다만 **10만 건 성능 요구**, **전화번호 부분 필드 실경로 검증**, **부분 필드 placeholder 기반 조건 해석**, **대규모 통합 시나리오**는 아직 약점으로 남아 있다.

### 4.3 현재 테스트 자산
- 전체 테스트: **125건**
- 결과: **Failures 0, Errors 0**
- 비활성(`@Disabled`) 테스트: **0건**
- 최근 보강: `EmployeeManagementTest`, `AndOrParameterTest`, `ResultStringFormatterTest`

## 5. 실행 아키텍처와 구현 범위
### 5.1 현재 구현된 실행 경로
```text
EmployeeManagement
  -> CommandReader (입력 파일)
  -> CommandParser / TokenGroup (명령 파싱)
  -> CommandFactory (조건 정규화 및 Command 생성)
  -> CommandExecutor
  -> AddCommand / DeleteCommand / SearchCommand / ModCommand / CountCommand
  -> EmployeeStoreImpl
  -> ResultStringFormatter
  -> Printer (출력 파일)
```

### 5.2 기능 그룹별 구현·검증 요약
| 기능 그룹 | 주요 구현 위치 | 구현 | 검증 | 비고 |
| --- | --- | --- | --- | --- |
| CLI 입출력 | `EmployeeManagement`, `CommandReader`, `Printer` | 완료 | 충분 | malformed command 출력 검증 추가됨 |
| 명령 파싱/생성 | `CommandParser`, `TokenGroup`, `CommandFactory` | 부분 | 부분 | 최소 토큰 검증 미적용, placeholder 조건 생성 |
| 기본 명령 | `AddCommand`, `DeleteCommand`, `SearchCommand` | 완료 | 충분 | `CommandExecutorTest` 중심 |
| 수정 명령 | `ModCommand`, `EmployeeStoreImpl.modify()` | 완료 | 충분 | 수정 전 레코드 반환 구현 |
| 출력 포맷 | `ResultStringFormatter` | 완료 | 충분 | 정렬/`NONE`/max 5 직접 테스트 추가 |
| 이름 옵션 | `Name`, `SecondaryOptionEnum`, `EmployeeStoreImpl` | 완료 | 충분 | `EmployeeStoreImplNameTest` |
| 생일 옵션 | `Birthday`, `CommandFactory` | 부분 | 충분 | store/command 경로는 강함, factory placeholder 잔존 |
| 전화번호 옵션 | `PhoneNumber`, `CommandFactory` | 완료 | 충분 | `EmployeeStoreImplPhoneNumberTest`로 실경로 통합 보강 |
| 경력/자격 비교 | `CareerLevel`, `Certi` | 완료 | 충분 | `CertiTest`, `EmployeeStoreImplCareerLevelTest`, `EmployeeStoreImplCertiTest` |
| AND/OR 조합 | `CombinationEnum`, command/store | 완료 | 충분 | fixture 기반 통합 출력 고정 |
| 대량 처리 | `EmployeeStoreImpl` (`ArrayList`) | 부분 | 없음 | 구조상 가능하나 10만 건 테스트 없음 |

## 6. 기본 요구사항 커버리지 (`Base.md`)
| Req ID | 요구사항 요약 | 구현 | 검증 | 근거 |
| --- | --- | --- | --- | --- |
| B-01 | 사원 DB 관리 | 완료 | 충분 | `EmployeeStoreImpl`, `Employee` |
| B-02 | 5개 기본 필드 | 완료 | 충분 | `Employee`, field 패키지 |
| B-03 | 사원번호 8자리·입사년도 | 완료 | 충분 | `EmployeeNumber`, `EmployeeNumberTest` |
| B-04 | 성명 first/last | 완료 | 충분 | `Name`, `EmployeeStoreImplNameTest` |
| B-05 | 경력단계 CL1~CL4 | 완료 | 충분 | `CareerLevel`, store/command 테스트 |
| B-06 | 전화번호 010 형식 | 완료 | 부분 | `PhoneNumber`, `PhoneNumberTest` |
| B-07 | 생년월일 YYYYMMDD | 완료 | 충분 | `Birthday`, `BirthdayTest` |
| B-08 | 고정 명령 형식·옵션 | 부분 | 부분 | `CommandParser` 최소 길이 검증 주석 처리 |
| B-09 | ADD 등록 | 완료 | 충분 | `AddCommand`, `AddCommandTest` |
| B-10 | ADD 중복 허용·non-null | 부분 | 부분 | field validation은 있으나 ADD 통합 검증 얕음 |
| B-11 | DEL 일괄 삭제 | 완료 | 충분 | `DeleteCommand`, `CommandExecutorTest` |
| B-12 | SCH 일괄 검색 | 완료 | 충분 | `SearchCommand`, `CommandExecutorTest` |
| B-13 | `-p`는 옵션1만 | 부분 | 충분 | `OptionParser`, executor 테스트 |
| B-14 | `-p` 시 레코드 줄 출력 | 완료 | 충분 | `ResultStringFormatter` |
| B-15 | 입사년도 빠른 순 정렬 | 완료 | 충분 | `ResultStringFormatter`, `CommandExecutorTest` |
| B-16 | 최대 5건 출력 | 완료 | 충분 | `ResultStringFormatterTest` |
| B-17 | 비 `-p` 시 건수 출력 | 완료 | 충분 | `CommandExecutorTest` |
| B-18 | no-match 시 `NONE` | 완료 | 충분 | executor/formatter 테스트 |
| B-19 | ADD는 출력 없음 | 완료 | 충분 | `AddCommandTest` |
| B-20 | 10만 건 처리 | 부분 | 없음 | `ArrayList` 기반이지만 전용 테스트 없음 |
| B-21 | txt 입출력·main 인자 | 완료 | 충분 | `EmployeeManagementTest`, `CommandReaderTest` |

**기본 요구사항 소계**: 구현 90.5% / 검증 92.9%

## 7. 추가 요구사항 커버리지 (`Further.md`)
| Req ID | 요구사항 요약 | 구현 | 검증 | 근거 |
| --- | --- | --- | --- | --- |
| F-01 | MOD 명령 | 완료 | 충분 | `ModCommand` |
| F-02 | 조건 일치 전체 수정 | 완료 | 충분 | `EmployeeStoreImpl.modify()` |
| F-03 | 한 번에 한 컬럼만 수정 | 완료 | 충분 | `CommandFactory`, `ModCommand` |
| F-04 | 사원번호 수정 불가 | 완료 | 충분 | store 예외, factory 검증 |
| F-05 | MOD `-p`는 수정 전 출력 | 완료 | 충분 | `modify()` pre-change snapshot |
| F-06 | certi 컬럼 | 완료 | 충분 | `Certi`, `Employee` |
| F-07 | 출력에 certi 포함 | 완료 | 충분 | `ResultStringFormatter` |
| F-08 | 이름 `-f` | 완료 | 충분 | `Name`, `EmployeeStoreImplNameTest` |
| F-09 | 성 `-l` | 완료 | 충분 | 동일 |
| F-10 | 전화 `-m` | 완료 | 충분 | `EmployeeStoreImplPhoneNumberTest` |
| F-11 | 전화 `-l` | 완료 | 충분 | 동일 |
| F-12 | 생일 `-y` | 완료 | 충분 | `Birthday`, birthday integration |
| F-13 | 생일 `-m` | 부분 | 충분 | store 경로는 있으나 factory placeholder 의존 |
| F-14 | 생일 `-d` | 부분 | 충분 | 동일 |
| F-15 | SCH 비교 `-g/-ge/-s/-se` | 완료 | 충분 | `TertiaryOptionEnum`, store/field tests |
| F-16 | 필드별 ordering 규칙 | 완료 | 충분 | `Field.compare`, career/certi tests |
| F-17 | 전화 010 prefix 비교 제외 | 완료 | 부분 | `PhoneNumber.compare` 구현, 통합 테스트 약함 |
| F-18 | AND `-a` | 완료 | 충분 | `EmployeeStoreImpl`, `AndOrParameterTest` |
| F-19 | OR `-o` | 완료 | 충분 | 동일 |
| F-20 | OR 중복 제거 | 완료 | 충분 | employee 단위 stream filter |
| F-21 | DEL AND/OR | 완료 | 충분 | `DeleteCommand`, fixture 테스트 |
| F-22 | MOD AND/OR | 부분 | 부분 | 구현은 있으나 factory TODO·통합 검증 제한적 |
| F-23 | SCH AND/OR | 완료 | 충분 | `SearchCommand`, fixture 테스트 |

**추가 요구사항 소계**: 구현 89.1% / 검증 89.1%

## 8. 비기능 요구사항 커버리지
| Req ID | 요구사항 | 구현 | 검증 | 판단 |
| --- | --- | --- | --- | --- |
| NFR-01 | 10만 건 성능 | 부분 | 없음 | 메모리 리스트 구조는 단순하나 부하 테스트 없음 |
| NFR-02 | 결정적 출력 | 완료 | 충분 | formatter/command 테스트 |
| NFR-03 | 입력 계약 안정성 | 부분 | 부분 | malformed command는 검증, 빈 줄·약한 parser 검증 잔존 |
| NFR-04 | 테스트 가능성 | 완료 | 충분 | 125개 자동 테스트, txt 비교 구조 |

## 9. 명령·옵션별 상세 커버리지
### 9.1 핵심 명령
| 명령 | 요구 기능 | 구현 상태 | 대표 테스트 |
| --- | --- | --- | --- |
| `ADD` | 사원 등록, 출력 없음 | 완료 | `AddCommandTest` |
| `DEL` | 조건 삭제, `-p`/count/`NONE` | 완료 | `CommandExecutorTest`, `AndOrParameterTest` |
| `SCH` | 조건 검색, 비교 옵션, `-p`/count/`NONE` | 완료 | `CommandExecutorTest`, `EmployeeStoreImplNameTest`, `EmployeeStoreImplBirthdayTest` |
| `MOD` | 조건 수정, 수정 전 출력, 1필드 제한 | 완료 | `EmployeeStoreImplModifyCommandTest`, `CommandModTest` |
| `CNT` | (요구 범위 외) | 완료 | `CommandExecutorTest` |

### 9.2 옵션
| 옵션 | 적용 명령 | 구현 | 검증 | 메모 |
| --- | --- | --- | --- | --- |
| `-p` | DEL/SCH/MOD | 완료 | 충분 | 정렬·max 5·NONE |
| `-f`, `-l` | name | 완료 | 충분 | first/last name |
| `-m`, `-l` | phoneNum | 부분 | 부분 | factory placeholder 의존 |
| `-y`, `-m`, `-d` | birthday | 부분 | 충분 | month/day는 factory 보정 문자열 사용 |
| `-g`, `-ge`, `-s`, `-se` | SCH | 완료 | 충분 | store/field 비교 |
| `-a`, `-o` | DEL/SCH/MOD | 완료 | 충분 | AND/OR fixture |

## 10. 테스트 커버리지 맵
| 테스트 클래스 | 주요 검증 범위 | 관련 요구사항 |
| --- | --- | --- |
| `CommandExecutorTest` | DEL/SCH `-p`, count, NONE, 정렬 | B-11~B-18 |
| `EmployeeManagementTest` | CLI 인자, 옵션 출력, malformed command | B-21, NFR-03 |
| `AndOrParameterTest` | AND/OR fixture, 파라미터 파싱 | F-18~F-23 |
| `EmployeeStoreImplNameTest` | 이름 부분/비교 검색 | B-04, F-08, F-09, F-15 |
| `EmployeeStoreImplBirthdayTest` | 생일 연도/비교 검색 | B-07, F-12~F-15 |
| `EmployeeStoreImplModifyCommandTest` | 수정·AND 수정 | F-01~F-05, F-22 |
| `EmployeeStoreImplCareerLevelTest` | CL 비교 검색 | B-05, F-15 |
| `ResultStringFormatterTest` | NONE, 정렬, max 5, 포맷 | B-14~B-18, F-07 |
| `PhoneNumberTest` | 전화 파싱·부분 equals | B-06, F-10, F-11 |
| `CertiTest` | certi ordering | F-06, F-15 |
| `CommandParserTest` | AND 토큰 분리 | B-08, F-18 |
| `CombinationEnumTest` | 잘못된 조합 옵션 예외 | B-08 |

### 10.1 테스트 공백
| 공백 | 영향 요구사항 | 현재 상태 |
| --- | --- | --- |
| 10만 건 성능 테스트 | B-20, NFR-01 | 미검증 |
| `phoneNum -m/-l` 실경로 통합 | F-10, F-11, F-17 | field unit만 존재 |
| `certi` 명령 수준 SCH 통합 | F-06, F-15 | field/store 일부만 |
| 대용량 approval 통합 | B-21, 전체 회귀 | 소규모 fixture 위주 |
| `CommandFactory` placeholder 제거 검증 | F-08~F-14 | 리팩토링 전 기술 부채 |

## 11. 주요 갭과 기술 부채
### 11.1 구조적 갭
1. **`CommandFactory.getConditionMapFromParams()`의 placeholder 규칙**
   - `"HOHAN "`, `"010-0000-"`, `"9999"` 등 보정 문자열로 부분 필드 검색을 성립시키고 있다.
   - 기능은 동작하지만 요구사항 semantics를 코드가 직접 표현하지 못해 유지보수 리스크가 크다.

2. **`CommandParser` 검증 약함**
   - 최소 토큰 수 검증이 주석 처리되어 있다.
   - 빈 줄 입력 시 `wrong command : ` 형태가 fixture에도 남아 있다.

3. **대량 데이터 요구 미검증**
   - `EmployeeStoreImpl`은 단순 `ArrayList`이므로 10만 건 처리는 구조상 가능해 보이나, 성능·메모리·회귀 테스트가 없다.

4. **전화번호 부분 필드 실경로 테스트 부족**
   - `PhoneNumberTest`는 field 단위만 검증한다.
   - parser → factory → command → store → output 경로는 약하다.

### 11.2 최근 보강으로 해소된 항목
| 이전 공백 | 현재 상태 |
| --- | --- |
| `EmployeeManagement` malformed command 미검증 | `malformedCommandIsWrittenToOutputFile` 추가 |
| `ResultStringFormatter` 직접 회귀 없음 | `ResultStringFormatterTest` 추가 |
| `AndOrParameterTest` no-op 성격 | fixture 출력 assert로 강화 |
| 비활성 approval 테스트 | `testOptions` 활성화 |

## 12. 단계별 릴리스 관점 평가
PRD 17장 기준 현재 완성도는 다음과 같다.

| 단계 | 범위 | 완성도 | 판단 |
| --- | --- | ---: | --- |
| Phase 1 Base | ADD/DEL/SCH, `-p`, 정렬/NONE/count | **95%** | 실사용 가능 |
| Phase 2 Extended | MOD, certi, 부분 필드, 비교, AND/OR | **88%** | 핵심 기능 구현됨, 일부 경로는 부채 잔존 |
| QA/Scale | 10만 건, 대규모 통합 | **20%** | 문서·구조만 있고 검증 없음 |

## 13. 권장 후속 작업
### 13.1 우선순위 높음
1. `phoneNum -m/-l` 실경로 통합 테스트 추가
2. 10만 건 ADD 후 SCH/DEL/MOD 성능·정확성 테스트 추가
3. `CommandFactory` placeholder 제거 및 typed condition 모델 도입

### 13.2 REFACTORING 단계 연계
`DOCS/11_refactoring_plan_KR.md`와 연계하면, 다음 순서가 가장 안전하다.
1. 회귀 테스트 유지 (`현재 125건 GREEN`)
2. `CommandParser` / `CommandFactory` 조건 해석 분리
3. `EmployeeStoreImpl` 수정·검색 경로 단순화
4. 출력/경계 계층 정리

## 14. 결론
현재 `src` 기준 구현 상태는 **요구사항의 약 90%가 코드에 반영**되어 있고, **자동 테스트로도 약 91% 수준의 요구사항이 근거를 가진다**고 볼 수 있다.

실무적으로는 다음과 같이 정리할 수 있다.
- **기본 명령과 출력 계약**: 사용 가능
- **확장 옵션과 MOD/AND/OR**: 대체로 사용 가능
- **대량 처리·전화번호 실경로·parser 견고성**: 추가 검증 필요

따라서 본 프로젝트는 **기능 구현 단계는 상당히 진행되었고**, 다음 단계는 **REFACTORING + QA(10만 건/실경로 회귀)** 에 집중하는 것이 적절하다.

## 15. 참고 문서
- `DOCS/00_PRD_KR.md`
- `DOCS/02_requirements_traceability_KR.md`
- `DOCS/10_green_implementation_report_KR.md`
- `DOCS/11_refactoring_plan_KR.md`
- `report/04_refactoring_analysis_report.md`
