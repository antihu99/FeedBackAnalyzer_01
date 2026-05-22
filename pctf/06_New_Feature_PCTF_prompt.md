# PCTF — New_Feature 단계 (Prompt 입력용)

| 항목 | 내용 |
|------|------|
| PCTF ID | `06_New_Feature_PCTF_prompt` |
| 시나리오 | `docs/02_work_scenario.md` §8 |
| 단계 | **New_Feature** (추가 기능 — FR-17~18) |
| 브랜치 | `New_Feature` (base: `A-01`) — 이미 `new_feature` 생성 시 동일 브랜치에서 진행 |
| PRD | FR-17 (Trend), FR-18 (File DB) |
| **선행** | RED·GREEN·REFACTORING 완료, `mvn test` 0 failures, JaCoCo ≥ **90%** |
| **본 PCTF 산출물** | CSV·스키마 문서, Feature TC, 구현, `report/04_New_Feature_report.md`, PR → A-01 |
| 예상 시간 | 3h |
| 작성일 | 2026-05-22 |

---

## PCTF → 산출물 흐름

```text
docs/02_work_scenario.md §8 (6단계)
         │
         ▼
[pctf/06_New_Feature_PCTF_prompt.md] ──PROMPT──▶ Agent
         │
         ├── NF-01: 브랜치 확인
         ├── NF-02: test_feedback_trend.csv + docs/10_feature_schema.md
         ├── NF-03: Feature TC (JUnit 5)
         ├── NF-04: FR-17 Trend + FR-18 File DB 구현
         ├── NF-05: report → commit + push
         └── NF-06: PR (Head: New_Feature → Base: A-01)
              ※ NF-01~05 각 단계 종료 시 GitHub commit + push 필수
```

---

## P — Purpose

- **FR-17**: `test_feedback_trend.csv`를 `src/main/resources/`에 두고, **기간별 피드백 건수 Trend**를 UI(차트)로 표시한다.
- **FR-18**: 감정·카테고리 **키워드를 File DB로 CRUD**하고, **애플리케이션 재기동 후에도** 설정이 유지된다.
- **회귀 유지**: 기존 RED TEST_PLAN TC(TA/FI/FH, TC-NEUTRAL) 및 `FeedbackControllerWebTest` **전부 PASS** (FR-19~21).
- **계약 유지**: 기존 HTTP 5 path·CSV `text` 컬럼 규칙 **불변** (`.cursorrules` `api_contract_frozen`).
- **마무리**: `report/04_New_Feature_report.md`, `New_Feature` → `A-01` PR.
- **Git (필수)**: 시나리오 **1~5단계**를 마칠 때마다 **반드시** `git commit` 후 **`git push origin <브랜치>`** 로 GitHub에 반영한다. (6단계는 PR 생성)

---

## C — Context

### 시나리오 6단계 ↔ PCTF 매핑

| 시나리오 순서 | 작업 | PCTF 단계 | 단계 종료 시 GitHub |
|---------------|------|-----------|---------------------|
| 1 | `git checkout -b New_Feature` | **NF-01** | **commit + push** |
| 2 | 샘플 CSV·스키마 문서 | **NF-02** | **commit + push** |
| 3 | Feature TC | **NF-03** | **commit + push** |
| 4 | 구현 | **NF-04** | **commit + push** |
| 5 | `report/04_New_Feature_report.md` | **NF-05** | **commit + push** |
| 6 | PR → A-01 | **NF-06** | PR 생성 (push는 5단계까지 완료) |

### 기능 요약

| # | 기능 | 시나리오 |
|---|------|----------|
| ① | **Trend** | `test_feedback_trend.csv` → `src/main/resources/` → 차트 UI |
| ② | **File DB** | 키워드 CRUD → 재기동 후 유지 |

### 필독·참조

| 유형 | 경로 |
|------|------|
| 시나리오 | `docs/02_work_scenario.md` §8 |
| PRD | `docs/00_prd.md` §5.4 (FR-17~18) |
| TDD | `tdd_rules.yaml` → `tracks.FEATURE` |
| 규칙 | `.cursorrules`, `작업규칙.TXT` §7단계 |
| TEST_PLAN (회귀) | `docs/07_RED_test_plan.md` |
| 기존 Web TC | `FeedbackControllerWebTest.java` |

### 진입 baseline (NF-00)

