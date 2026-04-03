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

package irc.gui.pixx;

import java.util.*;
import irc.*;
import irc.style.*;
import java.awt.*;
import java.awt.event.*;
import irc.gui.*;
import irc.gui.common.*;

/**
 * The AWT source.
 */
public class BaseAWTSource extends Panel implements GUISource,SourceListener,ActionListener,PixxScrollBarListener,FocusListener,StyledListListener,WindowListener,MouseWheelPanelListener,AWTStyleSelectorExListener
{
  /**
   * 
   */
  private static final long serialVersionUID=1L;
  /**
   * Enclosed source.
   */
  protected Source _source;
  /**
   * Vertical scrollbar.
   */
  protected PixxVerticalScrollBar _scroll;
  /**
   * Main panel.
   */
  protected Panel _panel;
  /**
   * Styled list.
   */
  protected StyledList _list;
  /**
   * Input text field.
   */
  protected AWTIrcTextField _textField;
  /**
   * Used to display the title bar.
   */
  protected FormattedStringDrawer _styler;
  /**
   * Title string.
   */
  protected String _title;
  /**
   * Stripped title string.
   */
  protected String _strippedTitle;
  private ListenerGroup _listeners;
  /**
   * Pixx configuration.
   */
  protected PixxConfiguration _pixxConfiguration;
  /**
   * Style selector.
   */
  protected AWTStyleSelectorEx _selector;
  /**
   * Overall alignment.
   */
  protected boolean _topToBottom;
  private MouseWheelPanelWrapper _wrapper;
  private String _nickPrefix;
  private String _nickPostfix;

  /**
   * Create a new AWTSource.
   * @param config the global configuration.
   * @param source the source of this awt source.
   */
  public BaseAWTSource(PixxConfiguration config,Source source)
  {
    this(config,source,false);
  }

  /**
   * Create a new AWTSource.
   * @param config the global configuration.
   * @param source the source of this awt source.
   * @param topToBottom true if the display should be performed from top to
   * bottom instead of bottom to top.
   */
  public BaseAWTSource(PixxConfiguration config,Source source,boolean topToBottom)
  {
    _topToBottom=topToBottom;
    _pixxConfiguration=config;
    _listeners=new ListenerGroup();
    _source=source;
    addFocusListener(this);
    _source.addSourceListener(this);
    _panel=new Panel();
    _panel.addFocusListener(this);
    _panel.setBackground(Color.white);
    _scroll=new PixxVerticalScrollBar(_pixxConfiguration,0,0,0.1);
    _scroll.addPixxScrollBarListener(this);
    setLayout(new BorderLayout());

    IRCConfiguration ircConfiguration=_pixxConfiguration.getIRCConfiguration();

    Color aslMale=_pixxConfiguration.getColor(PixxColorModel.COLOR_MALE);
    Color aslFemeale=_pixxConfiguration.getColor(PixxColorModel.COLOR_FEMEALE);
    Color aslUndef=_pixxConfiguration.getColor(PixxColorModel.COLOR_UNDEF);
    _list=new StyledList(ircConfiguration,true,ircConfiguration.getStyleContext(source.getType(),source.getName()),aslMale,aslFemeale,aslUndef);

    _list.addFocusListener(this);
    _list.addStyledListListener(this);
    _styler=new FormattedStringDrawer(ircConfiguration,getStyleContext());
    _textField=new AWTIrcTextField();
    
    boolean b=_pixxConfiguration.getB("displayentertexthere");
    if(b) _textField.setEnterTextHere(true,getText(PixxTextProvider.GUI_ENTER_TEXT_HERE));

    Color[] c=ircConfiguration.getStyleColors(getStyleContext());
    _textField.setBackground(c[0]);
    _textField.setForeground(c[1]);
    _textField.addFocusListener(this);
    Panel p=new Panel();
    p.setLayout(new BorderLayout());

    _wrapper=new MouseWheelPanelWrapper(_panel);
    _wrapper.addMouseWheelPanelListener(this);
    p.add(_wrapper,BorderLayout.CENTER);

    p.add(new PixxSeparator(PixxSeparator.BORDER_LEFT),BorderLayout.WEST);
    p.add(new PixxSeparator(PixxSeparator.BORDER_RIGHT),BorderLayout.EAST);
    p.add(new PixxSeparator(PixxSeparator.BORDER_UP),BorderLayout.NORTH);
    p.add(new PixxSeparator(PixxSeparator.BORDER_DOWN),BorderLayout.SOUTH);

    add(p,BorderLayout.CENTER);
    _panel.setLayout(new BorderLayout());
    _panel.add(_scroll,BorderLayout.EAST);
    _selector=new AWTStyleSelectorEx(_pixxConfiguration);
    _selector.setStyleContext(getStyleContext());
    _selector.addAWTStyleSelectorExListener(this);
    if(_pixxConfiguration.getB("styleselector"))
    {
      p=new Panel(new BorderLayout());
      p.add(_textField,BorderLayout.CENTER);
      p.add(_selector,BorderLayout.EAST);
      add(p,BorderLayout.SOUTH);
    }
    else
    {
      add(_textField,BorderLayout.SOUTH);
    }

    if(_topToBottom)
      _list.setFirst(0);
    else
      _list.setLast(0);

    _textField.addActionListener(this);
    setTitle(_source.getName());
    _panel.add(_list,BorderLayout.CENTER);
    
    _nickPrefix=reformat(_pixxConfiguration.getS("nickprefix"));
    _nickPostfix=reformat(_pixxConfiguration.getS("nickpostfix"));
  }

