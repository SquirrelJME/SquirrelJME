// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.data;

/**
 * This is used to obtain the number of bytes which were read from or written
 * to a given stream.
 *
 * @since 2016/07/10
 */
public interface SizedStream
{
	/**
	 * Returns the number of written or read bytes.
	 *
	 * @return The number of read or written bytes.
	 * @since 2016/07/10
	 */
	public abstract long size();
}

