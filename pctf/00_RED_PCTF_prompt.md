# RED 단계 PCTF Prompt — Feedback Analyzer

| 항목 | 내용 |
|------|------|
| 단계 | **RED** (2단계 — 테스트 구조 개선·TDD 실패 테스트 선행) |
| 브랜치 | `RED` (base: `A-01`) |
| PRD | FR-19, FR-20, FR-21 |
| 근거 | `README.md`, `project_purpose.md`, `docs/00_prd.md` ~ `docs/06_todo_list.md`, `tdd_rules.yaml`, `.cursorrules`, `작업규칙.TXT` |
| 작성일 | 2026-05-22 |
| 용도 | Cursor Agent에 **복사·붙여넣기**하여 RED 단계를 일관되게 수행 |

---

## PCTF 사용법

| 글자 | 의미 | 본 문서 섹션 |
|------|------|----------------|
| **P** | Purpose — 왜·무엇을 | §1 |
| **C** | Context — 현재 코드·제약 | §2 |
| **T** | Task — 실행 순서·세부 프롬프트 | §3 |
| **F** | Format — 산출물·금지·DoD | §4 |

**권장 순서**: Agent 세션 시작 시 **§5 마스터 프롬프트** 1회 → §3 하위 프롬프트를 순서대로 실행.

---

## 1. P — Purpose (목적)

### 1.1. 프로젝트 목적 (`project_purpose.md`, `README.md`)

- **Feedback Analyzer**: 자연어 고객 피드백 **수집·키워드 분류·감정 분석·시각화** (Spring Boot 3 + Thymeleaf).
- 초기 코드는 **의도적 코드 스멜·안티패턴** 포함 → 학습자가 TDD·리팩토링으로 클린 아키텍처로 개선.
- RED 단계 목표: **실패/회귀 테스트를 먼저 작성**하고, JaCoCo **line coverage ≥ 90%**로 품질 기준선 확보. **프로덕션 버그 수정은 GREEN**에서 수행.

### 1.2. RED 단계가 달성해야 할 것 (`docs/00_prd.md` §5.5, `docs/06_todo_list.md` M1)

| ID | 요구 | RED에서의 의미 |
|----|------|----------------|
| FR-19 | JUnit 5 TC | `TextAnalyzerTest`, `FiltersTest`, `FileHandlerTest` 존재 |
| FR-20 | 커버리지 ≥ 90% | `pom.xml` JaCoCo + `mvn test jacoco:report` |
| FR-21 | 회귀 기반 | GREEN 이후에도 동일 TC 유지·통과 (RED에서 삭제·@Disabled 금지) |

### 1.3. RED에서 하지 않을 것 (`tdd_rules.yaml` → `tracks.RED`)

- 중립 필터·multiline·로그 UI **본격 수정** (GREEN, FR-09~11)
- Controller Service 분리·패키지 대이동 (REFACTORING)
- HTTP 경로·CSV `text` 컬럼 계약 변경
- 테스트 삭제·무분별한 `@Disabled`로 커버리지 맞추기

---

## 2. C — Context (컨텍스트)

### 2.1. 기술 스택 (`README.md`, `.cursorrules`)

```bash
mvn spring-boot:run   # http://localhost:8080
mvn test
mvn test jacoco:report  # RED 완료 후
```

| 항목 | 값 |
|------|-----|
| Java | 17 |
| Spring Boot | 3.5.3 |
| 테스트 | JUnit 5, `spring-boot-starter-test` |
| 패키지 | `com.example.demo` |
| CSV | 필수 컬럼 `text`, 헤더 1행 |

### 2.2. 현재 테스트·커버리지 상태 (`docs/01_analysis.md` §8)

| 항목 | 상태 |
|------|------|
| `DemoApplicationTests` | `contextLoads()` **만** 존재 |
| `TextAnalyzerTest` / `FiltersTest` / `FileHandlerTest` | **없음** |
| JaCoCo | **미설정** (`pom.xml` 추가 필요) |
| 커버리지 | 0%에 가까움 → FR-20 미충족 |

### 2.3. RED에 반드시 반영할 회귀·버그 TC (`tdd_rules.yaml`, `docs/01_analysis.md` P0)

