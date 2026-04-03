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
import irc.dcc.*;

/**
 * The AWT dcc file interface.
 */
public class AWTDCCFile implements DCCFileListener,WindowListener
{
  /**
   * The file.
   */
  protected DCCFile _file;
  /**
   * The displayed frame.
   */
  protected Frame _frame;
  /**
   * The progress bar.
   */
  protected AWTProgressBar _bar;
  private PixxConfiguration _pixxConfiguration;

  /**
   * Create a new AWTDCCFile.
   * @param config the global irc configuration.
   * @param file the source DCCFile.
   */
  public AWTDCCFile(PixxConfiguration config,DCCFile file)
  {
    _pixxConfiguration=config;
    _file=file;
    _file.addDCCFileListener(this);

    String str="";
    if(file.isDownloading())
      str=_pixxConfiguration.getText(PixxTextProvider.GUI_RETREIVING_FILE,_file.getSize()+"");
    else
      str=_pixxConfiguration.getText(PixxTextProvider.GUI_SENDING_FILE,_file.getSize()+"");


    Label label=new Label(str);

    _frame=new Frame();
    _frame.setBackground(Color.white);

    _frame.setLayout(new BorderLayout());
    _frame.addWindowListener(this);

    _bar=new AWTProgressBar();
    _frame.add(label,BorderLayout.NORTH);
    _frame.add(_bar,BorderLayout.CENTER);

    _frame.setTitle(_file.getName());
    _frame.setSize(400,80);
    _frame.show();
  }

  /**
   * Release this object.
   */
  public void release()
  {
    _frame.removeAll();
    _file.removeDCCFileListener(this);
    _file=null;
    _frame.removeWindowListener(this);
    _frame.dispose();
    _frame=null;
  }

	/**
	 * Get the source DCCFile.
	 * @return source DCCFile.
	 */
  public DCCFile getFile()
  {
    return _file;
  }

  /**
   * Close this transfert.
   */
  public void close()
  {
    _frame.hide();
  }

  public void transmitted(Integer icount,DCCFile file)
  {
    //activate();
    int count=icount.intValue();
    if((count&32767)==0)
    {
      double pc=count;
      pc/=_file.getSize();
      _bar.setColor(Color.blue);
      _bar.setValue(pc);
      _bar.repaint();
    }
  }

  public void finished(DCCFile file)
  {
    _frame.setTitle(_pixxConfiguration.getText(PixxTextProvider.GUI_TERMINATED,_file.getName()));
    _bar.setColor(Color.green);
    _bar.setValue(1);
    _bar.repaint();
  }

  public void failed(DCCFile file)
  {
    _frame.setTitle(_pixxConfiguration.getText(PixxTextProvider.GUI_FAILED,_file.getName()));
    _bar.setColor(Color.red);
    _bar.repaint();

  }

  public void windowActivated(WindowEvent e) {}
  public void windowClosed(WindowEvent e) {}
  public void windowClosing(WindowEvent e)
  {
    EventDispatcher.dispatchEventAsync(_file,"leave",new Object[0]);
  }

  public void windowDeactivated(WindowEvent e) {}
  public void windowDeiconified(WindowEvent e) {}
  public void windowIconified(WindowEvent e) {}
  public void windowOpened(WindowEvent e) {}

}

