#!/usr/bin/env bash
# xiaohe 一键 Docker 部署（使用已打好的 xiaohe-admin.jar）
# 前置：先在仓库根目录执行 ./mvnw -pl xiaohe-admin -am package -DskipTests
# 用法：在仓库根目录执行 ./docker/deploy.sh [命令]
#   deploy（默认） 校验 jar、构建镜像并后台启动
#   build          仅校验 jar 并构建 app 镜像
#   up             启动（需已有镜像）
#   down           停止并删除容器
#   logs [服务名]  跟踪日志，默认 app
#   ps             查看状态
#   restart        重启应用容器
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
JAR_PATH="$REPO_DIR/xiaohe-admin/target/xiaohe-admin.jar"
cd "$SCRIPT_DIR"

cmd="${1:-deploy}"
if [ "$#" -ge 1 ]; then
  shift
fi

require_jar() {
  if [ ! -f "$JAR_PATH" ]; then
    echo "错误: 找不到打包产物 $JAR_PATH"
    echo "请先执行: ./mvnw -pl xiaohe-admin -am package -DskipTests"
    exit 1
  fi
}

case "$cmd" in
  deploy)
    require_jar
    echo "==> 检测到打包产物: $JAR_PATH"
    echo "==> 构建应用镜像..."
    if [ "${BUILD_NOCACHE:-0}" = "1" ]; then
      docker compose build --no-cache "$@"
    else
      docker compose build "$@"
    fi
    echo "==> 启动应用容器..."
    docker compose up -d
    echo ""
    echo "完成。应用端口: ${APP_HOST_PORT:-8002}（本机访问 http://localhost:${APP_HOST_PORT:-8002}）"
    echo "请确认 docker/.env 中 DB_*、REDIS_* 指向你已有的 MySQL、Redis。"
    echo "查看日志: $SCRIPT_DIR/deploy.sh logs"
    ;;
  build)
    require_jar
    docker compose build "$@"
    ;;
  up)
    docker compose up -d "$@"
    ;;
  down)
    docker compose down "$@"
    ;;
  logs)
    docker compose logs -f "${1:-app}"
    ;;
  ps)
    docker compose ps
    ;;
  restart)
    docker compose restart "${1:-app}"
    ;;
  *)
    echo "未知命令: $cmd"
    echo "可用: deploy | build | up | down | logs | ps | restart"
    exit 1
    ;;
esac
