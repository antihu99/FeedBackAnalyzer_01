# REFACTORING 단계 보고서 — FR-12~16

> **브랜치**: `REFACTORING`  
> **기간**: 2026-05-22  
> **선행**: GREEN @ `6e88371` (A-01)

---

## 1. 요약

| 단계 | FR | 커밋 메시지 (권장) | 상태 |
|------|-----|-------------------|------|
| 4 | FR-12, FR-13 | `REFACTOR step4: domain naming and constants` | 완료 |
| 5 | FR-14 | `REFACTOR step5: SentimentClassifier SRP` | 완료 |
| 6 | FR-15, FR-16 | `REFACTOR step6: Controller SRP and package split` | 완료 |

---

## 2. step4 — 네이밍·상수 (FR-12~13)

| Before | After |
|--------|-------|
| `fil()` | `filterFeedbacks()` |
| `sent()` | `analyzeSentiment()` |
| `kw()` | `analyzeKeywords()` |
| `fil_data` | `filteredFeedbacksForExport` |

- `Sentiment` enum 추가 (`config`)
- `Constants` 키워드 dedupe, `S_KEYWORDS` 제거

---

## 3. step5 — 감정 SRP (FR-14)

- `SentimentClassifier.classify(String)` 단일 규칙
- `TextAnalyzer`·`Filters` 위임
- 부록: `docs/09_REFACTOR_SRP_proposal.md`

---

## 4. step6 — Controller·패키지 (FR-15~16)

### 패키지 구조 (After)

```text
com.example.demo
├── DemoApplication.java
├── controller/     FeedbackController   (HTTP only)
├── service/        FeedbackService      (analyze, upload, filter, download)
├── model/          Feedback
├── config/         Constants, Sentiment
├── TextAnalyzer, Filters, SentimentClassifier, Session, Logger, ...
```

### FR-15

- Controller: `@GetMapping` / `@PostMapping` + `Model`/`HttpServletResponse`만
- CSV 파싱·집계·필터 오케스트레이션 → `FeedbackService`

### contract_immutable

| 항목 | 값 | 확인 |
|------|-----|------|
| `/` | GET | PASS |
| `/analyze` | POST | PASS |
| `/upload` | POST | PASS |
| `/filter` | POST | PASS |
| `/download` | GET | PASS |
| CSV 컬럼 | `text` | 유지 |

---

## 5. 테스트·커버리지

| 항목 | 결과 |
|------|------|
| `mvn test` | Tests **34**, Failures **0**, Errors **0** |
| JaCoCo (`com.example.demo`) | **≥ 90%** line |
| TC-NEUTRAL-01/02 | **PASS** |
| `FeedbackControllerWebTest` | 8/8 **PASS** |

---

## 6. Git (REFACTORING 브랜치)

```text
… → 6e88371 (GREEN, A-01)
→ 29821a6 PCTF prompts 4-6
→ 2d81f59 step4-5 naming + SentimentClassifier
→ (step6) Controller SRP + packages
→ (본 리포트) report/02_REFACTORING_report.md
```

**후속**: PR `REFACTORING` → `A-01`

---

## 7. 범위 외 (미수행)

- FR-10 multiline, FR-11 Logger UI (GREEN 별도 PCTF)
- FR-17~18 New_Feature
- `Session` static → HTTP session (선택 개선)
