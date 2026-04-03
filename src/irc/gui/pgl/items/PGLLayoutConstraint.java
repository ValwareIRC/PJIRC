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

import irc.gui.pgl.*;
import irc.gui.pgl.interfaces.*;

/**
 * PGLLayoutConstraint
 */
public class PGLLayoutConstraint implements ILayoutConstraint
{
  /**
   * West anchor.
   */
  public static final int ANCHOR_WEST=0;
  /**
   * East anchor.
   */
  public static final int ANCHOR_EAST=1;
  /**
   * North anchor.
   */
  public static final int ANCHOR_NORTH=0;
  /**
   * South anchor;
   */
  public static final int ANCHOR_SOUTH=1;
  /**
   * Relative anchor.
   */
  public static final int ANCHOR_RELATIVE=2;
  /**
   * Absolute size.
   */
  public static final int SIZE_ABSOLUTE=0;
  /**
   * Relative size.
   */
  public static final int SIZE_RELATIVE=1;

  private int _left;
  private int _top;
  private int _right;
  private int _bottom;
  private int _anchorLeft;
  private int _anchorTop;
  private int _anchorRight;
  private int _anchorBottom;
  private int _sizeLeft;
  private int _sizeTop;
  private int _sizeRight;
  private int _sizeBottom;

  /**
   * Create a new PGLLayoutConstraint
   */
  public PGLLayoutConstraint()
  {
  }

  /**
   * Create a new PGLLayoutConstraint
   * @param left
   * @param leftAnchor
   * @param leftSize
   * @param top
   * @param topAnchor
   * @param topSize
   * @param right
   * @param rightAnchor
   * @param rightSize
   * @param bottom
   * @param bottomAnchor
   * @param bottomSize
   */
  public PGLLayoutConstraint(int left,int leftAnchor,int leftSize,int top,int topAnchor,int topSize,int right,int rightAnchor,int rightSize,int bottom,int bottomAnchor,int bottomSize)
  {
    _left=left;
    _anchorLeft=leftAnchor;
    _sizeLeft=leftSize;
    _right=right;
    _anchorRight=rightAnchor;
    _sizeRight=rightSize;
    _top=top;
    _anchorTop=topAnchor;
    _sizeTop=topSize;
    _bottom=bottom;
    _anchorBottom=bottomAnchor;
    _sizeBottom=bottomSize;
  }

  public void initialize(PGLInitializationParameters params,Object userParams) throws PGLException
  {
    String left=params.getDescription().getString("Left");
    String right=params.getDescription().getString("Right");
    String top=params.getDescription().getString("Top");
    String bottom=params.getDescription().getString("Bottom");
    String anchorLeft=params.getDescription().getString("LeftAnchor");
    String anchorRight=params.getDescription().getString("RightAnchor");
    String anchorTop=params.getDescription().getString("TopAnchor");
    String anchorBottom=params.getDescription().getString("BottomAnchor");

    if(left==null) left="0";
    if(right==null) right="0";
    if(top==null) top="0";
    if(bottom==null) bottom="0";

    _sizeLeft=SIZE_ABSOLUTE;
    _sizeTop=SIZE_ABSOLUTE;
    _sizeRight=SIZE_ABSOLUTE;
    _sizeBottom=SIZE_ABSOLUTE;

    if(left.endsWith("%"))
    {
      left=left.substring(0,left.length()-1);
      _sizeLeft=SIZE_RELATIVE;
    }
    if(right.endsWith("%"))
    {
      right=right.substring(0,right.length()-1);
      _sizeRight=SIZE_RELATIVE;
    }
    if(top.endsWith("%"))
    {
      top=top.substring(0,top.length()-1);
      _sizeTop=SIZE_RELATIVE;
    }
    if(bottom.endsWith("%"))
    {
      bottom=bottom.substring(0,bottom.length()-1);
      _sizeBottom=SIZE_RELATIVE;
    }


    _left=Integer.parseInt(left);
    _right=Integer.parseInt(right);
    _top=Integer.parseInt(top);
    _bottom=Integer.parseInt(bottom);



    _anchorLeft=ANCHOR_WEST;
    if(anchorLeft!=null)
    {
      if(anchorLeft.equals("West")) _anchorLeft=ANCHOR_WEST;
      else if(anchorLeft.equals("East")) _anchorLeft=ANCHOR_EAST;
    }

    _anchorRight=ANCHOR_RELATIVE;
    if(anchorRight!=null)
    {
      if(anchorRight.equals("West")) _anchorRight=ANCHOR_WEST;
      else if(anchorRight.equals("East")) _anchorRight=ANCHOR_EAST;
      else if(anchorRight.equals("Relative")) _anchorRight=ANCHOR_RELATIVE;
    }

    _anchorTop=ANCHOR_NORTH;
    if(anchorTop!=null)
    {
      if(anchorTop.equals("North")) _anchorTop=ANCHOR_NORTH;
      else if(anchorTop.equals("South")) _anchorTop=ANCHOR_SOUTH;
    }

    _anchorBottom=ANCHOR_RELATIVE;
    if(anchorBottom!=null)
    {
      if(anchorBottom.equals("North")) _anchorBottom=ANCHOR_NORTH;
      else if(anchorBottom.equals("South")) _anchorBottom=ANCHOR_SOUTH;
      else if(anchorBottom.equals("Relative")) _anchorBottom=ANCHOR_RELATIVE;
    }
  }

