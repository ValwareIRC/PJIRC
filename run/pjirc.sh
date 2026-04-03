#!/usr/bin/env bash
# Run PJIRC standalone application
cd "$(dirname "$0")"
java -jar pjirc.jar "$@"
