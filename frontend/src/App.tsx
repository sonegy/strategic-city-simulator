import { useEffect, useMemo, useState } from 'react';
import { apiBaseUrl, getHealth } from './api/client';

export default function App() {
  const [health, setHealth] = useState<string>('unknown');
  const base = useMemo(() => apiBaseUrl(), []);

  useEffect(() => {
    getHealth()
      .then((ok) => setHealth(ok ? 'UP' : 'DOWN'))
      .catch(() => setHealth('ERROR'));
  }, []);

  return (
    <div className="container">
      <h1>Strategic City Simulator</h1>
      <p className="muted">Frontend Bootstrap (Vite + React)</p>

      <section>
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
    </div>
  );
}

