#!/usr/bin/env bash
#
# Build script for PJIRC (Plouf's Java IRC Client)
#
set -e

SRCDIR="src"
OUTDIR="build/classes"
JARFILE="run/pjirc.jar"

echo "=== PJIRC Build ==="

# Detect JDK version
JDK_VERSION=$(javac -version 2>&1 | grep -oP '\d+' | head -1)
echo "Detected JDK major version: $JDK_VERSION"

# Clean
rm -rf build
mkdir -p "$OUTDIR"

# Collect source files (excluding MS-specific security providers)
EXCLUDE_PATTERN="SpecificMS"

# JDK 23+ removed java.applet (JEP 471) — exclude applet-dependent sources
if [[ "$JDK_VERSION" -ge 23 ]]; then
  echo "JDK >= 23 detected: excluding applet classes (java.applet removed by JEP 471)"
  echo "  Desktop and browser/CheerpJ modes both work via IRCApplication.main()."
  EXCLUDE_PATTERN="SpecificMS\|IRCApplet\|AppletFileHandler\|AppletImageLoader\|AppletSoundHandler\|AppletURLHandler"
fi

find "$SRCDIR" -name "*.java" | grep -v "$EXCLUDE_PATTERN" > build/sources.txt
COUNT=$(wc -l < build/sources.txt)
echo "Compiling $COUNT source files..."

# Compile
javac -encoding ISO-8859-1 \
  -sourcepath "$SRCDIR" \
  -d "$OUTDIR" \
  @build/sources.txt 2>&1 | tee build/compile.log

# Check for compilation errors (grep returns 0 if errors found)
if grep -q "^[0-9]* error" build/compile.log; then
  echo ""
  echo "BUILD FAILED: compilation errors detected"
  exit 1
fi

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