| 항목 | 기대값 |
|------|--------|
| `mvn test` | Failures **0**, Errors **0** |
| JaCoCo | `com.example.demo` line ≥ **90%** |
| 브랜치 | `New_Feature` 또는 `new_feature` (A-01 머지 반영) |

```bash
git checkout New_Feature    # 없으면: git checkout -b New_Feature A-01
git pull origin New_Feature # 원격 있을 때
mvn test
mvn jacoco:report
```

### contract_immutable (기존 API — 변경 금지)

| 항목 | 값 |
|------|-----|
| HTTP paths | `/`, `/analyze`, `/upload`, `/filter`, `/download` |
| CSV 업로드 | 필수 컬럼 `text`, 헤더 1행 |

> FR-17~18용 **신규 path** 추가는 허용 (예: `/keywords`, `/trend`). 기존 5 path 동작·응답은 깨지 않아야 함.

### 목표 구조 (권장)

```text
src/main/resources/
  test_feedback_trend.csv      # FR-17 샘플
  (기존) application.properties

src/main/java/com/example/demo/
  service/
    TrendService.java            # CSV 로드·기간별 집계
    KeywordConfigService.java    # File DB CRUD·로드
  repository/
    KeywordFileRepository.java   # JSON/YAML 파일 영속
  config/
    Constants.java               # File DB 미로드 시 fallback

data/                            # gitignore 권장 (런타임 생성)
  keywords.json                  # FR-18 영속 저장

src/test/java/com/example/demo/
  TrendServiceTest.java
  KeywordFileRepositoryTest.java
  KeywordConfigServiceTest.java
  (선택) TrendControllerWebTest.java

docs/
  10_feature_schema.md           # NF-02 스키마·AC

report/
  04_New_Feature_report.md       # NF-05
```

### FR-17 Trend — CSV·UI 스키마 (NF-02 확정)

**파일**: `src/main/resources/test_feedback_trend.csv`

| 컬럼 | 필수 | 설명 | 예시 |
|------|------|------|------|
| `date` | ✅ | 피드백 일자 `yyyy-MM-dd` | `2026-05-01` |
| `text` | ✅ | 피드백 본문 (기존 CSV 규칙과 동일) | `배송이 빨라서 좋아요` |

**집계 규칙**

- `date`별 건수를 집계해 Trend 시리즈 생성 (라벨=날짜, 값=건수).
- 파일 없음·빈 파일·헤더만: UI에 **안내 메시지** (에러 페이지 대신 warning).

**UI (최소)**

- `index.html`에 **Trend** 섹션 추가: 막대/라인 차트 또는 `stats` 스타일 시계열 표.
- Chart.js CDN 또는 기존 `.stats` 패턴 재사용 (과도한 프론트 프레임워크 금지).

### FR-18 File DB — 영속·CRUD (NF-02 확정)

**저장소**: 프로젝트 루트 `data/keywords.json` (또는 `application.properties`로 경로 설정)

**JSON 구조 (예시)**

```json
{
  "sentiment": {
    "긍정": ["좋아요", "만족"],
    "부정": ["불만", "실망"],
    "중립": ["보통", "괜찮"]
  },
  "category": {
    "배송": { "main": ["배송", "택배"], "sub": {} }
  }
}
```

**동작**

| 동작 | 요구 |
|------|------|
| 최초 기동 | 파일 없으면 `Constants` 기본값으로 파일 **시드 생성** |
| CRUD | UI 또는 REST로 감정·카테고리 키워드 추가/삭제/조회 |
| 분석 반영 | `TextAnalyzer` / `Filters` / `SentimentClassifier`가 File DB를 **단일 소스**로 사용 |
| 재기동 | 수정된 키워드가 **유지**되고, 분류 결과에 반영 |

**OCP**: 새 키워드 추가 시 `Constants.java` 4~5곳 수정 **금지** → File DB + Service만 수정.

### Git — 단계 완료 시 필수 commit + push

> **규칙**: `docs/02_work_scenario.md` §8 시나리오 **1~5단계**를 끝낼 때마다 아래 루프를 **생략하지 않는다**.  
> 마지막에 한꺼번에 push하지 않는다.

```text
단계 작업 완료 → git add <해당 산출물만> → git commit → git push origin <브랜치>
```

| 변수 | 값 |
|------|-----|
| 브랜치 | `New_Feature` 또는 로컬 `new_feature` (일관되게 하나만 사용) |
| 원격 | `origin` |

**스테이징 제외**: `target/`, `**/*.class`, `data/keywords.json`(런타임 생성 시 `.gitignore`에 `data/` 추가 권장)

