// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip.streamwriter;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import net.multiphasicapps.io.CRC32Calculator;
import net.multiphasicapps.io.DataEndianess;
import net.multiphasicapps.io.ExtendedDataOutputStream;
import net.multiphasicapps.zip.ZipCompressionType;
import net.multiphasicapps.zip.ZipCRCConstants;

/**
 * This class is used to write to ZIP files in an unknown and stream based
 * manner where the size of the contents is completely unknown.
 *
 * When the stream is closed, the central directory of the ZIP file will be
 * written to the end of the file.
 *
 * This class is not thread safe.
 *
 * @since 2016/07/09
 */
public class ZipStreamWriter
	implements Closeable, Flushable
{
	/** The magic number for local files. */
	private static final int _LOCAL_FILE_MAGIC_NUMBER =
		0x04034B50;
	
	/** Data descriptor magic. */
	private static final int _DATA_DESCRIPTOR_MAGIC_NUMBER =
		0x08074B50;
	
	/** Central directory entry magic number. */
	private static final int _CENTRAL_DIRECTORY_MAGIC_NUMBER =
		0x02014B50;
	
	/** End of central directory magic number. */
	private static final int _END_DIRECTORY_MAGIC_NUMBER =
		0x06054B50;
	
	/** The maximum permitted file size. */
	private static final long _MAX_FILE_SIZE =
		0xFFFFFFFFL;
	
	/** General purpose flags for entries (use descriptor; UTF-8 names). */
	private static final int _GENERAL_PURPOSE_FLAGS =
		(1 << 3) | (1 << 11);
	
	/** The output stream to write to. */
	protected final ExtendedDataOutputStream output;
	
	/** Table of contents. */
	private final LinkedList<__TOCEntry__> _toc =
		new LinkedList<>();
	
	/** Was this stream closed? */
	private volatile boolean _closed;
	
	/** The current entry output (the inner portion). */
	private volatile __InnerOutputStream__ _inner;
	
	/** The current entry output (the outer portion). */
	private volatile __OuterOutputStream__ _outer;
	
	/** The best version number. */
	private volatile int _bestversion =
		Math.max(20, ZipCompressionType.DEFLATE.extractVersion());
	
	/**
	 * This initializes the stream for writing ZIP file data.
	 *
	 * @param __os The output stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/09
	 */
	public ZipStreamWriter(OutputStream __os)
		throws NullPointerException
	{
		// Check
		if (__os == null)
			throw new NullPointerException("NARG");
		
		// Create stream
		ExtendedDataOutputStream output;
		this.output = (output = new ExtendedDataOutputStream(__os));
		
		// Use little endian data by default
		output.setEndianess(DataEndianess.LITTLE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/09
	 */
	@Override
	public void close()
		throws IOException
	{
		// Do nothing if already closed
		if (this._closed)
			return;
		
		// {@squirreljme.error BF15 Cannot close the ZIP writer because
		// an entry is still being written.}
		if (this._inner != null || this._outer != null)
			throw new IOException("BF15");
		
		// Mark closed to prevent failing closes from writing multiple
		// times
		this._closed = true;
		
		// Get output and the TOC entries
		ExtendedDataOutputStream output = this.output;
		LinkedList<__TOCEntry__> toc = this._toc;
		int numtoc = toc.size();
		
		// The position where the central directory starts
		long cdstart = output.size();
		
		// Write all entries
		int bestversion = this._bestversion;
		for (__TOCEntry__ entry : toc)
		{
			// The entry position
			long epos = output.size();
			
			if (epos > _MAX_FILE_SIZE)
				throw new IOException();
			
			// Write directory header
			output.writeInt(_CENTRAL_DIRECTORY_MAGIC_NUMBER);
			
			// The created by version (use the highest version)
			output.writeShort(bestversion);
			
			// Version needed to extract
			ZipCompressionType ecomp = entry._compression;
			output.writeShort(ecomp.extractVersion());
			
			// General purpose flags
			output.writeShort(_GENERAL_PURPOSE_FLAGS);
			
			// Compression method
			output.writeShort(ecomp.method());
			
			// Date/time not used
			output.writeShort(0);
			output.writeShort(0);
			
			// CRC and sizes
			output.writeInt(entry._crc);
			output.writeInt((int)entry._compressed);
			output.writeInt((int)entry._uncompressed);
			
			// Write name length
			byte[] efn = entry._name;
			output.writeShort(efn.length);
			
			// No extra data
			output.writeShort(0);
			
			// No comment
			output.writeShort(0);
			
			// Always the first disk
			output.writeShort(0);
			
			// No iternal or external attributes
			output.writeShort(0);
			output.writeInt(0);
			
			// Relative offset to local header
			output.writeInt((int)entry._localposition);
			
			// Write file name
			output.write(efn);
		}
		
		// The position where it ends
		long cdend = output.size();
		
		// Write magic number
		output.writeInt(_END_DIRECTORY_MAGIC_NUMBER);
		
		// Only a single disk is written
		output.writeShort(0);
		output.writeShort(0);
		
		// Number of entries on this disk and in all of them
		output.writeShort(numtoc);
		output.writeShort(numtoc);
		
		// The size of the central directory
		output.writeInt((int)(cdend - cdstart));
		
		// Offset to the central directory
		output.writeInt((int)cdstart);
		
		// No comment
		output.writeShort(0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/09
	 */
	@Override
	public void flush()
		throws IOException
	{
		this.output.flush();
	}
	
	/**
	 * Starts writing a new entry in the output ZIP using the default
	 * compression.
	 *
	 * @param __name The name of the entry.
	 * @return An {@link OutputStream} which is used to write the ZIP file
	 * data.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/27
	 */
	public OutputStream nextEntry(String __name)
		throws IOException, NullPointerException
	{
		return nextEntry(__name, ZipCompressionType.DEFAULT_COMPRESSION);
	}
	
	/**
	 * Starts writing a new entry in the output ZIP.
	 *
	 * @param __name The name of the entry.
	 * @param __comp The compression method used.
	 * @return An {@link OutputStream} which is used to write the ZIP file
	 * data.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/15
	 */
	public OutputStream nextEntry(String __name, ZipCompressionType __comp)
		throws IOException, NullPointerException
	{
		// Check
		if (__name == null || __comp == null)
			throw new NullPointerException("NARG");
		
		// Lock
		LinkedList<__TOCEntry__> toc = this._toc;
		
		// {@squirreljme.error BF16 Cannot write new entry because the ZIP
		// has been closed.}
		if (this._closed)
			throw new IOException("BF16");
		
		// {@squirreljme.error BF17 Cannot create a new entry for output
		// because the previous entry has not be closed.}
		if (this._inner != null || this._outer != null)
			throw new IOException("BF17");
		
		// {@squirreljme.error BF18 A ZIP file cannot have more than
		// 65536 entries.}
		if (toc.size() >= 65535)
			throw new IOException("BF18");
		
		// {@squirreljme.error BF19 The length of the input file exceeds
		// 65535 UTF-8 characters. (The filename length)}
		byte[] utfname = __name.getBytes("utf-8");
		int fnn;
		if ((fnn = utfname.length) > 65535)
			throw new IOException(String.format("BF19 %d", fnn));
		
		// Setup contents
		__TOCEntry__ last = new __TOCEntry__(this.output.size(), utfname,
			__comp);
		toc.addLast(last);
		
		// Write ZIP header data
		ExtendedDataOutputStream output = this.output;
		output.writeInt(_LOCAL_FILE_MAGIC_NUMBER);
		
		// Extract version
		output.writeShort(__comp.extractVersion());
		
		// General purpose flag
		output.writeShort(_GENERAL_PURPOSE_FLAGS);
		
		// Method
		output.writeShort(__comp.method());
		
		// Modification date/time
		output.writeShort(0);
		output.writeShort(0);
		
		// CRC-32 and compress/uncompressed size are unknown
		output.writeInt(0);
		output.writeInt(0);
		output.writeInt(0);
		
		// Write file name bytes
		output.writeShort(fnn);
		
		// No extra field
		output.writeShort(0);
		
		// Write file name
		output.write(utfname);
		
		// Setup inner stream (for compressed size)
		__InnerOutputStream__ inner = new __InnerOutputStream__();
		
		// Wrap inner with the compression algorithm
		OutputStream wrapped = __comp.outputStream(inner);
		
		// Wrap that with the outer stream (uncompressed size)
		__OuterOutputStream__ outer = new __OuterOutputStream__(wrapped);
		
		// Set
		this._inner = inner;
		this._outer = outer;
		
		// Return the outer stream
		return outer;
	}
	
	/**
	 * Closes the current entry.
	 *
	 * @throws IOException If it could not be closed.
	 * @since 2016/07/15
	 */
	private void __closeEntry()
		throws IOException
	{
		// Lock
		LinkedList<__TOCEntry__> toc = this._toc;
		
		__InnerOutputStream__ inner = this._inner;
		__OuterOutputStream__ outer = this._outer;
		
		// {@squirreljme.error BF1a Cannot close entry because a current
		// one is not being used.}
		if (inner == null || outer == null)
			throw new IOException("BF1a");
		
		// Flush both sides
		inner.flush();
		outer.flush();
		
		// Need to fill the size information and CRC for later
		__TOCEntry__ last = toc.getLast();
		
		// Get sizes
		long uncomp = outer._size;
		long comp = inner._size;
		
		// {@squirreljme.error BF1b Either one or both of the compressed
		// or uncompressed file sizes exceeds 4GiB. (The uncompressed size;
		// The compressed size)}
		if (uncomp >= _MAX_FILE_SIZE || comp >= _MAX_FILE_SIZE)
			throw new IOException(String.format("BF1b %d %d", uncomp,
				comp));
		
		// Store sizes
		last._uncompressed = uncomp;
		last._compressed = comp;
		
		// Determine CRC
		int crc = outer.crccalc.checksum();
		last._crc = crc;
		
		// The magic number of the data descriptor is not needed, however
		// it helps prevent some abiguity when the input data stream is
		// not compressed and contains a ZIP file.
		ExtendedDataOutputStream output = this.output;
		output.writeInt(_DATA_DESCRIPTOR_MAGIC_NUMBER);
		
		// Write CRC and sizes
		output.writeInt((int)crc);
		output.writeInt((int)comp);
		output.writeInt((int)uncomp);
		
		// Clear streams to allow for next entry
		this._inner = null;
		this._outer = null;
	}
	
	/**
	 * The inner and outer streams are very similar.
	 *
	 * @since 2016/07/15
	 */
	private abstract class __BaseOutputStream__
		extends OutputStream
	{
		/** The wrapped stream. */
		protected final OutputStream wrapped;
		
		/** Is the outer side finished? */
		protected volatile boolean finished;
		
		/** The decompressed size. */
		volatile int _size;
		
		/**
		 * Initializes a new output stream for writing an entry.
		 *
		 * @param __os The output stream to wrap.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/07/15
		 */
		private __BaseOutputStream__(OutputStream __os)
			throws NullPointerException
		{
			// Check
			if (__os == null)
				throw new NullPointerException("NARG");
			
			// Set
			this.wrapped = __os;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/07/15
		 */
		@Override
		public abstract void close()
			throws IOException;
		
		/**
		 * {@inheritDoc}
		 * @since 2016/07/15
		 */
		@Override
		public void flush()
			throws IOException
		{
			// Ignore if finished since the streams should be disconnected
			// at this time
			if (this.finished)
				return;
			
			// Forward flush
			this.wrapped.flush();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/07/15
		 */
		@Override
		public void write(int __b)
			throws IOException
		{
			// {@squirreljme.error BF1c Cannot write a single byte because
			// the stream is closed.}
			if (this.finished)
				throw new IOException("BF1c");
			
			// {@squirreljme.error BF1d Cannot write a single byte because
			// the ZIP entry would exceed 4GiB.}
			int oldsize = this._size, newsize = oldsize + 1;
			if (newsize < 0 || newsize < oldsize)
				throw new IOException("BF1d");
			
			// Write data and increase size
			this.wrapped.write(__b);
			this._size = newsize;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/07/15
		 */
		@Override
		public void write(byte[] __b, int __o, int __l)
			throws IndexOutOfBoundsException, IOException, NullPointerException
		{
			// Check
			if (__b == null)
				throw new NullPointerException("NARG");
			int n = __b.length;
			if (__o < 0 || __l < 0 || (__o + __l) > n)
				throw new IndexOutOfBoundsException("IOOB");
			
			// {@squirreljme.error BF1e Cannot write multiple bytes because
			// the stream is closed.}
			if (this.finished)
				throw new IOException("BF1e");
			
			// {@squirreljme.error BF1f Cannot write multiple bytes because
			// the ZIP entry would exceed 4GiB.}
			int oldsize = this._size, newsize = oldsize + __l;
			if (newsize < 0 || newsize < oldsize)
				throw new IOException("BF1f");
			
			// Write data and increase size
			this.wrapped.write(__b, __o, __l);
			this._size = newsize;
		}
	}
	
	/**
	 * This is an output stream which is used when writing an entry.
	 *
	 * @since 2016/07/15
	 */
	private final class __InnerOutputStream__
		extends __BaseOutputStream__
	{
		/**
		 * Initializes a new output stream for writing an entry.
		 *
		 * @since 2016/07/15
		 */
		private __InnerOutputStream__()
		{
			super(ZipStreamWriter.this.output);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/07/15
		 */
		@Override
		public final void close()
			throws IOException
		{
			// Ignore if already finished
			if (this.finished)
				return;
			
			// Close and finish
			this.finished = true;
			ZipStreamWriter.this.__closeEntry();
		}
	}
	
	/**
	 * This is an output stream which is used when writing an entry.
	 *
	 * @since 2016/07/15
	 */
	private final class __OuterOutputStream__
		extends __BaseOutputStream__
	{
		/** CRC calculation. */
		protected final CRC32Calculator crccalc =
			new CRC32Calculator(ZipCRCConstants.CRC_REFLECT_DATA,
				ZipCRCConstants.CRC_REFLECT_REMAINDER,
				ZipCRCConstants.CRC_POLYNOMIAL, ZipCRCConstants.CRC_REMAINDER,
				ZipCRCConstants.CRC_FINALXOR);
		
		/**
		 * Initializes a new output stream for writing an entry.
		 *
		 * @param __os The output stream to wrap.
		 * @since 2016/07/15
		 */
		private __OuterOutputStream__(OutputStream __os)
		{
			super(__os);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/07/15
		 */
		@Override
		public final void close()
			throws IOException
		{
			// Ignore if already finished
			if (this.finished)
				return;
			
			// Close the wrapped stream
			this.finished = true;
			this.wrapped.close();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/07/15
		 */
		@Override
		public void flush()
			throws IOException
		{
			super.flush();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/07/16
		 */
		@Override
		public void write(int __b)
			throws IOException
		{
			// Send to output
			super.write(__b);
			
			// Calculate CRC
			this.crccalc.offer((byte)__b);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/07/16
		 */
		@Override
		public void write(byte[] __b, int __o, int __l)
			throws IndexOutOfBoundsException, IOException, NullPointerException
		{
			// Send to output
			super.write(__b, __o, __l);
			
			// Calculate CRC
			this.crccalc.offer(__b, __o, __l);
		}
	}
}

