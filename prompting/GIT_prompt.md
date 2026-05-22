# Git 명령 기록 — Feedback Analyzer

> `작업규칙.TXT`: Agent 대화 중 사용된 git 명령만 (중복 제거)  
> 갱신일: 2026-05-21

---

## 요약

| 항목 | 내용 |
|------|------|
| 작업 디렉터리 | `d:\Vs_workplace\Java_project\FeedBackAnalyzer_01` |
| 생성 브랜치 | `A-01`, `SPEC` |
| 원격 | `origin` → `https://github.com/antihu99/FeedBackAnalyzer_01.git` |
| 주요 커밋 | `4e1003e` (#00 First File Upload), `a306870` (SPEC 단계 진행) |

---

## 사용된 명령 (실행 순)

```bash
# 저장소 상태·브랜치 확인
cd "d:\Vs_workplace\Java_project\FeedBackAnalyzer_01"
git status
git branch -a
git remote -v

# A-01 생성·푸시
git checkout -b A-01
git push -u origin A-01

# 브랜치·동기화 확인
git branch --show-current
git status -sb
git fetch origin
git rev-parse HEAD
git rev-parse origin/A-01
git log --oneline -1 HEAD
git log --oneline -1 origin/A-01
git diff --stat origin/A-01...HEAD

# SPEC 생성·커밋·푸시 (.class 제외)
git checkout -b SPEC
git add docs/
git status
git diff --cached --stat
git commit -m "SPEC 단계 진행"
git push -u origin SPEC

# SPEC 이후 확인
git branch -vv
git ls-remote --heads origin
```

---

## 명령별 용도

| 명령 | 용도 |
|------|------|
| `git checkout -b A-01` | main에서 통합 브랜치 생성 |
| `git push -u origin A-01` | 원격 A-01 생성·upstream |
| `git checkout -b SPEC` | A-01에서 SPEC 분기 |
| `git add docs/` | 문서만 스테이징 (class 제외) |
| `git commit -m "SPEC 단계 진행"` | SPEC 커밋 |
| `git push -u origin SPEC` | 원격 SPEC·upstream |
| `git fetch origin` | PR/동기화 확인 전 fetch |

---

## 미실행·실패

| 명령/도구 | 결과 | 비고 |
|-----------|------|------|
| `gh pr list --head SPEC` | 실패 | gh 미인증 — GitHub API로 PR #1 확인 대체 |

---

## 후속 권장 (로컬 docs·prompting·report 푸시용)

```bash
git status
git add docs/ prompting/ report/ .cursorrules tdd_rules.yaml
# .class / target 미포함 확인
git commit -m "SPEC: docs 00-06, Mom Test, rules, todo, prompting, report"
git push origin SPEC
```
