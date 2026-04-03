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

package irc.gui.sdk;

import irc.*;
import irc.gui.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A simple window for displaying a Source.
 */
public class SimpleAWTSource extends WindowAdapter implements GUISource,ActionListener,SourceListener
{
  /**
   * The frame itself.
   */
  private Frame _frame;
  /**
   * The source we're displaying.
   */
  private Source _source;
  /**
   * The display area.
   */
  private TextArea _text;
  /**
   * The input field.
   */
  private TextField _field;

  /**
   * Create a new SimpleAWTSource.
   * @param config the IRCConfiguration instance, always usefull...
   * @param source the source we're supposed to handle.
   */
  public SimpleAWTSource(IRCConfiguration config,Source source)
  {
    _source=source;

    //Listen to generic source events.
    _source.addSourceListener(this);

    //Initialize and populate our window...
    _text=new TextArea();
    _field=new TextField();
    _field.addActionListener(this);
    _frame=new Frame();
    _frame.setLayout(new BorderLayout());
    _frame.addWindowListener(this);
    _frame.add(_text,BorderLayout.CENTER);
    _frame.add(_field,BorderLayout.SOUTH);

    _frame.setTitle(source.getName());

    _frame.setSize(640,400);
    //Time to play!
    _frame.show();
  } //SimpleAWTSource

  /**
   * Add a new line in the display area.
   * @param txt the content of the new line.
   */
  protected void print(String txt)
  {

    _text.append("["+_source+"] "+txt);
    _text.append("\n");
  } //print

  /**
   * Leave and close the window. Any ressources used by this instance will be
   * released and no further call should be performed on it.
   */
  public void leave()
  {
    _source.removeSourceListener(this);

    _frame.remove(_field);
    _frame.remove(_text);

    _field.removeActionListener(this);
    _field=null;
    _text=null;

    _frame.hide();
    _frame.removeWindowListener(this);
    _frame.dispose();
    _frame=null;
  } //leave


  /**
   * Set the current textfield text.
   * @param txt new textfield text.
   */
  public void setFieldText(String txt)
  {
    _field.setText(txt);
  } //setFieldText

  /**
   * Get the current textfield text.
   * @return the current textfield text.
   */
  public String getFieldText()
  {
    return _field.getText();
  } //getFieldText

  /**
   * Validate the current textfield text, as if user pressed return key.
   */
  public void validateText()
  {
    actionPerformed(null);
  } //validateText

  /**
   * Get the source.
   * @return the source.
   */
  public Source getSource()
  {
    return _source;
  } //getSource

  /**
   * Request the keyboard focus on this awt source.
   */
  public void requestFocus()
  {
    _frame.requestFocus();
  } //requestFocus

  /**
   * Get the source title.
   * @return source title.
   */
  public String getTitle()
  {
    return _frame.getTitle();
  } //getTitle

  public void windowClosing(WindowEvent e)
  {
    //Call the leave event on this source, as we consider this closing event
    //as a request to leave the source. Don't perform a direct method call
    //because we're not in the good thread!
    EventDispatcher.dispatchEventAsync(_source,"leave",new Object[0]);
  } //windowClosing

  public void actionPerformed(ActionEvent evt)
  {
    //Send the string to the source. Don't perform a direct method call because
    //we're not in the good thread!
    EventDispatcher.dispatchEventAsync(_source,"sendUserString",new Object[] {_field.getText()});
    _field.setText("");
  } //actionPerformed

  /**
   * A new message has been received.
   * @param nick source nick.
   * @param msg message.
   * @param source source.
   */
  public void messageReceived(String nick,String msg,Source source)
  {
    print(nick+"> "+msg);
  } //messageReceived

  /**
   * A new report has been received.
   * @param message report.
   * @param source source.
   */
  public void reportReceived(String message,Source source)
  {
    print(message);
  } //reportReceived

  /**
   * A new notice has been received.
   * @param nick source nick.
   * @param message notice.
   * @param source source.
   */
  public void noticeReceived(String nick,String message,Source source)
  {
    print("-"+nick+"- "+message);
  } //noticeReceived

  /**
   * A new action has been received.
   * @param nick source nick.
   * @param msg message.
   * @param source source.
   */
  public void action(String nick,String msg,Source source)
  {
    print(nick+" "+msg);
  } //action

  /**
   * A new clear request has been received.
   * @param source source.
   */
  public void clear(Source source)
  {
    _text.setText("");
  } //clear

} //SimpleAWTSource
