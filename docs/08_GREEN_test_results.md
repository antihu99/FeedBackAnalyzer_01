# GREEN 단계 TC 실행 결과

| 항목 | 내용 |
|------|------|
| 문서 유형 | **TEST_RESULTS** (GREEN 단계 실측) |
| 단계 | GREEN (FR-09, FR-20) |
| 브랜치 | `GREEN` (머지 전) |
| 작성 근거 | `docs/07_RED_test_plan.md`, `pctf/02_GREEN_PCTF_prompt.md` |
| 실행일 | 2026-05-22 |
| 검증 명령 | `mvn test`, `mvn test jacoco:report` |
| 상세 리포트 | `report/01_GREEN_bugfix_report.md` |

---

## 1. 문서 역할

| 구분 | 문서 | 역할 |
|------|------|------|
| 명세 | `07_RED_test_plan.md` | TC ID·Given-When-Then·RED/GREEN **기대** |
| **본 문서** | `08_GREEN_test_results.md` | GREEN 브랜치에서 **실제 실행 결과** (머지 전 스냅샷) |
| 요약 리포트 | `report/01_GREEN_bugfix_report.md` | 버그 수정·커버리지·Git 요약 |

---

## 2. 실행 요약 (`mvn test`)

### 2.1. GREEN 진입 전 (G-00 baseline)

| 항목 | 값 |
|------|-----|
| 프로덕션 | `Filters.java` — `S_KEYWORDS` 감정 판정 (RED baseline) |
| Tests run | **34** |
| Failures | **2** |
| Errors | **0** |
| 실패 TC | **TC-NEUTRAL-01**, **TC-NEUTRAL-02** |

### 2.2. GREEN 종료 (수정 후 · 본 문서 기준)

| 항목 | 값 |
|------|-----|
| 프로덕션 | `Filters.java` — `resolveSentimentLikeAnalyzer()` (`Constants.SENTIMENT_KEYWORDS` 통일) |
| Tests run | **34** |
| Failures | **0** |
| Errors | **0** |
| Skipped | **0** |
| JaCoCo line (`com.example.demo`) | **90.8%** (248 / 273) |
| FR-20 | **충족** (≥ 90%) |
| FR-09 (B类) | **충족** (TC-NEUTRAL 전부 PASS) |

---

## 3. TEST_PLAN TC 결과 매트릭스 (§5~§7)

`docs/07_RED_test_plan.md` 필수 TC **21건** + B类 2건. GREEN 실측은 전부 **PASS**.

| 구분 | TC ID 범위 | 건수 | RED 실측 | GREEN 실측 | 비고 |
|------|------------|------|----------|------------|------|
| A类 | TA-01 ~ TA-08 | 8 | PASS | **PASS** | 회귀 없음 |
| A类 | FI-01 ~ FI-08 | 8 | PASS | **PASS** | 회귀 없음 |
| B类 | TC-NEUTRAL-01, TC-NEUTRAL-02 | 2 | **FAIL** | **PASS** | FR-09 수정 |
| A类 | FH-01 ~ FH-03 | 3 | PASS | **PASS** | 회귀 없음 |
| **필수 소계** | — | **21** | FAIL 2 | **PASS 21** | — |

---

## 4. TextAnalyzerTest — TC별 결과 (§5)

| TC ID | 메서드 | RED | GREEN | 검증 내용 |
|-------|--------|-----|-------|-----------|
| TA-01 | `sent_positiveKeyword_incrementsPositive` | PASS | **PASS** | `"배송이 최고예요"` → 긍정 1 |
| TA-02 | `sent_negativeKeyword_incrementsNegative` | PASS | **PASS** | `"화가 나고 최악이에요"` → 부정 1 |
| TA-03 | `sent_noKeyword_defaultsNeutral` | PASS | **PASS** | `"보통이에요"` → 중립 1 |
| TA-04 | `sent_emptyList_allZero` | PASS | **PASS** | 빈 목록 → 0/0/0 |
| TA-05 | `sent_mixedCounts_correct` | PASS | **PASS** | 긍·부·중립 각 1 |
| TA-06 | `kw_deliveryCategory_matches` | PASS | **PASS** | 배송 카테고리 ≥ 1 |
| TA-07 | `kw_qualityCategory_matches` | PASS | **PASS** | 품질 카테고리 ≥ 1 |
| TA-08 | `kw_emptyList_allZero` | PASS | **PASS** | 카테고리 키 전부 0 |

