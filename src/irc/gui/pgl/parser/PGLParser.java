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

package irc.gui.pgl.parser;

import irc.gui.pgl.*;

import java.util.*;
import java.io.*;

/**
 * Definition.
 */
class Definition
{
  /**
   * The key.
   */
  public String key;
  /**
   * The description.
   */
  public PGLElementDescription description;
}

/**
 * TokenReader.
 */
class TokenReader
{
  private BufferedReader _reader;
  private Stack _pushed;

  /**
   * Create a new TokenReader.
   * @param reader underlying reader.
   */
  public TokenReader(BufferedReader reader)
  {
    _reader=reader;
    _pushed=new Stack();
  }

  /**
   * Push the given string back.
   * @param str string to push back.
   */
  public void pushBack(String str)
  {
    _pushed.push(str);
  }

  /**
   * Get the next token.
   * @param noeof true if eof is not expected.
   * @return next token.
   * @throws IOException
   */
  public String nextToken(boolean noeof) throws IOException
  {
    if(_pushed.size()>0)
    {
      String ans=(String)_pushed.pop();
      return ans;
    }


    boolean inLineComment=false;
    boolean inBlockComment=false;

    StringBuffer buffer=new StringBuffer();
    int i=_reader.read();
    if(i<0)
    {
      if(noeof) throw new IOException("Unexpected end of file");
      return null;
    }
    boolean inString=false;

    char prev=(char)0;

    while(i>=0)
    {
      char c=(char)i;
      if(c=='"' && !inLineComment && !inBlockComment) inString=!inString;

      if(!inString)
      {
        if(c=='/' && prev=='/' && !inBlockComment && !inLineComment) inLineComment=true;
        if(c=='*' && prev=='/' && !inBlockComment && !inLineComment) inBlockComment=true;
        if(c=='/' && prev=='*' && !inLineComment && inBlockComment) inBlockComment=false;
        if(c==' ' || c==(char)10 || c==(char)13 || c=='=' || c=='{' || c=='}')
        {
          if(c=='=' || c=='{' || c=='}')
          {
            if(buffer.length()==0) buffer.append(c);
            else pushBack(""+c);
          }
          if(c==(char)10 || c==(char)13 && inLineComment) inLineComment=false;
          if(buffer.length()>0) break;
        }
        else if(!inBlockComment && !inLineComment)
        {
          buffer.append(c);
        }
      }
      else
      {
        buffer.append(c);
      }
      i=_reader.read();
      prev=c;
    }

    if(inString) throw new IOException("Unterminated string");
    if(buffer.length()==0)
    {
      if(noeof) throw new IOException("Unexpected end of file");
      return null;
    }
    String ans=buffer.toString();
      return ans;
  }
}

/**
 * PGLParser.
 */
public class PGLParser implements Parser
{
  private PGLElementDescription resolveDescription(PGLElementDescription descr,Hashtable definitions)
  {
    if(descr.getKey()==null) return descr;
    PGLElementDescription parent=(PGLElementDescription)definitions.get(descr.getKey());
    if(parent==null) return descr;

    String string=parent.getString();
    if(descr.getString()!=null) string=descr.getString();
    PGLElement[] el=new PGLElement[descr.getElements().length+parent.getElements().length];

    for(int i=0;i<parent.getElements().length;i++) el[i]=parent.getElements()[i];
    for(int i=0;i<descr.getElements().length;i++) el[i+parent.getElements().length]=descr.getElements()[i];

    PGLElementDescription ans=new PGLElementDescription(parent.getKey(),string,el);
    
    return resolveDescription(ans,definitions);
  }

  private PGLElement[] readElementGroup(TokenReader reader,Hashtable definitions,boolean enclosed) throws IOException
  {
    Hashtable localDefinitions=new Hashtable(definitions);
    Vector v=new Vector();

    if(!enclosed) reader.pushBack("{");

    String first=reader.nextToken(true);
    if(!first.equals("{")) throw new IOException("Syntax error, invalid group opening token");
    String next=reader.nextToken(false);
    while((next!=null) && !next.equals("}"))
    {
      reader.pushBack(next);
      if(next.equals("Define"))
      {
        Definition def=readDefinition(reader,localDefinitions);
        localDefinitions.put(def.key,def.description);
      }
      else
      {
        v.insertElementAt(readElement(reader,localDefinitions),v.size());
      }
      next=reader.nextToken(false);
    }

    if(next==null && enclosed) throw new IOException("Unexpected end of file");


    PGLElement[] ans=new PGLElement[v.size()];
    for(int i=0;i<ans.length;i++) ans[i]=(PGLElement)v.elementAt(i);
    return ans;
  }

  private PGLElementDescription readDescription(TokenReader reader,Hashtable definitions) throws IOException
  {
    String key;
    String string=null;
    PGLElement[] el;
    
    String first=reader.nextToken(true);
    if(first.startsWith("\"") && first.endsWith("\""))
    {
      String string1=first;
      key=null;
      string=string1.substring(1,string1.length()-1);
      el=new PGLElement[0];
    }
    else
    {
      String nameOrType=first;
      key=nameOrType;
      String next=reader.nextToken(true);
      reader.pushBack(next);
      if(next.equals("{"))
      {
        el=readElementGroup(reader,definitions,true);
      }
      else
      {
        el=new PGLElement[0];
      }
    }
    
    PGLElementDescription ans=new PGLElementDescription(key,string,el);
    return ans;
  }

  private PGLElement readElement(TokenReader reader,Hashtable definitions) throws IOException
  {
    String key=reader.nextToken(true);
    String eq=reader.nextToken(true);
    String global=null;
    if(eq.equals("as"))
    {
      global=reader.nextToken(true);
      eq=reader.nextToken(true);
    }

    if(!eq.equals("=")) throw new IOException("Syntax error near element "+key);
    PGLElementDescription desc=resolveDescription(readDescription(reader,definitions),definitions);
    PGLElement ans=new PGLElement(key,desc,global);
    return ans;
  }

  private Definition readDefinition(TokenReader reader,Hashtable definitions) throws IOException
  {
    /*String define=*/reader.nextToken(true);

    PGLElement elem=readElement(reader,definitions);
    Definition ans=new Definition();
    ans.key=elem.getName();
    ans.description=elem.getDescription();
    return ans;
  }

  private PGLElement[] doParse(TokenReader reader) throws IOException
  {
    return readElementGroup(reader,new Hashtable(),false);
  }

  public PGLElement[] parse(BufferedReader reader) throws IOException
  {
    return doParse(new TokenReader(reader));
  }
}
