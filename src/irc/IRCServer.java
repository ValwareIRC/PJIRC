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
 * FirstLineFilter, used to handle CTCP codes.
 */
class FirstLineFilter
{
  private IRCServer _server;
  private IRCConfiguration _ircConfiguration;
  private ServerManager _mgr;

  /**
   * Create a new FirstLineFilter
   * @param serv
   * @param mgr
   * @param config
   */
  public FirstLineFilter(IRCServer serv,ServerManager mgr,IRCConfiguration config)
  {
    _ircConfiguration=config;
    _mgr=mgr;
    _server=serv;
  }

  /**
   * Release this object.
   */
  public void release()
  {
    _mgr=null;
    _server=null;
  }

  /**
   * Perform any needed action from a channel message.
   * @param channel channel name.
   * @param nick nickname.
   * @param msg actual message.
   * @return true if message was handled, false otherwise.
   */
  public boolean performFromChannelMessage(String channel,String nick,String msg)
  {
    if(!msg.startsWith("\1")) return false;

    msg=msg.substring(1);
    msg=msg.substring(0,msg.length()-1);
    String cmd="";
    String param="";
    int pos=msg.indexOf(' ');
    if(pos==-1)
    {
      cmd=msg.toLowerCase(java.util.Locale.ENGLISH);
    }
    else
    {
      cmd=msg.substring(0,pos).toLowerCase(java.util.Locale.ENGLISH);
      param=msg.substring(pos+1);
    }

    if(cmd.equals("action"))
    {
      Channel c=_server.getChannel(channel,false);
      if(c!=null) c.action(nick,param);
    }
    else if(cmd.equals("sound"))
    {
      _ircConfiguration.getAudioConfiguration().play(param);
      _server.sendStatusMessage("\2\3"+"4"+"["+nick+" "+cmd.toUpperCase(java.util.Locale.ENGLISH)+"]");
    }
    return true;
  }

  /**
   * Perform any needed action from a nick message.
   * @param nick nickname.
   * @param msg actual message.
   * @return true if message was handled, false otherwise.
   */
  public boolean performFromNickMessage(String nick,String msg)
  {
    if(!msg.startsWith("\1")) return false;

    msg=msg.substring(1);
    msg=msg.substring(0,msg.length()-1);
    String cmd="";
    String param="";
    int pos=msg.indexOf(' ');
    if(pos==-1)
    {
      cmd=msg.toLowerCase(java.util.Locale.ENGLISH);
    }
    else
    {
      cmd=msg.substring(0,pos).toLowerCase(java.util.Locale.ENGLISH);
      param=msg.substring(pos+1);
    }

    boolean show=true;
    if(cmd.equals("action"))
    {
      show=false;
      Query q=_server.getQuery(nick,false);
      if(q!=null) q.action(nick,param);
    }
    else if(cmd.equals("version"))
    {
      String data="PJIRC "+_ircConfiguration.getVersion();
      _server.execute("NOTICE "+nick+" :\1VERSION "+data+"\1");
    }
    else if(cmd.equals("ping"))
    {
      _server.execute("NOTICE "+nick+" :\1PING "+param+"\1");
    }
    else if(cmd.equals("time"))
    {
      String data=new Date().toString();
      _server.execute("NOTICE "+nick+" :\1TIME "+data+"\1");
    }
    else if(cmd.equals("finger"))
    {
      String data=_ircConfiguration.getS("fingerreply");
      _server.execute("NOTICE "+nick+" :\1FINGER "+data+"\1");
    }
    else if(cmd.equals("userinfo"))
    {
      String data=_ircConfiguration.getS("userinforeply");
      _server.execute("NOTICE "+nick+" :\1USERINFO "+data+"\1");
    }
    else if(cmd.equals("clientinfo"))
    {
      String data="This client is a Java application supporting the following CTCP tags : ACTION VERSION PING TIME FINGER USERINFO CLIENTINFO SOUND DCC";
      _server.execute("NOTICE "+nick+" :\1CLIENTINFO "+data+"\1");
    }
    else if(cmd.equals("sound"))
    {
      _ircConfiguration.getAudioConfiguration().play(param);
    }
    else if(cmd.equals("dcc"))
    {
      StringParser sp=new StringParser();
      String[] args=sp.parseString(param.toLowerCase(java.util.Locale.ENGLISH));
      if(args.length>=2)
      {
        if(args[0].equals("chat") && args[1].equals("chat") && _ircConfiguration.getB("allowdccchat"))
        {
          if(args.length>=4)
          {
            boolean bres=false;
            Object[] res=_server.specialRequest("DCCChatRequest",new Object[] {nick});
            for(int i=0;i<res.length;i++) if(((Boolean)res[i]).booleanValue()) bres=true;
            
            if(bres)
            {
              try
              {
                DCCChatServer cserver=new DCCChatServer(_ircConfiguration,_server.getNick(),nick);
                cserver.openActive(args[2],args[3]);
                _mgr.newServer(cserver,false);
              }
              catch(Throwable ex)
              {
                ex.printStackTrace();
              }
            }
          }
        }
        if(args[0].equals("send") && _ircConfiguration.getB("allowdccfile"))
        {
          if(args.length>=5)
          {
            String fname=args[1];
            String ip=args[2];
            String port=args[3];
            String size=args[4];
            Object[] res=_server.specialRequest("DCCFileRequest",new Object[] {nick,fname,new Integer(size)});
            File dest=null;
            if(res.length>0) dest=(File)res[0];
            if(dest!=null)
            {
              try
              {
                DCCFileHandler handler=new DCCFileHandler(_ircConfiguration,nick,dest);
                handler.receive(ip,port,size);
                _mgr.newServer(handler,false);
              }
              catch(Throwable ex)
              {
                ex.printStackTrace();
              }
            }
          }
        }
      }
    }
    if(show) _server.sendStatusMessage("\2\3"+"4"+"["+nick+" "+cmd.toUpperCase(java.util.Locale.ENGLISH)+"]");
    return true;
  }

