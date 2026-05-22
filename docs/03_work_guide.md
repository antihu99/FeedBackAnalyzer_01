# Feedback Analyzer — 작업 안내

> **근거**: `작업규칙.TXT`, `project_purpose.md`, `README.md`, `docs/00_prd.md`, `docs/01_analysis.md`  
> **작성일**: 2026-05-21  
> **대상**: 실습 참여자·리뷰어

---

## 1. 문서 역할

| 문서 | 역할 |
|------|------|
| `00_prd.md` | **무엇을** 만들 것인가 (요구사항) |
| `01_analysis.md` | **현재 코드**가 어떤 상태인가 (갭·미션) |
| `02_work_scenario.md` | **어떤 순서로** 진행하는가 (시나리오) |
| **본 문서** | **규칙·Git·산출물·품질**을 어떻게 지킬 것인가 (안내) |
| `.cursorrules` | Cursor Agent 코딩·브랜치·금지 규칙 (YAML) |
| `tdd_rules.yaml` | RED/GREEN/REFACTOR TDD·커버리지 규칙 (YAML) |

---

## 2. 프로젝트 한눈에 보기

### 2.1. 실행 (`README.md`)

```bash
git clone https://github.com/antihu99/FeedBackAnalyzer_01.git
cd FeedBackAnalyzer_01
mvn spring-boot:run
# http://localhost:8080
```

### 2.2. CSV (`README.md`)

- 필수 컬럼: **`text`**
- 1행: 헤더 (업로드 시 스킵)

### 2.3. 개발 전략 5단계 (`작업규칙.TXT`)

1. 문제 코드·요구사항 분석  
2. OCP·SRP·입력 검증 구현  
3. TC 구현  
4. 추가 요구사항 + TC  
5. 회고·발표  

---

## 3. 산출물·폴더 규칙

### 3.1. docs/ — 작업 산출물

| 규칙 | 내용 |
|------|------|
| 형식 | `{순번2자리}_{제목}.md` |
| 순번 | 폴더 내 최대 번호 + 1 |

**현재 docs 인덱스**

| 파일 | 설명 |
|------|------|
| `00_prd.md` | 제품 요구사항 |
| `01_analysis.md` | 코드베이스·미션 분석 |
| `02_work_scenario.md` | 단계별 시나리오 |
| `03_work_guide.md` | 본 작업 안내 |
| `04_mom_test.md` | Mom Test 질문·가정 검증 |
| `05_code_smell.md` | 코드 스멜 사례 3건 |
| `06_todo_list.md` | To-Do·마일스톤·회귀 체크리스트 (Phase 6) |
| `07_RED_test_plan.md` | RED 단계 **TEST_PLAN** (TC PASS/FAIL 명세) |
| `08_GREEN_test_results.md` | GREEN 단계 **TC 실행 결과** (머지 전 실측) |

### 3.1.0. pctf/ — 단계별 PCTF (Prompt 입력)

| 파일 | 산출물 |
|------|--------|
| `00_RED_test_plan_PCTF.md` | `docs/07_RED_test_plan.md` |
| `01_RED_PCTF_prompt.md` | `src/test/**`, JaCoCo, `report/00_RED_coverage_report.md` |
| `02_GREEN_PCTF_prompt.md` | 프로덕션 최소 수정, `docs/08_GREEN_test_results.md`, `report/01_GREEN_bugfix_report.md` |

### 3.1.1. 프로젝트 루트 — Agent·TDD 규칙 (YAML)

| 파일 | 경로 | 설명 |
|------|------|------|
| Cursor 규칙 | [`.cursorrules`](../.cursorrules) | 스택, 브랜치, API 고정, P0 수정, Git·docs 규칙 |
| TDD 규칙 | [`tdd_rules.yaml`](../tdd_rules.yaml) | RED/GREEN/REFACTOR/FEATURE 트랙, JaCoCo 90%, 필수 TC |

- 코딩 전: **`.cursorrules`** → **`tdd_rules.yaml`** 순으로 확인
- 현재 브랜치에 맞는 `tracks.{RED|GREEN|REFACTOR|FEATURE}` 섹션만 적용

### 3.2. report/ — 결과 보고서

| 규칙 | 형식 `{NN}_{제목}.md` |
|------|----------------------|
| 예시 | `00_RED_coverage_report.md` |
| 예시 | `01_GREEN_bugfix_report.md` |
| 예시 | `04_retrospective.md` |

### 3.3. prompting/ — Agent 기록

