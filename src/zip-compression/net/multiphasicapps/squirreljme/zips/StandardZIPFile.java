// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.zips;

import java.nio.channels.SeekableByteChannel;

/**
 * This provides access to a ZIP file.
 *
 * @since 2016/02/26
 */
public class StandardZIPFile
{
	/** The base channel to read from. */
	protected final SeekableByteChannel channel;
	
	/**
	 * Initializes the zip file using the given byte channel which contains
	 * the ZIP file data.
	 *
	 * @param __sbc The source channel to read from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/02/26
	 */
	public StandardZIPFile(SeekableByteChannel __sbc)
		throws NullPointerException
	{
		// Check
		if (__sbc == null)
			throw new NullPointerException();
		
		// Set
		channel = __sbc;
	}
}

