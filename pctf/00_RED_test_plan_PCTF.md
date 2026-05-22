# PCTF — RED TEST_PLAN 작성 (Prompt 입력용)

| 항목 | 내용 |
|------|------|
| PCTF ID | `00_RED_test_plan_PCTF` |
| 단계 | RED |
| 브랜치 | `RED` |
| **산출물 (Deliverable)** | **`docs/07_RED_test_plan.md`** — 문서 유형: **TEST_PLAN** |
| 본 파일 역할 | Agent **PROMPT에 붙여넣을** PCTF (코드·테스트 구현 금지) |
| 후속 PCTF | `pctf/01_RED_PCTF_prompt.md` — TEST_PLAN 기반 테스트·JaCoCo **구현** |

---

## PCTF → TEST_PLAN 흐름

```text
[pctf/00_RED_test_plan_PCTF.md]  ──PROMPT 입력──▶  Agent
                                                    │
                                                    ▼
                              [docs/07_RED_test_plan.md]  ◀── 산출물 TEST_PLAN
                                                    │
[pctf/01_RED_PCTF_prompt.md]   ◀── TEST_PLAN 참조 ──┘
```

---

## P — Purpose

| 항목 | 내용 |
|------|------|
| 목적 | RED 단계 **TEST_PLAN** 문서를 공식 산출물로 확정 |
| Agent가 할 일 | 소스·PRD·분석을 읽고 **`docs/07_RED_test_plan.md`만 작성** |
| Agent가 하지 않을 일 | `src/test/**` 작성, `pom.xml` 수정, 프로덕션 코드 수정 |
| TEST_PLAN이 담을 것 | PASS/FAIL TC 목록, Given-When-Then, RED `mvn test` 판정, JaCoCo·구현 순서 |

### RED 테스트 오해 방지 (TEST_PLAN §1에 반드시 기재)

- RED ≠ 모든 테스트 실패
- **A类**: baseline 동작 **PASS** 고정 (~85%)
- **B类**: TC-NEUTRAL-01/02 **FAIL** (GREEN까지, FR-09)
- RED 종료: `failures ≥ 1`(B类) + JaCoCo ≥ 90% + 프로덕션 미수정

---

## C — Context (입력 근거)

Agent는 TEST_PLAN 작성 전 아래를 읽는다.

| 우선순위 | 경로 | 추출할 정보 |
|----------|------|-------------|
| 1 | `docs/00_prd.md` | FR-19~21 |
| 2 | `docs/01_analysis.md` | P0-1 중립 불일치, §8 테스트 현황 |
| 3 | `docs/05_code_smell.md` | 사례1 감정 이중화 |
| 4 | `tdd_rules.yaml` | `tracks.RED`, `required_test_suites`, `test_cases_from_analysis` |
| 5 | `src/main/java/com/example/demo/TextAnalyzer.java` | `sent()`, `kw()` |
| 6 | `src/main/java/com/example/demo/Filters.java` | `fil()`, `S_KEYWORDS` |
| 7 | `src/main/java/com/example/demo/FileHandler.java` | `save()`, `saveResult()` |
| 8 | `README.md`, `project_purpose.md` | 기능·학습 목표 |

### baseline 감정 규칙 (TEST_PLAN §2에 요약)

```text
TextAnalyzer.sent()  → Constants 긍/부만, 나머지 "중립"
Filters.fil()        → S_KEYWORDS 별도 Map, "괜찮" 긍정·중립 중복 → 긍정 우선
→ "괜찮은 서비스였어요": sent=중립, fil(중립)=제외 → B类 FAIL
```

---

## T — Task

| 순서 | 작업 |
|------|------|
| T-1 | 위 Context 문서·소스 분석 |
| T-2 | **`docs/07_RED_test_plan.md`** 생성 (F § 템플릿 준수) |
| T-3 | TC 표에 **RED 기대 결과** 열: `PASS` / `FAIL` / `N/A` |
| T-4 | B类 TC-NEUTRAL-01/02에 **실패 원인·GREEN 인계** 명시 |
| T-5 | (선택) `docs/03_work_guide.md` docs 인덱스에 `07_RED_test_plan.md` 한 줄 추가 |

**코드·테스트 파일 생성 금지.**

---

## F — Format (산출물: `docs/07_RED_test_plan.md`)

### 파일 규칙

| 항목 | 값 |
|------|-----|
| 경로 | `docs/07_RED_test_plan.md` |
| 문서 유형 | **TEST_PLAN** |
| 명명 | `docs/` 규칙 `{NN}_{제목}.md`, 순번 **07** |
| 언어 | 한국어 |

