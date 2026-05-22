# PCTF — GREEN 단계 TDD 구현 (Prompt 입력용)

| 항목 | 내용 |
|------|------|
| PCTF ID | `02_GREEN_PCTF_prompt` |
| 단계 | **GREEN** (TDD 최소 구현만 — **REFACTORING 금지**) |
| 브랜치 | `GREEN` (base: `A-01`, RED 머지 반영) |
| PRD | FR-09 (`docs/07_RED_test_plan.md` §6.2 B类) |
| **선행 산출물** | `docs/07_RED_test_plan.md`, `src/test/**` (RED), `report/00_RED_coverage_report.md` |
| 본 PCTF 산출물 | 프로덕션 **최소 수정**, `report/01_GREEN_bugfix_report.md`, 단계별 Git push |
| 작성일 | 2026-05-22 |

---

## PCTF → 산출물 흐름

```text
docs/07_RED_test_plan.md (TC 표 · GREEN 열)
         │
         ▼
[pctf/02_GREEN_PCTF_prompt.md] ──PROMPT──▶ Agent
         │
         ├── (단계별) mvn test → FAIL 확인 → 최소 프로덕션 수정 → PASS 확인
         ├── (단계별) git commit + git push origin GREEN
         └── report/01_GREEN_bugfix_report.md
```

---

## P — Purpose

- **RED에서 FAIL이었던 B类 TC만** 프로덕션 **최소 수정**으로 **PASS**시킨다.
- A类(TA/FI/FH)는 RED에서 이미 PASS — GREEN에서는 **회귀 없음**만 확인한다.
- `docs/07_RED_test_plan.md` §5~§7 TC ID·메서드명·assert **변경·삭제·@Disabled 금지**.
- **REFACTORING 금지**: 메서드/클래스 rename, Controller 분리, 대규모 구조 변경, 매직넘버 정리 등은 하지 않는다.
- GREEN 종료: `mvn test` → **Failures=0**, **Errors=0**, JaCoCo line ≥ **90%** 유지.

---

## C — Context

### 필독·참조 Java

| 유형 | 경로 |
|------|------|
| TEST_PLAN | `docs/07_RED_test_plan.md` (§5 TA, §6 FI+NEUTRAL, §7 FH, §12 GREEN 인계) |
| 규칙 | `tdd_rules.yaml` → `tracks.GREEN`, `.cursorrules` |
| **수정 가능 (최소)** | `Filters.java`, `TextAnalyzer.java`, `Constants.java` |
| **수정 금지 (본 PCTF)** | `src/test/**`, `pom.xml`(JaCoCo 유지), REFACTOR 대상 전부 |
| 테스트 클래스 | `TextAnalyzerTest.java`, `FiltersTest.java`, `FileHandlerTest.java` |

### RED 종료 baseline (진입 조건)

| 항목 | 기대값 |
|------|--------|
| `mvn test` | Failures **2** (TC-NEUTRAL-01, TC-NEUTRAL-02), Errors **0** |
| A类 TA/FI/FH | 전부 **PASS** |
| JaCoCo | `com.example.demo` ≥ **90%** |

### B类 실패 원인 (§6.2, §12)

```text
"괜찮은 서비스였어요"
  → TextAnalyzer.sent(): Constants 긍정에 "괜찮" 없음 → 중립 집계
  → Filters.fil(중립): S_KEYWORDS 긍정에 "괜찮" 있음 → 긍정 분류 → 중립 필터 제외
```

**최소 수정 방향 (택1, 과도 추상화 금지)**

1. `Filters.fil` 감정 판정을 `TextAnalyzer.sent()`와 **동일 규칙**으로 맞춤 (권장), 또는  
2. `Constants` / `S_KEYWORDS` 키워드 정렬로 `sent()`·`fil()` 결과 일치.

### Git

```bash
git checkout GREEN
git pull origin GREEN   # 필요 시
# 단계 완료마다: git add <프로덕션 파일> → commit → git push origin GREEN
```

