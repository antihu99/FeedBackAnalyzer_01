# QA REVIEW — 리팩토링 전후 비교 보고서

| 항목 | 값 |
|------|-----|
| 단계 | 작업규칙.TXT §8, PCTF `07_QA_REVIEW` |
| 브랜치 | `QA` |
| Before | `6e88371` (GREEN, REFACTORING 전) |
| After | `QA` HEAD — REFACTORING + New_Feature 머지 |
| 작성일 | 2026-05-22 |
| 참조 | `report/03_REFACTORING_report.md`, `docs/12_QA_review_outline.md` |

---

## 1. 요약

REFACTORING(FR-12~16)은 GREEN 기준선 `6e88371` 대비 **네이밍·상수 정리**, **감정 판별 단일화(SRP)**, **Controller/Service 분리·패키지 구조화**를 달성했다. 외부 HTTP 계약 5 path와 CSV `text` 컬럼은 유지되었고, `mvn clean test` **41건 0 실패**, JaCoCo line **90.4%**로 회귀 기준을 충족한다.

| FR | 개선 한 줄 |
|----|-----------|
| FR-12 | 축약 메서드명 → 도메인 의미 메서드명 |
| FR-13 | `S_KEYWORDS`·중복 리스트 제거, `Sentiment` enum |
| FR-14 | `SentimentClassifier` 단일 규칙, Analyzer/Filter 위임 |
| FR-15 | Controller HTTP 전용, 비즈니스 → `FeedbackService` |
| FR-16 | `controller` / `service` / `model` / `config` 패키지 |

---

## 2. 비교 기준

| 구분 | Git | 패키지·파일 특징 |
|------|-----|------------------|
| **Before** | `6e88371` | 모든 클래스 `com.example.demo` 루트 |
| **After** | `QA` HEAD | 레이어 패키지 + New_Feature 엔드포인트(`/keywords/*`, Trend) |

```text
Before (6e88371)                    After (QA)
com.example.demo                    com.example.demo
├── FeedbackController.java         ├── controller/FeedbackController.java
├── Filters.java (fil, S_KEYWORDS)  ├── service/FeedbackService.java
├── TextAnalyzer.java (sent, kw)    ├── SentimentClassifier.java
├── Constants.java                  ├── config/Constants.java, Sentiment.java
└── Feedback.java                   └── model/Feedback.java
```

---

## 3. FR-12~13 — 네이밍·상수

### 3.1 메서드·필드 네이밍 (FR-12)

**Before** (`6e88371`, `Filters.java`)

```java
public List<Feedback> fil(List<Feedback> dataList, String sFilter, String kFilter) {
    // ...
    String currentSentiment = resolveSentimentLikeAnalyzer(item.getText());
    // ...
}
```

**After** (`QA`, `Filters.java`)

```java
public List<Feedback> filterFeedbacks(List<Feedback> dataList,
                                    String sentimentFilter,
                                    String keywordFilter) {
    List<Feedback> afterSentiment = applySentimentFilter(dataList, sentimentFilter);
    List<Feedback> finalFiltered = applyKeywordFilter(afterSentiment, keywordFilter);
    return finalFiltered;
}
```

**Before** (`6e88371`, `TextAnalyzer.java`)

```java
public Map<String, Integer> sent(List<Feedback> feedbacks) { /* ... */ }
public Map<String, Integer> kw(List<Feedback> feedbacks) { /* ... */ }
```

**After** (`QA`, `TextAnalyzer.java`)

```java
public Map<String, Integer> analyzeSentiment(List<Feedback> feedbacks) { /* ... */ }
public Map<String, Integer> analyzeKeywords(List<Feedback> feedbacks) { /* ... */ }
```

**Before** (`6e88371`, `FeedbackController.java`)

```java
private List<Feedback> fil_data = new ArrayList<>();
// filter/download에서 fil_data 사용
```

**After** (`QA`, `FeedbackService.java`)

```java
private List<Feedback> filteredFeedbacksForExport = new ArrayList<>();
```

**효과**: Mom Test Q5~Q7(감정·필터 일치) 추적 시 메서드명만으로 의도 파악 가능. API URL·폼 파라미터명은 변경 없음.

---

### 3.2 상수·enum (FR-13)

**Before** (`6e88371`, `Filters.java` — `Constants`와 별도 `S_KEYWORDS`)

```java
private static final Map<String, List<String>> S_KEYWORDS = new HashMap<>();
static {
    S_KEYWORDS.put("긍정", Arrays.asList("좋아요", "만족", /* ... */));
    S_KEYWORDS.put("부정", Arrays.asList("나쁘", "불만", /* ... */));
    S_KEYWORDS.put("중립", Arrays.asList("괜찮", "보통", /* ... */));
}
// resolveSentimentLikeAnalyzer: Constants 긍정/부정만 검사, 중립 키워드는 S_KEYWORDS와 불일치 가능
```

**Before** (`6e88371`, `Constants.java` — 감정 키워드 중복 삽입)

