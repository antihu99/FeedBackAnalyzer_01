# 회고 보고서 — Feedback Analyzer 프로젝트

| 항목 | 내용 |
|------|-----|
| 문서 ID | RPT-RETRO-001 |
| 단계 | 9 — 회고·발표 (`작업규칙.TXT` §5, `docs/02_work_scenario.md` §9) |
| 브랜치 | `QA` (산출 후 `A-01` 통합 권장) |
| 작성일 | 2026-05-22 |
| 근거 | `project_purpose.md` §1.3, `docs/00_prd.md`, 단계별 `report/00`~`05` |

---

## 0. 프로젝트 한눈에 보기

| 단계 | 브랜치 | 핵심 성과 | 보고서 |
|------|--------|-----------|--------|
| 1 SPEC | `SPEC` | PRD·Mom Test·TDD 규칙 | `report/00_SPEC_phase_report.md` |
| 2 RED | `RED` | JUnit 34+ TC, JaCoCo ≥90% | `report/01_RED_coverage_report.md` |
| 3 GREEN | `GREEN` | 중립 버그·multiline·Logger UI | `report/02_GREEN_bugfix_report.md` |
| 4~6 REFACTORING | `REFACTORING` | FR-12~16 네이밍·SRP·패키지 | `report/03_REFACTORING_report.md` |
| 7 New_Feature | `new_feature` | Trend·File DB (FR-17~18) | `report/04_New_Feature_report.md` |
| 8 REVIEW | `QA` | 전후 비교 (`6e88371` vs After) | `report/05_REVIEW_refactoring_report.md` |
| 9 회고 | `QA` | 본 문서 | `report/06_retrospective.md` |

**최종 기술 지표 (QA HEAD, `mvn clean test`)**

| 항목 | Before (RED 시점) | After (QA) |
|------|-------------------|------------|
| 테스트 수 | 34 → 실패 2 (RED) | **41**, 0 failures |
| JaCoCo line | 90.9% | **90.4%** |
| HTTP 계약 | 5 path | 5 path + `/keywords/*` |
| 패키지 | 단일 루트 | controller / service / model / config |

---

## 1. 실습 목표 vs 달성도

### 1.1 학습·제품 목표 (`project_purpose.md` §1.3, PRD §3)

| 목표 | 달성 | 근거 |
|------|------|------|
| 코드 스멜·안티패턴 식별 | ✅ | `docs/05_code_smell.md` 3건, `01_analysis.md` |
| MVC/레이어드 구조 개선 | ✅ | `FeedbackService` + `controller` 패키지 (FR-15~16) |
| 비즈니스·UI 분리 | ✅ | Controller HTTP only, Thymeleaf는 View만 |
| 리팩토링 기법 실습 | ✅ | rename, extract, SRP, enum (`Sentiment`) |
| 테스트 가능 아키텍처 | ✅ | 41 tests, MockMvc, 단위 테스트 분리 |
| 커버리지 90% | ✅ | JaCoCo line 90.4% 유지 |

### 1.2 단계별 FR 달성 (요약)

| FR 구간 | 내용 | 상태 |
|---------|------|------|
| FR-09~11 | GREEN 버그·UI | ✅ `02_GREEN` |
| FR-12~16 | REFACTORING | ✅ `03_REFACTORING`, `05_REVIEW` |
| FR-17~18 | Trend·File DB | ✅ `04_New_Feature` |
| FR-19~21 | 테스트·커버리지·문서 | ✅ RED~QA 문서 체계 |

### 1.3 미완·부분 달성

| 항목 | 상태 | 비고 |
|------|------|------|
| `A-01` → `main` 릴리스 PR | 🟡 | PR #3 Open — `gh auth` 후 제목·본문 갱신 필요 |
| `Session` static → HTTP session | ⬜ | 선택 개선, 회귀 범위 밖 |
| `C:\tmp` 업로드 경로 | 🟡 | 동작하나 NFR-03(이식성) 잔여 |
| 팀 발표 | ⬜ | 본 문서는 발표 원고·체크리스트 역할 |

**종합 달성도**: 실습·리팩토링·신규 기능 목표 **약 90%** — 릴리스 PR·발표 일정만 잔여.

---

## 2. AI 활용 — 도움이 된 순간과 한계

### 2.1 도움이 된 순간

| 영역 | 활용 방식 | 효과 |
|------|-----------|------|
| SPEC·PRD | `@Codebase` 분석 → `00_prd.md`, 시나리오 | 요구 추적 매트릭스 일관성 |
| RED | PCTF 프롬프트 + TC 목록 → JUnit·JaCoCo 설정 | 90% 커버리지 목표 빠른 도달 |
| GREEN | 실패 TC·스택트레이스 기반 최소 수정 | FR-09 중립 버그 집중 수정 |
| REFACTORING | Before/After diff·패키지 이동 순서 제안 | 계약 불변 유지하며 step4~6 분리 |
| New_Feature | CSV 스키마·File DB·Chart.js 연동 초안 | FR-17~18 일괄 구현·테스트 41건 |
| QA REVIEW | `git show 6e88371` 발췌 + 보고서 템플릿 | FR-12~16 검증 문서 자동화 |
| Git | `prompting/GIT_prompt.md` 명령 이력 | 단계별 commit 메시지 재현 |

### 2.2 한계·주의점

