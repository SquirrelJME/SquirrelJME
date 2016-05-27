// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terp;

/**
 * This class is used to keep statistics on the virtual machine.
 *
 * @since 2016/05/14
 */
public class TerpStatistics
{
	/** Lock on the statistics. */
	protected final Object lock =
		new Object();
	
	/**
	 * This initializes the class whichs keeps statistics.
	 *
	 * @since 2016/05/14
	 */
	public TerpStatistics()
	{
	}
}

