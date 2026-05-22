# To-Do 리스트 — FeedBack Analyzer (Java)

> **근거**: `docs/00_prd.md`, `docs/01_analysis.md`, `docs/02_work_scenario.md`, `docs/03_work_guide.md`, `docs/04_mom_test.md`, `docs/05_code_smell.md`, `.cursorrules`, `tdd_rules.yaml`  
> **작성일**: 2026-05-21 · **갱신**: 2026-05-22 (`docs/14` 검증 반영)  
> **규칙**: 코드 작성 없음 — 작업 목록·완료 기준만 정의

---

## 🔴 필수 (Must-Have) — v1.0 릴리스 차단 항목

| 상태 | 작업 설명 | 연관 PRD | 완료 기준 (누가 · 무엇을 · 통과 조건) |
|------|-----------|----------|--------------------------------------|
| [x] | **RED**: `TextAnalyzerTest` 작성 — 긍정/부정/중립·카테고리·빈 목록 | FR-19, FR-04, FR-05 | `e07ca6b`, `report/01` |
| [x] | **RED**: `FiltersTest` 작성 — **TC-NEUTRAL-01/02** (RED 의도 실패) | FR-19, FR-06, FR-09 | `07`, GREEN에서 PASS |
| [x] | **RED**: `FileHandlerTest` 작성 | FR-19 | Green |
| [x] | **RED**: JaCoCo **≥ 90%** | FR-20 | 90.9% → 90.4% 유지 |
| [x] | **RED**: `report/01_RED_coverage_report.md` | FR-20 | 완료 |
| [x] | **RED** 브랜치 → `A-01` 머지 | PRD §9 | PR #2, merge |
| [x] | **GREEN**: FR-09 감정 규칙 통합 (`Filters`↔`TextAnalyzer`) | FR-09 | `6e88371`, TC-NEUTRAL PASS |
| [x] | **GREEN/Feature**: `index.html` multiline (`textarea`) | FR-10 | `852fc4c` (New_Feature UI) |
| [ ] | **GREEN**: 로그 level UI | FR-11 | 미구현 — 후속 |
| [x] | **GREEN**: `mvn test` Green + 90% | FR-21, FR-20 | 41 tests @ A-01 |
| [x] | **GREEN**: `report/02` + → `A-01` | FR-09 | PR #4 MERGED |
| [x] | **REFACTORING**: FR-12 네이밍 | FR-12 | step4 `2d81f59` |
| [x] | **REFACTORING**: FR-13 상수·enum | FR-13 | `Sentiment`, dedupe |
| [x] | **REFACTORING**: FR-14 `SentimentClassifier` | FR-14 | step5 |
| [x] | **REFACTORING**: FR-15~16 Service·패키지 | FR-15, FR-16 | step6 `0c23667` |
| [x] | **REFACTORING**: `report/03` + → `A-01` | FR-12~16 | A-01 반영 |
| [x] | **New_Feature**: Trend + File DB | FR-17~18 | `bc1724f`, `report/04` |
| [x] | **QA REVIEW**: Before/After + `docs/12`, `report/05` | 작업규칙 §8 | `afcbc53` |
| [x] | **회고**: `report/06_retrospective.md` | §6.1-8 | 문서 완료 |
| [ ] | **Baseline E2E**: §3.4 formal 기록 | FR-01~08 | `11`·수동 smoke |
| [ ] | **릴리스 PR**: `A-01` → `main` | PRD §9 | `pr3_body_*`, `gh auth` |
| [ ] | **팀 발표** | 회고 | 일정 별도 |

---

## 🟡 권장 (Should-Have) — 품질 향상 항목

