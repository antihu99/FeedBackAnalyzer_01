# New_Feature 단계 TC 실행·회귀 검증 결과

| 항목 | 내용 |
|------|------|
| 문서 유형 | **TEST_RESULTS** (New_Feature 단계 실측) |
| 단계 | New_Feature (FR-17, FR-18, FR-19~21 회귀) |
| 브랜치 | `new_feature` |
| 작성 근거 | `docs/10_feature_schema.md`, `pctf/06_New_Feature_PCTF_prompt.md` |
| 실행일 | 2026-05-22 |
| 검증 명령 | `mvn test`, `mvn test jacoco:report` |
| 상세 리포트 | `report/04_New_Feature_report.md` |

---

## 1. 문서 역할

| 구분 | 문서 | 역할 |
|------|------|------|
| 스키마·AC | `10_feature_schema.md` | Feature TC ID·CSV/JSON 명세 |
| TEST_PLAN (회귀) | `07_RED_test_plan.md` | TA/FI/FH·TC-NEUTRAL **기대** |
| GREEN 실측 | `08_GREEN_test_results.md` | GREEN 종료 baseline (34 tests) |
| **본 문서** | `11_New_Feature_test_results.md` | New_Feature **실측** + **회귀** |
| 요약 리포트 | `report/04_New_Feature_report.md` | 구현·Git·AC 요약 |

---

## 2. 실행 요약 (`mvn test`)

| 항목 | GREEN 종료 (`08`) | **New_Feature 종료 (본 문서)** |
|------|-------------------|-------------------------------|
| Tests run | 34 | **41** (+7 Feature·Web) |
| Failures | 0 | **0** |
| Errors | 0 | **0** |
| Skipped | 0 | **0** |
| JaCoCo line (`com.example.demo`) | 90.8% (248/273) | **90.4%** (413/457) |
| FR-20 | 충족 | **충족** (≥ 90%) |
| FR-19~21 (회귀) | 충족 | **충족** |

> JaCoCo 분모 증가(신규 클래스·라인)로 비율은 90.8% → 90.4%이나, **90% 기준 충족**.

---

## 3. Feature TC 결과 (FR-17, FR-18)

`docs/10_feature_schema.md` §Feature TC. New_Feature에서 **신규 추가 5건** — 전부 **PASS**.

| TC ID | 테스트 클래스 | 메서드 | FR | Given | Then (실측) | 결과 |
|-------|---------------|--------|-----|-------|-------------|------|
| **TC-TREND-01** | `TrendServiceTest` | `loadTrendData_returnsSeriesByDate` | FR-17 | classpath `test_feedback_trend.csv` | labels 비어 있지 않음, counts 8건 합계 | **PASS** |
| **TC-TREND-02** | `TrendServiceTest` | `loadTrendData_missingFile_returnsEmptyWithMessage` | FR-17 | 존재하지 않는 경로 | labels/counts empty, `message` not null | **PASS** |
| **TC-KEYWORD-01** | `KeywordFileRepositoryTest` | `saveAndLoad_roundTrip` | FR-18 | @TempDir JSON 파일 | save 후 load 시 키워드·카테고리 동일 | **PASS** |
| **TC-KEYWORD-02** | `KeywordConfigServiceTest` | `addKeyword_persistsAfterSimulatedRestart` | FR-18 | 파일에 `영속키워드` 추가 후 새 Service 로드 | 재로드 목록에 키워드 포함 | **PASS** |
| **TC-KEYWORD-03** | `KeywordConfigServiceTest` | `removeKeyword_excludedFromClassification` | FR-18 | `유니크테스트` 추가 후 삭제 | classify 중립, 긍정 필터 0건 | **PASS** |

### 3.1. FR-17 Trend 상세 (TC-TREND-01)

| 검증 항목 | 기대 | 실측 |
|-----------|------|------|
| CSV 행 수 | 8 data rows | `totalCount() == 8` |
| 날짜 라벨 | 5일 (`2026-05-01` ~ `2026-05-05`) | labels.size() == counts.size() ≥ 1 |
| 리소스 | `src/main/resources/test_feedback_trend.csv` | classpath 로드 성공 |

### 3.2. FR-18 File DB 상세

