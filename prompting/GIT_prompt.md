# Git 명령 기록 — Feedback Analyzer

> `작업규칙.TXT`: Agent 대화 중 사용된 git 명령만 (**중복 제거**)  
> **최종 갱신**: 2026-05-22

---

## 요약

| 항목 | 내용 |
|------|------|
| 작업 디렉터리 | `d:\Vs_workplace\Java_project\FeedBackAnalyzer_01` |
| **현재 브랜치** | **`QA`** |
| **QA HEAD** | step5 후 갱신 (`71f241f` + 회고) |
| **A-01 HEAD** | `bc1724f` + QA 문서 머지 예정 |
| **new_feature HEAD** | **`bc1724f`** |
| **gh 인증** | **미로그인** — `gh pr edit` 수동 필요 |
| **REFACTORING HEAD** | **`2ccbe96`** |
| 원격 | `https://github.com/antihu99/FeedBackAnalyzer_01.git` |
| SPEC | `a306870` → `9b03001` |
| RED | `e6c9b09` → `e07ca6b` |
| GREEN | `87136db` → `6e88371` (4커밋) |
| **PR** | #1~#2 Merged · **#3 A-01→main OPEN** · #4 GREEN→A-01 **MERGED** · **#5 REFACTORING→A-01 OPEN** |

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
| `87136db` | GREEN: TC-NEUTRAL-01/02 neutral filter alignment (FR-09) | `origin/GREEN` |
| `c9b926a` | GREEN: report/02_GREEN_bugfix_report.md | `origin/GREEN` |
| `47a62be` | GREEN: prompting·PCTF 문서화 | `origin/GREEN` |
| `6e88371` | GREEN: docs/08_GREEN_test_results.md (TC 실행 결과, 머지 전) | `origin/GREEN` |

**머지 GREEN → A-01**: Fast-forward `e07ca6b`..`6e88371` → `git push origin A-01`  
**PR #4**: MERGED (GitHub) · **PR #3**: OPEN, 제목 `SPEC + RED + GREEN` 갱신

---

## 사용된 명령 (실행 순, 중복 제거)

```bash
cd "d:\Vs_workplace\Java_project\FeedBackAnalyzer_01"

# --- 조회 ---
git status
git branch -a
git fetch origin
git log --oneline -5
git log origin/main..origin/A-01 --oneline
git rev-parse A-01 origin/GREEN origin/A-01

# --- A-01 ---
git checkout A-01
git pull origin A-01

# --- SPEC → A-01 ---
git merge SPEC
git push origin A-01

# --- RED ---
git checkout RED
git push -u origin RED
git commit -m "RED 단계 진행: TEST_PLAN + JUnit + JaCoCo 90.9%"
git push origin RED
git checkout A-01 && git merge RED && git push origin A-01

# --- GREEN ---
git checkout GREEN
git commit -m "GREEN: TC-NEUTRAL-01/02 neutral filter alignment (FR-09)"
git commit -m "GREEN: report/02_GREEN_bugfix_report.md"
git commit -m "GREEN: prompting·PCTF 문서화"
git commit -m "GREEN: docs/08_GREEN_test_results.md (TC 실행 결과, 머지 전)"
git push origin GREEN

# --- GREEN → A-01 (로컬 머지) ---
git checkout A-01
git pull origin A-01
git merge GREEN
git push origin A-01

# --- baseline 확인 (G-00) ---
git stash push -- src/main/java/com/example/demo/Filters.java
mvn test
git stash pop
```

---

## PR (GitHub)

| PR | base | head | 상태 | 비고 |
|----|------|------|------|------|
| #1 | A-01 | SPEC | Merged | SPEC 문서 |
| #2 | A-01 | RED | Merged | RED PCTF·테스트 |
| **#3** | **main** | **A-01** | **OPEN** | 릴리스 SPEC+RED+GREEN (`6e88371`) |
| #4 | A-01 | GREEN | **MERGED** | FR-09·리포트·docs/08 |

PR #3 갱신: REST API `PATCH /pulls/3` — 제목·본문 GREEN 반영

---

## 스테이징 제외 (항상)

- `target/**`, `**/*.class`
- `target/maven-status/`, `target/surefire-reports/`, `target/site/jacoco/`

---

## 커밋 이력 (REFACTORING)

