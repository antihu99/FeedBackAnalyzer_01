# GREEN 단계 버그 수정 보고서

| 항목 | 내용 |
|------|------|
| 단계 | GREEN |
| 브랜치 | `GREEN` |
| 근거 TEST_PLAN | `docs/07_RED_test_plan.md` |
| PCTF | `pctf/02_GREEN_PCTF_prompt.md` |
| FRD | FR-09 (감정 분석·필터 일치) |
| 실행일 | 2026-05-22 |
| 명령 | `mvn test`, `mvn test jacoco:report` |

---

## 1. G-00 — GREEN 진입 baseline (수정 전)

| 항목 | 값 |
|------|-----|
| Tests run | **34** |
| Failures | **2** |
| Errors | **0** |
| 실패 TC | TC-NEUTRAL-01, TC-NEUTRAL-02 |

`Filters.java` HEAD(RED 머지) 기준 — `S_KEYWORDS`로 감정 판정 시 `괜찮`이 긍정으로 분류되어 `TextAnalyzer.sent()`와 불일치.

---

## 2. G-06 — GREEN 종료 판정

| 항목 | 값 |
|------|-----|
| Tests run | **34** |
| Failures | **0** |
| Errors | **0** |
| JaCoCo line (`com.example.demo`) | **90.8%** (248 / 273) |
| FR-20 | **충족** (≥ 90%) |

---

## 3. TC별 RED → GREEN 결과

| TC ID | 테스트 메서드 | RED | GREEN | 비고 |
|-------|---------------|-----|-------|------|
| TA-01 ~ TA-08 | TextAnalyzerTest | PASS | **PASS** | 회귀 없음 |
| FI-01 ~ FI-08 | FiltersTest (A类) | PASS | **PASS** | 회귀 없음 |
| **TC-NEUTRAL-01** | `sentimentAnalysis_matchesNeutralFilter` | **FAIL** | **PASS** | FR-09 |
| **TC-NEUTRAL-02** | `mixedList_filterNeutralCount_matchesSentimentBucket` | **FAIL** | **PASS** | FR-09 |
| FH-01 ~ FH-03 | FileHandlerTest | PASS | **PASS** | 회귀 없음 |
| COV-* / contextLoads | 기타 | PASS | **PASS** | 회귀 없음 |

### B类 수정 전·후

| TC ID | RED 실패 요약 | GREEN 결과 |
|-------|---------------|------------|
| TC-NEUTRAL-01 | `sent()` 중립 1, `fil(중립)` 0건 | 둘 다 1건 일치 |
| TC-NEUTRAL-02 | `sent()` 중립 2, `fil(중립)` 1건 | 건수 일치 (2) |

---

## 4. 프로덕션 수정 요약

| 파일 | 변경 | REFACTORING |
|------|------|-------------|
| `Filters.java` | `fil()` 감정 판정을 `resolveSentimentLikeAnalyzer()`로 통일 — `Constants.SENTIMENT_KEYWORDS`만 사용 (`TextAnalyzer.sent()`와 동일 규칙) | **미수행** (rename·구조분리 없음) |
| `TextAnalyzer.java` | **미수정** | — |
| `Constants.java` | **미수정** | — |
| `src/test/**` | **미수정** | — |

### 수정 핵심 (FR-09)

```text
Before: Filters.fil → S_KEYWORDS (긍정에 "괜찮" 포함) → 긍정 분류
After:  Filters.fil → Constants.SENTIMENT_KEYWORDS (긍/부만, 없으면 중립) → TextAnalyzer.sent()와 동일
```

`S_KEYWORDS` Map은 키워드 필터 경로에서 미사용 상태로 유지 (GREEN 범위 내 최소 변경).

---

## 5. 단계별 실행 기록

| 단계 | 작업 | 결과 |
|------|------|------|
| G-00 | baseline `mvn test` | Failures=2 확인 |
| G-01 | TextAnalyzerTest 회귀 | PASS (8/8) |
| G-02 | FiltersTest A类 회귀 | PASS (8/8) |
| G-03~04 | TC-NEUTRAL-01/02 | PASS (동일 수정으로 해결) |
| G-05 | FileHandlerTest 회귀 | PASS (3/3) |
| G-06 | 전체 + JaCoCo | Failures=0, ≥90% |
| G-07 | 본 리포트 | 작성 완료 |

---

## 6. 범위 외 (본 PCTF 미수행)

| FRD | 항목 | 담당 |
|-----|------|------|
| FR-10 | `index.html` multiline | 별도 PCTF |
| FR-11 | Logger level UI | 별도 PCTF |
| FR-08/03 | download/upload | 별도 PCTF |

---

## 7. REFACTORING 인계

- `fil`/`sent` rename, Controller Service 분리, `S_KEYWORDS` 정리 → **REFACTORING** 브랜치
- GREEN 종료 조건: TEST_PLAN §5~§7 TC 전부 PASS, JaCoCo ≥ 90%

---

## 8. 참조

| 문서 | 경로 |
|------|------|
| TEST_PLAN | `docs/07_RED_test_plan.md` |
| RED 리포트 | `report/00_RED_coverage_report.md` |
| GREEN PCTF | `pctf/02_GREEN_PCTF_prompt.md` |
| TDD | `tdd_rules.yaml` → `tracks.GREEN` |