**스테이징 제외**: `target/`, `**/*.class`

---

## T — Task (단계별 TC · mvn test · Git)

각 단계 공통 루프:

```text
1) mvn test (또는 -Dtest=대상클래스) → FAIL/ PASS 확인
2) FAIL이면 프로덕션 최소 수정 (테스트 수정 금지)
3) mvn test 재실행 → 해당 TC PASS 확인
4) 변경 있으면 git commit + git push origin GREEN
```

### 단계 요약표

| 단계 | TC ID | TEST_PLAN § | RED | GREEN 목표 | 프로덕션 수정 | 커밋 메시지 (권장) |
|------|-------|-------------|-----|------------|---------------|-------------------|
| **G-00** | (baseline) | §2.3, §11 | 2 FAIL | 2 FAIL 확인 | 없음 | *(커밋 생략)* |
| **G-01** | TA-01 ~ TA-08 | §5 | PASS | 회귀 PASS 유지 | 없음(통상) | `GREEN: TA-01~TA-08 regression verified` *(변경 없으면 생략)* |
| **G-02** | FI-01 ~ FI-08 | §6.1 | PASS | 회귀 PASS 유지 | 없음(통상) | `GREEN: FI-01~FI-08 regression verified` *(변경 없으면 생략)* |
| **G-03** | **TC-NEUTRAL-01** | §6.2 | **FAIL** | **PASS** | `Filters.java` 등 최소 | `GREEN: TC-NEUTRAL-01 sentimentAnalysis_matchesNeutralFilter (FR-09)` |
| **G-04** | **TC-NEUTRAL-02** | §6.2 | **FAIL** | **PASS** | G-03 이어서 최소 | `GREEN: TC-NEUTRAL-02 mixedList_filterNeutralCount_matchesSentimentBucket (FR-09)` |
| **G-05** | FH-01 ~ FH-03 | §7 | PASS | 회귀 PASS 유지 | 없음(통상) | `GREEN: FH-01~FH-03 regression verified` *(변경 없으면 생략)* |
| **G-06** | 전체 + JaCoCo | §9, §11 | — | Failures=0, ≥90% | 없음 | `GREEN: all TEST_PLAN TC pass, mvn test 0 failures` |
| **G-07** | 리포트 | `tdd_rules` | — | 문서화 | 없음 | `GREEN: report/01_GREEN_bugfix_report.md` |

---

### G-00 — GREEN 진입 baseline

| 항목 | 내용 |
|------|------|
| 명령 | `mvn test` |
| 확인 | Failures=**2**, Errors=0; 실패 테스트명에 `TC-NEUTRAL-01`, `TC-NEUTRAL-02` 포함 |
| 수정 | 없음 |

---

### G-01 — TextAnalyzerTest (TA-01 ~ TA-08)

| TC ID | 메서드명(권장) | RED | GREEN 작업 |
|-------|----------------|-----|------------|
| TA-01 | `sent_positiveKeyword_incrementsPositive` | PASS | `mvn test -Dtest=TextAnalyzerTest` → PASS 확인 |
| TA-02 | `sent_negativeKeyword_incrementsNegative` | PASS | 동일 |
| TA-03 | `sent_noKeyword_defaultsNeutral` | PASS | 동일 |
| TA-04 | `sent_emptyList_allZero` | PASS | 동일 |
| TA-05 | `sent_mixedCounts_correct` | PASS | 동일 |
| TA-06 | `kw_deliveryCategory_matches` | PASS | 동일 |
| TA-07 | `kw_qualityCategory_matches` | PASS | 동일 |
| TA-08 | `kw_emptyList_allZero` | PASS | 동일 |

- FAIL 발생 시: **TA assert 깨뜨리지 말 것** — B类 수정이 TA를 깨뜨렸는지 역추적.
- **커밋**: 프로덕션 변경 없으면 생략.

---

### G-02 — FiltersTest A类 (FI-01 ~ FI-08)