  /**
   * Perform any needed action from a notice message.
   * @param nick nickname.
   * @param msg actual message.
   * @return true if message was handled, false otherwise.
   */
  public boolean performFromNotice(String nick,String msg)
  {
    if(!msg.startsWith("\1")) return false;

    msg=msg.substring(1);
    msg=msg.substring(0,msg.length()-1);
    String cmd="";
    String param="";
    int pos=msg.indexOf(' ');
    if(pos==-1)
    {
      cmd=msg.toLowerCase(java.util.Locale.ENGLISH);
    }
    else
    {
      cmd=msg.substring(0,pos).toLowerCase(java.util.Locale.ENGLISH);
      param=msg.substring(pos+1);
    }

    Source source=_server.getDefaultSource();
    if(cmd.equals("ping"))
    {
      long d=(new Long(param)).longValue();
      long delta=(new Date()).getTime()-d;
      if(source!=null) source.report("\2\3"+"4"+_ircConfiguration.getText(IRCTextProvider.CTCP_PING_REPLY,nick,(delta/1000.0)+""));
      return true;
    }
    if(source!=null) source.report("\2\3"+"4"+"["+nick+" "+cmd.toUpperCase(java.util.Locale.ENGLISH)+" reply] : "+param);
    return true;
  }
}

/**
 * The IRC server.
 */
public class IRCServer extends IRCObject implements Server, ServerProtocolListener
{
  private ServerProtocol _protocol;
  private Hashtable _channels;
  private Hashtable _queries;
  private Hashtable _chanlist;
  private Status _status;

  private Hashtable _ignoreList;

  private ListenerGroup _listeners;
  private ListenerGroup _replylisteners;
  private ListenerGroup _messagelisteners;
  private String[] _askedNick;
  private String _nick;
  private String _userName;
  private int _tryNickIndex;
  private ModeHandler _mode;
  private String[] _host;
  private int[] _port;
  private String _passWord[];
  private int _tryServerIndex;
	private boolean _connected;
	private String _name;
	private Source _defaultSource;
	private boolean _serverLeaving;
	private boolean _registered;
	private FirstLineFilter _filter;
  //private boolean _nickWaiting=false;


  private char[] _nickModes={'o','h','v'};
  private char[] _nickPrefixes={'@','%','+'};
  private char[] _channelPrefixes={'#','&','!','+'};
  private char[][] _globalModes={{'b'},{'k'},{'l'},{'i','m','n','p','s','t','a','q','r'}};

  /**
   * Create a new IRCServer.
   * @param config global IRCConfiguration.
   * @param mgr the server manager.
   * @param nick claimed nick.
   * @param altNick claimed alternate nick.
   * @param userName user name.
   * @param name the server name.
   */
  public IRCServer(IRCConfiguration config,ServerManager mgr,String nick,String altNick,String userName,String name)
  {
    super(config);
    _filter=new FirstLineFilter(this,mgr,config);
    _serverLeaving=false;
    _name=name;
    _userName=userName;
    _askedNick=new String[2];
    _askedNick[0]=nick;
    _askedNick[1]=altNick;
    _nick=nick;
    _connected=false;
    _ignoreList=new Hashtable();

    _channels=new Hashtable();
    _queries=new Hashtable();
    _chanlist=new Hashtable();

    _listeners=new ListenerGroup();
    _replylisteners=new ListenerGroup();
    _messagelisteners=new ListenerGroup();

    _status=new Status(_ircConfiguration,this);
    _defaultSource=_status;

    _protocol=new ServerProtocol(_ircConfiguration);
    _protocol.addServerProtocolListener(this);


    _host=null;
    _mode=new ModeHandler(_globalModes,_nickModes);
  }

  /**
   * Send a special request event to all listeners.
   * @param request request string.
   * @param params request parameters.
   * @return results.
   */
  public Object[] specialRequest(String request,Object[] params)
  {
    return _listeners.sendEvent("specialServerRequest",request,this,params);
  }

  public void release()
  {
    _protocol.removeServerProtocolListener(this);
    _filter.release();
    super.release();
  }

