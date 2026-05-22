# 발표용 Keep 5 — 프롬프트·코드 리뷰 운영

> Feedback Analyzer · Cursor AI · PCTF 기반 QA  
> 상세: `docs/15_kpt_prompt_code_review_retrospective.md`

---

## Keep 1. PCTF + 작업규칙 = 실행 계약

- 단계마다 **산출물 경로 · 브랜치 · 커밋 메시지**를 프롬프트에 고정
- 예: `pctf/07` §★ PROMPT → `docs/12`, `report/05`, `QA step2/4`
- **효과**: Agent가 범위 밖 구현·커밋을 줄이고, 재작업 감소

---

## Keep 2. Before / After = Git SHA

- **Before** `6e88371` (GREEN 직후) · **After** `A-01` HEAD
- `git show <SHA>`로 코드 인용 → 리뷰 근거가 명확
- **효과**: “리팩토링 전” 정의가 팀·문서·본인 간 일치

---

## Keep 3. 단계마다 commit + push

- New_Feature step1~5, QA step2/4/5처럼 **한 PCTF 단계 = 한 push**
- 통합 브랜치 `A-01`, 기능 브랜치는 **삭제하지 않음**
- **효과**: PR·롤백·인수인계가 단계 단위로 추적 가능

---

## Keep 4. prompting 기록 = 나중의 나를 위한 자산

- `User_prompt.md` (#1~#58), `GIT_prompt.md`, `{단계}_prompt.md`
- 단계 종료 시 **프롬프트·Git 동기화 1회** (Ritual)
- **효과**: 대화가 끊겨도(#41) 이어가기 · KPT·발표 재현 용이

---

## Keep 5. 검증은 숫자로 끝낸다

- Agent “통과”보다 **`mvn clean test`** (stale `target/` 방지)
- 보고서에 **Tests / Failures / JaCoCo %** 표기 (예: 41 · 0 · 90.4%)
- QA는 **READ-ONLY** — 리뷰는 문서, 구현은 다른 세션
- **효과**: 코드 리뷰·발표 시 설득력 · 회귀 신뢰

---

## 발표 한 줄 (30초)

> **규칙으로 단계를 잠그고, SHA로 전후를 말하고, push와 문서로 남기고, clean test 숫자로 끝낸다.**
