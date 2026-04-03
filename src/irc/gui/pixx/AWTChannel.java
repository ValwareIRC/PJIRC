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

import java.util.*;
import irc.*;
import java.awt.*;
import java.awt.event.*;
import irc.tree.*;
import irc.style.*;

/**
 * NickNameComparator.
 */
class NickNameComparator implements irc.tree.Comparator
{
  private char[] _prefixes;

  /**
   * Create a new NickNameComparator
   * @param prefixes
   */
  public NickNameComparator(char[] prefixes)
  {
    _prefixes=prefixes;
  }

  private int getPrefixCompareIndex(String nick)
  {
    if(nick.length()==0) return _prefixes.length;
    for(int i=_prefixes.length-1;i>=0;i--)
      if(nick.charAt(0)==_prefixes[i]) return i;
    return _prefixes.length;
  }

  public int compare(Object i1,Object i2)
  {
    String n1=(String)i1;
    String n2=(String)i2;
    n1=n1.toLowerCase(java.util.Locale.ENGLISH).toUpperCase(java.util.Locale.ENGLISH);
    n2=n2.toLowerCase(java.util.Locale.ENGLISH).toUpperCase(java.util.Locale.ENGLISH);
    int prefixCmp=getPrefixCompareIndex(n1)-getPrefixCompareIndex(n2);
    if(prefixCmp<0) return -1;
    if(prefixCmp>0) return 1;

    return n1.compareTo(n2);
  }
}

/**
 * The AWT Channel.
 */
public class AWTChannel extends BaseAWTSource implements ChannelListener2,PixxNickListListener
{
  /**
   * 
   */
  private static final long serialVersionUID=1L;
  private ScrollablePixxNickList _nicks;
	private Label _label;
  private SortedList _sortedList;
  private Hashtable _modeMapping;
  private NickMenuHandler _menu;

  /**
   * Create a new AWTChannel.
   * @param config the global irc configuration.
   * @param c the source channel.
   */
  public AWTChannel(PixxConfiguration config,Channel c)
  {
    super(config,c);

    _menu=new NickMenuHandler(config,this,c);
    _nicks=new ScrollablePixxNickList(_pixxConfiguration,c.getIRCServer().getNickPrefixes());
    c.addChannelListener2(this);
    _nicks.addPixxNickListListener(this);
    _sortedList=new SortedList(new NickNameComparator(c.getIRCServer().getNickPrefixes()));
    _modeMapping=new Hashtable();
		_label=new Label("");
    _label.setBackground(_pixxConfiguration.getColor(PixxColorModel.COLOR_BACK));
    _label.setForeground(_pixxConfiguration.getColor(PixxColorModel.COLOR_WHITE));
    if(_pixxConfiguration.getIRCConfiguration().getB("asl"))
		{
		  Panel right=new Panel();
			right.setLayout(new BorderLayout());
			right.add(_nicks,BorderLayout.CENTER);
			Panel outlabel=new Panel();
      outlabel.setLayout(new BorderLayout());
      outlabel.add(new PixxSeparator(PixxSeparator.BORDER_LEFT),BorderLayout.WEST);
      outlabel.add(new PixxSeparator(PixxSeparator.BORDER_RIGHT),BorderLayout.EAST);
      outlabel.add(new PixxSeparator(PixxSeparator.BORDER_UP),BorderLayout.NORTH);
      outlabel.add(new PixxSeparator(PixxSeparator.BORDER_DOWN),BorderLayout.SOUTH);
			outlabel.add(_label,BorderLayout.CENTER);
			right.add(outlabel,BorderLayout.SOUTH);
			add(right,BorderLayout.EAST);
		}
		else
		{
      add(_nicks,BorderLayout.EAST);
		}
    doLayout();
    title();
    
    
    if(_pixxConfiguration.getB("showchannelyoujoin"))
      print("*** "+getText(PixxTextProvider.SOURCE_YOU_JOINED_AS,c.getName(),c.getServer().getNick()),3);
  }

  public void release()
  {
    _menu.release();
    ((Channel)_source).removeChannelListener2(this);
    _nicks.removePixxNickListListener(this);
    _nicks.release();
    _menu=null;
    super.release();
  }