  private String reformat(String string)
  {
    String ans="";
    for(int i=0;i<string.length();i++)
    {
      char c=string.charAt(i);
      if(c=='\\')
      {
        if(i!=string.length()-1)
        {
          i++;
          c=string.charAt(i);
          if(c=='k') c=(char)3;
          else if(c=='b') c=(char)2;
          else if(c=='u') c=(char)31;
          else if(c=='r') c=(char)22;
          else if(c=='o') c=(char)15;
          else if(c=='s') c=' ';
        }
      }
      ans+=c;
    }
    return ans;
  }

  /**
   * Release this object. No further call may be performed on this object.
   */
  public void release()
  {
    _wrapper.removeMouseWheelPanelListener(this);
    _source.removeSourceListener(this);
    _panel.removeFocusListener(this);
    _scroll.removePixxScrollBarListener(this);
    _scroll.release();
    _list.removeFocusListener(this);
    _list.removeStyledListListener(this);
    _textField.removeFocusListener(this);
    _selector.removeAWTStyleSelectorExListener(this);
    _selector.release();
    _textField.removeActionListener(this);
    _textField.release();

    _list.release();
    removeAll();
  }

  /**
   * Change the front color of the style selector.
   * @param color the new front color.
   */
  public void setFrontColor(int color)
  {
    _selector.getStyleSelector().setFrontColor(color);
  }
  
  /**
   * Change the background color of the style selector.
   * @param color the new background color.
   */
  public void setBackColor(int color)
  {
    _selector.getStyleSelector().setBackColor(color);
  }
  
  /**
   * Set the new bold status.
   * @param bold the new bold status.
   */
  public void setBold(boolean bold)
  {
    _selector.getStyleSelector().setBold(bold);
  }
  
  /**
   * Set the new underline status.
   * @param underline the new underline statu.
   */
  public void setUnderline(boolean underline)
  {
    _selector.getStyleSelector().setUnderline(underline);
  }
  
  public Dimension getPreferredSize()
  {
    return new Dimension(600,300);
  }

  /**
   * Set the current textfield text.
   * @param txt new textfield text.
   */
  public void setFieldText(String txt)
  {
    _textField.setText(txt);
    _textField.setCaretPosition(txt.length());
  }

  /**
   * Get the current textfield text.
   * @return the current textfield text.
   */
  public String getFieldText()
  {
    return _textField.getText();
  }

  /**
   * Validate the current textfield text, as if user pressed return key.
   */
  public void validateText()
  {
    _textField.validateText();
  }

  /**
   * Get style context for this awt source.
   * @return style context for this awt source.
   */
  public StyleContext getStyleContext()
  {
    return _pixxConfiguration.getIRCConfiguration().getStyleContext(_source.getType(),_source.getName());
  }

  /**
   * Get formatted text associated with the given text code, with no parameter.
   * @param code text code.
   * @return formatted text.
   */
  public String getText(int code)
  {
    return _pixxConfiguration.getText(code);
  }

  /**
   * Get formatted text associated with the given text code, with one parameter.
   * @param code text code.
   * @param p1 first parameter.
   * @return formatted text.
   */
  public String getText(int code,String p1)
  {
    return _pixxConfiguration.getText(code,p1);
  }

  /**
   * Get formatted text associated with the given text code, with two parameters.
   * @param code text code.
   * @param p1 first parameter.
   * @param p2 second parameter.
   * @return formatted text.
   */
  public String getText(int code,String p1,String p2)
  {
    return _pixxConfiguration.getText(code,p1,p2);
  }

