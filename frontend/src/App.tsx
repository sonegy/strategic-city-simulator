import { useEffect, useMemo, useState } from 'react';
import { apiBaseUrl, getHealth } from './api/client';
import NewGameDialog from './components/NewGameDialog';
import type { StartSessionResponse } from './api/sessions';
import Dashboard from './components/Dashboard';

export default function App() {
  const [health, setHealth] = useState<string>('unknown');
  const base = useMemo(() => apiBaseUrl(), []);
  const [open, setOpen] = useState(false);
  const [session, setSession] = useState<StartSessionResponse | null>(null);

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

      <section>
        <h2>새 게임</h2>
        <p className="hint">난이도를 선택해 새 게임 세션을 시작합니다.</p>
        <div className="actions">
          <button className="btn primary" onClick={() => setOpen(true)}>새 게임 시작</button>
        </div>
        {session && (
          <div style={{ marginTop: 12 }}>
            <p>세션이 생성되었습니다. 세션 ID: <code>{session.sessionId}</code> (난이도: {session.difficulty})</p>
          </div>
        )}
      </section>

      <NewGameDialog
        open={open}
        onClose={() => setOpen(false)}
        onStarted={(res) => setSession(res)}
      />

      {session && (
        <div style={{ marginTop: 16 }}>
          <Dashboard sessionId={session.sessionId} initialScores={session.scores as any} />
        </div>
      )}
    </div>
  );
}
