// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import java.io.IOException;

/**
 * This is a data processor which handles RFC 1951 deflate streams.
 *
 * @since 2016/03/11
 */
public class DeflateDataProcessor
	extends DataProcessor
{
	/** Input bits. */
	protected final CircularBitBuffer inputbits =
		new CircularBitBuffer(input);
	
	/** Output bits. */
	protected final CircularBitBuffer outputbits =
		new CircularBitBuffer(output);
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/11
	 */
	@Override
	protected void process()
		throws IOException
	{
		throw new Error("TODO");
	}
}

