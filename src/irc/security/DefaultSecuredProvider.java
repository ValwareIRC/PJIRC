/*****************************************************/
/*          This java file is a part of the          */
/*                                                   */
/*           -  Plouf's Java IRC Client  -           */
/*                                                   */
/*   Copyright (C)  2002 - 2004 Philippe Detournay   */
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

package irc.security;

import java.io.*;
import java.net.*;
import java.awt.*;
import irc.WebSocketSocket;

/**
 * Default secured provider.
 */
public class DefaultSecuredProvider implements SecuredProvider
{
  /**
   * When true, getSocket() returns a WebSocket-backed socket
   * for browser-based usage via CheerpJ.
   */
  public static boolean useWebSocket = false;

  public Socket getSocket(String host,Integer port) throws UnknownHostException,IOException
  {
    if (useWebSocket) {
      return new WebSocketSocket(host, port.intValue());
    }
    return new Socket(host,port.intValue());
  }

  public ServerSocket getServerSocket(Integer port) throws IOException
  {
    return new ServerSocket(port.intValue());
  }

  public FileInputStream getFileInputStream(File file) throws IOException
  {
    return new FileInputStream(file);
  }

  public FileOutputStream getFileOutputStream(File file) throws IOException
  {
    return new FileOutputStream(file);
  }

  public Integer getFileSize(File file)
  {
    return new Integer((int)file.length());
  }

  public File getLoadFile(String title)
  {
    Frame f=new Frame();
    FileDialog dlg=new FileDialog(f,title,FileDialog.LOAD);
    dlg.show();
    File ans=null;
    if(dlg.getFile()!=null) ans=new File(dlg.getDirectory()+dlg.getFile());
    dlg.hide();
    dlg.dispose();
    f.dispose();
    return ans;
  }

  public File getSaveFile(String title)
  {
    Frame f=new Frame();
    FileDialog dlg=new FileDialog(f,title,FileDialog.SAVE);
    dlg.show();
    File ans=null;
    if(dlg.getFile()!=null) ans=new File(dlg.getDirectory()+dlg.getFile());
    dlg.hide();
    dlg.dispose();
    f.dispose();
    return ans;
  }

  public File getSaveFile(String file,String title)
  {
    Frame f=new Frame();
    FileDialog dlg=new FileDialog(f,title,FileDialog.SAVE);
    dlg.setFile(file);
    dlg.show();
    File ans=null;
    if(dlg.getFile()!=null) ans=new File(dlg.getDirectory()+dlg.getFile());
    dlg.hide();
    dlg.dispose();
    f.dispose();
    return ans;
  }

  public InetAddress getLocalHost() throws UnknownHostException
  {
    InetAddress[] addresses=InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
    return addresses[addresses.length-1];
  }

  public String resolve(InetAddress addr)
  {
    return addr.getHostName();
  }

  public boolean tryProvider()
  {
    return true;
  }

  public String getName()
  {
    return "Default Security Provider";
  }

}