| TC ID | 설명 | RED | GREEN |
|-------|------|-----|-------|
| **TC-NEUTRAL-01** | `"보통입니다"` 등 — `analyzeSentiment`와 `중립` 필터 결과 **일치** | 작성·**실패 허용** | 통과 필수 |
| **TC-NEUTRAL-02** | `"괜찮"` 등 — 긍정/중립 우선순위 꼬임 없음 | 작성·실패 허용 | 통과 필수 |
| (참고) TC-DOWNLOAD-01, TC-UPLOAD-01 | 다운로드·업로드 | 선택(RED) | GREEN 권장 |

**원인 요약** (`docs/05_code_smell.md` 사례1): `TextAnalyzer` vs `Filters.S_KEYWORDS` vs `Constants` **감정 규칙 이중화**.

### 2.4. 테스트 대상 클래스·메서드 힌트

| 클래스 | RED에서 검증할 행위 |
|--------|---------------------|
| `TextAnalyzer` | `analyzeSentiment` (긍정/부정/중립), `analyzeKeywords` (카테고리), 빈 목록 |
| `Filters` | `fil` — 감정·키워드·복합 필터, `중립`/`전체` |
| `FileHandler` | `save` / `saveResult`, 빈 리스트 |
| `DemoApplicationTests` | `contextLoads` 유지 |

### 2.5. API·계약 (RED에서도 불변 — `docs/00_prd.md` §6.4)

`GET /`, `POST /analyze`, `POST /upload`, `POST /filter`, `GET /download` — 경로·메서드 변경 금지.

### 2.6. 선행 문서 읽기 순서 (Agent)

1. `docs/00_prd.md` → `docs/01_analysis.md` → `docs/02_work_scenario.md` §5  
2. `.cursorrules` → `tdd_rules.yaml` (`tracks.RED`만)  
3. `docs/06_todo_list.md` — Must RED 항목  
4. `src/main/java/com/example/demo/{TextAnalyzer,Filters,FileHandler}.java`

---

## 3. T — Task (작업·실행 프롬프트)

### 3.0. 사전 Git (`docs/02_work_scenario.md` §5)

```bash
git checkout RED
git pull origin A-01   # A-01 최신화 필요 시
```

커밋 시 `target/`, `*.class` **제외** (`docs/03_work_guide.md` CM-02~03).

---

### 3.1. [T-1] JaCoCo 설정

```
@pom.xml
`tdd_rules.yaml`의 jacoco 규칙에 맞게 org.jacoco:jacoco-maven-plugin을 추가해줘.
- package com.example.demo line coverage minimum 90%
- DemoApplication은 coverage exclude
- verify 단계에서 report 생성되도록 설정
변경 후 `mvn test jacoco:report` 실행 방법을 알려줘.
```

---

### 3.2. [T-2] TextAnalyzerTest

```
@TextAnalyzer.java @tdd_rules.yaml
RED 브랜치에서 TextAnalyzerTest(JUnit 5)를 작성해줘.
위치: src/test/java/com/example/demo/TextAnalyzerTest.java

필수 커버:
- analyzeSentiment: 긍정/부정/중립(기본·키워드) 각 1건 이상
- analyzeKeywords: 카테고리(배송·품질·가격·서비스·사용성) 매칭
- 빈 feedback 목록

규칙:
- 프로덕션 로직 수정 금지(컴파일용 최소 변경만)
- Constants는 실제 fixture 사용, 과도한 mock 금지
- FR-19, FR-04, FR-05 연결 주석 1줄씩
실행: mvn test -Dtest=TextAnalyzerTest
```

---

### 3.3. [T-3] FiltersTest (중립 TC 필수)

```
@Filters.java @TextAnalyzer.java @docs/01_analysis.md
RED 브랜치에서 FiltersTest(JUnit 5)를 작성해줘.
위치: src/test/java/com/example/demo/FiltersTest.java

필수 커버:
- filter_sentiment: 긍정, 부정, 중립, 전체
- filter_keyword: 특정 카테고리, 전체
- 감정+키워드 복합 필터

필수 회귀 TC (tdd_rules.yaml):
- TC-NEUTRAL-01: 텍스트 "보통이에요" — analyzeSentiment 결과와 중립 필터 결과 일치 검증
- TC-NEUTRAL-02: "괜찮" 포함 문장 — 중립 필터가 의도치 않게 비우지 않음

baseline에서 실패하면 정상(RED). 실패 테스트 이름을 리포트에 기록할 것.
프로덕션 버그 수정은 하지 말 것.
```

