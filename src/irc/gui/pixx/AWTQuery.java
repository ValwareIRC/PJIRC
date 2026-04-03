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
import irc.*;

import irc.style.*;
import java.awt.event.*;

/**
 * The AWTQuery.
 */
public class AWTQuery extends BaseAWTSource implements QueryListener,ReplyServerListener
{
  /**
   * 
   */
  private static final long serialVersionUID=1L;
  private NickMenuHandler _menu;

  /**
   * Create a new AWTQuery.
   * @param config the global irc configuration.
   * @param query the source query.
   */
  public AWTQuery(PixxConfiguration config,Query query)
  {
    super(config,query);
    _menu=new NickMenuHandler(config,this,query);
    query.addQueryListener(this);
    query.getIRCServer().addReplyServerListener(this);
    update();
  }

  public void release()
  {
    ((Query)_source).removeQueryListeners(this);
    ((Query)_source).getIRCServer().removeReplyServerListener(this);
    _menu.release();
    _menu=null;
    super.release();
  }

  private void update()
  {
    String whois=((Query)_source).getWhois();
    String[] nick=new String[2];
    nick[0]=_source.getName()+":"+_pixxConfiguration.getIRCConfiguration().formatASL(whois);
    nick[1]=_source.getServer().getNick()+":"+_pixxConfiguration.getIRCConfiguration().formatASL(_source.getServer().getUserName());
    _list.setNickList(nick);
    title();
  }

  private void title()
  {
    String whois=((Query)_source).getWhois();
    if(whois.length()>0)
      setTitle(_source.getName()+" ("+_pixxConfiguration.getIRCConfiguration().formatASL(whois)+")");
    else
      setTitle(_source.getName());

  }

  public void nickChanged(String newNick,Query query)
  {
    update();
  }

  public void whoisChanged(String whois,Query query)
  {
    update();
  }

  private String whois(String nick)
  {
    nick=nick.toLowerCase(java.util.Locale.ENGLISH);
    if(nick.equals(_source.getName().toLowerCase(java.util.Locale.ENGLISH))) return ((Query)_source).getWhois();
    if(nick.equals(_source.getServer().getNick().toLowerCase(java.util.Locale.ENGLISH))) return _source.getServer().getUserName();
    return "";
  }

  public void nickEvent(StyledList lis,String nick,MouseEvent e)
  {
    if(_pixxConfiguration.matchMouseConfiguration("nickpopup",e))
    {
      _menu.popup(nick,whois(nick),_list,e.getX(),e.getY());
    }
    else
    {
      super.nickEvent(lis,nick,e);
    }
  }

  public Boolean replyReceived(String prefix,String id,String params[],IRCServer server)
  {
    if(id.equals("301")) //away
    {
      if(params[1].toLowerCase(java.util.Locale.ENGLISH).equals(_source.getName().toLowerCase(java.util.Locale.ENGLISH)))
      {
        String toSend=getText(PixxTextProvider.SOURCE_AWAY,params[1])+" :";
        for(int i=2;i<params.length;i++) toSend+=" "+params[i];
        _source.report(toSend);
      }
    }
    return Boolean.FALSE;
  }

}

