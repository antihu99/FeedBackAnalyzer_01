# PCTF — RED 단계 구현 (Prompt 입력용)

| 항목 | 내용 |
|------|------|
| PCTF ID | `01_RED_PCTF_prompt` |
| 단계 | RED |
| 브랜치 | `RED` |
| PRD | FR-19, FR-20, FR-21 |
| **선행 산출물** | **`docs/07_RED_test_plan.md`** (TEST_PLAN) |
| **선행 PCTF** | `pctf/00_RED_test_plan_PCTF.md` (TEST_PLAN 미존재 시 먼저 실행) |
| 본 PCTF 산출물 | `pom.xml`, `src/test/**`, `report/01_RED_coverage_report.md` |

---

## PCTF → 산출물 흐름

```text
docs/07_RED_test_plan.md (TEST_PLAN)
         │
         ▼
[pctf/01_RED_PCTF_prompt.md] ──PROMPT──▶ Agent
         │
         ├── pom.xml (JaCoCo)
         ├── src/test/.../*Test.java
         └── report/01_RED_coverage_report.md
```

---

## P — Purpose

- **TEST_PLAN에 정의된 TC를 JUnit으로 구현**하고 JaCoCo 90%를 달성한다.
- **B类(TC-NEUTRAL-*)는 RED에서 FAIL**이어야 한다. 프로덕션 수정은 **GREEN**.
- `docs/07_RED_test_plan.md`와 구현·리포트가 **1:1 대응**해야 한다.

---

## C — Context

| 항목 | 내용 |
|------|------|
| 필독 | `@docs/07_RED_test_plan.md` 전체 |
| 규칙 | `tdd_rules.yaml` → `tracks.RED`, `.cursorrules` |
| 현재 | `DemoApplicationTests.contextLoads()` 만 존재 |

```bash
git checkout RED
mvn test
mvn test -Dtest=TextAnalyzerTest
mvn test jacoco:report
```

---

## T — Task (실행 순서)

| # | 작업 | TEST_PLAN 참조 |
|---|------|----------------|
| T-1 | JaCoCo `pom.xml` | §9 |
| T-2 | `TextAnalyzerTest` — TA-01~08, RED **PASS** | §5 |
| T-3 | `FiltersTest` FI-01~08 **PASS**, TC-NEUTRAL **FAIL** | §6 |
| T-4 | `FileHandlerTest` FH-01~03 **PASS** | §7 |
| T-5 | `mvn test` failures=**2**, jacoco ≥ 90% | §8, §9 |
| T-6 | `report/01_RED_coverage_report.md` | §10 #8 |
| T-7 | 커밋·PR RED→A-01 | — |

---

## F — Format

### 구현 산출물

| 경로 | 유형 |
|------|------|
| `docs/07_RED_test_plan.md` | TEST_PLAN (선행, 변경 최소) |
| `pom.xml` | JaCoCo |
| `src/test/java/com/example/demo/*Test.java` | JUnit |
| `report/01_RED_coverage_report.md` | RED 결과 |

### DoD

- [ ] TEST_PLAN §5~§7 모든 TC ID가 테스트 코드에 존재
- [ ] `mvn test`: Failures=**2** (TC-NEUTRAL-01/02), Errors=0
- [ ] JaCoCo `com.example.demo` ≥ 90%
- [ ] 프로덕션 `TextAnalyzer`/`Filters` **미수정**
- [ ] `target/`, `*.class` 미커밋

---

## ★ PROMPT — Agent에 붙여넣기

```
[PCTF 01 — RED 구현]

P (Purpose)
- docs/07_RED_test_plan.md(TEST_PLAN)에 따라 JUnit·JaCoCo 구현.
- A类 TC → RED에서 PASS. B类 TC-NEUTRAL-01/02 → RED에서 FAIL(2건).
- 프로덕션 버그 수정 금지(GREEN).

C (Context)
- @docs/07_RED_test_plan.md @tdd_rules.yaml @TextAnalyzer.java @Filters.java @FileHandler.java
- 브랜치 RED. TEST_PLAN §10 구현 순서 따름.

T (Task)
1. pom.xml JaCoCo (§9)
2. TextAnalyzerTest: TA-01~08 (§5) → mvn test -Dtest=TextAnalyzerTest → failures=0
3. FiltersTest: FI-01~08 PASS (§6.1), TC-NEUTRAL-01/02 FAIL (§6.2)
4. FileHandlerTest: FH-01~03 (§7)
5. mvn test → failures=2 확인; jacoco:report ≥90%
6. report/01_RED_coverage_report.md — TEST_PLAN TC 표 + 실제 PASS/FAIL + jacoco %

F (Format)
- TEST_PLAN TC ID·메서드명 준수. @Disabled·테스트 삭제 금지.
- 커밋: pom, src/test, report만.

완료 시: mvn test 요약, jacoco %, FAIL 2건 TC ID 보고.
```

---

## 공식 프롬프트 (작업규칙.TXT)

```
@DemoApplicationTests.java 커버리지 90% 달성을 위한 JUnit 5 테스트를 작성해줘.
TextAnalyzer·Filters·FileHandler 각 클래스별 TC 포함.
```

> **반드시** `@docs/07_RED_test_plan.md` 와 함께 사용.

---

## 교차 참조

| 문서 | 경로 |
|------|------|
| TEST_PLAN PCTF | `pctf/00_RED_test_plan_PCTF.md` |
| TEST_PLAN 산출물 | `docs/07_RED_test_plan.md` |
| TDD | `tdd_rules.yaml` |