| TC ID | 메서드명(권장) | RED | GREEN 작업 |
|-------|----------------|-----|------------|
| FI-01 | `fil_positive_only` | PASS | `mvn test -Dtest=FiltersTest#fil_positive_only` 등 → PASS |
| FI-02 | `fil_negative_only` | PASS | 동일 |
| FI-03 | `fil_neutral_keyword보통` | PASS | 동일 |
| FI-04 | `fil_all_sentiment` | PASS | 동일 |
| FI-05 | `fil_keyword_delivery` | PASS | 동일 |
| FI-06 | `fil_keyword_all` | PASS | 동일 |
| FI-07 | `fil_combined_sentimentAndKeyword` | PASS | 동일 |
| FI-08 | `fil_emptyList_returnsEmpty` | PASS | 동일 |

- G-03 전에 FI 회귀 baseline 확보용.
- **커밋**: 변경 없으면 생략.

---

### G-03 — TC-NEUTRAL-01 (핵심 GREEN #1)

| 항목 | 내용 |
|------|------|
| TC ID | **TC-NEUTRAL-01** |
| 클래스·메서드 | `FiltersTest.sentimentAnalysis_matchesNeutralFilter` |
| Given | `["괜찮은 서비스였어요"]` |
| Then | ① `sent()` → `중립`==1 ② `fil(중립,전체)` → size==1 |

**실행**

```bash
mvn test -Dtest=FiltersTest#sentimentAnalysis_matchesNeutralFilter
# → FAIL 확인 (RED 기대)

# 최소 프로덕션 수정 후

mvn test -Dtest=FiltersTest#sentimentAnalysis_matchesNeutralFilter
# → PASS 확인
```

**Git**

```bash
git add src/main/java/com/example/demo/Filters.java
# TextAnalyzer/Constants도 수정했으면 함께 add
git commit -m "GREEN: TC-NEUTRAL-01 sentimentAnalysis_matchesNeutralFilter (FR-09)"
git push origin GREEN
```

---

### G-04 — TC-NEUTRAL-02 (핵심 GREEN #2)

| 항목 | 내용 |
|------|------|
| TC ID | **TC-NEUTRAL-02** |
| 클래스·메서드 | `FiltersTest.mixedList_filterNeutralCount_matchesSentimentBucket` |
| Given | 4건 혼합 리스트 (§6.2) |
| Then | `sent().get("중립")` == `fil(중립,전체).size()` |

**실행**

```bash
mvn test -Dtest=FiltersTest#mixedList_filterNeutralCount_matchesSentimentBucket
# → FAIL 확인 (G-03만으로 PASS면 재확인)

# 추가 최소 수정 필요 시만 적용

mvn test -Dtest=FiltersTest#mixedList_filterNeutralCount_matchesSentimentBucket
# → PASS 확인
```

**Git**

- G-03과 **동일 커밋**으로 01·02 동시 PASS면:  
  `GREEN: TC-NEUTRAL-01/02 neutral filter alignment (FR-09)` 1회 커밋 가능.  
- 02만 추가 수정 시:

```bash
git commit -m "GREEN: TC-NEUTRAL-02 mixedList_filterNeutralCount_matchesSentimentBucket (FR-09)"
git push origin GREEN
```

---

### G-05 — FileHandlerTest (FH-01 ~ FH-03)

| TC ID | 메서드명(권장) | RED | GREEN 작업 |
|-------|----------------|-----|------------|
| FH-01 | `saveResult_doesNotThrow` | PASS | `mvn test -Dtest=FileHandlerTest` → PASS |
| FH-02 | `save_delegatesToSaveResult` | PASS | 동일 |
| FH-03 | `saveResult_emptyList` | PASS | 동일 |

- **커밋**: 변경 없으면 생략.

---

### G-06 — 전체 GREEN 판정

| 항목 | 내용 |
|------|------|
| 명령 | `mvn test` → `mvn test jacoco:report` |
| 확인 | Failures=**0**, Errors=**0**; JaCoCo line ≥ **90%** |
| 금지 | TC-NEUTRAL `@Disabled`, 테스트 assert 완화 |