**단계별 커밋·푸시 (시나리오 1:1)**

```bash
# NF-01 (시나리오 1) — 브랜치 준비
git checkout -b New_Feature A-01   # 또는 기존 브랜치 checkout
git commit --allow-empty -m "FEATURE step1: start New_Feature branch from A-01"
git push -u origin New_Feature

# NF-02 (시나리오 2)
git add src/main/resources/test_feedback_trend.csv docs/10_feature_schema.md
git commit -m "FEATURE step2: add trend CSV sample and feature schema (FR-17, FR-18)"
git push origin New_Feature

# NF-03 (시나리오 3)
git add src/test/java/
git commit -m "FEATURE step3: add Trend and Keyword File DB tests (FR-17, FR-18)"
git push origin New_Feature

# NF-04 (시나리오 4) — FR-17·FR-18 구현 완료 후 1회
git add src/main/java/ src/main/resources/templates/ src/main/resources/
git commit -m "FEATURE step4: Trend visualization and File DB keywords (FR-17, FR-18)"
git push origin New_Feature

# NF-05 (시나리오 5)
git add report/04_New_Feature_report.md
git commit -m "FEATURE step5: report/04_New_Feature_report.md"
git push origin New_Feature

# NF-06 (시나리오 6) — PR만 (추가 commit 없음, push는 5단계에서 완료)
gh pr create --base A-01 --head New_Feature ...
```

> NF-04를 **하위 작업(04a Trend → 04b File DB)** 으로 나눌 때: 시나리오 4 **전체**가 끝나기 전에는 push하지 않고, 04a·04b **모두** 완료 후 **step4 커밋 1회 + push** 한다. (중간 저장이 필요하면 로컬 commit만 하고 push는 step4 종료 시 1회)

---

## T — Task (단계별)

각 단계 공통 루프 (**NF-01~05 필수**):

```text
1) 해당 산출물 작성
2) mvn test (또는 -Dtest=대상) 실행 — NF-01·NF-02는 기존 TC 0 failures 유지
3) 회귀 0 failures 확인 (NF-04 이후)
4) git add <해당 단계 파일만>
5) git commit -m "FEATURE stepN: …"
6) git push origin <브랜치>   ← GitHub 반영 필수
```

### 단계 요약표

| 단계 | 시나리오 | 목표 | 산출물 | commit + push (필수) |
|------|----------|------|--------|----------------------|
| **NF-00** | — | baseline Green | — | *(커밋·push 생략)* |
| **NF-01** | 1 | 브랜치 | `New_Feature` | `FEATURE step1: start New_Feature branch from A-01` + **push** |
| **NF-02** | 2 | CSV·스키마 | `test_feedback_trend.csv`, `docs/10_feature_schema.md` | `FEATURE step2: …` + **push** |
| **NF-03** | 3 | Feature TC | `*Test.java` | `FEATURE step3: …` + **push** |
| **NF-04** | 4 | 구현 (04a+04b) | Service, UI, repository | `FEATURE step4: …` + **push** (시나리오 4 종료 시 1회) |
| **NF-05** | 5 | 리포트 | `report/04_New_Feature_report.md` | `FEATURE step5: …` + **push** |
| **NF-06** | 6 | PR | GitHub PR | `gh pr create` (추가 commit 없음) |

---

### NF-00 — 진입 baseline

| 항목 | 내용 |
|------|------|
| 명령 | `mvn test` → `mvn jacoco:report` |
| 확인 | Failures=0; JaCoCo line ≥ 90% |
| 수정 | 없음 |

---

### NF-01 — 브랜치 (시나리오 1)

```bash
git checkout A-01
git pull origin A-01
git checkout New_Feature || git checkout -b New_Feature A-01
# 이미 new_feature 사용 중이면: git checkout new_feature
```

- `A-01`에 GREEN·REFACTORING 머지 반영 여부 확인.

**Git (시나리오 1 종료 — 필수)**

```bash
# 파일 변경이 없으면 allow-empty, 있으면 해당 파일만 add
git commit --allow-empty -m "FEATURE step1: start New_Feature branch from A-01"
git push -u origin New_Feature    # 최초 push 시 -u, 이후 push origin New_Feature
```

---

### NF-02 — 샘플 CSV·스키마 문서 (시나리오 2)

