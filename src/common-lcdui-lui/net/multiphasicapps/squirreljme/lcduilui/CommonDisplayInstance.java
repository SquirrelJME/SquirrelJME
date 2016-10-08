// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcduilui;

/**
 * This is an instance of a common display driver.
 *
 * @since 2016/10/08
 */
public interface CommonDisplayInstance
{
	/**
	 * This returns the capabilities of a given display, which is dependent
	 * on the display this represents.
	 *
	 * @return The bitflag of supported capabilities.
	 * @since 2016/10/08
	 */
	public abstract int getCapabilities();
}

