# To-Do 리스트 — FeedBack Analyzer (Java)

> **근거**: `docs/00_prd.md`, `docs/01_analysis.md`, `docs/02_work_scenario.md`, `docs/03_work_guide.md`, `docs/04_mom_test.md`, `docs/05_code_smell.md`, `.cursorrules`, `tdd_rules.yaml`  
> **작성일**: 2026-05-21  
> **규칙**: 코드 작성 없음 — 작업 목록·완료 기준만 정의

---

## 🔴 필수 (Must-Have) — v1.0 릴리스 차단 항목

| 상태 | 작업 설명 | 연관 PRD | 완료 기준 (누가 · 무엇을 · 통과 조건) |
|------|-----------|----------|--------------------------------------|
| [ ] | **RED**: `TextAnalyzerTest` 작성 — 긍정/부정/중립·카테고리·빈 목록 | FR-19, FR-04, FR-05 | **개발자**가 TC 실행 시 의도된 실패/통과가 문서화됨; `mvn test` 리포트에 클래스 존재 |
| [ ] | **RED**: `FiltersTest` 작성 — 감정·카테고리·복합 필터, **중립 TC-NEUTRAL-01** | FR-19, FR-06, FR-09 | **개발자**가 `중립` 필터 TC 실행; baseline에서 실패 시 `report/00_RED`에 기록 |
| [ ] | **RED**: `FileHandlerTest` 작성 | FR-19 | **개발자**가 `save`/`saveResult` TC Green 또는 실패 사유 문서화 |
| [ ] | **RED**: JaCoCo 설정 + line coverage **≥ 90%** | FR-20, NFR-04 | **개발자**가 `mvn test jacoco:report` 후 `target/site/jacoco`에서 `com.example.demo` ≥ 90% 스크린/수치 제출 |
| [ ] | **RED**: `report/01_RED_coverage_report.md` 작성 | FR-20 | **개발자**가 미통과 TC 목록·커버리지 % 기재; **리뷰어** 확인 |
| [ ] | **RED** 브랜치 PR → `A-01` 머지 | PRD §9 | **개발자** PR 생성; **리뷰어** Approve |
| [ ] | **GREEN**: 감정 판별 **단일 규칙**으로 `TextAnalyzer`·`Filters` 통합 | FR-09, FR-14 | **개발자**가 TC-NEUTRAL-01·TC-NEUTRAL-02 **통과**; 동일 문장 분석·필터 감정 일치 |
| [ ] | **GREEN**: `index.html` multiline 입력·표시·서버 수신 일관 | FR-10 | **개발자**가 줄바꿈 포함 텍스트 E2E 입력; **QA/본인** 화면·재조회 시 문자열 유지 확인 |
| [ ] | **GREEN**: 로그 level(warning/error) UI on/off + 페이지 표시 | FR-11 | **개발자**가 토글 시 해당 level만 목록 표시; error 기본 표시 정책 README 또는 PR 본문 명시 |
| [ ] | **GREEN**: `mvn test` **전체 통과** + 커버리지 90% 유지 | FR-21, FR-20 | **개발자** CI/로컬 전체 Green; JaCoCo 재측정 |
| [ ] | **GREEN**: `report/02_GREEN_bugfix_report.md` + PR → `A-01` | FR-09~11 | **리뷰어** 버그 3건 AC 서명 |
| [ ] | **REFACTORING**: `fil`/`sent`/`kw`/`fil_data` 도메인 네이밍 개선 | FR-12 | **개발자**가 public 메서드 rename 완료; **리뷰어**가 diff에서 구 API URL 불변 확인 |
| [ ] | **REFACTORING**: `Constants` → enum/설정 클래스, 키워드 중복 제거 | FR-13 | **개발자**가 카테고리 1개 추가 시 수정 파일 ≤ 2개(목표) 또는 PRD 예외 문서화 |
| [ ] | **REFACTORING**: `FeedbackController` 비즈니스 로직 → Service, 패키지 분리 | FR-15, FR-16 | **개발자**가 `controller`/`service`/`model`/`config` 구조; Controller에 CSV 파싱·집계 로직 없음; **테스트 Green** |
| [ ] | **REFACTORING**: PR → `A-01` + `report/03_REFACTORING_report.md` | FR-12~16 | **리뷰어** SRP·OCP 체크리스트 통과 |
| [ ] | **New_Feature**: `test_feedback_trend.csv` + Trend 시각화 | FR-17 | **개발자**가 리소스 추가·UI 차트 표시; 빈/미존재 시 안내 메시지 TC 통과 |
| [ ] | **New_Feature**: File DB 감정·키워드 CRUD·재기동 유지 | FR-18 | **개발자**가 키워드 변경 후 재시작해도 설정 유지 TC 통과 |
| [ ] | **New_Feature**: PR → `A-01` + `report/04_New_Feature_report.md` | FR-17~18 | **리뷰어** 기능 AC 확인 |
| [ ] | **Baseline E2E**: FR-01~08 시나리오 A~C 통과 | FR-01~08, PRD §10 | **개발자**가 `02_work_scenario.md` §3.4 체크리스트 전항목 통과 기록 |
| [ ] | **릴리스 PR**: `A-01` → `main` | PRD §9, §10 | **개발자** PR; **강사/리뷰어** 최종 Approve; 기능 브랜치 미삭제 확인 |
| [ ] | **회고**: `report/05_retrospective.md` + 팀 발표 | `project_purpose.md` §6.1-8 | **팀** 4개 질문 답변 완료; 발표 일정 충족 |

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
| [x] | `prompting/` SPEC 대화·`User_prompt.md`·`GIT_prompt.md` | `03_work_guide.md` §3.3 | **Agent** SPEC 00~02 완료; RED~New_Feature 각 1파일 **추가 필요** |
| [ ] | SPEC PR #1 (`SPEC`→`A-01`) 머지 및 `A-01`에 최신 docs 동기화 | PRD §9 | **리뷰어** Merge; **개발자** `A-01` pull 후 docs 00~06 존재 확인 |

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
| [ ] | 감정 규칙 이중화 — 분석·필터 결과 불일치 | `TextAnalyzer` vs `Filters.S_KEYWORDS` vs `Constants` | `05_code_smell.md` 사례1 → SentimentClassifier 단일화 (FR-09/14) |
| [ ] | God Controller — CSV·상태·다운로드 혼재 | 초기 레거시 단일 책임 | `05_code_smell.md` 사례2 → Service 분리 (FR-15/16) |
| [ ] | 키워드 Shotgun Surgery·가짜 도메인 | `Constants`+`UIComponents`+`Filters` 분산 | `05_code_smell.md` 사례3 → File DB/설정 (FR-18) |
| [ ] | `Session` static 전역 상태 | HTTP 세션 미사용 설계 | REFACTORING 시 요청/세션 스코프 상태 |
| [ ] | `Logger` static + `@Service` 혼재 | print 기반 로깅 | FR-11 UI 연동 + SLF4J 검토 (v2) |
| [ ] | 테스트 `contextLoads` only | RED 미착수 | FR-19~21, `tdd_rules.yaml` |
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
| 7 | **README** | 설치·실행·CSV·docs 링크(`00_prd`~`06_todo`) 최신 |
| 8 | **Git 위생** | 커밋에 `*.class`, `target/` 미포함 |
| 9 | **문서 정합** | `01_analysis.md` P0 항목 해소 또는 Known Issue 명시 |
| 10 | **브랜치** | 기능 브랜치 → `A-01` 머지 완료 후 릴리스 PR |