| 검증 항목 | TC | 실측 |
|-----------|-----|------|
| 파일 영속 round-trip | TC-KEYWORD-01 | JSON save/load 일치 |
| 재기동 시뮬레이션 | TC-KEYWORD-02 | 두 번째 `KeywordConfigService` 로드 시 키워드 유지 |
| 삭제 후 분류·필터 | TC-KEYWORD-03 | `SentimentClassifier`·`Filters` 동일 규칙, 긍정 제외 |
| HTTP 연동 | Web `keywords_add` / `keywords_remove` | status 200 (§7) |

---

## 4. 회귀 검증 — TEST_PLAN 필수 TC (FR-19, FR-21)

`docs/07_RED_test_plan.md` §5~§7 **21건** + B类 TC-NEUTRAL **2건**. New_Feature 종료 시 **전부 PASS** (GREEN 대비 **회귀 없음**).

| 구분 | TC ID 범위 | 건수 | GREEN (`08`) | New_Feature (회귀) |
|------|------------|------|--------------|-------------------|
| A类 | TA-01 ~ TA-08 | 8 | PASS | **PASS** |
| A类 | FI-01 ~ FI-08 | 8 | PASS | **PASS** |
| B类 | TC-NEUTRAL-01, TC-NEUTRAL-02 | 2 | PASS | **PASS** |
| A类 | FH-01 ~ FH-03 | 3 | PASS | **PASS** |
| **필수 소계** | — | **21** | PASS 21 | **PASS 21** |

### 4.1. TC-NEUTRAL 회귀 (FR-09 유지)

| TC ID | 메서드 | GREEN | New_Feature | 비고 |
|-------|--------|-------|-------------|------|
| TC-NEUTRAL-01 | `sentimentAnalysis_matchesNeutralFilter` | PASS | **PASS** | File DB·`SentimentClassifier` 단일화 유지 |
| TC-NEUTRAL-02 | `mixedList_filterNeutralCount_matchesSentimentBucket` | PASS | **PASS** | 감정 집계·필터 건수 일치 |

> `KeywordConfigService` 도입 후에도 in-memory 시드·테스트용 `SentimentClassifier()` 호환으로 **중립 필터 동작 유지** 확인.

---

## 5. 회귀 검증 — 테스트 클래스별 (전체 41건)

| 테스트 클래스 | tests | failures | errors | 신규/변경 |
|---------------|-------|----------|--------|-----------|
| `TextAnalyzerTest` | 8 | 0 | 0 | 회귀 |
| `FiltersTest` | 10 | 0 | 0 | 회귀 |
| `FileHandlerTest` | 3 | 0 | 0 | 회귀 |
| `FeedbackControllerWebTest` | 10 | 0 | 0 | **+2** (keywords) |
| `TrendServiceTest` | 2 | 0 | 0 | **신규** |
| `KeywordFileRepositoryTest` | 1 | 0 | 0 | **신규** |
| `KeywordConfigServiceTest` | 2 | 0 | 0 | **신규** |
| `FeedbackTest` | 1 | 0 | 0 | 회귀 |
| `SessionTest` | 1 | 0 | 0 | 회귀 |
| `LoggerTest` | 1 | 0 | 0 | 회귀 |
| `UIComponentsTest` | 1 | 0 | 0 | 회귀 (KeywordConfig 주입) |
| `DemoApplicationTests` | 1 | 0 | 0 | 회귀 |
| **합계** | **41** | **0** | **0** | +7 vs GREEN |

---

## 6. Web·HTTP 회귀 (기존 5 path + Feature path)

### 6.1. 기존 API 계약 (`api_contract_frozen`) — 회귀 PASS

| Method | Path | Web TC | New_Feature |
|--------|------|--------|-------------|
| GET | `/` | `index_returnsOk` | **PASS** |
| POST | `/analyze` | `analyze_withText`, `analyze_emptyText` | **PASS** |
| POST | `/upload` | `upload_csv` | **PASS** |
| POST | `/filter` | `filter_positive`, `filter_emptyResult`, `filter_noFeedbacks` | **PASS** |
| GET | `/download` | `download_afterFilter` | **PASS** |

### 6.2. Feature API (FR-18)

