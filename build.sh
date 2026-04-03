#!/usr/bin/env bash
#
# Build script for PJIRC (Plouf's Java IRC Client)
#
set -e

SRCDIR="src"
OUTDIR="build/classes"
JARFILE="run/pjirc.jar"

echo "=== PJIRC Build ==="

# Clean
rm -rf build
mkdir -p "$OUTDIR"

# Collect source files (excluding MS-specific security providers)
find "$SRCDIR" -name "*.java" | grep -v "SpecificMS" > build/sources.txt
COUNT=$(wc -l < build/sources.txt)
echo "Compiling $COUNT source files..."

# Compile
javac -encoding ISO-8859-1 -source 8 -target 8 \
  -sourcepath "$SRCDIR" \
  -d "$OUTDIR" \
  @build/sources.txt 2>&1 | grep -v "warning:" || true

# Create manifest
mkdir -p "$(dirname "$JARFILE")"
cat > build/MANIFEST.MF <<EOF
Manifest-Version: 1.0
Main-Class: irc.IRCApplication
EOF

# Build JAR
jar cfm "$JARFILE" build/MANIFEST.MF -C "$OUTDIR" .

echo "Built $JARFILE ($(du -h "$JARFILE" | cut -f1))"
echo ""
echo "Run with:  cd run && java -jar pjirc.jar"
