# PCTF — REFACTORING 4단계: 네이밍·상수 (Prompt 입력용)

| 항목 | 내용 |
|------|------|
| PCTF ID | `03_REFACTORING_step4_naming` |
| 시나리오 | `docs/02_work_scenario.md` §7.1 |
| 단계 | **REFACTORING — 4단계** (네이밍·상수, FR-12~13) |
| 브랜치 | `REFACTORING` (base: `A-01`, GREEN 머지 반영) |
| PRD | FR-12, FR-13 |
| **선행** | GREEN 완료 — `mvn test` Failures=0, JaCoCo ≥90% |
| 본 PCTF 산출물 | rename·Constants 정리, **단계 커밋 1회+** |
| 예상 시간 | 1h |
| 작성일 | 2026-05-22 |

---

## PCTF → 산출물 흐름

```text
docs/07_RED_test_plan.md (TC — 동작 불변)
         │
         ▼
[pctf/03 § PROMPT] ──▶ fil/sent/kw rename + Constants 정리
         │
         ├── mvn test → 0 failures
         ├── git commit + git push origin REFACTORING
         └── (선행) pctf/04, pctf/05
```

---

## P — Purpose

- **도메인 네이밍** 개선 (FR-12): `fil`→`filterFeedbacks`, `sent`→`analyzeSentiment`, `kw`→`analyzeKeywords`, `fil_data`→`filteredFeedbacksForExport`.
- **매직 스트링·중복 키워드** 정리 (FR-13): `Constants` → enum 또는 설정 클래스 후보, 감정/카테고리 키워드 중복 축소.
- **동작 불변**: `docs/07_RED_test_plan.md` TC **전부 PASS** 유지 (assert 의도 동일).
- **계약 불변**: HTTP 경로·CSV `text` 컬럼 변경 금지 (`.cursorrules`, `tdd_rules.yaml` → `tracks.REFACTOR.contract_immutable`).

---

## C — Context

### 필독

| 유형 | 경로 |
|------|------|
| 시나리오 | `docs/02_work_scenario.md` §7.1 |
| PRD | `docs/00_prd.md` FR-12~13 |
| rename_map | `.cursorrules` → `naming_refactor_targets.rename_map` |
| TDD | `tdd_rules.yaml` → `tracks.REFACTOR` |
| TEST_PLAN | `docs/07_RED_test_plan.md` |
| GREEN 결과 | `docs/08_GREEN_test_results.md` |

### 수정 대상 (본 단계)

| 파일 | 작업 |
|------|------|
| `TextAnalyzer.java` | `sent`→`analyzeSentiment`, `kw`→`analyzeKeywords` |
| `Filters.java` | `fil`→`filterFeedbacks` |
| `Constants.java` | 매직 스트링·중복 키워드 → enum/설정 클래스 (과도 추상화 금지) |
| `FeedbackController.java` | 메서드 호출·`fil_data` 필드 rename |
| `src/test/**` | 프로덕션 rename에 맞춰 **호출부만** 갱신 (TC assert 의도 유지) |

### 수정 금지 (본 단계)

| 금지 | 이유 |
|------|------|
| 감정 로직 **새 클래스** 추출 | **5단계** (FR-14) |
| Controller→Service 분리 | **6단계** (FR-15~16) |
| URL·CSV 계약 변경 | REFACTOR 계약 |
| TEST_PLAN TC 삭제·`@Disabled` | 회귀 기준 유지 |

### rename_map (공식)

| Before | After |
|--------|-------|
| `fil` | `filterFeedbacks` |
| `sent` | `analyzeSentiment` |
| `kw` | `analyzeKeywords` |
| `fil_data` | `filteredFeedbacksForExport` |

### Git

```bash
git checkout REFACTORING
git pull origin REFACTORING
# 완료 후:
git add <변경 파일>
git commit -m "REFACTOR step4: domain naming and constants (FR-12, FR-13)"
git push origin REFACTORING
```

**스테이징 제외**: `target/`, `**/*.class`

---

## T — Task

### R4-00 — 진입 baseline

