# RED 단계 TEST_PLAN — Feedback Analyzer

| 항목 | 내용 |
|------|------|
| 문서 유형 | **TEST_PLAN** |
| 단계 | RED (FR-19, FR-20, FR-21) |
| 브랜치 | `RED` |
| 작성 근거 | `pctf/00_RED_test_plan_PCTF.md` (PCTF 00) |
| 분석 근거 | `docs/01_analysis.md` P0-1, `docs/05_code_smell.md` 사례1, `tdd_rules.yaml` |
| 연관 PCTF | `pctf/01_RED_PCTF_prompt.md` |
| 작성일 | 2026-05-22 |

---

## 1. 문서 메타

| 항목 | 값 |
|------|-----|
| 목적 | RED 단계 JUnit·JaCoCo의 **공식 테스트 명세** (PASS/FAIL 기대 고정) |
| 독자 | 개발자, Agent, 리뷰어 |
| 범위 | `TextAnalyzer`, `Filters`, `FileHandler` 단위 테스트 + JaCoCo 90% |
| 산출물 위치 | `docs/07_RED_test_plan.md` (본 문서) |

---

## 2. RED 테스트 전략

### 2.1. RED ≠ 모든 테스트 실패

| 구분 | 비율(권장) | RED `mvn test` | 역할 |
|------|------------|----------------|------|
| **A类 — 통과 고정** | ~85% | **PASS** | baseline 동작 회귀 명세 |
| **B类 — 버그 노출** | 2건 필수 | **FAIL** | FR-09, GREEN 수정 입력 |
| **C类 — 커버리지 보강** | 필요 시 | **PASS** | JaCoCo 90% 미달 시만 추가 |

### 2.2. baseline 감정 규칙 (P0-1)

```text
TextAnalyzer.sent()
  → Constants.SENTIMENT_KEYWORDS: "긍정" / "부정" 키워드만 contains 검사
  → 매칭 없으면 무조건 "중립" 집계 (중립 키워드 목록 없음)

Filters.fil(sFilter, kFilter)
  → Filters.S_KEYWORDS (TextAnalyzer와 별도 Map)
  → 긍정 → 부정 → 중립 순 검사; "괜찮"은 긍정·중립 양쪽에 존재 → 긍정 우선
```

| 입력 문장 | `sent()` 집계 | `fil("중립","전체")` | B类 관련 |
|-----------|---------------|----------------------|----------|
| `배송이 최고예요` | 긍정 | 제외 | — |
| `보통이에요` | 중립 (기본값) | 포함 (`보통` 키워드) | — |
| `괜찮은 서비스였어요` | 중립 | **제외** (긍정 분류) | **TC-NEUTRAL-01 FAIL** |
| `화가 나고 최악이에요` | 부정 | 제외 (중립 필터 시) | — |

### 2.3. RED 종료 시 `mvn test` 허용 형태

| 항목 | RED | GREEN |
|------|-----|-------|
| Failures | **≥ 1** (B类 2건 권장) | **0** |
| Errors | **0** | **0** |
| JaCoCo line (`com.example.demo`) | **≥ 90%** | **≥ 90%** |
| 프로덕션 코드 수정 | **금지** | 허용 (최소 수정) |

**정상 RED 종료 예**: `Tests: 24, Failures: 2, Errors: 0`, JaCoCo 92%

### 2.4. Fixture 공통 규칙

- `new Feedback("텍스트")` 사용; `Constants`·`CATEGORY_KEYWORDS` **실제 데이터** (감정 로직 mock 금지)
- `TextAnalyzer`·`Filters`는 `@SpringBootTest` 없이 **단위 생성** 또는 `@ExtendWith(MockitoExtension)` + `new` 인스턴스
- `Session` static 상태에 의존하지 않음

---

## 3. 테스트 범위

### 3.1. In Scope

| 대상 | FRD |
|------|-----|
| `TextAnalyzerTest` | FR-19, FR-04, FR-05 |
| `FiltersTest` | FR-19, FR-06, FR-09 |
| `FileHandlerTest` | FR-19 |
| JaCoCo `pom.xml` | FR-20, NFR-04 |
| `DemoApplicationTests.contextLoads` | 유지 (FR-21 기반) |

### 3.2. Out of Scope (RED)

