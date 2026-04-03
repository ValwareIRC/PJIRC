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

import irc.gui.pgl.*;
import java.awt.*;
import java.util.*;
import irc.gui.pgl.interfaces.*;

/**
 * PGLLayout
 */
public class PGLLayout implements ILayout
{
  private Hashtable _components;

  /**
   * Create a new PGLLayout
   */
  public PGLLayout()
  {
    _components=new Hashtable();
  }

  public void initialize(PGLInitializationParameters params,Object userParams) throws PGLException
  {
  }

  public void uninitialize()
  {
    _components.clear();
  }

  public void addLayoutComponent(Component comp,Object constraint)
  {
    if(constraint==null)
    {
      constraint=new PGLLayoutConstraint(0,PGLLayoutConstraint.ANCHOR_WEST,PGLLayoutConstraint.SIZE_ABSOLUTE,
                                         0,PGLLayoutConstraint.ANCHOR_NORTH,PGLLayoutConstraint.SIZE_ABSOLUTE,
                                         0,PGLLayoutConstraint.ANCHOR_EAST,PGLLayoutConstraint.SIZE_ABSOLUTE,
                                         0,PGLLayoutConstraint.ANCHOR_SOUTH,PGLLayoutConstraint.SIZE_ABSOLUTE);
    }

    if(!(constraint instanceof PGLLayoutConstraint)) throw new RuntimeException("Invalid layout constraint "+constraint);
    _components.put(comp,constraint);
  }

  public void addLayoutComponent(String name,Component comp)
  {
    addLayoutComponent(comp,name);
  }

  public void layoutContainer(Container parent)
  {
    int width=parent.getSize().width;
    int height=parent.getSize().height;

    Enumeration e=_components.keys();
    while(e.hasMoreElements())
    {
      Component c=(Component)e.nextElement();
      PGLLayoutConstraint constraint=(PGLLayoutConstraint)_components.get(c);
      c.setBounds(constraint.getLeft(width),constraint.getTop(height),constraint.getWidth(width),constraint.getHeight(height));
    }
  }

  public Dimension minimumLayoutSize(Container parent)
  {
    return new Dimension(0,0);
  }

  public Dimension preferredLayoutSize(Container parent)
  {
    return new Dimension(0,0);
  }

  public Dimension maximumLayoutSize(Container parent)
  {
    return new Dimension(100000,100000);
  }

  public float getLayoutAlignmentX(Container target)
  {
    return 0;
  }

  public float getLayoutAlignmentY(Container target)
  {
    return 0;
  }

  public void invalidateLayout(Container target)
  {
  }

  public void removeLayoutComponent(Component comp)
  {
    _components.remove(comp);
  }
}
