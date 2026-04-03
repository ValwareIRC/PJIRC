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
 * The PGLElementDescription contains an attribute of a pgl element.
 */
public class PGLElementDescription
{
  private String _key;
  private String _string;
  private PGLElement[] _elements;

  /**
   * Create a new PGLElementDescription
   * @param key attribute key.
   * @param string attribute strin.
   * @param elements attribute sub-elements.
   */
  public PGLElementDescription(String key,String string,PGLElement[] elements)
  {
    PGLElement[] copy=new PGLElement[elements.length];
    for(int i=0;i<copy.length;i++) copy[i]=new PGLElement(elements[i].getName(),elements[i].getDescription(),elements[i].getGlobalName());
    setKey(key);
    setString(string);
    setElements(copy);
  }
  
  /**
   * Get all elements matching the given name.
   * @param name name to match.
   * @return an array of all matching elements.
   */
  public PGLElement[] getAllElements(String name)
  {
    Vector v=new Vector();

    for(int i=0;i<getElements().length;i++)
    {
      if(getElements()[i].getName().equals(name)) v.insertElementAt(getElements()[i],v.size());
    }

    PGLElement[] ans=new PGLElement[v.size()];
    for(int i=0;i<ans.length;i++) ans[i]=(PGLElement)v.elementAt(i);
    return ans;
  }

  /**
   * Get the first element matching the given name, or null if none.
   * @param name name to match.
   * @return first matching element, or null if none.
   */
  public PGLElement getElement(String name)
  {
    for(int i=getElements().length-1;i>=0;i--)
    {
      if(getElements()[i].getName().equals(name)) return getElements()[i];
    }
    return null;
  }

  /**
   * Get the string value of the description for the given element.
   * @param name name to match.
   * @return the value string of the matching element, or null if not found.
   */
  public String getString(String name)
  {
    PGLElement elem=getElement(name);
    if(elem==null) return null;
    return elem.getDescription().getString();
  }

  /**
   * Set the key.
   * @param key The key to set.
   */
  private void setKey(String key)
  {
    _key=key;
  }

  /**
   * Get the key.
   * @return Returns the key.
   */
  public String getKey()
  {
    return _key;
  }

  /**
   * Set the string.
   * @param string The string to set.
   */
  private void setString(String string)
  {
    _string=string;
  }

  /**
   * Get the string.
   * @return Returns the string.
   */
  public String getString()
  {
    return _string;
  }

  /**
   * Set the elements.
   * @param elements The elements to set.
   */
  private void setElements(PGLElement[] elements)
  {
    _elements=elements;
  }

  /**
   * Get the elements.
   * @return Returns the elements.
   */
  public PGLElement[] getElements()
  {
    return _elements;
  }
}
