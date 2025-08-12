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
   ├─ api/client.ts
   └─ styles.css
```

## 백엔드 연동 메모
- 기본적으로 Vite dev 서버 프록시가 `/api`와 `/actuator` 경로를 `http://localhost:8080`으로 전달합니다.
- 필요 시 `VITE_API_BASE_URL`를 설정하여 직접 CORS 요청을 보낼 수 있습니다. 이 경우 백엔드 CORS 허용이 필요합니다.

## 이슈
- 관련 이슈: [#41](https://github.com/sonegy/strategic-city-simulator/issues/41)