  public Enumeration getSources()
  {
    Vector v=new Vector();
    Enumeration e;
    e=_channels.elements();
    while(e.hasMoreElements()) v.insertElementAt(e.nextElement(),v.size());
    e=_queries.elements();
    while(e.hasMoreElements()) v.insertElementAt(e.nextElement(),v.size());
    e=_chanlist.elements();
    while(e.hasMoreElements()) v.insertElementAt(e.nextElement(),v.size());
    if(_status!=null) v.insertElementAt(_status,v.size());
    return v.elements();
  }

  public void enumerateSourcesAsCreated(ServerListener lis)
  {
    Enumeration e;
    e=_channels.elements();while(e.hasMoreElements()) lis.sourceCreated((Source)e.nextElement(),this,new Boolean(false));
    e=_queries.elements();while(e.hasMoreElements()) lis.sourceCreated((Source)e.nextElement(),this,new Boolean(false));
    e=_chanlist.elements();while(e.hasMoreElements()) lis.sourceCreated((Source)e.nextElement(),this,new Boolean(false));
    if(_status!=null) lis.sourceCreated(_status,this,new Boolean(true));
  }

  public void enumerateSourcesAsRemoved(ServerListener lis)
  {
    Enumeration e;
    e=_channels.elements();while(e.hasMoreElements()) lis.sourceRemoved((Source)e.nextElement(),this);
    e=_queries.elements();while(e.hasMoreElements()) lis.sourceRemoved((Source)e.nextElement(),this);
    e=_chanlist.elements();while(e.hasMoreElements()) lis.sourceRemoved((Source)e.nextElement(),this);
    if(_status!=null) lis.sourceRemoved(_status,this);
  }

  public void setDefaultSource(Source s)
  {
    _defaultSource=s;
  }

  /**
   * Get the default server source, or null if no default source is defined.
   * @return default source.
   */
  public Source getDefaultSource()
  {
    return _defaultSource;
  }

  /**
   * Set default configuration for the next connection.
   * @param host server host.
   * @param port server port.
   * @param passWord server password.
   */
  public void setServers(String host[],int port[],String passWord[])
  {
    _tryServerIndex=0;
    _host=new String[host.length];
    for(int i=0;i<host.length;i++) _host[i]=host[i];
    _port=new int[port.length];
    for(int i=0;i<port.length;i++) _port[i]=port[i];
    _passWord=new String[passWord.length];
    for(int i=0;i<passWord.length;i++) _passWord[i]=passWord[i];
  }

  public void connect()
  {
    _tryServerIndex=0;
    if(_host!=null)
      connect(_host,_port,_passWord);
  }

  private void connect(String host[],int port[],String[] passWord)
  {
    _registered=false;
    //if(_nickWaiting) return;
    if(_tryServerIndex==_host.length) return;
    _tryNickIndex=0;
    _passWord=passWord;
    if(_protocol.connecting())
    {
      sendStatusMessage(getText(IRCTextProvider.SERVER_UNABLE_TO_CONNECT_STILL,host[_tryServerIndex],_host[_tryServerIndex]));
      return;
    }
    if(_protocol.connected())
    {
      //sendStatusMessage(getText(IRCTextProvider.SERVER_DISCONNECTED,_host[_tryServerIndex]));
      //disconnect();
      _protocol.disconnect();
    }
	  _connected=false;
    sendStatusMessage(getText(IRCTextProvider.SERVER_CONNECTING));
    _protocol.connect(host[_tryServerIndex],port[_tryServerIndex]);
  }

  /**
   * Disconnect from the irc server.
   */
  public void disconnect()
  {
    //if(_nickWaiting) return;
    if(_protocol.connected())
    {
      if(_ircConfiguration.getS("quitmessage").length()==0)
      {
        execute("QUIT");
      }
      else
      {
        execute("QUIT :"+_ircConfiguration.get("quitmessage"));
      }
    }
    else
    {
      sendStatusMessage(getText(IRCTextProvider.SERVER_NOT_CONNECTED));
    }
  }

  /**
   * Return true if connected to the server, false otherwise.
   * @return connected state.
   */
  public boolean isConnected()
  {
	  return _connected;
  }

  public void connectionFailed(String message,String host)
  {
    sendStatusMessage(getText(IRCTextProvider.SERVER_UNABLE_TO_CONNECT,message));
    _tryServerIndex++;
    if(_tryServerIndex<_host.length)
      connect(_host,_port,_passWord);
  }

	private void nickUsed()
	{
	  if(_tryNickIndex>=_askedNick.length)
	  {
      //_nickWaiting=true;
      Object[] res=_listeners.sendEvent("cannotUseRequestedNicknames",new Object[] {this});
      if(res.length>0) _askedNick=(String[])res[0];
      //_nickWaiting=false;
	    _tryNickIndex=0;
	  }
	  else
	    if(_askedNick[_tryNickIndex].indexOf("?")==-1) _tryNickIndex++;
	}

