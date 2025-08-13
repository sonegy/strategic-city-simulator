import { useEffect } from 'react';
import { Routes, Route, Link } from 'react-router-dom';
import Landing from './pages/Landing';
import DashboardRoute from './pages/DashboardRoute';

export default function App() {
  // Routing 기반으로 전환하여 App 레벨의 세션 상태는 제거

  useEffect(() => {}, []);

  return (
    <div className="container">
      <h1>Strategic City Simulator</h1>
      <p className="muted">도시 지표를 확인하고 턴을 진행하세요.</p>

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
