# KPT 1페이지 요약 — 프롬프트·Code Review (발표 슬라이드)

**프로젝트** FeedBack Analyzer · **도구** Cursor AI + PCTF · **범위** SPEC → QA (프롬프트 58건)

---

## Keep · 계속할 것

| # | 한 줄 | 키워드 |
|---|--------|--------|
| 1 | PCTF·작업규칙으로 단계·산출물·커밋 고정 | 실행 계약 |
| 2 | Before/After를 Git SHA로 정의 (`6e88371` → A-01) | 재현 가능 리뷰 |
| 3 | PCTF 단계마다 commit + push, A-01 통합 | 추적 가능 Git |
| 4 | User/GIT/단계별 prompting 누적 | 대화·명령 자산 |
| 5 | `mvn clean test` + Tests/JaCoCo 숫자, QA는 READ-ONLY | 검증·신뢰 |

---

## Problem · 문제·아쉬움

| # | 한 줄 |
|---|--------|
| 1 | 문서 번호·시나리오 drift (`05` REVIEW vs `06` 회고) |
| 2 | `gh auth` 없이 PR 프롬프트 반복 → 자동화 실패 |
| 3 | `mvn test`만 실행 시 stale class → 11 errors |
| 4 | “진행하세요”만으로는 범위 흐림 · Meta(저장) 반복 |

---

## Try · 다음에 시도

| # | 한 줄 |
|---|--------|
| 1 | Code Review 전용 PROMPT 템플릿 (READ-ONLY + SHA + 산출물 경로) |
| 2 | 단계 종료 Ritual: prompting 1회 + KPT(`docs/15`) + 시나리오 정합 |
| 3 | PR은 본문 파일 먼저 · `gh auth` 후 1회만 |
| 4 | 발표·리뷰는 Keep/Problem/Try 각 3개 + Before/After 코드 1쌍 |

---

## 숫자 한 장 (성과)

| 항목 | 값 |
|------|-----|
| 사용자 프롬프트 | **58**건 (`User_prompt.md`) |
| 테스트 | **41** · Failures **0** |
| JaCoCo line | **~90.4%** |
| QA Before SHA | `6e88371` |

---

## 슬라이드 구성 제안 (5장)

1. **제목** — KPT: Cursor 프롬프트로 QA·코드 리뷰하기  
2. **Keep** — 위 표 5행 (아이콘: 🔒 SHA · 📤 push · 📋 문서)  
3. **Problem** — 4행 (gh · clean test · drift)  
4. **Try** — 4행 (템플릿 · Ritual)  
5. **마무리** — Keep 한 줄 + 숫자 표

---

## Keep 한 줄 (클로징)

**PCTF와 SHA로 단계를 잠그고, push와 prompting으로 남기고, clean test 숫자로 검증한다.**

---

*원문: `docs/15_kpt_prompt_code_review_retrospective.md` · Keep 상세: `presentation/01_keep_5_for_presentation.md`*
