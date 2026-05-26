# Git Command Summary

## 요약
이번 세션에서는 저장소 루트 재배치, 원격 연결 확인, 브랜치 동기화, `SPEC` 브랜치 체크아웃, 커밋 및 원격 푸시가 수행되었다.

## 사용한 Git 명령어 목록
중복 없이 정리한 주요 Git 명령은 아래와 같다.

```text
git status -sb
git status --short --branch
git remote -v
git remote show origin
git remote set-url origin https://github.com/antihu99/TeamProject.git
git branch --show-current
git branch --list SPEC
git branch -a -vv
git branch -vv
git fetch origin
git fetch --all --prune
git pull --ff-only origin main
git switch -c SPEC --track origin/SPEC
git rev-parse --show-toplevel
git diff HEAD
git diff --cached --stat
git log --oneline -5
git add --
git commit -F -
git push origin SPEC
```

## Git 작업 흐름
1. 현재 폴더가 Git 저장소인지 확인
2. 하위 저장소의 원격과 브랜치 상태 확인
3. 상위 폴더로 저장소 루트 이동 후 원격 재확인
4. `main` 브랜치 최신 상태 동기화
5. 원격 브랜치 정보 갱신
6. 로컬 `SPEC` 브랜치 생성 및 추적 연결
7. 문서 산출물 전체 스테이징
8. 커밋 생성
9. `origin/SPEC`로 푸시

## 최종 결과
- 현재 작업 브랜치: `SPEC`
- 원격 추적 브랜치: `origin/SPEC`
- 생성 커밋: `66e03dc`
- 커밋 메시지: `#1_AddProjectPlanningDocs`
