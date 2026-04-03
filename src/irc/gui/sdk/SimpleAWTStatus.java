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
 * A simple window for displaying a Status.
 */
public class SimpleAWTStatus extends SimpleAWTSource implements StatusListener
{
  private Status _status;

  /**
   * Create a new SimpleAWTStatus.
   * @param config the IRCConfiguration instance, always usefull...
   * @param status the status we're supposed to handle.
   */
  public SimpleAWTStatus(IRCConfiguration config,Status status)
  {
    super(config,status);
    _status=status;
    //Listener to specific status events.
    _status.addStatusListener(this);
  } //SimpleAWTStatus

  /**
   * Leave and close the window. Any ressources used by this instance will be
   * released and no further call should be performed on it.
   */
  public void leave()
  {
    _status.removeStatusListener(this);
    super.leave();
  } //leave

  /**
   * Our nick has changed.
   * @param nick new nick.
   * @param status the status.
   */
  public void nickChanged(String nick,Status status)
  {
    print("Your nick is now "+nick);
  } //nickChanged

  /**
   * Our mode has changed.
   * @param mode new mode.
   * @param status the status.
   */
  public void modeChanged(String mode,Status status)
  {
    print("mode "+mode+" applied");
  } //modeChanged

  /**
   * We've been invited on a channel.
   * @param channel channel we're invited to.
   * @param who nickname who invited us.
   * @param status the status.
   */
  public void invited(String channel,String who,Status status)
  {
    print("You've been invited to "+channel+" by "+who);
  } //invited

} //SimpleAWTStatus
