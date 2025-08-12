# Figma 임포트 가이드

이 폴더에는 Figma에서 즉시 가져와 참고/트레이싱할 수 있는 SVG 와이어프레임이 포함되어 있습니다. 각 SVG는 프레임 단위(1440×1024 데스크톱, 390×844 모바일)로 구성되었습니다.

## 파일 목록
- `figma/Main_Dashboard.svg`: 메인 대시보드(지표 요약 + 예산 슬라이더 + 턴 진행)
- `figma/Monthly_Report_Modal.svg`: 월간 보고서 모달
- `figma/Trends_Timeline.svg`: 지수 추이 그래프 + 이벤트 타임라인
- `figma/New_Game_Dialog.svg`: 새 게임 시작(난이도/초기상태)
- `figma/Event_Log_Drawer.svg`: 이벤트 로그 드로어
- `figma/Mobile_Layout.svg`: 모바일 레이아웃(상단 카드 + 하단 슬라이더)

## Figma로 가져오기
1. Figma에서 새 파일을 만들고 페이지를 `Wireframes`로 생성합니다.
2. 메뉴 `File > Place image` 또는 드래그&드롭으로 각 SVG를 캔버스에 올립니다.
3. 필요시 `Right click > Ungroup`로 그룹 해제 후, `Outline stroke`를 적용해 편집성을 높입니다.
4. Auto Layout 전환 팁:
   - 카테고리 카드 묶음: `Shift`로 카드들을 선택 → `Auto layout (Shift+A)` → 방향 `Vertical`, `Spacing 12`.
   - 예산 슬라이더 리스트: 리스트 컨테이너에 Auto layout → `Vertical`, `Spacing 8`, 내부 각 항목은 `Horizontal`, `Spacing 12`.

## 디자인 토큰 제안 (Figma Styles/Variables로 등록)
- Colors
  - `Neutral/100`: #111111
  - `Neutral/70`: #666666
  - `Neutral/30`: #CCCCCC
  - `Accent/Blue`: #2F80ED (강조, 링크)
  - `Accent/Green`: #27AE60 (상승)
  - `Accent/Red`: #EB5757 (하락)
- Typography
  - Display: Inter 24/32 Bold
  - H2: Inter 18/24 Semibold
  - Body: Inter 14/20 Regular
  - Caption: Inter 12/16 Regular
- Spacing (8pt 그리드)
  - XS 4 / S 8 / M 12 / L 16 / XL 24 / XXL 32

## 컴포넌트 브레이크다운
- `Card/Category`: 타이틀, 값, 변화 배지, 스파크라인
- `List/BudgetItem`: 라벨, 퍼센트, 슬라이더, 금액, 힌트
- `Banner/TopBar`: 도시명, 턴, 예산, 난이도, 액션(저장/설정)
- `Modal/MonthlyReport`: 헤더, 카테고리 변화 표, 이벤트 리스트, 재정 요약, CTA
- `Drawer/EventLog`: 월/이벤트/효과 항목 리스트

## 다음 단계
- 필요하시면 제가 컴포넌트화(Variants/Auto Layout/Constraints 세팅)된 Figma 파일 구조를 제안하거나, 실제 Figma 파일로 업로드해 드릴 수 있습니다. (네트워크 접근 허용 또는 공유 링크 제공 필요)

