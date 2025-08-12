#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR=$(cd "$(dirname "$0")" && pwd)

ENV_FILE="$SCRIPT_DIR/.env"
if [ -f "$ENV_FILE" ]; then
  ENV_OPT=("--env-file" "$ENV_FILE")
else
  ENV_OPT=()
fi

echo "[run-db] docker compose up -d (Postgres only)"
docker compose "${ENV_OPT[@]}" -f "$SCRIPT_DIR/docker-compose.db.yml" up -d
echo "[run-db] Postgres is starting. Logs: docker compose -f $SCRIPT_DIR/docker-compose.db.yml logs -f"

