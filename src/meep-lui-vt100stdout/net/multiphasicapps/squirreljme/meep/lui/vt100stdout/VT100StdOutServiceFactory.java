// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.meep.lui.vt100stdout;

import net.multiphasicapps.squirreljme.meep.lui.DisplayService;
import net.multiphasicapps.squirreljme.meep.lui.DisplayServiceFactory;
import net.multiphasicapps.squirreljme.meep.lui.InvalidDisplayServiceException;

/**
 * This is the factory service which utilizes VT100 standard codes by using
 * output to stdout.
 *
 * @since 2016/09/08
 */
public class VT100StdOutServiceFactory
	implements DisplayServiceFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2016/09/08
	 */
	@Override
	public DisplayService createDisplayService()
		throws InvalidDisplayServiceException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/08
	 */
	@Override
	public int priority()
	{
		return 1;
	}
}

