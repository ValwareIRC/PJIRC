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

package irc.gui.pgl.interfaces;

/**
 * Mapping group.
 */
public interface IMappingGroup extends IItem
{
  /**
   * Get the string value associated to the given alias name.
   * @param key alias name.
   * @return string value, or null if not defined.
   */
  public String getString(String key);
  /**
   * Get the item associated to the given alias name. 
   * @param key alias name.
   * @return item, or null if not defined.
   */
  public IItem getItem(String key);

  /**
   * Get the string value associated to the given alias name.
   * @param key alias name.
   * @param def default value.
   * @return string value, or def if not defined.
   */
  public String getString(String key,String def);
  /**
   * Get the item associated to the given alias name. 
   * @param key alias name.
   * @param def default value.
   * @return item, or def if not defined.
   */
  public IItem getItem(String key,IItem def);
}
