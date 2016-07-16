// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.crc32;

import net.multiphasicapps.io.datasink.DataSink;
import net.multiphasicapps.io.datasink.SinkProcessException;

/**
 * This is a data sink which supports the CRC 32 algorithm.
 *
 * @since 2016/07/16
 */
public class CRC32DataSink
	extends DataSink
{
	/** The polynomial to use. */
	protected final int polynomial;
	
	/** The final XOR value. */
	protected final int finalxor;
	
	/** The CRC Table. */
	final __CRC32Table__ _table;
	
	/** The current CRC value (remainder). */
	private volatile int _remainder;
	
	/**
	 * Initializes the CRC-32 data sink.
	 *
	 * @param __poly The polynomial.
	 * @param __initrem The initial remainder.
	 * @since 2016/07/16
	 */
	public CRC32DataSink(int __poly, int __initrem, int __fxor)
	{
		// Set
		this.polynomial = __poly;
		this.finalxor = __fxor;
		this._remainder = __initrem;
		
		// Setup table
		this._table = __CRC32Table__.__table(__poly);
	}
	
	/**
	 * Returns the currently calculated CRC value.
	 *
	 * @return The current CRC value.
	 * @throws SinkProcessException If the stream encountered an error during
	 * processing.
	 * @since 2016/07/16
	 */
	public int crc()
		throws SinkProcessException
	{
		// Lock
		synchronized (this.lock)
		{
			// Flush to get the latest value
			super.flush();
			
			// Return the current CRC
			return this._remainder ^ this.finalxor;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/16
	 */
	@Override
	protected void process(int __n)
		throws SinkProcessException
	{
		throw new Error("TODO");
	}
}

