// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.os.generic;

import net.multiphasicapps.io.data.RandomAccessData;

/**
 * This class supports reading the executable blob format.
 *
 * The layout of the blob is in the following format:
 * {@code
 *
 * }
 *
 * @since 2016/08/11
 */
public class GenericBlob
{
	/** The blob magic number. */
	public static final long MAGIC_NUMBER =
		0x537175697272656CL;
	
	/** The blob data. */
	protected final RandomAccessData data;
	
	/**
	 * Initializes the blob and uses the given random data.
	 *
	 * @param __d The data to the blob.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/11
	 */
	public GenericBlob(RandomAccessData __d)
		throws NullPointerException
	{
		// Check
		if (__d == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.data = __d;
	}
}

