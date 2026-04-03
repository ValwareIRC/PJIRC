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

import irc.*;
import irc.gui.pgl.*;
import irc.gui.pgl.interfaces.*;

/**
 * PGLSourceManagerRule
 */
public class PGLSourceManagerRule implements ISourceManagerRule
{
  private ListHandler _typeRule;
  private ListHandler _nameRule;
  private PGLElement _item;
  
  public boolean matches(Source src)
  {
    String type=src.getType();
    String name=src.getName();
    return _typeRule.inList(type)&&_nameRule.inList(name);
  }

  public PGLElement getItem()
  {
    return _item;
  }

  public void initialize(PGLInitializationParameters params,Object userParams) throws PGLException
  {
    String type=params.getDescription().getString("Type");
    String name=params.getDescription().getString("Name");
    if(type!=null)
      _typeRule=new ListHandler(type);
    else
      _typeRule=new ListHandler("all");
    
    if(name!=null)
      _nameRule=new ListHandler(name);
    else
      _nameRule=new ListHandler("all");
    
    _item=params.getDescription().getElement("Item");
  }

  public void uninitialize()
  {
  }

}
