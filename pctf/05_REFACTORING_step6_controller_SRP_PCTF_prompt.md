# PCTF — REFACTORING 6단계: Controller SRP (Prompt 입력용)

| 항목 | 내용 |
|------|------|
| PCTF ID | `05_REFACTORING_step6_controller_SRP` |
| 시나리오 | `docs/02_work_scenario.md` §7.3 |
| 단계 | **REFACTORING — 6단계** (Controller·패키지, FR-15~16) |
| 브랜치 | `REFACTORING` |
| PRD | FR-15, FR-16 |
| **선행** | `pctf/03`, `pctf/04` 완료 |
| 본 PCTF 산출물 | 패키지 분리, `report/03_REFACTORING_report.md`, PR 준비 |
| 예상 시간 | 1h |
| 작성일 | 2026-05-22 |

---

## P — Purpose

- **Controller = HTTP only** (FR-15): CSV 파싱·집계·필터 오케스트레이션 → Service.
- **패키지 분리** (FR-16): `controller`, `service`, `model`, `config`.
- **외부 계약 불변**: URL·CSV·Thymeleaf view 이름 유지 (`tdd_rules.yaml` contract_immutable).
- **마무리**: `report/03_REFACTORING_report.md`, REFACTORING → A-01 PR.

---

## C — Context

### contract_immutable (필수)

| 항목 | 값 |
|------|-----|
| HTTP paths | `/`, `/analyze`, `/upload`, `/filter`, `/download` |
| CSV column | `text` |
| 테스트 | `FeedbackControllerWebTest` **PASS** 유지 |

### 목표 패키지 구조

```text
com.example.demo
├── controller/     FeedbackController (@Controller)
├── service/          FeedbackService, (CsvUploadService 등 최소)
├── model/            Feedback, Session (또는 domain)
├── config/           Constants → SentimentConfig / CategoryConfig
├── (기존)            TextAnalyzer, Filters, FileHandler, Logger, UIComponents
    또는 service/로 이동 — 과도 분리 금지, PRD FR-16 충족 선에서 최소
```

> `DemoApplication`은 `com.example.demo` 루트 유지 (JaCoCo exclude).

### 수정 대상

| 영역 | 작업 |
|------|------|
| `FeedbackController` | 요청/응답·Model 속성만; 비즈니스 → `FeedbackService` |
| 신규 `FeedbackService` | analyze, upload CSV, filter, download 데이터 준비 |
| 패키지 이동 | `Feedback` → `model`, 설정 → `config` |
| `src/test/**` | import 경로·MockMvc 테스트 갱신 |
| `DemoApplication` | `@SpringBootApplication` scanBasePackages 조정 |

### Git (단계 + 마무리)

```bash
# 6단계
git commit -m "REFACTOR step6: Controller SRP and package split (FR-15, FR-16)"
git push origin REFACTORING

# 마무리 리포트
git commit -m "REFACTOR: report/03_REFACTORING_report.md"
git push origin REFACTORING
```

---

## T — Task

### R6-00 — baseline

`mvn test` → Failures=0

---

### R6-01 — FeedbackService (FR-15)

1. `analyzeFeedback`, `uploadCsv`, `filterFeedbacks`, `getExportRows` 등 Service 메서드.
2. Controller: `@PostMapping` / `@GetMapping`만 — Service 호출 + `Model`/`Response` 설정.
3. `filteredFeedbacksForExport` 상태 — Service 또는 session-scoped bean (static `Session` 개선은 선택).

---

### R6-02 — 패키지 분리 (FR-16)

1. `com.example.demo.controller.FeedbackController`
2. `com.example.demo.service.FeedbackService` (+ 필요 시 `AnalysisService`)
3. `com.example.demo.model.Feedback`
4. `com.example.demo.config.*` (Constants 분리 시)
5. 컴파일·`mvn test` — **Web 테스트 8건** 포함 PASS.

---

### R6-03 — Git (6단계)

