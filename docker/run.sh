#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR=$(cd "$(dirname "$0")" && pwd)
ROOT_DIR=$(cd "$SCRIPT_DIR/.." && pwd)

cd "$SCRIPT_DIR"

ENV_FILE="$SCRIPT_DIR/.env"
if [ -f "$ENV_FILE" ]; then
  ENV_OPT=("--env-file" "$ENV_FILE")
else
  ENV_OPT=()
fi

echo "[run] docker compose up -d"
docker compose "${ENV_OPT[@]}" -f docker-compose.yml up --build -d
echo "[run] App is starting. Check logs: docker compose logs -f"

