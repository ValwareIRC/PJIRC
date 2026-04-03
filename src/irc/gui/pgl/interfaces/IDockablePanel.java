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

/**
 * A dockable panel is a panel that can be docked/undocked in a new window.
 * It can only contain one single component.
 */
public interface IDockablePanel extends IVisibleItem
{
  /**
   * Dock the panel, if it is undocked.
   */
  public void dock();
  /**
   * Undock the panel, if it is docked.
   */
  public void undock();
  /**
   * Check whether the panel is docked or not.
   * @return true if panel is docked, false otherwise.
   */
  public boolean isDocked();

  /**
   * Get the underlying component.
   * @return the underlying component.
   */
  public IVisibleItem getComponent();

  /**
   * Add a listener.
   * @param listener listener to add.
   */
  public void addDockablePanelListener(IDockablePanelListener listener);
  
  /**
   * Remove a listener.
   * @param listener listener to remove.
   */
  public void removeDockablePanelListener(IDockablePanelListener listener);
}
