/*****************************************************/
/*          This java file is a part of the          */
/*                                                   */
/*           -  Plouf's Java IRC Client  -           */
/*                                                   */
/*   WebSocket transport for browser-based usage     */
/*   via CheerpJ. Bridges browser WebSocket API      */
/*   into Java InputStream/OutputStream.             */
/*                                                   */
/*****************************************************/

package irc;

import java.io.*;
import java.net.*;

/**
 * A Socket-like wrapper around a browser WebSocket connection.
 * Used when PJIRC runs as an applet via CheerpJ in modern browsers.
 *
 * All I/O is handled by JavaScript native methods:
 * - nativeConnect: creates WebSocket, blocks until connected
 * - nativeRead: blocks until a message is available, returns it
 * - nativeSend: sends a string via WebSocket
 * - nativeClose: closes the WebSocket
 */
public class WebSocketSocket extends Socket
{
  private boolean _closed;
  private int _wsId;
  private InputStream _in;
  private OutputStream _out;

  private static int _nextId = 0;

  /**
   * Create a new WebSocketSocket connecting to the given host and port.
   * @param host the IRC server host
   * @param port the IRC server port (e.g. 443 for wss)
   */
  public WebSocketSocket(String host, int port) throws IOException
  {
    _wsId = _nextId++;
    _closed = false;

    System.out.println("[WebSocketSocket] Connecting to " + host + ":" + port + " (id=" + _wsId + ")");

    // This blocks (async in JS) until the WebSocket is open
    String result = nativeConnect(_wsId, host, port);
    if (result != null && result.startsWith("ERROR:"))
    {
      _closed = true;
      throw new IOException("WebSocket connection failed: " + result.substring(6));
    }

    System.out.println("[WebSocketSocket] Connected (id=" + _wsId + ")");

    // Create InputStream that reads from WebSocket via native calls
    _in = new WebSocketInputStream(_wsId);

    // Create OutputStream that sends via WebSocket
    _out = new WebSocketOutputStream(_wsId);
  }

  // --- Native methods implemented in JavaScript via CheerpJ ---

  /**
   * Create a WebSocket connection. Blocks until connected.
   * @return null on success, "ERROR:message" on failure
   */
  public static native String nativeConnect(int wsId, String host, int port);

  /**
   * Read the next message from the WebSocket. Blocks until data available.
   * @return the message string, or null if connection closed
   */
  public static native String nativeRead(int wsId);

  /**
   * Send a string via WebSocket.
   */
  public static native void nativeSend(int wsId, String data);

  /**
   * Close the WebSocket.
   */
  public static native void nativeClose(int wsId);

  // --- InputStream wrapping nativeRead ---

  private static class WebSocketInputStream extends InputStream
  {
    private int _id;
    private byte[] _buf;
    private int _pos;
    private boolean _eof;

    WebSocketInputStream(int id)
    {
      _id = id;
      _buf = null;
      _pos = 0;
      _eof = false;
    }

    private boolean fillBuffer() throws IOException
    {
      if (_eof) return false;
      // nativeRead blocks (async JS) until a message arrives or close
      String msg = nativeRead(_id);
      if (msg == null)
      {
        _eof = true;
        return false;
      }
      try
      {
        _buf = msg.getBytes("UTF-8");
      }
      catch(UnsupportedEncodingException e)
      {
        _buf = msg.getBytes();
      }
      _pos = 0;
      return true;
    }

    public int read() throws IOException
    {
      if ((_buf == null || _pos >= _buf.length) && !fillBuffer())
        return -1;
      return _buf[_pos++] & 0xFF;
    }

    public int read(byte[] b, int off, int len) throws IOException
    {
      if ((_buf == null || _pos >= _buf.length) && !fillBuffer())
        return -1;
      int avail = _buf.length - _pos;
      int n = Math.min(len, avail);
      System.arraycopy(_buf, _pos, b, off, n);
      _pos += n;
      return n;
    }

    public int available()
    {
      if (_buf != null && _pos < _buf.length)
        return _buf.length - _pos;
      return 0;
    }

    public void close() throws IOException
    {
      _eof = true;
    }
  }

  // --- OutputStream wrapping nativeSend ---

  private static class WebSocketOutputStream extends OutputStream
  {
    private int _id;
    private ByteArrayOutputStream _buf;

    WebSocketOutputStream(int id)
    {
      _id = id;
      _buf = new ByteArrayOutputStream(512);
    }

    public void write(int b) throws IOException
    {
      _buf.write(b);
    }

    public void write(byte[] data, int off, int len) throws IOException
    {
      _buf.write(data, off, len);
    }

    public void flush() throws IOException
    {
      if (_buf.size() > 0)
      {
        String s;
        try
        {
          s = new String(_buf.toByteArray(), "UTF-8");
        }
        catch(UnsupportedEncodingException e)
        {
          s = new String(_buf.toByteArray());
        }
        _buf.reset();
        nativeSend(_id, s);
      }
    }

    public void close() throws IOException
    {
      flush();
    }
  }

  // --- Socket method overrides ---

  public InputStream getInputStream() throws IOException
  {
    return _in;
  }

  public OutputStream getOutputStream() throws IOException
  {
    return _out;
  }

  public boolean isConnected()
  {
    return !_closed;
  }

  public boolean isClosed()
  {
    return _closed;
  }

  public synchronized void close() throws IOException
  {
    if (!_closed)
    {
      _closed = true;
      nativeClose(_wsId);
      if (_in != null) _in.close();
    }
  }

  public int getLocalPort()
  {
    return 0;
  }

  public InetAddress getLocalAddress()
  {
    return null;
  }
}