	private void register()
	{
	  String tryUseNick=_askedNick[_tryNickIndex];
	  if(tryUseNick.length()==0) tryUseNick="Anon????";
	  String ans="";
    for(int i=0;i<tryUseNick.length();i++)
    {
      char c=tryUseNick.charAt(i);
      if(c=='?') c=(char)('0'+Math.random()*10);
      ans+=c;
    }
    if(_passWord[_tryServerIndex].length()>0) execute("pass "+_passWord[_tryServerIndex]);
    execute("nick "+ans);
    String name=_ircConfiguration.getS("userid");
    if(name.length()==0) name=ans;
    if(!_registered)
    {
      _registered=true;
      execute("user "+name+" 0 0 :"+_userName);
    }
	}

  /**
   * Get the local port of the remote connection.
   * @return the local, client-side port of the remote connection.
   */
	public int getLocalPort()
	{
	  return _protocol.getLocalPort();
	}

  public void connected(String host)
  {
    sendStatusMessage(getText(IRCTextProvider.SERVER_LOGIN));
		register();
  }

  private void clear(Hashtable l)
  {
    Enumeration e;
    e=l.elements();
    while(e.hasMoreElements()) _listeners.sendEvent("sourceRemoved",e.nextElement(),this);
    e=l.elements();
    while(e.hasMoreElements()) ((Source)e.nextElement()).release();
    l.clear();
  }

  public void disconnected(String host)
  {
    sendStatusMessage(getText(IRCTextProvider.SERVER_DISCONNECTED,host));

    clear(_channels);
    clear(_queries);
    clear(_chanlist);

    _mode.reset();
    if(_status!=null) _status.modeChanged(getMode());
    //_defaultSource=null;

    _connected=false;
    _listeners.sendEvent("serverDisconnected",this);

    if(_serverLeaving)
    {
      _listeners.sendEvent("sourceRemoved",_status,this);
      deleteStatus("");
      _listeners.sendEvent("serverLeft",this);
    }
  }

  public void sendStatusMessage(String msg)
  {
    if(_status!=null) _status.report(msg);
  }

  /**
   * Get all the channels.
   * @return an enumeration of channels.
   */
  public Enumeration getChannels()
  {
    return _channels.elements();
  }

  /**
   * Get all the queries.
   * @return an enumeration of queries.
   */
  public Enumeration getQueries()
  {
    return _queries.elements();
  }

  /**
   * Get all the chanlists.
   * @return an enumeration of chanlists.
   */
  public Enumeration getChanLists()
  {
    return _chanlist.elements();
  }

  /**
   * Get the channel from its name. If this channel doesn't exist, it is created only
   * if create boolean is set.
   * @param name channel name.
   * @param create true if channel must be created if not existing.
   * @return channel, or null.
   */
  public Channel getChannel(String name,boolean create)
  {
    Channel c=(Channel)_channels.get(name.toLowerCase(java.util.Locale.ENGLISH));
    if((c==null) && create)
    {
      c=new Channel(_ircConfiguration,name,this);
      _channels.put(name.toLowerCase(java.util.Locale.ENGLISH),c);
      _listeners.sendEvent("sourceCreated",c,this,new Boolean(true));
    }
    return c;
  }

  /**
   * Get the query from its name. If this query doesn't exist, it is created.
   * The query cannot be get if the server is not connected.
   * @param nick query name.
   * @param local true if this query has been created following a local request.
   * @return query, or null if server was not connected.
   */
  public Query getQuery(String nick,boolean local)
  {
    if(!_connected) return null;
    if(_ircConfiguration.getB("disablequeries")) return null;
    Query c=(Query)_queries.get(nick.toLowerCase(java.util.Locale.ENGLISH));
    if(c==null)
    {
      c=new Query(_ircConfiguration,nick,this);
      _queries.put(nick.toLowerCase(java.util.Locale.ENGLISH),c);
      _listeners.sendEvent("sourceCreated",c,this,new Boolean(local));

    }
    return c;
  }

  /**
   * Get the chanlist from its name. If this chanlist doesn't exist, it is created.
   * @param name chanlist name.
   * @return channel.
   */
  private ChanList getChanList(String name)
  {
    ChanList c=(ChanList)_chanlist.get(name.toLowerCase(java.util.Locale.ENGLISH));
    if(c==null)
    {
      c=new ChanList(_ircConfiguration,this,name);
      _chanlist.put(name.toLowerCase(java.util.Locale.ENGLISH),c);
      _listeners.sendEvent("sourceCreated",c,this,new Boolean(true));
    }
    return c;
  }

  /**
   * Request to leave the given channel.
   * @param name channel name.
   */
  public void leaveChannel(String name)
  {
    execute("part "+name);
  }

  /**
   * Request to leave the given query.
   * @param name query name.
   */
  public void leaveQuery(String name)
  {
    Query q=getQuery(name,false);
    if(q==null) return;
    _listeners.sendEvent("sourceRemoved",q,this);
    deleteQuery(name);
  }

  public void leave()
  {
    leaveStatus("");
  }

