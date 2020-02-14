// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

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

