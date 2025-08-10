#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR=$(cd "$(dirname "$0")/.." && pwd)
cd "$ROOT_DIR"

ENV_FILE="docker/.env"
if [ -f "$ENV_FILE" ]; then
  # shellcheck disable=SC1090
  source "$ENV_FILE"
fi

IMAGE=${IMAGE:-strategic-city-simulator}
TAG=${TAG:-local}

echo "[clean] Removing image ${IMAGE}:${TAG} if exists"
docker rmi -f "${IMAGE}:${TAG}" 2>/dev/null || true
echo "[clean] Done"

