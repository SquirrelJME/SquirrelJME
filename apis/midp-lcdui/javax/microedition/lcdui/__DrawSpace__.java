// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import net.multiphasicapps.squirreljme.lcdui.DisplayHead;
import net.multiphasicapps.squirreljme.lcdui.DisplayOrientation;

/**
 * This class is used to manage the draw space of a display or a tabbed area
 * which can contain something.
 *
 * @since 2017/10/27
 */
final class __DrawSpace__
{
	/** The current display orientation. */
	protected final DisplayOrientation orientation;
	
	/** Is the orientation natural? */
	protected final boolean naturalorientation;
	
	/** The dimensions of the draw space. */
	protected final int spacewidth, spaceheight;
	
	/** The position of the content area. */
	protected final int contentx, contenty;
	
	/** The size of the content area. */
	protected final int contentwidth, contentheight;
	
	/** Draw space for the content area? */
	private volatile __DrawSpace__ _contentdrawspace;
	
	/**
	 * Initializes a virtual draw space which is used when no display has
	 * been initialized, it is treated as a virtual one so that failure does
	 * not occur.
	 *
	 * @since 2017/10/27
	 */
	__DrawSpace__()
	{
		// Default parameters
		this.orientation = DisplayOrientation.LANDSCAPE;
		this.naturalorientation = true;
		this.spacewidth = 1;
		this.spaceheight = 1;
		
		// There is no content area
		this.contentx = -1;
		this.contenty = -1;
		this.contentwidth = -1;
		this.contentheight = -1;
	}
	
	/**
	 * Initializes the draw space for the given display.
	 *
	 * @param __d The display to calculate the draw space for.
	 * @since 2017/10/27
	 */
	__DrawSpace__(Display __d)
		throws NullPointerException
	{
		if (__d == null)
			throw new NullPointerException("NARG");
		
		// Need the display head for parameters
		DisplayHead head = __d._head;
		
		// Need the current orientation and it must be known if it is natural
		// because if it is not natural it must be wrapped in an Image
		DisplayOrientation orientation = head.orientation();
		boolean naturalorientation = head.isNaturalOrientation(orientation);
		
		if (true)
			throw new todo.TODO();
		
		// Determine the content area size
		int contentx = todo.TODO.missingInteger(),
			contenty = todo.TODO.missingInteger(),
			contentwidth = todo.TODO.missingInteger(),
			contentheight = todo.TODO.missingInteger();
		if (true)
			throw new todo.TODO();
		
		// Set properties
		this.orientation = orientation;
		this.naturalorientation = naturalorientation;
		this.spacewidth = spacewidth;
		this.spaceheight = spaceheight;
		this.contentx = contentx;
		this.contenty = contenty;
		this.contentwidth = contentwidth;
		this.contentheight = contentheight;
	}
	
	/**
	 * Returns the draw space of the content area if there is one.
	 *
	 * @return The content area's draw space.
	 * @since 2017/10/27
	 */
	public __DrawSpace__ contentArea()
	{
		// No content area?
		int contentwidth = this.contentwidth;
		if (contentwidth < 0)
			return null;
		
		// Has it already been created? Then use it without recreating it
		__DrawSpace__ contentdrawspace = this._contentdrawspace;
		if (contentdrawspace != null)
			return contentdrawspace;
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the graphics which draws on this draw space.
	 *
	 * @return The graphics to draw on the draw space.
	 * @since 2017/10/27
	 */
	public Graphics graphics()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the height.
	 *
	 * @return The height.
	 * @since 2017/10/27
	 */
	public int height()
	{
		return this.spaceheight;
	}
	
	/**
	 * Is the draw space no longer valid for whatever it is drawing on?
	 *
	 * It becomes invalid if the size changes or any required parameters such
	 * as rotation.
	 *
	 * @return If it is invalid.
	 * @since 2017/10/27
	 */
	public boolean isInvalid()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the orientation of the display.
	 *
	 * @return The display orientation.
	 * @since 2017/10/27
	 */
	public DisplayOrientation orientation()
	{
		return this.orientation;
	}
	
	/**
	 * This must be called at the tail of every paint operation which goes back
	 * up the drawspace tree to draw the parent elements as required.
	 *
	 * @since 2017/10/27
	 */
	public void tailPaint()
	{
		throw new todo.TODO();
	}	
	
	/**
	 * Returns the width.
	 *
	 * @return The width.
	 * @since 2017/10/27
	 */
	public int width()
	{
		return this.spacewidth;
	}
}

