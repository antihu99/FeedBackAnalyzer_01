# User Prompt 기록 — Feedback Analyzer



> `작업규칙.TXT`: 사용자 입력 prompt만 표 형식 + 요약  

> **최종 갱신**: 2026-05-22



---



## 요약



| 항목 | 내용 |

|------|------|

| 총 프롬프트 수 | **42** |

| SPEC 관련 | #1~#17 |

| RED 관련 | #18~#32 |

| GREEN 관련 | #33~#40 |

| **A-01 HEAD** | `6e88371` (SPEC + RED + GREEN 통합) |

| GREEN 최종 커밋 | `6e88371` — TC 결과 문서 + 머지 완료 |

| 테스트 | Tests 34, Failures **0**, JaCoCo **90.8%** |

| PR | #4 GREEN→A-01 **MERGED** · #3 A-01→main **OPEN** (SPEC+RED+GREEN) |



---



## 프롬프트 목록



| # | 일시 | 단계 | 요약 | 원문 |

|---|------|------|------|------|

| 1 | 2026-05-21 | Git | A-01 생성 | GIT 의 원격과 로컬에 A-01 브랜치를 생성해줘 |

| 2 | 2026-05-21 | SPEC | 1차 분석 | @Codebase … analysis.md 저장 |

| 3 | 2026-05-21 | Git | 브랜치 확인 | 현재 브랜치를 확인해주세요 |

| 4 | 2026-05-21 | Git | 동기화 확인 | 원격과 로컬이 동일한가요? |

| 5 | 2026-05-21 | SPEC | SPEC 1차 push | SPEC 브랜치, "SPEC 단계 진행", CLASS 제외 |

| 6 | 2026-05-21 | Git | 브랜치 확인 | 현재 브랜치를 확인해주세요 |

| 7 | 2026-05-21 | Git | PR 확인 | PR 생성도 확인해주세요 |

| 8 | 2026-05-21 | SPEC | docs·시나리오·안내 | 작업규칙·README 기반 시나리오·작업안내 |

| 9 | 2026-05-21 | SPEC | PRD | project_purpose·README → PRD |

| 10 | 2026-05-21 | SPEC | docs 2차 재작성 | docs 폴더 PRD→analysis→시나리오·안내 |

| 11 | 2026-05-21 | Phase1 | Mom Test | Mom Test 10 + code_smell 3건, 코드 금지 |

| 12 | 2026-05-21 | Phase3 | Agent·TDD YAML | .cursorrules, tdd_rules.yaml |

| 13 | 2026-05-21 | Phase3/6 | guide 링크 | 03_work_guide에 규칙 링크 |

| 14 | 2026-05-21 | Phase6 | To-Do | toDo_list Markdown |

| 15 | 2026-05-21 | SPEC | prompting·report | Agent 대화 prompt 저장, SPEC 보고서 |

| 16 | 2026-05-21 | SPEC | 2차 push | "#SPEC 단계 : PRD, TODO, 작업 시나이오 작성" |

| 17 | 2026-05-21 | SPEC | prompt 갱신 | 저장된 PROMPT 파일 UPDATE |

| 18 | 2026-05-22 | Git | SPEC→A-01 | SPEC 브랜치를 A-01에 MERGE |

| 19 | 2026-05-22 | Git | A-01 push | PUSH 도 진행해줘 |

| 20 | 2026-05-22 | Git | RED 생성 | A-01에서 RED 브랜치 만들기 |

| 21 | 2026-05-22 | Git | RED push | RED 브랜치 PUSH |

| 22 | 2026-05-22 | RED | PCTF 1차 | DOCS·README·project_purpose → RED PCTF 생성 |

| 23 | 2026-05-22 | Git | RED 업로드 | GitHub RED 동기화, "Red 단계진행을 위한 준비" |

| 24 | 2026-05-22 | Git | PR RED→A-01 | a-01과 red 브랜치 PR |

| 25 | 2026-05-22 | Git | 원격 동기화 | 원격과 동기화 해줘 |

| 26 | 2026-05-22 | RED | 테스트 플랜 PCTF | 실패 TC 불명확 → 테스트 플랜 PCTF 후 RED PCTF 재작성 |

| 27 | 2026-05-22 | RED | PCTF 번호 재정리 | 00=TEST_PLAN, PROMPT 입력형, 산출물 TEST_PLAN |

| 28 | 2026-05-22 | RED | PCTF 00 확인 | pctf/00_RED_test_plan_PCTF.md (파일명만) |

| 29 | 2026-05-22 | RED | **PCTF 00 실행** | [PCTF 00 — RED TEST_PLAN 작성] 전체 PROMPT |

