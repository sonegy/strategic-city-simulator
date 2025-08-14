-- V2: 난이도별 초기 예산/금고 컬럼 추가
ALTER TABLE game_sessions
    ADD COLUMN IF NOT EXISTS initial_budget BIGINT NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS treasury BIGINT NOT NULL DEFAULT 0;