1. `src/main/resources/test_feedback_trend.csv` 생성 (최소 **5행** 이상, `date`+`text`).
2. `docs/10_feature_schema.md` 작성:
   - FR-17 CSV 컬럼·집계·UI AC
   - FR-18 JSON 스키마·CRUD·재기동 AC
   - Feature TC ID 목록(§ Feature TC 표)
3. `mvn test` — 기존 TC만 실행, **0 failures** 유지.

**Git (시나리오 2 종료 — 필수)**

```bash
git add src/main/resources/test_feedback_trend.csv docs/10_feature_schema.md
git commit -m "FEATURE step2: add trend CSV sample and feature schema (FR-17, FR-18)"
git push origin New_Feature
```

---

### NF-03 — Feature TC (시나리오 3)

**신규 테스트만 추가.** 기존 `docs/07_RED_test_plan.md` TC **삭제·@Disabled·assert 완화 금지**.

#### Feature TC 표

| TC ID | 테스트 클래스 | 메서드 (권장명) | Given | When | Then | FR |
|-------|---------------|-----------------|-------|------|------|-----|
| TC-TREND-01 | `TrendServiceTest` | `loadTrendData_returnsSeriesByDate` | classpath CSV 존재 | `loadTrendSeries()` | 날짜별 건수 ≥1, 합계=행 수 | FR-17 |
| TC-TREND-02 | `TrendServiceTest` | `loadTrendData_missingFile_returnsEmptyWithMessage` | 파일 없음/빈 리소스 | `loadTrendSeries()` | empty + message 키 존재 | FR-17 |
| TC-KEYWORD-01 | `KeywordFileRepositoryTest` | `saveAndLoad_roundTrip` | 키워드 1건 추가 | save → load | 동일 목록 | FR-18 |
| TC-KEYWORD-02 | `KeywordConfigServiceTest` | `addKeyword_persistsAfterSimulatedRestart` | @TempDir 파일 | add → new Service(load) | 키워드 유지 | FR-18 |
| TC-KEYWORD-03 | `KeywordConfigServiceTest` | `removeKeyword_excludedFromClassification` | 키워드 삭제 | analyze/filter | 해당 키워드 미매칭 | FR-18 |
| TC-REGRESS-01 | *(기존)* | `mvn test` 전체 | REFACTORING 완료 상태 | `mvn test` | 0 failures, jacoco ≥90% | FR-19~21 |

> NF-03에서 TC-TREND/KEYWORD는 **RED→GREEN** 순으로: 먼저 FAIL 확인 후 NF-04에서 구현해 PASS.

```bash
mvn test -Dtest=TrendServiceTest
mvn test -Dtest=KeywordFileRepositoryTest,KeywordConfigServiceTest
```

**Git (시나리오 3 종료 — 필수)**

```bash
git add src/test/java/
git commit -m "FEATURE step3: add Trend and Keyword File DB tests (FR-17, FR-18)"
git push origin New_Feature
```

---

### NF-04 — 구현 (시나리오 4)

#### NF-04a — FR-17 Trend

1. `TrendService`: classpath에서 `test_feedback_trend.csv` 로드, `date` 파싱·집계.
2. `FeedbackService.prepareIndex` 또는 전용 endpoint에서 trend 모델 속성 주입.
3. `index.html`: Trend 섹션·차트 렌더 (`trendLabels`, `trendCounts` 등).
4. `mvn test -Dtest=TrendServiceTest` → **PASS**.
5. `mvn test` 전체 → **0 failures**.

#### NF-04b — FR-18 File DB

1. `KeywordFileRepository`: JSON read/write, 디렉터리 자동 생성.
2. `KeywordConfigService`: CRUD + `Constants` 시드.
3. `SentimentClassifier` / `TextAnalyzer` / `Filters`가 File DB 키워드 사용 (감정 규칙 **단일화** 유지).
4. UI: 키워드 관리 폼(감정별·카테고리별) 또는 REST + 간단 페이지.
5. `mvn test -Dtest=Keyword*` → **PASS**; **TC-NEUTRAL-01/02** 포함 전체 PASS.
6. (수동) 키워드 추가 → 프로세스 재기동 → 동일 키워드로 분류 확인.

```bash
mvn test
mvn jacoco:report
```

**Git (시나리오 4 종료 — 필수, 04a·04b 모두 완료 후 1회)**

```bash
git add src/main/java/ src/main/resources/templates/ src/main/resources/
git commit -m "FEATURE step4: Trend visualization and File DB keywords (FR-17, FR-18)"
git push origin New_Feature
```

---

