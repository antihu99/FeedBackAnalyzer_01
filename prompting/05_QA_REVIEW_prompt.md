# QA REVIEW · 회고 — Agent 기록

| 항목 | 내용 |
|------|-----|
| PCTF | `pctf/07_QA_REVIEW_PCTF_prompt.md` |
| 브랜치 | `QA` |
| 기간 | 2026-05-22 |

---

## QA-00 — baseline

```bash
mvn clean test
mvn jacoco:report
# Tests 41, Failures 0; JaCoCo line 90.4%
```

---

## QA-02 — outline

- 산출: `docs/12_QA_review_outline.md`
- 커밋: `0283d46` — `QA step2: add QA review outline (docs/12)`

---

## QA-04 — refactoring review report

- 산출: `report/05_REVIEW_refactoring_report.md`
- Before: `6e88371`, After: QA HEAD
- 커밋: `71f241f` — `QA step4: report/05_REVIEW_refactoring_report.md`

---

## QA-05 — 회고 (9단계)

- 산출: `report/06_retrospective.md`
- 4문항: 목표 달성도, AI 활용, TC 영향, 클린코드 소감
- 커밋: `QA step5: report/06_retrospective.md`

---

## Git

```bash
git checkout QA
git add docs/12_QA_review_outline.md
git commit -m "QA step2: add QA review outline (docs/12)"
git push origin QA

git add report/05_REVIEW_refactoring_report.md
git commit -m "QA step4: report/05_REVIEW_refactoring_report.md"
git push origin QA

git add report/06_retrospective.md prompting/05_QA_REVIEW_prompt.md
git commit -m "QA step5: report/06_retrospective.md"
git push origin QA

# PR (로컬 gh auth 필요)
gh pr create --base A-01 --head QA --title "QA: review docs and retrospective"
git checkout A-01 && git merge QA && git push origin A-01
```
