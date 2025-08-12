export function apiBaseUrl(): string | undefined {
  const base = import.meta.env.VITE_API_BASE_URL as string | undefined;
  return base && base.length > 0 ? base.replace(/\/$/, '') : undefined;
}

async function doGet(path: string): Promise<Response> {
  const base = apiBaseUrl();
  const url = base ? `${base}${path}` : path; // dev proxy handles relative
  return fetch(url, { credentials: 'omit' });
}

export async function getHealth(): Promise<boolean> {
  try {
    const res = await doGet('/actuator/health');
    if (!res.ok) return false;
    const data = await res.json();
    return data?.status === 'UP';
  } catch {
    return false;
  }
}

