# User Prompt 기록 — Feedback Analyzer

> `작업규칙.TXT`: 사용자 입력 prompt만 표 형식 + 요약  
> 갱신일: 2026-05-21

---

## 요약

| 항목 | 내용 |
|------|------|
| 총 프롬프트 수 | 13 |
| 주요 단계 | Git(A-01/SPEC) → 분석·문서(SPEC) → Mom Test → 규칙·To-Do → 기록·보고서 |
| 코드 구현 요청 | **없음** (문서·Git·YAML만) |

---

## 프롬프트 목록

| # | 일시(세션) | 단계 | 요약 | 원문 |
|---|------------|------|------|------|
| 1 | 2026-05-21 | Git | A-01 로컬·원격 생성 | GIT 의 원격과 로컬에 A-01 브랜치를 생성해줘 |
| 2 | 2026-05-21 | SPEC | 코드베이스 분석 문서 | @Codebase Spring Boot 프로젝트의 전체 구조와 문제점, 미션 안내를 분석해서 docs 폴더에 analysis.md 저장해줘. |
| 3 | 2026-05-21 | Git | 현재 브랜치 확인 | 현재 브랜치를 확인해주세요 |
| 4 | 2026-05-21 | Git | 로컬·원격 동일 여부 | 원격과 로컬이 동일한가요? |
| 5 | 2026-05-21 | SPEC | SPEC 브랜치·푸시 | a-01 브랜치에서 SPEC 브랜치를 만들어주고, 지금까지 작업한 파일을 업로드 해줘 "SPEC 단계 진행" 이라고 COMMENT 해줘. 업로드시 CLASS 파일은 제외하고 올려줘 |
| 6 | 2026-05-21 | Git | 브랜치 확인 | 현재 브랜치를 확인해주세요 |
| 7 | 2026-05-21 | Git | PR 확인 | PR 생성도 확인해주세요 |
| 8 | 2026-05-21 | SPEC | docs 전면 재작성 | @작업규칙.TXT @README.md … PRD, analysis, 작업시나리오, 작업규칙 상세(→ 작업안내) |
| 9 | 2026-05-21 | SPEC | PRD 작성 | @project_purpose.md @README.md 를 참고하여, PRD 를 만들어줘 |
| 10 | 2026-05-21 | SPEC | docs 재작성(2차) | DOCS 폴더 파일을 다시 작성 … PRD → analysis → 시나리오·안내 |
| 11 | 2026-05-21 | Phase1 | Mom Test + code_smell | … Mom Test 질문 10개 … code_smell 문서 … 구현·코드 금지 |
| 12 | 2026-05-21 | Phase3 | cursorrules + tdd | .cursorrules 과 tdd_rules 을 작성할 YAML 적어서 파일로 남겨줘 |
| 13 | 2026-05-21 | Phase3/6 | work_guide 링크 | 네 (06 링크 + prompting + 보고서) |

---

## 단계별 그룹

### Git · 브랜치 (#1, #3, #4, #5, #6, #7)

- `A-01`, `SPEC` 생성·푸시·동기화·PR #1 확인

### SPEC · 문서 (#2, #8, #9, #10)

- analysis → 00_prd → 01_analysis → 02_scenario → 03_guide

### Phase 1 · 3 · 6 (#11, #12, #13)

- Mom Test, code_smell, `.cursorrules`, `tdd_rules.yaml`, `06_todo_list`, prompting/report
