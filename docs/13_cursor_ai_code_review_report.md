# 코드 리뷰 및 개선 보고서

| 항목 | 내용 |
|------|------|
| 문서 ID | DOC-REVIEW-001 |
| 작성 도구 | **Cursor AI** (Agent + PCTF 프롬프트 기반 분석) |
| 프로젝트 | FeedBackAnalyzer_01 — 고객 피드백 분석 시스템 |
| 검토 기준선 (Before) | Git `6e88371` (GREEN 완료, REFACTORING 전) |
| 검토 결과 (After) | `A-01` @ `ad5678b` (REFACTORING + New_Feature + QA 문서) |
| 작성일 | 2026-05-22 |
| 연관 문서 | `docs/05_code_smell.md`, `docs/12_QA_review_outline.md`, `report/05_REVIEW_refactoring_report.md` |

> 본 문서는 Cursor AI가 `@Codebase`·Mom Test·PRD·단계별 PCTF를 분석한 **REVIEW 검토 결과**를 팀 보고용 Markdown으로 통합한 것이다. (본 프로젝트는 Java/Spring Boot만 사용 — C/C++ 소스 없음)

---

## 1. 분석 배경 (프로젝트·목적)

### 1.1 프로젝트 개요

| 항목 | 내용 |
|------|------|
| 프로젝트명 | 리팩토링 챌린지: **Feedback Analyzer** |
| 목적 | 레거시 Spring Boot 앱의 코드 스멜 식별 → TDD·리팩토링·신규 기능 → QA 검증 |
| 기술 스택 | Java 17, Spring Boot 3.5.3, Thymeleaf, OpenCSV, Maven |
| 실행 | `mvn spring-boot:run` → `http://localhost:8080` |
| Git 전략 | `main` ← `A-01` ← 단계 브랜치 (`SPEC`, `RED`, `GREEN`, `REFACTORING`, `new_feature`, `QA`) |

### 1.2 분석 목적 (Cursor AI 수행 범위)

1. **초기 코드베이스**의 God Controller·감정 규칙 이중화·테스트 부재를 PRD·Mom Test와 매핑  
2. **Dual-Track TDD**: RED(실패 TC) → GREEN(최소 수정) → REFACTORING(계약 불변 구조 개선)  
3. **8단계 REVIEW**: 리팩토링 전후 Before/After 코드 검증 및 회귀(테스트·JaCoCo·HTTP 계약)  
4. **개선 조치 추적**: 피드백 → 수정 → 결과를 단일 표로 정리  

### 1.3 분석 방법

| 방법 | 설명 |
|------|------|
| `@Codebase` 정적 분석 | 클래스·패키지·의존 관계, `docs/01_analysis.md` |
| Mom Test | `docs/04_mom_test.md`, `docs/05_code_smell.md` 3건 |
| PCTF 프롬프트 | RED/GREEN/REFACTORING/New_Feature/QA REVIEW 단계별 산출물 |
| Git diff | `git show 6e88371` vs `A-01` HEAD |
| 실행 검증 | `mvn clean test`, JaCoCo, `FeedbackControllerWebTest` |

---

## 2. Cursor AI 분석 요약 (항목별 표)

### 2.1 코드 품질·아키텍처

| 분석 항목 | Before (초기·6e88371) | Cursor AI 판단 | After (A-01) |
|-----------|----------------------|----------------|--------------|
| 패키지 구조 | 단일 `com.example.demo` | 레이어 미분리, 변경 영향 큼 | `controller` / `service` / `model` / `config` |
| Controller 책임 | HTTP+CSV+분석+다운로드 혼재 | **God Object** (사례 2) | HTTP만, `FeedbackService` 위임 |
| 감정 판별 | `TextAnalyzer` + `Filters.S_KEYWORDS` 이중 | **규칙 불일치** (사례 1, FR-09) | `SentimentClassifier` 단일 규칙 |
| 상수·키워드 | `Constants` 중복·`S_KEYWORDS` 별도 | Shotgun Surgery (사례 3) | `Sentiment` enum, dedupe, File DB |
| 네이밍 | `fil`, `sent`, `kw`, `fil_data` | 도메인 의미 불명 | `filterFeedbacks`, `analyzeSentiment` 등 |
| 상태 관리 | `Session` static, `fil_data` | HTTP 세션 아님, 테스트 어려움 | 잔존(향후 과제) |
| 테스트 | `contextLoads` 수준 | 커버리지·회귀 불가 | **41** tests, JaCoCo **90.4%** |

### 2.2 기능·계약

