/*****************************************************/
/*          This java file is a part of the          */
/*                                                   */
/*           -  Plouf's Java IRC Client  -           */
/*                                                   */
/*   Copyright (C)  2002 - 2005 Philippe Detournay   */
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

import java.util.*;
import java.io.*;
import irc.dcc.prv.*;

/**
 * The CTCP interpretor.
 */
public class CTCPInterpretor extends BasicInterpretor
{
  /**
   * The CTCPFilter.
   */
  //protected CTCPFilter _filter;
  protected ServerManager _mgr;

  /**
   * Create a new CTCPInterpretor.
   * @param config global configuration.
   * @param next next interpretor to use if command is unknown.
   * @param mgr server manager
   */
  public CTCPInterpretor(IRCConfiguration config,Interpretor next/*,CTCPFilter filter*/,ServerManager mgr)
  {
    super(config,next);
    _mgr=mgr;
    //_filter=filter;
  }

  private void send(Server s,String destination,String msg)
  {
    s.say(destination,"\1"+msg+"\1");
  }

  protected void handleCommand(Source source,String cmd,String[] parts,String[] cumul)
  {
    try
    {
      if(cmd.equals("ctcp"))
      {
        test(cmd,parts,1);
        if(parts[1].toLowerCase(java.util.Locale.ENGLISH).equals("ping"))
        {
          test(cmd,parts,2);
          send(source.getServer(),parts[2],"PING "+(new Date()).getTime());
          //_filter.ping(source.getServer(),parts[2]);
        }
        else if(parts[1].toLowerCase(java.util.Locale.ENGLISH).equals("action"))
        {
          test(cmd,parts,2);
          if(source.talkable())
          {
            send(source.getServer(),source.getName(),"ACTION "+cumul[2]);
            //_filter.action(source.getServer(),source.getName(),cumul[2]);
            source.action(source.getServer().getNick(),cumul[2]);
          }
          else
          {
            source.report(getText(IRCTextProvider.INTERPRETOR_NOT_ON_CHANNEL));
          }
        }
        else if(parts[1].toLowerCase(java.util.Locale.ENGLISH).equals("dcc"))
        {
          test(cmd,parts,2);
          if(parts[2].toLowerCase(java.util.Locale.ENGLISH).equals("chat") && _ircConfiguration.getB("allowdccchat"))
          {
            test(cmd,parts,3);
            //_filter.chat(source.getServer(),parts[3]);
            Server s=source.getServer();
            String nick=parts[3];
            try
            {
              DCCChatServer cserver=new DCCChatServer(_ircConfiguration,s.getNick(),nick);
              String arg=cserver.openPassive();
              if(arg.length()==0)
              {
                cserver.sendStatusMessage(getText(IRCTextProvider.DCC_UNABLE_PASSIVE_MODE));
              }
              else
              {
                send(s,nick,"DCC CHAT chat "+arg);
              }
              _mgr.newServer(cserver,false);
            }
            catch(Throwable ex)
            {
              _ircConfiguration.internalError("dcc chat error",ex,"plouf@pjirc.com");
            }

          }
          else if(parts[2].toLowerCase(java.util.Locale.ENGLISH).equals("send") && _ircConfiguration.getB("allowdccfile"))
          {
            String file=null;
            test(cmd,parts,3);
            
            if(parts.length>4) file=cumul[4];

            Server s=source.getServer();
            String nick=parts[3];
            try
            {
              File f;
              if(file!=null)
                f=new File(file);
              else
                f=_ircConfiguration.getSecurityProvider().getLoadFile("Send file");

              DCCFileHandler handler=new DCCFileHandler(_ircConfiguration,nick,f);
              char guil=34;
              String filename=f.getName();
              if(filename.indexOf(" ")!=-1) filename=guil+filename+guil;
              String arg=filename+" "+handler.send();
              send(s,nick,"DCC SEND "+arg);
              _mgr.newServer(handler,false);

            }
            catch(Throwable ex)
            {
              _ircConfiguration.internalError("dcc send error",ex,"plouf@pjirc.com");
            }
          }
          else
          {
            source.report(getText(IRCTextProvider.INTERPRETOR_UNKNOWN_DCC,parts[2]));
          }
        }
        else if(parts[1].toLowerCase(java.util.Locale.ENGLISH).equals("raw"))
        {
          test(cmd,parts,3);
          send(source.getServer(),parts[2],cumul[3]);
//          _filter.genericSend(source.getServer(),parts[2],cumul[3]);
        }
        else if(parts[1].toLowerCase(java.util.Locale.ENGLISH).equals("sound"))
        {
          test(cmd,parts,2);
          send(source.getServer(),source.getName(),"SOUND "+parts[2]);
        }
        else
        {
          test(cmd,parts,2);
          send(source.getServer(),parts[2],parts[1]);
          //_filter.genericSend(source.getServer(),parts[2],parts[1]);
        }
      }
      else
      {
        super.handleCommand(source,cmd,parts,cumul);
      }
    }
    catch(NotEnoughParametersException ex)
    {
      source.report(getText(IRCTextProvider.INTERPRETOR_INSUFFICIENT_PARAMETERS,ex.getMessage()));
    }
  }
}

