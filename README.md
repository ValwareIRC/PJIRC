# PJIRC — Plouf's Java IRC Client

A full-featured Java-based IRC client, originally created by Philippe Detournay (2002–2005).

This is a source restoration from the [SourceForge CVS repository](https://sourceforge.net/projects/pjirc/), compiled and runnable on modern JDKs.

## Building

Requires **JDK 8+** (tested on OpenJDK 17).

```bash
chmod +x build.sh
./build.sh
```

This compiles all sources and produces `run/pjirc.jar`.

### JDK compatibility notes

| JDK Version | Desktop mode | Browser/CheerpJ mode |
|-------------|-------------|----------------------|
| 8–22        | Full support | Full support |
| 23+         | Full support | Full support |

JDK 23 removed `java.applet` ([JEP 471](https://openjdk.org/jeps/471)). The build script automatically detects this and excludes the legacy applet-related classes. Both desktop and browser/CheerpJ modes work on all JDK versions — the browser path uses `cheerpjRunMain()` via `IRCApplication.main()` instead of the Applet API.

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

- **CheerpJ 4.2** runs `IRCApplication.main()` inside the browser via `cheerpjRunMain()`
- The `-ws` flag activates WebSocket transport
- Instead of raw TCP sockets (blocked by browsers), PJIRC connects via `wss://` to the IRC server
- JavaScript native method implementations bridge browser's `WebSocket` API into Java's `InputStream`/`OutputStream`
- No `java.applet.Applet` dependency — works with JARs built on any JDK version

### Configuring the web client

Edit the configuration section in `chat.html`:

```js
// ---- Configuration ----
const NICK     = "PJIRCWebUser";
const FULLNAME = "PJIRC Web User";
const HOST     = "irc.unrealircd.org";
const PORT     = "443";
const GUI      = "pixx";
// -----------------------
```

These values are passed to `IRCApplication.main()` via the `-p` flag with `-ws` for WebSocket transport.

### Requirements

- The IRC server **must** support WebSocket connections (e.g. UnrealIRCd with `listen` block for websocket on the target port)
- The server's TLS certificate must be valid (browsers enforce this for `wss://`)

### Legacy applet mode

The original `IRCApplet` class (extending `java.applet.Applet`) is still included for historical reference and compiles on JDK 8–22. On JDK 23+, the applet classes are automatically excluded by the build script since `java.applet` was removed. The modern browser path uses `cheerpjRunMain()` with `IRCApplication.main()` and does not depend on the Applet API.

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
