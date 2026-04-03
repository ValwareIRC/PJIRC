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

package irc.gui.pgl;

import java.util.*;

/**
 * PGLInitializationParameters.
 */
public class PGLInitializationParameters
{
  private PGLElementDescription _description;
  private Hashtable _globals;

  /**
   * Create a new PGLInitializationParameters.
   * @param description
   * @param globals
   */
  public PGLInitializationParameters(PGLElementDescription description,Hashtable globals)
  {
    setDescription(description);
    setGlobals(globals);
  }

  /**
   * Create a new PGLInitializationParameters.
   * @param old old instance.
   */
  public PGLInitializationParameters(PGLInitializationParameters old)
  {
    setDescription(old.getDescription());
    setGlobals(old.getGlobals());
  }

  /**
   * Set the description.
   * @param description The description to set.
   */
  private void setDescription(PGLElementDescription description)
  {
    _description=description;
  }

  /**
   * Get the description.
   * @return Returns the description.
   */
  public PGLElementDescription getDescription()
  {
    return _description;
  }

  /**
   * Set the globals.
   * @param globals The globals to set.
   */
  private void setGlobals(Hashtable globals)
  {
    _globals=globals;
  }

  /**
   * Get the globals.
   * @return Returns the globals.
   */
  public Hashtable getGlobals()
  {
    return _globals;
  }
}
