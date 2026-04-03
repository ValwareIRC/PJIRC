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
import irc.gui.pgl.*;
import irc.gui.pgl.interfaces.*;

/**
 * PGLPanel
 */
public class PGLPanel extends Panel implements IPanel
{
  /**
   * 
   */
  private static final long serialVersionUID=1L;
  private IPanelElement[] _elements;
  private ILayout _layout;

  /**
   * Create a new PGLPanel
   */
  public PGLPanel()
  {
  }

  public void placeOn(Container container,Object constraint)
  {
    container.add(this,constraint);
  }

  public void removeFrom(Container container)
  {
    container.remove(this);
  }

  public void setLayout(ILayout layout)
  {
    super.setLayout(layout);
  }

  public void add(IVisibleItem item,ILayoutConstraint constraint)
  {
    item.placeOn(this,constraint);
  }

  public void remove(IVisibleItem item)
  {
    item.removeFrom(this);
  }

  public void initialize(PGLInitializationParameters params,Object userParams) throws PGLException
  {
    PGLElement layoutElement=params.getDescription().getElement("Layout");
    if(layoutElement!=null)
    {
      _layout=(ILayout)layoutElement.createInstance(params,userParams);
      setLayout(_layout);
    }

    PGLElement elements[]=params.getDescription().getAllElements("Element");
    _elements=new IPanelElement[elements.length];
    for(int i=0;i<elements.length;i++)
    {
      _elements[i]=(IPanelElement)elements[i].createInstance(params,userParams);
    }

    for(int i=_elements.length-1;i>=0;i--)
      add(_elements[i].getComponent(),_elements[i].getConstraint());
  }

  public void uninitialize()
  {
    for(int i=0;i<_elements.length-1;i++)
    {
      remove(_elements[i].getComponent());
      _elements[i].uninitialize();
    }

    _elements=null;
    removeAll();

    _layout.uninitialize();
    _layout=null;
  }

}
