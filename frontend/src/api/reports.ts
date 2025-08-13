import { apiBaseUrl } from './client';
import type { CategoryType } from './types';

export interface ReportEventDto {
  type: string;
  description: string;
  occurredAt: string;
}

export interface ReportResponse {
  year: number | null;
  month: number | null;
  scores: Record<CategoryType, number>;
  overall: number;
  events: ReportEventDto[];
  cumulativeByType: Record<string, number>;
}

export async function getLatestReport(sessionId: number): Promise<ReportResponse> {
  const base = apiBaseUrl();
  const url = (base ?? '') + `/api/v1/sessions/${sessionId}/reports`;
  const res = await fetch(url);
  if (!res.ok) {
    const text = await res.text().catch(() => '');
    throw new Error(`Failed to load report: ${res.status} ${text}`);
  }
  return (await res.json()) as ReportResponse;
}