  /**
   * Request to leave the status. This will cause server leaving.
   * @param name Status name. Unused.
   */
  public void leaveStatus(String name)
  {
    if(_status==null) return;
    if(isConnected())
    {
      _serverLeaving=true;
      disconnect();
    }
    else
    {
      _listeners.sendEvent("sourceRemoved",_status,this);
      deleteStatus("");
      _listeners.sendEvent("serverLeft",this);
    }
    /*long time=System.currentTimeMillis();
    while(isConnected())
    {
      try
      {
        Thread.sleep(100);
        if(System.currentTimeMillis()-time>10000) break;
      }
      catch(InterruptedException ex)
      {
      }
    }*/
    /*_listeners.sendEvent("sourceRemoved",_status,this);
    deleteStatus(name);
    _listeners.sendEvent("serverLeft",this);*/
  }

  /**
   * Request to leave the given channel list.
   * @param name chanlist name.
   */
  public void leaveChanList(String name)
  {
    _listeners.sendEvent("sourceRemoved",getChanList(name),this);
    deleteChanList(name);
  }

  private void deleteSource(Source src)
  {
    if(src==_defaultSource) _defaultSource=null;
    src.release();
  }

  private void deleteChannel(String name)
  {
    deleteSource((Source)_channels.remove(name.toLowerCase(java.util.Locale.ENGLISH)));
  }

  private void deleteQuery(String name)
  {
    deleteSource((Source)_queries.remove(name.toLowerCase(java.util.Locale.ENGLISH)));
  }

  private void deleteChanList(String name)
  {
    deleteSource((Source)_chanlist.remove(name.toLowerCase(java.util.Locale.ENGLISH)));
  }

  private void deleteStatus(String name)
  {
    deleteSource(_status);
    _status=null;
  }

  public String getServerName()
  {
    if(_name.length()==0)
    {
      if(_tryServerIndex<_host.length)
        return _host[_tryServerIndex];
      return _host[0];
    }
       
    return _name;
  }

  /**
   * Get this server's status, or null if this server has no status.
   * @return the status, or null if the server hasno status.
   */
  public Status getStatus()
  {
    return _status;
  }

  /**
   * Add a server listener.
   * @param l listener to add.
   */
  public void addServerListener(ServerListener l)
  {
    _listeners.addListener(l);
  }

  /**
   * Remove a listener.
   * @param l listener to remove.
   */
  public void removeServerListener(ServerListener l)
  {
    _listeners.removeListener(l);
  }

  /**
   * Add a reply listener.
   * @param l listener to add.
   */
  public void addReplyServerListener(ReplyServerListener l)
  {
    _replylisteners.addListener(l);
  }

  /**
   * Add a message listener.
   * @param l listener to add.
   */
  public void addMessageServerListener(MessageServerListener l)
  {
    _messagelisteners.addListener(l);
  }

  /**
   * Remove a reply listener.
   * @param l listener to remove.
   */
  public void removeReplyServerListener(ReplyServerListener l)
  {
    _replylisteners.removeListener(l);
  }

  /**
   * Remove a message listener.
   * @param l listener to remove.
   */
  public void removeMessageServerListener(MessageServerListener l)
  {
    _messagelisteners.removeListener(l);
  }

  /**
   * Get an array of all known channel prefixes.
   * @return an array of all channel prefixes.
   */
  public char[] getChannelPrefixes()
  {
    return _channelPrefixes;
  }

  /**
   * Get an array of all known nickname prefixes.
   * @return array of all nickname prefixes.
   */
  public char[] getNickPrefixes()
  {
    return _nickPrefixes;
  }

  /**
   * Get an array of all known nickname modes.
   * @return array of all nickname modes.
   */
  public char[] getNickModes()
  {
    return _nickModes;
  }

  /**
   * Get an array of all known A,B,C,D channel modes.
   * @return array of all channel modes. This is an array of four char arrays.
   */
  public char[][] getChannelModes()
  {
    return _globalModes;
  }

  /**
   * Get the nick prefix associated with the given nick mode.
   * @param mode nick mode.
   * @return nick prefix for this mode.
   */
  public String getNickPrefix(String mode)
  {
    if(mode.length()==0) return "";
    char cmode=mode.charAt(0);
    for(int i=0;i<_nickModes.length;i++) if(_nickModes[i]==cmode) return ""+_nickPrefixes[i];
    return "";
  }

  /**
   * Get the nick mode associated with the given nick prefix.
   * @param prefix nick prefix.
   * @return nick mode for this prefix.
   */
  public String getNickMode(String prefix)
  {
    if(prefix.length()==0) return "";
    char cprefix=prefix.charAt(0);
    for(int i=0;i<_nickPrefixes.length;i++) if(_nickPrefixes[i]==cprefix) return ""+_nickModes[i];
    return "";
  }

  private void setNicks(Channel c,Vector nicks)
  {
    String[] n=new String[nicks.size()];
    String[] modes=new String[nicks.size()];

    for(int i=0;i<nicks.size();i++)
    {
      n[i]=(String)nicks.elementAt(i);
      modes[i]="";
      if(n[i].length()>0)
      {
        modes[i]=getNickMode(""+n[i].charAt(0));
        if(modes[i].length()!=0) n[i]=n[i].substring(1);
      }
    }
    c.setNicks(n,modes);
  }