```bash
git add src/main/java/ src/test/java/
git commit -m "REFACTOR step6: Controller SRP and package split (FR-15, FR-16)"
git push origin REFACTORING
```

---

### R6-04 — REFACTORING 리포트 + PR 준비

| 파일 | 내용 |
|------|------|
| `report/03_REFACTORING_report.md` | step4~6 요약, FR-12~16 체크리스트, mvn test·jacoco, Before/After 구조 |

```bash
git add report/03_REFACTORING_report.md
git commit -m "REFACTOR: report/03_REFACTORING_report.md"
git push origin REFACTORING
```

PR: `gh pr create --base A-01 --head REFACTORING --title "REFACTORING: FR-12~16 structure (naming, SRP, packages)"`

---

## F — Format

### DoD (6단계)

- [ ] Controller에 CSV 파싱·비즈니스 집계 로직 없음
- [ ] 패키지 `controller`/`service`/`model`/`config` 존재
- [ ] URL 5종 불변, `mvn test` 0 failures
- [ ] JaCoCo ≥90%
- [ ] step6 commit + report commit + push

### 금지

- URL path 변경
- TEST_PLAN TC 삭제
- New_Feature (FR-17~18) 선행 구현

---

## ★ PROMPT — Agent에 붙여넣기

```
[PCTF 05 — REFACTORING 6단계: Controller SRP FR-15~16 + 마무리]

P (Purpose)
- FR-15: FeedbackController HTTP only, FeedbackService 분리
- FR-16: packages controller, service, model, config
- URL /, /analyze, /upload, /filter, /download 불변. mvn test 전부 PASS.
- report/03_REFACTORING_report.md 작성 후 push. (PR 생성은 사용자 요청 시)

C (Context)
- @docs/02_work_scenario.md §7.3 @docs/00_prd.md FR-15~16
- @FeedbackController.java @tdd_rules.yaml tracks.REFACTOR contract_immutable
- @FeedbackControllerWebTest.java
- 선행: pctf/03 step4, pctf/04 step5 완료

T (Task)
R6-00: mvn test → 0 failures

R6-01: FeedbackService — analyze, upload, filter, download 데이터
  Controller는 매핑·Model만

R6-02: 패키지 이동 + DemoApplication scan
  mvn test → Web 포함 PASS, jacoco ≥90%

R6-03:
  git commit -m "REFACTOR step6: Controller SRP and package split (FR-15, FR-16)"
  git push origin REFACTORING

R6-04: report/03_REFACTORING_report.md (step4~6, FR-12~16, jacoco)
  git commit -m "REFACTOR: report/03_REFACTORING_report.md"
  git push origin REFACTORING

F (Format)
- 완료 보고: 패키지 트리, URL 불변 확인, mvn test, report 경로.
```

---

## ★ PROMPT — 마스터 (4~6 연속 실행 시)

```
[PCTF REFACTORING — 4~6단계 연속 (시나리오 §7)]

순서: pctf/03 → pctf/04 → pctf/05 각 § PROMPT 실행.
각 단계마다 mvn test PASS 후 commit + git push origin REFACTORING.

커밋 메시지:
1) REFACTOR step4: domain naming and constants (FR-12, FR-13)
2) REFACTOR step5: SentimentClassifier SRP and TextAnalyzer extract (FR-14)
3) REFACTOR step6: Controller SRP and package split (FR-15, FR-16)
4) REFACTOR: report/03_REFACTORING_report.md

계약: HTTP 5 path, CSV text 컬럼 불변. TEST_PLAN TC 유지.
```

---

## 교차 참조

| 문서 | 경로 |
|------|------|
| step4 | `pctf/03_REFACTORING_step4_naming_PCTF_prompt.md` |
| step5 | `pctf/04_REFACTORING_step5_sentiment_SRP_PCTF_prompt.md` |
| 시나리오 | `docs/02_work_scenario.md` §7 |
| TDD | `tdd_rules.yaml` → `tracks.REFACTOR` |
