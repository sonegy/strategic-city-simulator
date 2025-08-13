import { useMemo, useState } from 'react';
import type { CategoryType } from '../api/types';
import { CATEGORY_ORDER, CATEGORY_LABEL } from '../api/types';
import BudgetPanel from './BudgetPanel';
import { simulateMonth } from '../api/simulations';

type Props = {
  sessionId: number;
  initialScores: Record<CategoryType, number>;
};

export default function Dashboard({ sessionId, initialScores }: Props) {
  const [scores, setScores] = useState<Record<CategoryType, number>>(initialScores);
  const [delta, setDelta] = useState<Record<CategoryType, number>>(() => {
    const d: Record<CategoryType, number> = { DEFENSE: 0, DIPLOMACY: 0, ECONOMY: 0, POLITICS: 0, CULTURE: 0, ENVIRONMENT: 0 };
    return d;
  });
  const [ratios, setRatios] = useState<Record<CategoryType, number>>({
    DEFENSE: 1 / 6,
    DIPLOMACY: 1 / 6,
    ECONOMY: 1 / 6,
    POLITICS: 1 / 6,
    CULTURE: 1 / 6,
    ENVIRONMENT: 1 / 6,
  });
  const [events, setEvents] = useState<{ name: string; type: string; impact: Record<CategoryType, number> }[]>([]);
  const overall = useMemo(() => {
    const w: Record<CategoryType, number> = {
      ECONOMY: 0.25, POLITICS: 0.20, DEFENSE: 0.15, DIPLOMACY: 0.15, CULTURE: 0.15, ENVIRONMENT: 0.10,
    } as any;
    let sum = 0;
    for (const k of CATEGORY_ORDER) sum += (scores[k] ?? 0) * (w[k] ?? 0);
    return Math.round(sum * 10) / 10;
  }, [scores]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const runSimulate = async () => {
    setLoading(true); setError(null);
    try {
      const res = await simulateMonth(sessionId, ratios);
      setScores(res.after as any);
      setDelta(res.delta as any);
      setEvents(res.events);
    } catch (e: any) {
      setError(e?.message ?? '시뮬레이션 중 오류가 발생했습니다.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="row" style={{ gap: 16 }}>
      <div>
        <section>
          <h2>지표 요약</h2>
          <div className="cards">
            {CATEGORY_ORDER.map((k) => (
              <div key={k} className="card">
                <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                  <strong>{CATEGORY_LABEL[k]}</strong>
                  <span>{scores[k] ?? 0}</span>
                </div>
                <div className={`delta ${delta[k] ? (delta[k] > 0 ? 'up' : 'down') : ''}`}>
                  {delta[k] ? (delta[k] > 0 ? `▲ +${delta[k]}` : `▼ ${delta[k]}`) : '▬ ±0'}
                </div>
              </div>
            ))}
          </div>
          <div style={{ marginTop: 8 }} className="hint">종합 지수(가중): {overall}</div>
        </section>

        <section>
          <h2>이벤트 로그(최근 턴)</h2>
          {events.length === 0 ? (
            <p className="hint">최근 턴 이벤트 없음</p>
          ) : (
            <ul>
              {events.map((e, idx) => (
                <li key={idx}>
                  <strong>{e.name}</strong> <span className="hint">({e.type})</span>
                </li>
              ))}
            </ul>
          )}
        </section>
      </div>
      <div>
        <section>
          <BudgetPanel ratios={ratios} onChange={setRatios} />
          <div className="actions" style={{ marginTop: 12 }}>
            <button className="btn primary" onClick={runSimulate} disabled={loading}>
              {loading ? '턴 진행 중…' : '턴 진행 ▶'}
            </button>
          </div>
          {error && <p className="error">{error}</p>}
        </section>
      </div>
    </div>
  );
}
