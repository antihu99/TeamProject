# Git Command Summary

## 요약
이번 세션에서는 저장소 연결 및 초기 동기화, `SPEC`/`RED`/`GREEN` 브랜치 작업, 문서 커밋, `RED -> main` 반영, `GREEN` 브랜치 생성과 푸시, `main` 기준 `REFACTORING` 브랜치 생성과 원격 추적 연결, 그리고 이후 REFACTORING 단계의 원격/로컬 동기화 재확인까지 수행되었다.

## 사용한 Git 명령어 목록
중복 없이 정리한 주요 Git 명령은 아래와 같다.

```text
git status -sb
git status --short --branch
git remote -v
git remote show origin
git remote set-url origin https://github.com/antihu99/TeamProject.git
git rev-parse --is-inside-work-tree
git branch --show-current
git branch --list SPEC
git branch --list GREEN
git branch --list REFACTORING --verbose --no-abbrev
git branch --list main RED --verbose --no-abbrev
git branch --all --verbose --no-abbrev
git branch -a -vv
git branch -vv
git fetch origin
git fetch --prune origin
git fetch --all --prune
git rev-list --left-right --count origin/REFACTORING...HEAD
git pull --ff-only origin main
git switch -c SPEC --track origin/SPEC
git switch REFACTORING
git checkout main
git checkout -b GREEN
git branch REFACTORING main
git merge RED
git stash push -u -m "green-wip-before-main-merge" --
git stash list
git stash pop
git rev-parse --show-toplevel
git diff HEAD
git diff --cached --stat
git diff --stat
git log --oneline -5
git log --oneline -n 10
git log --oneline --decorate -n 12
git log --oneline --decorate --graph --all -n 20
git add --
git commit -F -
git commit -m
git push origin SPEC
git push origin RED
git push origin main
git push -u origin GREEN
git push -u origin REFACTORING
git worktree list
```

## Git 작업 흐름
1. 현재 저장소의 원격, 브랜치, 동기화 상태를 확인
2. `SPEC` 브랜치를 생성하고 추적 연결 후 커밋/푸시
3. `RED` 브랜치 기반 문서 작업을 커밋/푸시
4. GREEN 변경을 stash로 보관
5. `RED`를 `main`에 fast-forward 반영
6. 갱신된 `main`에서 `GREEN` 브랜치 생성
7. stash 복원 후 GREEN 변경 커밋
8. `main`, `GREEN`을 원격에 푸시
9. `git fetch --all --prune`, `git branch -vv`로 GREEN 단계 최종 동기화 확인
10. `main` 기준으로 `REFACTORING` 브랜치를 생성
11. `origin/REFACTORING`에 새 브랜치를 푸시하고 upstream tracking 연결
12. 현재 작업 브랜치를 `REFACTORING`로 전환
13. `git fetch --all --prune`와 ahead/behind 확인으로 `REFACTORING`의 원격/로컬 동기화 상태를 재검증

## 최종 결과
- 현재 작업 브랜치: `REFACTORING`
- 원격 추적 브랜치: `origin/REFACTORING`
- `main` 최신 동기화 기준 커밋: `e354a49`
- `REFACTORING` 생성 기준 커밋: `e354a49`
- `REFACTORING`은 `#9_SaveGreenReportAndPromptLog` 커밋에서 시작했다
- 최근 동기화 확인 시점 기준 `origin/REFACTORING...HEAD` ahead/behind: `0/0`