| 항목 | 담당 단계 | FRD |
|------|-----------|-----|
| 감정 규칙 단일화·중립 필터 수정 | GREEN | FR-09 |
| `index.html` multiline | GREEN | FR-10 |
| Logger level UI | GREEN | FR-11 |
| TC-DOWNLOAD-01, TC-UPLOAD-01 | GREEN | FR-08, FR-03 |
| Controller Service 분리 | REFACTORING | FR-15~16 |

---

## 4. 테스트 클래스·파일 매핑

| TC 그룹 | 테스트 클래스 | 클래스 under test | 파일 경로 | TC 수 |
|---------|---------------|-------------------|-----------|-------|
| TA | `TextAnalyzerTest` | `TextAnalyzer` | `src/test/java/com/example/demo/TextAnalyzerTest.java` | 8 |
| FI + NEUTRAL | `FiltersTest` | `Filters` | `src/test/java/com/example/demo/FiltersTest.java` | 10 |
| FH | `FileHandlerTest` | `FileHandler` | `src/test/java/com/example/demo/FileHandlerTest.java` | 3 |
| — | `DemoApplicationTests` | `DemoApplication` | 기존 유지 | 1 |

---

## 5. TextAnalyzerTest — TC 표

| TC ID | 테스트 클래스 | 메서드명(권장) | Given | When | Then | RED 기대 | FRD | GREEN |
|-------|---------------|----------------|-------|------|------|----------|-----|-------|
| TA-01 | TextAnalyzerTest | `sent_positiveKeyword_incrementsPositive` | 피드백 1건: `"배송이 최고예요"` | `textAnalyzer.sent(list)` | 반환 Map `긍정`==1, `부정`==0, `중립`==0 | **PASS** | FR-04 | N/A |
| TA-02 | TextAnalyzerTest | `sent_negativeKeyword_incrementsNegative` | 피드백 1건: `"화가 나고 최악이에요"` | `sent(list)` | `부정`==1 | **PASS** | FR-04 | N/A |
| TA-03 | TextAnalyzerTest | `sent_noKeyword_defaultsNeutral` | 피드백 1건: `"보통이에요"` (긍/부 키워드 없음) | `sent(list)` | `중립`==1 | **PASS** | FR-04 | N/A |
| TA-04 | TextAnalyzerTest | `sent_emptyList_allZero` | 빈 `List<Feedback>` | `sent(list)` | 긍/부/중립 모두 0 | **PASS** | FR-19 | N/A |
| TA-05 | TextAnalyzerTest | `sent_mixedCounts_correct` | 긍 1건 + 부 1건 + 중립 1건 (`보통이에요`) | `sent(list)` | 각 감정 1, 합계 3 | **PASS** | FR-04 | N/A |
| TA-06 | TextAnalyzerTest | `kw_deliveryCategory_matches` | `"택배 배송이 지연됐어요"` | `kw(list)` | `배송` 카운트 ≥ 1 | **PASS** | FR-05 | N/A |
| TA-07 | TextAnalyzerTest | `kw_qualityCategory_matches` | `"품질이 별로예요"` | `kw(list)` | `품질` 카운트 ≥ 1 | **PASS** | FR-05 | N/A |
| TA-08 | TextAnalyzerTest | `kw_emptyList_allZero` | 빈 목록 | `kw(list)` | `Constants.CATEGORY_KEYWORDS` 모든 키 0 | **PASS** | FR-19 | N/A |

---

## 6. FiltersTest — TC 표

### 6.1. A类 — PASS

