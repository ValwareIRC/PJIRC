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
import java.awt.*;

/**
 * A single server.
 */
class ServerItem
{
  /**
   * Hostname.
   */
  public String host;
  /**
   * Server post.
   */
  public int port;
  /**
   * Optionnal server password.
   */
  public String pass;
}

/**
 * Toolkit for Configuration creation.
 */
public class ConfigurationLoader
{
  private ParameterProvider _provider;
  private URLHandler _handler;
  private ImageLoader _loader;
  private SoundHandler _sound;
  private FileHandler _file;

  /**
   * Create a new IRCConfigurationLoader.
   * @param provider parameter provider to load the configuration from.
   * @param handler URL handler.
   * @param loader Image loader.
   * @param sound Sound handler.
   * @param file File handler.
   */
  public ConfigurationLoader(ParameterProvider provider,URLHandler handler,ImageLoader loader,SoundHandler sound,FileHandler file)
  {
    _provider=provider;
    _handler=handler;
    _loader=loader;
    _sound=sound;
    _file=file;
  }

  /**
   * Create a new IRCConfiguration object, using the given ParameterProvider.
   * @return a new IRCConfiguration instance.
   * @throws Exception if an error occurs.
   */
  public IRCConfiguration loadIRCConfiguration() throws Exception
  {
    return getIRCConfiguration();
  }

  /**
   * Create a new StartupConfiguration object, using the given ParameterProvider.
   * @return a new StartupConfiguration instance.
   * @throws Exception if a mandatory parameter is not supplied or if an error occurs.
   */
  public StartupConfiguration loadStartupConfiguration() throws Exception
  {
    return getStartupConfiguration();
  }

  private String getParameter(String key)
  {
    return _provider.getParameter(key);
  }

  private boolean getBoolean(String key,boolean def)
  {
    String v=getParameter(key);
    if(v==null) return def;
    v=v.toLowerCase(java.util.Locale.ENGLISH).trim();
    if(v.equals("true") || v.equals("on") || v.equals("1")) return true;
    return false;
  }

  private String getString(String key,String def)
  {
    String v=getParameter(key);
    if(v==null) return def;
    return v;
  }

  private int getInt(String key,int def)
  {
    String v=getParameter(key);
    if(v==null) return def;
    try
    {
      return Integer.parseInt(v);
    }
    catch(Exception e)
    {
      return def;
    }

  }

  private void readBackgroundConfig(IRCConfiguration config)
  {
    StringParser parser=new StringParser();
    String[] arr=getArray("style:backgroundimage");
    for(int i=0;i<arr.length;i++)
    {
      String cmd=arr[i];
      String back[]=parser.parseString(cmd);
      if(back.length>=4)
      {
        String type=back[0];
        String name=back[1];
        int tiling=new Integer(back[2]).intValue();
        String image=back[3];
        config.setBackgroundImage(type,name,image);
        config.setBackgroundTiling(type,name,tiling);
      }
    }
  }

  private TextProvider getTextProvider()
  {
    String lang=getString("language","english");
    String encoding=getString("languageencoding","");
    String extension=getString("lngextension","lng");
    String backlang=getString("backuplanguage","english");
    String backencoding=getString("backuplanguageencoding","");
    return new FileTextProvider(lang+"."+extension,encoding,backlang+"."+extension,backencoding,_file);
  }

  private String[] getArray(String name)
  {
    Vector v=new Vector();
    String cmd;
    int i=1;
    do
    {
      cmd=getParameter(name+i);
      if(cmd!=null) v.insertElementAt(cmd,v.size());
      i++;
    }
    while(cmd!=null);
    String[] ans=new String[v.size()];
    for(i=0;i<v.size();i++) ans[i]=(String)v.elementAt(i);
    return ans;
  }

  private void readSmileys(IRCConfiguration config)
  {
    String[] arr=getArray("style:smiley");
    for(int i=0;i<arr.length;i++)
    {
      String cmd=arr[i];
      int pos=cmd.indexOf(" ");
      if(pos!=-1)
      {
        String match=cmd.substring(0,pos).trim();
        String file=cmd.substring(pos+1).trim();
        config.addSmiley(match,file);
      }
    }
  }