  private void decodeVariable(String key,String val)
  {
    if(key.toLowerCase(java.util.Locale.ENGLISH).equals("prefix"))
    {
      if(!val.startsWith("(")) return;
      int pos=val.indexOf(")");
      if(pos<0) return;
      String modes=val.substring(1,pos);
      String prefixes=val.substring(pos+1);
      if(prefixes.length()!=modes.length()) return;

      _nickModes=new char[modes.length()];
      for(int i=0;i<modes.length();i++) _nickModes[i]=modes.charAt(i);
      _nickPrefixes=new char[modes.length()];
      for(int i=0;i<prefixes.length();i++) _nickPrefixes[i]=prefixes.charAt(i);
    }
    else if(key.toLowerCase(java.util.Locale.ENGLISH).equals("chantypes"))
    {
      _channelPrefixes=new char[val.length()];
      for(int i=0;i<_channelPrefixes.length;i++) _channelPrefixes[i]=val.charAt(i);
    }
    else if(key.toLowerCase(java.util.Locale.ENGLISH).equals("chanmodes"))
    {
      int pos=val.indexOf(',');
      if(pos<0) return;
      String a=val.substring(0,pos);
      val=val.substring(pos+1);
      pos=val.indexOf(',');
      if(pos<0) return;
      String b=val.substring(0,pos);
      val=val.substring(pos+1);
      pos=val.indexOf(',');
      if(pos<0) return;
      String c=val.substring(0,pos);
      String d=val.substring(pos+1);
      _globalModes=new char[4][];
      _globalModes[0]=new char[a.length()];for(int i=0;i<a.length();i++) _globalModes[0][i]=a.charAt(i);
      _globalModes[1]=new char[b.length()];for(int i=0;i<b.length();i++) _globalModes[1][i]=b.charAt(i);
      _globalModes[2]=new char[c.length()];for(int i=0;i<c.length();i++) _globalModes[2][i]=c.charAt(i);
      _globalModes[3]=new char[d.length()];for(int i=0;i<d.length();i++) _globalModes[3][i]=d.charAt(i);
    }
  }

  private void learnServerVariables(String var[])
  {
    for(int i=1;i<var.length;i++)
    {
      String v=var[i];
      int pos=v.indexOf('=');
      String key;
      String val;
      if(pos<0)
      {
        key=v;
        val="";
      }
      else
      {
        key=v.substring(0,pos);
        val=v.substring(pos+1);
      }
      decodeVariable(key,val);
    }
    _mode=new ModeHandler(_globalModes,_nickModes);
  }

  public void replyReceived(String prefix,String id,String params[])
  {
    Object[] b=_replylisteners.sendEvent("replyReceived",new Object[] {prefix,id,params,this});
    for(int i=0;i<b.length;i++) if(((Boolean)b[i]).booleanValue()) return;
    
    if(id.equals("324")) //mode : RPL_CHANNELMODEIS
    {
      Channel c=getChannel(params[1],false);
      if(c!=null)
      {
        String mode="";
        for(int i=2;i<params.length;i++) mode+=" "+params[i];
        mode=mode.substring(1);
        c.applyMode(mode,"");
      }
    }
    else if(id.equals("332")) //topic : RPL_TOPIC
    {
      Channel c=getChannel(params[1],false);
      if(c!=null) c.setTopic(params[2],"");
    }
    else if(id.equals("353")) //names : RPL_NAMREPLY
    {
      int first=1;
      if(params[1].length()==1) first++;
      Channel c=getChannel(params[first],false);
      if(c!=null)
      {
        String nick="";
        Vector nicks=new Vector();
        for(int i=0;i<params[first+1].length();i++)
        {
          char u=params[first+1].charAt(i);
          if(u==' ')
          {
            if(nick.length()>0) nicks.insertElementAt(nick,nicks.size());
            nick="";
          }
          else
          {
            nick+=u;
          }
        }
        if(nick.length()>0) nicks.insertElementAt(nick,nicks.size());
        setNicks(c,nicks);
      }
    }
    else if(id.equals("001")) //RPL_WELCOME
    {
      String nick=params[0];
      if(!(nick.equals(_nick)))
      {
        _nick=nick;
        if(_status!=null) _status.nickChanged(nick);
      }
			_connected=true;
      _listeners.sendEvent("serverConnected",this);
    }
    else if(id.equals("005")) //RPL_ISUPPORT
    {
      learnServerVariables(params);
    }
    else if(id.equals("321")) ///list begin : RPL_LISTSTART
    {
      getChanList(_host[_tryServerIndex]).begin();
    }
    else if(id.equals("322")) ///list : RPL_LIST
    {
      String name=params[1];
      int count=new Integer(params[2]).intValue();
			if((count<32767) && (isChannel(name)))
			{
        String topic=params[3];
        getChanList(_host[_tryServerIndex]).addChannel(new ChannelInfo(name,topic,count));
			}
    }
    else if(id.equals("323")) ///list end : RPL_LISTEND
    {
      getChanList(_host[_tryServerIndex]).end();
    }
		else if(id.equals("433")) //nick used : ERR_NICKNAMEINUSE
		{
		  if(!_connected)
		  {
		    nickUsed();
		    register();
		  }
		}
    //We failed to rejoin the channel
    //ERR_INVITEONLYCHAN || ERR_CHANNELISFULL || ERR_NOSUCHCHANNEL || ERR_BANNEDFROMCHAN || ERR_BADCHANNELKEY || ERR_BADCHANMASK || ERR_TOOMANYCHANNELS 
    else if(id.equals("473") || id.equals("471") || id.equals("403") || id.equals("474") || id.equals("475") || id.equals("476") || id.equals("405"))
    {
      String cname=params[1];
      Channel channel=getChannel(cname,false);
      if(channel!=null)
      {
        sendStatusMessage(getText(IRCTextProvider.SERVER_AUTOREJOIN_FAILED,cname));
        _listeners.sendEvent("sourceRemoved",channel,this);
        deleteChannel(cname);
      }
    }
    //We're performing an action on a channel we're not into
    else if(id.equals("442")) //ERR_NOTONCHANNEL
    {
      Channel chan=getChannel(params[1],false);
      if(chan!=null)
      {
        _listeners.sendEvent("sourceRemoved",chan,this);
        deleteChannel(chan.getName());
      }
    }
    else
    {
      /*String toSend="";
      for(int i=1;i<params.length;i++) toSend+=" "+params[i];
      toSend=toSend.substring(1);
      sendStatusMessage(toSend);*/
    }

  }

