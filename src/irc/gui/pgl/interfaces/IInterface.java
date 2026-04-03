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

import java.util.*;
import irc.*;
import java.awt.*;

/**
 * The root PGL interface.
 */
public interface IInterface extends IVisibleItem
{
  /**
   * Get the current selected source panel.
   * @return current selected source panel, or null if none is defined.
   */
  public ISourcePanel getCurrentSourcePanel();
  /**
   * Set the current source panel.
   * @param panel new source panel to be activated as current source.
   */
  public void setCurrentSourcePanel(ISourcePanel panel);
  /**
   * Get all source panels.
   * @return all source panels.
   */
  public Enumeration getAllSourcePanels();

  /**
   * Add a listener.
   * @param listener new listener to add.
   */
  public void addInterfaceListener(IInterfaceListener listener);
  /**
   * Remove a listener.
   * @param listener listener to remove.
   */
  public void removeInterfaceListener(IInterfaceListener listener);

  /**
   * Notify this interface that a new source has been created.
   * @param source created source.
   * @param bring true if the source should be brang.
   */
  public void notifySourceCreated(Source source,Boolean bring);
  /**
   * Notify this interface that a source has been removed.
   * @param source removed source.
   */
  public void notifySourceRemoved(Source source);
  /**
   * Notify this interface that a server has been created.
   * @param server created server.
   */
  public void notifyServerCreated(Server server);
  /**
   * Notify this interface that a server has been removed.
   * @param server removed server.
   */
  public void notifyServerRemoved(Server server);
  /**
   * Notify this interface that a server has connected.
   * @param server connected server.
   */
  public void notifyServerConnected(Server server);
  /**
   * Notify this interface that a server has disconnected.
   * @param server disconnected server.
   */
  public void notifyServerDisconnected(Server server);

  /**
   * Get the source panel associated to the given source, or null if
   * not found.
   * @param source source.
   * @return source panel, or null.
   */
  public ISourcePanel getSourcePanelFromSource(Source source);
  
  /**
   * Get the component of this interface.
   * @return the interface's component.
   */
  public Component getComponent();
}
