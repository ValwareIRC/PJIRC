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

import irc.*;
import irc.security.DefaultSecuredProvider;
import java.awt.*;

/**
 * Root IRCApplet, what is actually displayed in the browser.
 */
public class IRCApplet extends java.applet.Applet implements ParameterProvider
{
  /**
   * 
   */
  private static final long serialVersionUID=1L;
  private IRCApplication _application;

  public void init()
  {
    try
    {
      EventDispatcher.disableBadThreadWarning();
      EventDispatcher.dispatchEventSyncEx(this,"startEff",new Object[0]);
      EventDispatcher.enableBadThreadWarning();
    }
    catch(Throwable ex)
    {
      throw new RuntimeException(ex.toString());
    }
  }

  public void destroy()
  {
    try
    {
      EventDispatcher.disableBadThreadWarning();
      EventDispatcher.dispatchEventSyncEx(this,"stopEff",new Object[0]);
      EventDispatcher.enableBadThreadWarning();
    }
    catch(Throwable ex)
    {
      throw new RuntimeException(ex.toString());
    }
  }

  /**
   * Internally used.
   */
  public void startEff()
  {
    try
    {
      // Enable WebSocket transport if configured (for browser-based usage)
      String ws=getParameter("websocket");
      if(ws!=null && (ws.equals("true") || ws.equals("1") || ws.equals("yes")))
      {
        DefaultSecuredProvider.useWebSocket=true;
        System.out.println("PJIRC: WebSocket transport enabled");
      }

      ParameterProvider provider=this;

      String useFileParameter=getParameter("fileparameter");
      if(useFileParameter!=null)
      {
        FileHandler file=new AppletFileHandler(this);
        provider=new ParameterMixer(provider,new StreamParameterProvider(file.getInputStream(useFileParameter)));
      }

      ConfigurationLoader loader=new ConfigurationLoader(provider,new AppletURLHandler(getAppletContext()),new AppletImageLoader(this),new AppletSoundHandler(this),new AppletFileHandler(this));
      IRCConfiguration ircConfiguration=loader.loadIRCConfiguration();
      StartupConfiguration startupConfiguration=loader.loadStartupConfiguration();

      _application=new IRCApplication(ircConfiguration,startupConfiguration,this);
      _application.init();
      setVisible(false);
      setVisible(true);
    }
    catch(Throwable e)
    {
      setLayout(new FlowLayout(FlowLayout.LEFT));
      add(new Label("Startup error : "+e));
    }
  }

  /**
   * Internally used.
   */
  public void stopEff()
  {
    if(_application!=null)
      _application.uninit();
    _application=null;

  }

  /** -- javascript support -- **/

  /**
   * Send the given string to the current source interpretor.
   * @param str string to send to the interpretor.
   */
  public void sendString(String str)
  {
    if(_application!=null) EventDispatcher.dispatchEventAsync(_application,"sendString",new Object[] {str});
  }

  /**
   * Send the given command to the given source interpretor.
   * @param serverName the source's server name, or an empty string if no server filtering needs to be done.
   * @param type the source type.
   * @param name the source name.
   * @param cmd the command to send.
   */
  public void sendString(String serverName,String type,String name,String cmd)
  {
    if(_application!=null) EventDispatcher.dispatchEventAsync(_application,"sendString",new Object[] {serverName,type,name,cmd});
  }
  
  /**
   * Set the current textfield text.
   * @param txt new textfield text.
   */
  public void setFieldText(String txt)
  {
    if(_application!=null) EventDispatcher.dispatchEventAsync(_application,"setFieldText",new Object[] {txt});
  }

  /**
   * Get the current textfield text.
   * @return the current textfield text.
   */
  public String getFieldText()
  {
    if(_application!=null)
    {
      try
      {
        return (String)EventDispatcher.dispatchEventAsyncAndWaitEx(_application,"getFieldText",new Object[0]);
      }
      catch(Throwable ex)
      {
        throw new RuntimeException(ex.toString());
      }
    }
    return "";
  }

  /**
   * Validate the current textfield text, as if user pressed return key.
   */
  public void validateText()
  {
    if(_application!=null) EventDispatcher.dispatchEventAsync(_application,"validateText",new Object[0]);
  }

  /**
   * Request the active source to gain focus.
   */
  public void requestSourceFocus()
  {
    if(_application!=null) EventDispatcher.dispatchEventAsync(_application,"requestSourceFocus",new Object[0]);
  }

  /**
   * Request the given source to gain focus.
   * @param serverName the source's server name, or an empty string if no server filtering needs to be done.
   * @param type the source type.
   * @param name the source name.
   */
  public void requestSourceFocus(String serverName,String type,String name)
  {
    if(_application!=null) EventDispatcher.dispatchEventAsync(_application,"requestSourceFocus",new Object[] {serverName,type,name});
  }
  
  /**
   * Send the given event value to the given plugin.
   * @param pluginName the plugin name.
   * @param event the event value to be sent.
   */
  public void sendPluginEvent(String pluginName,Object event)
  {
    if(_application!=null) EventDispatcher.dispatchEventAsync(_application,"sendPluginEvent",new Object[] {pluginName,event});
  }

  /**
   * Get the plugin value from the given plugin name.
   * @param pluginName the plugin name.
   * @param valueName the value name.
   * @return the returned plugin value, or null if the plugin is not found.
   */
  public Object getPluginValue(String pluginName,Object valueName)
  {
    if(_application!=null)
    {
      try
      {
        return EventDispatcher.dispatchEventAsyncAndWaitEx(_application,"getPluginValue",new Object[] {pluginName,valueName});
      }
      catch(Throwable ex)
      {
        throw new RuntimeException(ex.toString());
      }
    }
    return null;
  }
  
  /**
   * Get the IRCApplication instance. For advanced use only.
   * @return the IRCApplication instance.
   */
  public IRCApplication getIRCApplication()
  {
    return _application;
  }

}
