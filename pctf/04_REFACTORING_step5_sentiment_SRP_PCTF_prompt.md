# PCTF — REFACTORING 5단계: 감정 로직 SRP (Prompt 입력용)

| 항목 | 내용 |
|------|------|
| PCTF ID | `04_REFACTORING_step5_sentiment_SRP` |
| 시나리오 | `docs/02_work_scenario.md` §7.2 |
| 단계 | **REFACTORING — 5단계** (중복·긴 함수, FR-14) |
| 브랜치 | `REFACTORING` |
| PRD | FR-14 |
| **선행** | `pctf/03` 완료 (rename 반영) |
| 본 PCTF 산출물 | `SentimentClassifier`(또는 동등), SRP 부록, **단계 커밋** |
| 예상 시간 | 1.5h |
| 작성일 | 2026-05-22 |

---

## P — Purpose

- **감정 판별 로직 단일 클래스** (FR-14): `TextAnalyzer`·`Filters`가 **동일 구현**을 위임.
- `TextAnalyzer` 내 **20줄 이상** 메서드·중복 루프 **추출·통합** (SRP).
- `resolveSentimentLikeAnalyzer` / `analyzeSentiment` 내부 중복 제거.
- **동작·TC 불변**: TC-NEUTRAL-01/02 포함 TEST_PLAN **PASS** 유지.

---

## C — Context

### 필독

| 유형 | 경로 |
|------|------|
| 시나리오 | `docs/02_work_scenario.md` §7.2 |
| PRD | `docs/00_prd.md` FR-14 |
| 분석 | `docs/01_analysis.md` P0-1 (이미 GREEN 수정됨 — 구조만 단일화) |
| 코드 스멜 | `docs/05_code_smell.md` 사례1 |
| 선행 PCTF | `pctf/03_REFACTORING_step4_naming_PCTF_prompt.md` |

### 수정 대상

| 파일 | 작업 |
|------|------|
| **신규** | `SentimentClassifier.java` (또는 `SentimentService`) — `classify(String text): String` |
| `TextAnalyzer.java` | 감정·키워드 분석에서 감정 부분 위임, 긴 메서드 추출 |
| `Filters.java` | `filterFeedbacks` 감정 판정 → Classifier 위임 (`S_KEYWORDS` 감정 분기 제거) |
| `docs/09_REFACTOR_SRP_proposal.md` **또는** `report/02_REFACTORING_report.md` §부록 | SRP 제안·Before/After |

### 수정 금지 (본 단계)

| 금지 | 이유 |
|------|------|
| `controller`/`service`/`model`/`config` **패키지 분리** | **6단계** FR-15~16 |
| URL·CSV 변경 | 계약 불변 |
| TEST_PLAN TC 삭제·assert 완화 | 회귀 |

### Git

```bash
git pull origin REFACTORING
git commit -m "REFACTOR step5: SentimentClassifier SRP and TextAnalyzer extract (FR-14)"
git push origin REFACTORING
```

---

## T — Task

### R5-00 — baseline

| 명령 | `mvn test` |
| 확인 | Failures=0 (step4 이후) |

---

### R5-01 — SentimentClassifier (FR-14)

1. 단일 public API 예: `String classify(String text)` — `Constants`/`Sentiment` enum 사용.
2. 규칙: GREEN과 동일 — 긍/부 키워드만, 없으면 `중립`.
3. `TextAnalyzer.analyzeSentiment`: 피드백 순회 + Classifier 호출.
4. `Filters.filterFeedbacks`: 감정 필터 시 Classifier만 사용.
5. `mvn test -Dtest=FiltersTest` → TC-NEUTRAL-01/02 **PASS** 필수.

---

### R5-02 — 긴 메서드·중복 추출

1. `TextAnalyzer` 20줄 이상 블록 → private 메서드 또는 Classifier로 이동.
2. `Filters.filterFeedbacks` 키워드 필터 로직만 Filters에 유지.
3. `mvn test` + `jacoco:report` → ≥90%.

---

### R5-03 — SRP 문서

| 경로 | 내용 |
|------|------|
| `docs/09_REFACTOR_SRP_proposal.md` (권장) | 책임 분리 표, Before/After 다이어그램(텍스트), FR-14 매핑 |

`docs/` 순번 규칙: `03_work_guide.md` §3.1 — 09 = 최대+1.

---

### R5-04 — Git

```bash
git add src/main/java/ docs/09_REFACTOR_SRP_proposal.md
git commit -m "REFACTOR step5: SentimentClassifier SRP and TextAnalyzer extract (FR-14)"
git push origin REFACTORING
```

---

## F — Format

### DoD

- [ ] SentimentClassifier 1곳에서 감정 규칙 정의
- [ ] TextAnalyzer·Filters 위임, 중복 제거
- [ ] TC-NEUTRAL-01/02 PASS
- [ ] SRP 부록 문서 1건
- [ ] mvn test 0 failures, JaCoCo ≥90%
- [ ] commit + push

---

## ★ PROMPT — Agent에 붙여넣기

```
[PCTF 04 — REFACTORING 5단계: 감정 SRP FR-14]

P (Purpose)
- FR-14: 감정 판별 단일 클래스(SentimentClassifier 등). TextAnalyzer·Filters 위임.
- TextAnalyzer 20줄 이상·중복 로직 추출. 동작·TEST_PLAN TC 불변.
- 패키지 분리(controller/service)는 하지 않음(6단계).

C (Context)
- @docs/02_work_scenario.md §7.2 @docs/05_code_smell.md 사례1
- @TextAnalyzer.java @Filters.java @Constants.java (step4 rename 반영 후)
- @FiltersTest.java TC-NEUTRAL-01/02 @docs/07_RED_test_plan.md
- 선행: pctf/03 step4 완료, 브랜치 REFACTORING

T (Task)
R5-00: mvn test → 0 failures

R5-01: SentimentClassifier 생성, analyzeSentiment·filterFeedbacks 위임
  mvn test -Dtest=FiltersTest → NEUTRAL PASS

R5-02: TextAnalyzer 긴 메서드 추출, mvn test jacoco:report ≥90%

R5-03: docs/09_REFACTOR_SRP_proposal.md (SRP Before/After, FR-14)

R5-04:
  git add src/main/java docs/09_REFACTOR_SRP_proposal.md
  git commit -m "REFACTOR step5: SentimentClassifier SRP and TextAnalyzer extract (FR-14)"
  git push origin REFACTORING

F (Format)
- 완료 보고: Classifier 경로, NEUTRAL TC 결과, jacoco %, commit 해시.
```

---

## 교차 참조

| 문서 | 경로 |
|------|------|
| 이전 | `pctf/03_REFACTORING_step4_naming_PCTF_prompt.md` |
| 다음 | `pctf/05_REFACTORING_step6_controller_SRP_PCTF_prompt.md` |
