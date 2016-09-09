// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.meep.lui.terminal;

import java.util.Iterator;
import net.multiphasicapps.squirreljme.meep.lui.DisplayDriver;
import net.multiphasicapps.squirreljme.meep.lui.DisplayProvider;

/**
 * This implements the terminal display driver.
 *
 * @since 2016/09/09
 */
public class TerminalDisplayProvider
	implements DisplayProvider
{
	/**
	 * {@inheritDoc}
	 * @since 2016/09/09
	 */
	@Override
	public Iterator<DisplayDriver> iterator()
	{
		throw new Error("TODO");
	}
}

