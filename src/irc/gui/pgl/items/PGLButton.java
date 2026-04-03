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
 * PGLButton
 */
public class PGLButton extends Button implements IButton
{
  /**
   * 
   */
  private static final long serialVersionUID=1L;

  /**
   * Create a new PGLButton
   */
  public PGLButton()
  {
    super();
  }

  public void placeOn(Container container,Object constraint)
  {
    container.add(this,constraint);
  }

  public void removeFrom(Container container)
  {
    container.remove(this);
  }

  public void initialize(PGLInitializationParameters params,Object userParams) throws PGLException
  {
    String text=params.getDescription().getString("Text");
    if(text!=null) setLabel(text);
  }

  public void uninitialize()
  {
  }
}
