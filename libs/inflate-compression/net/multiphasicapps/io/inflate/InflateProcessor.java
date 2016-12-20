// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
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