---

### 3.4. [T-4] FileHandlerTest

```
@FileHandler.java @tdd_rules.yaml
RED 브랜치에서 FileHandlerTest(JUnit 5)를 작성해줴.
위치: src/test/java/com/example/demo/FileHandlerTest.java

필수 커버:
- save 또는 saveResult 호출 시 예외 없음
- 빈 리스트 처리

FileHandler가 Controller에서 미사용(Lava Flow)이어도 단위 테스트는 작성.
FR-19 연결.
```

---

### 3.5. [T-5] 커버리지 측정·보강

```
@Codebase src/test/java/com/example/demo/
`mvn test jacoco:report` 실행 후 com.example.demo 패키지 line coverage가 90% 미만이면
- Controller/Session 등 누락 구간에 최소 단위 테스트 추가
- 테스트 삭제·@Disabled로 수치 맞추기 금지

90% 달성 시 target/site/jacoco/index.html 기준 수치를 요약해줘.
```

---

### 3.6. [T-6] RED 커버리지 보고서

```
@docs/03_work_guide.md @tdd_rules.yaml
report/00_RED_coverage_report.md를 작성해줘.

포함:
- JaCoCo line coverage % (com.example.demo)
- 추가한 테스트 클래스·메서드 목록
- RED에서 의도적으로 실패하는 TC (TC-NEUTRAL-01/02 등) — 실패 메시지 요약
- GREEN에서 수정할 항목 목록 (FR-09 연결)
- mvn test 실행 결과 (failures/errors 개수)

코드 구현 변경 없이 문서만 작성해도 됨(이미 테스트 완료 후).
```

---

### 3.7. [T-7] 커밋·PR

```
RED 단계 산출물만 커밋해줘.
포함: pom.xml(JaCoCo), src/test/**, report/00_RED_coverage_report.md
제외: target/, *.class

커밋 메시지 예: RED 단계 진행: JaCoCo + TextAnalyzer/Filters/FileHandler 테스트

PR: RED → A-01
본문: 변경 요약, mvn test 결과, jacoco %, report 링크
```

---

## 4. F — Format (산출물·금지·완료 기준)

### 4.1. 필수 산출물

| 경로 | 설명 |
|------|------|
| `pom.xml` | JaCoCo 플러그인·90% rule |
| `src/test/java/com/example/demo/TextAnalyzerTest.java` | FR-19 |
| `src/test/java/com/example/demo/FiltersTest.java` | FR-19, TC-NEUTRAL-* |
| `src/test/java/com/example/demo/FileHandlerTest.java` | FR-19 |
| `src/test/java/com/example/demo/DemoApplicationTests.java` | `contextLoads` 유지 |
| `report/00_RED_coverage_report.md` | FR-20 |
| `prompting/01_RED_prompt.md` | (단계 종료 후) Agent 대화 전체 기록 |
| `prompting/User_prompt.md` | 사용자 프롬프트 표 갱신 |
| `prompting/GIT_prompt.md` | Git 명령 갱신 |

### 4.2. DoD 체크리스트 (`docs/02_work_scenario.md` §5, `docs/03_work_guide.md` §7)

- [ ] 브랜치 `RED`, base `A-01`
- [ ] 3개 테스트 클래스 존재·`mvn test` 실행 가능
- [ ] JaCoCo line coverage **≥ 90%** (`com.example.demo`)
- [ ] TC-NEUTRAL-01/02 **테스트 코드 존재** (실패는 RED에서 허용, 문서화 필수)
- [ ] `report/00_RED_coverage_report.md` 작성
- [ ] PR RED → A-01 (리뷰어 Approve)
- [ ] `target/`, `*.class` 미커밋

### 4.3. 금지·주의 (F — Forbidden)

| ID | 금지 |
|----|------|
| F-01 | GREEN용 버그 수정(중립 필터·multiline·Logger UI)을 RED PR에 포함 |
| F-02 | 테스트 삭제·@Disabled로 커버리지 90% 달성 |
| F-03 | HTTP API·CSV `text` 계약 변경 |
| F-04 | `target/`, `*.class` 커밋 |
| F-05 | REFACTORING 규모 패키지 이동·public API rename |
| F-06 | RED 없이 GREEN 진행 (`tdd_rules.yaml` workflow) |

