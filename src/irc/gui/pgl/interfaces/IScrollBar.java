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
 * A generic scrollbar.
 */
public interface IScrollBar extends IVisibleItem
{
  /**
   * Set the minimum value.
   * @param minimum minimum value.
   */
  public void setMinimum(int minimum);
  /**
   * Set the maximum value.
   * @param maximum maximum value.
   */
  public void setMaximum(int maximum);
  /**
   * Set the current value.
   * @param value current value.
   */
  public void setValue(int value);
  /**
   * Get the minimum value.
   * @return the minimum value.
   */
  public int getMinimum();
  /**
   * Get the maximum value.
   * @return the maximum value.
   */
  public int getMaximum();
  /**
   * Get the current value.
   * @return the current value.
   */
  public int getValue();
  /**
   * Add a scroller listener.
   * @param lis new listener to add.
   */
  public void addScrollBarListener(IScrollBarListener lis);
  /**
   * Remove a scroller listener.
   * @param lis listener to remove.
   */
  public void removeScrollBarListener(IScrollBarListener lis);
}
