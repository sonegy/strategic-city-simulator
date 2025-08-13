export type Difficulty = 'EASY' | 'NORMAL' | 'HARD';

export interface StartSessionResponse {
  sessionId: number;
  difficulty: Difficulty;
  scores: Record<string, number>;
}

import { apiBaseUrl } from './client';

export async function startSession(difficulty: Difficulty): Promise<StartSessionResponse> {
  const base = apiBaseUrl();
  const url = (base ?? '') + '/api/v1/sessions';
  const res = await fetch(url, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ difficulty }),
  });
  if (!res.ok) {
    const text = await res.text().catch(() => '');
    throw new Error(`Failed to start session: ${res.status} ${text}`);
  }
  return (await res.json()) as StartSessionResponse;
}

