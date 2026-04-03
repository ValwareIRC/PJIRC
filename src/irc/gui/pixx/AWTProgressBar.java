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

package irc.gui.pixx;

import java.awt.*;

/**
 * A progress bar panel.
 */
public class AWTProgressBar extends Panel
{

  /**
   * 
   */
  private static final long serialVersionUID=1L;
  private double _v;
  private Color _c;

  /**
   * Set the value, between 0 and 1 inclusive.
   * @param v the value, between 0 and 1 inclusive.
   */
  public void setValue(double v)
  {
    _v=v;
  }

  /**
   * Set the display color.
   * @param c the display color.
   */
  public void setColor(Color c)
  {
    _c=c;
  }

  public void paint(Graphics g)
  {
    super.paint(g);
    int w=getSize().width;
    int h=getSize().height;

    int pos=(int)(_v*w);
    g.setColor(_c);
    g.fillRect(0,0,pos,h);
    g.setColor(Color.white);
    g.fillRect(pos,0,w-pos,h);
  }

}