	public void doLayout()
	{
	  _label.setText("");
		super.doLayout();
	}

  public void setVisible(boolean b)
  {
    super.setVisible(b);
    if(!b) _nicks.dispose();
  }


  private String getFullModeNick(String nick,String mode)
  {
    Channel c=(Channel)getSource();
    char[] _prefixes=c.getIRCServer().getNickPrefixes();
    char[] _modes=c.getIRCServer().getNickModes();
    char[][] chanmodes=c.getIRCServer().getChannelModes();
    ModeHandler h=new ModeHandler(mode,chanmodes,_modes);
    for(int i=0;i<_modes.length;i++)
    {
      if(h.hasMode(_modes[i])) return _prefixes[i]+nick;
    }

    return nick;
  }

  private String getUnprefixedNick(String nick)
  {
    if(nick.length()==0) return nick;
    Channel c=(Channel)getSource();
    char[] _prefixes=c.getIRCServer().getNickPrefixes();
    for(int i=0;i<_prefixes.length;i++)
      if(nick.charAt(0)==_prefixes[i]) return nick.substring(1);
    return nick;
  }


  private void setNicks(String[] nicks)
  {
    for(int i=0;i<nicks.length;i++) addNick(nicks[i]);
  }

  private void addNick(String nick)
  {
    String mode=((Channel)_source).getNickMode(nick);
    if(mode!=null)
    {
      String full=getFullModeNick(nick,mode);
      _sortedList.add(full);
      _modeMapping.put(nick,full);
    }
  }

  private void removeNick(String nick)
  {
    String full=(String)_modeMapping.get(nick);
    if(full!=null)
    {
      _sortedList.remove(full);
      _modeMapping.remove(nick);
    }
  }

  private void updateNick(String nick)
  {
    removeNick(nick);
    addNick(nick);
  }

  private void update()
  {
    String[] n=new String[_modeMapping.size()];
    Enumeration e=_modeMapping.keys();
    int i=0;
    while(e.hasMoreElements()) n[i++]=(String)e.nextElement();
    _textField.setCompleteList(n);

    n=new String[_modeMapping.size()];
    e=_modeMapping.keys();
    i=0;
    while(e.hasMoreElements())
    {
      String nick=(String)e.nextElement();
      n[i++]=nick+":"+((Channel)_source).whois(getUnprefixedNick(nick));
    }
    _list.setNickList(n);

    n=new String[_sortedList.getSize()];
    e=_sortedList.getItems();
    i=0;
    while(e.hasMoreElements())
		{
      String nick=(String)e.nextElement();
      String whois=((Channel)_source).whois(getUnprefixedNick(nick));
		  n[i++]=nick+":"+whois;
		}

    _nicks.set(n);
    title();
  }

  public synchronized void nickSet(String[] nicks,String[] modes,Channel channel)
  {
    setNicks(nicks);
    update();
  }
  
  public synchronized void nickReset(Channel channel)
  {
    _sortedList.clear();
    _modeMapping.clear();
    _nicks.removeAll();
    update();
  }

  public synchronized void nickJoin(String nick,String mode,Channel channel)
  {
    addNick(nick);
    update();
    if(_pixxConfiguration.getB("showchannelnickjoin"))
      print("*** "+getText(PixxTextProvider.SOURCE_HAS_JOINED,nick,_source.getName()),3);
  }

  public synchronized void nickPart(String nick,String reason,Channel channel)
  {
    removeNick(nick);
    update();
    if(_pixxConfiguration.getB("showchannelnickpart"))
    {
      if(reason.length()>0)
        print("*** "+getText(PixxTextProvider.SOURCE_HAS_LEFT,nick,_source.getName())+" ("+reason+")",3);
      else
        print("*** "+getText(PixxTextProvider.SOURCE_HAS_LEFT,nick,_source.getName()),3);
    }
  }

  public synchronized void nickKick(String nick,String by,String reason,Channel channel)
  {
    removeNick(nick);
    update();
    if(_pixxConfiguration.getB("showchannelnickkick"))
    {
      if(reason.length()>0)
        print("*** "+getText(PixxTextProvider.SOURCE_HAS_BEEN_KICKED_BY,nick,by)+" ("+reason+")",3);
      else
        print("*** "+getText(PixxTextProvider.SOURCE_HAS_BEEN_KICKED_BY,nick,by),3);
    }
    if(nick.equals(_source.getServer().getNick()))
    {
      _source.getServer().sendStatusMessage(getText(PixxTextProvider.SOURCE_YOU_KICKED,channel.getName(),by)+" ("+reason+")");
    }
  }