| 상태 | 작업 설명 | 연관 PRD | 완료 기준 |
|------|-----------|----------|-----------|
| [ ] | CSV 다운로드 `Content-Disposition` 표준 형식 수정 | FR-08 | **개발자**가 `attachment; filename="filtered_feedback.csv"` 로 응답; 브라우저 다운로드 TC-DOWNLOAD-01 통과 |
| [ ] | 다운로드 데이터 소스 — 필터 없이도 분석 결과 또는 전체 목록보내기 정책 확정·구현 | FR-08 | **PM/개발자**가 정책 1줄 문서화; **개발자** E2E에서 빈 CSV 재현 불가 |
| [ ] | CSV 업로드 `C:\tmp` 제거 → `MultipartFile` 스트림/공통 temp | FR-03, NFR-03 | **개발자** Linux/Windows 동일 TC-UPLOAD-01 통과 |
| [ ] | 업로드 직후 `sentimentResults`/`keywordResults` Model 전달 | FR-03, FR-07 | **개발자** 업로드만으로 통계 영역 표시; E2E 스크린 |
| [ ] | `index.html`에 `feedbacks`/`filteredFeedbacks` 목록 렌더링 | FR-07 | **사용자(본인)** 필터 후 목록에서 문장 확인 가능 |
| [ ] | `.gitignore`에 `target/`, `*.class` 추가 및 추적 해제 | NFR-04, Tech Debt | **개발자** `git status`에 target 변경 없음 |
| [ ] | `src/main/resources/test_feedbacks.csv` 샘플 추가 | FR-03, README | **개발자** README 업로드 예시로 E2E 재현 |
| [ ] | `Feedback`에 `sentiment`/`category` 분석 후 저장 | FR-04~05 | **개발자** 단위 테스트에서 Feedback 필드 non-null 검증 |
| [ ] | `FileHandler` Lava Flow 제거 — 연동 또는 삭제 | FR-03 | **리뷰어** 미사용 주입/데드 코드 없음 확인 |
| [x] | `prompting/` 00~05 + `User_prompt`·`GIT_prompt` | §3.3 | SPEC~QA 완료 (#58) |
| [x] | SPEC·docs 00~14 on `A-01` | PRD §9 | `eee9f2b` HEAD |

---

## 🟢 선택 (Nice-to-Have) — v2.0 후보

| 상태 | 작업 설명 | 기대 가치 |
|------|-----------|-----------|
| [ ] | 감정 분석 **전략 패턴**으로 알고리즘 교체 가능 | OCP 강화, A/B 규칙 실험 |
| [ ] | Thymeleaf `#dates` → `#temporals` 마이그레이션 | Spring Boot 3 호환·경고 제거 |
| [ ] | 피드백 목록 페이지네이션·검색 | 대량 CSV UX |
| [ ] | HTTP 세션 또는 Spring Session으로 `Session` static 대체 | 다중 사용자·재시작 안정성 |
| [ ] | Epic/Gherkin 추적 문서 (`Phase 4`) | 요구-TC-코드 추적 자동화 |
| [ ] | BCE·계약 문서 (`Phase 2`) | 경계·계약 명시적 관리 |
| [ ] | CI(GitHub Actions)에서 `mvn test` + JaCoCo gate | PR마다 회귀 자동 차단 |

---

## 🔵 기술 부채 (Tech Debt)

| 상태 | 문제 설명 | 발생 원인 | 해결 방향 |
|------|-----------|-----------|-----------|
| [x] | 감정 규칙 이중화 | 사례1 | `SentimentClassifier` (FR-09/14) |
| [x] | God Controller | 사례2 | `FeedbackService` (FR-15/16) |
| [x] | 키워드 Shotgun Surgery | 사례3 | File DB (FR-18) |
| [ ] | `Session` static 전역 상태 | HTTP 세션 미사용 | v2 백로그 |
| [ ] | `Logger` static + `@Service` 혼재 | FR-11 미완 | v2 |
| [x] | 테스트 `contextLoads` only | RED 완료 | 41 tests |
| [ ] | README vs 실제 구조·파일명 불일치 | 초기 문서 drift | Phase 6 README 갱신 (`03_work_guide.md` §10) |
| [ ] | docs 구버전 파일 잔존 (`analysis.md`, `01_work_scenario.md` 등) | 폴더 재구성 | **개발자** 중복 파일 정리 PR (문서만) |

---

## ✅ 완료 항목 (Done)

| 상태 | 완료 내용 | 완료일 | 관련 커밋/PR |
|------|-----------|--------|--------------|
| [x] | Git `A-01`·`SPEC` 브랜치 생성 및 `origin` 푸시 | 2026-05-21 | `git push -u origin SPEC` |
| [x] | SPEC 초기 분석 `docs/analysis.md` 커밋 | 2026-05-21 | 커밋 `SPEC 단계 진행` (`a306870`) |
| [x] | PR #1 Open — `SPEC` → `A-01` | 2026-05-21 | [PR #1](https://github.com/antihu99/FeedBackAnalyzer_01/pull/1) |
| [x] | `docs/00_prd.md` 작성 (purpose + README 기반) | 2026-05-21 | 로컬 docs (미푸시 가능) |
| [x] | `docs/01_analysis.md` (PRD + src 분석) | 2026-05-21 | 로컬 docs |
| [x] | `docs/02_work_scenario.md` | 2026-05-21 | 로컬 docs |
| [x] | `docs/03_work_guide.md` (+ `.cursorrules`/`tdd_rules` 링크) | 2026-05-21 | 로컬 docs |
| [x] | `docs/04_mom_test.md` (Mom Test 10문항) | 2026-05-21 | Phase 1, 코드 금지 |
| [x] | `docs/05_code_smell.md` (사례 3건) | 2026-05-21 | Phase 1 |
| [x] | `.cursorrules` (YAML) Agent 규칙 | 2026-05-21 | Phase 3 |
| [x] | `tdd_rules.yaml` (RED/GREEN/REFACTOR/FEATURE) | 2026-05-21 | Phase 3 |
| [x] | `docs/06_todo_list.md` | 2026-05-21 | Phase 6 |
| [x] | `prompting/00_SPEC_prompt.md`, `User_prompt.md`, `GIT_prompt.md` | 2026-05-21 | `작업규칙.TXT` §prompting |
| [x] | `report/00_SPEC_phase_report.md` | 2026-05-21 | SPEC 결과 보고서 |
| [x] | RED~New_Feature·QA report `01`~`06` | 2026-05-22 | `A-01` 통합 |
| [x] | `docs/12`~`14` QA·리뷰·흐름 검증 | 2026-05-22 | `eee9f2b` |

---

## 📋 회귀 방지 체크리스트 (PRD §6.4 API 계약 · §10 수용 · `tdd_rules.yaml` 기반)

**배포(또는 `A-01`→`main` 머지) 전 — 담당: 개발자 실행 · 리뷰어 승인**

| # | 확인 항목 | 통과 기준 |
|---|-----------|-----------|
| 1 | **API 계약** | `GET /`, `POST /analyze`, `POST /upload`, `POST /filter`, `GET /download` 경로·메서드 변경 없음 (문서화된 예외만) |
| 2 | **CSV 계약** | 업로드 CSV `text` 컬럼 필수; 헤더 1행 스킵; 다운로드 UTF-8 BOM + `text` 헤더 |
| 3 | **계약·회귀 테스트** | `mvn test` **0 failures**; FR-09 중립·FR-08 다운로드·FR-03 업로드 관련 TC Green |
| 4 | **커버리지** | JaCoCo line coverage **≥ 90%** (`tdd_rules.yaml` `coverage_minimum_percent`) |
| 5 | **Dual-Track** | RED에서 추가한 TC가 GREEN/REFACTOR 후에도 Green (테스트 삭제·@Disabled 없음) |
| 6 | **산출물** | 해당 단계 `report/{NN}_*.md` 존재 |
| 7 | **README** | 설치·실행·CSV·docs 링크(`00_prd`~`14`) 최신 |
| 8 | **Git 위생** | 커밋에 `*.class`, `target/` 미포함 |
| 9 | **문서 정합** | `01_analysis.md` P0 항목 해소 또는 Known Issue 명시 |
| 10 | **브랜치** | 기능 브랜치 → `A-01` 머지 완료 후 릴리스 PR |

---

## 🗓️ 마일스톤

| 마일스톤 | 포함 항목 (PRD) | 목표일 | 상태 | 완료 담당 |
|----------|-----------------|--------|------|-----------|
| M0 — 환경·문서 기반 | SPEC, Mom Test, docs 00~06 | 2026-05-21 | ✅ 완료 | `A-01` |
| M1 — RED | FR-19~21, JaCoCo ≥90% | 2026-05-22 | ✅ 완료 | `e07ca6b` |
| M2 — GREEN | FR-09 (+ FR-10 NF, FR-11 ⬜) | 2026-05-22 | 🟡 부분 | `6e88371` |
| M3 — REFACTORING | FR-12~16 | 2026-05-22 | ✅ 완료 | step4~6 |
| M4 — New Feature | FR-17~18 | 2026-05-22 | ✅ 완료 | `bc1724f` |
| M5 — QA·문서 | `12`~`14`, `report/05` | 2026-05-22 | ✅ 완료 | `afcbc53` |
| M6 — v1.0 Release | `A-01`→`main` | — | 🟡 진행 중 | PR #3 본문 준비 |
| M7 — 회고·발표 | `report/06_retrospective.md` | 2026-05-22 | 🟡 문서 완료 | 팀 발표 대기 |

---

## 📎 문서 인덱스

| 파일 | 용도 |
|------|------|
| `docs/00_prd.md` | 요구사항 ID |
| `docs/01_analysis.md` | P0/P1 갭 |
| `docs/02_work_scenario.md` | E2E·브랜치 순서 |
| `docs/03_work_guide.md` | Git·산출물 |
| `docs/06_todo_list.md` | 본 To-Do |
| `docs/12`~`14` | QA·리뷰·흐름 검증 |
| `docs/14_work_flow_verification.md` | 시나리오 준수 검증 |
| `tdd_rules.yaml` | 트랙별 merge 조건 |
