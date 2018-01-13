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

import cc.squirreljme.runtime.lcdui.DisplayHead;
import cc.squirreljme.runtime.lcdui.DisplayOrientation;

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
	
	/** The image to draw onto, may be null if not needed. */
	protected final Image image;
	
	/** The display to draw onto, may be null if there is no display. */
	protected final Display display;
	
	/** The parent draw space. */
	protected final __DrawSpace__ parent;
	
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
		
		// Use a very small image
		this.image = Image.createImage(1, 1);
		
		// Not used
		this.display = null;
		this.parent = null;
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
		
		// A virtual image which may be drawn on
		Image image;
		
		// The draw space size
		int spacewidth, spaceheight;
		
		// A wrapped image must be generated for this to work properly
		if (!naturalorientation)
			throw new todo.TODO();
		
		// Otherwise this is trivially setup
		else
		{
			spacewidth = head.displayVirtualWidthPixels();
			spaceheight = head.displayVirtualHeightPixels();
			
			// This is drawn naturally on the head
			image = null;
		}
		
		// For now use a content area which matches the whole area
		int contentx = 0,
			contenty = 0,
			contentwidth = spacewidth,
			contentheight = spaceheight;
		
		// Set properties
		this.orientation = orientation;
		this.naturalorientation = naturalorientation;
		this.spacewidth = spacewidth;
		this.spaceheight = spaceheight;
		this.contentx = contentx;
		this.contenty = contenty;
		this.contentwidth = contentwidth;
		this.contentheight = contentheight;
		this.image = image;
		this.display = __d;
		this.parent = null;
	}
	
	/**
	 * Initializes the drawspace for the client area.
	 *
	 * @param __parent The parent draw space.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/27
	 */
	public __DrawSpace__(__DrawSpace__ __parent)
		throws NullPointerException
	{
		if (__parent == null)
			throw new NullPointerException("NARG");
		
		// Get content area size
		int contentwidth = __parent.contentwidth,
			contentheight = __parent.contentheight;
		
		// Orientation is always natural
		this.orientation = (contentwidth > contentheight ?
			DisplayOrientation.LANDSCAPE : DisplayOrientation.PORTRAIT);
		this.naturalorientation = true;
		
		// The size of the parent's content area
		this.spacewidth = contentwidth;
		this.spaceheight = contentheight;
		
		// There is no content area
		this.contentx = -1;
		this.contenty = -1;
		this.contentwidth = -1;
		this.contentheight = -1;
		
		// Generate image where contents are drawn
		this.image = Image.createImage(contentwidth, contentheight);
		
		// Set parent
		this.parent = __parent;
		
		// Display not used
		this.display = null;
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
		
		// Setup draw space for the client
		this._contentdrawspace = (contentdrawspace = new __DrawSpace__(this));
		return contentdrawspace;
	}
	
	/**
	 * Returns the graphics which draws on this draw space.
	 *
	 * @return The graphics to draw on the draw space.
	 * @since 2017/10/27
	 */
	public Graphics graphics()
	{
		// If an image is specified always draw on that
		Image image = this.image;
		if (image != null)
			return image.getGraphics();
		
		// Otherwise use the display's graphics to draw
		Display display = this.display;
		if (display != null)
			return display._head.graphics();
		
		// Should not occur
		throw new RuntimeException("OOPS");
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
		return true;
		/*throw new todo.TODO();*/
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
	 * @param __sncd Suppress non-client area draw, such as the parts which
	 * are associated with title bars and such?
	 * @since 2017/10/27
	 */
	public void tailPaint(boolean __sncd)
	{
		// Draw the content area on the parent draw space?
		__DrawSpace__ contentarea = contentArea();
		if (contentarea != null)
		{
			// Image is drawn here
			Graphics g = graphics();
			
			// Client area always has an imahe
			g.drawImage(contentarea.image, this.contentx, this.contenty, 0);
		}
		
		// Is the orientation non-natural? then the image must be drawn on
		// whatever this is to draw on correctly
		if (!this.naturalorientation)
		{
			throw new todo.TODO();
		}
		
		// Tell display to repaint itself
		Display display = this.display;
		if (display != null)
			display._head.graphicsPainted();
		
		// Recurse up to the parent draw space
		__DrawSpace__ parent = this.parent;
		if (parent != null)
			parent.tailPaint(__sncd);
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

