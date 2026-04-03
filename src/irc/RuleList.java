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

package irc;

import java.util.*;

/**
 * RuleItem.
 */
class RuleItem
{
  /**
   * Handlers.
   */
  public ListHandler[] handlers;
  /**
   * Value.
   */
  public Object value;
}

/**
 * Implements a set of rules.
 */
public class RuleList
{
  private Vector _list;
  private Object _def;

  /**
   * Create a new, empty, set of rules.
   */
  public RuleList()
  {
    _list=new Vector();
    _def=null;
  }

  /**
   * Set the default value. By default, this value is null.
   * @param v new default value.
   */
  public void setDefaultValue(Object v)
  {
    _def=v;
  }

  /**
   * Get the default value.
   * @return the default value.
   */
  public Object getDefaultValue()
  {
    return _def;
  }

  /**
   * Add a new rule. The rule uses the given array of list patterns (see ListHandler)
   * and has the given value.
   * @param pattern array of pattern.
   * @param value rule value.
   */
  public void addRule(String[] pattern,Object value)
  {
    ListHandler[] handlers=new ListHandler[pattern.length];
    for(int i=0;i<pattern.length;i++) handlers[i]=new ListHandler(pattern[i]);
    RuleItem item=new RuleItem();
    item.handlers=handlers;
    item.value=value;
    _list.insertElementAt(item,_list.size());
  }

  private boolean match(RuleItem item,String[] pattern)
  {
    ListHandler[] handlers=item.handlers;
    if(pattern.length!=handlers.length) return false;
    for(int i=0;i<handlers.length;i++)
      if(!handlers[i].inList(pattern[i])) return false;
    return true;
  }

  /**
   * Find the first matching rule for the given values.
   * @param values array of values to be tested againts patterns.
   * @return value of the first matching rule, or default value if no matcing
   * rule is found.
   */
  public Object findValue(String[] values)
  {
    int l=_list.size();
    for(int i=0;i<l;i++)
    {
      RuleItem item=(RuleItem)_list.elementAt(i);
      if(match(item,values)) return item.value;
    }
    return _def;
  }
}
