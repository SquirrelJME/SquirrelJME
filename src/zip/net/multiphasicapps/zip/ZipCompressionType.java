// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This represents the type of compression that is used in a ZIP file.
 *
 * @since 2016/07/15
 */
public enum ZipCompressionType
{
	/** Data is not compressed. */
	NO_COMPRESSION(10, 0),
	
	/** Deflate algorithm. */
	DEFLATE(20, 8),
	
	/** End. */
	;
	
	/** The default compression algorithm to use. */
	public static final ZipCompressionType DEFAULT_COMPRESSION =
		DEFLATE;
	
	/** The version needed to extract. */
	protected final int extractversion;
	
	/** The compression method. */
	protected final int method;
	
	/**
	 * Initializes the enumeration.
	 *
	 * @param __xv The version needed to extract.
	 * @param __m The compression method identifier.
	 * @since 2016/07/15
	 */
	private ZipCompressionType(int __xv, int __m)
	{
		this.extractversion = __xv;
		this.method = __m;
	}
	
	/**
	 * Returns the version which is needed to extract this data.
	 *
	 * @return The required version.
	 * @since 2016/07/15
	 */
	public final int extractVersion()
	{
		return this.extractversion;
	}
	
	/**
	 * Returns the compression method.
	 *
	 * @return The compression method.
	 * @since 2016/07/15
	 */
	public final int method()
	{
		return this.method;
	}
	
	/**
	 * Creates an output stream which wraps another for output which is used
	 * to write the associated data.
	 *
	 * @param __os The output stream to write into.
	 * @return An output stream for writing the data for this given compression
	 * method.
	 * @throws IOException If the stream could not be created.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/15
	 */
	public final OutputStream outputStream(OutputStream __os)
		throws IOException, NullPointerException
	{
		// Check
		if (__os == null)
			throw new NullPointerException("NARG");
		
		// Depends on the compression
		switch (this)
		{
				// No compression, just use a normally forwarding stream
			case NO_COMPRESSION:
				return new DataOutputStream(__os);
			
				// {@squirreljme.error BF01 Compressing using the given
				// compression algorithm is not supported. (The compression
				// algorithm)}
			default:
				throw new IOException(String.format("BF01 %s", this));
		}
	}
}

