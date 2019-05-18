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
public class State
{
	/**
	 * Represents a box.
	 *
	 * @since 2019/05/18
	 */
	public static final class Box
	{
		/** X coordinate. */
		public int x;
		
		/** Y coordinate. */
		public int y;
		
		/** Width. */
		public int w;
		
		/** Height. */
		public int h;
		
		/**
		 * Initializes a default box.
		 *
		 * @since 2019/05/18
		 */
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
		public Box(int __x, int __y, int __w, int __h)
		{
			this.x = __x;
			this.y = __y;
			this.w = __w;
			this.h = __h;
		}
	}
}