**Git**

```bash
git commit -m "GREEN: all TEST_PLAN TC pass, mvn test 0 failures"
git push origin GREEN
```

*(프로덕션 diff 없고 리포트만 남을 경우 G-07과 합칠 수 있음)*

---

### G-07 — GREEN 리포트

| 경로 | 내용 |
|------|------|
| `report/01_GREEN_bugfix_report.md` | TC별 RED→GREEN 결과, 수정 파일·요약, `mvn test`/JaCoCo 수치 |

**Git**

```bash
git add report/01_GREEN_bugfix_report.md
git commit -m "GREEN: report/01_GREEN_bugfix_report.md"
git push origin GREEN
```

---

## F — Format

### 산출물

| 경로 | 유형 |
|------|------|
| `src/main/java/com/example/demo/Filters.java` | B类 최소 수정 (필수 가능) |
| `src/main/java/com/example/demo/TextAnalyzer.java` | 규칙 통일 시만 |
| `src/main/java/com/example/demo/Constants.java` | 키워드 정렬 시만 |
| `report/01_GREEN_bugfix_report.md` | GREEN 결과 |
| `src/test/**` | **변경 금지** |

### DoD

- [ ] G-00: baseline Failures=2 확인
- [ ] G-01~02, G-05: TA/FI/FH 회귀 PASS (또는 변경 없음 확인)
- [ ] G-03: **TC-NEUTRAL-01** PASS + 커밋·push
- [ ] G-04: **TC-NEUTRAL-02** PASS + 커밋·push
- [ ] G-06: 전체 `mvn test` Failures=0, JaCoCo ≥ 90%
- [ ] G-07: `report/01_GREEN_bugfix_report.md` + push
- [ ] REFACTORING(네이밍·SRP·Controller 분리) **미수행**
- [ ] `target/`, `*.class` 미커밋

### 금지 (GREEN에서 하지 말 것)

| 금지 | 이유 |
|------|------|
| `src/test/**` 수정·삭제·`@Disabled` | RED 계약 유지 |
| `fil`→`filterFeedbacks` 등 rename | REFACTORING |
| Controller Service 분리 | REFACTORING |
| `index.html` multiline, Logger UI | **본 PCTF 범위 외** (`작업규칙` FR-10/11 — 별도 PCTF 권장) |

---

## ★ PROMPT — Agent에 붙여넣기 (마스터)

아래 블록 **전체**를 Cursor PROMPT에 복사한다. 단계(G-03, G-04)만 따로 실행할 때는 §T 해당 절 + 본 블록의 P/C/F를 사용한다.

