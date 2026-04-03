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

/**
 * A source interpretor.
 */
public interface Interpretor
{
  /**
   * Send the given string to the server.
   * @param s the source.
   * @param str the string to send.
   */
  public void sendString(Source s,String str);

  /**
   * Set the next interpretor.
   * @param next interpretor to use. May be null.
   */
  public void setNextInterpretor(Interpretor next);

  /**
   * Get the next interpretor.
   * @return the next interpretor, or null if this interpretor is the last.
   */
  public Interpretor getNextInterpretor();

  /**
   * Check whether the given interpretor is already in the interpretor chain.
   * @param in the interpretor to check.
   * @return true if in is in the chain, false otherwise.
   */
  public boolean isInside(Interpretor in);

  /**
   * Add the given interpretor at the end of this interpretor chain. If in is
   * already in the chain, nothing is done.
   * @param in interpretor to add.
   */
  public void addLast(Interpretor in);
}