| 항목 | 내용 |
|------|------|
| 명령 | `mvn test` |
| 확인 | Failures=**0**, Errors=**0** |
| 수정 | 없음 |

---

### R4-01 — 메서드 rename (FR-12)

1. `TextAnalyzer`: `analyzeSentiment`, `analyzeKeywords` (시그니처·반환 타입 동일).
2. `Filters`: `filterFeedbacks`.
3. `FeedbackController`: 호출부·`filteredFeedbacksForExport` 필드.
4. 테스트: `TextAnalyzerTest`, `FiltersTest`, `FeedbackControllerWebTest` 호출부 갱신.
5. `mvn test` → 전체 PASS.

**JUnit 테스트 메서드명** (`sent_positiveKeyword_…` 등)은 TEST_PLAN 식별자 — **변경하지 않아도 됨** (프로덕션 API만 rename).

---

### R4-02 — Constants 정리 (FR-13)

1. 감정 라벨 `"긍정"`/`"부정"`/`"중립"` → enum 또는 `Sentiment` 상수 (1곳 정의).
2. `SENTIMENT_KEYWORDS` / `CATEGORY_KEYWORDS` 중복 문자열 축소.
3. `Filters.S_KEYWORDS` — 미사용이면 제거 또는 5단계 전까지 주석·deprecated (본 단계에서 dead code 정리 권장).
4. `mvn test` + `mvn test jacoco:report` → line ≥ **90%** 유지.

---

### R4-03 — Git commit·push

```bash
git add src/main/java/ src/test/java/
git commit -m "REFACTOR step4: domain naming and constants (FR-12, FR-13)"
git push origin REFACTORING
```

---

## F — Format

### DoD

- [ ] R4-00: baseline 0 failures
- [ ] `fil`/`sent`/`kw`/`fil_data` rename 완료 (`.cursorrules` map 일치)
- [ ] FR-13: Constants enum/설정화 (최소 1개 개선, PR 본문에 요약)
- [ ] `mvn test` Failures=0, JaCoCo ≥90%
- [ ] commit + `git push origin REFACTORING`

### 산출물

| 경로 | 유형 |
|------|------|
| `src/main/java/**` | rename·Constants |
| `src/test/java/**` | 호출부 동기화 |

---

## ★ PROMPT — Agent에 붙여넣기

```
[PCTF 03 — REFACTORING 4단계: 네이밍·상수 FR-12~13]

P (Purpose)
- FR-12: fil→filterFeedbacks, sent→analyzeSentiment, kw→analyzeKeywords, fil_data→filteredFeedbacksForExport
- FR-13: Constants 매직 스트링·중복 키워드 → enum/설정 클래스 (최소 변경)
- mvn test 전부 PASS 유지. HTTP URL·CSV text 컬럼 불변.
- 5단계(감정 단일 클래스)·6단계(Service 분리)는 하지 않음.

C (Context)
- @docs/02_work_scenario.md §7.1 @docs/00_prd.md FR-12~13
- @.cursorrules naming_refactor_targets @tdd_rules.yaml tracks.REFACTOR
- @Constants.java @TextAnalyzer.java @Filters.java @FeedbackController.java
- @TextAnalyzerTest.java @FiltersTest.java @docs/07_RED_test_plan.md
- 브랜치 REFACTORING

T (Task)
R4-00: mvn test → Failures=0 확인

R4-01: 프로덕션·테스트 호출부 rename (.cursorrules rename_map)
  mvn test → PASS

R4-02: Constants enum/설정 정리, S_KEYWORDS dead code 정리
  mvn test jacoco:report → line ≥90%

R4-03:
  git add src/main/java src/test/java
  git commit -m "REFACTOR step4: domain naming and constants (FR-12, FR-13)"
  git push origin REFACTORING

F (Format)
- TEST_PLAN TC assert 의도 불변. 완료 시: rename 목록, mvn test 요약, commit 해시.
```

---

## 교차 참조

| 문서 | 경로 |
|------|------|
| 다음 단계 | `pctf/04_REFACTORING_step5_sentiment_SRP_PCTF_prompt.md` |
| REFACTOR 시나리오 | `docs/02_work_scenario.md` §7 |
