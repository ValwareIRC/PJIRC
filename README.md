# PJIRC — Plouf's Java IRC Client

A full-featured Java-based IRC client, originally created by Philippe Detournay (2002–2005).

This is a source restoration from the [SourceForge CVS repository](https://sourceforge.net/projects/pjirc/), compiled and runnable on modern JDKs.

## Building

Requires Java 8+ (tested on OpenJDK 17).

```bash
chmod +x build.sh
./build.sh
```

This compiles all sources and produces `run/pjirc.jar`.

## Running

```bash
cd run
java -jar pjirc.jar
```

By default it reads `pjirc.cfg` from the working directory. Edit `run/pjirc.cfg` to set your nickname, server, and other options.

### Command-line options

```
# Config file mode (default: pjirc.cfg)
java -jar pjirc.jar -f <config-file>

# Direct parameters mode
java -jar pjirc.jar -p <nick> <fullname> <server> <gui>
# Example: java -jar pjirc.jar -p MyNick "My Name" irc.libera.chat pixx
```

### GUI themes

- **pixx** — Full-featured AWT theme (default)
- **pgl** — PGL-based theme
- **sdk** — Minimal SDK theme

## Configuration

See `run/pjirc.cfg` for available options. Key settings:

| Parameter | Description |
|-----------|-------------|
| `nick` | IRC nickname |
| `fullname` | Full name / real name |
| `host` | IRC server hostname |
| `port` | IRC server port (default: 6667) |
| `gui` | GUI theme: `pixx`, `pgl`, or `sdk` |
| `language` | Language file name (without `.lng`) |

## Project Structure

```
src/              Java source (package root)
  IRCApplet.java  Applet entry point
  irc/            Core IRC engine
    gui/pixx/     Pixx GUI theme
    gui/pgl/      PGL GUI theme  
    gui/sdk/      SDK GUI theme
    dcc/          DCC file/chat support
    security/     Security providers
    plugin/       Plugin framework
run/              Runtime directory
  pjirc.jar       Built application
  pjirc.cfg       Configuration file
  english.lng     English language strings
build.sh          Build script
```

## License

GNU General Public License v2.0 — see source file headers for details.

## Credits

- **Philippe Detournay** (theplouf) — Original author
- Restored from SourceForge project: https://sourceforge.net/projects/pjirc/
