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
chmod +x build.sh
./pjirc.sh -n Nick123 -s irc.libera.chat -p 6697 -t
```

By default it reads `pjirc.cfg` from the working directory. Edit `run/pjirc.cfg` to set your nickname, server, and other options.

### Command-line options

```
./pjirc.sh -n <nick> -s <server> -p <port> [-t] [-g <gui>]
./pjirc.sh -f <config-file>

# Examples:
./pjirc.sh -n MyNick -s irc.libera.chat -p 6667
./pjirc.sh -n MyNick -s irc.example.com -p 6697 -t
./pjirc.sh -f run/pjirc.cfg
```

| Flag | Description |
|------|-------------|
| `-n NICK` | IRC nickname (required) |
| `-s SERVER` | IRC server hostname (required) |
| `-p PORT` | IRC server port (required) |
| `-t` | Enable TLS/SSL (use for port 6697) |
| `-g GUI` | GUI theme: `pixx`, `pgl`, `sdk` (default: pixx) |
| `-f FILE` | Use a config file instead of flags |
| `-h` | Show help |

### GUI themes

- **pixx** — Full-featured AWT theme (default)
- **pgl** — PGL-based theme
- **sdk** — Minimal SDK theme

## Web Browser Usage

PJIRC was originally a Java applet for embedding IRC in web pages. While browser Java plugin support is long dead, PJIRC can now run in modern browsers using [CheerpJ](https://cheerpj.com/) (a WebAssembly-based JVM) with **WebSocket transport** to connect to IRC servers that support WebSocket (e.g. UnrealIRCd, InspIRCd with websocket module).

### Quick Start

1. Build PJIRC: `./build.sh`
2. Serve the `run/` directory via any HTTP server:
   ```bash
   cd run && python3 -m http.server 8090
   ```
3. Open `http://localhost:8090/chat.html` in a browser

### How it works

- **CheerpJ 4.2** runs the PJIRC Java applet inside the browser
- The `websocket=true` applet parameter activates WebSocket transport
- Instead of raw TCP sockets (blocked by browsers), PJIRC connects via `wss://` to the IRC server
- JavaScript native method implementations bridge browser's `WebSocket` API into Java's `InputStream`/`OutputStream`

### Configuring the applet

Edit `chat.html` to change the server, nickname, and other settings via `<param>` tags:

```html
<applet archive="pjirc.jar" code="IRCApplet" width="900" height="600">
  <param name="nick" value="PJIRCWebUser">
  <param name="host" value="irc.unrealircd.org">
  <param name="port" value="443">
  <param name="gui" value="pixx">
  <param name="language" value="english">
  <param name="websocket" value="true">
</applet>
```

The `websocket` parameter is the key addition — it tells PJIRC to use `wss://` WebSocket connections instead of raw TCP.

### Requirements

- The IRC server **must** support WebSocket connections (e.g. UnrealIRCd with `listen` block for websocket on the target port)
- The server's TLS certificate must be valid (browsers enforce this for `wss://`)

### Legacy applet mode

For archival reference, the original raw-TCP applet mode no longer works in any modern browser. The `websocket=true` parameter is what makes PJIRC viable as a web client again.

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
| `websocket`                    | Use WebSocket transport (`true`/`false`)     |
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
    WebSocketSocket.java  WebSocket transport for browser mode
    gui/pixx/     Pixx GUI theme
    gui/pgl/      PGL GUI theme  
    gui/sdk/      SDK GUI theme
    dcc/          DCC file/chat support
    security/     Security providers
    plugin/       Plugin framework
run/              Runtime directory
  pjirc.jar       Built application
  pjirc.cfg       Configuration file
  chat.html       Browser-based client (CheerpJ + WebSocket)
  english.lng     English language strings
build.sh          Build script
```

## License

GNU General Public License v2.0 — see source file headers for details.

## Credits

- **Philippe Detournay** (theplouf) — Original author
- Restored from SourceForge project: https://sourceforge.net/projects/pjirc/
