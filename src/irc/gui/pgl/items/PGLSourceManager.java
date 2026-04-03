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

package irc.gui.pgl.items;

import irc.*;
import irc.gui.pgl.*;
import irc.gui.pgl.interfaces.*;
import java.util.*;
import java.awt.*;

/**
 * PGLSourceManager
 */
public class PGLSourceManager implements ISourceManager,IInterfaceListener
{
  private ISourceManagerRule[] _rules;
  private PGLGlobalParameter _global;
  private PGLInitializationParameters _params;
  private Hashtable _sources;
  
  public void initialize(PGLInitializationParameters params,Object userParams) throws PGLException
  {
    PGLElement elements[]=params.getDescription().getAllElements("Rule");
    _rules=new ISourceManagerRule[elements.length];
    for(int i=0;i<elements.length;i++)
    {
      _rules[i]=(ISourceManagerRule)elements[i].createInstance(params,userParams);
    }
    
    _global=(PGLGlobalParameter)userParams;

    _global.getInterface().addInterfaceListener(this);
    
    _params=params;
    
    _sources=new Hashtable();
  }

  public void uninitialize()
  {
    for(int i=0;i<_rules.length-1;i++)
    {
      _rules[i].uninitialize();
    }

    _global.getInterface().removeInterfaceListener(this);
    _global=null;
    
    _rules=null;
    
    _sources=null;
  }
  
  public ISourcePanel getCurrentSource()
  {
    return null;
  }

  public void sourceCreated(Source source,Boolean bring,IInterface inter)
  {
    for(int i=0;i<_rules.length;i++)
    {
      if(_rules[i].matches(source))
      {
        PGLElement item=_rules[i].getItem();
        
        PGLSourceSpecificParameter spec=new PGLSourceSpecificParameter(_global,source);
        
        try
        {
          ISourcePanel instance=(ISourcePanel)item.createInstance(_params,spec);
          
          Frame f=new Frame();
          f.setSize(640,400);
          f.setTitle(source.getName());
          f.setLayout(new BorderLayout());
          instance.placeOn(f,BorderLayout.CENTER);
          f.show();
          
          _sources.put(source,f);
        }
        catch(PGLException ex)
        {
          //error...
          ex.printStackTrace();
        }
        
        break;
      }
    }
  }

  public void sourceRemoved(Source source,IInterface inter)
  {
    Frame f=(Frame)_sources.get(source);
    f.hide();
    f.dispose();
    _sources.remove(source);
  }

  public void serverCreated(Server server,IInterface inter)
  {
  }

  public void serverRemoved(Server server,IInterface inter)
  {
  }

  public void serverConnected(Server server,IInterface inter)
  {
  }

  public void serverDisconnected(Server server,IInterface inter)
  {
  }

  public void unhandledUserCommand(Source source,String str)
  {
  }

}