| Method | Path | Web TC | 결과 |
|--------|------|--------|------|
| POST | `/keywords/add` | `keywords_add` | **PASS** |
| POST | `/keywords/remove` | `keywords_remove` | **PASS** |

---

## 7. JaCoCo (FR-20)

| 항목 | 값 |
|------|-----|
| 패키지 | `com.example.demo` (+ 하위 `service`, `controller`, `model`, `repository`, `config`) |
| LINE covered | **413** |
| LINE missed | **44** |
| **Line coverage** | **90.4%** |
| FR-20 (≥ 90%) | **충족** |
| 리포트 경로 | `target/site/jacoco/index.html` |

### 7.1. 신규·변경 클래스 (Feature)

| 클래스 | LINE covered | LINE missed | 비고 |
|--------|--------------|-------------|------|
| `TrendService` | 22 | 8 | TC-TREND-01/02 |
| `KeywordConfigService` | 56 | 7 | TC-KEYWORD-02/03 |
| `KeywordFileRepository` | 9 | 1 | TC-KEYWORD-01 |
| `TrendSeries` | 10 | 0 | — |
| `KeywordStore` | 9 | 0 | — |
| `FeedbackService` | 87 | 21 | Web·Trend·keyword |
| `FeedbackController` | 11 | 4 | +keywords mapping |

`DemoApplication` — jacoco exclude (기존과 동일)

---

## 8. PCTF 단계별 검증 (NF-00 ~ NF-05)

| PCTF 단계 | 검증 내용 | 실측 |
|-----------|-----------|------|
| NF-00 | `mvn test` baseline 0 failures | **PASS** |
| NF-01 | 브랜치 `new_feature` | 완료 |
| NF-02 | CSV·`10_feature_schema.md` | 완료 |
| NF-03 | Feature TC 5건 추가 | **PASS** (본 문서 §3) |
| NF-04 | Trend UI + File DB 구현 | **PASS** + 회귀 §4 |
| NF-05 | `report/04_New_Feature_report.md` | 완료 |

---

## 9. AC·완료 판정

### 9.1. FR-17~18 AC (`10_feature_schema.md`)

| AC | 판정 | 근거 TC/검증 |
|----|------|--------------|
| classpath CSV 날짜별 건수 | ✅ | TC-TREND-01 |
| Trend UI 표시 | ✅ | `index.html` Trend 섹션, Web `/` 200 |
| CSV 없을 때 안내 | ✅ | TC-TREND-02 |
| File DB 시드 | ✅ | `KeywordConfigService.loadOrSeed()` |
| 키워드 CRUD UI/API | ✅ | `/keywords/add`, `/keywords/remove` Web PASS |
| 재기동 영속 | ✅ | TC-KEYWORD-02 |
| 삭제 키워드 미반영 | ✅ | TC-KEYWORD-03 |

### 9.2. FR-19~21

| FR | 판정 | 근거 |
|----|------|------|
| FR-19 | ✅ | JUnit 5 — Feature 5 + TEST_PLAN 21 PASS |
| FR-20 | ✅ | JaCoCo line **90.4%** |
| FR-21 | ✅ | `mvn test` Failures=0, Errors=0 |

### 9.3. 종합

| 상태 | 조건 | 판정 |
|------|------|------|
| ✅ 완료 | Feature TC 5건 PASS | 충족 |
| ✅ 완료 | TEST_PLAN 21 + NEUTRAL 2 회귀 PASS | 충족 |
| ✅ 완료 | HTTP 5 path 회귀 PASS | 충족 |
| ✅ 완료 | JaCoCo ≥ 90% | 충족 |
| ⏳ | PR `new_feature` → `A-01` | 리뷰·머지 대기 |

---

## 10. 참조

| 문서 | 경로 |
|------|------|
| Feature 스키마 | `docs/10_feature_schema.md` |
| TEST_PLAN | `docs/07_RED_test_plan.md` |
| GREEN 실측 | `docs/08_GREEN_test_results.md` |
| New_Feature PCTF | `pctf/06_New_Feature_PCTF_prompt.md` |
| New_Feature 리포트 | `report/04_New_Feature_report.md` |
| 시나리오 §8 | `docs/02_work_scenario.md` |
