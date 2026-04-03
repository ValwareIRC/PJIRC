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

package irc.gui.sdk;

import irc.*;
import irc.gui.*;
import java.awt.*;
import java.util.*;

/**
 * Simple PJIRC Interface.
 */
public class Interface extends IRCInterface
{
  /**
   * Some display area for the main component.
   */
  private TextArea _text;
  /**
   * The source dictionnary. Allow us to get the source window for
   * the given source.
   */
  private Hashtable _sources;

  /**
   * Create a new SDK Interface.
   * @param config the global irc configuration.
   */
  public Interface(IRCConfiguration config)
  {
    super(config);
  }

  /**
   * Load this interface.
   */
  public void load()
  {
    super.load();
    _text=new TextArea();
    _sources=new Hashtable();

    print("PJIRC v"+_ircConfiguration.getVersion());
  } //Interface


  /**
   * Release any ressources used by this instance. This should be the last
   * method call on the instance.
   */
  public void unload()
  {
    super.unload();
    _text=null;
    _sources.clear();
    _sources=null;
  } //release

  /**
   * Add a new line in the display area.
   * @param txt the content of the new line.
   */
  private void print(String txt)
  {
    _text.append(txt);
    _text.append("\n");
    System.out.println(txt);
  } //print

  /**
   * Notify this interface that a new source has been created.
   * @param source the newly created source.
   * @param bring is true if the newly created source should gain immediate
   * focus, false is no particular action is to be taken.
   */
  public void sourceCreated(Source source,Boolean bring)
  {
    print("Source created : "+source);
    SimpleAWTSource awtSource;

    //Do we know this source type?
    if(source instanceof Channel)
    {
      //This is a channel...
      awtSource=new SimpleAWTChannel(_ircConfiguration,(Channel)source);
    }
    else if(source instanceof Status)
    {
      //This is a status..
      awtSource=new SimpleAWTStatus(_ircConfiguration,(Status)source);
    }
    else
    {
      //Use a default awt source...
      awtSource=new SimpleAWTSource(_ircConfiguration,source);
    }

    //Remember the link between the source and the awt source.
    _sources.put(source,awtSource);
  } //sourceCreated

  /**
   * Notify this interface that an existing source has been removed. No further
   * call should be performed on this source.
   * @param source the removed source.
   */
  public void sourceRemoved(Source source)
  {
    //What was the awt source associated with this source?
    SimpleAWTSource awt=(SimpleAWTSource)_sources.get(source);
    //Just leave it.
    awt.leave();
    print("Source removed : "+source);
  } //sourceRemoved

  /**
   * Notify this interface that a new server has been created.
   * @param s the newly created server.
   */
  public void serverCreated(Server s)
  {
    print("Server created : "+s);
  } //serverCreated

  /**
   * Notify this interface that an existing server has acheived connection to
   * its remote host.
   * @param s connected server.
   */
  public void serverConnected(Server s)
  {
    print("Server connected : "+s);
  } //serverConnected

  /**
   * Notify this interface that an existing server has disconnected from its
   * remote host.
   * @param s disconnected server.
   */
  public void serverDisconnected(Server s)
  {
    print("Server disconnected : "+s);
  } //serverDisconnected

  /**
   * Notify this interface that an existing server has been removed. No further
   * call should be performed on this server.
   * @param s removed server.
   */
  public void serverRemoved(Server s)
  {
    print("Server removed : "+s);
  } //serverRemoved

  /**
   * Get the component associated with this interface.
   * @return the interface component.
   */
  public Component getComponent()
  {
    return _text;
  } //getComponent
} //Interface