| 분석 항목 | Before | Cursor AI 판단 | After |
|-----------|--------|----------------|-------|
| HTTP API | 5 path | 계약 유지 필요 | 5 path **유지** + `/keywords/*` |
| CSV 컬럼 | `text` | 불변 | **유지** |
| 중립 필터 | 분석·필터 불일치 | TC-NEUTRAL RED 실패 | **PASS** |
| Trend (FR-17) | 없음 | 신규 요구 | `TrendService` + CSV + UI |
| File DB (FR-18) | 하드코딩만 | 운영 비용 높음 | `KeywordConfigService` + JSON 파일 |

### 2.3 프로세스·문서

| 분석 항목 | Cursor AI 산출 |
|-----------|----------------|
| 요구 추적 | `docs/00_prd.md`, `docs/02_work_scenario.md` |
| 테스트 명세 | `docs/07_RED_test_plan.md`, `docs/08`·`11` 결과 |
| 리팩토링 검증 | `report/05_REVIEW_refactoring_report.md`, `docs/12_QA_review_outline.md` |
| 회고 | `report/06_retrospective.md` |
| Agent 이력 | `prompting/00`~`05`, `User_prompt.md`, `GIT_prompt.md` |

### 2.4 종합 점수 (정성)

| 영역 | Before | After | 비고 |
|------|--------|-------|------|
| 가독성 | △ | ◎ | 도메인 네이밍·패키지 |
| SRP/OCP | × | ○ | Service·Classifier 분리 |
| 테스트 가능성 | × | ◎ | 41 TC, MockMvc |
| 운영 확장성 | △ | ○ | File DB, Trend |
| 릴리스 준비도 | × | ○ | `A-01` → `main` PR 본문 준비 |

---

## 3. 개선/처리 내역

| 피드백사항 | 수정 조치 | 결과 |
|------------|-----------|------|
| **P0-1** 감정 분석·필터 규칙 불일치 (`괜찮` 등 중립) | GREEN: `Filters`가 `Constants`와 동일 규칙 사용 → 이후 `SentimentClassifier` 단일화 | TC-NEUTRAL-01/02 **PASS** |
| **P0-2** `S_KEYWORDS`와 `Constants` 이중 정의 | REFACTORING: `S_KEYWORDS` 제거, `SentimentClassifier` + `config.Constants` | 분석·필터 감정 **일치** |
| **P0-3** God Controller (CSV·분석·다운로드 혼재) | `FeedbackService` 추출, `controller.FeedbackController` HTTP 전용 | Controller **~67 LOC**, SRP 충족 |
| **P0-4** `fil`/`sent`/`kw`/`fil_data` 비도메인 네이밍 | FR-12 rename + private 메서드 분리 (`applySentimentFilter` 등) | 리뷰·유지보수성 **향상** |
| **P0-5** `Constants` 키워드 리스트 중복 | `dedupe()`, `Sentiment` enum, `FILTER_ALL` | 카테고리 시드 **1곳** |
| **P0-6** JUnit·커버리지 부재 | RED: TEST_PLAN, JUnit 34+, JaCoCo 플러그인 | line **≥90%** 달성 |
| **P1-1** 테스트 34→회귀 부족 (New_Feature) | Trend·Keyword TC 7건 추가 | **41** tests, 0 failures |
| **P1-2** 감정 키워드 하드코딩만 (운영) | `KeywordFileRepository`, `/keywords/add\|remove` | FR-18 **PASS**, 재기동 유지 |
| **P1-3** Trend 시각화 없음 | `test_feedback_trend.csv`, `TrendService`, Chart UI | FR-17 **PASS** |
| **P2-1** 리팩토링 검증 문서 없음 | QA: `docs/12`, `report/05` Before/After | FR-12~16 **검증 완료** |
| **P2-2** `mvn test`만 시 stale class 충돌 | 문서화: **`mvn clean test`** 권장 | Agent·CI 재현성 **개선** |
| **P2-3** `Session` static (HTTP 세션 아님) | — (범위 외) | 백로그 등록 |
| **P2-4** 업로드 `C:\tmp` 하드코딩 | — (부분 잔존 Service) | NFR-03 follow-up |
| **P2-5** multiline·Logger UI (FR-10/11) | — (선택) | GREEN 별도 또는 후속 |

---

## 4. 전후 코드 비교 (Java)

> **Before** = `6e88371` · **After** = `A-01` HEAD (현재 소스)

### 4.1 FR-12 — 필터 메서드 네이밍 (`Filters`)

**Before**

