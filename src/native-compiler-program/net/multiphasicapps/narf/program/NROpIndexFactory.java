// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.program;

/**
 * This factory is used to return an index which is uniquely associated with
 * an operation. All operations within a program must be mapped to a single
 * address.
 *
 * @since 2016/05/09
 */
public interface NROpIndexFactory
{
	/**
	 * Returns the next available operation index.
	 *
	 * @return The next index.
	 * @since 2016/05/09
	 */
	public abstract int next();
}

