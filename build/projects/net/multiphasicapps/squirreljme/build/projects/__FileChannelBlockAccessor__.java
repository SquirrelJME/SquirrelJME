// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.projects;

import java.io.IOException;
import java.nio.channels.FileChannel;
import net.multiphasicapps.zip.blockreader.BlockAccessor;

/**
 * This wraps a file channel and provides block level access to it.
 *
 * @since 2016/12/27
 */
class __FileChannelBlockAccessor__
	implements BlockAccessor
{
	/** The file channel to wrap. */
	protected final FileChannel channel;
	
	/**
	 * Initializes the block accessor for the file channel.
	 *
	 * @param __fc The channel to access data from.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/27
	 */
	__FileChannelBlockAccessor__(FileChannel __fc)
		throws IOException, NullPointerException
	{
		// Check
		if (__fc == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.channel = __fc;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/27
	 */
	@Override
	public void close()
		throws IOException
	{
		this.channel.close();
	}
}