### 4.4. 품질 명령

```bash
mvn test
mvn test jacoco:report
# 리포트: target/site/jacoco/index.html
```

---

## 5. 마스터 프롬프트 (RED 세션 — 복사용)

아래 블록을 **한 번에** Agent에 붙여넣어 RED 단계 전체를 진행한다.

```
[RED PCTF — Feedback Analyzer]

P (Purpose)
- 브랜치 RED에서 FR-19~21 충족: JUnit5 테스트 선행, JaCoCo line coverage ≥ 90%.
- 프로덕션 버그 수정은 GREEN; RED에서는 실패 테스트·회귀 TC 문서화가 목표.
- 근거: project_purpose.md(테스트 가능 아키텍처), README.md(기능), docs/00_prd.md FR-19~21.

C (Context)
- 현재 DemoApplicationTests.contextLoads()만 존재. TextAnalyzer/Filters/FileHandler 테스트 없음.
- JaCoCo 미설정. 감정 규칙 이중화로 TC-NEUTRAL-01/02가 baseline에서 실패할 수 있음(정상).
- API 고정: GET /, POST /analyze, /upload, /filter, GET /download. CSV 필수 컬럼 text.
- 먼저 읽기: docs/01_analysis.md, tdd_rules.yaml tracks.RED, .cursorrules.

T (Task) — 순서 고정
1. pom.xml에 JaCoCo 플러그인 추가 (90%, DemoApplication exclude)
2. TextAnalyzerTest 작성 (긍정/부정/중립, 키워드 카테고리, 빈 목록)
3. FiltersTest 작성 (감정/키워드/복합 + TC-NEUTRAL-01, TC-NEUTRAL-02)
4. FileHandlerTest 작성 (save/saveResult, 빈 리스트)
5. mvn test jacoco:report — com.example.demo ≥ 90%까지 최소 테스트 보강
6. report/00_RED_coverage_report.md (커버리지 %, 실패 TC 목록, GREEN 할 일)
7. 커밋(테스트·pom·report만) 후 PR RED→A-01

F (Format)
- 산출물: 위 테스트 3종, report/00_RED_coverage_report.md, JaCoCo 설정.
- 금지: 중립필터/multiline/Logger UI 수정, target/*.class 커밋, 테스트 삭제로 커버리지 조작.
- 완료: 90% 이상 + 3 Test 클래스 + 리포트 + mvn test 결과 문서화.

공식 1줄 프롬프트(작업규칙.TXT):
@DemoApplicationTests.java 커버리지 90% 달성을 위한 JUnit 5 테스트를 작성해줘.
TextAnalyzer·Filters·FileHandler 각 클래스별 TC 포함.

각 단계 후 mvn test 실행하고 결과를 요약해줘.
```

---

## 6. 공식 프롬프트 (작업규칙.TXT · `.cursorrules`)

```
@DemoApplicationTests.java 커버리지 90% 달성을 위한 JUnit 5 테스트를 작성해줘.
TextAnalyzer·Filters·FileHandler 각 클래스별 TC 포함.
```

---

## 7. 교차 참조

| 문서 | 경로 |
|------|------|
| 설치·기능 | `README.md` |
| 학습 목표·스멜 | `project_purpose.md` |
| PRD FR-xx | `docs/00_prd.md` |
| P0 갭·TC | `docs/01_analysis.md` |
| RED 시나리오 | `docs/02_work_scenario.md` §5 |
| Git·산출물 | `docs/03_work_guide.md` |
| To-Do Must | `docs/06_todo_list.md` |
| TDD RED 트랙 | `tdd_rules.yaml` → `tracks.RED` |
| SPEC Agent 기록 | `prompting/00_SPEC_prompt.md` |
| SPEC 보고서 | `report/00_SPEC_phase_report.md` |

---

## 8. RED 완료 후 다음 단계

| 순서 | 작업 |
|------|------|
| 1 | PR RED → `A-01` 머지 |
| 2 | `git checkout -b GREEN` (from `A-01`) |
| 3 | `pctf/` 또는 `prompting/01_RED_prompt.md`에 대화 기록 |
| 4 | GREEN PCTF — FR-09~11 (중립·multiline·로그 UI) |
