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

import irc.gui.pgl.interfaces.*;
import java.awt.event.*;
import irc.*;
import irc.gui.pgl.*;

/**
 * PGLSourceTextField
 */
public class PGLSourceTextField implements ISourceSpecificItem,ActionListener,IVisibleItem
{
  private Source _source;
  private PGLIRCTextField _field;

  public void initialize(PGLInitializationParameters params,Object userParams) throws PGLException
  {
    _field=new PGLIRCTextField();
    _field.initialize(params,userParams);
    PGLSourceSpecificParameter p=(PGLSourceSpecificParameter)userParams;
    _source=p.getSource();
    _field.addActionListener(this);
  }

  public void uninitialize()
  {
    _field.removeActionListener(this);
    _source=null;
    _field.uninitialize();
    _field=null;
  }

  public void placeOn(Container container,Object constraint)
  {
    _field.placeOn(container,constraint);
  }

  public void removeFrom(Container container)
  {
    _field.removeFrom(container);
  }

  public Source getSource()
  {
    return _source;
  }

  /**
   * Validate the text and send it to the source.
   */
  public void sendText()
  {
    _source.sendUserString(_field.getText());
    _field.setText("");
  }

  public void actionPerformed(ActionEvent evt)
  {
    EventDispatcher.dispatchEventAsync(this,"sendText",new Object[0]);
  }
}