| 한계 | 사례 | 대응 |
|------|------|------|
| `gh` 미인증 | `gh pr create` / `gh pr edit` 실패 | 로컬 `gh auth login` 또는 GitHub UI |
| stale `target/` | 구 `FeedbackController.class` → 빈 충돌 | **`mvn clean test` 필수** |
| 인코딩·터미널 | `git show` 한글 깨짐 | IDE/소스 파일 기준 발췌 |
| 범위 확장 | New_Feature까지 한 브랜치에 누적 | PCTF 단계별 commit으로 롤백 가능하게 유지 |
| 검증 책임 | AI가 “통과”라고 해도 | **항상 로컬 `mvn clean test`로 확인** |

### 2.3 활용 팁 (후속 실습용)

1. **PCTF + 작업규칙**을 먼저 고정 → Agent 출력이 산출물 경로·커밋 메시지와 일치한다.  
2. **Before SHA**(`6e88371`)를 QA에 명시 → 리뷰 보고서 재현성 확보.  
3. **프로덕션 코드 수정 금지** 단계(QA REVIEW)는 문서만 커밋해 회귀 리스크를 줄인다.

---

## 3. TC가 개선에 미친 영향 · TC 작성 팁

### 3.1 영향

| 시점 | TC 역할 | 결과 |
|------|---------|------|
| RED | 실패가 스펙 | TC-NEUTRAL-01/02 → FR-09 구현 압력 |
| GREEN | 회귀 방어막 | 중립·multiline·Logger 수정 후 34 tests Green |
| REFACTORING | rename 후 깨짐 조기 발견 | `TextAnalyzerTest`, `FiltersTest` 메서드명 갱신 |
| New_Feature | AC 고정 | Trend·Keyword File DB 전용 테스트 추가 → 41 tests |
| QA | E2E 5 path + keywords | `FeedbackControllerWebTest` 10건 |

**핵심**: “리팩토링은 테스트 없이 위험하다”는 가설이 **TC-NEUTRAL**과 **MockMvc**로 실증됐다. `SentimentClassifier` 도입 후에도 분석·필터 감정이 한 규칙으로 묶였다.

### 3.2 TC 작성 팁

1. **Mom Test 질문 → TC ID** (`docs/04_mom_test.md`, `07_RED_test_plan.md`)  
2. **실패 메시지에 FR 번호** 포함 (추적성)  
3. **단위 + 웹** 분리: 감정 로직은 `SentimentClassifier`/`Filters` 단위, 계약은 `FeedbackControllerWebTest`  
4. **경계값**: `FILTER_ALL`("전체"), 빈 CSV, 중립-only 문장  
5. **커버리지 90%**는 “라인 수”보다 **핵심 분기(감정·필터·다운로드)** 우선  
6. CI/로컬 동일하게 **`mvn clean test`** (stale class 방지)

---

## 4. 클린코드·리팩토링 — 장점과 어려운 점

### 4.1 장점 (체감)

| 주제 | Before | After | 체감 |
|------|--------|-------|------|
| 가독성 | `fil`, `sent` | `filterFeedbacks`, `analyzeSentiment` | 코드 리뷰·온보딩 용이 |
| SRP | God Controller | Controller + Service | 변경 범위 예측 가능 |
| DRY | `S_KEYWORDS` vs `Constants` | `SentimentClassifier` | Mom Test Q5~7 신뢰도 상승 |
| OCP | 하드코딩만 | File DB + `KeywordConfigService` | 키워드 운영 비용 감소 (FR-18) |
| 패키지 | 12파일 한 폴더 | 4 레이어 | IDE 탐색·의존 방향 명확 |

### 4.2 어려운 점

| 어려움 | 원인 | 교훈 |
|--------|------|------|
| 단계 경계 | REFACTORING 중 기능 요구 혼입 | 브랜치·PCTF 단계 엄수 |
| 레거시 `Session` static | HTTP 세션 미사용 설계 | 다음 스프린트에서 분리 |
| 테스트 호환 생성자 | `SentimentClassifier()`, `Filters(…)` | 리팩터링 시 **테스트 깨짐 예산** 확보 |
| 문서·번호 충돌 | `report/05` = REVIEW vs 회고 | **06_retrospective**로 분리 (본 문서) |
| 통합 브랜치 동기화 | 여러 PR·fast-forward | `A-01` HEAD 기준 QA Before/After 재확인 |

### 4.3 한 줄 소감

> **테스트가 있는 리팩토링**은 두려움을 줄이고, **의미 있는 이름·단일 감정 규칙**은 Mom Test에서 말한 “숫자가 맞는다”는 신뢰를 코드로 옮긴다.

---

## 5. 다음 액션 (릴리스·발표)

| # | 작업 | 담당 | 명령/링크 |
|---|------|------|-----------|
| 1 | `QA` → `A-01` 머지 또는 PR | 개발자 | `git merge QA` / `gh pr create --base A-01 --head QA` |
| 2 | `A-01` → `main` 릴리스 PR | 개발자 | `docs/pr3_body_update.md` 참고 |
| 3 | `gh auth login` | 로컬 | PR 생성·편집 |
| 4 | 팀 발표 | 팀 | 본 문서 §1~4 요약 슬라이드 |
| 5 | (선택) `Session`·`C:\tmp` 개선 | 백로그 | `docs/06_todo_list.md` Should-Have |

---

## 부록 — 참고 문서

- `report/05_REVIEW_refactoring_report.md` — FR-12~16 Before/After  
- `docs/12_QA_review_outline.md` — QA 비교 축  
- `prompting/User_prompt.md` — 사용자 프롬프트 이력 (#1~#56+)  
- `prompting/GIT_prompt.md` — Git 명령 이력  
