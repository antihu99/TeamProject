# NEW_FEATURE 단계 구현 계획서

## 1. 추가 요구사항 요약
`Further.md` 기준 신규 기능은 다음 6개 묶음으로 정리된다.

| 묶음 | 요구사항 | 현재 상태 |
| --- | --- | --- |
| NF-01 | `certi` 컬럼 및 출력 반영 | 구현 완료, 명령 경로 테스트 보강 |
| NF-02 | `MOD` 명령 및 수정 전 출력 | 구현 완료 |
| NF-03 | 이름/전화번호/생년월일 세부 옵션 | 구현 완료, 전화번호 실경로 테스트 보강 |
| NF-04 | `SCH` 비교 옵션 `-g/-ge/-s/-se` | 구현 완료 |
| NF-05 | `AND`/`OR` 조합 옵션 | 구현 완료 |
| NF-06 | 출력 형식(`certi` 포함, max 5, NONE) | 구현 완료 |

`DOCS/12_requirements_coverage_report_KR.md` 기준으로 Further 요구사항은 이미 약 89% 구현되어 있었으므로, NEW_FEATURE 단계는 **미검증 경로 보강**과 **조건 해석 가독성 개선**에 집중한다.

## 2. 기능별 구현 순서
1. **certi 데이터 모델 및 출력** — `Employee`, `ADD`, `ResultStringFormatter`
2. **MOD 명령** — 조건 검색, 1필드 수정, 수정 전 레코드 반환
3. **이름/생일 세부 옵션** — `-f/-l`, `-y/-m/-d`
4. **전화번호 세부 옵션** — `-m/-l` 실경로 통합 테스트
5. **SCH 비교 옵션** — `-g/-ge/-s/-se`, `certi` ordering
6. **AND/OR 조합** — DEL/SCH/MOD 복합 조건 및 중복 제거

## 3. Base.md와 충돌 가능 지점
| 충돌 지점 | 설명 | 대응 |
| --- | --- | --- |
| 출력 스키마 확장 | Base 출력에 `certi` 없음, Further는 포함 | formatter 전 경로에 `certi` 유지 |
| `MOD` vs `DEL/SCH` | 수정 전 스냅샷 반환 필요 | `modify()`가 변경 전 리스트 반환 |
| 부분 필드 vs 전체 필드 | Base는 전체 필드 exact match | subfieldIndex + placeholder 조건값 |
| 옵션2/3 의미 확장 | birthday `-m` vs phone `-m` | `SecondaryOptionEnum` field-aware index |
| AND/OR 중복 | Base 단일 조건 | stream filter로 employee 단위 dedup |

## 4. 파일별 구현 계획
| 파일 | 작업 |
| --- | --- |
| `CommandFactory.java` | `buildSearchValue()`로 부분 필드 조건 생성 규칙 명시화 |
| `EmployeeStoreImplPhoneNumberTest.java` | phone `-m/-l`, 비교, MOD, OR 통합 테스트 신설 |
| `EmployeeStoreImplCertiTest.java` | certi 비교/수정 명령 경로 테스트 신설 |
| `DOCS/12_requirements_coverage_report_KR.md` | NEW_FEATURE 이후 커버리지 갱신 권장 |
| `report/05_new_feature_analysis_report.md` | 구현·검증 결과 보고 |

## 5. 4인 분담안
| 브랜치 | 담당 | 범위 |
| --- | --- | --- |
| `A_01_NEW_FEATURE` | certi + ADD/출력 | `Certi`, `Employee`, formatter, certi 통합 테스트 |
| `A_02_NEW_FEATURE` | MOD | `ModCommand`, `modify()`, MOD AND/OR |
| `A_03_NEW_FEATURE` | 세부 옵션 | name/phone/birthday partial-field, `CommandFactory` |
| `A_04_NEW_FEATURE` | 비교·조합·회귀 | tertiary options, AND/OR fixture, 전체 `mvn test` |

## 6. 테스트/회귀 검증 계획
- 신규: `EmployeeStoreImplPhoneNumberTest`, `EmployeeStoreImplCertiTest`
- 회귀: `CommandExecutorTest`, `EmployeeStoreImplNameTest`, `EmployeeStoreImplBirthdayTest`, `AndOrParameterTest`
- 전체: `mvn test` (목표: Failures 0)

## 7. 남은 리스크
- `CommandFactory` placeholder 방식은 동작하지만 장기적으로 typed condition 모델로 대체 필요
- 10만 건 성능 테스트는 NEW_FEATURE 범위 밖
- `Field.compareString` 디버그 출력은 테스트 로그 노이즈 유발
