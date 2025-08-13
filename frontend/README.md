# Strategic City Simulator - Frontend (T4-1)

Vite + React 기반의 SPA 프론트엔드 스캐폴드입니다. 로컬 개발 시 백엔드(8080)와 연동되며, `/actuator/health` 헬스체크 및 `/api` 프록시 구성을 제공합니다.

## 빠른 시작

1) Node.js LTS(>=18) 설치 후 의존성 설치
```
cd frontend
npm install
```

2) 로컬 개발 서버 실행
```
npm run dev
```
- 기본 포트: 5173
- 백엔드 프록시: `/api`, `/actuator` → `http://localhost:8080`

3) 환경 변수
- 개발: `.env.development` (샘플 포함)
- 프로덕션: `.env.production`
- 공통: `VITE_API_BASE_URL` (선택). 설정 시 프록시 대신 해당 주소를 직접 사용합니다.

## 빌드
```
npm run build
npm run preview
```

## 구조
```
frontend/
├─ index.html
├─ package.json
├─ vite.config.ts
├─ tsconfig.json
├─ tsconfig.node.json
├─ .env.development
├─ .env.production
└─ src/
   ├─ main.tsx
   ├─ App.tsx
   ├─ styles.css
   ├─ api/
   │  ├─ client.ts
   │  ├─ sessions.ts
   │  ├─ simulations.ts
   │  └─ types.ts
   └─ components/
      ├─ NewGameDialog.tsx
      ├─ Dashboard.tsx
      └─ BudgetPanel.tsx
```

## 백엔드 연동 메모
- 기본적으로 Vite dev 서버 프록시가 `/api`와 `/actuator` 경로를 `http://localhost:8080`으로 전달합니다.
- 필요 시 `VITE_API_BASE_URL`를 설정하여 직접 CORS 요청을 보낼 수 있습니다. 이 경우 백엔드 CORS 허용이 필요합니다.

## 이슈
- 관련 이슈: [#41](https://github.com/sonegy/strategic-city-simulator/issues/41)

## UX 제안: 초기 로딩 → 새 게임/기존 선택 → 대시보드
와이어프레임(`docs/wireframes/README.md`) 기준으로, 초기 진입 시 다음 흐름을 권장합니다.

- 첫 화면(Landing)
  - 선택지: [새 게임 시작] / [기존 게임 선택]
  - 새 게임: `NewGameDialog`를 열어 난이도 선택 → `POST /api/v1/sessions` 호출 → 세션 생성
  - 기존 게임: (권장) 세션 ID 입력 → 해당 세션 URL로 이동(`/sessions/:id`)
    - 선택적으로 로컬에 저장된 최근 세션 ID를 불러와 빠른 진입 버튼 제공

- 세션 진입(Main Dashboard)
  - 컴포넌트: `Dashboard`
  - 기능: 지표 카드(현재 값/변화), 예산 슬라이더(`BudgetPanel`, 합계 자동 100%), [턴 진행] → `POST /api/v1/sessions/{id}/simulate`
  - 이벤트 로그: 최근 턴 이벤트 표시

- 상태 전이 제안(App 레벨)
  - 상태 A(세션 없음): Landing + `NewGameDialog`
  - 상태 B(세션 있음): `Dashboard` 렌더링
  - 세션 지속성: 기본은 URL 기반(`/sessions/:id`)으로 복원. `localStorage`는 선택적 편의 기능(“최근 세션 계속하기”)으로만 사용

- 라우팅(선택)
  - 권장: `react-router` 도입으로 딥링크/새로고침 대응
    - `/` → Landing, `/sessions/:id` → Dashboard
    - 새 게임 성공 시 `navigate('/sessions/{id}')`
    - Dashboard 마운트 시 `GET /api/v1/sessions/{id}/reports`로 초기 상태 복원

 에러 처리 가이드
 - 존재하지 않는 ID: “세션을 찾을 수 없음” 안내 + [새 게임 시작] CTA
 - 네트워크 오류: 재시도/가이드 표시

 현재 구현은 App에서 “새 게임 시작 → NewGameDialog → 세션 생성 시 Dashboard 렌더”까지 연결되어 있습니다. 위 제안(라우팅 기반 Landing/세션 복원/선택적 localStorage)을 반영하면 와이어프레임 의도와 완전히 일치하는 구조가 됩니다. 필요하시면 해당 구조로 리팩터링 PR을 준비하겠습니다.
