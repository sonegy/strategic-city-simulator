import { useEffect, useMemo, useState } from 'react';
import { apiBaseUrl, getHealth } from './api/client';
import { Routes, Route, Link } from 'react-router-dom';
import Landing from './pages/Landing';
import DashboardRoute from './pages/DashboardRoute';

export default function App() {
  const [health, setHealth] = useState<string>('unknown');
  const base = useMemo(() => apiBaseUrl(), []);
  // Routing 기반으로 전환하여 App 레벨의 세션 상태는 제거

  useEffect(() => {
    getHealth()
      .then((ok) => setHealth(ok ? 'UP' : 'DOWN'))
      .catch(() => setHealth('ERROR'));
  }, []);

  return (
    <div className="container">
      <h1>Strategic City Simulator</h1>
      <p className="muted">Frontend Bootstrap (Vite + React)</p>

      <section style={{ marginBottom: 16 }}>
        <h2>Backend 연결 상태</h2>
        <ul>
          <li>
            API Base: <code>{base ?? '(proxy)'}</code>
          </li>
          <li>
            Health: <strong>{health}</strong>
          </li>
        </ul>
        <p className="hint">
          개발 모드에서는 Vite 프록시가 <code>/api</code>, <code>/actuator</code>를
          <code>http://localhost:8080</code>으로 전달합니다.
        </p>
      </section>

      <nav style={{ marginBottom: 16 }}>
        <Link className="btn" to="/">홈</Link>
      </nav>

      <Routes>
        <Route path="/" element={<Landing />} />
        <Route path="/sessions/:id" element={<DashboardRoute />} />
      </Routes>
    </div>
  );
}
