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
 * A default interpretor.
 */
public class DefaultInterpretor extends BasicInterpretor
{
  private StartupConfiguration _start;
  private ServerManager _mgr;
  private PluginManager _plugin;

  /**
   * Create a new DefaultInterpretor.
   * @param config global irc configuration.
   * @param start statup configuration.
   * @param mgr the server manager to be called upon server creation.
   * @param plugin the plugin manager.
   */
  public DefaultInterpretor(IRCConfiguration config,StartupConfiguration start,ServerManager mgr,PluginManager plugin)
  {
    super(config);
    _start=start;
    _mgr=mgr;
    _plugin=plugin;
  }

  protected void handleCommand(Source source,String cmd,String[] parts,String[] cumul)
  {
    try
    {
      if(cmd.equals("newserver"))
      {
        if(_ircConfiguration.getB("multiserver"))
        {
          test(cmd,parts,2);
          int port=6667;
          String pass="";
          String alias=parts[1];
          if(parts.length>3) port=new Integer(parts[3]).intValue();
          if(parts.length>4) pass=parts[4];
          String host=parts[2];

          IRCServer server=new IRCServer(_ircConfiguration,_mgr,_start.getNick(),_start.getAltNick(),_start.getName(),alias);
          server.setServers(new String[] {host},new int[] {port},new String[] {pass});
          _mgr.newServer(server,true);
        }
        else
        {
          source.report(getText(IRCTextProvider.INTERPRETOR_MULTISERVER_DISABLED));
        }
      }
      else if(cmd.equals("load"))
      {
        test(cmd,parts,1);
        _plugin.loadPlugin(parts[1]);
      }
      else if(cmd.equals("unload"))
      {
        test(cmd,parts,1);
        _plugin.unloadPlugin(parts[1]);
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

