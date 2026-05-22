# RED 단계 커버리지 보고서

| 항목 | 내용 |
|------|------|
| 단계 | RED |
| 브랜치 | `RED` |
| 근거 TEST_PLAN | `docs/07_RED_test_plan.md` |
| 실행일 | 2026-05-22 |
| 명령 | `mvn test`, `mvn jacoco:report` |

---

## 1. `mvn test` 요약

| 항목 | 값 |
|------|-----|
| Tests run | **34** |
| Failures | **2** |
| Errors | **0** |
| Skipped | 0 |

RED 단계에서 **의도적 실패 2건** — 정상 RED 종료 조건 충족.

---

## 2. JaCoCo

| 항목 | 값 |
|------|-----|
| 패키지 | `com.example.demo` |
| Line coverage | **90.9%** (249 / 274) |
| 제외 | `DemoApplication` |
| 리포트 | `target/site/jacoco/index.html` |
| FR-20 | **충족** (≥ 90%) |

---

## 3. TC 분류표 (TEST_PLAN 대비)

| TC ID | 테스트 클래스 | TEST_PLAN RED | 실제 결과 | 비고 |
|-------|---------------|---------------|-----------|------|
| TA-01 ~ TA-08 | TextAnalyzerTest | PASS | **PASS** | 8/8 |
| FI-01 ~ FI-08 | FiltersTest | PASS | **PASS** | 8/8 |
| **TC-NEUTRAL-01** | FiltersTest | FAIL | **FAIL** | filtered.size 0 (기대 1) |
| **TC-NEUTRAL-02** | FiltersTest | FAIL | **FAIL** | sent 중립 2 vs fil 1 |
| FH-01 ~ FH-03 | FileHandlerTest | PASS | **PASS** | 3/3 |
| COV-* | Session, Feedback, Logger, UI, Web | PASS | **PASS** | 커버리지 보강 |
| contextLoads | DemoApplicationTests | PASS | **PASS** | 1 |

### B类 실패 메시지 요약

| TC ID | 실패 요약 |
|-------|-----------|
| TC-NEUTRAL-01 | `괜찮은 서비스였어요` — `sent()` 중립 1건, `fil(중립,전체)` 0건 |
| TC-NEUTRAL-02 | 혼합 4건 — `sent().get("중립")==2`, `fil(중립,전체).size()==1` |

---

## 4. 추가한 테스트 파일

| 파일 | TC 수 |
|------|-------|
| `TextAnalyzerTest.java` | 8 |
| `FiltersTest.java` | 10 |
| `FileHandlerTest.java` | 3 |
| `FeedbackControllerWebTest.java` | 8 |
| `SessionTest.java` | 1 |
| `FeedbackTest.java` | 1 |
| `LoggerTest.java` | 1 |
| `UIComponentsTest.java` | 1 |

`pom.xml`: JaCoCo 플러그인 추가.

---

## 5. 프로덕션 코드 변경

| 항목 | RED |
|------|-----|
| `TextAnalyzer.java` | **미수정** |
| `Filters.java` | **미수정** |
| `FileHandler.java` | **미수정** |
| `FeedbackController.java` | **미수정** |

---

## 6. GREEN 인계 (FR-09)

| # | 작업 |
|---|------|
| 1 | `TextAnalyzer` ↔ `Filters` 감정 규칙 단일화 |
| 2 | TC-NEUTRAL-01, TC-NEUTRAL-02 **PASS** |
| 3 | `mvn test` failures **0**, JaCoCo ≥ 90% 유지 |
| 4 | 본 TEST_PLAN TC 삭제·@Disabled 금지 |

---

## 7. 참조

| 문서 | 경로 |
|------|------|
| TEST_PLAN | `docs/07_RED_test_plan.md` |
| PCTF 구현 | `pctf/01_RED_PCTF_prompt.md` |
| TDD | `tdd_rules.yaml` |
