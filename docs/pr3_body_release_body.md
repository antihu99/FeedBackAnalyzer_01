## Summary

- **A-01** 통합 릴리스: SPEC → RED → GREEN → REFACTORING → **New_Feature** → **QA REVIEW·회고**
- **HEAD** `afcbc53` (2026-05-22)
- Dual-Track TDD·리팩토링·신규 기능·QA 문서까지 **단일 통합 브랜치**로 `main` 반영

## Included phases

| Phase | Branch | Highlights |
|-------|--------|------------|
| SPEC | `SPEC` | PRD, analysis, work guide, Mom Test |
| RED | `RED` | TEST_PLAN, JUnit, JaCoCo ≥90% |
| GREEN | `GREEN` | FR-09 neutral filter |
| REFACTORING | `REFACTORING` | FR-12~16, SentimentClassifier, Controller/Service |
| New_Feature | `new_feature` | FR-17 Trend, FR-18 File DB |
| QA | `QA` | Refactoring Before/After (`6e88371`), retrospective |

## Quality gates

| Check | Result |
|-------|--------|
| `mvn clean test` | **41** tests, **0** failures |
| JaCoCo line (`com.example.demo`) | **~90.4%** (≥90%) |
| TC-NEUTRAL-01/02 | PASS |
| HTTP | 5 legacy paths + `/keywords/add`, `/keywords/remove` |

## Key reports

- `report/00` ~ `06_retrospective.md`
- `docs/11_New_Feature_test_results.md`
- `report/05_REVIEW_refactoring_report.md`

## Test plan

- [ ] `mvn clean test` on `A-01`
- [ ] `mvn jacoco:report` — line ≥ 90%
- [ ] Smoke: Trend UI + keyword File DB
- [ ] Feature branches **not deleted**

## Notes

- Use **`mvn clean test`** if ApplicationContext bean conflict (stale `target/`)
- Follow-up: Session HTTP migration, upload path (`C:\tmp`)
