-- V2: 난이도별 초기 예산/금고 컬럼 추가
-- H2(PostgreSQL mode) 호환: 다중 ADD COLUMN/IF NOT EXISTS를 지양하고 개별 ALTER 사용
ALTER TABLE game_sessions ADD COLUMN initial_budget BIGINT NOT NULL DEFAULT 0;
ALTER TABLE game_sessions ADD COLUMN treasury BIGINT NOT NULL DEFAULT 0;