  public synchronized void nickQuit(String nick,String reason,Channel channel)
  {
    removeNick(nick);
    update();
    if(_pixxConfiguration.getB("showchannelnickquit"))
    {
      if(reason.length()>0)
        print("*** "+getText(PixxTextProvider.SOURCE_HAS_QUIT,nick)+" ("+reason+")",2);
      else
        print("*** "+getText(PixxTextProvider.SOURCE_HAS_QUIT,nick),2);
    }
  }

  private void title()
  {
    int count=_sortedList.getSize();
    String title="";
    if(_pixxConfiguration.getB("displaychannelname")) title+=_source.getName();
    if(_pixxConfiguration.getB("displaychannelcount")) title+=" ["+count+"]";
    if(_pixxConfiguration.getB("displaychannelmode")) title+=" ["+((Channel)_source).getMode()+"]";
    if(_pixxConfiguration.getB("displaychanneltopic"))
    {
      if(title.length()!=0)
        title+=": "+((Channel)_source).getTopic();
      else
        title=((Channel)_source).getTopic();
    }
    setTitle(title.trim());
  }

  public synchronized void topicChanged(String topic,String by,Channel channel)
  {
    if(_pixxConfiguration.getB("showchanneltopicchanged"))
    {
      if(by.length()==0)
        print("*** "+getText(PixxTextProvider.SOURCE_TOPIC_IS,topic),3);
      else
        print("*** "+getText(PixxTextProvider.SOURCE_CHANGED_TOPIC,by,topic),3);
    }
    title();
  }

  public synchronized void modeApply(String mode,String from,Channel channel)
  {
    if(_pixxConfiguration.getB("showchannelmodeapply"))
    {
      if(from.length()>0)
        print("*** "+getText(PixxTextProvider.SOURCE_CHANNEL_MODE,from,mode),3);
      else
        print("*** "+getText(PixxTextProvider.SOURCE_CHANNEL_MODE_IS,mode),3);
    }
    title();
  }

  public synchronized void nickModeApply(String nick,String mode,String from,Channel channel)
  {
    if(_pixxConfiguration.getB("showchannelnickmodeapply"))
      print("*** "+getText(PixxTextProvider.SOURCE_USER_MODE,from,mode,nick),3);
    updateNick(nick);
    update();
  }

  public synchronized void nickChanged(String oldNick,String newNick,Channel channel)
  {
    if(_pixxConfiguration.getB("showchannelnickchanged"))
      print("*** "+getText(PixxTextProvider.SOURCE_KNOWN_AS,oldNick,newNick),3);
    removeNick(oldNick);
    addNick(newNick);
    update();
  }

  public void nickWhoisUpdated(String nick,String whois,Channel channel)
	{
    update();
	}

  public void eventOccured(String nick,MouseEvent e)
  {
    if(_pixxConfiguration.matchMouseConfiguration("nickpopup",e))
    {
      _menu.popup(nick,((Channel)_source).whois(nick),_nicks,e.getX(),e.getY());
    }
    else if(_pixxConfiguration.matchMouseConfiguration("nickquery",e))
    {
      if(_pixxConfiguration.getB("automaticqueries"))
      {
        if(!nick.equals(_source.getServer().getNick()))
        {
          _source.sendString("/focus Query "+nick);
          _source.sendString("/query "+nick);
        }
      }
    }
  }

	public void ASLEventOccured(String nick,String info)
	{
    _label.setText(_pixxConfiguration.getIRCConfiguration().formatASL(info));
	}

  public void nickEvent(StyledList lis,String nick,MouseEvent e)
  {
    if(_pixxConfiguration.matchMouseConfiguration("nickpopup",e))
    {
      _menu.popup(nick,((Channel)_source).whois(nick),_list,e.getX(),e.getY());
    }
    else
    {
      super.nickEvent(lis,nick,e);
    }
  }
}
