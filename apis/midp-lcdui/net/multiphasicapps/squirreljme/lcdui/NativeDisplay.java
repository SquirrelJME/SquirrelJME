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

import javax.microedition.lcdui.Display;

/**
 * This is the base class for native display engines which also provides
 * native widgets and interfaces.
 *
 * @since 2017/05/23
 */
public abstract class NativeDisplay
{
	/**
	 * This is the head for a single display.
	 *
	 * @since 2017/05/23
	 */
	public abstract class Head
	{
		/**
		 * Initializes the display head.
		 *
		 * @since 2017/05/24
		 */
		protected Head()
		{
		}
	}
	
	/**
	 * This interface is used to create displays.
	 *
	 * @since 2017/05/23
	 */
	public static interface DisplayConstructor
	{
		/**
		 * This creates the native display using the given head.
		 *
		 * @param __h The native head which is used by the display.
		 * @since 2017/05/23
		 */
		public abstract Display createDisplay(NativeDisplay.Head __h);
	}
}

