# REFACTORING SRP 제안 — 감정 분류 (FR-14)

> **단계**: REFACTORING 5단계  
> **브랜치**: `REFACTORING`  
> **작성일**: 2026-05-22

---

## 1. 문제 (Before)

| 클래스 | 책임 혼재 |
|--------|-----------|
| `TextAnalyzer` | 감정 집계 + **감정 판별 로직** + 카테고리 집계 |
| `Filters` | 감정 필터 + **동일 감정 판별 로직 복제** + 카테고리 필터 |

GREEN 이후에도 `classifySentimentLabel()` / `resolveSentimentLabel()` 이 **두 곳**에 존재 → `05_code_smell.md` 사례1.

---

## 2. 해결 (After)

| 클래스 | 단일 책임 |
|--------|-----------|
| `SentimentClassifier` | 텍스트 → `긍정`/`부정`/`중립` 라벨 (FR-14) |
| `TextAnalyzer` | 피드백 목록 → 감정·카테고리 **집계** |
| `Filters` | 피드백 목록 → 감정·카테고리 **필터** |

```text
Feedback text
      │
      ▼
SentimentClassifier.classify()
      │
      ├──▶ TextAnalyzer.analyzeSentiment()  (건수 Map)
      └──▶ Filters.applySentimentFilter()   (목록 필터)
```

---

## 3. FR 매핑

| FR | 구현 |
|----|------|
| FR-12 | `filterFeedbacks`, `analyzeSentiment`, `analyzeKeywords`, `filteredFeedbacksForExport` (4단계) |
| FR-13 | `Sentiment` enum, `Constants` dedupe (4단계) |
| FR-14 | `SentimentClassifier` 단일 규칙 (5단계) |

---

## 4. 회귀

- `mvn test`: 34 tests, 0 failures  
- TC-NEUTRAL-01/02: **PASS** (분석·필터 동일 Classifier 위임)  
- HTTP URL·CSV `text` 컬럼: **불변**

---

## 5. 후속 (6단계)

- `FeedbackController` → `FeedbackService` 분리 (FR-15~16)  
- `pctf/05_REFACTORING_step6_controller_SRP_PCTF_prompt.md`