| TC ID | 테스트 클래스 | 메서드명(권장) | Given | When | Then | RED 기대 | FRD | GREEN |
|-------|---------------|----------------|-------|------|------|----------|-----|-------|
| FI-01 | FiltersTest | `fil_positive_only` | `"정말 만족합니다"` 1건 | `filters.fil(list,"긍정","전체")` | 결과 size==1, 텍스트 일치 | **PASS** | FR-06 | N/A |
| FI-02 | FiltersTest | `fil_negative_only` | `"불만이에요"` 1건 | `fil(list,"부정","전체")` | size==1 | **PASS** | FR-06 | N/A |
| FI-03 | FiltersTest | `fil_neutral_keyword보통` | `"보통이에요"` 1건 | `fil(list,"중립","전체")` | size==1 | **PASS** | FR-06 | N/A |
| FI-04 | FiltersTest | `fil_all_sentiment` | 긍·부·중립 문장 각 1건 (3건) | `fil(list,"전체","전체")` | size==3 | **PASS** | FR-06 | N/A |
| FI-05 | FiltersTest | `fil_keyword_delivery` | `"택배 배송 지연"` | `fil(list,"전체","배송")` | size≥1, 배송 sub 키워드 매칭 | **PASS** | FR-06 | N/A |
| FI-06 | FiltersTest | `fil_keyword_all` | 서로 다른 카테고리 2건 | `fil(list,"전체","전체")` | 입력 전체 반환 | **PASS** | FR-06 | N/A |
| FI-07 | FiltersTest | `fil_combined_sentimentAndKeyword` | `"배송이 빨라서 최고예요"` | `fil(list,"긍정","배송")` | size==1 (교집합) | **PASS** | FR-06 | N/A |
| FI-08 | FiltersTest | `fil_emptyList_returnsEmpty` | 빈 목록 | `fil(list,"중립","전체")` | empty, 예외 없음 | **PASS** | FR-19 | N/A |

### 6.2. B类 — FAIL (GREEN에서 PASS 필수)

| TC ID | 테스트 클래스 | 메서드명(권장) | Given | When | Then (요구사항) | RED 기대 | FRD | GREEN |
|-------|---------------|----------------|-------|------|-----------------|----------|-----|-------|
| TC-NEUTRAL-01 | FiltersTest | `sentimentAnalysis_matchesNeutralFilter` | `["괜찮은 서비스였어요"]` | ① `textAnalyzer.sent(data)` ② `filters.fil(data,"중립","전체")` | ① `중립`==1 ② filtered.size()==1 (동일 피드백 포함) | **FAIL** | FR-09 | **PASS** |
| TC-NEUTRAL-02 | FiltersTest | `mixedList_filterNeutralCount_matchesSentimentBucket` | ① `"최고예요"` ② `"최악이에요"` ③ `"괜찮은 서비스"` ④ `"보통이에요"` | `sent(all)` 후 `fil(all,"중립","전체").size()` | `sent().get("중립")` == 필터 결과 건수 | **FAIL** | FR-09 | **PASS** |

**B类 실패 원인 (baseline, 수정 금지)**  
- `괜찮`이 `Constants` 긍정 목록에 없어 `sent()`는 중립.  
- `S_KEYWORDS` 긍정에 `괜찮` 포함 → `fil()`은 긍정 분류 → 중립 필터에서 누락.

---

## 7. FileHandlerTest — TC 표

| TC ID | 테스트 클래스 | 메서드명(권장) | Given | When | Then | RED 기대 | FRD | GREEN |
|-------|---------------|----------------|-------|------|------|----------|-----|-------|
| FH-01 | FileHandlerTest | `saveResult_doesNotThrow` | Feedback 1건 리스트 | `fileHandler.saveResult(list)` | 예외 없음 | **PASS** | FR-19 | N/A |
| FH-02 | FileHandlerTest | `save_delegatesToSaveResult` | Feedback 2건 | `fileHandler.save(list)` | 예외 없음 (saveResult 위임) | **PASS** | FR-19 | N/A |
| FH-03 | FileHandlerTest | `saveResult_emptyList` | 빈 리스트 | `saveResult(empty)` | 예외 없음 | **PASS** | FR-19 | N/A |

> Controller 미연동(Lava Flow)이어도 FR-19·커버리지 목적으로 단위 TC 작성.

---

## 8. RED 결과 매트릭스 (A/B/C类)

| 구분 | TC ID 범위 | 건수 | RED 기대 | 비고 |
|------|------------|------|----------|------|
| A类 | TA-01 ~ TA-08 | 8 | PASS | TextAnalyzer |
| A类 | FI-01 ~ FI-08 | 8 | PASS | Filters |
| B类 | TC-NEUTRAL-01, TC-NEUTRAL-02 | 2 | **FAIL** | Filters + TextAnalyzer 연동 assert |
| A类 | FH-01 ~ FH-03 | 3 | PASS | FileHandler |
| C类 | COV-01 ~ COV-03 (선택) | 0~3 | PASS | 90% 미달 시만 |
| **합계 (필수)** | — | **21** | **FAIL 2** | + `contextLoads` 1 |

