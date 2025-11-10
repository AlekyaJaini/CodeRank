#!/usr/bin/env bash
set -euo pipefail

# python-runner entrypoint
# expects: SRC_B64 (required), optional FILE (default main.py), optional STDIN_B64
# writes /sandbox/<FILE>, ensures stdin.txt exists, runs with timeout

mkdir -p /sandbox
cd /sandbox

put_b64_to_file() {
  local b64="$1"; shift
  local dest="$1"; shift
  if [ -n "${b64:-}" ]; then
    echo "$b64" | base64 --decode > "$dest"
  fi
}

# choose filename: prefer FILE env, default to main.py
FILE="${FILE:-main.py}"

# write source
if [ -n "${SRC_B64:-}" ]; then
  put_b64_to_file "$SRC_B64" "$FILE"

else

  exit 2
fi

# stdin
if [ -n "${STDIN_B64:-}" ]; then
  put_b64_to_file "$STDIN_B64" "stdin.txt"
else
  : > stdin.txt
fi

# run with timeout (configurable via TIMEOUT_SEC env)
TIMEOUT="${TIMEOUT_SEC:-5}s"

timeout "$TIMEOUT" python3 "$FILE" < stdin.txt
exit $?
