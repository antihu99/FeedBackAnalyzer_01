# Git 명령 기록 — Feedback Analyzer

> `작업규칙.TXT`: Agent 대화 중 사용된 git 명령만 (**중복 제거**)  
> **최종 갱신**: 2026-05-22

---

## 요약

| 항목 | 내용 |
|------|------|
| 작업 디렉터리 | `d:\Vs_workplace\Java_project\FeedBackAnalyzer_01` |
| 현재 브랜치 (기록 시점) | `GREEN` |
| 원격 | `https://github.com/antihu99/FeedBackAnalyzer_01.git` |
| SPEC 커밋 | `a306870` → `9b03001` |
| RED 커밋 | `e6c9b09` → `e07ca6b` |
| GREEN 커밋 | `87136db` → `c9b926a` (+ 문서화) |
| A-01 HEAD | `e07ca6b` (GREEN PR 대기) |
| PR | #1 SPEC→A-01 · #2 RED→A-01 · #3 A-01→main · **#4 GREEN→A-01** |

---

## 커밋 이력 (SPEC)

| 해시 | 메시지 | push |
|------|--------|------|
| `a306870` | SPEC 단계 진행 | `git push -u origin SPEC` |
| `9b03001` | #SPEC 단계 : PRD, TODO, 작업 시나이오 작성 | `git push origin SPEC` |

---

## 커밋 이력 (RED)

| 해시 | 메시지 | push |
|------|--------|------|
| `e6c9b09` | Red 단계진행을 위한 준비 | `git push -u origin RED` |
| `e07ca6b` | RED 단계 진행: TEST_PLAN + JUnit + JaCoCo 90.9% | `git push origin RED` |

**머지**: `git merge RED` on `A-01` → `git push origin A-01`

---

## 커밋 이력 (GREEN)

| 해시 | 메시지 | push |
|------|--------|------|
| `87136db` | GREEN: TC-NEUTRAL-01/02 neutral filter alignment (FR-09) | `git push origin GREEN` |
| `c9b926a` | GREEN: report/01_GREEN_bugfix_report.md | `git push origin GREEN` |
| *(문서화)* | GREEN: prompting·PCTF 문서화 | `git push origin GREEN` |

**PR**: `GREEN` → `A-01` (#4, `origin/A-01..origin/GREEN` 2커밋+문서)

---

## 사용된 명령 (실행 순, 중복 제거)

```bash
cd "d:\Vs_workplace\Java_project\FeedBackAnalyzer_01"

# --- 조회 ---
git status
git status -sb
git branch
git branch -a
git branch -vv
git remote -v
git remote get-url origin
git fetch origin
git rev-parse HEAD
git rev-parse origin/A-01
git rev-parse origin/SPEC
git rev-parse origin/RED
git log --oneline -1 HEAD
git log --oneline -3
git log --oneline -5
git log origin/A-01..origin/RED --oneline
git log origin/main..origin/A-01 --oneline
git diff --stat origin/A-01...HEAD
git ls-remote --heads origin RED
git stash list

# --- A-01 ---
git checkout -b A-01
git push -u origin A-01
git checkout A-01
git pull origin A-01

# --- SPEC → A-01 ---
git merge SPEC -m "Merge branch 'SPEC' into A-01"
git push origin A-01

# --- SPEC ---
git checkout -b SPEC
git add docs/
git commit -m "SPEC 단계 진행"
git push -u origin SPEC
git add docs/ prompting/ report/ .cursorrules tdd_rules.yaml
git commit -m "#SPEC 단계 : PRD, TODO, 작업 시나이오 작성"
git push origin SPEC

# --- RED 브랜치 ---
git checkout A-01
git branch RED
git checkout RED
git push -u origin RED
git add pctf/
git commit -m "Red 단계진행을 위한 준비"
git push -u origin RED

# --- RED 구현 커밋 ---
git add pom.xml docs/07_RED_test_plan.md docs/03_work_guide.md pctf/ \
  report/00_RED_coverage_report.md \
  src/test/java/com/example/demo/TextAnalyzerTest.java \
  src/test/java/com/example/demo/FiltersTest.java \
  src/test/java/com/example/demo/FileHandlerTest.java \
  src/test/java/com/example/demo/FeedbackTest.java \
  src/test/java/com/example/demo/SessionTest.java \
  src/test/java/com/example/demo/LoggerTest.java \
  src/test/java/com/example/demo/UIComponentsTest.java \
  src/test/java/com/example/demo/FeedbackControllerWebTest.java
git add -u pctf/
git commit -m "RED 단계 진행: TEST_PLAN + JUnit + JaCoCo 90.9%"
git push origin RED

# --- RED → A-01 ---
git fetch origin
git checkout A-01
git pull origin A-01
git merge RED -m "Merge branch 'RED' into A-01"
git push origin A-01

# --- GREEN 브랜치 (A-01 기준, RED 머지 후) ---
git checkout GREEN
git add src/main/java/com/example/demo/Filters.java
git commit -m "GREEN: TC-NEUTRAL-01/02 neutral filter alignment (FR-09)"
git add report/01_GREEN_bugfix_report.md
git commit -m "GREEN: report/01_GREEN_bugfix_report.md"
git push origin GREEN

# --- GREEN 문서화 ---
git add prompting/02_GREEN_prompt.md prompting/01_RED_prompt.md prompting/User_prompt.md prompting/GIT_prompt.md pctf/02_GREEN_PCTF_prompt.md
git commit -m "GREEN: prompting·PCTF 문서화"
git push origin GREEN

# --- 원격 동기화 (main 충돌 시) ---
git pull origin RED
git checkout main
git stash push -u -m "sync-temp" -- "Cursor AI_퀴즈_김경림.docx"
git pull origin main
git checkout RED
```

---

## 명령 ↔ 용도

| 명령 | 용도 |
|------|------|
| `git merge SPEC` (on A-01) | SPEC → A-01 |
| `git branch RED` | A-01 기준 RED 생성 |
| `git add pctf/` + commit | PCTF 준비 (`e6c9b09`) |
| `git add pom.xml src/test/ docs/07 …` | RED 구현 (`e07ca6b`) |
| `git merge RED` (on A-01) | RED → A-01 통합 |
| `git push origin RED` / `A-01` | 원격 반영 |
| `git ls-remote --heads origin RED` | 원격 RED 존재 확인 |

---

## 스테이징 제외 (항상)

- `target/**`
- `**/*.class`
- `target/maven-status/`, `target/surefire-reports/`, `target/site/jacoco/`

---

## PR (GitHub API / gh)

| PR | base | head | 비고 |
|----|------|------|------|
| #1 | A-01 | SPEC | SPEC 단계 |
| #2 | A-01 | RED | PCTF 준비 |
| #3 | main | A-01 | SPEC+RED 릴리스 |
| **#4** | **A-01** | **GREEN** | **FR-09, GREEN 리포트·문서** |

`gh pr create --base A-01 --head GREEN`

---

## 후속 권장 (REFACTORING)

```bash
git checkout A-01
git pull origin A-01
git merge GREEN
git push origin A-01
git checkout -b REFACTORING
```
