import { useMemo } from 'react';
import type { CategoryType } from '../api/types';
import { CATEGORY_ORDER, CATEGORY_LABEL } from '../api/types';

type Props = {
  ratios: Record<CategoryType, number>; // 0.0~1.0
  onChange: (next: Record<CategoryType, number>) => void;
};

function clamp01(x: number) { return Math.max(0, Math.min(1, x)); }

// Equalize others so that total remains 1.0.
function rebalance(
  ratios: Record<CategoryType, number>,
  changedKey: CategoryType,
  newValue: number
): Record<CategoryType, number> {
  const next = { ...ratios } as Record<CategoryType, number>;
  next[changedKey] = clamp01(newValue);
  const keys = CATEGORY_ORDER.filter((k) => k !== changedKey);
  const sumOthers = keys.reduce((s, k) => s + next[k], 0);
  const targetOthers = 1 - next[changedKey];
  if (targetOthers <= 0) {
    keys.forEach((k) => (next[k] = 0));
    return next;
  }
  if (sumOthers === 0) {
    const even = targetOthers / keys.length;
    keys.forEach((k) => (next[k] = even));
    return next;
  }
  // Equal proportional scaling to fit targetOthers
  const scale = targetOthers / sumOthers;
  keys.forEach((k) => (next[k] = next[k] * scale));
  return next;
}

export default function BudgetPanel({ ratios, onChange }: Props) {
  const percent = (k: CategoryType) => Math.round((ratios[k] ?? 0) * 100);
  const totalPct = useMemo(
    () => Math.round(CATEGORY_ORDER.reduce((s, k) => s + (ratios[k] ?? 0), 0) * 100),
    [ratios]
  );

  return (
    <div>
      <h3>월간 예산 배분</h3>
      <p className="hint">합계가 100%가 되도록 자동 균형 조정됩니다.</p>
      <div style={{ display: 'grid', gap: 12 }}>
        {CATEGORY_ORDER.map((k) => (
          <div key={k}>
            <label style={{ display: 'flex', justifyContent: 'space-between' }}>
              <span>{CATEGORY_LABEL[k]}</span>
              <span>{percent(k)}%</span>
            </label>
            <input
              type="range"
              min={0}
              max={100}
              step={1}
              value={percent(k)}
              onChange={(e) => {
                const v = Number(e.target.value) / 100;
                onChange(rebalance(ratios, k, v));
              }}
            />
          </div>
        ))}
      </div>
      <div style={{ marginTop: 8 }} className="hint">합계: {totalPct}%</div>
    </div>
  );
}

