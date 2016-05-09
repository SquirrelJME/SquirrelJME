// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.codeparse;

import net.multiphasicapps.narf.program.NROpIndexFactory;

/**
 * This is used to count operations within the program.
 *
 * @since 2016/05/09
 */
final class __OpIndexFactory__
	implements NROpIndexFactory
{
	/** The current index. */
	private volatile int _dx;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/09
	 */
	@Override
	public int next()
	{
		return _dx++;
	}
}

