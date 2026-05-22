# PRD — Feedback Analyzer (고객 피드백 분석 시스템)

| 항목 | 내용 |
|------|------|
| 문서 ID | PRD-FA-001 |
| 버전 | 2.0 |
| 작성일 | 2026-05-21 |
| 근거 | `project_purpose.md`, `README.md` |
| 상태 | Approved (Draft for Review) |

---

## 1. 문서 목적

`project_purpose.md`의 학습·기능 목표와 `README.md`의 사용자 관점 요구를 **제품 요구사항(PRD)** 으로 통합한다. 이후 `01_analysis.md`, 개발·테스트·PR의 **단일 기준 문서**로 사용한다.

---

## 2. 제품 정의

### 2.1. 한 줄 정의

자연어 고객 피드백을 **수집 → 감정·주제 분류 → 필터·시각화 → CSV보내기** 하는 Spring Boot 웹 대시보드.

### 2.2. 제품 속성

| 구분 | 내용 |
|------|------|
| 유형 | 로컬 웹 애플리케이션 (실습·리팩토링 챌린지) |
| 사용자 | CS/운영 분석 담당자, PM, Java 학습자 |
| 배포 | `mvn spring-boot:run` → `http://localhost:8080` |

### 2.3. 기술 스택

| 항목 | 값 |
|------|-----|
| Language | Java 17 |
| Framework | Spring Boot 3.5.3 |
| View | Thymeleaf |
| CSV | OpenCSV 5.11.2 |
| Build | Maven |

---

## 3. 목표

### 3.1. 제품 목표

| ID | 목표 | 성공 지표 |
|----|------|-----------|
| PG-01 | 피드백 입력~분석 결과 확인 | E2E 5분 이내 (로컬) |
| PG-02 | 감정·카테고리별 통계 제공 | 긍정/부정/중립, 5개 카테고리 집계 |
| PG-03 | 조건별 필터·다운로드 | 필터 후 CSV 다운로드 |
| PG-04 | 분석·필터 결과 일관성 | 동일 문장 → 동일 감정 (GREEN 이후) |

### 3.2. 학습 목표 (`project_purpose.md` §1.3)

- 코드 스멜·안티패턴 식별
- MVC/레이어드 아키텍처, SRP·OCP
- TDD, 커버리지 **90% 이상**
- 비즈니스 로직과 UI 분리
- 상태 관리·DI 설계

---

## 4. 사용자·시나리오

### 4.1. 페르소나

| 페르소나 | 니즈 |
|----------|------|
| 분석 담당자 | CSV 대량 업로드, 감정·주제별 건수 |
| PM | 긍정/부정 비율, 카테고리 이슈 파악 |
| 학습자 | 레거시 개선, TC·리팩토링 실습 |

### 4.2. 핵심 사용자 여정 (`README.md` + `project_purpose.md` §2.2)

1. `http://localhost:8080` 접속
2. 피드백 텍스트 입력 **또는** CSV 업로드
3. 시스템이 감정·키워드(카테고리) 분석
4. 감정/키워드 필터로 결과 조회
5. 통계 확인 후 필요 시 CSV 다운로드

---

## 5. 기능 요구사항

### 5.1. Baseline (MVP) — 현행 제공 대상

`project_purpose.md` §2.1 · `README.md` 주요 기능.

| ID | 기능 | 설명 | API |
|----|------|------|-----|
| FR-01 | 대시보드 | 단일 페이지 UI | `GET /` |
| FR-02 | 텍스트 입력 | 수동 피드백 추가·누적 분석 | `POST /analyze` |
| FR-03 | CSV 업로드 | `text` 컬럼 파싱·적재 | `POST /upload` |
| FR-04 | 감정 분석 | 긍정·부정·중립 건수 | `TextAnalyzer.sent()` |
| FR-05 | 카테고리 분석 | 5개 카테고리 건수 | `TextAnalyzer.kw()` |
| FR-06 | 필터링 | 감정·카테고리 복합 필터 | `POST /filter` |
| FR-07 | 시각화 | 감정·키워드 분포(건수) | Thymeleaf `index.html` |
| FR-08 | CSV 다운로드 | 필터 결과보내기 | `GET /download` |

### 5.2. 품질·버그 수정 (GREEN) — Must

`project_purpose.md` §6.1 단계 3.

| ID | 요구사항 | Acceptance Criteria |
|----|----------|---------------------|
| FR-09 | 중립 필터 정합성 | 분석·필터가 **동일 감정 규칙** 사용; `중립` 필터 TC 통과 |
| FR-10 | Multiline 입력 | 여러 줄 텍스트 입력·저장·표시 |
| FR-11 | 로그 UI Level | warning/error 등 **페이지에서** 표시 on/off |

### 5.3. 구조 개선 (REFACTORING)

`project_purpose.md` §6.1 단계 4~6.

