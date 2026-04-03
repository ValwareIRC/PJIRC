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

package irc;

/**
 * The text provider.
 */
public interface TextProvider
{

  /**
   * First user message.
   */
  public static final int USER_BASE=0x8000;
  /**
   * Undefined string.
   */
  public static final int ERROR_NOT_DEFINED=0xffff;

	/**
	 * Get the formatted string.
	 * @param formattedCode string code.
	 * @param param parameters.
	 * @return formatted string.
	 */
	public String getString(int formattedCode,String param[]);

  /**
   * Get the formatted string.
   * @param code string code.
   * @return formatted string.
   */
	public String getString(int code);

  /**
   * Get the formatted string.
   * @param code string code.
   * @param param1 first parameter.
   * @return formatted string.
   */
  public String getString(int code,String param1);

  /**
   * Get the formatted string.
   * @param code string code.
   * @param param1 first parameter.
   * @param param2 second parameter.
   * @return formatted string.
   */
  public String getString(int code,String param1,String param2);

  /**
   * Get the formatted string.
   * @param code string code.
   * @param param1 first parameter.
   * @param param2 second parameter.
   * @param param3 third parameter.
   * @return formatted string.
   */
  public String getString(int code,String param1,String param2,String param3);

}

