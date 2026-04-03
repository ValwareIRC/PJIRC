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

package irc.gui.pgl.items;

import java.awt.*;
import java.awt.event.*;

import irc.gui.pgl.interfaces.*;
import irc.gui.pgl.*;
import irc.*;

/**
 * PGLScrollBar
 */
public class PGLScrollBar implements IScrollBar,AdjustmentListener
{
  private ListenerGroup _listeners;
  private Scrollbar _bar;

  /**
   * Create a new PGLScrollBar
   */
  public PGLScrollBar()
  {
    _listeners=new ListenerGroup();
  }

  public void initialize(PGLInitializationParameters params,Object userParams) throws PGLException
  {
    _bar=new Scrollbar(Scrollbar.VERTICAL);
    _bar.addAdjustmentListener(this);
  }

  public void uninitialize()
  {
    _bar.removeAdjustmentListener(this);
    _bar=null;
    _listeners=null;
  }

  public void placeOn(Container container,Object constraint)
  {
    container.add(_bar,constraint);
  }

  public void removeFrom(Container container)
  {
    container.remove(_bar);
  }

  public void adjustmentValueChanged(AdjustmentEvent e)
  {
    _listeners.sendEventAsync("scrollBarValueChanged",new Object[] {this});
  }

  public void setMinimum(int minimum)
  {
    _bar.setMinimum(minimum);
  }

  public void setMaximum(int maximum)
  {
    _bar.setMaximum(maximum);
  }

  public void setValue(int value)
  {
    _bar.setValue(value);
  }

  public int getMinimum()
  {
    return _bar.getMinimum();
  }

  public int getMaximum()
  {
    return _bar.getMaximum();
  }

  public int getValue()
  {
    return _bar.getValue();
  }

  public void addScrollBarListener(IScrollBarListener lis)
  {
    _listeners.addListener(lis);
  }

  public void removeScrollBarListener(IScrollBarListener lis)
  {
    _listeners.removeListener(lis);
  }

}
