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

/**
 * Channel interpretor.
 */
public class ChannelInterpretor extends IRCInterpretor
{
  /**
   * Create a new ChannelInterpretor.
   * @param config global configuration.
   */
  public ChannelInterpretor(IRCConfiguration config)
  {
    super(config);
  }

  private boolean isChannel(String name,Source source)
  {
    if(name.length()==0) return false;
    Server s=source.getServer();
    if(s instanceof IRCServer)
    {
      char[] prefixes=((IRCServer)s).getChannelPrefixes();
      for(int i=0;i<prefixes.length;i++) if(name.charAt(0)==prefixes[i]) return true;
    }
    return false;
  }

  protected void handleCommand(Source source,String cmd,String[] parts,String[] cumul)
  {
    try
    {
      if(cmd.equals("part"))
      {
        if(parts.length==1)
        {
          sendString(source,"/part "+source.getName());
        }
        else
        {
          if(isChannel(parts[1],source))
            super.handleCommand(source,cmd,parts,cumul);
          else
            sendString(source,"/part "+source.getName()+" "+cumul[1]);
        }
      }
      else if(cmd.equals("hop"))
      {
        sendString(source,"/part");
        sendString(source,"/join "+source.getName());
      }
      else if(cmd.equals("onotice"))
      {
        test(cmd,parts,1);
        if(isChannel(parts[1],source))
          super.handleCommand(source,cmd,parts,cumul);
        else
          sendString(source,"/onotice "+source.getName()+" "+cumul[1]);
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