  private void configureFonts(IRCConfiguration config)
  {
    //type name fname fsize
    StringParser parser=new StringParser();
    String[] arr=getArray("style:sourcefontrule");
    for(int i=0;i<arr.length;i++)
    {
      String cmd=arr[i];
      String back[]=parser.parseString(cmd);
      if(back.length>=4)
      {
        String type=back[0].toLowerCase(java.util.Locale.ENGLISH);
        String name=back[1].toLowerCase(java.util.Locale.ENGLISH);
        String fname=back[2].toLowerCase(java.util.Locale.ENGLISH);
        
        if(fname.startsWith("'") && fname.endsWith("'"))
          fname=fname.substring(1,fname.length()-1);
        
        int fsize=new Integer(back[3].toLowerCase(java.util.Locale.ENGLISH)).intValue();
        config.setFont(type,name,new Font(fname,Font.PLAIN,fsize));
      }
    }
  }

  private void configureTextColors(IRCConfiguration config)
  {
    //type name {index=value}*
    String[] arr=getArray("style:sourcecolorrule");
    for(int i=0;i<arr.length;i++)
    {
      StringTokenizer tok=new StringTokenizer(arr[i]);
      if(!tok.hasMoreElements()) continue;
      String type=(String)tok.nextElement();
      if(!tok.hasMoreElements()) continue;
      String name=(String)tok.nextElement();
      Color[] c=new Color[16];
      config.loadDefaultColors(c);
      while(tok.hasMoreElements())
      {
        String s=(String)tok.nextElement();
        int pos=s.indexOf('=');
        if(pos<0) continue;
        String before=s.substring(0,pos).trim();
        String after=s.substring(pos+1).trim();
        int index=Integer.parseInt(before);
        Color col=new Color(Integer.parseInt(after,16));
        if((index>=0) && (index<=15)) c[index]=col;
      }
      config.setSourceColor(type,name,c);
    }
  }

  private void readSound(IRCConfiguration config)
  {
    AudioConfiguration ac=config.getAudioConfiguration();
    if(getParameter("soundbeep")!=null) ac.setBeep(getParameter("soundbeep"));
    if(getParameter("soundquery")!=null) ac.setQuery(getParameter("soundquery"));
    String[] arr=getArray("soundword");
    for(int i=0;i<arr.length;i++)
    {
      String cmd=arr[i];
      cmd=cmd.trim();
      int pos=cmd.indexOf(' ');
      if(pos!=-1)
      {
        String word=cmd.substring(0,pos).trim();
        String sound=cmd.substring(pos+1).trim();
        ac.setWord(word,sound);
      }
    }
  }
  