| 해시 | 메시지 | push |
|------|--------|------|
| `29821a6` | REFACTOR: PCTF prompts for steps 4-6 | `origin/REFACTORING` |
| `2d81f59` | REFACTOR step4-5: naming, Sentiment enum, SentimentClassifier (FR-12~14) | `origin/REFACTORING` |
| `0c23667` | REFACTOR step6: Controller SRP and package split (FR-15, FR-16) | `origin/REFACTORING` |
| `3324267` | REFACTOR: report/03_REFACTORING_report.md | `origin/REFACTORING` |
| `2ccbe96` | docs: User_prompt REFACTORING step6 완료 (#42) | `origin/REFACTORING` |

```bash
git checkout REFACTORING
git push -u origin REFACTORING
# PR #5 REFACTORING → A-01
```

---

## 커밋 이력 (new_feature)

| 해시 | 메시지 | push |
|------|--------|------|
| `3113e08` | docs: 프로젝트 작업 규칙 파일 추가 | `origin/new_feature` |
| `291bd85` | FEATURE step1: start New_Feature branch from A-01 | `origin/new_feature` |
| `523133d` | FEATURE step2: add trend CSV sample and feature schema | `origin/new_feature` |
| `e690d9c` | FEATURE step3: add Trend and Keyword File DB tests | `origin/new_feature` |
| `852fc4c` | FEATURE step4: Trend visualization and File DB keywords | `origin/new_feature` |
| `b7e4d54` | FEATURE step5: report/04_New_Feature_report.md | `origin/new_feature` |
| `f3e8518` | FEATURE: docs/11 test results and prompting | `origin/new_feature` |
| `86b4775` | docs: 작업규칙 8단계 REVIEW(QA) 단계 추가 | `origin/new_feature` |

**머지**: `git checkout A-01 && git merge new_feature` → Fast-forward `28be9e9`..`bc1724f`, `git push origin A-01`  
**PR #3** (`A-01`→`main`): HEAD 자동 반영 `bc1724f` · 제목·본문 갱신 → `docs/pr3_body_update.md` 참고

```bash
git checkout A-01 && git merge new_feature && git push origin A-01
gh pr edit 3 --title "Release: A-01 → main (SPEC + RED + GREEN + REFACTORING + New_Feature)" --body-file docs/pr3_body_update.md
```

```bash
gh auth status
gh auth login
gh pr create --base A-01 --head new_feature
```

```bash
git checkout A-01
git pull origin A-01
git checkout -b new_feature   # 또는 기존 new_feature
git commit --allow-empty -m "FEATURE step1: start New_Feature branch from A-01"
git push -u origin new_feature
# step2~5: FEATURE stepN: ... (각 단계 후 push origin new_feature)
# PR: new_feature → A-01
```

---

## 커밋 이력 (QA)

| 해시 | 메시지 | push |
|------|--------|------|
| `0283d46` | QA step2: add QA review outline (docs/12) | `git push origin QA` |
| `71f241f` | QA step4: report/05_REVIEW_refactoring_report.md | `git push origin QA` |
| *(step5)* | QA step5: report/06_retrospective.md | `git push origin QA` |

```bash
git checkout QA
git add docs/12_QA_review_outline.md && git commit -m "QA step2: add QA review outline (docs/12)"
git add report/05_REVIEW_refactoring_report.md && git commit -m "QA step4: report/05_REVIEW_refactoring_report.md"
git add report/06_retrospective.md prompting/05_QA_REVIEW_prompt.md && git commit -m "QA step5: report/06_retrospective.md"
git push origin QA
gh pr create --base A-01 --head QA   # gh auth login 필요
git checkout A-01 && git pull origin A-01 && git merge QA && git push origin A-01
```

## 릴리스 PR (A-01 → main)

| 해시 | 메시지 | push |
|------|--------|------|
| `8cdb538` | docs: A-01 to main release PR body (PR #3, HEAD afcbc53) | `git push origin A-01` |

```bash
# gh auth login 후
gh pr edit 3 --title "Release: A-01 → main (SPEC through QA)" --body-file docs/pr3_body_release_body.md
# 또는
gh pr create --base main --head A-01 --title "Release: A-01 → main (SPEC through QA)" --body-file docs/pr3_body_release_body.md
```

- 템플릿: `docs/pr3_body_update.md` (상세), `docs/pr3_body_release_body.md` (gh `--body-file`용)

---

## prompting 동기화 (2026-05-22)

| 파일 | 역할 |
|------|------|
| `00_SPEC_prompt.md` | SPEC Agent 기록 |
| `01_RED_prompt.md` | RED Agent 기록 |
| `02_GREEN_prompt.md` | GREEN Agent 기록 |
| `03_REFACTORING_prompt.md` | REFACTORING step4~6 Agent 기록 |
| `04_New_Feature_prompt.md` | New_Feature PCTF 06 Agent 기록 |
| `05_QA_REVIEW_prompt.md` | QA REVIEW·회고 Agent 기록 |
| `User_prompt.md` | 사용자 prompt 표 (#1~#58) |
| `GIT_prompt.md` | 본 문서 |
