# New_Feature 스키마·AC (FR-17, FR-18)

| 항목 | 내용 |
|------|------|
| 단계 | New_Feature |
| PRD | FR-17, FR-18 |
| PCTF | `pctf/06_New_Feature_PCTF_prompt.md` |

---

## FR-17 — Trend (`test_feedback_trend.csv`)

### CSV

| 컬럼 | 필수 | 형식 | 설명 |
|------|------|------|------|
| `date` | ✅ | `yyyy-MM-dd` | 피드백 일자 |
| `text` | ✅ | 문자열 | 피드백 본문 |

**리소스 경로**: `src/main/resources/test_feedback_trend.csv`

### 집계

- `date`별 행 건수 → Trend 시리즈 (`labels`, `counts`)
- 파일 없음/빈 데이터: `trendMessage` 안내, 빈 차트

### AC

- [ ] classpath CSV 로드 시 날짜별 건수 ≥ 1
- [ ] `index.html` Trend 섹션에 시계열 표시
- [ ] 미존재 시 warning 메시지 (TC-TREND-02)

---

## FR-18 — File DB (`data/keywords.json`)

### JSON

```json
{
  "sentiment": {
    "긍정": ["좋아요", "만족"],
    "부정": ["불만"],
    "중립": ["보통", "괜찮"]
  },
  "category": {
    "배송": {
      "main": ["배송", "택배"],
      "sub": { "time": ["배송지연"] }
    }
  }
}
```

### AC

- [ ] 최초 기동: 파일 없으면 `Constants` 기본값으로 시드
- [ ] 감정 키워드 추가/삭제 API·UI
- [ ] 재기동 후 키워드 유지 (TC-KEYWORD-02)
- [ ] 삭제 키워드는 분류·필터에 미반영 (TC-KEYWORD-03)

**설정**: `keywords.file.path=data/keywords.json` (`application.properties`)

---

## Feature TC ID

| TC ID | 클래스 | FR |
|-------|--------|-----|
| TC-TREND-01 | `TrendServiceTest` | FR-17 |
| TC-TREND-02 | `TrendServiceTest` | FR-17 |
| TC-KEYWORD-01 | `KeywordFileRepositoryTest` | FR-18 |
| TC-KEYWORD-02 | `KeywordConfigServiceTest` | FR-18 |
| TC-KEYWORD-03 | `KeywordConfigServiceTest` | FR-18 |

---

## 회귀 (FR-19~21)

- `docs/07_RED_test_plan.md` TA/FI/FH·TC-NEUTRAL 전부 PASS
- JaCoCo line ≥ 90%