  private IRCConfiguration getIRCConfiguration() throws Exception
  {
    String gui=getString("gui","notprovided");
    IRCConfiguration config=new IRCConfiguration(getTextProvider(),_handler,_loader,_sound,_file,_provider,new PrefixedParameterProvider(_provider,gui+":"));

    config.setJoinList(getString("authorizedjoinlist",""));
    config.setLeaveList(getString("authorizedleavelist",""));
    config.setCommandList(getString("authorizedcommandlist",""));

    config.set("style:floatingasl",getBoolean("style:floatingasl",false));
    config.set("style:floatingaslalpha",getInt("style:floatingaslalpha",170));
    config.set("style:backgroundimage",getBoolean("style:backgroundimage",false));
    config.set("style:bitmapsmileys",getBoolean("style:bitmapsmileys",false));
    config.set("style:linespacing",getInt("style:linespacing",0));
    config.set("style:maximumlinecount",getInt("style:maximumlinecount",1024));

    config.set("style:highlightlinks",getBoolean("style:highlightlinks",false));
    
    config.set("aslseparatorstring",getString("aslseparatorstring",""));
    config.set("noasldisplayprefix",getString("noasldisplayprefix",""));
    config.set("quitmessage",getString("quitmessage",""));
    config.set("asl",getBoolean("asl",false));
    config.set("aslmale",getString("aslmale","m"));
    config.set("aslfemale",getString("aslfemale","f"));
    config.set("useinfo",getBoolean("useinfo",false));
    config.set("coding",getInt("coding",1));
    config.set("userid",getString("userid",""));
    config.set("style:righttoleft",getBoolean("style:righttoleft",false));
    config.set("autoconnection",getBoolean("autoconnection",true));
    config.set("useidentserver",getBoolean("useidentserver",true));
    config.set("multiserver",getBoolean("multiserver",false));
    config.set("aslunknown",getString("aslunknown","x"));
    config.set("gui",getString("gui",null));
    config.set("fingerreply",getString("fingerreply","A lucky Plouf's IRC user"));
    config.set("userinforeply",getString("userinforeply","A lucky Plouf's IRC user"));
    config.set("allowdccchat",getBoolean("allowdccchat",true));
    config.set("allowdccfile",getBoolean("allowdccfile",true));
    config.set("disablequeries",getBoolean("disablequeries",false));
    config.set("autorejoin",getBoolean("autorejoin",false));
    
    // Enable TLS if ssl=true in config
    if(getBoolean("ssl",false))
    {
      irc.security.DefaultSecuredProvider.useTLS=true;
    }

    config.setInitialisation(getArray("init"));
    
    readBackgroundConfig(config);
    readSmileys(config);
    configureFonts(config);
    configureTextColors(config);
    readSound(config);

    return config;
  }

  private ServerItem[] readServers(String dhost,int dport,String dpass)
  {
    Vector res=new Vector();
    ServerItem item=new ServerItem();
    item.host=dhost;
    item.port=new Integer(dport).intValue();
    item.pass=dpass;
    res.insertElementAt(item,res.size());

    String[] arr=getArray("alternateserver");
    for(int i=0;i<arr.length;i++)
    {
      String cmd=arr[i];
      int pos=cmd.indexOf(" ");
      if(pos>=0)
      {
        String host=cmd.substring(0,pos).trim();
        String port=cmd.substring(pos+1).trim();
        String pass="";
        pos=port.indexOf(" ");
        if(pos>=0)
        {
          pass=port.substring(pos+1).trim();
          port=port.substring(0,pos).trim();
        }
        item=new ServerItem();
        item.host=host;
        item.port=new Integer(port).intValue();
        item.pass=pass;
        res.insertElementAt(item,res.size());
      }
    }
    ServerItem[] ans=new ServerItem[res.size()];
    for(int i=0;i<ans.length;i++) ans[i]=(ServerItem)res.elementAt(i);
    return ans;
  }

  private StartupConfiguration getStartupConfiguration() throws Exception
  {
    String nick=getParameter("nick");
    if(nick==null) throw new Exception("Mandatory 'nick' parameter not provided");
    String name=getParameter("name");
    if(name==null) name=getParameter("fullname");
    if(name==null) throw new Exception("Mandatory 'fullname' parameter not provided");
    String host=getParameter("host");
    if(host==null) throw new Exception("Mandatory 'host' parameter not provided");
    String pass=getParameter("password");
    if(pass==null) pass="";
    String sport=getParameter("port");
    if(sport==null) sport="6667";
    int port=new Integer(sport).intValue();
    String altNick=getParameter("alternatenick");
    if(altNick==null) altNick=nick+"?";
    String alias=getParameter("serveralias");
    if(alias==null) alias="";

    ServerItem[] items=readServers(host,port,pass);
    String[] hosts=new String[items.length];
    int[] ports=new int[items.length];
    String[] passs=new String[items.length];
    for(int i=0;i<items.length;i++)
    {
      hosts[i]=items[i].host;
      ports[i]=items[i].port;
      passs[i]=items[i].pass;
    }
    return new StartupConfiguration(nick,altNick,name,passs,hosts,ports,alias,getArray("command"),getArray("plugin"));
  }

}