  /**
   * Get formatted text associated with the given text code, with three parameters.
   * @param code text code.
   * @param p1 first parameter.
   * @param p2 second parameter.
   * @param p3 third parameter.
   * @return formatted text.
   */
  public String getText(int code,String p1,String p2,String p3)
  {
    return _pixxConfiguration.getText(code,p1,p2,p3);
  }

  /**
   * Add listener.
   * @param lis the listener to add.
   */
  public void addBaseAWTSourceListener(BaseAWTSourceListener lis)
  {
    _listeners.addListener(lis);
  }

  /**
   * Remove listener.
   * @param lis the listener to remove.
   */
  public void removeBaseAWTSourceListener(BaseAWTSourceListener lis)
  {
    _listeners.removeListener(lis);
  }

  /**
   * Set this source title.
   * @param title the source title.
   */
  public void setTitle(String title)
  {
    if(title.equals(_title)) return;
    _title=title;
    _strippedTitle=_styler.getStripped(title);
    _listeners.sendEvent("titleChanged",this);
  }

  /**
   * Get the stripped title.
   * @return the stripped title.
   */
  public String getStrippedTitle()
  {
    return _strippedTitle;
  }

  /**
   * Get the title.
   * @return the title.
   */
  public String getTitle()
  {
    return _title;
  }

  /**
   * Get a shorter title.
   * @return a shorter title.
   */
  public String getShortTitle()
  {
    return _source.getName();
  }

  /**
   * Get the source.
   * @return the source.
   */
  public Source getSource()
  {
    return _source;
  }

  public void actionPerformed(ActionEvent e)
  {
    EventDispatcher.dispatchEventAsync(this,"actionPerformedEff",new Object[] {e});
  }

  /**
   * Internally used.
   * @param e
   */
  public void actionPerformedEff(ActionEvent e)
  {
    if(_textField.getText().length()==0) return;
    String prefix="";
    prefix=_selector.getPrefix();

    if(!_textField.getText().startsWith("/"))
    {
      _source.sendUserString(prefix+_textField.getText());
    }
    else
    {
      StringParser parser=new StringParser();
      String[] parts=parser.parseString(StringParser.trim(_textField.getText()));
      if(parts.length>=2)
      {
        if(parts[0].toLowerCase(Locale.ENGLISH).equals("/join"))
        {
          _source.sendString("/focus Channel "+parts[1]);
        }
      }
      _source.sendUserString(_textField.getText());
    }

    if(_textField!=null) _textField.setText("");

  }

  public void fontSelected(Font fnt)
  {
    _list.setFont(fnt);
  }


  /**
   * Clear this awt source display.
   * @param source source to be cleared.
   */
  public void clear(Source source)
  {
    _list.clear();
    _scroll.setMaximum(_list.getLineCount()-1);
    _scroll.setValue(_list.getLast());
    _listeners.sendEvent("eventOccured",this);
  }

  /**
   * Test wether the specified line should be highlighted.
   * @param msg line to test.
   * @return true if msg should be highlighted, false otherwise.
   */
  protected boolean needHighLight(String msg)
  {
    msg=msg.toLowerCase(java.util.Locale.ENGLISH);
    if(_pixxConfiguration.highLightNick())
    {
      String myNick=_source.getServer().getNick().toLowerCase(java.util.Locale.ENGLISH);
      if(msg.indexOf(myNick)!=-1) return true;
    }

    Enumeration e=_pixxConfiguration.getHighLightWords();
    while(e.hasMoreElements())
    {
      String word=((String)(e.nextElement())).toLowerCase(java.util.Locale.ENGLISH);
      if(msg.indexOf(word)!=-1) return true;
    }
    return false;
  }

  /**
   * Test wether the specified line should trigger "on word" sounds. Play the
   * sounds associated with the words.
   * @param msg line to test.
   */
  protected void checkSound(String msg)
  {
    msg=msg.toLowerCase(java.util.Locale.ENGLISH);
    AudioConfiguration ac=_pixxConfiguration.getIRCConfiguration().getAudioConfiguration();
    Enumeration e=ac.getSoundWords();
    while(e.hasMoreElements())
    {
      String word=((String)(e.nextElement())).toLowerCase(java.util.Locale.ENGLISH);
      if(msg.indexOf(word)!=-1) ac.onWord(word);
    }
  }

