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

import irc.*;
import irc.style.*;
import irc.gui.pgl.*;
import irc.gui.pgl.interfaces.*;

/**
 * PGLStyledList.
 */
public class PGLStyledList implements IMessageHistory,SourceListener,IScrollBarListener
{
  private StyledList _list;
  private Source _source;
  private IScrollBar _vscroll;
  private IScrollBar _hscroll;
  private boolean _updating=false;

  public void initialize(PGLInitializationParameters params,Object userParams) throws PGLException
  {
    PGLElement emap=params.getDescription().getElement("Mapping");
    if(emap!=null)
    {
      IMappingGroup map=(IMappingGroup)emap.createInstance(params,userParams);
      _vscroll=(IScrollBar)map.getItem("VScroll");
      _hscroll=(IScrollBar)map.getItem("HScroll");
      map.uninitialize();
    }

    if(_vscroll!=null) _vscroll.addScrollBarListener(this);
    if(_hscroll!=null) _hscroll.addScrollBarListener(this);

    PGLSourceSpecificParameter p=(PGLSourceSpecificParameter)userParams;

    IRCConfiguration config=p.getConfiguration();
    boolean wrap=true;
    _source=p.getSource();
    _source.addSourceListener(this);

    _list=new StyledList(config,wrap,config.getStyleContext(_source.getType(),_source.getName()));
  }

  public void uninitialize()
  {
    _list.dispose();
    _list=null;
    _source.removeSourceListener(this);
    _source=null;
    if(_vscroll!=null) _vscroll.removeScrollBarListener(this);
    if(_hscroll!=null) _hscroll.removeScrollBarListener(this);
    _vscroll=null;
    _hscroll=null;

  }

  public void placeOn(Container container,Object constraint)
  {
    container.add(_list,constraint);
  }

  public void removeFrom(Container container)
  {
    container.remove(_list);
  }

  public Source getSource()
  {
    return _source;
  }

  /**
   * Add a single line.
   * @param line
   */
  public void addLine(String line)
  {
    _list.addLine(line);
  }

  /**
   * Clear the list.
   */
  public void clear()
  {
    _list.clear();
  }

  /**
   * Print a message.
   * @param msg
   * @param color
   * @param bold
   * @param underline
   */
  protected void print(String msg,int color,boolean bold,boolean underline)
  {
    if(color!=1) msg="\3"+color+msg;
    if(bold) msg="\2"+msg;
    if(underline) msg=((char)31)+msg;

    _list.addLine(msg);
    if(_vscroll!=null)
    {
      _updating=true;
      _vscroll.setMaximum(_list.getLineCount()-1);
      _vscroll.setValue(_list.getLast());
      _updating=false;
    }
  }

  /**
   * Print a message.
   * @param msg
   * @param color
   */
  protected void print(String msg,int color)
  {
    print(msg,color,false,false);
  }

  /**
   * Print a message.
   * @param msg
   */
  protected void print(String msg)
  {
    print(msg,1,false,false);
  }


  public void messageReceived(String nick,String msg,Source source)
  {
    print("<"+nick+"> "+msg);
  }

  public void reportReceived(String message,Source source)
  {
    print(message);
  }

  public void noticeReceived(String nick,String message,Source source)
  {
    print("-"+nick+"- "+message,5);
  }

  public void action(String nick,String msg,Source source)
  {
    print("* "+nick+" "+msg,6);
  }

  public void clear(Source source)
  {
    _list.clear();
  }

  public void scrollBarValueChanged(IScrollBar scrollBar)
  {
    if(_updating) return;
    if(scrollBar==_vscroll) _list.setLast(scrollBar.getValue());
  }
}
