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
import net.multiphasicapps.io.CRC32Calculator;
import net.multiphasicapps.io.DecompressionInputStream;
import net.multiphasicapps.io.DynamicHistoryInputStream;
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
	
	/** The descriptor size if there is no header. */
	private static final int _HEADERLESS_DESCRIPTOR_SIZE =
		12;
	
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
	protected final DecompressionInputStream cin;
	
	/** Is the content length undefined? */
	protected final boolean undefined;
	
	/** Is EOF detected? */
	protected final boolean detectseof;
	
	/** The expected CRC. */
	protected final int expectedcrc;
	
	/** The expected file uncompressed size. */
	protected final int expecteduncompsize;
	
	/** The expected file compressed size. */
	protected final int expectedcompsize;
	
	/** Single byte read. */
	private final byte[] _solo =
		new byte[1];
	
	/** Used for peeking bytes to detect EOF. */
	private final byte[] _peeking;
	
	/** Has this been closed? */
	private volatile boolean _closed;
	
	/** The number of uncompressed bytes read. */
	private volatile long _readuncomp;
	
	/** Has EOF been reached? */
	private volatile boolean _eof;
	
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
		DecompressionInputStream dis;
		this.cin = (dis = __method.inputStream(__ins, this.crc));
		this.undefined = __undef;
		this.detectseof = dis.detectsEOF();
		this.expectedcrc = __crc;
		this.expecteduncompsize = __uncomp;
		this.expectedcompsize = __comp;
		this._peeking = (__undef ? new byte[_MAX_DESCRIPTOR_SIZE] : null);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/22
	 */
	@Override
	public void close()
		throws IOException
	{
		if (!this._closed)
		{
			// Mark closed
			this._closed = true;
			
			// Read all input bytes until EOF, except when EOF was already
			// reached
			if (!this._eof)
			{
				byte[] buf = new byte[512];
				while (read(buf) >= 0)
					;
			}
			
			// Tell the ZIP reader that this entry is gone and the next
			// can be read
			this.zipreader.__closeEntry(this);
		}
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
		
		// If EOF reached, do nothing
		if (this._eof)
			return -1;
		
		// Reading an undefined number of bytes?
		// If so then a data descriptor will need to be checked
		if (this.undefined)
		{
			// If EOF is detectable then read in the contents until such
			// things occur. Then read the data descriptor to verify that it
			// actually is correct
			if (this.detectseof)
				return __detectedRead(__b, __o, __l);
			
			// Otherwise, the input stream has to be peeked constantly to
			// detect the data descriptor.
			else
				return __probingRead(__b, __o, __l);
		}
		
		// Read of a defined number of bytes
		else
			return __definedRead(__b, __o, __l);
	}
	
	/**
	 * This is a read of input which has a defined size.
	 *
	 * @param __b The array to read into.
	 * @param __o The offset into the array.
	 * @param __l The number of bytes to potentially read.
	 * @return The number of bytes read.
	 * @throws IOException On read errors.
	 * @since 2017/08/23
	 */
	private int __definedRead(byte[] __b, int __o, int __l)
		throws IOException
	{
		// Needed to check things
		DecompressionInputStream cin = this.cin;
		long cinusz = cin.uncompressedBytes(),
			cincsz = cin.compressedBytes();
		
		// Never read more than the maximum in unsigned bytes
		int rest = (int)(this.expecteduncompsize - cinusz);
		if (__l > rest)
			__l = rest;
		
		// Read data
		int rc = this.cin.read(__b, __o, __l);
		
		// EOF reached?
		if (rc < 0)
		{
			// Mark EOF
			this._eof = true;
			
			// {@squirreljme.error BF0u Reached end of file in the entry
			// however the size it consumes and/or its CRC does not match
			// the expected values. (The expected CRC; The actual CRC;
			// The expected uncompressed size; The actual uncompressed
			// size; The expected compressed size; The actual compressed
			// size)}
			CRC32Calculator crc = this.crc;
			int expectedcrc = this.expectedcrc,
				expecteduncompsize = this.expecteduncompsize,
				expectedcompsize = this.expectedcompsize;
			if (expecteduncompsize != cinusz ||
				expectedcompsize != cincsz ||
				expectedcrc != crc.checksum())
				throw new ZipException(String.format(
					"BF0u %08x %08x %d %d %d %d", expectedcrc,
					crc.checksum(), expecteduncompsize, cinusz,
					expectedcompsize, cincsz));
			
			// Nothing read
			return -1;
		}
		
		// Mark as read
		this._readuncomp += rc;
		return rc;
	}
	
	/**
	 * Read of undefined size data, but where the EOF is detectable.
	 *
	 * @param __b The array to read into.
	 * @param __o The offset into the array.
	 * @param __l The number of bytes to potentially read.
	 * @return The number of bytes read.
	 * @throws IOException On read errors.
	 * @since 2017/08/23
	 */
	private int __detectedRead(byte[] __b, int __o, int __l)
		throws IOException
	{
		// This is as simple as reading the input bytes
		DecompressionInputStream cin = this.cin;
		int rc = cin.read(__b, __o, __l);
		
		// If EOF was not reached, then just return with the read bytes
		if (rc >= 0)
			return rc;
		
		// EOF was reached from the compressed stream, so the data descriptor
		// has to immedietly follow
		DynamicHistoryInputStream dhin = this.dhin;
		byte[] peeking = this._peeking;
		
		// Mark EOF so future reads fail
		this._eof = true;
		
		// {@squirreljme.error BF0v Could not find end of entry because the
		// entry exceeds the bounds of the ZIP file. (The number of read
		// bytes)}
		int probed = dhin.peek(0, peeking, 0, _MAX_DESCRIPTOR_SIZE);
		if (probed < _HEADERLESS_DESCRIPTOR_SIZE)
			throw new ZipException(String.format("BF0v %d", probed));
		
		// The specification says the descriptor is optional
		int offset = (_DESCRIPTOR_MAGIC_NUMBER ==
			ZipStreamReader.__readInt(peeking, 0) ? 4 : 0);

		// Read descriptor fields
		int ddcrc = ZipStreamReader.__readInt(peeking, offset),
			ddcomp = ZipStreamReader.__readInt(peeking, offset + 4),
			dduncomp = ZipStreamReader.__readInt(peeking, offset + 8);

		// {@squirreljme.error BF0w Reached end of file in the entry
		// however the size it consumes and/or its CRC does not match
		// the expected values. (The expected CRC; The actual CRC;
		// The expected uncompressed size; The actual uncompressed
		// size; The expected compressed size; The actual compressed
		// size)}
		CRC32Calculator crc = this.crc;
		long cinusz = cin.uncompressedBytes(),
			cincsz = cin.compressedBytes();
		if (dduncomp != cinusz ||
			ddcomp != cincsz ||
			ddcrc != crc.checksum())
			throw new ZipException(String.format(
				"BF0w %08x %08x %d %d %d %d", ddcrc,
				crc.checksum(), dduncomp, cinusz,
				ddcomp, cincsz));
		
		// EOF is OK now
		return -1;
	}
	
	/**
	 * Read of undefined size data, however since the input is not known it
	 * must be probed for the end to be detected.
	 *
	 * @param __b The array to read into.
	 * @param __o The offset into the array.
	 * @param __l The number of bytes to potentially read.
	 * @return The number of bytes read.
	 * @throws IOException On read errors.
	 * @since 2017/08/23
	 */
	private int __probingRead(byte[] __b, int __o, int __l)
		throws IOException
	{
		DynamicHistoryInputStream dhin = this.dhin;
		byte[] peeking = this._peeking;
		CRC32Calculator crc = this.crc;
		DecompressionInputStream cin = this.cin;
		
		// Due to the nature the end of a stream must be detected for this
		// data, the input must be read a single byte at a time which
		// introduces much overhead
		int d = 0;
		for (int i = __o, e = __o + __l; i < e; i++, d++)
		{
			// {@squirreljme.error BF0x Could not find end of entry because the
			// entry exceeds the bounds of the ZIP file. (The number of read
			// bytes)}
			int probed = dhin.peek(0, peeking, 0, _MAX_DESCRIPTOR_SIZE);
			if (probed < _HEADERLESS_DESCRIPTOR_SIZE)
				throw new ZipException(String.format("BF0x %d", probed));
		
			// According to the specification, the magic number is optional and
			// might not be specified
			// Regardless if it is or not, potentially skip it
			int offset = (_DESCRIPTOR_MAGIC_NUMBER ==
				ZipStreamReader.__readInt(peeking, 0) ? 4 : 0);
		
			// Read descriptor fields
			int ddcrc = ZipStreamReader.__readInt(peeking, offset),
				ddcomp = ZipStreamReader.__readInt(peeking, offset + 4),
				dduncomp = ZipStreamReader.__readInt(peeking, offset + 8);
			
			// EOF occurs?
			if (ddcomp == cin.compressedBytes() &&
				dduncomp == cin.uncompressedBytes() &&
				ddcrc == crc.checksum())
			{
				// Mark EOF
				this._eof = true;
				return (d == 0 ? -1 : d);
			}
			
			// {@squirreljme.error BF0y Reached end of file before the end
			// of the ZIP entry could be found.}
			int rc = cin.read();
			if (rc < 0)
				throw new ZipException("BF0y");
			__b[i] = (byte)rc; 
		}
		
		// Read count
		return d;
	}
}

