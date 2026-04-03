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

package irc.style;

/**
 * The URLRecognizer.
 */
public class URLRecognizer implements WordRecognizer
{
  private boolean isAlpha(String s)
  {
    s=s.toLowerCase(java.util.Locale.ENGLISH);
    for(int i=0;i<s.length();i++) if((s.charAt(i)<'a') || (s.charAt(i)>'z')) return false;
    return true;
  }

  public boolean recognize(String word)
  {
    if(word.startsWith("http://")) return true;
    if(word.startsWith("ftp://")) return true;
    if(word.startsWith("www.")) return true;
    if(word.startsWith("ftp.")) return true;
    int a=word.indexOf('.');
    if(a==-1) return false;
    int b=word.lastIndexOf('.');
    if(a==b) return false;
    String ext=word.substring(b+1);
    if(!isAlpha(ext)) return false;
    if((ext.length()==2) || (ext.length()==3)) return true;
    return false;
  }

  public String getType()
  {
    return "url";
  }


}

