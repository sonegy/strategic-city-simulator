import { useEffect } from 'react';
import { Routes, Route, Link, useLocation } from 'react-router-dom';
import Landing from './pages/Landing';
import DashboardRoute from './pages/DashboardRoute';

export default function App() {
  // Routing 기반으로 전환하여 App 레벨의 세션 상태는 제거

  useEffect(() => {}, []);
  const location = useLocation();
  const onHome = location.pathname === '/';

  return (
    <div className="container">
      <header style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 12 }}>
        <h1 style={{ margin: 0 }}>Strategic City Simulator</h1>
        {!onHome && (
          <nav>
            <Link className="btn ghost" to="/">← 홈</Link>
          </nav>
        )}
      </header>
      {onHome && <p className="muted" style={{ marginTop: 0 }}>도시 지표를 확인하고 턴을 진행하세요.</p>}

      <Routes>
        <Route path="/" element={<Landing />} />
        <Route path="/sessions/:id" element={<DashboardRoute />} />
      </Routes>
    </div>
  );
}