| ID | 요구사항 |
|----|----------|
| FR-12 | 도메인 네이밍 (`fil`, `sent`, `kw` 제거) |
| FR-13 | 매직 스트링·중복 키워드 → enum/설정 클래스 |
| FR-14 | 감정 판별 로직 단일화 |
| FR-15 | Controller = HTTP only, Service 분리 |
| FR-16 | 패키지: `controller`, `service`, `model`, `config` |

### 5.4. 추가 기능 (New_Feature)

`project_purpose.md` §6.1 단계 7.

| ID | 요구사항 | 입력 |
|----|----------|------|
| FR-17 | Trend 시각화 | `test_feedback_trend.csv` |
| FR-18 | File DB 키워드 관리 | 감정·필터 키워드 CRUD·영속 |

### 5.5. 테스트 (RED)

| ID | 요구사항 | 기준 |
|----|----------|------|
| FR-19 | JUnit 5 TC | TextAnalyzer, Filters, FileHandler |
| FR-20 | 커버리지 | **≥ 90%** (JaCoCo) |
| FR-21 | 회귀 | GREEN 이후 `mvn test` 전체 통과 |

---

## 6. 입출력 명세

### 6.1. 입력 (`project_purpose.md` §2.3)

| 기능 | 입력 예시 | 출력 예시 |
|------|-----------|-----------|
| 텍스트 입력 | "배송이 너무 늦어요. 화가 납니다." | 감정: 부정, 카테고리: 배송 |
| CSV 업로드 | `feedbacks.csv` (`text` 컬럼) | 분석 테이블·통계 |
| 감정 분석 | — | 긍정 40%, 중립 20%, 부정 40% |
| 키워드 필터 | "배송" | 배송 관련 N건 |

### 6.2. CSV 규칙 (`README.md`)

| 규칙 | 내용 |
|------|------|
| 필수 컬럼 | `text` |
| 헤더 | 1행 (업로드 시 스킵) |
| 인코딩 | UTF-8 권장 |

### 6.3. 카테고리 (Baseline)

배송 · 품질 · 가격 · 서비스 · 사용성

### 6.4. API 계약 (리팩토링 시 유지)

| Method | Path |
|--------|------|
| GET | `/` |
| POST | `/analyze` |
| POST | `/upload` |
| POST | `/filter` |
| GET | `/download` |

---

## 7. 비기능 요구사항

| ID | 요구사항 | 목표 |
|----|----------|------|
| NFR-01 | 로컬 실행 | `mvn spring-boot:run` 성공 |
| NFR-02 | 응답 | 1,000건 이하 분석 ≤ 3초 (로컬) |
| NFR-03 | OS 호환 | CSV 업로드 Windows/Linux (임시파일 이식) |
| NFR-04 | 테스트 | Coverage ≥ 90% |
| NFR-05 | 설계 | SRP, OCP (키워드 확장) |
| NFR-06 | UI | 한국어 기본 |

---

## 8. 범위

### In Scope

FR-01~FR-21, 문서화, Git 단계별 PR, 회고·발표.

### Out of Scope (v1)

- 외부 NLP/ML API
- 인증·멀티테넌트
- RDBMS (File DB로 대체)
- 모바일 네이티브 앱

---

## 9. 릴리스 단계 (PRD ↔ 브랜치)

| 단계 | 브랜치 | PRD 범위 | 공수(참고) |
|------|--------|----------|------------|
| 1 | SPEC | 문서·분석 | 1h |
| 2 | RED | FR-19~21 | 2h |
| 3 | GREEN | FR-09~11 | 1.5h |
| 4~6 | REFACTORING | FR-12~16 | 3.5h |
| 7 | New_Feature | FR-17~18 | 3h |
| 8 | — | 회고·발표 | 2h |

**Git**: 기능 브랜치 → `A-01` 머지 → 최종 `A-01` → `main` (브랜치 삭제 안 함).

---

## 10. 수용 기준 (Release)

- [ ] FR-01~08 Baseline E2E 통과
- [ ] FR-09~11 GREEN 완료
- [ ] FR-19~21 커버리지 90%
- [ ] FR-12~16 REFACTORING 완료
- [ ] FR-17~18 New_Feature 완료
- [ ] `01_analysis.md` 갭 항목 해소 또는 문서화

---

## 11. 관련 문서

| 문서 | 경로 |
|------|------|
| 프로젝트 목적 | `project_purpose.md` |
| 사용자 README | `README.md` |
| 코드 분석 | `docs/01_analysis.md` |
| 작업 시나리오 | `docs/02_work_scenario.md` |
| 작업 안내 | `docs/03_work_guide.md` |
| 작업 규칙 원본 | `작업규칙.TXT` |

---

## 12. 변경 이력

| 버전 | 일자 | 내용 |
|------|------|------|
| 1.0 | 2026-05-21 | 최초 작성 |
| 2.0 | 2026-05-21 | docs 폴더 재구성, README·purpose 재정합 |
