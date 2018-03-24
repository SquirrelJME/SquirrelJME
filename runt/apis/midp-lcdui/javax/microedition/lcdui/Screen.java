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

/**
 * This is the base class for all user interactable displays.
 *
 * It is only recommended to change the screen contents when it is not being
 * displayed.
 *
 * @since 2017/02/28
 */
public abstract class Screen
	extends Displayable
{
	/**
	 * Initializes the base screen.
	 *
	 * @since 2017/02/28
	 */
	Screen()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/24
	 */
	@__SerializedEvent__
	@Override
	final void __doPaint(Graphics __g, int __pw, int __ph)
	{
		// Not needed for screen elements
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/24
	 */
	@__SerializedEvent__
	@Override
	final void __doShown(boolean __shown)
	{
		// Not needed for screen elements
	}
}


