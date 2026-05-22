# Agent 대화 기록 — SPEC 단계

| 항목 | 내용 |
|------|------|
| 단계 | SPEC (1단계 — 프로젝트 개요·준비) |
| 브랜치 | `SPEC` |
| 기간 | 2026-05-21 |
| 규칙 | `작업규칙.TXT` § prompting |
| 코드 변경 | **없음** (문서·Git·규칙 파일만) |

---

## 1. 대화 요약

본 세션에서 Agent는 **Feedback Analyzer** SPEC 단계 산출물을 작성했다.  
Git 브랜치(`A-01`, `SPEC`) 생성·푸시, PR #1 확인, `docs/` 재구성(00~06), Mom Test, `.cursorrules`/`tdd_rules.yaml`, To-Do, prompting/report 기록까지 수행했다.

---

## 2. 대화 타임라인

### 2.1. Git — A-01 · SPEC

**User**  
> GIT 의 원격과 로컬에 A-01 브랜치를 생성해줘

**Agent**  
- `main`에서 `A-01` 생성, `origin/A-01` 푸시, upstream 설정

**User**  
> 원격과 로컬이 동일한가요?

**Agent**  
- 커밋 `4e1003e` 동일; 워킹 디렉터리에 `target/`·미커밋 변경만 존재

---

### 2.2. 1차 분석 문서

**User**  
> @Codebase … docs 폴더에 analysis.md 저장해줘

**Agent**  
- `project_purpose.md`, `README.md`, `src/` 분석
- `docs/analysis.md` 작성 (구조·문제·미션)

---

### 2.3. SPEC 푸시

**User**  
> a-01 브랜치에서 SPEC 브랜치 … "SPEC 단계 진행" … CLASS 제외

**Agent**  
- `git checkout -b SPEC`
- `git add docs/` (`analysis.md`만)
- `git commit -m "SPEC 단계 진행"` → `a306870`
- `git push -u origin SPEC`

---

### 2.4. 브랜치·PR 확인

**User**  
> 현재 브랜치 / PR 생성 확인

**Agent**  
- 현재: `SPEC`, `origin/SPEC` 동기
- PR #1 Open: `SPEC` → `A-01`, 제목 `#1 SPEC 단계 PR`

---

### 2.5. docs 폴더 전면 재작성

**User**  
> @작업규칙.TXT @README.md @docs/analysis.md … PRD, analysis, 작업시나리오, 작업안내

**Agent**  
- `docs/00_prd.md`
- `docs/01_analysis.md` (PRD + src)
- `docs/02_work_scenario.md`
- `docs/03_work_guide.md`
- 구버전 `analysis.md`, `01_work_scenario.md` 등 삭제

---

### 2.6. Mom Test · 코드 스멜

**User**  
> Mom Test 10개 + code_smell 3건, 코드 금지

**Agent**  
- `docs/04_mom_test.md`
- `docs/05_code_smell.md`

---

### 2.7. Cursor · TDD 규칙

**User**  
> .cursorrules 와 tdd_rules YAML

**Agent**  
- `.cursorrules`
- `tdd_rules.yaml`
- `03_work_guide.md` 링크 추가 (User 승인)

---

### 2.8. To-Do

**User**  
> To-Do 리스트 작성 (Must/Should/Nice/Tech Debt/Done/회귀/마일스톤)

**Agent**  
- `docs/06_todo_list.md`

---

### 2.9. prompting · report (본 요청)

**User**  
> 06 링크 추가 + Agent 대화 prompting 저장 + 보고서

**Agent**  
- `03_work_guide.md` §06 링크
- `prompting/00_SPEC_prompt.md` (본 파일)
- `prompting/User_prompt.md`, `GIT_prompt.md`
- `report/00_SPEC_phase_report.md`

---

## 3. 생성·수정 파일 목록

### docs/

| 파일 | 비고 |
|------|------|
| `00_prd.md` | PRD v2.0 |
| `01_analysis.md` | PRD+src 분석 |
| `02_work_scenario.md` | 단계 시나리오 |
| `03_work_guide.md` | 작업 안내 |
| `04_mom_test.md` | Mom Test 10 |
| `05_code_smell.md` | 사례 3건 |
| `06_todo_list.md` | To-Do |

### 루트

| 파일 | 비고 |
|------|------|
| `.cursorrules` | Agent YAML |
| `tdd_rules.yaml` | TDD 트랙 |

### prompting/ · report/

| 파일 | 비고 |
|------|------|
| `prompting/00_SPEC_prompt.md` | 본 대화록 |
| `prompting/User_prompt.md` | 사용자 프롬프트 표 |
| `prompting/GIT_prompt.md` | Git 명령 모음 |
| `report/00_SPEC_phase_report.md` | SPEC 결과 보고서 |

### Git에 포함된 SPEC 커밋 (원격)

- `docs/analysis.md` only (`a306870`) — 이후 docs 00~06은 **로컬 추가·푸시 대기**

---

## 4. 미완 · 후속 (Agent 권고)

1. `docs/00`~`06`, `.cursorrules`, `tdd_rules.yaml`, `prompting/`, `report/` 일괄 커밋·푸시
2. PR #1 갱신 또는 신규 커밋 push 후 리뷰
3. PR #1 → `A-01` 머지
4. RED 브랜치 착수 (`06_todo_list.md` M1)

---

## 5. 참고

- 상세 사용자 입력: `prompting/User_prompt.md`
- Git 명령: `prompting/GIT_prompt.md`
- SPEC 결과: `report/00_SPEC_phase_report.md`