### TEST_PLAN 필수 목차 (Agent가 이 구조로 작성)

```markdown
# RED 단계 TEST_PLAN — Feedback Analyzer

## 1. 문서 메타
## 2. RED 테스트 전략 (PASS/FAIL 구분, mvn test 허용 형태)
## 3. 테스트 범위 (In/Out of Scope)
## 4. 테스트 클래스·파일 매핑
## 5. TextAnalyzerTest — TC 표 (TA-xx, Given-When-Then, RED: PASS/FAIL)
## 6. FiltersTest — TC 표 (FI-xx + TC-NEUTRAL-01/02, RED: PASS/FAIL)
## 7. FileHandlerTest — TC 표 (FH-xx)
## 8. RED 결과 매트릭스 (A/B/C类)
## 9. 커버리지·JaCoCo 요구 (FR-20)
## 10. 구현 순서 (JaCoCo → 테스트 클래스 → mvn test → report)
## 11. RED 완료 판정 / 잘못된 판정 예
## 12. GREEN 인계 (TC-NEUTRAL 통과 조건)
## 13. 참조 문서
```

### TC 표 필수 컬럼

| TC ID | 테스트 클래스 | 메서드명(권장) | Given | When | Then | RED 기대 | FRD | GREEN |
|-------|---------------|----------------|-------|------|------|----------|-----|-------|

- **RED 기대**: `PASS` | `FAIL` | `N/A` 만 사용
- B类 최소 2건: `TC-NEUTRAL-01`, `TC-NEUTRAL-02` → **FAIL**

### TEST_PLAN DoD

- [ ] `docs/07_RED_test_plan.md` 존재, 목차 §1~§13 충족
- [ ] TA/FI/FH + TC-NEUTRAL 전 TC ID 부여
- [ ] RED ≠ 전부 실패 설명 포함
- [ ] `src/test/**` 미생성

---

## ★ PROMPT — Agent에 붙여넣기 (본 PCTF)

아래 블록 **전체**를 Cursor PROMPT에 복사한다.

```
[PCTF 00 — RED TEST_PLAN 작성]

P (Purpose)
- 산출물은 오직 docs/07_RED_test_plan.md (문서 유형: TEST_PLAN).
- RED 단계에서 어떤 JUnit TC를 PASS/FAIL로 둘지 명세한다.
- 코드·pom·src/test 작성 금지.

C (Context)
- 읽기: docs/00_prd.md(FR-19~21), docs/01_analysis.md(P0-1), docs/05_code_smell.md(사례1),
  tdd_rules.yaml(tracks.RED), TextAnalyzer.java, Filters.java, FileHandler.java.
- baseline: TextAnalyzer.sent()는 긍/부만 검사 후 중립; Filters.fil()은 S_KEYWORDS 별도 규칙.
  "괜찮은 서비스였어요" → 분석 중립 vs 중립 필터 제외 = 불일치.

T (Task)
1. 위 파일 분석
2. docs/07_RED_test_plan.md 작성 — pctf/00_RED_test_plan_PCTF.md §F 목차·TC 표 컬럼 준수
3. TC 포함:
   - TextAnalyzerTest: TA-01~08 (RED 기대 PASS)
   - FiltersTest: FI-01~08 (PASS), TC-NEUTRAL-01/02 (FAIL, FR-09)
   - FileHandlerTest: FH-01~03 (PASS)
4. §8 매트릭스: A类 PASS ~85%, B类 FAIL 2건, C类 커버리지 보강
5. §10 구현 순서: JaCoCo → 테스트 클래스 → failures=2 확인 → jacoco 90% → report

F (Format)
- 경로: docs/07_RED_test_plan.md
- 한국어, Given-When-Then, RED 기대 열 필수
- TEST_PLAN만 출력; 다른 파일 수정 최소(docs 인덱스 1줄만 허용)

완료 시: 생성한 docs/07_RED_test_plan.md 경로와 TC 개수(PASS/FAIL) 요약만 답변.
```

---

## 완료 후 다음 단계

| 순서 | 작업 | PCTF |
|------|------|------|
| 1 | `docs/07_RED_test_plan.md` 확정 | **본 문서 (00)** |
| 2 | TEST_PLAN 기반 테스트·JaCoCo 구현 | `pctf/01_RED_PCTF_prompt.md` |

---

## 교차 참조

| 문서 | 경로 |
|------|------|
| TEST_PLAN 산출물 | `docs/07_RED_test_plan.md` |
| RED 구현 PCTF | `pctf/01_RED_PCTF_prompt.md` |
| TDD | `tdd_rules.yaml` |
