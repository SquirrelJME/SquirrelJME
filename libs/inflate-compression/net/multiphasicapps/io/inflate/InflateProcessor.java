// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.inflate;

import java.io.IOException;
import net.multiphasicapps.io.streamproc.StreamProcessor;

/**
 * This is used to inflate deflate compressed streams.
 *
 * @since 2016/12/20
 */
public class InflateProcessor
	extends StreamProcessor
{
	/**
	 * {@inheritDoc}
	 * @since 2016/12/20
	 */
	@Override
	public void close()
		throws IOException
	{
		// Does nothing
	}
}

