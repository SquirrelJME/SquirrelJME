// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip.streamreader;

import java.io.Flushable;
import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.io.crc32.CRC32Calculator;
import net.multiphasicapps.io.compressionstream.CompressionInputStream;
import net.multiphasicapps.io.dynhistin.DynamicHistoryInputStream;
import net.multiphasicapps.zip.ZipCompressionType;
import net.multiphasicapps.zip.ZipCRCConstants;
import net.multiphasicapps.zip.ZipException;

/**
 * This provides an interface to interact with a single entry within a ZIP
 * stream.
 *
 * This class is not thread safe.
 *
 * @since 2016/07/19
 */
public final class ZipStreamEntry
	extends InputStream
{
	/** The maximum size the data descriptor can be (if there is one). */
	private static final int _MAX_DESCRIPTOR_SIZE =
		16;
	
	/** Data descriptor magic number. */
	private static final int _DESCRIPTOR_MAGIC_NUMBER =
		0x08074B50;
	
	/** CRC calculation. */
	protected final CRC32Calculator crc =
		new CRC32Calculator(ZipCRCConstants.CRC_REFLECT_DATA,
			ZipCRCConstants.CRC_REFLECT_REMAINDER,
			ZipCRCConstants.CRC_POLYNOMIAL, ZipCRCConstants.CRC_REMAINDER,
			ZipCRCConstants.CRC_FINALXOR);
	
	/** The owning stream reader. */
	protected final ZipStreamReader zipreader;
	
	/** The name of the file. */
	protected final String filename;
	
	/** The compression method. */
	protected final ZipCompressionType method;
	
	/** The dynamic input stream to read from. */
	protected final DynamicHistoryInputStream dhin;
	
	/** The compressed stream which also has counting. */
	protected final CompressionInputStream cin;
	
	/** Is the content length undefined? */
	protected final boolean undefined;
	
	/** The expected CRC. */
	protected final int expectedcrc;
	
	/** The expected file uncompressed size. */
	protected final int expecteduncompsize;
	
	/** The expected file compressed size. */
	protected final int expectedcompsize;
	
	/** Single byte read. */
	private final byte[] _solo =
		new byte[1];
	
	/** Has this been closed? */
	private volatile boolean _closed;
	
	/** The number of uncompressed bytes read. */
	private volatile long _readuncomp;
	
	/**
	 * Initializes the entry.
	 *
	 * @param __zsr The owning stream reader.
	 * @param __fn The name of the entry.
	 * @param __undef Is the size and CRC undefined?
	 * @param __crc The expected CRC.
	 * @param __comp The compressed size.
	 * @param __uncomp The uncompressed size.
	 * @param __method The compression method.
	 * @param __ins The input data source.
	 * @throws IOException If the decompressor could not be initialized.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/19
	 */
	ZipStreamEntry(ZipStreamReader __zsr, String __fn, boolean __undef,
		int __crc, int __comp, int __uncomp, ZipCompressionType __method,
		DynamicHistoryInputStream __ins)
		throws IOException, NullPointerException
	{
		// Check
		if (__zsr == null || __fn == null || __method == null ||
			__ins == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.zipreader = __zsr;
		this.filename = __fn;
		this.method = __method;
		this.dhin = __ins;
		this.cin = __method.inputStream(__ins, this.crc);
		this.undefined = __undef;
		this.expectedcrc = __crc;
		this.expecteduncompsize = __uncomp;
		this.expectedcompsize = __comp;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/22
	 */
	@Override
	public void close()
		throws IOException
	{
		throw new todo.TODO();
		/*
		if (!this._closed)
		{
			// Mark closed
			this._closed = true;
			
			// Read all the remaining bytes
			while (read() >= 0)
				;
			
			// Calculate the CRC
			__HigherStream__ higher = this._higher;
			higher.flush();
			
			// Close the higher end
			this._higher.close();
			
			// {@squirreljme.error BG03 The expected CRC and the actual
			// CRC do not match, the data is corrupt. (The expected CRC;
			// The actual CRC)}
			int wantcrc, wascrc;
			if ((wantcrc = this._wantcrc) !=
				(wascrc = higher.crccalc.checksum()))
				throw new ZipException(String.format("BG03 %08x %08x",
					wantcrc, wascrc));
			
			// Clear it
			this.zipreader.__closeEntry(this);
		}*/
	}
	
	/**
	 * Returns the compression type that the entry uses.
	 *
	 * @return The compression type.
	 * @since 2016/07/19
	 */
	public ZipCompressionType compressionType()
	{
		return this.method;
	}
	
	/**
	 * Returns the name of the entry.
	 *
	 * @return The entry name.
	 * @since 2016/07/19
	 */
	public String name()
	{
		return this.filename;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/19
	 */
	@Override
	public int read()
		throws IOException
	{
		// Try reading a single byte
		byte[] solo = this._solo;
		for (;;)
		{
			int rv = read(solo, 0, 1);
			
			// EOF?
			if (rv < 0)
				return rv;
			
			// Try again
			else if (rv == 0)
				continue;
			
			// Return that byte
			else
				return (solo[0] & 0xFF);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/22
	 */
	@Override
	public int read(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		int n = __b.length;
		if (__o < 0 || __l < 0 || (__o + __l) > n)
			throw new IndexOutOfBoundsException("IOOB");
		
		CRC32Calculator crc = this.crc;
		CompressionInputStream cin = this.cin;
		long cinusz = cin.uncompressedBytes(),
			cincsz = cin.compressedBytes();
		
		// Reading an undefined number of bytes?
		boolean undefined = this.undefined;
		if (undefined)
			throw new todo.TODO();
		
		// Read of a defined number of bytes
		else
		{
			// Never read more than the maximum in unsigned bytes
			int rest = (int)(this.expecteduncompsize - cinusz);
			if (__l > rest)
				__l = rest;
			
			// Read data
			int rc = this.cin.read(__b, __o, __l);
			
			// EOF reached?
			if (rc < 0)
				throw new todo.TODO();
			
			// Mark as read
			this._readuncomp += rc;
			return rc;
		}
	}
}

