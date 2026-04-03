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

/**
 * The AWTStatus.
 */
public class AWTStatus extends BaseAWTSource implements StatusListener,ReplyServerListener
{

  /**
   * 
   */
  private static final long serialVersionUID=1L;

  /**
   * Create a new AWTStatus.
   * @param config global irc configuration.
   * @param s source status.
   */
  public AWTStatus(PixxConfiguration config,Status s)
  {
    super(config,s);
    s.addStatusListener(this);
    s.getIRCServer().addReplyServerListener(this);
    title();
  }

  public void release()
  {
    ((Status)_source).removeStatusListener(this);
    ((Status)_source).getIRCServer().removeReplyServerListener(this);
    super.release();
  }

  private String getSourceName()
  {
    if(!_pixxConfiguration.getIRCConfiguration().getB("multiserver"))
    {
      if(_pixxConfiguration.getIRCConfiguration().getB("useinfo"))
        return getText(PixxTextProvider.SOURCE_INFO);
      return getText(PixxTextProvider.SOURCE_STATUS);
    }

    return ((Status)_source).getServerName();
  }

  private void title()
  {
    setTitle(getSourceName()+": "+ ((Status)_source).getNick()+" ["+((Status)_source).getMode()+"]");
  }

  public String getShortTitle()
  {
    return getSourceName();
  }

  /**
   * A notice has been received.
   * @param from source nickname.
   * @param msg notice string.
   */
  public void noticeReceived(String from,String msg)
  {
    print("-"+from+"- "+msg,5);
  }

  public void nickChanged(String nick,Status status)
  {
    print("*** "+getText(PixxTextProvider.SOURCE_YOUR_NICK,nick),3);
    title();
  }

  public void modeChanged(String mode,Status status)
  {
    if(mode.length()>0) print("*** "+getText(PixxTextProvider.SOURCE_YOUR_MODE,mode),3);
    title();
  }

  public void invited(String channel,String who,Status status)
  {
    if(status.getIRCServer().getDefaultSource()!=null) status.getIRCServer().getDefaultSource().report(getText(PixxTextProvider.SOURCE_YOU_INVITED,who,channel));
  }

  public Boolean replyReceived(String prefix,String id,String params[],IRCServer server)
  {
    if(id.equals("301")) //away
    {
      String toSend=getText(PixxTextProvider.SOURCE_AWAY,params[1])+" :";
      for(int i=2;i<params.length;i++) toSend+=" "+params[i];
      _source.report(toSend);
    }
    return Boolean.FALSE;
  }

}

