# QA REVIEW 개요 — 리팩토링 전후 비교 (8단계)

| 항목 | 값 |
|------|-----|
| PCTF | `pctf/07_QA_REVIEW_PCTF_prompt.md` |
| 브랜치 | `QA` (base: `A-01`) |
| 작성일 | 2026-05-22 |
| 상세 보고서 | `report/05_REVIEW_refactoring_report.md` |

---

## 1. Before / After Git 기준

| 구분 | 커밋 | 설명 |
|------|------|------|
| **Before** | `6e88371` | GREEN 완료 직후, REFACTORING **이전** |
| **After** | `QA` HEAD (`cc52b52` 기준) | REFACTORING + New_Feature 머지 반영 (`A-01` 동일) |

```bash
git show 6e88371:src/main/java/com/example/demo/Filters.java
git show HEAD:src/main/java/com/example/demo/Filters.java
```

---

## 2. 비교 축 (FR-12~16)

| FR | 주제 | Before (`6e88371`) | After (`QA`) | 검증 |
|----|------|-------------------|--------------|------|
| FR-12 | 도메인 네이밍 | `fil`, `sent`, `kw`, `fil_data` | `filterFeedbacks`, `analyzeSentiment`, `analyzeKeywords`, `filteredFeedbacksForExport` | 단위·웹 테스트 Green |
| FR-13 | 상수·enum | `S_KEYWORDS` 중복, `Constants` 키워드 중복 | `config.Constants` dedupe, `Sentiment` enum, `FILTER_ALL` | `config` 패키지, 카테고리 1곳 시드 |
| FR-14 | 감정 SRP | `Filters.resolveSentimentLikeAnalyzer` vs `TextAnalyzer.sent` 이중 규칙 | `SentimentClassifier.classify` 단일화, 위임 | TC-NEUTRAL-01/02 |
| FR-15 | Controller SRP | `FeedbackController` CSV·집계·필터 혼재 | `FeedbackService` 오케스트레이션 | Controller ~67 LOC, HTTP만 |
| FR-16 | 패키지 | 단일 `com.example.demo` | `controller` / `service` / `model` / `config` | 컴파일·스캔 충돌 없음 |

참조: `report/03_REFACTORING_report.md`, `docs/05_code_smell.md` 사례 1~3.

---

## 3. 보고서 목차 (`report/05`와 동일)

1. 요약  
2. 비교 기준 (Before / After SHA)  
3. FR-12~13 네이밍·상수 (Before/After 코드)  
4. FR-14 감정 SRP (Before/After 코드)  
5. FR-15~16 Controller·패키지 (Before/After 코드)  
6. 계약·회귀 검증 (HTTP 5 path, `mvn test`, JaCoCo)  
7. 개선 효과·잔여 리스크  
8. (선택) New_Feature 요약  
9. 결론·A-01 머지 권고  

---

## 4. `git show` 추출 파일 목록

| # | Before (`6e88371`) | After (`QA` HEAD) |
|---|-------------------|-------------------|
| 1 | `src/main/java/com/example/demo/FeedbackController.java` | `controller/FeedbackController.java` + `service/FeedbackService.java` |
| 2 | `src/main/java/com/example/demo/Filters.java` | `Filters.java` + `SentimentClassifier.java` |
| 3 | `src/main/java/com/example/demo/TextAnalyzer.java` | `TextAnalyzer.java` |
| 4 | `src/main/java/com/example/demo/Constants.java` | `config/Constants.java`, `config/Sentiment.java` |
| 5 | `src/main/java/com/example/demo/Feedback.java` | `model/Feedback.java` |
| 6 | (없음) 패키지 단일 루트 | `controller/`, `service/`, `model/`, `config/` |

---

## 5. 회귀·커버리지 체크리스트 (QA-00 / QA-04 §6)

| 항목 | 기대 | QA-00 결과 (2026-05-22) |
|------|------|-------------------------|
| `mvn clean test` | 0 failures, 0 errors | **41** tests, **0** failures, **0** errors |
| JaCoCo `com.example.demo` line | ≥ 90% | **90.4%** (413/457) |
| TC-NEUTRAL-01/02 | PASS | `TextAnalyzerTest`, `FiltersTest` Green |
| HTTP 계약 5 path | GET `/`, POST `/analyze`, `/upload`, `/filter`, GET `/download` | `FeedbackControllerWebTest` 10/10 PASS |
| New_Feature (부록) | Trend, `/keywords/*` | `TrendServiceTest`, keyword tests Green |

> **참고**: `mvn test`만 실행 시 `target/`에 구버전 `FeedbackController.class` 잔존 시 Spring 빈 충돌 가능 → **`mvn clean test` 권장**.

---

## 6. Git 단계 매핑

| PCTF 단계 | 산출물 | 커밋 메시지 |
|-----------|--------|-------------|
| QA-02 | 본 문서 | `QA step2: add QA review outline (docs/12)` |
| QA-04 | `report/05_REVIEW_refactoring_report.md` | `QA step4: report/05_REVIEW_refactoring_report.md` |