  private String extractNick(String full)
  {
    int pos=full.indexOf('!');
    if(pos==-1) return full;
    return full.substring(0,pos);
  }

  private boolean isChannel(String name)
  {
    if(name.length()==0) return false;
    for(int i=0;i<_channelPrefixes.length;i++) if(name.charAt(0)==_channelPrefixes[i]) return true;
    return false;
  }

  private void globalNickRemove(String nick,String reason)
  {
    Enumeration e=_channels.elements();
    while(e.hasMoreElements())
    {
      Channel c=(Channel)e.nextElement();
      if(c.hasNick(nick)) c.quitNick(nick,reason);
    }
  }

  private void globalNickChange(String oldNick,String newNick)
  {
    Enumeration e;
    e=_channels.elements();
    while(e.hasMoreElements())
    {
      Channel c=(Channel)e.nextElement();
      if(c.hasNick(oldNick)) c.changeNick(oldNick,newNick);
    }

    Query q=(Query)_queries.get(oldNick.toLowerCase(java.util.Locale.ENGLISH));
    if(q!=null)
    {
      _queries.remove(oldNick.toLowerCase(java.util.Locale.ENGLISH));
      q.changeNick(newNick);
      Query existing=(Query)_queries.get(newNick.toLowerCase(java.util.Locale.ENGLISH));
      if(existing!=null) existing.leave();
      _queries.put(newNick.toLowerCase(java.util.Locale.ENGLISH),q);
    }
  }

  /**
   * Return true if this server is ignoring the given nick, false otherwise.
   * @param nick nick to test.
   * @return the ignore status of the given nick.
   */
	public synchronized boolean ignore(String nick)
	{
	  return _ignoreList.get(nick)!=null;
	}

  /**
   * Ignore the given nick.
   * @param nick nick to ignore.
   */
	public synchronized void addIgnore(String nick)
	{
	  _ignoreList.put(nick,nick);
	}

  /**
   * Remove the given list from the ignore list.
   * @param nick nick to remove from ignore list.
   */
	public synchronized void removeIgnore(String nick)
	{
	  _ignoreList.remove(nick);
	}