  public void uninitialize()
  {
  }

  private int relative(int mode,int pc,int val)
  {
    if(mode==SIZE_ABSOLUTE) return pc;
    return val*pc/100;
  }

  /**
   * Get the left position based on the parent's width.
   * @param width
   * @return left position.
   */
  public int getLeft(int width)
  {
    if(_anchorLeft==ANCHOR_WEST) return relative(_sizeLeft,_left,width);
    else if(_anchorLeft==ANCHOR_EAST) return width+relative(_sizeLeft,_left,width)-1;
    else return relative(_sizeLeft,_left,width);
  }

  /**
   * Get the right position based on the parent's width.
   * @param width
   * @return right position.
   */
  public int getRight(int width)
  {
    if(_anchorRight==ANCHOR_WEST) return relative(_sizeRight,_right,width);
    else if(_anchorRight==ANCHOR_EAST) return width+relative(_sizeRight,_right,width)-1;
    else if(_anchorRight==ANCHOR_RELATIVE) return getLeft(width)+relative(_sizeRight,_right,width)-1;
    else return relative(_sizeRight,_right,width);
  }

  /**
   * Get the width based on the parent's width.
   * @param width
   * @return width.
   */
  public int getWidth(int width)
  {
    return getRight(width)-getLeft(width)+1;
  }

  /**
   * Get the top position based on the parent's height.
   * @param height
   * @return top position.
   */
  public int getTop(int height)
  {
    if(_anchorTop==ANCHOR_NORTH) return relative(_sizeTop,_top,height);
    else if(_anchorTop==ANCHOR_SOUTH) return height+relative(_sizeTop,_top,height)-1;
    else return relative(_sizeTop,_top,height);
  }

  /**
   * Get the button position based on the parent's height.
   * @param height
   * @return button position.
   */
  public int getBottom(int height)
  {
    if(_anchorBottom==ANCHOR_NORTH) return relative(_sizeBottom,_bottom,height);
    else if(_anchorBottom==ANCHOR_SOUTH) return height+relative(_sizeBottom,_bottom,height)-1;
    else if(_anchorBottom==ANCHOR_RELATIVE) return getTop(height)+relative(_sizeBottom,_bottom,height)-1;
    else return relative(_sizeBottom,_bottom,height);
  }

  /**
   * Get the height based on the parent's height.
   * @param height
   * @return height.
   */
  public int getHeight(int height)
  {
    return getBottom(height)-getTop(height)+1;
  }
}