```
[PCTF 02 — GREEN TDD (TEST_PLAN TC만)]

P (Purpose)
- docs/07_RED_test_plan.md TC 표 기준 GREEN: B类 TC-NEUTRAL-01/02를 PASS.
- A类(TA-01~08, FI-01~08, FH-01~03) 회귀 PASS 유지.
- 프로덕션 최소 수정만. REFACTORING(네이밍·구조분리·대규모 리팩터) 금지.
- src/test 수정·@Disabled·테스트 삭제 금지.

C (Context)
- @docs/07_RED_test_plan.md @tdd_rules.yaml(tracks.GREEN)
- @TextAnalyzer.java @Filters.java @Constants.java @FileHandler.java
- @TextAnalyzerTest.java @FiltersTest.java @FileHandlerTest.java
- 브랜치 GREEN. RED 종료: mvn test Failures=2 (TC-NEUTRAL-01/02).

T (Task) — 단계별 실행
G-00: mvn test → Failures=2, TC-NEUTRAL-01/02 실패 확인 (커밋 생략)

G-01 TA-01~08: mvn test -Dtest=TextAnalyzerTest → 전부 PASS (변경 없으면 커밋 생략)

G-02 FI-01~08: mvn test -Dtest=FiltersTest → NEUTRAL 제외 8건 PASS 확인
     (또는 개별 메서드). 변경 없으면 커밋 생략

G-03 TC-NEUTRAL-01:
  1) mvn test -Dtest=FiltersTest#sentimentAnalysis_matchesNeutralFilter → FAIL 확인
  2) Filters/TextAnalyzer/Constants 최소 수정 (sent·fil 감정 규칙 일치)
  3) 재실행 → PASS
  4) git add src/main/java/... → commit -m "GREEN: TC-NEUTRAL-01 sentimentAnalysis_matchesNeutralFilter (FR-09)"
  5) git push origin GREEN

G-04 TC-NEUTRAL-02:
  1) mvn test -Dtest=FiltersTest#mixedList_filterNeutralCount_matchesSentimentBucket → FAIL 확인
  2) 추가 최소 수정(필요 시)
  3) 재실행 → PASS
  4) commit -m "GREEN: TC-NEUTRAL-02 mixedList_filterNeutralCount_matchesSentimentBucket (FR-09)"
  5) git push origin GREEN
  (01·02 동시 해결됐으면 하나의 커밋으로 묶어도 됨)

G-05 FH-01~03: mvn test -Dtest=FileHandlerTest → PASS (변경 없으면 커밋 생략)

G-06: mvn test → Failures=0, Errors=0; mvn jacoco:report → line ≥90%
     commit -m "GREEN: all TEST_PLAN TC pass, mvn test 0 failures" (변경 있을 때)
     git push origin GREEN

G-07: report/01_GREEN_bugfix_report.md (TC 표·PASS/FAIL·수정 요약·jacoco)
     commit -m "GREEN: report/01_GREEN_bugfix_report.md"
     git push origin GREEN

F (Format)
- TEST_PLAN TC ID·메서드명·assert 유지.
- 커밋: src/main/java 프로덕션, report만. target/ 제외.
- 완료 보고: 최종 mvn test 요약, jacoco %, TC-NEUTRAL PASS 여부, push 완료 여부.
```

---

## ★ PROMPT — 단계별 붙여넣기 (G-03 예시)

```
[PCTF 02 — G-03 TC-NEUTRAL-01]

@docs/07_RED_test_plan.md §6.2 @Filters.java @TextAnalyzer.java @FiltersTest.java

1. mvn test -Dtest=FiltersTest#sentimentAnalysis_matchesNeutralFilter → FAIL 확인
2. 프로덕션 최소 수정으로 TC-NEUTRAL-01 PASS (테스트 수정 금지, REFACTOR 금지)
3. 동일 테스트 재실행 → PASS 확인
4. git commit -m "GREEN: TC-NEUTRAL-01 sentimentAnalysis_matchesNeutralFilter (FR-09)"
5. git push origin GREEN
```

---

## 범위 외 (별도 진행 권장)

`작업규칙.TXT` 3단계·`tdd_rules.yaml` `required_fixes` 중 TEST_PLAN §5~§7에 **TC ID가 없는** 항목:

| FRD | 작업 | 본 PCTF |
|-----|------|---------|
| FR-10 | `index.html` multiline | 범위 외 |
| FR-11 | Logger level UI | 범위 외 |
| FR-08/03 | download/upload | 범위 외 |

→ `pctf/03_GREEN_UI_PCTF_prompt.md` 등 **별도 PCTF**로 분리 권장.

---

## 교차 참조

| 문서 | 경로 |
|------|------|
| TEST_PLAN | `docs/07_RED_test_plan.md` |
| RED PCTF | `pctf/01_RED_PCTF_prompt.md` |
| RED 리포트 | `report/00_RED_coverage_report.md` |
| GREEN 시나리오 | `docs/02_work_scenario.md` §6 |
| TDD GREEN | `tdd_rules.yaml` → `tracks.GREEN` |
| 작업규칙 3단계 | `작업규칙.TXT` (FR-10/11은 본 PCTF와 분리) |
