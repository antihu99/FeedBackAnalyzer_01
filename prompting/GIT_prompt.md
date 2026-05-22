# Git 명령 기록 — Feedback Analyzer

> `작업규칙.TXT`: Agent 대화 중 사용된 git 명령만 (**중복 제거**)  
> **최종 갱신**: 2026-05-21

---

## 요약

| 항목 | 내용 |
|------|------|
| 작업 디렉터리 | `d:\Vs_workplace\Java_project\FeedBackAnalyzer_01` |
| 현재 브랜치 | `SPEC` |
| upstream | `origin/SPEC` |
| 원격 | `https://github.com/antihu99/FeedBackAnalyzer_01.git` |
| SPEC 커밋 | `a306870` → `9b03001` |
| PR | #1 Open — `SPEC` → `A-01` |

---

## 커밋 이력 (SPEC)

| 해시 | 메시지 | push |
|------|--------|------|
| `a306870` | SPEC 단계 진행 | `git push -u origin SPEC` (최초) |
| `9b03001` | #SPEC 단계 : PRD, TODO, 작업 시나이오 작성 | `git push origin SPEC` |

---

## 사용된 명령 (실행 순, 중복 제거)

```bash
cd "d:\Vs_workplace\Java_project\FeedBackAnalyzer_01"

# --- 조회 ---
git status
git status -sb
git branch -a
git branch --show-current
git branch -vv
git remote -v
git fetch origin
git rev-parse HEAD
git rev-parse origin/A-01
git rev-parse origin/SPEC
git log --oneline -1 HEAD
git log --oneline -1 origin/A-01
git log --oneline -1 origin/SPEC
git log --oneline -3 SPEC
git diff --stat origin/A-01...HEAD
git diff --stat origin/A-01
git ls-remote --heads origin

# --- A-01 ---
git checkout -b A-01
git push -u origin A-01

# --- SPEC (1차: analysis.md만) ---
git checkout -b SPEC
git add docs/
git diff --cached --stat
git commit -m "SPEC 단계 진행"
git push -u origin SPEC

# --- SPEC (2차: docs 00-06, prompting, report, rules) ---
git add docs/ prompting/ report/ .cursorrules tdd_rules.yaml
git diff --cached --stat
git commit -m "#SPEC 단계 : PRD, TODO, 작업 시나이오 작성"
git push origin SPEC
```

---

## 명령 ↔ 용도

| 명령 | 용도 |
|------|------|
| `git checkout -b A-01` | 통합 브랜치 생성 |
| `git push -u origin A-01` | 원격 A-01 |
| `git checkout -b SPEC` | SPEC 분기 (A-01 기준) |
| `git add docs/` | 1차: analysis만 |
| `git add docs/ prompting/ report/ .cursorrules tdd_rules.yaml` | 2차: SPEC 전체 산출물 |
| `git commit -m "SPEC 단계 진행"` | 1차 커밋 |
| `git commit -m "#SPEC 단계 : PRD, TODO, 작업 시나이오 작성"` | 2차 커밋 |
| `git push -u origin SPEC` | SPEC 최초 upstream |
| `git push origin SPEC` | 2차 push |
| `git fetch origin` | 원격·PR 상태 확인 |

---

## 스테이징 제외 (항상)

- `target/**`
- `**/*.class`
- `target/maven-status/`, `target/surefire-reports/`

---

## 미실행·실패

| 도구 | 결과 |
|------|------|
| `gh pr list` | 실패 (gh 미인증) — GitHub API로 PR #1 확인 |

---

## 후속 권장

```bash
# prompting 갱신본 반영 시 (선택)
git add prompting/
git commit -m "SPEC: prompting 기록 갱신 (9b03001 이후)"
git push origin SPEC

# A-01 머지 후 RED
git checkout A-01
git pull origin A-01
git checkout -b RED
```
