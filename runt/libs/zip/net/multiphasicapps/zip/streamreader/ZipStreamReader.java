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

import java.io.Closeable;
import java.io.EOFException;
import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.io.CRC32Calculator;
import net.multiphasicapps.io.DataEndianess;
import net.multiphasicapps.io.DynamicHistoryInputStream;
import net.multiphasicapps.io.ExtendedDataInputStream;
import net.multiphasicapps.zip.IBM437CodePage;
import net.multiphasicapps.zip.ZipCompressionType;
import net.multiphasicapps.zip.ZipException;

/**
 * This class supports stream based reading of input ZIP files.
 *
 * Only files up to 2GiB in length are supported. If a data descriptor is
 * specified for entries then they must have the optional descriptor magic
 * number included.
 *
 * This class is not thread safe.
 *
 * @since 2016/07/19
 */
public class ZipStreamReader
	implements Closeable
{
	/** The maximum support version for extracting. */
	private static final int _MAX_EXTRACT_VERSION =
		20;
	
	/** The minumum size of the local header. */
	private static final int _MINIMUM_HEADER_SIZE =
		30;
	
	/** The local header magic number. */
	private static final int _LOCAL_HEADER_MAGIC =
		0x04034B50;
	
	/** The dynamic history stream. */
	protected final DynamicHistoryInputStream input;
	
	/** This is used after an input structure is detected. */
	protected final ExtendedDataInputStream data;
	
	/** This can hold the local header except for the comment and filename. */
	private final byte[] _localheader =
		new byte[_MINIMUM_HEADER_SIZE];
	
	/** The current entry being read, cannot next entry if this is the case. */
	private volatile ZipStreamEntry _entry;
	
	/** End of file reached? */
	private volatile boolean _eof;
	
	/** Deferred exceptions, set after an entry read fails. */
	private volatile ZipException _defer;
	
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
		DynamicHistoryInputStream q;
		this.input = (q = new DynamicHistoryInputStream(__is));
		ExtendedDataInputStream d;
		this.data = (d = new ExtendedDataInputStream(q));
		d.setEndianess(DataEndianess.LITTLE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/19
	 */
	@Override
	public void close()
		throws IOException
	{
		// Mark EOF
		this._eof = true;
		
		// Close the source
		this.input.close();
		this.data.close();
	}
	
	/**
	 * If an entry is detected and it could not be read, then this exception
	 * may be set to detect such events.
	 *
	 * @return The deferred exception or {@code null} if there is none.
	 * @since 2016/09/11
	 */
	public ZipException deferred()
	{
		ZipException rv = this._defer;
		this._defer = null;
		return rv;
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
		// {@squirreljme.error BF0z An entry is currently being read, it
		// must first be closed.}
		if (this._entry != null)
			throw new IOException("BF0z");
		
		// End of file reached?
		if (this._eof)
			return null;
		
		// Read until an entry is found
		DynamicHistoryInputStream input = this.input;
		ExtendedDataInputStream data = this.data;
		byte[] localheader = this._localheader;
		for (; !this._eof;)
		{
			// Peek the magic number
			int rhcount = input.peek(0, localheader, 0, 4);
		
			// Could not fit the magic number, treat as EOF
			if (rhcount < 4)
			{
				this._eof = true;
				return null;
			}
		
			// Does not match the magic number for local headers
			int lhskip = __skipLocalHeader(localheader);
			
			// Not one
			if (lhskip > 0)
			{
				// Read
				try
				{
					data.readFully(localheader, 0, lhskip);
				}
				
				// End of file
				catch (EOFException e)
				{
					this._eof = true;
				}
				
				// Return null on the next loop
				continue;
			}
			
			// Read the rest of the header
			rhcount = input.peek(0, localheader);
			
			// EOF reached (cannot fit a local header in this many bytes)
			// Ignore the somewhat malformed ZIP since it could be part of
			// another file structure due to polyglots
			if (rhcount < _MINIMUM_HEADER_SIZE)
			{
				this._eof = true;
				return null;
			}
			
			// Deferred exception?
			ZipException defer = null;
			
			// Check the version needed for extracting
			// Note that some ZIP writing software sets the upper byte when it
			// should not. Since the made by version is not stored in the
			// local file header, the byte will just be stripped.
			int xver = __readUnsignedShort(localheader, 4) & 0xFF;
			boolean deny = false;
			deny |= (xver < 0 || xver > _MAX_EXTRACT_VERSION);
			
			// {@squirreljme.error BF10 Zip version not suppored. (The
			// version)}
			if (defer == null && deny)
				defer = new ZipException(String.format("BF10 %d",
					xver));
			
			// Read bit flags
			int gpfs = __readUnsignedShort(localheader, 6);
			boolean utf = (0 != (gpfs & (1 << 11)));
			boolean undefinedsize = (0 != (gpfs & (1 << 3)));
			
			// Cannot read encrypted entries
			deny |= (0 != (gpfs & 1));
			
			// {@squirreljme.error BF11 Encrypted entries not supported.}
			if (defer == null && deny)
				defer = new ZipException("BF11");
			
			// Read the compression method
			ZipCompressionType cmeth = ZipCompressionType.forMethod(
				__readUnsignedShort(localheader, 8));
			deny |= (cmeth == null);
			
			// {@squirreljme.error BF12 Compression method not supported.
			// (The method)}
			if (defer == null && deny)
				defer = new ZipException(String.format("BF12 %d", cmeth));
			
			// Read CRC32
			int crc = __readInt(localheader, 14);
			
			// Read Compressed size
			int csz = __readInt(localheader, 18);
			if (!undefinedsize)
				deny |= (csz < 0);
			
			// Uncompressed size
			int usz = __readInt(localheader, 22);
			if (!undefinedsize)
				deny |= (usz < 0);
			
			// {@squirreljme.error BF13 Entry exceeds 2GiB in size.
			// (The compressed size; The uncompressed size)}
			if (defer == null && deny)
				defer = new ZipException(String.format("BF13 %d %d", csz,
					usz));
			
			// File name length
			int fnl = __readUnsignedShort(localheader, 26);
			
			// Comment length
			int cml = __readUnsignedShort(localheader, 28);
			
			// If denying, read a single byte and try again, this could
			// just be very ZIP-like data or the local header number could
			// be a constant in an executable.
			if (deny)
			{
				// Defer the issue, if set
				if (defer != null)
					this._defer = defer;
				
				// Skip 4 bytes because the header was already read
				this.data.readFully(localheader, 0, 4);
				continue;
			}
			
			// Read the local header normally to consume it
			data.readFully(localheader);
			
			// Read the file name, if EOF was reached then ignore
			byte[] rawname = new byte[fnl];
			data.readFully(rawname);
			
			// If UTF-8 then use internal handling
			String filename;
			if (utf)
				filename = new String(rawname, 0, fnl, "utf-8");
		
			// Otherwise use codepage handling, Java ME only has two
			// character sets available
			else
				filename = IBM437CodePage.toString(rawname, 0, fnl);
			
			// Skip the comment
			data.readFully(localheader, 0, Math.min(cml,
				_MINIMUM_HEADER_SIZE));
			
			// Create entry so the data can actually be used
			ZipStreamEntry rv = new ZipStreamEntry(this, filename,
				undefinedsize, crc, csz, usz, cmeth, input);
			this._entry = rv;
			return rv;
		}
		
		// No entry
		this._eof = true;
		return null;
	}
	
	/**
	 * Closes an entry so that the next one can be read.
	 *
	 * @param __ent The entry to close.
	 * @throws IOException If it could not be closed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/20
	 */
	final void __closeEntry(ZipStreamEntry __ent)
		throws IOException, NullPointerException
	{
		// Check
		if (__ent == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BF14 Close of an incorrect entry.}
		if (this._entry != __ent)
			throw new IOException("BF14");
		
		// Clear it
		this._entry = null;
	}
	
	/**
	 * Reads an unsigned integer value.
	 *
	 * @param __b The byte array to read from.
	 * @param __p The position to read from.
	 * @return The read value.
	 * @since 2016/07/19
	 */
	static int __readInt(byte[] __b, int __p)
	{
		return (__b[__p] & 0xFF) |
			((__b[__p + 1] & 0xFF) << 8) |
			((__b[__p + 2] & 0xFF) << 16) |
			((__b[__p + 3] & 0xFF) << 24);
	}
	
	/**
	 * Reads an unsigned short from the given byte array.
	 *
	 * @param __b The byte array to read from.
	 * @param __p The position to read from.
	 * @return The read value.
	 * @since 2016/07/19
	 */
	static int __readUnsignedShort(byte[] __b, int __p)
	{
		return (__b[__p] & 0xFF) |
			((__b[__p + 1] & 0xFF) << 8);
	}
	
	/**
	 * Checks if the specified buffer starts with the local header magic
	 * number and if not returns the number of bytes to skip.
	 *
	 * @param __b The bytes to check, from the zero index.
	 * @return Zero means this is the local header, otherwise a value up to 4.
	 * @since 2016/07/19
	 */
	private static int __skipLocalHeader(byte[] __b)
	{
		// Read values
		byte lha = __b[0], lhb = __b[1], lhc = __b[2], lhd = __b[3];
		
		// Is this the magic number?
		if (lha == 0x50 && lhb == 0x4B && lhc == 0x03 && lhd == 0x04)
			return 0;
		
		// Next byte over
		else if (lhb == 0x50 && lhc == 0x4B && lhd == 0x03)
			return 1;
		
		// Skip two bytes
		else if (lhc == 0x50 && lhd == 0x4B)
			return 2;
		
		// Last byte could be it
		if (lhd == 0x50)
			return 3;
		
		// None of them
		return 4;
	}
}

