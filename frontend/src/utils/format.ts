export function formatKRW(amount?: number | null): string {
  if (amount == null || !Number.isFinite(amount as number)) return '-';
  try {
    return new Intl.NumberFormat('ko-KR', { style: 'currency', currency: 'KRW', maximumFractionDigits: 0 }).format(
      Number(amount)
    );
  } catch {
    return `${amount}원`;
  }
}

