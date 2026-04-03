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

/**
 * Basic interpretor.
 */
public class BasicInterpretor extends RootInterpretor implements Interpretor
{
  /**
   * Create a new BasicInterpretor without default interpretor.
   * @param config the configuration.
   */
  public BasicInterpretor(IRCConfiguration config)
  {
    this(config,null);
  }

  /**
   * Create a new BasicInterpretor.
   * @param config the configuration.
   * @param next next interpretor to be used if the command is unknown. If null,
   * the command will be sent as it to the server.
   */
  public BasicInterpretor(IRCConfiguration config,Interpretor next)
  {
    super(config,next);
  }

  /**
   * Handle the received command.
   * @param source the source that emitted the command.
   * @param cmd the hole command line.
   * @param parts the parsed command line.
   * @param cumul the cumul parsed command line.
   */
  protected void handleCommand(Source source,String cmd,String[] parts,String[] cumul)
  {
    try
    {
      Server server=source.getServer();
      if(cmd.equals("echo"))
      {
        test(cmd,parts,1);
        source.report(cumul[1]);
      }
      else if(cmd.equals("sleep"))
      {
        test(cmd,parts,1);
        try
        {
          int ms=(new Integer(parts[1])).intValue();
          Thread.sleep(ms);
        }
        catch(Exception ex)
        {
          //Invalid integer or interrupted, ignore it...
        }
      }
      else if(cmd.equals("me"))
      {
        test(cmd,parts,1);
        sendString(source,"/ctcp action "+cumul[1]);
      }
      else if(cmd.equals("beep"))
			{
			  _ircConfiguration.getAudioConfiguration().beep();
			}
			else if(cmd.equals("play"))
			{
			 test(cmd,parts,1);
       _ircConfiguration.getAudioConfiguration().play(parts[1]);
		  }
		  else if(cmd.equals("sound"))
		  {
        test(cmd,parts,1);
        if(source.talkable())
        {
          sendString(source,"/ctcp sound "+cumul[1]);
		      sendString(source,"/play "+cumul[1]);
		    }
        else
        {
          source.report(getText(IRCTextProvider.INTERPRETOR_NOT_ON_CHANNEL));
        }
		  }
      else if(cmd.equals("url"))
      {
        test(cmd,parts,1);
        if(parts.length>=3)
          _ircConfiguration.getURLHandler().openURL(parts[1],parts[2]);
        else
          _ircConfiguration.getURLHandler().openURL(parts[1]);
      }
      else if(cmd.equals("clear"))
      {
        source.clear();
      }
      else if(cmd.equals("leave"))
      {
        source.leave();
      }
      else if(cmd.equals("msg"))
      {
        test(cmd,parts,2);
        boolean said=false;
        Enumeration e=server.getSources();
        while(e.hasMoreElements())
        {
          Source s=(Source)e.nextElement();
          if(s.getName().equals(parts[1]))
          {
            say(s,cumul[2]);
            said=true;
          }
        }
        if(!said) server.say(parts[1],cumul[2]);
      }
      else if(cmd.equals("ping"))
      {
        test(cmd,parts,1);
        sendString(source,"/ctcp ping "+cumul[1]);
      }
      else if(cmd.equals("dcc"))
      {
        test(cmd,parts,1);
        sendString(source,"/ctcp dcc "+cumul[1]);
      }
      else if(cmd.equals("raw"))
			{
			  server.execute(cumul[1]);
			}
      else if(cmd.equals("version"))
      {
        source.report("PJIRC v"+_ircConfiguration.getVersion());
      }
      else if(cmd.equals("gc"))
      {
        System.gc();
        source.report(Runtime.getRuntime().freeMemory()+" "+Runtime.getRuntime().totalMemory());
      }
      else if(cmd.equals("onserver"))
      {
        test(cmd,parts,2);
        String s=parts[1];
        if(s.equals(server.getServerName()))
        {
          if((cumul[2].startsWith("/")) && (server instanceof IRCServer))
            ((IRCServer)server).getStatus().sendString(cumul[2]);
          else
            server.execute(cumul[2]);
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

