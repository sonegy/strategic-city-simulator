export type CategoryType =
  | 'DEFENSE'
  | 'DIPLOMACY'
  | 'ECONOMY'
  | 'POLITICS'
  | 'CULTURE'
  | 'ENVIRONMENT';

export const CATEGORY_ORDER: CategoryType[] = [
  'ECONOMY',
  'POLITICS',
  'DEFENSE',
  'DIPLOMACY',
  'CULTURE',
  'ENVIRONMENT',
];

export const CATEGORY_LABEL: Record<CategoryType, string> = {
  DEFENSE: '국방',
  DIPLOMACY: '외교',
  ECONOMY: '경제',
  POLITICS: '정치',
  CULTURE: '문화',
  ENVIRONMENT: '환경',
};

