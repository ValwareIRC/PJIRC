/*****************************************************/
/*          This java file is a part of the          */
/*                                                   */
/*           -  Plouf's Java IRC Client  -           */
/*                                                   */
/*   Copyright (C)  2002 - 2005 Philippe Detournay   */
/*                                                   */
/*         All contacts : theplouf@yahoo.com         */
/*                                                   */
/*  PJIRC is free software; you can redistribute     */
/*  it and/or modify it under the terms of the GNU   */
/*  General Public License as published by the       */
/*  Free Software Foundation; version 2 or later of  */
/*  the License.                                     */
/*                                                   */
/*  PJIRC is distributed in the hope that it will    */
/*  be useful, but WITHOUT ANY WARRANTY; without     */
/*  even the implied warranty of MERCHANTABILITY or  */
/*  FITNESS FOR A PARTICULAR PURPOSE.  See the GNU   */
/*  General Public License for more details.         */
/*                                                   */
/*  You should have received a copy of the GNU       */
/*  General Public License along with PJIRC; if      */
/*  not, write to the Free Software Foundation,      */
/*  Inc., 59 Temple Place, Suite 330, Boston,        */
/*  MA  02111-1307  USA                              */
/*                                                   */
/*****************************************************/
package irc;

import java.util.*;

/**
 * The server class.
 */
public interface Server
{
  /**
   * Say the specified string. This string is sent as it to the remote server.
   * @param destination message destination.
   * @param str message itself.
   */
  public void say(String destination,String str);

  /**
   * Execute the given command on the remote server.
   * @param str the command to execute.
   */
  public void execute(String str);

  /**
   * Send the given message to the server's status.
   * @param str string to send to the status.
   */
  public void sendStatusMessage(String str);

  /**
   * Get the nickname on this server.
   * @return the nickname on the server.
   */
  public String getNick();

  /**
   * Get the username on this server.
   * @return the username on this server.
   */
  public String getUserName();

  /**
   * Get the server name.
   * @return the server name.
   */
  public String getServerName();
  
  /**
   * Try to connect to the server, using default configuration.
   */
  public void connect();

  /**
   * Disconnect from the server.
   */
  public void disconnect();

  /**
   * Test whether the server is connected.
   * @return true if server is connected, false otherwise.
   */
  public boolean isConnected();

  /**
   * Get an enumeration of all the sources associated with this
   * server.
   * @return an enumeration of Source.
   */
  public Enumeration getSources();

  /**
   * Enumerate all sources, as if they were created and notified to the given
   * listener.
   * @param l listener to call.
   */
  public void enumerateSourcesAsCreated(ServerListener l);

  /**
   * Enumerate all sources, as if they were removed and notified to the given
   * listener.
   * @param l listener to call.
   */
  public void enumerateSourcesAsRemoved(ServerListener l);

  /**
   * Set the default output source this server should use if the destination
   * source is undefined.
   * @param source default source.
   */
  public void setDefaultSource(Source source);

  /**
   * Add a server listener.
   * @param l listener to add.
   */
  public void addServerListener(ServerListener l);

  /**
   * Remove a listener.
   * @param l listener to remove.
   */
  public void removeServerListener(ServerListener l);

  /**
   * Leave all sources, then leave the server.
   */
  public void leave();
}

