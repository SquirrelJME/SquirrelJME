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
	/**
	 * Required non-finished bits in the queue, this is for optimal processing
	 * so that partial states are simpler.
	 */
	protected static final int REQUIRED_BITS =
		48;
	
	/** Input bits. */
	protected final CircularBitBuffer inputbits =
		new CircularBitBuffer();
	
	/** The bit compactor for queing added bits. */
	protected final BitCompactor compactor =
		new BitCompactor(new BitCompactor.Callback()
			{
				/**
				 * {@inheritDoc}
				 * @since 2016/03/11
				 */
				@Override
				public void ready(byte __v)
				{
					// Give it to the output data
					output.offerLast(__v);
				}
			});
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/11
	 */
	@Override
	protected void process()
		throws IOException, WaitingException
	{
		// Take all bytes which are available to the input and add them to the
		// input bit buffer
		while (input.hasAvailable())
			inputbits.offerLastInt(input.removeFirst(), 0xFF);
		
		// Require more available bytes if not finished
		if (!isFinished() && inputbits.available() < REQUIRED_BITS)
			throw new WaitingException();
		
		throw new Error("TODO");
	}
}

