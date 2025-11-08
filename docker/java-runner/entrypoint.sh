#!/usr/bin/env bash
set -euo pipefail

# decode source and stdin
if [ -z "${SRC_B64:-}" ]; then
  echo "No source provided" >&2
  exit 2
fi

echo "$SRC_B64" | base64 --decode > /sandbox/$(basename "${FILE:-Main.java}") 2>/dev/null || true
# if FILE env is provided, trust it; otherwise default to Main.java
# But in our approach we rely on container entrypoint to detect filename by env or default.

# decode to STDIN file
if [ -n "${STDIN_B64:-}" ]; then
  echo "$STDIN_B64" | base64 --decode > /sandbox/stdin.txt
else
  echo -n "" > /sandbox/stdin.txt
fi

# determine language by presence of files (simple heuristic)
if [ -f /sandbox/Main.java ]; then
  # compile
  javac /sandbox/Main.java 2> /sandbox/compile_err.txt || { cat /sandbox/compile_err.txt >&2; exit 3; }
  # run with timeout (3s)
 # run without internal timeout (outer runner handles this)
 exec java -cp /sandbox Main < /sandbox/stdin.txt
elif [ -f /sandbox/main.py ]; then
  timeout 5s python3 /sandbox/main.py < /sandbox/stdin.txt
  exit $?
elif [ -f /sandbox/main.cpp ]; then
  g++ -O2 -std=gnu++17 /sandbox/main.cpp -o /sandbox/main 2> /sandbox/compile_err.txt || { cat /sandbox/compile_err.txt >&2; exit 3; }
  timeout 5s /sandbox/main < /sandbox/stdin.txt
  exit $?
else
  echo "No recognised source file." >&2
  exit 2
fi