```java
SENTIMENT_KEYWORDS.put("긍정", Arrays.asList(
    "좋아요", "만족", /* ... */,
    "좋아요", "만족", /* 동일 항목 반복 */
));
```

**After** (`QA`, `config/Sentiment.java`)

```java
public enum Sentiment {
    POSITIVE("긍정"),
    NEGATIVE("부정"),
    NEUTRAL("중립");
    // getLabel()
}
```

**After** (`QA`, `config/Constants.java`)

```java
public static final String FILTER_ALL = "전체";

SENTIMENT_KEYWORDS.put(Sentiment.POSITIVE.getLabel(), dedupe(
    "좋아요", "만족", "감사", "최고", /* ... */
));
// dedupe(LinkedHashSet)로 중복 제거
```

**효과**: 감정 라벨 단일 출처(`Sentiment`), 키워드 시드 1곳(`Constants`) — Shotgun Surgery 완화 (`docs/05_code_smell.md` 사례 3). `Filters`의 `S_KEYWORDS` 제거로 FR-09 이중 규칙 원인 제거.

---

## 4. FR-14 — 감정 SRP

### 4.1 Before: 이중 감정 규칙

**Before** (`6e88371`, `TextAnalyzer.sent`)

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

**Before** (`6e88371`, `Filters` — 필터 시 별도 로직)

```java
private String resolveSentimentLikeAnalyzer(String text) {
    String txt = text.toLowerCase();
    if (Constants.SENTIMENT_KEYWORDS.get("긍정").stream().anyMatch(k -> txt.contains(k))) {
        return "긍정";
    }
    if (Constants.SENTIMENT_KEYWORDS.get("부정").stream().anyMatch(k -> txt.contains(k))) {
        return "부정";
    }
    return "중립";
}
```

분석 집계와 필터가 **동일 파일·동일 알고리즘을 복제** — `S_KEYWORDS`와 불일치 시 TC-NEUTRAL 실패(RED P0-1).

---

### 4.2 After: SentimentClassifier 단일화

**After** (`QA`, `SentimentClassifier.java`)

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

**After** (`QA`, `TextAnalyzer` — 위임)

```java
for (Feedback feedback : feedbacks) {
    String label = sentimentClassifier.classify(feedback.getText());
    counts.put(label, counts.get(label) + 1);
}
```

**After** (`QA`, `Filters` — 위임)

```java
if (sentimentClassifier.classify(item.getText()).equals(sentimentFilter)) {
    matched.add(item);
}
```

**효과**: FR-09 단일 규칙 충족. OCP 관점에서 감정 알고리즘 변경 시 `SentimentClassifier`(+ File DB `KeywordConfigService`)만 수정.

---

## 5. FR-15~16 — Controller SRP·패키지

### 5.1 Before: God Controller

**Before** (`6e88371`, `FeedbackController.java` 발췌)

```java
@Controller
public class FeedbackController {
    @Autowired private TextAnalyzer textAnalyzer;
    @Autowired private Filters filters;
    // FileHandler, UIComponents, Logger ...
    private List<Feedback> fil_data = new ArrayList<>();

    @PostMapping("/analyze")
    public String analyze(@RequestParam("text") String text, Model model) {
        List<Feedback> feedbacks = Session.getCurrentFeedbacks();
        if (text != null && !text.trim().isEmpty()) {
            feedbacks.add(new Feedback(text.trim()));
        }
        // logger, Session.updateInternalData
        Map<String, Integer> sentimentResults = textAnalyzer.sent(feedbacks);
        Map<String, Integer> keywordResults = textAnalyzer.kw(feedbacks);
        model.addAttribute("sentimentResults", sentimentResults);
        // ...
        return "index";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        File tmpFile = new File("C:\\tmp\\" + file.getOriginalFilename());
        CSVReader csvReader = new CSVReader(new FileReader(tmpFile));
        // 파싱·세션·모델 속성 — Controller 내부
    }
}
```

HTTP 매핑 + 세션 + CSV + 분석 + 필터 + 다운로드가 **한 클래스**에 혼재 (`docs/05_code_smell.md` 사례 2).

---

### 5.2 After: Controller HTTP only + Service

**After** (`QA`, `controller/FeedbackController.java`)

```java
@Controller
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/")
    public String index(Model model) {
        feedbackService.prepareIndex(model);
        return "index";
    }

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

    @PostMapping("/filter")
    public String filter(@RequestParam("sentiment") String sentiment,
                         @RequestParam("keyword") String keyword,
                         Model model) {
        feedbackService.filterFeedbacks(sentiment, keyword, model);
        return "index";
    }

    @GetMapping("/download")
    public void downloadFile(HttpServletResponse response) throws IOException {
        feedbackService.writeFilteredCsv(response);
    }
}
```

**After** (`QA`, `service/FeedbackService.java` 발췌)