**PCTF 단계**: G-01 — `mvn test -Dtest=TextAnalyzerTest` → 8/8 PASS

---

## 5. FiltersTest — TC별 결과 (§6)

### 5.1. A类 (FI-01 ~ FI-08)

| TC ID | 메서드 | RED | GREEN | 검증 내용 |
|-------|--------|-----|-------|-----------|
| FI-01 | `fil_positive_only` | PASS | **PASS** | 긍정 필터 1건 |
| FI-02 | `fil_negative_only` | PASS | **PASS** | 부정 필터 1건 |
| FI-03 | `fil_neutral_keyword보통` | PASS | **PASS** | `"보통이에요"` 중립 1건 |
| FI-04 | `fil_all_sentiment` | PASS | **PASS** | 전체 3건 |
| FI-05 | `fil_keyword_delivery` | PASS | **PASS** | 배송 키워드 ≥ 1 |
| FI-06 | `fil_keyword_all` | PASS | **PASS** | 2건 전체 반환 |
| FI-07 | `fil_combined_sentimentAndKeyword` | PASS | **PASS** | 긍정+배송 교집합 1 |
| FI-08 | `fil_emptyList_returnsEmpty` | PASS | **PASS** | 빈 목록 0건 |

**PCTF 단계**: G-02 — NEUTRAL 제외 8건 PASS

### 5.2. B类 (FR-09) — GREEN 핵심

| TC ID | 메서드 | RED | GREEN | Given | Then (실측) |
|-------|--------|-----|-------|-------|-------------|
| **TC-NEUTRAL-01** | `sentimentAnalysis_matchesNeutralFilter` | **FAIL** | **PASS** | `["괜찮은 서비스였어요"]` | `sent()` 중립 **1**, `fil(중립,전체)` **1** |
| **TC-NEUTRAL-02** | `mixedList_filterNeutralCount_matchesSentimentBucket` | **FAIL** | **PASS** | 4건 혼합 리스트 | `sent().get("중립")` **2** == `fil(중립).size()` **2** |

#### B类 RED 실패 vs GREEN 통과 (상세)

| TC ID | RED 실패 원인 | GREEN 수정 후 |
|-------|---------------|---------------|
| TC-NEUTRAL-01 | `sent()` 중립 1, `fil(중립)` **0** (`괜찮`→S_KEYWORDS 긍정) | 분석·필터 모두 중립 **1** |
| TC-NEUTRAL-02 | `sent()` 중립 **2**, `fil(중립)` **1** | 집계·필터 건수 **일치 (2)** |

**PCTF 단계**: G-03, G-04 — `Filters.fil()` 감정 판정을 `TextAnalyzer.sent()`와 동일 규칙으로 통일

---

## 6. FileHandlerTest — TC별 결과 (§7)

| TC ID | 메서드 | RED | GREEN | 검증 내용 |
|-------|--------|-----|-------|-----------|
| FH-01 | `saveResult_doesNotThrow` | PASS | **PASS** | 1건 saveResult 예외 없음 |
| FH-02 | `save_delegatesToSaveResult` | PASS | **PASS** | save → saveResult 위임 |
| FH-03 | `saveResult_emptyList` | PASS | **PASS** | 빈 목록 예외 없음 |

**PCTF 단계**: G-05 — 3/3 PASS

---

## 7. 커버리지 보강·기타 테스트 (C类 · FR-21)

TEST_PLAN §8 C类 및 JaCoCo 90% 달성용. GREEN에서도 **전부 PASS**.

| TC ID | 테스트 클래스 | 메서드(실제) | RED | GREEN |
|-------|---------------|--------------|-----|-------|
| COV-01 | FeedbackTest | `feedback_textGetter` | PASS | **PASS** |
| COV-02 | SessionTest | `session_initAndFeedbacks` | PASS | **PASS** |
| COV-03 | FeedbackControllerWebTest | `index_returnsOk` | PASS | **PASS** |
| — | FeedbackControllerWebTest | `analyze_withText` 등 7건 | PASS | **PASS** |
| — | LoggerTest | `logger_levels` | PASS | **PASS** |
| — | UIComponentsTest | `getCategories_returnsFive` | PASS | **PASS** |
| — | DemoApplicationTests | `contextLoads` | PASS | **PASS** |