```java
public List<Feedback> fil(List<Feedback> dataList, String sFilter, String kFilter) {
    List<Feedback> tmpFiltered = new ArrayList<>();
    if (!"전체".equals(sFilter)) {
        for (Feedback item : dataList) {
            String currentSentiment = resolveSentimentLikeAnalyzer(item.getText());
            if (currentSentiment.equals(sFilter)) {
                tmpFiltered.add(item);
            }
        }
    } else {
        tmpFiltered = new ArrayList<>(dataList);
    }
    // ... keyword filter ...
    return finalFiltered;
}
```

**After**

```java
public List<Feedback> filterFeedbacks(List<Feedback> dataList,
                                    String sentimentFilter,
                                    String keywordFilter) {
    List<Feedback> afterSentiment = applySentimentFilter(dataList, sentimentFilter);
    List<Feedback> finalFiltered = applyKeywordFilter(afterSentiment, keywordFilter);
    return finalFiltered;
}
```

---

### 4.2 FR-13 — 감정 키워드 중복·이중 Map

**Before** (`Filters.java` — `S_KEYWORDS` 별도 유지)

```java
private static final Map<String, List<String>> S_KEYWORDS = new HashMap<>();
static {
    S_KEYWORDS.put("긍정", Arrays.asList("좋아요", "만족", /* ... */));
    S_KEYWORDS.put("부정", Arrays.asList("나쁘", "불만", /* ... */));
    S_KEYWORDS.put("중립", Arrays.asList("괜찮", "보통", /* ... */));
}
```

**After** (`config/Sentiment.java` + `SentimentClassifier` — `S_KEYWORDS` 제거)

```java
public enum Sentiment {
    POSITIVE("긍정"),
    NEGATIVE("부정"),
    NEUTRAL("중립");
    // getLabel()
}
```

```java
// Constants.java — dedupe 적용
SENTIMENT_KEYWORDS.put(Sentiment.POSITIVE.getLabel(), dedupe(
    "좋아요", "만족", "감사", "최고", /* ... */
));
```

---

### 4.3 FR-14 — 감정 판별 SRP (`SentimentClassifier`)

**Before** (`TextAnalyzer.sent` — 필터와 별도 복제)

```java
for (Feedback f : feedbacks) {
    String txt = f.getText().toLowerCase();
    String s = "중립";
    if (Constants.SENTIMENT_KEYWORDS.get("긍정").stream().anyMatch(k -> txt.contains(k))) {
        s = "긍정";
    } else if (Constants.SENTIMENT_KEYWORDS.get("부정").stream().anyMatch(k -> txt.contains(k))) {
        s = "부정";
    }
    res.put(s, res.get(s) + 1);
}
```

**After** (단일 클래스 + 위임)

```java
@Service
public class SentimentClassifier {
    public String classify(String text) {
        String lowerText = text.toLowerCase();
        if (containsAny(lowerText, keywordConfig.getSentimentKeywords()
                .getOrDefault(Sentiment.POSITIVE.getLabel(), List.of()))) {
            return Sentiment.POSITIVE.getLabel();
        }
        if (containsAny(lowerText, keywordConfig.getSentimentKeywords()
                .getOrDefault(Sentiment.NEGATIVE.getLabel(), List.of()))) {
            return Sentiment.NEGATIVE.getLabel();
        }
        return Sentiment.NEUTRAL.getLabel();
    }
}
```

```java
// TextAnalyzer — 위임
String label = sentimentClassifier.classify(feedback.getText());
counts.put(label, counts.get(label) + 1);
```

---

### 4.4 FR-15~16 — Controller SRP·패키지

**Before** (`FeedbackController` — 비즈니스·CSV 혼재)

```java
@Controller
public class FeedbackController {
    @Autowired private TextAnalyzer textAnalyzer;
    @Autowired private Filters filters;
    private List<Feedback> fil_data = new ArrayList<>();

    @PostMapping("/analyze")
    public String analyze(@RequestParam("text") String text, Model model) {
        List<Feedback> feedbacks = Session.getCurrentFeedbacks();
        if (text != null && !text.trim().isEmpty()) {
            feedbacks.add(new Feedback(text.trim()));
        }
        Map<String, Integer> sentimentResults = textAnalyzer.sent(feedbacks);
        Map<String, Integer> keywordResults = textAnalyzer.kw(feedbacks);
        model.addAttribute("sentimentResults", sentimentResults);
        return "index";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        File tmpFile = new File("C:\\tmp\\" + file.getOriginalFilename());
        CSVReader csvReader = new CSVReader(new FileReader(tmpFile));
        // ...
    }
}
```

**After** (`controller.FeedbackController` + `FeedbackService`)

```java
@Controller
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/analyze")
    public String analyze(@RequestParam("text") String text, Model model) {
        feedbackService.analyzeFeedback(text, model);
        return "index";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        feedbackService.uploadCsv(file, model);
        return "index";
    }
}
```