  /**
   * Print the given message on the awt source display.
   * @param msg message to print.
   * @param color color to use.
   * @param bold true if message should be in bold.
   * @param underline if message should be underlined.
   */
  protected void print(String msg,int color,boolean bold,boolean underline)
  {
    if(color!=1) msg="\3"+color+msg;
    if(bold) msg="\2"+msg;
    if(underline) msg=((char)31)+msg;

    if(_pixxConfiguration.getB("timestamp"))
    {
      Calendar cal=Calendar.getInstance();
      String hour=""+cal.get(Calendar.HOUR_OF_DAY);
      if(hour.length()==1) hour="0"+hour;
      String min=""+cal.get(Calendar.MINUTE);
      if(min.length()==1) min="0"+min;
      msg="["+hour+":"+min+"] "+msg;
    }
    _list.addLine(msg);
    _scroll.setMaximum(_list.getLineCount()-1);
    _scroll.setValue(_list.getLast());
    _listeners.sendEvent("eventOccured",this);
  }

  /**
   * Print the given message on the awt source display.
   * @param msg message to print.
   * @param color color to use.
   */
  protected void print(String msg,int color)
  {
    print(msg,color,false,false);
  }

  /**
   * Print the given message on the awt source display.
   * @param msg message to print.
   */
  protected void print(String msg)
  {
    print(msg,1,false,false);
  }

  private String formatNick(String nick)
  {
    return _nickPrefix+nick+_nickPostfix;
  }

  public void messageReceived(String nick,String str,Source source)
  {
    checkSound(str);
    if(needHighLight(str))
    {
      print(formatNick(nick)+str,_pixxConfiguration.getI("highlightcolor"));
    }
    else
    {
      print(formatNick(nick)+str);
    }
  }

  public void reportReceived(String msg,Source source)
  {
    print(msg);
  }

  public void noticeReceived(String from,String msg,Source source)
  {
    print("-"+from+"- "+msg,5);
  }

  public void action(String nick,String msg,Source source)
  {
    print("* "+nick+" "+msg,6);
  }

  /**
   * Leave this awt source.
   */
  public void leave()
  {
    _source.leave();
  }

  public void mouseWheelMoved(Integer amount)
  {
    int i=amount.intValue();
    _scroll.setValue(_scroll.getValue()+i);
    if(_topToBottom)
      _list.setFirst(_scroll.getValue());
    else
      _list.setLast(_scroll.getValue());
  }

  public void valueChanged(PixxScrollBar pixScrollBar)
  {
    if(_topToBottom)
      _list.setFirst(_scroll.getValue());
    else
      _list.setLast(_scroll.getValue());
  }

  public void focusGained(FocusEvent e)
  {
    if(e.getComponent()!=_textField)
      _textField.requestFocus();
  }

  public void focusLost(FocusEvent e)
  {
  }

  public void channelEvent(StyledList lis,String chan,MouseEvent e)
  {
    if(_pixxConfiguration.matchMouseConfiguration("channeljoin",e))
    {
      _source.sendString("/focus Channel "+chan);
      _source.sendString("/join "+chan);
    }
  }

  public void URLEvent(StyledList lis,String url,MouseEvent e)
  {
    if(_pixxConfiguration.matchMouseConfiguration("urlopen",e))
    {
      _source.sendString("/url "+url);
    }
  }

  public void nickEvent(StyledList lis,String nick,MouseEvent e)
  {
    if(_pixxConfiguration.matchMouseConfiguration("nickquery",e))
    {
      if(nick.equals(getSource().getServer().getNick())) return;
      if(_pixxConfiguration.getB("automaticqueries"))
      {
        _source.sendString("/focus Query "+nick);
        _source.sendString("/query "+nick);
      }
    }
  }

  public String toString()
  {
    return "AWTSource : "+getStrippedTitle();
  }

  public void setVisible(boolean b)
  {
    super.setVisible(b);
    if(!b) _list.dispose();
  }

  public void copyEvent(StyledList lis,String txt,MouseEvent e)
  {
    Frame f=new Frame();
    f.setTitle(getText(IRCTextProvider.GUI_COPY_WINDOW));
    f.addWindowListener(this);
    f.setLayout(new GridLayout(1,1));
    Panel p=new Panel();
    p.setLayout(new GridLayout(1,1));
    f.add(p);
    TextArea c=new TextArea();
    c.setFont(new Font("",Font.PLAIN,12));

    c.setText(txt);
    p.add(c);
    f.setSize(400,300);
    f.show();
  }

  public void virtualSizeChanged(StyledList lis)
  {
  }

  public void windowActivated(WindowEvent e) {}
  public void windowClosed(WindowEvent e) {}
  public void windowClosing(WindowEvent e)
  {
    e.getWindow().hide();
    e.getWindow().dispose();
  }
  public void windowDeactivated(WindowEvent e) {}
  public void windowDeiconified(WindowEvent e) {}
  public void windowIconified(WindowEvent e) {}
  public void windowOpened(WindowEvent e) {}

}