### C类 — 커버리지 보강 (선택)

| TC ID | 테스트 클래스 | Given | When | Then | RED 기대 |
|-------|---------------|-------|------|------|----------|
| COV-01 | FeedbackTest | 생성자·getter | 인스턴스 생성 | text 반환 | PASS |
| COV-02 | SessionTest | static 필드 | 접근·기본값 | 예외 없음 | PASS |
| COV-03 | FeedbackControllerTest | MockMvc (선택) | `GET /` | 200 | PASS |

---

## 9. 커버리지·JaCoCo 요구 (FR-20)

| 항목 | 요구사항 |
|------|----------|
| 플러그인 | `org.jacoco:jacoco-maven-plugin` (`tdd_rules.yaml`) |
| 대상 패키지 | `com.example.demo` line coverage **≥ 90%** |
| 제외 | `com.example.demo.DemoApplication` |
| 명령 | `mvn test jacoco:report` |
| 리포트 경로 | `target/site/jacoco/index.html` |
| 금지 | B类 FAIL TC를 `@Disabled`로 90% 달성 |

---

## 10. 구현 순서

| # | 작업 | 산출물 | 검증 |
|---|------|--------|------|
| 1 | TEST_PLAN 확정 | `docs/07_RED_test_plan.md` | 본 문서 |
| 2 | JaCoCo 설정 | `pom.xml` | `mvn test` |
| 3 | TextAnalyzerTest TA-01~08 | `TextAnalyzerTest.java` | failures=0 |
| 4 | FiltersTest FI-01~08 | `FiltersTest.java` | failures=0 |
| 5 | FiltersTest B类 추가 | TC-NEUTRAL-01/02 | `mvn test` failures=**2** |
| 6 | FileHandlerTest FH-01~03 | `FileHandlerTest.java` | failures=2 유지 |
| 7 | C类 보강 (필요 시) | COV-* | jacoco ≥ 90% |
| 8 | RED 리포트 | `report/00_RED_coverage_report.md` | FR-20 |

> 구현 Agent: `pctf/01_RED_PCTF_prompt.md`

---

## 11. RED 완료 판정 / 잘못된 판정

| 상태 | 조건 | 판정 |
|------|------|------|
| ✅ 완료 | failures=2, errors=0, jacoco≥90%, B类 TC 존재, 프로덕션 미수정 | RED 종료 |
| ❌ 미완 | failures=0 | B类 누락 — 버그 미노출 |
| ❌ 미완 | failures≥대부분 | A类 테스트 오류 |
| ❌ 미완 | jacoco<90% | FR-20 미충족 |
| ❌ 미완 | RED PR에 `Filters`/`TextAnalyzer` 로직 수정 | GREEN 침범 |

---

## 12. GREEN 인계

| 항목 | 내용 |
|------|------|
| FRD | FR-09 (감정·필터 일치) |
| 수정 방향 | `SentimentClassifier` 단일화 또는 `Filters.fil`이 `TextAnalyzer`와 동일 규칙 사용 |
| 통과 조건 | TC-NEUTRAL-01, TC-NEUTRAL-02 → **PASS** |
| 유지 | 본 TEST_PLAN의 TC ID·메서드명·assert 의도 (테스트 삭제 금지) |
| 이후 | FR-10, FR-11 (`pctf` GREEN 단계) |

---

## 13. 참조 문서

| 문서 | 경로 |
|------|------|
| PCTF 00 (TEST_PLAN 작성) | `pctf/00_RED_test_plan_PCTF.md` |
| PCTF 01 (구현) | `pctf/01_RED_PCTF_prompt.md` |
| PRD FR-19~21 | `docs/00_prd.md` §5.5 |
| P0-1 중립 불일치 | `docs/01_analysis.md` |
| 코드 스멜 사례1 | `docs/05_code_smell.md` |
| TDD RED 트랙 | `tdd_rules.yaml` → `tracks.RED` |
| RED 시나리오 | `docs/02_work_scenario.md` §5 |
| To-Do M1 | `docs/06_todo_list.md` |
