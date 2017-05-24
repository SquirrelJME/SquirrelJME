// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.host.javase;

import net.multiphasicapps.squirreljme.lcdui.NativeDisplay;

/**
 * This is a display which implements the native display interface on top of
 * Java's Swing interface.
 *
 * @since 2017/05/23
 */
public class SwingNativeDisplay
	extends NativeDisplay
{
	/** The single Swing display head instance, only one is needed. */
	protected final SwingHead head =
		new SwingHead();
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/24
	 */
	@Override
	public NativeDisplay.Head[] heads()
	{
		return new NativeDisplay.Head[]{this.head};
	}
	
	/**
	 * This is class used for Swing display heads.
	 *
	 * @since 2017/05/24
	 */
	public class SwingHead
		extends NativeDisplay.Head
	{
		/**
		 * Initializes the swing head.
		 *
		 * @since 2017/05/24
		 */
		protected SwingHead()
		{
		}
	}
}