---

## 🗓️ 마일스톤

| 마일스톤 | 포함 항목 (PRD) | 목표일 | 상태 | 완료 담당 |
|----------|-----------------|--------|------|-----------|
| M0 — 환경·문서 기반 | SPEC 문서, Mom Test, code_smell, `.cursorrules`, `tdd_rules.yaml`, `06_todo_list` | 2026-05-21 | 🟡 진행 중 | 개발자 (docs 푸시·PR 머지 남음) |
| M1 — RED | FR-19, FR-20, FR-21 | +2일 | ⬜ 대기 | 개발자 + 리뷰어 |
| M2 — GREEN | FR-09, FR-10, FR-11 | +1.5일 | ⬜ 대기 | 개발자 |
| M3 — REFACTORING | FR-12, FR-13, FR-14, FR-15, FR-16 | +3.5일 | ⬜ 대기 | 개발자 + 리뷰어 |
| M4 — New Feature | FR-17, FR-18 | +3일 | ⬜ 대기 | 개발자 |
| M5 — v1.0 Release | FR-01~08 E2E, PRD §10 전항, `A-01`→`main` | +1일 | ⬜ 대기 | 리뷰어/강사 |
| M6 — 회고·발표 | `report/05_retrospective.md`, 팀 리뷰 | +2일 | ⬜ 대기 | 팀 전원 |

---

## 📎 문서 인덱스

| 파일 | 용도 |
|------|------|
| `docs/00_prd.md` | 요구사항 ID |
| `docs/01_analysis.md` | P0/P1 갭 |
| `docs/02_work_scenario.md` | E2E·브랜치 순서 |
| `docs/03_work_guide.md` | Git·산출물 |
| `docs/06_todo_list.md` | 본 To-Do |
| `tdd_rules.yaml` | 트랙별 merge 조건 |
