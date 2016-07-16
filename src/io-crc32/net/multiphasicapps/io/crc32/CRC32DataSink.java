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
	/** Working buffer size. */
	private static final int _WORK_BUFFER =
		32;
	
	/** The polynomial to use. */
	protected final int polynomial;
	
	/** The final XOR value. */
	protected final int finalxor;
	
	/** Reflect the data? */
	protected final boolean reflectdata;
	
	/** Reflect the remainder? */
	protected final boolean reflectremainder;
	
	/** The CRC Table. */
	final __CRC32Table__ _table;
	
	/** The work buffer. */
	private byte[] _work =
		new byte[_WORK_BUFFER];
	
	/** The current CRC value (remainder). */
	private volatile int _remainder;
	
	/**
	 * Initializes the CRC-32 data sink.
	 *
	 * @param __rdata Reflect the data?
	 * @param __rrem Reflect the remainder?
	 * @param __poly The polynomial.
	 * @param __initrem The initial remainder.
	 * @param __fxor The value to XOR the remainder with on return.
	 * @since 2016/07/16
	 */
	public CRC32DataSink(boolean __rdata, boolean __rrem, int __poly,
		int __initrem, int __fxor)
	{
		// Set
		this.reflectdata = __rdata;
		this.reflectremainder = __rrem;
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
			int rem = this._remainder;
			return (this.reflectremainder ? Integer.reverse(rem) : rem) ^
				this.finalxor;
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
		// Read data into the work buffer
		byte[] work = this._work;
		boolean reflectdata = this.reflectdata;
		int remainder = this._remainder;
		int[] table = this._table._table;
		for (;;)
		{
			int rc = accept(work);
		
			// Nothing to process?
			if (rc <= 0)
				break;
		
			// Handle crc
			for (int i = 0; i < rc; i++)
			{
				// Read in data value
				int val = work[i] & 0xFF;
			
				// Reflect the data?
				if (reflectdata)
					val = Integer.reverse(val) >>> 24;
			
				int d = (val ^ (remainder >>> 24));
				remainder = table[d] ^ (remainder << 8);
			}
		}
		
		// Set new remainder
		this._remainder = remainder;
	}
}

