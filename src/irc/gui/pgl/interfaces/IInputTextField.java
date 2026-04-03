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

import java.awt.event.*;

/**
 * A text input field.
 */
public interface IInputTextField extends IVisibleItem
{
  /**
   * Add action listener, ie when the text has been validated.
   * @param listener listener to add.
   */
  public void addActionListener(ActionListener listener);
  /**
   * Remove an action listener.
   * @param listener listener to remove.
   */
  public void removeActionListener(ActionListener listener);

  /**
   * Get the field text content.
   * @return field text content.
   */
  public String getText();
  /**
   * Set the fied text content.
   * @param text new text content.
   */
  public void setText(String text);
  /**
   * Validate the current text content.
   */
  public void validateInput();

  /**
   * Set the list of all known words that will be automatically
   * completed when the completion key is pressed.
   * @param dictionary array of all know words.
   */
  public void setDictionary(String[] dictionary);
}
