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

package irc;

import java.util.*;

/**
 * IRC interpretor.
 */
public class IRCInterpretor extends BasicInterpretor
{
  /**
   * Create a new IRCInterpretor.
   * @param config global configuration.
   */
  public IRCInterpretor(IRCConfiguration config)
  {
    super(config);
  }

  protected void handleCommand(Source source,String cmd,String[] parts,String[] cumul)
  {
    try
    {

      IRCServer server=(IRCServer)source.getServer();
      if(cmd.equals("amsg"))
      {
        test(cmd,parts,1);
        Enumeration e=server.getChannels();
        while(e.hasMoreElements())
        {
          ((Channel)e.nextElement()).sendString(cumul[1]);
        }
      }
      else if(cmd.equals("ame"))
      {
        test(cmd,parts,1);
        Enumeration e=server.getChannels();
        while(e.hasMoreElements())
        {
          ((Channel)e.nextElement()).sendString("/me "+cumul[1]);
        }
      }
      else if(cmd.equals("list"))
      {
        if(parts.length<=1)
          server.execute("LIST");
        else
          server.execute("LIST "+parts[1]);
      }
      else if(cmd.equals("topic"))
      {
        test(cmd,parts,2);
        server.execute("TOPIC "+parts[1]+" :"+cumul[2]);
      }
      else if(cmd.equals("away"))
      {
        if(parts.length<=1)
          server.execute("AWAY");
        else
          server.execute("AWAY :"+cumul[1]);
      }
      else if(cmd.equals("quit"))
      {
        if(parts.length>1)
          server.execute("QUIT :"+cumul[1]);
        else
          server.execute("QUIT");
      }
      else if(cmd.equals("part"))
      {
        test(cmd,parts,1);
        if(parts.length==2)
        {
          server.execute("PART "+parts[1]);
        }
        else
        {
          server.execute("PART "+parts[1]+" :"+cumul[2]);
        }
      }
      else if(cmd.equals("kick"))
      {
        test(cmd,parts,2);
        if(parts.length==3)
        {
          server.execute("KICK "+parts[1]+" "+parts[2]);
        }
        else
        {
          server.execute("KICK "+parts[1]+" "+parts[2]+" :"+cumul[3]);
        }
      }
      else if(cmd.equals("notice"))
      {
        test(cmd,parts,2);
        server.execute("NOTICE "+parts[1]+" :"+cumul[2]);
        source.report("-> -"+parts[1]+"- "+cumul[2]);
      }
      else if(cmd.equals("onotice"))
      {
        test(cmd,parts,2);
        sendString(source,"/notice @"+parts[1]+" "+cumul[2]);
      }
      else if(cmd.equals("join"))
      {
        test(cmd,parts,1);
        String chan=parts[1];
        if(!chan.startsWith("#") && !chan.startsWith("!") && !chan.startsWith("&") && !chan.startsWith("+"))
          chan='#'+chan;
        if(parts.length<=2)
          server.execute("JOIN "+chan);
        else
          server.execute("JOIN "+chan+" "+parts[2]);
      }
      else if(cmd.equals("j"))
      {
        sendString(source,"/join "+cumul[1]);
      }
      else if(cmd.equals("query"))
      {
        test(cmd,parts,1);
        server.getQuery(parts[1],true);
      }
		else if(cmd.equals("ignore"))
		{
		  test(cmd,parts,1);
			if(!server.ignore(parts[1]))
			{
		    server.addIgnore(parts[1]);
				source.report(getText(IRCTextProvider.INTERPRETOR_IGNORE_ON,parts[1]));
			}
		}
		else if(cmd.equals("unignore"))
		{
		  test(cmd,parts,1);
			if(server.ignore(parts[1]))
			{
			  server.removeIgnore(parts[1]);
				source.report(getText(IRCTextProvider.INTERPRETOR_IGNORE_OFF,parts[1]));
			}
		}
      else if(cmd.equals("server"))
      {
        test(cmd,parts,1);
        int port=6667;
        String pass="";
        if(parts.length>2) port=(new Integer(parts[2])).intValue();
        if(parts.length>3) pass=parts[3];
        String host=parts[1];
        if(server.isConnected()) server.disconnect();
        server.setServers(new String[] {host},new int[] {port},new String[] {pass});
        server.connect();
      }
      else if(cmd.equals("connect"))
      {
        server.connect();
      }
      else if(cmd.equals("disconnect"))
      {
        server.disconnect();
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

