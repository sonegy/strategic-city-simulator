import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import Dashboard from '../components/Dashboard';
import { getLatestReport } from '../api/reports';
import type { CategoryType } from '../api/types';

export default function DashboardRoute() {
  const { id } = useParams();
  const sessionId = Number(id);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [scores, setScores] = useState<Record<CategoryType, number> | null>(null);

  useEffect(() => {
    let mounted = true;
    async function load() {
      if (!Number.isFinite(sessionId) || sessionId <= 0) {
        setError('잘못된 세션 ID입니다.');
        setLoading(false);
        return;
      }
      try {
        const rep = await getLatestReport(sessionId);
        if (!mounted) return;
        setScores(rep.scores as any);
      } catch (e: any) {
        setError(e?.message ?? '리포트 로딩 실패');
      } finally {
        setLoading(false);
      }
    }
    load();
    return () => {
      mounted = false;
    };
  }, [sessionId]);

  if (loading) return <div className="container"><p>로딩 중…</p></div>;
  if (error) return (
    <div className="container">
      <p className="error" style={{ marginBottom: 12 }}>{error}</p>
      <Link className="btn" to="/">새 게임 시작으로 돌아가기</Link>
    </div>
  );
  if (!scores) return null;

  return (
    <div className="container">
      <Dashboard sessionId={sessionId} initialScores={scores} />
    </div>
  );
}

