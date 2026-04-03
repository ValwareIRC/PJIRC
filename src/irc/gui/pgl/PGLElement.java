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

import irc.gui.pgl.interfaces.*;

/**
 * The PGLElement is the main result of the parsing of a PGL file.
 */
public class PGLElement
{
  private String _name;
  private PGLElementDescription _description;
  private String _globalName;
  
  /**
   * Create a new PGLElement.
   * @param name element name.
   * @param description element description.
   * @param globalName optional global name.
   */
  public PGLElement(String name,PGLElementDescription description,String globalName)
  {
    setName(name);
    setDescription(description);
    setGlobalName(globalName);
  }
  
  /**
   * Creates a new instance of this PGLElement.
   * @param params global initialization parameters.
   * @param userParams context-specific parameters.
   * @return the new instance.
   * @throws PGLException
   */
  public IItem createInstance(PGLInitializationParameters params,Object userParams) throws PGLException
  {
    try
    {
      Class cl=Class.forName("irc.gui.pgl.items."+getDescription().getKey());
      IItem item=(IItem)cl.newInstance();
      PGLInitializationParameters parameters=new PGLInitializationParameters(getDescription(),params.getGlobals());
      item.initialize(parameters,userParams);
      if(getGlobalName()!=null) params.getGlobals().put(getGlobalName(),item);
      return item;
    }
    catch(Throwable ex)
    {
      ex.printStackTrace();
      throw new PGLException("Unable to create item "+getDescription().getKey()+": "+ex.toString());
    }
  }

  /**
   * Set the name.
   * @param name The name to set.
   */
  private void setName(String name)
  {
    _name=name;
  }

  /**
   * Get the name.
   * @return Returns the name.
   */
  public String getName()
  {
    return _name;
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
   * Set the globalName.
   * @param globalName The globalName to set.
   */
  private void setGlobalName(String globalName)
  {
    _globalName=globalName;
  }

  /**
   * Get the globalName.
   * @return Returns the globalName.
   */
  public String getGlobalName()
  {
    return _globalName;
  }
}
