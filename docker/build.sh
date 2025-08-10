#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR=$(cd "$(dirname "$0")/.." && pwd)
cd "$ROOT_DIR"

# 환경 변수 로드
ENV_FILE="docker/.env"
if [ -f "$ENV_FILE" ]; then
  # shellcheck disable=SC1090
  source "$ENV_FILE"
fi

IMAGE=${IMAGE:-strategic-city-simulator}
TAG=${TAG:-local}

echo "[build] Building image: ${IMAGE}:${TAG}"
docker build -t "${IMAGE}:${TAG}" -f Dockerfile .
echo "[build] Done: ${IMAGE}:${TAG}"

