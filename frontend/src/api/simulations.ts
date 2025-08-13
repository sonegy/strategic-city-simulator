import { apiBaseUrl } from './client';
import type { CategoryType } from './types';

export interface SimulateResponse {
  before: Record<CategoryType, number>;
  after: Record<CategoryType, number>;
  delta: Record<CategoryType, number>;
  events: { name: string; type: string; impact: Record<CategoryType, number> }[];
  overallIndex: number;
}

export async function simulateMonth(
  sessionId: number,
  ratios: Record<CategoryType, number>
): Promise<SimulateResponse> {
  const base = apiBaseUrl();
  const url = (base ?? '') + `/api/v1/sessions/${sessionId}/simulate`;
  const res = await fetch(url, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ budgetRatios: ratios }),
  });
  if (!res.ok) {
    const text = await res.text().catch(() => '');
    throw new Error(`Failed to simulate: ${res.status} ${text}`);
  }
  return (await res.json()) as SimulateResponse;
}

