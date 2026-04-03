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

package irc.gui.sdk;

import irc.*;

/**
 * A simple window for displaying a Channel.
 */
public class SimpleAWTChannel extends SimpleAWTSource implements ChannelListener2
{
  /**
   * The channel.
   */
  private Channel _channel;

  /**
   * Create a new SimpleAWTChannel.
   * @param config the global irc configuration.
   * @param channel the channel.
   */
  public SimpleAWTChannel(IRCConfiguration config,Channel channel)
  {
    super(config,channel);
    _channel=channel;
    //Listen to specific channel events.
    _channel.addChannelListener2(this);
  } //SimpleAWTChannel

  /**
   * Leave and close the window. Any ressources used by this instance will be
   * released and no further call should be performed on it.
   */
  public void leave()
  {
    _channel.removeChannelListener2(this);
    super.leave();
  } //leave

  /**
   * The channel has changed all its nick list.
   * @param nicks new nicks.
   * @param modes new modes.
   * @param channel the channel.
   */
  public void nickSet(String nicks[],String modes[],Channel channel)
  {
    String str="Channel users :";
    for(int i=0;i<nicks.length;i++) str+=" "+nicks[i];
    print(str);
  } //nickSet

  /**
   * The channel should empty its nick list.
   * @param channel the channel.
   */
  public void nickReset(Channel channel)
  {
    print("Nick list has been reset");
  } //nickReset
  
  /**
   * A new nick has joined.
   * @param nick the nick who joined.
   * @param mode nick mode.
   * @param channel the channel.
   */
  public void nickJoin(String nick,String mode,Channel channel)
  {
    print(nick+" has joined");
  } //nickJoin

  /**
   * A nick has quit.
   * @param nick the nick who quit.
   * @param reason reason.
   * @param channel the channel.
   */
  public void nickQuit(String nick,String reason,Channel channel)
  {
    print(nick+" has quit ("+reason+")");
  } //nickQuit

  /**
   * A nick has part.
   * @param nick the nick who part.
   * @param reason reason.
   * @param channel the channel.
   */
  public void nickPart(String nick,String reason,Channel channel)
  {
    print(nick+" has left ("+reason+")");
  } //nickPart

  /**
   * A nick has been kicked.
   * @param nick the nick who has been kicked.
   * @param by the nick who kicked.
   * @param reason kick reason.
   * @param channel the channel.
   */
  public void nickKick(String nick,String by,String reason,Channel channel)
  {
    print(nick+" has been kicked by "+by+" ("+reason+")");
  } //nickKick

  /**
   * The topic has been changed.
   * @param topic new topic.
   * @param by user who changed topic.
   * @param channel the channel.
   */
  public void topicChanged(String topic,String by,Channel channel)
  {
    //If the changing user is empty, it is a statement and not a
    //change event.
    if(by.length()>0)
    {
      print(by+" changes topic to "+topic);
    } //if
    else
    {
      print("topic is "+topic);
    } //else
  } //topicChanged

  /**
   * Channel mode applied.
   * @param mode applied mode.
   * @param from user who applied mode.
   * @param channel the channel.
   */
  public void modeApply(String mode,String from,Channel channel)
  {
    //If the changing user is empty, it is a statement and not a
    //change event.
    if(from.length()>0)
    {
      print("channel mode "+mode+" applied by "+from);
    } //if
    else
    {
      print("channel mode is "+mode);
    } //else
  } //modeApply

  /**
   * Nick mode applied.
   * @param nick user on wich mode applied.
   * @param mode applied mode.
   * @param from user who applied mode.
   * @param channel the channel.
   */
  public void nickModeApply(String nick,String mode,String from,Channel channel)
  {
    print(from+" sets mode "+mode+" on "+nick);
  } //nickModeApply

  /**
   * Nick changed.
   * @param oldNick old nick.
   * @param newNick new nick.
   * @param channel the channel.
   */
  public void nickChanged(String oldNick,String newNick,Channel channel)
  {
    print(oldNick+" changes nick to "+newNick);
  } //nickChanged

  /**
   * Whois bufferised information has been updated.
   * @param nick nick on wich new whois information is available.
   * @param whois whois string for nick.
   * @param channel the channel.
   */
  public void nickWhoisUpdated(String nick,String whois,Channel channel)
  {
    //We don't care about neither asl nor whois information.
  } //nickWhoisUpdated

} //SimpleAWTChannel