```java
// service/FeedbackService.java — 오케스트레이션
private List<Feedback> filteredFeedbacksForExport = new ArrayList<>();

public void analyzeFeedback(String text, Model model) {
    // Session, TextAnalyzer.analyzeSentiment, Model 속성
}

public void filterFeedbacks(String sentiment, String keyword, Model model) {
    // filters.filterFeedbacks → filteredFeedbacksForExport
}
```

---

### 4.5 FR-17~18 — 신규 기능 (After만, Before 없음)

**After** — Trend + File DB (Cursor AI New_Feature PCTF 구현)

```java
// TrendService — CSV 기반 시계열
public TrendSeries loadTrendSeries() { /* test_feedback_trend.csv */ }

// KeywordConfigService — File DB
public void addSentimentKeyword(String sentiment, String keyword) { /* data/keywords.json */ }
```

```java
// Controller — 신규 엔드포인트 (계약 확장)
@PostMapping("/keywords/add")
public String addKeyword(@RequestParam("sentiment") String sentiment,
                         @RequestParam("keyword") String keyword, Model model) {
    feedbackService.addSentimentKeyword(sentiment, keyword, model);
    return "index";
}
```

---

## 5. 효과 및 향후 과제

### 5.1 정량·정성 효과

| 지표 | Before | After | 효과 |
|------|--------|-------|------|
| 단위·통합 테스트 | ~1 (contextLoads) | **41**, 0 failures | 회귀 안전망 확보 |
| JaCoCo line (`com.example.demo`) | — | **90.4%** | PRD FR-20 충족 |
| 감정 규칙 출처 | 3곳 | **1곳** (`SentimentClassifier`) | Mom Test Q5~7 신뢰도 ↑ |
| Controller LOC (HTTP) | 200+ (혼재) | **~67** | 변경 범위 예측 가능 |
| 패키지 depth | 1 | **4 레이어** | IDE 탐색·온보딩 ↑ |
| HTTP 계약 | 5 | 5 + keywords | 하위 호환 + 확장 |

### 5.2 Cursor AI 활용 효과

| 장점 | 한계 |
|------|------|
| PCTF·작업규칙으로 산출물 경로·커밋 메시지 일관 | `gh auth` 없으면 PR 자동화 불가 |
| Before SHA(`6e88371`) 고정으로 REVIEW 재현 | 터미널 `git show` 한글 깨짐 가능 |
| RED→GREEN→REFACTOR 단계 분리 제안 | 범위 넘어선 변경은 수동 검토 필요 |
| `mvn clean test` 이슈(stale class) 조기 발견 | 최종 검증은 **항상 로컬 실행** 필수 |

### 5.3 향후 과제 (백로그)

| 우선순위 | 과제 | 연관 FR/NFR | 비고 |
|----------|------|-------------|------|
| P0 | `A-01` → `main` 릴리스 PR 머지 | PRD §9 | `docs/pr3_body_release_body.md` |
| P1 | `Session` static → HTTP Session | — | 멀티 사용자·테스트 격리 |
| P1 | CSV 업로드 `C:\tmp` 제거 | FR-03, NFR-03 | `MultipartFile` 스트림 |
| P2 | FR-10 multiline, FR-11 Logger UI | GREEN 잔여 | UX 개선 |
| P2 | `Filters` 내 `System.out.println` → Logger | — | 운영 로그 정리 |
| P3 | E2E 시나리오 A~C 자동화 | FR-01~08 | Selenium/Playwright 검토 |
| P3 | 팀 발표 | `report/06_retrospective.md` | 4문항 슬라이드화 |

### 5.4 결론

Cursor AI 분석은 **코드 스멜 3건(Mom Test)** 을 PRD·TC·Git Before/After와 연결해, Dual-Track TDD와 REFACTORING을 **계약 불변** 상태로 완료하도록 안내했다. 현재 `A-01`은 기능·구조·테스트·문서 관점에서 **main 릴리스 가능 수준**이며, 잔여 과제는 주로 **인프라·UX·세션** 영역에 집중된다.

---

## 부록 — 참고 문서·경로

| 구분 | 경로 |
|------|------|
| 코드 스멜 | `docs/05_code_smell.md` |
| QA 개요 | `docs/12_QA_review_outline.md` |
| 상세 Before/After | `report/05_REVIEW_refactoring_report.md` |
| 회고 | `report/06_retrospective.md` |
| 릴리스 PR | `docs/pr3_body_update.md`, `docs/pr3_body_release_body.md` |
| PCTF QA | `pctf/07_QA_REVIEW_PCTF_prompt.md` |
