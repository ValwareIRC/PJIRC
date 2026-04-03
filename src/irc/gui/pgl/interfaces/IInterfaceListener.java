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

package irc.gui.pgl.interfaces;

import irc.*;

/**
 * An interface listener.
 */
public interface IInterfaceListener
{
  /*public void sourcePanelCreated(ISourcePanel panel,IInterface inter);
  public void sourcePanelRemoved(ISourcePanel panel,IInterface inter);
  public void currentSourcePanelChanged(ISourcePanel panel,IInterface inter);*/

  /**
   * A new source has been created.
   * @param source create source.
   * @param bring should this source be brang.
   * @param inter source interface.
   */
  public void sourceCreated(Source source,Boolean bring,IInterface inter);
  /**
   * A source has been removed.
   * @param source removed source.
   * @param inter source interface.
   */
  public void sourceRemoved(Source source,IInterface inter);
  /**
   * A server has been created.
   * @param server created server.
   * @param inter source interface.
   */
  public void serverCreated(Server server,IInterface inter);
  /**
   * A server has been removed.
   * @param server removed server.
   * @param inter source interface.
   */
  public void serverRemoved(Server server,IInterface inter);
  /**
   * A server has connected.
   * @param server source server.
   * @param inter source interface.
   */
  public void serverConnected(Server server,IInterface inter);
  /**
   * A server has disconnected.
   * @param server source server.
   * @param inter source interface.
   */
  public void serverDisconnected(Server server,IInterface inter);

  /**
   * The user has typed an unknown command.
   * @param source source.
   * @param str typed string.
   */
  public void unhandledUserCommand(Source source,String str);
}
