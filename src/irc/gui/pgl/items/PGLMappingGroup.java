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

import irc.gui.pgl.interfaces.*;
import irc.gui.pgl.*;

/**
 * PGLMappingGroup
 */
public class PGLMappingGroup implements IMappingGroup
{
  private PGLInitializationParameters _params;

  public String getString(String key)
  {
    return getString(key,null);
  }

  public IItem getItem(String key)
  {
    return getItem(key,null);
  }

  public String getString(String key,String def)
  {
    String ans=_params.getDescription().getString(key);
    if(ans!=null) return ans;
    return def;
  }

  public IItem getItem(String key,IItem def)
  {
    String name=getString(key);
    if(name==null) return def;
    IItem item=(IItem)_params.getGlobals().get(name);
    if(item==null) return def;
    return item;
  }

  public void initialize(PGLInitializationParameters params,Object userParams) throws PGLException
  {
    _params=params;
  }

  public void uninitialize()
  {
    _params=null;
  }

}
