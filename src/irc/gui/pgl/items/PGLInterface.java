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

import java.util.*;
import java.awt.*;

import irc.gui.pgl.*;
import irc.gui.pgl.interfaces.*;
import irc.*;

/**
 * PGLInterface.
 */
public class PGLInterface extends Panel implements IInterface
{
  /**
   * 
   */
  private static final long serialVersionUID=1L;
  //private PGLInitializationParameters _params;
  //private Object _userParams;
  private ListenerGroup _listeners;
  private IInterfaceElement[] _elements;
  private ILayout _layout;

  public void placeOn(Container container,Object constraint)
  {
    container.add(this,constraint);
  }

  public void removeFrom(Container container)
  {
    container.remove(this);
  }

  private void setLayout(ILayout layout)
  {
    super.setLayout(layout);
  }
  
  private void add(IVisibleItem item,ILayoutConstraint constraint)
  {
    item.placeOn(this,constraint);
  }

  private void remove(IVisibleItem item)
  {
    item.removeFrom(this);
  }

  public void initialize(PGLInitializationParameters params,Object userParams) throws PGLException
  {
    PGLElement layoutElement=params.getDescription().getElement("Layout");
    if(layoutElement!=null)
    {
      _layout=(ILayout)layoutElement.createInstance(params,userParams);
      setLayout(_layout);
    }

    PGLElement elements[]=params.getDescription().getAllElements("Element");
    _elements=new IInterfaceElement[elements.length];
    for(int i=0;i<elements.length;i++)
    {
      _elements[i]=(IInterfaceElement)elements[i].createInstance(params,userParams);
    }

    for(int i=_elements.length-1;i>=0;i--)
    {
      IItem item=_elements[i].getComponent();
      if(item instanceof IVisibleItem)
        add((IVisibleItem)item,_elements[i].getConstraint());
    }
  
    //_params=params;
    //_userParams=userParams;

  }

  public void uninitialize()
  {
    for(int i=0;i<_elements.length-1;i++)
    {
      IItem item=_elements[i].getComponent();
      if(item instanceof IVisibleItem)
        remove((IVisibleItem)item);
      _elements[i].uninitialize();
    }

    _elements=null;
    removeAll();

    _layout.uninitialize();
    _layout=null;
  }

  /**
   * Create a new PGLInterface
   */
  public PGLInterface()
  {
    _listeners=new ListenerGroup();
  }

  public ISourcePanel getCurrentSourcePanel()
  {
    return null;
  }

  public void setCurrentSourcePanel(ISourcePanel panel)
  {
    _listeners.sendEvent("currentSourcePanelChanged",panel,this);
  }

  public Enumeration getAllSourcePanels()
  {
    return new Vector().elements();
  }

  public void addInterfaceListener(IInterfaceListener listener)
  {
    _listeners.addListener(listener);
  }

  public void removeInterfaceListener(IInterfaceListener listener)
  {
    _listeners.removeListener(listener);
  }

  public void notifySourceCreated(Source source,Boolean bring)
  {
    _listeners.sendEvent("sourceCreated",source,bring,this);
  }

  public void notifySourceRemoved(Source source)
  {
    _listeners.sendEvent("sourceRemoved",source,this);
  }

  public void notifyServerCreated(Server server)
  {
    _listeners.sendEvent("serverCreated",server,this);
  }

  public void notifyServerRemoved(Server server)
  {
    _listeners.sendEvent("serverRemoved",server,this);
  }

  public void notifyServerConnected(Server server)
  {
    _listeners.sendEvent("serverConnected",server,this);
  }

  public void notifyServerDisconnected(Server server)
  {
    _listeners.sendEvent("serverDisconnected",server,this);
  }

  public ISourcePanel getSourcePanelFromSource(Source source)
  {
    return null;
  }

  public Component getComponent()
  {
    return this;
  }

}