| 30 | 2026-05-22 | RED | **PCTF 01 실행** | [PCTF 01 — RED 구현] 전체 PROMPT |

| 31 | 2026-05-22 | Git | RED push·머지 | GITHUB RED 업로드 → RED→A-01 머지 → A-01→main PR |

| 32 | 2026-05-22 | RED | prompt 저장 | RED 단계 PROMPT 기억·문서화 요청 |

| 33 | 2026-05-22 | GREEN | 진행 확인 | 기존에 입력한 prompt 를 진행하고 있나요? |

| 34 | 2026-05-22 | GREEN | **PCTF 02 실행** | GREEN PCTF 실행 |

| 35 | 2026-05-22 | GREEN | PR·문서화 | a-01 과 green 브랜치 pr 도 생성하고, 문서화도 진행해줘 |

| 36 | 2026-05-22 | GREEN | TC 결과 문서 | MERGE 전 GREEN TC 결과를 docs 폴더에 만들어줘 |

| 37 | 2026-05-22 | Git | GREEN→A-01 머지 | a-01 과 green 브랜치를 머지 해줘 |

| 38 | 2026-05-22 | Git | PR 개념 질문 | pr 갱신이랑 뭔가요? (Ask) |

| 39 | 2026-05-22 | Git | **PR #3 갱신** | A-01 → main PR 갱신해주세요 |

| 40 | 2026-05-22 | Meta | **prompting 동기화** | prompting 폴더에 있는 프롬프트들을 동기화 해주세요 |
| 41 | 2026-05-22 | REFACTORING | **작업 재개** | agent PROMPT 사라짐 → REFACTORING step4~5 이어서 진행 |
| 42 | 2026-05-22 | REFACTORING | **Git push + step6** | step4~5 push, step6 패키지 분리, report/02, origin/REFACTORING push |



---



## 단계별 Agent 기록 파일



| 단계 | 파일 | 상태 |

|------|------|------|

| SPEC | `00_SPEC_prompt.md` | 완료 |

| RED | `01_RED_prompt.md` | 완료 |

| GREEN | `02_GREEN_prompt.md` | 완료 |
| REFACTORING | `03_REFACTORING_prompt.md` | step4~6 완료 |

| Git 명령 | `GIT_prompt.md` | 본 갱신 포함 |

| 사용자 prompt | `User_prompt.md` | 본 문서 |



---



## GREEN 단계 핵심 PROMPT (#34)



- **입력**: `pctf/02_GREEN_PCTF_prompt.md` §★ PROMPT

- **산출물**: `Filters.java`, `report/01_GREEN_bugfix_report.md`, `docs/08_GREEN_test_results.md`

- **결과**: Failures **0**, JaCoCo **90.8%**, TC-NEUTRAL **PASS**



전문: `prompting/02_GREEN_prompt.md` §2



---



## RED 단계 핵심 PROMPT (#29, #30)



### #29 — PCTF 00 (TEST_PLAN)



- **산출물**: `docs/07_RED_test_plan.md`



### #30 — PCTF 01 ★ RED 완료



- **산출물**: `pom.xml`, `src/test/**`, `report/00_RED_coverage_report.md`

- **결과**: Failures **2**, JaCoCo **90.9%**



전문: `prompting/01_RED_prompt.md` §2



---



## 단계별 그룹



### SPEC (#1~#17)



`9b03001` → PR #1



### RED (#18~#31)



`e07ca6b` → PR #2, #3(초안)



### GREEN (#33~#40)



PCTF 02 → PR #4 → `docs/08` → A-01 머지 → PR #3 갱신 → prompting 동기화



---



## Agent 응답 요약 (GREEN)



| # | 주요 산출 |

|---|-----------|

| 33 | RED 완료·GREEN 대기 안내 |

| 34 | FR-09 `Filters.java`, 리포트, push `87136db`·`c9b926a` |

| 35 | PR #4, `02_GREEN_prompt.md`, `01_RED_prompt.md` |

| 36 | `docs/08_GREEN_test_results.md`, commit `6e88371` |

| 37 | `GREEN`→`A-01` fast-forward, `origin/A-01` = `6e88371` |

| 38 | PR #3 갱신 개념 설명 |

| 39 | PR #3 제목·본문 SPEC+RED+GREEN 반영 |

| 40 | prompting 5종 동기화 |



---



## Agent 응답 요약 (RED)



| # | 주요 산출 |

|---|-----------|

| 29 | `docs/07_RED_test_plan.md` |

| 30 | JUnit, JaCoCo 90.9%, `report/00_RED_coverage_report.md` |

| 31 | `e07ca6b`, PR #3 Open |


