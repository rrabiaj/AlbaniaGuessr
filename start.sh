#!/usr/bin/env bash
# AlbaniaGuessr - Start Script
# Nis backend-in (port 8080) dhe frontend-in (port 3000) lokalisht
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
echo "================================"
echo "  AlbaniaGuessr 🇦🇱 - Local Dev"
echo "================================"

# 1. Start Backend (Spring Boot)
echo ""
echo "☕ Starting Backend on :8080..."
cd "$SCRIPT_DIR/backend"

if [ ! -f target/albania-guessr-backend-*.jar ]; then
  echo "   Building backend with Maven..."
  mvn package -DskipTests -q
fi

JAR=$(ls target/albania-guessr-backend-*.jar 2>/dev/null | head -1)
if [ -n "$JAR" ]; then
  java -jar "$JAR" --server.port=8080 > /tmp/albaniaguessr-backend.log 2>&1 &
  BACKEND_PID=$!
  echo "   Backend started (PID: $BACKEND_PID)"
  sleep 3
  if curl -sf http://localhost:8080/api/daily-challenge > /dev/null 2>&1; then
    echo "   ✅ Backend is healthy!"
  else
    echo "   ⚠️  Backend might still be starting..."
  fi
else
  echo "   ⚠️  No JAR found. Run 'mvn package -DskipTests' first."
  exit 1
fi

# 2. Start Frontend (Vite dev server)
echo ""
echo "🌐 Starting Frontend on :3000..."
cd "$SCRIPT_DIR/frontend"

if [ ! -d node_modules ]; then
  echo "   Installing dependencies..."
  npm install
fi

npx vite --port 3000 --host > /tmp/albaniaguessr-frontend.log 2>&1 &
FRONTEND_PID=$!
echo "   Frontend started (PID: $FRONTEND_PID)"

echo ""
echo "================================"
echo "  ✅ AlbaniaGuessr is running!"
echo "================================"
echo "  🌐 Frontend : http://localhost:3000"
echo "  ☕ Backend  : http://localhost:8080"
echo "  📝 Logs     : /tmp/albaniaguessr-*.log"
echo ""
echo "  Press Ctrl+C to stop both services"
echo "================================"

# Trap Ctrl+C to kill both
trap "echo ''; echo 'Stopping...'; kill $BACKEND_PID $FRONTEND_PID 2>/dev/null; exit 0" INT TERM

# Wait for either to exit
wait