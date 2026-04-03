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

package irc.gui.pixx;

import irc.*;
import java.util.*;

/**
 * The AWT interpretor for gui-relative commands.
 */
public class AWTInterpretor extends RootInterpretor
{
  private PixxMDIInterface _mdi;
  private PixxConfiguration _config;

  /**
   * Create a new DefaultInterpretor.
   * @param config global irc configuration.
   * @param mdi the interface.
   */
  public AWTInterpretor(PixxConfiguration config,PixxMDIInterface mdi)
  {
    super(config.getIRCConfiguration());
    _mdi=mdi;
    _config=config;
  }

  protected void handleCommand(Source source,String cmd,String[] parts,String[] cumul)
  {
    try
    {
    
      if(cmd.equals("dock"))
      {
        BaseAWTSource asource=_mdi.findBaseAWTSource(source);
        if(asource!=null) _mdi.dock(asource);
      }
      else if(cmd.equals("undock"))
      {
        BaseAWTSource asource=_mdi.findBaseAWTSource(source);
        if(asource!=null) _mdi.undock(asource);
      }
      else if(cmd.equals("color"))
      {
        test(cmd,parts,1);
        BaseAWTSource asource=_mdi.findBaseAWTSource(source);
        if(asource!=null)
        {
          String front=parts[1];
          String back="";
          int pos=front.indexOf(",");
          if(pos>=0)
          {
            back=front.substring(pos+1);
            front=front.substring(0,pos);
          }
          try
          {
            int c=Integer.parseInt(front);
            asource.setFrontColor(c);
          }
          catch(Exception ex)
          {
          }
          try
          {
            int c=Integer.parseInt(back);
            asource.setBackColor(c);
          }
          catch(Exception ex)
          {
          }
        }
      }
      else if(cmd.equals("bold"))
      {
        test(cmd,parts,1);
        BaseAWTSource asource=_mdi.findBaseAWTSource(source);
        if(asource!=null)
        {
          if(parts[1].equals("1")) asource.setBold(true);
          else if(parts[1].equals("0")) asource.setBold(false);
        }
      }
      else if(cmd.equals("underline"))
      {
        test(cmd,parts,1);
        BaseAWTSource asource=_mdi.findBaseAWTSource(source);
        if(asource!=null)
        {
          if(parts[1].equals("1")) asource.setUnderline(true);
          else if(parts[1].equals("0")) asource.setUnderline(false);
        }
      }
      else if(cmd.equals("highlight"))
      {
        test(cmd,parts,1);
        for(int i=1;i<parts.length;i++) _config.addHighLightWord(parts[i]);
      }
      else if(cmd.equals("unhighlight"))
      {
        test(cmd,parts,1);
        for(int i=1;i<parts.length;i++) _config.removeHighLightWord(parts[i]);
      }
      else if(cmd.equals("focus"))
      {
        test(cmd,parts,2);
        Enumeration e=source.getServer().getSources();
        while(e.hasMoreElements())
        {
          Source s=(Source)e.nextElement();
          if(s.getType().equals(parts[1]) && s.getName().toLowerCase(Locale.ENGLISH).equals(parts[2].toLowerCase(Locale.ENGLISH)))
          {
            BaseAWTSource asource=_mdi.findBaseAWTSource(s);
            if(asource!=null) _mdi.setActive(asource);
            break;
          }
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