```java
@Service
public class FeedbackService {
    @Autowired private TextAnalyzer textAnalyzer;
    @Autowired private Filters filters;
    private List<Feedback> filteredFeedbacksForExport = new ArrayList<>();

    public void analyzeFeedback(String text, Model model) {
        // 세션·피드백 추가·analyzeSentiment/analyzeKeywords·Model 속성
    }

    public void uploadCsv(MultipartFile file, Model model) {
        // CSV 파싱·세션 갱신
    }

    public void filterFeedbacks(String sentiment, String keyword, Model model) {
        // filters.filterFeedbacks → filteredFeedbacksForExport
    }
}
```

**효과**: FR-15 SRP — Controller는 Spring MVC 경계만. FR-16 — `model.Feedback`, `config.Constants`로 의존 방향 명확화.

---

### 5.3 패키지 트리 (FR-16)

**After** (`QA`)

```text
src/main/java/com/example/demo/
├── DemoApplication.java
├── controller/FeedbackController.java
├── service/FeedbackService.java, TrendService.java, KeywordConfigService.java, ...
├── model/Feedback.java, TrendSeries.java
├── config/Constants.java, Sentiment.java
├── SentimentClassifier.java, TextAnalyzer.java, Filters.java, Session.java, ...
```

---

## 6. 계약·회귀 검증

### 6.1 HTTP 계약 (contract_immutable)

| Path | Method | Before | After | 확인 |
|------|--------|--------|-------|------|
| `/` | GET | ✓ | ✓ | `FeedbackControllerWebTest` |
| `/analyze` | POST | ✓ | ✓ | 동일 |
| `/upload` | POST | ✓ | ✓ | 동일 |
| `/filter` | POST | ✓ | ✓ | 동일 |
| `/download` | GET | ✓ | ✓ | 동일 |
| CSV 컬럼 | `text` | ✓ | ✓ | 업로드 테스트 |

New_Feature 추가 path (회귀 범위 외, 부록): `POST /keywords/add`, `POST /keywords/remove`.

---

### 6.2 테스트·JaCoCo (QA-00, 2026-05-22)

```text
mvn clean test
Tests run: 41, Failures: 0, Errors: 0

mvn jacoco:report
com.example.demo LINE: 90.4% (413 covered / 457 total)
```

| TC | 내용 | 결과 |
|----|------|------|
| TC-NEUTRAL-01 | 중립 문장 집계 | PASS (`TextAnalyzerTest`) |
| TC-NEUTRAL-02 | 중립 필터 | PASS (`FiltersTest`) |
| Web | MockMvc 5 path + keywords | PASS (`FeedbackControllerWebTest` 10) |

> 실행 시 `target/classes`에 구 `FeedbackController.class`가 남으면 `ConflictingBeanDefinitionException` 발생 — **`mvn clean test` 필수**.

---

## 7. 개선 효과·잔여 리스크

### 개선 효과

| 관점 | Before | After |
|------|--------|-------|
| Mom Test Q5~Q7 | 분석·필터 감정 불일치 위험 | `SentimentClassifier` 단일 규칙 |
| God Controller | CSV·상태·다운로드 혼재 | `FeedbackService` 분리 |
| 유지보수 | `fil`/`sent`/`S_KEYWORDS` 3중 | rename + enum + config 패키지 |
| 테스트 | 34 (REFACTORING 시점) | **41** (New_Feature 포함) |

### 잔여 리스크 (범위 외·선택 개선)

| 항목 | 상태 | 비고 |
|------|------|------|
| `Session` static | 미변경 | HTTP 세션 전환은 별도 PCTF |
| `Filters` 내 `System.out.println` | 잔존 | 로깅 정리 여지 |
| `C:\tmp` 업로드 경로 | Service 내 레거시 | FR-03 별도 |
| File DB (FR-18) | After에 반영 | REFACTORING 범위 밖, QA 부록 참조 |

---

## 8. (선택) New_Feature 요약

`A-01`/`QA` HEAD에는 FR-17~18이 추가되어 REFACTORING After 스냅샷에 포함된다.

| FR | 내용 | 주요 클래스 |
|----|------|-------------|
| FR-17 | `test_feedback_trend.csv` Trend UI | `TrendService`, `index.html` Chart |
| FR-18 | 감정 키워드 File DB | `KeywordFileRepository`, `KeywordConfigService`, `/keywords/add\|remove` |

상세: `report/04_New_Feature_report.md`, `docs/11_New_Feature_test_results.md`.

---

## 9. 결론·A-01 머지 권고

- **FR-12~16** 목표(네이밍, 상수, 감정 SRP, Controller/패키지 분리)는 Before `6e88371` 대비 After 코드에서 **확인 가능**하며, 회귀 테스트·JaCoCo 기준을 **충족**한다.
- **권고**: `QA` → `A-01` 머지 또는 PR 리뷰 후 `A-01` 유지. (이미 `A-01`에 동일 커밋이 있으면 QA 브랜치는 **문서 커밋만** 추가 push.)
- **9단계 회고**: `report/06_retrospective.md` — 본 보고서 범위 외.

---

## 부록 — Git 명령

```bash
git show 6e88371:src/main/java/com/example/demo/Filters.java
git show HEAD:src/main/java/com/example/demo/Filters.java
git log --oneline 6e88371..HEAD
```
