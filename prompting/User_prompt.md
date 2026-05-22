# User Prompt 기록 — Feedback Analyzer

> `작업규칙.TXT`: 사용자 입력 prompt만 표 형식 + 요약  
> **최종 갱신**: 2026-05-21

---

## 요약

| 항목 | 내용 |
|------|------|
| 총 프롬프트 수 | **17** |
| SPEC 관련 | 12 |
| Git 확인 | 4 |
| 주요 단계 | A-01/SPEC → 분석·문서 → Mom Test → 규칙·To-Do → prompting/report → 2차 push → prompt 갱신 |
| 코드 구현 요청 | **없음** |
| 원격 SPEC 최신 커밋 | `9b03001` — `#SPEC 단계 : PRD, TODO, 작업 시나이오 작성` |

---

## 프롬프트 목록

| # | 일시 | 단계 | 요약 | 원문 |
|---|------|------|------|------|
| 1 | 2026-05-21 | Git | A-01 생성 | GIT 의 원격과 로컬에 A-01 브랜치를 생성해줘 |
| 2 | 2026-05-21 | SPEC | 1차 분석 | @Codebase Spring Boot 프로젝트의 전체 구조와 문제점, 미션 안내를 분석해서 docs 폴더에 analysis.md 저장해줘. |
| 3 | 2026-05-21 | Git | 브랜치 확인 | 현재 브랜치를 확인해주세요 |
| 4 | 2026-05-21 | Git | 동기화 확인 | 원격과 로컬이 동일한가요? |
| 5 | 2026-05-21 | SPEC | SPEC 1차 push | a-01 브랜치에서 SPEC 브랜치를 만들어주고, 지금까지 작업한 파일을 업로드 해줘 "SPEC 단계 진행" 이라고 COMMENT 해줘. 업로드시 CLASS 파일은 제외하고 올려줘 |
| 6 | 2026-05-21 | Git | 브랜치 확인 | 현재 브랜치를 확인해주세요 |
| 7 | 2026-05-21 | Git | PR 확인 | PR 생성도 확인해주세요 |
| 8 | 2026-05-21 | SPEC | docs·시나리오·안내 | @작업규칙.TXT @README.md … 작업시나리오 문서와, 작업규칙 상세 문서(→ 작업안내) 만들어줘 |
| 9 | 2026-05-21 | SPEC | PRD | @project_purpose.md @README.md 를 참고하여, PRD 를 만들어줘 |
| 10 | 2026-05-21 | SPEC | docs 2차 재작성 | DOCS 폴더 파일을 다시 작성하고 싶어 … PRD → analysis → 작업시나리오·작업안내 |
| 11 | 2026-05-21 | Phase1 | Mom Test | docs 참고 … Mom Test 질문 10개 … code_smell 3건 bullet … 구현·코드 금지 |
| 12 | 2026-05-21 | Phase3 | Agent·TDD YAML | .cursorrules 과 tdd_rules 을 작성할 YAML 적어서 파일로 남겨줘 |
| 13 | 2026-05-21 | Phase3/6 | guide 링크 | 네 (`03_work_guide`에 `.cursorrules`/`tdd_rules` 링크) |
| 14 | 2026-05-21 | Phase6 | To-Do | docs 참고 toDo_list 작성 … Must/Should/Nice/Tech Debt/Done/회귀/마일스톤 … Markdown만 |
| 15 | 2026-05-21 | SPEC | prompting·report | 네. 지금까지 Agent 대화를 작업규칙 활용해 prompt 저장, 보고서 작성 |
| 16 | 2026-05-21 | SPEC | 2차 push | 네.. github spec 브랜치 업로드 "#SPEC 단계 : PRD, TODO, 작업 시나이오 작성" COMMENT |
| 17 | 2026-05-21 | SPEC | prompt 갱신 | 저장된 PROMPT 파일을 UPDATE해줘 |

---

## 단계별 그룹

### Git (#1, #3, #4, #6, #7)

- `A-01`, `SPEC` 생성·동기화·PR #1

### SPEC 문서 (#2, #8~#11, #14)

- analysis → PRD(00) → analysis(01) → scenario(02) → guide(03) → mom_test(04) → code_smell(05) → todo(06)

### 규칙·기록·배포 (#12~#17)

- `.cursorrules`, `tdd_rules.yaml`, prompting/, report/, push `9b03001`, prompt UPDATE

---

## Agent 응답 요약 (프롬프트별)

| # | 주요 산출 |
|---|-----------|
| 5 | commit `a306870`, `origin/SPEC` |
| 10~14 | docs 00~06 |
| 15 | prompting 3종 + `report/00_SPEC_phase_report.md` |
| 16 | commit `9b03001`, 14 files push |
| 17 | prompting 3파일 UPDATE (본 갱신) |
