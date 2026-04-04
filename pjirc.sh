#!/usr/bin/env bash
# Run PJIRC — Plouf's Java IRC Client
# Usage:
#   ./pjirc.sh -n MyNick -s irc.libera.chat -p 6667
#   ./pjirc.sh -n MyNick -s irc.example.com -p 6697 -t
#   ./pjirc.sh -f path/to/config.cfg
#
# Required:
#   -n NICK       IRC nickname
#   -s SERVER     IRC server hostname
#   -p PORT       IRC server port
#
# Optional:
#   -t            Enable TLS/SSL (use for port 6697)
#   -w            Enable WebSocket transport
#   -g GUI        GUI theme: pixx, pgl, sdk (default: pixx)
#   -f FILE       Use a config file instead of -n/-s/-p
#   -h            Show this help

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR/run"

usage() {
  sed -n '2,17p' "$SCRIPT_DIR/pjirc.sh" | sed 's/^# \?//'
  exit "${1:-0}"
}

NICK=""
SERVER=""
PORT=""
GUI=""
CFGFILE=""
TLS=0
WS=0

while getopts "n:s:p:g:f:twh" opt; do
  case "$opt" in
    n) NICK="$OPTARG" ;;
    s) SERVER="$OPTARG" ;;
    p) PORT="$OPTARG" ;;
    g) GUI="$OPTARG" ;;
    f) CFGFILE="$OPTARG" ;;
    t) TLS=1 ;;
    w) WS=1 ;;
    h) usage 0 ;;
    *) usage 1 ;;
  esac
done

if [[ -n "$CFGFILE" ]]; then
  exec java -jar pjirc.jar -f "$CFGFILE"
else
  missing=()
  [[ -z "$NICK" ]]   && missing+=("-n NICK")
  [[ -z "$SERVER" ]] && missing+=("-s SERVER")
  [[ -z "$PORT" ]]   && missing+=("-p PORT")

  if [[ ${#missing[@]} -gt 0 ]]; then
    echo "Error: missing required parameters: ${missing[*]}" >&2
    echo "" >&2
    usage 1
  fi

  GUI="${GUI:-pixx}"
  FULLNAME="PJIRC User"
  ARGS=(-jar pjirc.jar -p "$NICK" "$FULLNAME" "$SERVER" "$PORT" "$GUI")
  if [[ "$TLS" -eq 1 ]]; then
    ARGS+=(-ssl)
  fi
  if [[ "$WS" -eq 1 ]]; then
    ARGS+=(-ws)
  fi
  exec java "${ARGS[@]}"
fi
