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

package irc.gui.pgl;

import irc.*;
import irc.gui.*;
import java.awt.*;
import java.util.*;
import java.io.*;

import irc.gui.pgl.interfaces.*;
import irc.gui.pgl.parser.*;

/**
 * PGL Interface.
 */
public class Interface extends IRCInterface
{
  private IInterface _inter;
  private Vector _tmpListeners;

  /**
   * Create a new Interface
   * @param config the global irc configuration.
   */
  public Interface(IRCConfiguration config)
  {
    super(config);
  }

  public void load()
  {
    super.load();
    
    try
    {
      Parser parser=new PGLParser();
      PGLElement[] elements=parser.parse(new BufferedReader(new InputStreamReader(new FileInputStream("Test.pgl"))));
      PGLElement root=null;
      for(int i=elements.length-1;i>=0;i--)
      {
        if(elements[i].getName().equals("Interface")) root=elements[i];
      }
      if(root==null) throw new PGLException("Interface element not found");

      PGLInitializationParameters params=new PGLInitializationParameters(null,new Hashtable());
      PGLGlobalParameter p=new PGLGlobalParameter(this,_ircConfiguration);
      _tmpListeners=new Vector();
      _inter=(IInterface)root.createInstance(params,p);
      for(int i=0;i<_tmpListeners.size();i++) getIInterface().addInterfaceListener((IInterfaceListener)_tmpListeners.elementAt(i));
      _tmpListeners=null;
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }

  /**
   * Add an interface listener.
   * @param lis the new listener to add.
   */
  public void addInterfaceListener(IInterfaceListener lis)
  {
    if(_tmpListeners==null)
    {
      getIInterface().addInterfaceListener(lis);
      return;
    }
    _tmpListeners.insertElementAt(lis,_tmpListeners.size());
  }
  
  /**
   * Remove an interface listener.
   * @param lis the listener to remove.
   */
  public void removeInterfaceListener(IInterfaceListener lis)
  {
    if(_tmpListeners==null)
    {
      getIInterface().removeInterfaceListener(lis);
      return;
    }
    for(int i=0;i<_tmpListeners.size();i++) if(_tmpListeners.elementAt(i)==lis)
    {
      _tmpListeners.removeElementAt(i);
      break;
    }
  }

  public void unload()
  {
    _inter.uninitialize();
    super.unload();
    _inter=null;
  }

  private IInterface getIInterface()
  {
    return _inter;
  }

  public void sourceCreated(Source source,Boolean bring)
  {
    getIInterface().notifySourceCreated(source,bring);

  } //sourceCreated

  /**
   * Notify this interface that an existing source has been removed. No further
   * call should be performed on this source.
   * @param source the removed source.
   */
  public void sourceRemoved(Source source)
  {
    getIInterface().notifySourceRemoved(source);
  } //sourceRemoved

  /**
   * Notify this interface that a new server has been created.
   * @param s the newly created server.
   */
  public void serverCreated(Server s)
  {
    getIInterface().notifyServerCreated(s);
  } //serverCreated

  /**
   * Notify this interface that an existing server has acheived connection to
   * its remote host.
   * @param s connected server.
   */
  public void serverConnected(Server s)
  {
    getIInterface().notifyServerConnected(s);
  } //serverConnected

  /**
   * Notify this interface that an existing server has disconnected from its
   * remote host.
   * @param s disconnected server.
   */
  public void serverDisconnected(Server s)
  {
    getIInterface().notifyServerDisconnected(s);
  } //serverDisconnected

  /**
   * Notify this interface that an existing server has been removed. No further
   * call should be performed on this server.
   * @param s removed server.
   */
  public void serverRemoved(Server s)
  {
    getIInterface().notifyServerRemoved(s);
  } //serverRemoved

  /**
   * Get the component associated with this interface.
   * @return the interface component.
   */
  public Component getComponent()
  {
    return getIInterface().getComponent();
  } //getComponent
} //Interface
