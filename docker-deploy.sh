#!/usr/bin/env bash
# 仓库根目录一键 Docker 部署，转发至 docker/deploy.sh
exec "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/docker/deploy.sh" "$@"
