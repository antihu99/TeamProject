# Git Command Summary

## 요약
이번 세션에서는 저장소 연결 및 초기 동기화, `SPEC`/`RED`/`GREEN` 브랜치 작업, 문서 커밋, `RED -> main` 반영, `GREEN` 브랜치 생성, 커밋/푸시, 최종 동기화 확인이 수행되었다.

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
git branch --list GREEN
git branch --list main RED --verbose --no-abbrev
git branch -a -vv
git branch -vv
git fetch origin
git fetch --all --prune
git pull --ff-only origin main
git switch -c SPEC --track origin/SPEC
git checkout main
git checkout -b GREEN
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
9. `git fetch --all --prune`, `git branch -vv`로 최종 동기화 확인

## 최종 결과
- 현재 작업 브랜치: `GREEN`
- 원격 추적 브랜치: `origin/GREEN`
- `main` 최신 원격 반영 커밋: `faea5e6`
- `GREEN` 최신 커밋: `0494c56`
- `GREEN` 커밋 메시지: `#8_ApplyGreenBaseCommandFixes`
