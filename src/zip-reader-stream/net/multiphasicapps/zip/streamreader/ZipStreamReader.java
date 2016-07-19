// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip.streamreader;

import java.io.Closeable;
import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.io.crc32.CRC32DataSink;
import net.multiphasicapps.io.dynhistin.DynamicHistoryInputStream;
import net.multiphasicapps.zip.ZipCompressionType;

/**
 * This class supports stream based reading of input ZIP files.
 *
 * @since 2016/07/19
 */
public class ZipStreamReader
	implements Closeable
{
	/** The dynamic history stream. */
	protected final DynamicHistoryInputStream input;
	
	/**
	 * Initializes the reader for input ZIP file data.
	 *
	 * @param __is The input stream to source bytes from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/19
	 */
	public ZipStreamReader(InputStream __is)
		throws NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.input = new DynamicHistoryInputStream(__is);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/19
	 */
	@Override
	public void close()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the next entry in the streamed ZIP file for {@code null} if no
	 * such entry exists.
	 *
	 * @return The next entry or {@code null} if there is none.
	 * @throws IOException On read errors.
	 * @since 2016/07/19
	 */
	public ZipStreamEntry nextEntry()
		throws IOException
	{
		throw new Error("TODO");
	}
}

