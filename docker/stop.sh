#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR=$(cd "$(dirname "$0")" && pwd)
cd "$SCRIPT_DIR"

ENV_FILE="$SCRIPT_DIR/.env"
if [ -f "$ENV_FILE" ]; then
  ENV_OPT=("--env-file" "$ENV_FILE")
else
  ENV_OPT=()
fi

echo "[stop] docker compose down"
docker compose "${ENV_OPT[@]}" -f docker-compose.yml down

