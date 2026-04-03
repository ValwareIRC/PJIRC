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

package irc.security.prv;

import java.io.*;
import java.net.*;
import com.ms.security.*;

/**
 * A secured server socket for microsoft internet explorer.
 */
public class SpecificMSSecuredServerSocket extends ServerSocket
{

  /**
   * Create a new ServerSocket.
   * @param port port on wich to listener.
   * @throws IOException if error occurs.
   */
  public SpecificMSSecuredServerSocket(int port) throws IOException
  {
    super(port);
  }

  public Socket accept() throws IOException
  {
    try
    {
      PolicyEngine.assertPermission(PermissionID.NETIO);
      return super.accept();
    }
    catch(Throwable e)
    {
      return super.accept();
    }

  }

}