### NF-05 — 리포트 (시나리오 5)

| 파일 | 필수 섹션 |
|------|-----------|
| `report/04_New_Feature_report.md` | FR-17/18 AC 체크리스트, Feature TC 결과, jacoco %, Before/After 구조, 수동 검증(재기동) |

**Git (시나리오 5 종료 — 필수)**

```bash
git add report/04_New_Feature_report.md
git commit -m "FEATURE step5: report/04_New_Feature_report.md"
git push origin New_Feature
```

---

### NF-06 — PR → A-01 (시나리오 6)

> 1~5단계에서 이미 **push 완료** 상태여야 한다. 본 단계는 **PR 생성**만 수행한다.

```bash
git status   # working tree clean 권장
gh pr create --base A-01 --head New_Feature \
  --title "New_Feature: Trend visualization and File DB keywords (FR-17~18)" \
  --body "$(cat <<'EOF'
## Summary
- FR-17: test_feedback_trend.csv 기반 Trend 차트 UI
- FR-18: File DB 키워드 CRUD 및 재기동 후 영속
- FR-19~21: mvn test 0 failures, JaCoCo ≥90% 유지

## Test plan
- [ ] TC-TREND-01/02 PASS
- [ ] TC-KEYWORD-01~03 PASS
- [ ] TC-NEUTRAL-01/02 및 Web 테스트 회귀 PASS
- [ ] 키워드 변경 후 재기동 유지 수동 확인

EOF
)"
```

---

## F — Format

### DoD (`docs/02_work_scenario.md` §8)

- [ ] **FR-17**: `test_feedback_trend.csv` in resources, Trend UI 표시, 없을 때 안내
- [ ] **FR-18**: 키워드 CRUD, `data/keywords.json`(또는 설정 경로) 영속, 재기동 후 유지
- [ ] **FR-19~21**: `mvn test` 0 failures; JaCoCo line ≥ **90%**
- [ ] 기존 HTTP 5 path 동작 유지
- [ ] `docs/10_feature_schema.md`, `report/04_New_Feature_report.md` 작성
- [ ] 시나리오 **1~5단계** 각각 **commit + push** 완료 (GitHub에 5개 커밋 이상)
- [ ] PR **New_Feature → A-01**

### 금지

- 시나리오 여러 단계를 **한 commit/push로 묶기**
- 단계 완료 후 **push 생략** (로컬 commit만 하고 GitHub 미반영)
- `src/test/**` 기존 TEST_PLAN TC 삭제·@Disabled
- `target/`, `*.class` 커밋
- GREEN/REFACTOR 단계 **대규모 재리팩터** (본 단계는 기능 추가만)
- 감정 로직 `TextAnalyzer` vs `Filters` **이중 구현** (File DB도 단일 Classifier 경유)
- 기존 5 API path **breaking change**

### 완료 보고 (Agent → 사용자)

1. NF 단계별 실행 요약 (시나리오 1~5 각 **commit 해시** + **push 완료** 여부)  
2. 최종 `mvn test` / jacoco %  
3. Feature TC PASS 표  
4. PR URL  

---

## ★ PROMPT — Agent에 붙여넣기 (마스터)

아래 블록 **전체**를 Cursor PROMPT에 복사한다. 단계만 실행할 때는 §T 해당 절 + 본 블록 P/C/F를 사용한다.