| 항목 | 건수 |
|------|------|
| TEST_PLAN 필수 TC | 21 |
| C类·Web·기타 | 13 |
| **합계 (mvn test)** | **34** |

---

## 8. PCTF 단계별 TC 확인 (G-00 ~ G-07)

| PCTF 단계 | 대상 TC | GREEN 실측 | 프로덕션 변경 |
|-----------|---------|------------|---------------|
| G-00 | 전체 (baseline) | Failures **2** | 없음 |
| G-01 | TA-01 ~ TA-08 | PASS 8/8 | 없음 |
| G-02 | FI-01 ~ FI-08 | PASS 8/8 | 없음 |
| G-03 | TC-NEUTRAL-01 | **PASS** | `Filters.java` |
| G-04 | TC-NEUTRAL-02 | **PASS** | (G-03 동일 수정) |
| G-05 | FH-01 ~ FH-03 | PASS 3/3 | 없음 |
| G-06 | 전체 + JaCoCo | Failures **0**, 90.8% | 없음 |
| G-07 | 리포트 | `report/01_GREEN_bugfix_report.md` | — |

---

## 9. JaCoCo 클래스별 (GREEN 종료)

| 클래스 | LINE missed | LINE covered | 비고 |
|--------|-------------|--------------|------|
| TextAnalyzer | 0 | 33 | TA TC 전부 커버 |
| Filters | 0 | 35 | FI + NEUTRAL TC 커버 |
| FileHandler | 0 | 7 | FH TC 커버 |
| Constants | 1 | 45 | static 초기화 |
| FeedbackController | 12 | 80 | Web COV |
| Session | 5 | 14 | COV-02 |
| Logger | 7 | 21 | COV |
| UIComponents | 0 | 3 | COV |
| **패키지 합계** | **25** | **248** | **90.8%** |

`DemoApplication` — jacoco exclude (TEST_PLAN §9)

---

## 10. GREEN 완료 판정

| 상태 | 조건 | 판정 |
|------|------|------|
| ✅ 완료 | TEST_PLAN §5~§7 TC 21건 GREEN **PASS** | 충족 |
| ✅ 완료 | B类 TC-NEUTRAL-01/02 **PASS** | FR-09 충족 |
| ✅ 완료 | `mvn test` Failures=0, Errors=0 | 충족 |
| ✅ 완료 | JaCoCo line ≥ 90% | 충족 (90.8%) |
| ✅ 완료 | `src/test/**` 미변경 | 충족 |
| ⏳ 머지 전 | PR #4 `GREEN` → `A-01` | 리뷰·머지 대기 |

---

## 11. 범위 외 (본 GREEN TC 결과에 미포함)

| FRD | 항목 | TEST_PLAN | 비고 |
|-----|------|-----------|------|
| FR-10 | multiline 입력 | Out of Scope §3.2 | 별도 PCTF |
| FR-11 | Logger level UI | Out of Scope §3.2 | 별도 PCTF |
| FR-08/03 | download/upload 버그 | TC-DOWNLOAD/UPLOAD | GREEN 확장 PCTF |

---

## 12. REFACTORING 인계

- GREEN TC **전부 PASS** 상태에서 구조 개선 진행
- `fil`/`sent` rename, `S_KEYWORDS` 정리, Controller 분리 — **REFACTORING** 단계
- 본 문서 TC ID·assert는 **변경 금지** (회귀 기준)

---

## 13. 참조

| 문서 | 경로 |
|------|------|
| TEST_PLAN | `docs/07_RED_test_plan.md` |
| RED 실측 | `report/00_RED_coverage_report.md` |
| GREEN 리포트 | `report/01_GREEN_bugfix_report.md` |
| GREEN PCTF | `pctf/02_GREEN_PCTF_prompt.md` |
| PR (머지 전) | #4 `GREEN` → `A-01` |
