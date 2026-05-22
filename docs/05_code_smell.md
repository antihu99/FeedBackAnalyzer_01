# 코드 스멜 사례 — Feedback Analyzer (초기 버전)

| 항목 | 내용 |
|------|------|
| 근거 | `docs/04_mom_test.md` 인터뷰 가설, `project_purpose.md` §4, `docs/01_analysis.md` |
| Phase | Phase 1 — **구현·코드 수정 금지** |
| 작성일 | 2026-05-21 |

---

## 사례 3건 (Mom Test → 스멜 매핑)

- **사례 1 — 감정 규칙 이중화 (중립 필터)**  
  - **Mom Test**: Q5~Q7 — 보고용 부정 비율과 필터 결과가 맞지 않거나, 중립만 골라도 건수가 어긋남.  
  - **스멜**: 중복 코드(`TextAnalyzer` / `Filters.S_KEYWORDS` / `Constants`), Shotgun Surgery.  
  - **안티패턴**: 서로 다른 규칙으로 동일 도메인(감정) 처리 → **Feature Envy·불일치**.  
  - **초기 버전**: `괜찮` 긍정·중립 중복, `sent()`는 중립 키워드 미검사 (`01_analysis.md` P0-1).  
  - **PRD**: FR-09.

- **사례 2 — 수집·필터·다운로드 단절 (God Controller)**  
  - **Mom Test**: Q2, Q7, Q9 — 업로드 직후 통계 없음, 필터 없이 다운로드 시 빈 CSV, 보고용 파일 형식 오류.  
  - **스멜**: God Object(`FeedbackController`), 전역 상태(`fil_data`, `Session` static), Lava Flow(`FileHandler` 미사용).  
  - **안티패턴**: UI·수집·분석·보내기 한 클래스 — **God Function**.  
  - **초기 버전**: `C:\tmp` 하드코딩, `Content-Disposition` 오류, `/upload` 후 분석 Model 누락.  
  - **PRD**: FR-03, FR-08, FR-15~16.

- **사례 3 — 키워드·카테고리 운영 비용 (가짜 도메인)**  
  - **Mom Test**: Q3, Q4, Q10 — 키워드 분류 오류·분기마다 기준 변경·결국 수동 재분류.  
  - **스멜**: 매직/하드코딩(`Constants` + `UIComponents.CATS`), 부적절한 네이밍(`fil`, `kw`), 테스트 미비.  
  - **안티패턴**: Shotgun Surgery(카테고리 1개 추가 시 4~5파일), `Feedback`의 `sentiment`/`category` 미사용.  
  - **초기 버전**: `Filters`가 `CATEGORY_KEYWORDS` `sub` Map에 강결합; File DB 없음.  
  - **PRD**: FR-12~13, FR-18.

---

## 관련 문서

- Mom Test 질문 전문: `docs/04_mom_test.md`  
- 전체 스멜 목록: `project_purpose.md` §4.1~4.2  
- 구현 갭: `docs/01_analysis.md` §5