```
[PCTF 06 — New_Feature FR-17~18 (시나리오 §8)]

P (Purpose)
- FR-17: test_feedback_trend.csv → Trend 차트 UI
- FR-18: File DB 키워드 CRUD + 재기동 후 영속
- FR-19~21: 기존 TEST_PLAN·Web TC 회귀 PASS, JaCoCo ≥90%
- 시나리오 1~5단계마다 GitHub에 commit + push 필수. 6단계 PR: New_Feature → A-01

C (Context)
- @docs/02_work_scenario.md §8 @docs/00_prd.md FR-17~18
- @pctf/06_New_Feature_PCTF_prompt.md @tdd_rules.yaml tracks.FEATURE
- @FeedbackService.java @SentimentClassifier.java @Filters.java @TextAnalyzer.java
- @index.html @FeedbackControllerWebTest.java @docs/07_RED_test_plan.md
- 브랜치: New_Feature (또는 new_feature). 선행: REFACTORING 완료, mvn test 0 failures

T (Task) — 시나리오 6단계 순서 (각 단계 종료 시 commit + push 필수)
공통: git add → git commit → git push origin <브랜치>  (NF-06 제외)

NF-00: mvn test → 0 failures; mvn jacoco:report → ≥90%  (commit/push 생략)

NF-01 (시나리오 1): git checkout -b New_Feature A-01
  git commit --allow-empty -m "FEATURE step1: start New_Feature branch from A-01"
  git push -u origin New_Feature

NF-02 (시나리오 2): test_feedback_trend.csv + docs/10_feature_schema.md
  git commit -m "FEATURE step2: add trend CSV sample and feature schema (FR-17, FR-18)"
  git push origin New_Feature

NF-03 (시나리오 3): TrendServiceTest, Keyword*Test (FAIL 허용 → NF-04에서 PASS)
  git commit -m "FEATURE step3: add Trend and Keyword File DB tests (FR-17, FR-18)"
  git push origin New_Feature

NF-04 (시나리오 4): 04a Trend + 04b File DB 구현 → mvn test 0 failures
  git commit -m "FEATURE step4: Trend visualization and File DB keywords (FR-17, FR-18)"
  git push origin New_Feature

NF-05 (시나리오 5): report/04_New_Feature_report.md
  git commit -m "FEATURE step5: report/04_New_Feature_report.md"
  git push origin New_Feature

NF-06 (시나리오 6): gh pr create --base A-01 --head New_Feature  (추가 commit 없음)

F (Format)
- 기존 HTTP 5 path·CSV text 컬럼 불변. TEST_PLAN TC 유지.
- 시나리오 1~5: 단계마다 commit+push. 여러 단계를 한 commit에 묶지 않음.
- 커밋: src/, docs/, report만. target/·*.class 제외.
- 완료 보고: step1~5 commit 해시, push 완료, Feature TC, mvn test, jacoco %, PR URL.
```

---

## ★ PROMPT — 단계별 붙여넣기 (NF-04a 예시)

```
[PCTF 06 — NF-04a Trend FR-17 (시나리오 4 일부)]

@pctf/06_New_Feature_PCTF_prompt.md §NF-04a
@src/main/resources/test_feedback_trend.csv @index.html @FeedbackService.java

1. TrendService: date별 건수 집계, CSV 없을 때 empty+message
2. index.html Trend 섹션(차트 또는 stats 시계열)
3. mvn test -Dtest=TrendServiceTest → PASS
4. 이어서 NF-04b File DB 구현 (04b 완료 전 commit/push 금지)
5. 04a+04b 완료, mvn test 0 failures 후:
   git commit -m "FEATURE step4: Trend visualization and File DB keywords (FR-17, FR-18)"
   git push origin New_Feature
```

---

## ★ PROMPT — 단계별 붙여넣기 (NF-04b 예시)

```
[PCTF 06 — NF-04b File DB FR-18]

@pctf/06_New_Feature_PCTF_prompt.md §NF-04b
@SentimentClassifier.java @config/Constants.java @Filters.java

1. KeywordFileRepository + data/keywords.json 영속
2. KeywordConfigService CRUD, 기동 시 Constants 시드
3. 분류 로직이 File DB 단일 소스 사용 (TC-NEUTRAL 회귀 주의)
4. mvn test -Dtest=KeywordFileRepositoryTest,KeywordConfigServiceTest → PASS
5. mvn test → 0 failures; jacoco ≥90%
6. 04a와 합쳐 시나리오 4 종료 시 step4 commit + push (위 NF-04 Git 블록 참고)
```

---

## 범위 외 (본 PCTF에서 하지 않음)

| 항목 | 이유 |
|------|------|
| `report/06_retrospective.md` | 시나리오 9 (회고) — `05`는 QA REVIEW |
| A-01 → main 릴리스 PR | 시나리오 9.2 |
| `.gitignore`에 `target/` 추가 | 별도 housekeeping (권장만 문서화) |

---

## 교차 참조

| 문서 | 경로 |
|------|------|
| 시나리오 §8 | `docs/02_work_scenario.md` |
| PRD FR-17~18 | `docs/00_prd.md` §5.4 |
| To-Do | `docs/06_todo_list.md` (New_Feature 행) |
| TDD FEATURE | `tdd_rules.yaml` → `tracks.FEATURE` |
| REFACTORING 선행 | `pctf/05_REFACTORING_step6_controller_SRP_PCTF_prompt.md` |
| TEST_PLAN 회귀 | `docs/07_RED_test_plan.md` |
| 작업규칙 7단계 | `작업규칙.TXT` |
