# KPT 회고 — Code Review·QA 단계 프롬프트 사용 경험

| 항목 | 내용 |
|------|------|
| 문서 ID | DOC-KPT-001 |
| 회고 유형 | **KPT** (Keep · Problem · Try) |
| 범위 | Feedback Analyzer — SPEC~QA, **코드 리뷰·문서 검증** 중심 |
| 도구 | Cursor AI (Agent) + PCTF·`작업규칙.TXT` |
| 작성일 | 2026-05-22 |
| 참고 | `report/06_retrospective.md`, `docs/13`, `docs/14`, `prompting/User_prompt.md` (#57~#58) |

---

## 회고 배경

코드 리뷰(Code Review) 작업은 이 프로젝트에서 **프로덕션 코드를 고치지 않고**, 리팩토링 전후를 **검증·설명·기록**하는 단계(QA REVIEW, Cursor AI 분석 보고서, 시나리오 정합 점검)로 정의되었다.  
약 **58회**의 사용자 프롬프트와 **PCTF 07** 마스터 PROMPT, `@작업규칙.TXT`·`@docs/02_work_scenario.md` 첨부가 핵심 입력이었다.

```text
[사용자] @07_QA_REVIEW_PCTF_prompt.md + @작업규칙.TXT + Before SHA 6e88371
    → [Agent] docs/12, report/05, mvn clean test, commit+push (QA)
    → [사용자] "회고·PR·시나리오 정합·KPT" 후속 프롬프트
```

---

## Keep — 계속 유지할 것

### 1. PCTF + 작업규칙을 “실행 계약”으로 쓰기

| Keep | 이유 | 사례 |
|------|------|------|
| **§★ PROMPT 블록만 붙여 실행** | 산출물 경로·커밋 메시지·브랜치가 고정되어 재작업 감소 | `pctf/07` QA-02~04 → `docs/12`, `report/05` |
| **단계마다 commit + push** | 리뷰·롤백·PR diff가 단계 단위로 보임 | `QA step2` → `step4` → `step5` |
| **READ-ONLY 리뷰 단계 명시** | QA에서 코드 손대지 않아 회귀·논쟁 최소화 | FR-12~16 **문서만** 검증 |

### 2. Before/After를 Git SHA로 고정

| Keep | 이유 | 사례 |
|------|------|------|
| **Before = `6e88371` (GREEN 직후)** | “리팩토링 전” 정의가 팀·문서 간 일치 | `report/05`, `docs/13` |
| **`git show <SHA>:path` 발췌** | 코드 인용의 출처가 명확 | Filters `fil` → `filterFeedbacks` 비교 |
| **After = `A-01` HEAD** | 통합 브랜치 = 실제 배포 후보 | 41 tests, 90.4% JaCoCo |

### 3. @참조 파일로 컨텍스트 한 번에 주기

| Keep | 이유 | 사례 |
|------|------|------|
| `@작업규칙.TXT` `@docs/02_work_scenario.md` `@docs/03_work_guide.md`** | Agent가 브랜치·산출물 규칙을 따름 | 시나리오 검증 `docs/14` |
| `@pctf/07_QA_REVIEW_PCTF_prompt.md` (라인 범위)** | 범위 밖 구현·커밋 방지 | 288–325 마스터 PROMPT |
| **`prompting/` 동기화 요청** | 대화가 끊겨도 #41처럼 이어하기 쉬움 | `User_prompt.md`, `GIT_prompt.md` |

### 4. 검증은 프롬프트 밖에서 “한 번 더”

| Keep | 이유 | 사례 |
|------|------|------|
| **`mvn clean test` (not `mvn test`만)** | stale `target/` 빈 충돌 방지 | ApplicationContext `FeedbackController` 중복 |
| **회귀 수치를 보고서에 표로 기록** | 리뷰어·발표 시 설득력 | Tests 41, 0 failures, 90.4% |
| **User_prompt # 표 누적** | 무엇을 언제 요청했는지 KPT·감사 추적 | #57 QA, #58 후속 |

### 5. 코드 리뷰 산출물을 “역할별”로 쪼개기

| Keep | 문서 | 역할 |
|------|------|------|
| `docs/12` | 비교 축·체크리스트 | 리뷰 **입문** |
| `report/05` | FR별 Before/After Java | **기술 리뷰 본문** |
| `docs/13` | Cursor AI 통합 보고서 | **대외·발표용** 요약 |
| `docs/14` | 시나리오 준수 검증 | **프로세스 리뷰** |
| `report/06` | 4문항 회고 | **학습 회고** (본 KPT는 `15`에 프롬프트 특화) |

---

## Problem — 문제·아쉬움

### 1. 프롬프트·문서 간 불일치

| Problem | 영향 | 경험 |
|---------|------|------|
| **회고 파일 번호 혼동** (`05` vs `06`) | Agent·시나리오가 서로 다른 경로 제안 | `02`는 `05_retrospective`, 실제는 `05`=REVIEW |
| **시나리오에 QA 단계 누락** (초기 `02`) | 8단계 작업규칙과 문서 버전 drift | `docs/14` 작성 후 `02`·`03` 수동 정합 필요 |
| **GREEN 3건 vs 실제 완료 시점** | FR-10 multiline이 GREEN이 아닌 NF에서 완료 | 리뷰 보고 시 “어느 단계 AC?” 혼란 |

### 2. 프롬프트만으로 끝나지 않는 작업

| Problem | 영향 | 경험 |
|---------|------|------|
| **`gh auth` 없음** | “PR 생성해줘” 반복 (#51, #53, #56) → 실패 | Agent 환경 한계, 사용자 재시도 |
| **짧은 후속만** (“네 진행하세요”) | Agent가 이전 맥락 요약에 의존 | 대화 요약 후에도 진행 가능했으나, 범위 모호 시 누락 위험 |
| **프롬프트 저장 요청이 잦음** (#40, #43, #56) | Meta 작업 비중 ↑ | 개발 속도보다 기록 동기화에 시간 소모 |

### 3. Code Review 프롬프트의 기술적 함정

| Problem | 영향 | 경험 |
|---------|------|------|
| **`git show` 터미널 한글 깨짐** | Before 코드 인용 품질 저하 | 보고서는 IDE/소스 기준으로 재작성 |
| **“통과”와 실제 빌드 불일치** | `mvn test`만 실행 시 11 errors | `mvn clean test`로 재검증 필요 |
| **범위 넓은 @Codebase** | 리뷰 단계인데 구현 제안 유혹 | PCTF “프로덕션 수정 금지”로 방어 |

### 4. 프롬프트 설계·운영

| Problem | 영향 | 경험 |
|---------|------|------|
| **PCTF 없이 단발성 요청** | 산출물·커밋 메시지 매번 협상 | REFACTORING 재개(#41) 때 PCTF 재첨부 필요 |
| **Ask vs Do 혼재** | #54, #55는 설명, #56은 실행 | 같은 세션에서 기대치 불일치 |
| **영문/한글 혼용 커밋 메시지** | `git log` 가독성 | 기능은 문제없으나 팀 규칙 통일 어려움 |

---

## Try — 다음에 시도할 것

### 1. 프롬프트 템플릿 (Code Review 전용)

다음 QA·코드 리뷰 시 **첫 메시지에 고정**:

```markdown
## Role
READ-ONLY code review. No production code changes.

## Baseline
- Before: git `<SHA>`
- After: branch `<BRANCH>` HEAD
- Verify: `mvn clean test` + jacoco ≥ 90%

## Deliverables (only these paths)
- docs/12_...
- report/05_...

## Git
Branch `<QA>`; after each step: commit + push with message `QA stepN: ...`
```

→ **Try**: `pctf/08_CODE_REVIEW_prompt.md` 초안으로 저장해 두기.

### 2. “한 프롬프트 = 한 커밋” 체크리스트

| Try | 내용 |
|-----|------|
| **종료 조건 명시** | “완료 시: commit hash, report path, test count 출력” |
| **실패 시 분기** | `gh` 실패 → “본문만 `docs/pr3_body_release_body.md` 작성, PR은 수동” |
| **검증 명령 고정** | 항상 `mvn clean test` (clean 필수 한 줄 주석) |

### 3. 문서·번호 단일 출처 (SSOT)

| Try | 내용 |
|-----|------|
| **`03_work_guide` report 표** | 05=REVIEW, 06=회고 — 새 단계는 07부터 |
| **시나리오 갱신 규칙** | `작업규칙.TXT` 변경 시 `02` §갱신일 한 줄 필수 |
| **KPT는 단계 종료 시** | QA 끝 → `docs/15` (본 문서) 템플릿 복사 |

### 4. PR·Git 프롬프트 분리

| Try | 내용 |
|-----|------|
| **PR 전용 프롬프트** | `gh auth status` 결과 붙여넣기 후 `gh pr edit`만 요청 |
| **개발 프롬프트와 분리** | 리뷰 세션에서는 `@Codebase` 최소화, `@report/05` 위주 |
| **User_prompt 태그** | 표에 `Meta` / `QA` / `Git` / `Ask` 열 유지 (#54~#56 교훈) |

### 5. 팀·발표용 축약 프롬프트

| Try | 내용 |
|-----|------|
| **“발표 슬라이드 5장” 프롬프트** | `docs/13` + `report/06` + **본 KPT** 입력 → 슬라이드 아웃라인만 생성 |
| **Keep 3 / Problem 3 / Try 3** | KPT 항목 수 상한 — 발표 시간 5분 가정 |
| **Before/After 1블록만** | 발표용은 Filters 또는 Controller **한 쌍**만 |

### 6. 학습 루프

| Try | 시기 | 행동 |
|-----|------|------|
| **RED 직후** | TC 실패 목록을 PR 본문에 붙이는 프롬프트 | 리뷰어가 GREEN 범위 합의 |
| **REFACTOR 전** | “계약 5 path 불변” 확인 프롬프트 1회 | URL 변경 diff 차단 |
| **릴리스 전** | `docs/14` 형식 검증 프롬프트 재사용 | 시나리오·산출물 누락 자동 점검 |

---

## KPT 한 줄 요약

| 구분 | 한 줄 |
|------|------|
| **Keep** | PCTF+SHA+`mvn clean test`+단계별 commit — 코드 리뷰를 **재현 가능한 문서 작업**으로 만든다 |
| **Problem** | 문서 번호·시나리오 drift, `gh`/stale build, 짧은 “진행하세요”만으로는 범위가 흐려진다 |
| **Try** | Code Review 전용 PROMPT 템플릿, SSOT, Git/리뷰 프롬프트 분리, KPT를 단계 종료 Ritual로 고정 |

---

## 부록 — Code Review 단계에서 효과가 컸던 프롬프트 패턴

| 패턴 | 예시 | 효과 |
|------|------|------|
| **마스터 PROMPT 인용** | `@07_QA_REVIEW_PCTF_prompt.md (288-325)` | 범위·산출물·Git 한 번에 전달 |
| **파일 첨부 3종** | 작업규칙 + 시나리오 + work_guide | 브랜치·명명 규칙 준수 |
| **숫자로 끝내기** | “41 tests, 90.4%, commit `71f241f`” | 완료 보고 신뢰도 ↑ |
| **후속을 구체화** | “회고 + A-01→main PR 본문 + 시나리오 정합” | #58 한 번에 여러 산출물 |
| **Meta를 묶기** | “prompting 동기화 + GIT_prompt 갱신” | #40, #43 중복 감소 |

---

## 관련 문서

| 경로 | 용도 |
|------|------|
| `docs/13_cursor_ai_code_review_report.md` | 기술·프로세스 통합 리뷰 |
| `docs/14_work_flow_verification.md` | 시나리오 준수 검증 |
| `report/06_retrospective.md` | 프로젝트 전체 4문항 회고 |
| `prompting/05_QA_REVIEW_prompt.md` | QA Agent·Git 기록 |
| `prompting/User_prompt.md` | 사용자 프롬프트 #1~#58 |
