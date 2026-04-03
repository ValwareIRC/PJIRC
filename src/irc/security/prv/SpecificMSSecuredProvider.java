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

package irc.security.prv;

import irc.security.*;
import java.io.*;
import java.net.*;
import java.awt.*;

import com.ms.security.*;

/**
 * Specific secured provider to use with microsoft internet explorer.
 */
public class SpecificMSSecuredProvider implements SecuredProvider
{

  public Socket getSocket(String host,Integer port) throws UnknownHostException,IOException
  {
    try
    {
      PolicyEngine.assertPermission(PermissionID.NETIO);
      return new Socket(host,port.intValue());
    }
    catch(Throwable e)
    {
      return new Socket(host,port.intValue());
    }
  }

  public ServerSocket getServerSocket(Integer port) throws IOException
  {
    try
    {
      PolicyEngine.assertPermission(PermissionID.NETIO);
      return new SpecificMSSecuredServerSocket(port.intValue());
    }
    catch(Throwable e)
    {
      return new SpecificMSSecuredServerSocket(port.intValue());
    }
  }

  public FileInputStream getFileInputStream(File file) throws IOException
  {
    try
    {
      PolicyEngine.assertPermission(PermissionID.FILEIO);
      return new FileInputStream(file);
    }
    catch(Throwable e)
    {
      return new FileInputStream(file);
    }
  }

  public Integer getFileSize(File file)
  {
    try
    {
      PolicyEngine.assertPermission(PermissionID.FILEIO);
      return new Integer((int)file.length());
    }
    catch(Throwable e)
    {
      return new Integer((int)file.length());
    }
  }

  public FileOutputStream getFileOutputStream(File file) throws IOException
  {
    try
    {
      PolicyEngine.assertPermission(PermissionID.FILEIO);
      return new FileOutputStream(file);
    }
    catch(Throwable e)
    {
      return new FileOutputStream(file);
    }
  }

  /**
   * Open a file dialog.
   * @param top parent window.
   * @param title dialog title.
   * @param type dialog type.
   * @return newly created file dialog.
   */
  public FileDialog getFileDialog(Frame top,String title,int type)
  {
    try
    {
      PolicyEngine.assertPermission(PermissionID.UI);
      return new FileDialog(top,title,type);
    }
    catch(Throwable e)
    {
      return new FileDialog(top,title,type);
    }
  }

  public File getLoadFile(String title)
  {
    try
    {
      PolicyEngine.assertPermission(PermissionID.UI);
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
    catch(Throwable e)
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
  }

  public File getSaveFile(String title)
  {
    try
    {
      PolicyEngine.assertPermission(PermissionID.UI);
      Frame f=new Frame();
      FileDialog dlg=new FileDialog(f,title,FileDialog.SAVE);
      dlg.show();
      File ans=new File(dlg.getDirectory()+dlg.getFile());
      dlg.hide();
      dlg.dispose();
      f.dispose();
      return ans;
    }
    catch(Throwable e)
    {
      Frame f=new Frame();
      FileDialog dlg=new FileDialog(f,title,FileDialog.SAVE);
      dlg.show();
      File ans=new File(dlg.getDirectory()+dlg.getFile());
      dlg.hide();
      dlg.dispose();
      f.dispose();
      return ans;
    }
  }

  public File getSaveFile(String file,String title)
  {
    try
    {
      PolicyEngine.assertPermission(PermissionID.UI);
      Frame f=new Frame();
      FileDialog dlg=new FileDialog(f,title,FileDialog.SAVE);
      dlg.setFile(file);
      dlg.show();
      File ans=new File(dlg.getDirectory()+dlg.getFile());
      dlg.hide();
      dlg.dispose();
      f.dispose();
      return ans;
    }
    catch(Throwable e)
    {
      Frame f=new Frame();
      FileDialog dlg=new FileDialog(f,title,FileDialog.SAVE);
      dlg.setFile(file);
      dlg.show();
      File ans=new File(dlg.getDirectory()+dlg.getFile());
      dlg.hide();
      dlg.dispose();
      f.dispose();
      return ans;
    }
  }
  
  public InetAddress getLocalHost() throws UnknownHostException
  {
    try
    {
      PolicyEngine.assertPermission(PermissionID.NETIO);
      InetAddress[] addresses=InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
      return addresses[addresses.length-1];
    }
    catch(Throwable e)
    {
      InetAddress[] addresses=InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
      return addresses[addresses.length-1];
    }
  }

  public String resolve(InetAddress addr)
  {
    try
    {
      PolicyEngine.assertPermission(PermissionID.NETIO);
      return addr.getHostName();
    }
    catch(Throwable e)
    {
      return addr.getHostName();
    }

  }

  public boolean tryProvider()
  {
    try
    {
      PolicyEngine.assertPermission(PermissionID.FILEIO);
      PolicyEngine.assertPermission(PermissionID.NETIO);
      PolicyEngine.assertPermission(PermissionID.UI);
      return true;
    }
    catch(Throwable e)
    {
      return false;
    }
  }

  public String getName()
  {
    return "Microsoft Internet Explorer Security Provider";
  }
}

