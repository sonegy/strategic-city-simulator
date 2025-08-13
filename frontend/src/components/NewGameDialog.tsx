import { useEffect, useState } from 'react';
import type { Difficulty, StartSessionResponse } from '../api/sessions';
import { startSession } from '../api/sessions';

type Props = {
  open: boolean;
  onClose: () => void;
  onStarted: (res: StartSessionResponse) => void;
};

const difficulties: Difficulty[] = ['EASY', 'NORMAL', 'HARD'];

export default function NewGameDialog({ open, onClose, onStarted }: Props) {
  const [difficulty, setDifficulty] = useState<Difficulty>('NORMAL');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!open) {
      setError(null);
      setLoading(false);
      setDifficulty('NORMAL');
    }
  }, [open]);

  if (!open) return null;

  const handleStart = async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await startSession(difficulty);
      onStarted(res);
      onClose();
    } catch (e: any) {
      setError(e?.message ?? '시작 중 오류가 발생했습니다.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal-backdrop" role="dialog" aria-modal="true">
      <div className="modal">
        <h2>새 게임</h2>
        <div className="field">
          <label>난이도</label>
          <div className="radio-group">
            {difficulties.map((d) => (
              <label key={d} className="radio">
                <input
                  type="radio"
                  name="difficulty"
                  value={d}
                  checked={difficulty === d}
                  onChange={() => setDifficulty(d)}
                  disabled={loading}
                />
                <span>{d}</span>
              </label>
            ))}
          </div>
          <p className="hint">설명: 세수/효율/이벤트 빈도가 난이도에 따라 달라집니다.</p>
        </div>

        {error && <p className="error">{error}</p>}

        <div className="actions">
          <button className="btn" onClick={onClose} disabled={loading}>
            취소
          </button>
          <button className="btn primary" onClick={handleStart} disabled={loading}>
            {loading ? '시작 중…' : '시작하기'}
          </button>
        </div>
      </div>
    </div>
  );
}

