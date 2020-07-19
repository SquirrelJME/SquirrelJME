// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.phoneui;

/**
 * This class contains the drawing state to modify what is on screen and
 * such.
 *
 * @since 2019/05/18
 */
@Deprecated
public class State
{
	/** The focus index coordinates. */
	@Deprecated
	public final Box focusbox =
		new Box(-1, -1, 0, 0);
	
	/** The focused index. */
	@Deprecated
	public int focusdx;
	
	/**
	 * Represents a box.
	 *
	 * @since 2019/05/18
	 */
	@Deprecated
	public static final class Box
	{
		/** X coordinate. */
		@Deprecated
		public int x;
		
		/** Y coordinate. */
		@Deprecated
		public int y;
		
		/** Width. */
		@Deprecated
		public int w;
		
		/** Height. */
		@Deprecated
		public int h;
		
		/**
		 * Initializes a default box.
		 *
		 * @since 2019/05/18
		 */
		@Deprecated
		public Box()
		{
		}
		
		/**
		 * Initializes a box with the given coordinates and dimensions.
		 *
		 * @param __x X position.
		 * @param __y Y position.
		 * @param __w The width.
		 * @param __h The height.
		 * @since 2019/05/18
		 */
		@Deprecated
		public Box(int __x, int __y, int __w, int __h)
		{
			this.x = __x;
			this.y = __y;
			this.w = __w;
			this.h = __h;
		}
		
		/**
		 * Sets all of the box coordinates.
		 *
		 * @param __x X position.
		 * @param __y Y position.
		 * @param __w The width.
		 * @param __h The height.
		 * @since 2019/05/18
		 */
		@Deprecated
		public final void set(int __x, int __y, int __w, int __h)
		{
			this.x = __x;
			this.y = __y;
			this.w = __w;
			this.h = __h;
		}
	}
}