| 파일 | 내용 |
|------|------|
| `00_SPEC_prompt.md` | SPEC 단계 Agent 대화·산출물 |
| `01_RED_prompt.md` | RED 단계 Agent 대화·PCTF·JUnit |
| `02_GREEN_prompt.md` | GREEN 단계 Agent 대화·FR-09 수정 |
| `User_prompt.md` | 사용자 프롬프트 **표** (#1~#40) + 요약 |
| `GIT_prompt.md` | git 명령·PR·커밋 이력 (중복 제거) |

> 단계 종료·머지 후 `User_prompt.md`·`GIT_prompt.md`·해당 `{NN}_prompt.md` 를 **동기화**한다.

#### User_prompt 표 예시

| 일시 | 단계 | 요약 | 원문 |
|------|------|------|------|
| 2026-05-21 | SPEC | PRD·analysis 재작성 | (프롬프트 전문) |

---

## 4. Git·브랜치 안내

### 4.1. 브랜치 구조 (`작업규칙.TXT`)

```text
main
 └── A-01                 ← 통합 (저장소: A-01)
      ├── SPEC
      ├── RED
      ├── GREEN
      ├── REFACTORING
      └── New_Feature

최종: A-01 → main (기능 브랜치 삭제 안 함)
```

> 규칙 문서 `A_01`/`spec` 표기 ≠ 실제 `A-01`/`SPEC` — **실제 브랜치명 우선**.

### 4.2. 단계 ↔ 브랜치 ↔ PRD

| 단계 | 브랜치 | PRD | 공식 프롬프트 |
|------|--------|-----|---------------|
| 1 | SPEC | 문서 | `@Codebase 전체 구조·미션 안내 분석해줘` |
| 2 | RED | FR-19~21 | `@DemoApplicationTests.java 커버리지 90%…` |
| 3 | GREEN | FR-09~11 | Filters 중립 / index multiline / Logger UI |
| 4 | REFACTORING | FR-12~13 | Constants·TextAnalyzer 네이밍 |
| 5 | REFACTORING | FR-14 | TextAnalyzer 추출·중복 |
| 6 | REFACTORING | FR-15~16 | FeedbackController 분리 |
| 7 | New_Feature | FR-17~18 | Trend + File DB |

### 4.3. PR 규칙

| ID | 규칙 |
|----|------|
| PR-01 | 기능 브랜치 → **base `A-01`** |
| PR-02 | 단계당 1 PR 권장 |
| PR-03 | 본문: 변경 요약 + `mvn test` + 관련 docs/report 링크 |
| PR-04 | 최종만 `A-01` → `main` |
| PR-05 | 머지 후 기능 브랜치 **삭제 금지** |

### 4.4. 커밋 규칙

| ID | 규칙 |
|----|------|
| CM-01 | 메시지에 단계명 (예: `SPEC 단계 진행`, `GREEN: 중립 필터 수정`) |
| CM-02 | **`.class` 커밋 금지** |
| CM-03 | `target/` 커밋 금지 — `.gitignore` 권장 |
| CM-04 | 비밀·키 파일 금지 |

#### 커밋 전 체크

```bash
git status
# Changes to be committed에 .class 없는지 확인
git add docs/ src/ pom.xml
git commit -m "SPEC 단계 진행"
git push -u origin SPEC
```

---

## 5. Dual-Track 품질 안내

> 상세 트랙·TC·merge 조건: [`tdd_rules.yaml`](../tdd_rules.yaml)

### 5.1. PART 3 — TDD (`작업규칙.TXT`)

| 트랙 | 역할 |
|------|------|
| **RED** | 실패 테스트 먼저 (버그·요구 반영) |
| **GREEN** | 최소 구현으로 통과 |

- 외부 계약(URL, CSV `text`) **변경 최소화**

### 5.2. PART 4 — Refactoring

- GREEN TC **유지**
- HTTP API·CSV 형식 **불변**
- 구조·네이밍·중복만 개선

### 5.3. PART 5 — QA

- JaCoCo **≥ 90%**
- 단계 종료 시 `report/` 보고서

### 5.4. PART 6 — Git

- `red` → `green` → `refactoring` → `new_feature` 순서 권장
- 각 단계 A-01 머지 후 다음 브랜치

---

## 6. PART 2 문서화 Phase (병행)

| Phase | 산출물 | 코드 |
|-------|--------|------|
| 1 | 문제·Mom Test | **금지** |
| 2 | BCE·계약·RED 목록 | 설계만 |
| 3 | [`.cursorrules`](../.cursorrules) + [`tdd_rules.yaml`](../tdd_rules.yaml) | Agent·TDD 규칙 (YAML) ✅ |
| 4 | Epic·Gherkin | 추적 |
| 5 | PRD | `00_prd.md` ✅ |
| 6 | README·To-Do | [`06_todo_list.md`](06_todo_list.md) ✅ · README 갱신 예정 |

---

## 7. 단계별 작업 체크리스트

### SPEC

- [ ] `00_prd.md` 작성
- [ ] `01_analysis.md` (src + PRD 분석)
- [ ] `02_work_scenario.md`, `03_work_guide.md`
- [ ] `04_mom_test.md`, `05_code_smell.md` (Phase 1)
- [ ] `.cursorrules`, `tdd_rules.yaml` (Phase 3)
- [ ] `06_todo_list.md` (Phase 6)
- [ ] `prompting/` · `report/00_SPEC_phase_report.md`
- [ ] PR SPEC → A-01
- [ ] `prompting/` 기록

### RED

- [ ] `docs/07_RED_test_plan.md` (TEST_PLAN, `pctf/00_RED_test_plan_PCTF.md`)
- [ ] JaCoCo 설정
- [ ] TextAnalyzer / Filters / FileHandler TC
- [ ] coverage ≥ 90%
- [ ] `report/00_RED_coverage_report.md`

### GREEN

- [ ] `pctf/02_GREEN_PCTF_prompt.md` — `docs/07_RED_test_plan.md` TC 단계별 (TC-NEUTRAL-01/02)
- [ ] FR-09 중립 필터 (`01_analysis.md` P0-1)
- [ ] FR-10 multiline
- [ ] FR-11 로그 UI
- [ ] `mvn test` Green
- [ ] `report/01_GREEN_bugfix_report.md`

### REFACTORING

- [ ] FR-12~16
- [ ] 패키지 분리
- [ ] `report/02_REFACTORING_report.md`

### New_Feature

- [ ] `test_feedback_trend.csv`
- [ ] Trend UI + File DB
- [ ] `report/03_New_Feature_report.md`

### 회고

- [ ] `report/04_retrospective.md`
- [ ] PR A-01 → main

---

## 8. Agent(Cursor) 활용 안내

### 8.1. 필수 규칙 파일

| 파일 | 용도 |
|------|------|
| [`.cursorrules`](../.cursorrules) | Agent 공통: 스택, 금지 사항, API 계약, 브랜치·공식 프롬프트 |
| [`tdd_rules.yaml`](../tdd_rules.yaml) | 단계별 TDD: 커버리지 90%, 필수 테스트·merge 조건 |

### 8.2. 행동 규칙

| 규칙 | 내용 |
|------|------|
| AI-01 | `작업규칙.TXT` 공식 프롬프트 우선 |
| AI-02 | `@파일`로 범위 제한 |
| AI-03 | 단계 종료 후 `prompting/` 저장 |
| AI-04 | Git 명령 → `GIT_prompt.md` |
| AI-05 | push 전 `.class` 제외 확인 |
| AI-06 | 코딩 전 `.cursorrules` + `tdd_rules.yaml`의 **현재 브랜치 트랙** 확인 |

**권장 읽기 순서**

1. `docs/00_prd.md` → `docs/01_analysis.md` → `docs/02_work_scenario.md`
2. [`.cursorrules`](../.cursorrules) → [`tdd_rules.yaml`](../tdd_rules.yaml)
3. 해당 브랜치(RED/GREEN 등)에서만 코드 수정

---

## 9. 알려진 이슈 빠른 참조 (`01_analysis.md`)

| 우선순위 | 이슈 | 단계 |
|----------|------|------|
| P0 | 중립 필터 불일치 | GREEN |
| P0 | 로그 UI 없음 | GREEN |
| P1 | download 헤더·fil_data | GREEN |
| P1 | C:\tmp 업로드 | GREEN |
| P2 | test CSV 없음 | New_Feature / 리소스 추가 |
| P2 | target Git 추적 | REFACTORING (.gitignore) |

---

## 10. README 정합 (Phase 6)

`README.md` 갱신 시 반영할 항목:

| 현재 README | 수정 방향 |
|-------------|-----------|
| `feedback_analyzer_java/` | `FeedBackAnalyzer_01` |
| `Constant` | `Constants` |
| `filters`, `session` | `Filters`, `Session` |
| `test_feedbacks.csv` | 리소스 추가 또는 문구 수정 |
| docs 링크 | `docs/00_prd.md` 등 |

---

## 11. 용어·참고

| 용어 | 설명 |
|------|------|
| A-01 | 통합 브랜치 |
| FR-xx | `00_prd.md` 기능 요구 ID |
| P0/P1 | `01_analysis.md` 우선순위 |
| File DB | 로컬 파일 기반 키워드 저장 (FR-18) |

| 문서 | 경로 |
|------|------|
| 원본 규칙 | `작업규칙.TXT` |
| 목적·스멜 | `project_purpose.md` |
| 사용자 | `README.md` |
| Cursor Agent | [`.cursorrules`](../.cursorrules) |
| TDD·커버리지 | [`tdd_rules.yaml`](../tdd_rules.yaml) |
| Mom Test | `docs/04_mom_test.md` |
| 코드 스멜 | `docs/05_code_smell.md` |
| To-Do | `docs/06_todo_list.md` |
| SPEC 보고서 | `report/00_SPEC_phase_report.md` |
| Agent 대화 | `prompting/00_SPEC_prompt.md`, `01_RED_prompt.md`, `02_GREEN_prompt.md` |
| 사용자 prompt | `prompting/User_prompt.md` |
| Git 기록 | `prompting/GIT_prompt.md` |

---

## 12. 문의·에스컬레이션

| 상황 | 참고 문서 |
|------|-----------|
| 요구사항 불명확 | `00_prd.md` |
| 어떤 파일 수정? | `01_analysis.md` §6.3 |
| 진행 순서? | `02_work_scenario.md` |
| Git·커밋·폴더? | 본 문서 §3~4 |
| Agent·TDD 규칙? | `.cursorrules`, `tdd_rules.yaml` |
| RED/GREEN 무엇부터? | `tdd_rules.yaml` → `tracks` |
| 남은 작업 목록? | `06_todo_list.md` |
| SPEC 단계 결과? | `report/00_SPEC_phase_report.md` |
