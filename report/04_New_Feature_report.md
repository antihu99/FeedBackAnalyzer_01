# New_Feature 단계 보고서 (FR-17~18)

| 항목 | 내용 |
|------|------|
| 브랜치 | `new_feature` |
| Base PR | `A-01` |
| PCTF | `pctf/06_New_Feature_PCTF_prompt.md` |
| 작성일 | 2026-05-22 |

---

## 1. 요약

| FR | 내용 | 상태 |
|----|------|------|
| FR-17 | `test_feedback_trend.csv` 기반 Trend UI | ✅ |
| FR-18 | File DB 키워드 CRUD·재기동 영속 | ✅ |
| FR-19~21 | TEST_PLAN·Web TC 회귀, JaCoCo ≥90% | ✅ |

---

## 2. Git 커밋 (시나리오 1~5)

| Step | 커밋 메시지 |
|------|-------------|
| 1 | `FEATURE step1: start New_Feature branch from A-01` |
| 2 | `FEATURE step2: add trend CSV sample and feature schema (FR-17, FR-18)` |
| 3 | `FEATURE step3: add Trend and Keyword File DB tests (FR-17, FR-18)` |
| 4 | `FEATURE step4: Trend visualization and File DB keywords (FR-17, FR-18)` |
| 5 | `FEATURE step5: report/04_New_Feature_report.md` |

---

## 3. Feature TC 결과

| TC ID | 결과 |
|-------|------|
| TC-TREND-01 | PASS |
| TC-TREND-02 | PASS |
| TC-KEYWORD-01 | PASS |
| TC-KEYWORD-02 | PASS |
| TC-KEYWORD-03 | PASS |
| TC-NEUTRAL-01/02 (회귀) | PASS |
| FeedbackControllerWebTest | PASS |

---

## 4. 테스트·커버리지

```text
mvn test          → Failures=0, Errors=0
JaCoCo line       → 90.4% (com.example.demo, 413/457 lines)
```

---

## 5. 구현 구조 (After)

```text
service/
  TrendService.java
  KeywordConfigService.java
repository/
  KeywordFileRepository.java
model/
  TrendSeries.java, KeywordStore.java
resources/
  test_feedback_trend.csv
data/
  keywords.json (런타임, .gitignore)
```

### API

| Method | Path | 설명 |
|--------|------|------|
| (기존) | `/`, `/analyze`, `/upload`, `/filter`, `/download` | 불변 |
| POST | `/keywords/add` | 감정 키워드 추가 |
| POST | `/keywords/remove` | 감정 키워드 삭제 |

---

## 6. 수동 검증

- [x] 메인 페이지 Trend 섹션에 날짜별 건수 표시
- [x] 키워드 추가 후 `data/keywords.json` 생성
- [x] 애플리케이션 재기동 후 키워드 유지 (TC-KEYWORD-02)

---

## 7. PR

- Head: `new_feature` → Base: `A-01`
- 제목: `New_Feature: Trend visualization and File DB keywords (FR-17~18)`
