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
import irc.gui.pgl.interfaces.*;

/**
 * PGLPanelElement
 */
public class PGLPanelElement implements IPanelElement
{
  private IVisibleItem _component;
  private ILayoutConstraint _layout;

  public void initialize(PGLInitializationParameters params,Object userParams) throws PGLException
  {
    PGLElement component=params.getDescription().getElement("Component");
    if(component==null) throw new PGLException("Mandatory 'Component' parameter not found in PGLPanelElement");
    _component=(IVisibleItem)component.createInstance(params,userParams);

    PGLElement layout=params.getDescription().getElement("LayoutConstraint");
    if(layout!=null) _layout=(ILayoutConstraint)layout.createInstance(params,userParams);
  }

  public void uninitialize()
  {
    _component.uninitialize();
    _layout.uninitialize();
    _component=null;
    _layout=null;
  }

  public IVisibleItem getComponent()
  {
    return _component;
  }

  public ILayoutConstraint getConstraint()
  {
    return _layout;
  }

}