  public void messageReceived(String prefix,String command,String params[])
  {
    Object[] b=_messagelisteners.sendEvent("messageReceived",new Object[] {prefix,command,params,this});
    for(int i=0;i<b.length;i++) if(((Boolean)b[i]).booleanValue()) return;


    String toSend="";
    for(int i=0;i<params.length;i++) toSend+=" "+params[i];


    command=command.toLowerCase(java.util.Locale.ENGLISH);

    String nick=extractNick(prefix);

    if(command.equals("notice"))
    {
      if(!ignore(nick))
      {
        if(!_filter.performFromNotice(nick,params[1]))
          if(_defaultSource!=null) _defaultSource.noticeReceived(nick,params[1]);
      }
    }
    else if(command.equals("privmsg"))
    {
		  if(!ignore(nick))
			{
        if(isChannel(params[0]))
        {
          if(!_filter.performFromChannelMessage(params[0],nick,params[1]))
          {
            Channel c=getChannel(params[0],false);
            if(c!=null) c.messageReceived(nick,params[1]);
          }
        }
        else
        {
          if(!_filter.performFromNickMessage(nick,params[1]))
          {
            Query q=getQuery(nick,false);
            if(q!=null) q.messageReceived(nick,params[1]);
          }
        }
			}
    }
    else if(command.equals("join"))
    {
      if(!nick.equals(getNick()))
      {
        Channel c=getChannel(params[0],false);
        if(c!=null) c.joinNick(nick,"");
      }
      else
      {
        Channel c=getChannel(params[0],true);
        if(c!=null)
        {
          c.resetNicks();
          execute("mode "+params[0]);
        }
      }
    }
    else if(command.equals("part"))
    {
      Channel c=getChannel(params[0],false);
      if(c!=null)
      {
        if(params.length>1)
        {
          c.partNick(nick,params[1]);
        }
        else
        {
          c.partNick(nick,"");
        }
        if(nick.equals(getNick()))
        {
          _listeners.sendEvent("sourceRemoved",c,this);
          deleteChannel(c.getName());
        }
      }
    }
    else if(command.equals("kick"))
    {
      Channel c=getChannel(params[0],false);
      if(c!=null)
      {
        String target=params[1];
        String reason="";
        if(params.length>2) reason=params[2];
        c.kickNick(target,nick,reason);
        if(target.equals(getNick()))
        {
          if(_ircConfiguration.getB("autorejoin"))
          {
            c.report(getText(IRCTextProvider.SERVER_AUTOREJOIN_ATTEMPT,c.getName()));
            execute("join "+params[0]);
          }
          else
          {
            _listeners.sendEvent("sourceRemoved",c,this);
            deleteChannel(c.getName());
          }
        }
      }
    }
    else if(command.equals("topic"))
    {
      Channel c=getChannel(params[0],false);
      if(c!=null) c.setTopic(params[1],nick);
    }
    else if(command.equals("mode"))
    {
      String full="";
      for(int i=1;i<params.length;i++) full+=params[i]+" ";
      if(isChannel(params[0]))
      {
        Channel c=getChannel(params[0],false);
        if(c!=null)
        {
          MultiModeHandler h=new MultiModeHandler(full,_globalModes,_nickModes);
          while(!h.terminated())
          {
            h.next();
            if(h.isPrefix() || h.isModeA())
            {
              c.applyUserMode(h.getParameter(),h.getMode(),nick);
            }
            else
            {
              if(h.hasParameter())
                c.applyMode(h.getMode()+" "+h.getParameter(),nick);
              else
                c.applyMode(h.getMode(),nick);
            }
          }
        }
      }
      else if(nick.equals(getNick()))
      {
        _mode.apply(full);
        if(_status!=null) _status.modeChanged(getMode());
      }
    }
    else if(command.equals("nick"))
    {
      if(nick.equals(getNick()))
      {
        _nick=params[0];
        if(_status!=null) _status.nickChanged(getNick());
      }
      globalNickChange(nick,params[0]);
    }
    else if(command.equals("quit"))
    {
      if(params.length>0)
        globalNickRemove(nick,params[0]);
      else
        globalNickRemove(nick,"");
    }
    else if(command.equals("ping"))
    {
      execute("pong :"+params[0]);
   //   sendStatusMessage("\3"+"3"+"PING? PONG!");
    }
    else if(command.equals("invite"))
    {
      String invited=params[0];
      String channel=params[1];
      if(invited.equals(getNick()))
      {
        if(_status!=null) _status.invited(channel,nick);

        //if(_defaultSource!=null) _defaultSource.report(getText(IRCTextProvider.SOURCE_YOU_INVITED,nick,channel));
      }
    }
    else if(command.equals("error"))
    {
      sendStatusMessage(getText(IRCTextProvider.SERVER_ERROR,params[0]));
    }
    else
    {
      //   System.out.println("("+command+") "+prefix+" -> "+toSend);
    }

  }

  public String getNick()
  {
    return _nick;
  }

  public String getUserName()
  {
    return _userName;
  }

  /**
   * Get the current status mode.
   * @return status mode.
   */
  public String getMode()
  {
    return _mode.getMode();
  }

  public void say(String destination,String str)
  {
    execute("PRIVMSG "+destination+" :"+str);
  }

  public void execute(String str)
  {
    int pos=str.indexOf(' ');
    if(pos>=0)
    {
      String cmd=str.substring(0,pos).toLowerCase(java.util.Locale.ENGLISH);
      if(cmd.equals("join"))
      {
        String rem=str.substring(pos+1);
        pos=rem.indexOf(' ');
        if(pos>=0) rem=rem.substring(0,pos);
        if(!_ircConfiguration.mayJoin(rem)) return;
      }
      else if(cmd.equals("part"))
      {
        String rem=str.substring(pos+1);
        pos=rem.indexOf(' ');
        if(pos>=0) rem=rem.substring(0,pos);
        if(!_ircConfiguration.mayLeave(rem)) return;
      }
    }

    pos=str.indexOf(' ');
    if(pos>0)
    {
      String cmd=str.substring(0,pos).toUpperCase(java.util.Locale.ENGLISH);
      String param=str.substring(pos+1);
      str=cmd+" "+param;
    }
    else
    {
      str=str.toUpperCase(java.util.Locale.ENGLISH);
    }
    sendString(str);
  }

  private void sendString(String str)
  {
    try
    {
      _protocol.sendString(str);
    }
    catch(Exception e)
    {
      sendStatusMessage(getText(IRCTextProvider.SERVER_ERROR,e.getMessage()));
    }
  }

}

