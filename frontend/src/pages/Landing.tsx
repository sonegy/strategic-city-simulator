import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import NewGameDialog from '../components/NewGameDialog';
import type { StartSessionResponse } from '../api/sessions';

export default function Landing() {
  const [open, setOpen] = useState(false);
  const [existingId, setExistingId] = useState('');
  const navigate = useNavigate();

  const gotoExisting = () => {
    const id = Number(existingId);
    if (!Number.isFinite(id) || id <= 0) return;
    navigate(`/sessions/${id}`);
  };

  return (
    <>
      <p className="muted">세션을 선택하여 시작하세요.</p>

      <section style={{ marginBottom: 16 }}>
        <h2>새 게임</h2>
        <div className="actions">
          <button className="btn primary" onClick={() => setOpen(true)}>새 게임 시작</button>
        </div>
      </section>

      <section>
        <h2>기존 게임</h2>
        <div style={{ display: 'flex', gap: 8, alignItems: 'center' }}>
          <input
            type="number"
            inputMode="numeric"
            placeholder="세션 ID 입력"
            value={existingId}
            onChange={(e) => setExistingId(e.target.value)}
            style={{ padding: '8px 10px', borderRadius: 8, border: '1px solid #4b5563', background: '#111827', color: '#e5e7eb' }}
          />
          <button className="btn" onClick={gotoExisting} disabled={!existingId}>이동</button>
        </div>
        <p className="hint" style={{ marginTop: 6 }}>예: /sessions/123 으로 이동합니다.</p>
      </section>

      <NewGameDialog
        open={open}
        onClose={() => setOpen(false)}
        onStarted={(res: StartSessionResponse) =>
          navigate(`/sessions/${res.sessionId}`, { state: { sessionMeta: res } })
        }
      />
    </>
  );
}
