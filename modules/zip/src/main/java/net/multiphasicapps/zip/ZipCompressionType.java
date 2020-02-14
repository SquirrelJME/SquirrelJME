// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.io.Checksum;
import net.multiphasicapps.io.ChecksumInputStream;
import net.multiphasicapps.io.CompressionLevel;
import net.multiphasicapps.io.DecompressionInputStream;
import net.multiphasicapps.io.DeflaterOutputStream;
import net.multiphasicapps.io.InflaterInputStream;

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
	 * Creates an input stream which wraps another for input which is used
	 * to read the associated data.
	 *
	 * @param __is The input stream to read from.
	 * @return An input stream for reading the data for this given
	 * compression method.
	 * @throws IOException If the stream could not be initialized.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/19
	 */
	public final DecompressionInputStream inputStream(InputStream __is)
		throws IOException, NullPointerException
	{
		return inputStream(__is, null);
	}
	
	/**
	 * Creates an input stream which wraps another for input which is used
	 * to read the associated data.
	 *
	 * @param __is The input stream to read from.
	 * @param __cs An optional target checksum where the CRC for uncompressed
	 * data is stored.
	 * @return An input stream for reading the data for this given
	 * compression method.
	 * @throws IOException If the stream could not be initialized.
	 * @throws NullPointerException If no input stream was specified.
	 * @since 2016/08/22
	 */
	public final DecompressionInputStream inputStream(InputStream __is,
		Checksum __cs)
		throws IOException, NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// Depends on the compression
		switch (this)
		{
				// Just use the same stream
			case NO_COMPRESSION:
				return new __NoDecompressionInputStream__((__cs != null ?
					new ChecksumInputStream(__cs, __is) : __is));
			
				// Inflate
			case DEFLATE:
				return new InflaterInputStream(__is, __cs);
				
				// {@squirreljme.error BF02 Decompressing using the given
				// method is not supported. (The current compression method)}
			default:
				throw new IOException(String.format("BF02 %s", this));
		}
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
	 * to write the associated data. The default compression level is used.
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
		return this.outputStream(__os, CompressionLevel.DEFAULT);
	}
	
	/**
	 * Creates an output stream which wraps another for output which is used
	 * to write the associated data.
	 *
	 * @param __os The output stream to write into.
	 * @param __cl The compression level to use.
	 * @return An output stream for writing the data for this given compression
	 * method.
	 * @throws IOException If the stream could not be created.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/15
	 */
	public final OutputStream outputStream(OutputStream __os,
		CompressionLevel __cl)
		throws IOException, NullPointerException
	{
		// Check
		if (__os == null || __cl == null)
			throw new NullPointerException("NARG");
		
		// Depends on the compression
		switch (this)
		{
				// No compression, use the same stream
			case NO_COMPRESSION:
				return __os;
				
				// Inflate
			case DEFLATE:
				return new DeflaterOutputStream(__os, __cl);
			
				// {@squirreljme.error BF03 Compressing using the given
				// compression algorithm is not supported. (The compression
				// algorithm)}
			default:
				throw new IOException(String.format("BF03 %s", this));
		}
	}
	
	/**
	 * Returns the compression method identified by the given value.
	 *
	 * @param __m The compression method identifier.
	 * @return The compression type identifier for this method, or {@code null}
	 * if it is unknown.
	 * @since 2016/07/19
	 */
	public static ZipCompressionType forMethod(int __m)
	{
		switch (__m)
		{
			case 0: return ZipCompressionType.NO_COMPRESSION;
			case 8: return ZipCompressionType.DEFLATE;
			
				// Unknown
			default:
				return null;
		}
	}
}

