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

## Running in a Web Browser

PJIRC was originally designed as a **Java applet** embedded in web pages — the classic way to offer IRC chat directly on a website. Modern browsers no longer support Java applets (NPAPI plugin support was removed from Chrome in 2015 and Firefox in 2017), but you can still run PJIRC in the browser using [CheerpJ](https://cheerpj.com/), a WebAssembly-based JVM that requires no plugins or client-side Java installation.

### Using CheerpJ

1. Place `pjirc.jar` and your language files (`english.lng`, `pixx-english.lng`, etc.) in a directory served by a web server.

2. Create an HTML page like this:

```html
<!DOCTYPE html>
<html>
<head>
  <title>PJIRC Web Chat</title>
  <script src="https://cjrtnc.leaningtech.com/4.0/cj3loader.js"></script>
</head>
<body>
  <cheerpj-applet
    archive="pjirc.jar"
    code="IRCApplet"
    width="800"
    height="600"
    param_nick="WebUser"
    param_fullname="PJIRC Web User"
    param_host="irc.libera.chat"
    param_port="6667"
    param_gui="pixx"
    param_language="english">
  </cheerpj-applet>
</body>
</html>
```

3. Serve the directory over HTTP(S). For quick local testing:

```bash
cd run
python3 -m http.server 8080
# Then open http://localhost:8080/chat.html
```

### Traditional Applet Tag (legacy reference)

For historical reference, PJIRC was originally embedded using:

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

> **Note:** This only works in very old browsers with Java plugin support. Use CheerpJ for modern browsers.

### Applet Parameters

All configuration options from `pjirc.cfg` can be passed as applet `<param>` tags (or `param_` attributes with CheerpJ). Key parameters:

| Parameter | Description |
|-----------|-------------|
| `nick` | IRC nickname |
| `fullname` | Full name / real name |
| `host` | IRC server hostname |
| `port` | IRC server port (default: 6667) |
| `gui` | GUI theme: `pixx`, `pgl`, or `sdk` |
| `language` | Language file name (without `.lng`) |
| `command1`, `command2`, ... | Auto-run commands (e.g. `/join #channel`) |
| `style:backgroundcolor` | Background color (integer RGB) |
| `style:foregroundcolor` | Text color (integer RGB) |

### JavaScript API

When running as an applet, PJIRC exposes methods callable from JavaScript:

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
