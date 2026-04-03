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

## Web Browser Usage (Historical)

PJIRC was originally designed as a **Java applet** embedded in web pages — the classic way to offer IRC chat directly on a website during the 2000s. The applet had full TCP socket access through the browser's Java plugin, allowing it to connect to IRC servers directly.

### Why it no longer works in browsers

**Modern browsers cannot run PJIRC as a web client.** There are two compounding issues:

1. **Java plugin support is gone.** All major browsers removed NPAPI/Java plugin support (Chrome in 2015, Firefox in 2017). The `<applet>` tag is dead.

2. **Browsers block raw TCP sockets.** Even with WebAssembly-based Java runtimes like [CheerpJ](https://cheerpj.com/), IRC won't work because browsers only allow HTTP/HTTPS/WebSocket connections — not raw TCP to `irc.example.com:6667`. You'll get `java.net.UnknownHostException`. CheerpJ's own docs confirm that anything beyond same-origin HTTP requires a Tailscale VPN proxy layer.

### If you need web-based IRC

For modern web IRC, consider purpose-built clients like [Kiwi IRC](https://kiwiirc.com/) or [The Lounge](https://thelounge.chat/), which use WebSocket gateways to bridge browser connections to IRC servers.

### Legacy Applet Tag (historical reference)

For archival reference, PJIRC was originally embedded in web pages like this:

```html
<applet code="IRCApplet" archive="pjirc.jar" width="800" height="600">
  <param name="nick" value="WebUser">
  <param name="fullname" value="PJIRC User">
  <param name="host" value="irc.example.com">
  <param name="port" value="6667">
  <param name="gui" value="pixx">
  <param name="language" value="english">
</applet>
```

### Applet Parameters

All configuration options from `pjirc.cfg` can also be passed as applet `<param>` tags:

| Parameter                      | Description                                  |
| ------------------------------ | -------------------------------------------- |
| `nick`                         | IRC nickname                                 |
| `fullname`                     | Full name / real name                        |
| `host`                         | IRC server hostname                          |
| `port`                         | IRC server port (default: 6667)              |
| `gui`                          | GUI theme: `pixx`, `pgl`, or `sdk`           |
| `language`                     | Language file name (without `.lng`)           |
| `command1`, `command2`, ...    | Auto-run commands (e.g. `/join #channel`)    |
| `style:backgroundcolor`       | Background color (integer RGB)               |
| `style:foregroundcolor`        | Text color (integer RGB)                     |

### JavaScript API

When running as an applet (in legacy browsers), PJIRC exposes methods callable from JavaScript:

- `sendString(text)` — Send text to the current channel/query
- `sendString(server, type, name, cmd)` — Send command to a specific source
- `setFieldText(text)` / `getFieldText()` — Control the input field
- `validateText()` — Submit the current input (like pressing Enter)
- `requestSourceFocus()` — Focus the active IRC source

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
