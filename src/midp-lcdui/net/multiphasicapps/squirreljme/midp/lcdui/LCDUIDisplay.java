// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.midp.lcdui;

import javax.microedition.lcdui.Display;
import net.multiphasicapps.squirreljme.lcduilui.CommonDisplay;

/**
 * This class acts as the base for display drivers which are able to duplicate
 * the LCDUI functionality.
 *
 * @since 2016/10/08
 */
public abstract class LCDUIDisplay
	extends CommonDisplay<Display>
{
	/**
	 * {@inheritDoc}
	 * @since 2016/10/08
	 */
	@Override
	public abstract LCDUIDisplayInstance createInstance();
	
	/**
	 * This checks if the given display supports all of the given
	 * capabilities.
	 *
	 * @param __caps The capabilities to check.
	 * @return {@code true} if all capabilities are valid.
	 * @since 2016/10/08
	 */
	public abstract boolean hasCapabilities(int __caps);
}

