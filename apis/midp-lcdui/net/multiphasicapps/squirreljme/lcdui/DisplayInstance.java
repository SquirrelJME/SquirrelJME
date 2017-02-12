// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui;

/**
 * This is an instance of a displayable as it is connected to a
 * {@link Display}. It provides to {@link Display} and {@link Displayable} an
 * interface to provide updates in a non-global fashion.
 *
 * @since 2017/02/08
 */
public interface DisplayInstance
{
	/**
	 * Specifies that the instance should be destroyed, likely because it
	 * is no longer associated with a display.
	 *
	 * @since 2017/02/08
	 */
	public abstract void destroy();
	
	/**
	 * Maps the given keycode to an action.
	 *
	 * @param __kc The keycode to convert to an action.
	 * @return The action associated with the given keycode or {@code 0} if
	 * no action is associated with the given key.
	 * @since 2017/02/12
	 */
	public abstract int getActionForKey(int __kc);
	
	/**
	 * Returns the height of the displayable in pixels.
	 *
	 * @return The height in pixels or if the displayable is not shown, a
	 * default height.
	 * @since 2017/02/08
	 */
	public abstract int getHeight();
	
	/**
	 * Returns the width of the displayable in pixels.
	 *
	 * @return The width in pixels or if the displayable is not shown, a
	 * default width.
	 * @since 2017/02/08
	 */
	public abstract int getWidth();
	
	/**
	 * Tells the display instance to be repainted if this operation is
	 * supported.
	 *
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2017/02/10
	 */
	public abstract void repaint(int __x, int __y, int __w, int __h);
	
	/**
	 * Sets the state of the instance engine.
	 *
	 * @param __s The state to use.
	 * @since 2017/02/08
	 */
	public abstract void setState(int __s);
	
	/**
	 * Sets the title of the instance.
	 *
	 * @param __s The title to use, if {@code null} then a default should be
	 * used instead.
	 * @since 2017/02/08
	 */
	public abstract void setTitle(String __s);
	
	/**
	 * Performs an update of the instance and renders it.
	 *
	 * @since 2017/02/08
	 */
	public abstract void update();
}

