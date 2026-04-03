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

package irc.gui.pixx;

import java.awt.*;
import java.awt.event.*;
import irc.*;

/**
 * The font selector window.
 */
public class FontSelector implements WindowListener,ActionListener,Runnable
{
  private Choice _name;
  private TextField _size;
  private Button _ok;
  private Frame _f;
  private FontSelectorListener _lis;

  /**
   * Create a new font selector.
   * @param config the irc configuration.
   */
  public FontSelector(PixxConfiguration config)
  {
    _f=new Frame();
    _f.setTitle(config.getText(PixxTextProvider.GUI_FONT_WINDOW));
    _name=new Choice();
    _name.add("Monospaced");
    _name.add("Serif");
    _name.add("SansSerif");
    _name.add("Dialog");
    _name.add("DialogInput");
    _size=new TextField("12");
    _ok=new Button(config.getText(PixxTextProvider.GUI_FONT_WINDOW_OK));
    _ok.addActionListener(this);
    Panel p=new Panel();
    _f.add(p);
    p.setLayout(new FlowLayout(FlowLayout.CENTER));
    p.add(_name);
    p.add(_size);
    p.add(_ok);


    //_f.setResizable(false);
    _f.setSize(200,80);
    _f.addWindowListener(this);
  }

  public void run()
  {
    if(_f!=null) _f.dispose();
    _f=null;
  }

  /**
   * Release this object.
   */
  public void release()
  {
    _ok.removeActionListener(this);
    _f.removeWindowListener(this);
    _f.removeAll();
    Thread t=new Thread(this,"Frame disposal thread");
    t.start();
    //_f.dispose();
    //_f=null;
    _lis=null;
  }

  /**
   * Ask for a font to be selected, calling back the given listener once
   * choice is performed.
   * @param lis listener to be called once font is selected.
   */
  public void selectFont(FontSelectorListener lis)
  {
    _lis=lis;
    _f.show();
  }

  public void windowActivated(WindowEvent e) {}
  public void windowClosed(WindowEvent e) {}
  public void windowClosing(WindowEvent e)
  {
    _f.hide();
  }

  public void windowDeactivated(WindowEvent e) {}
  public void windowDeiconified(WindowEvent e) {}
  public void windowIconified(WindowEvent e) {}
  public void windowOpened(WindowEvent e) {}

  private Font getResult()
  {
    String name=_name.getSelectedItem() ;
    int size=12;
    try
    {
      size=(new Integer(_size.getText())).intValue();
    }
    catch(Exception ex)
    {
    }
    return new Font(name,Font.PLAIN,size);
  }

  public void actionPerformed(ActionEvent e)
  {
    _f.hide();
    Font f=getResult();
    if(_lis!=null) EventDispatcher.dispatchEventAsync(_lis,"fontSelected",new Object[] {f});
  }
}
