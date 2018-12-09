// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.ui;

import cc.squirreljme.runtime.lcdui.LCDUIProbe;
import javax.microedition.lcdui.Graphics;
import java.util.LinkedList;
import java.util.List;

/**
 * This represents a stack drawing area which continues downwards as it is
 * needed for the child elements.
 *
 * @since 2018/12/08
 */
public final class UIStack
{
	/** The drawable thing. */
	public final UIDrawable drawable;
	
	/** The reserved width. */
	public final int reservedwidth;
	
	/** The reserved height. */
	public final int reservedheight;
	
	/** Children stacks. */
	public final List<UIStack> kids =
		new LinkedList<>();
	
	/** The X position from the parent. */
	public int xoffset;
	
	/** The Y position from the parent. */
	public int yoffset;
	
	/** The total drawing width. */
	public int drawwidth;
	
	/** The total drawing height. */
	public int drawheight;
	
	/**
	 * Initializes the stack with the given view width and height.
	 *
	 * @param __d The drawable thing.
	 * @param __w Reserved width.
	 * @param __h Reserved height.
	 * @since 2018/12/08
	 */
	public UIStack(UIDrawable __d, int __w, int __h)
	{
		this.drawable = __d;
		this.reservedwidth = __w;
		this.reservedheight = __h;
		
		// Draw width defaults to the reserved width
		this.drawwidth = __w;
		
		// Draw self but using the same code as the kids!
		this.kids.add(null);
	}
	
	/**
	 * Adds the item to the draw stack.
	 *
	 * @param __s The stack to add.
	 * @since 2018/12/08
	 */
	public final void add(UIStack __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Get the drawing height because we will stack our height on top of
		// that!
		int drawheight = this.drawheight,
			subresheight = __s.reservedheight,
			newdrawheight = drawheight + subresheight;
		
		// Set position of the child relative to our own offset, stack
		// drawable going down more
		__s.xoffset = this.xoffset + 0;
		__s.yoffset = this.yoffset + drawheight;
		
		// If the child never set a drawheight, set it for them
		if (__s.drawheight == 0)
			__s.drawheight = subresheight;
		
		// For next run or for drawing
		this.drawheight = newdrawheight;
		
		// Add to kids to make sure it draws
		this.kids.add(__s);
		
		// Hint dimensions
		UIDrawable drawable = __s.drawable;
		if (drawable != null)
			LCDUIProbe.probe().hintDimensions(drawable,
				__s.drawwidth, __s.drawheight);
	}
	
	/**
	 * Add stack item which is at an exact position.
	 *
	 * @param __s The item to add.
	 * @param __x X position.
	 * @param __y Y position.
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2018/12/08
	 */
	public final void addExact(UIStack __s, int __x, int __y, int __w, int __h)
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Just set directly
		__s.xoffset = __x;
		__s.yoffset = __y;
		__s.drawwidth = __w;
		__s.drawheight = __h;
		
		// Add kid
		this.kids.add(__s);
		
		// Hint dimensions
		UIDrawable drawable = __s.drawable;
		if (drawable != null)
			LCDUIProbe.probe().hintDimensions(drawable,
				__w, __h);
	}
	
	/**
	 * Renders this stack item.
	 *
	 * @param __parent The parent draw stack.
	 * @param __g The graphics to draw into.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/08
	 */
	public final void render(UIStack __parent, Graphics __g)
		throws NullPointerException
	{
		if (__g == null)
			throw new NullPointerException("NARG");
		
		// Remember the base translation and clip details
		int origtransx = __g.getTranslateX(),
			origtransy = __g.getTranslateY(),
			origclipx = __g.getClipX(),
			origclipy = __g.getClipY(),
			origclipw = __g.getClipWidth(),
			origcliph = __g.getClipHeight();
		
		// Draw all the kids
		for (UIStack kid : this.kids)
		{
			// If no kid was specified this is ourself
			if (kid == null)
				kid = this;
			
			// Reset the translation
			__g.translate(-__g.getTranslateX(), -__g.getTranslateY());
			
			// Set the clip for this child so that is always in that area
			__g.setClip(kid.xoffset, kid.yoffset,
				kid.drawwidth, kid.drawheight);
			
			// Then limit the clip into our original clipping bound so that
			// we do not draw outside of it
			__g.clipRect(origclipx, origclipy,
				origclipw, origcliph);
			
			// Translate to the child's coordinate space so it is (0, 0).
			__g.translate(kid.xoffset, kid.yoffset);
			
			// Drawing ourself
			if (kid == this)
			{
				// Debug
				__g.drawRect(0, 0, kid.drawwidth - 2, kid.drawheight - 2);
				__g.drawLine(0, 0, kid.drawwidth, kid.drawheight);
				__g.drawLine(0, kid.drawheight, kid.drawwidth, 0);
				
				// Draw whatever drawable this is
				UIDrawable drawable = this.drawable;
				if (drawable != null)
					LCDUIProbe.probe().draw(drawable, __parent, this, __g);
			}
			
			// Draw child
			else
				kid.render(this, __g);
		}
		
		// Reset the translation
		__g.translate(-__g.getTranslateX(), -__g.getTranslateY());
		
		// Restore the old clip
		__g.setClip(origclipx, origclipy, origclipw, origcliph);
		
		// Then restore the old translation
		__g.translate(origtransx, origtransy);
	}
}

