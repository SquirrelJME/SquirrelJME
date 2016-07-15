// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip.streamwriter;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import net.multiphasicapps.io.data.ExtendedDataOutputStream;
import net.multiphasicapps.io.data.DataEndianess;
import net.multiphasicapps.zip.ZipCompressionType;

/**
 * This class is used to write to ZIP files in an unknown and stream based
 * manner where the size of the contents is completely unknown.
 *
 * When the stream is closed, the central directory of the ZIP file will be
 * written to the end of the file.
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
	
	/** The maximum permitted file size. */
	private static final long _MAX_FILE_SIZE =
		0xFFFFFFFFL;
	
	/** Lock for safety. */
	protected final Object lock =
		new Object();
	
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
		
		// Lock
		synchronized (this.lock)
		{
			// Do nothing if already closed
			if (this._closed)
				return;
			
			// {@squirreljme.error BC01 Cannot close the ZIP writer because
			// an entry is still being written.}
			if (this._inner != null || this._outer != null)
				throw new IOException("BC01");
			
			throw new Error("TODO");
		}
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
		synchronized (this.lock)
		{
			// {@squirreljme.error BC04 Cannot write new entry because the ZIP
			// has been closed.}
			if (this._closed)
				throw new IOException("BC04");
			
			// {@squirreljme.error BC02 Cannot create a new entry for output
			// because the previous entry has not be closed.}
			if (this._inner != null || this._outer != null)
				throw new IOException("BC02");
			
			// {@squirreljme.error BC0a A ZIP file cannot have more than
			// 65536 entries.}
			if (toc.size() >= 65535)
				throw new IOException("BC0a");
			
			// {@squirreljme.error BC03 The length of the input file exceeds
			// 65535 UTF-8 characters. (The filename length)}
			byte[] utfname = __name.getBytes("utf-8");
			int fnn;
			if ((fnn = utfname.length) > 65535)
				throw new IOException(String.format("BC03 %d", fnn));
			
			// Setup contents
			__TOCEntry__ last = new __TOCEntry__(this.output.size(), utfname,
				__comp);
			toc.addLast(last);
			
			// Write ZIP header data
			ExtendedDataOutputStream output = this.output;
			output.writeInt(_LOCAL_FILE_MAGIC_NUMBER);
			
			// Extract version
			output.writeShort(__comp.extractVersion());
			
			// General purpose flag (Unknown size, UTF-8 file names)
			output.writeShort((1 << 3) | (1 << 11));
			
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
			System.err.println("TODO -- Calculate output CRC.");
			
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
		synchronized (this.lock)
		{
			__InnerOutputStream__ inner = this._inner;
			__OuterOutputStream__ outer = this._outer;
			
			// {@squirreljme.error BC09 Cannot close entry because a current
			// one is not being used.}
			if (inner == null || outer == null)
				throw new IOException("BC09");
			
			// Need to fill the size information and CRC for later
			__TOCEntry__ last = toc.getLast();
			
			// Get sizes
			long uncomp = outer._size;
			long comp = inner._size;
			
			// {@squirreljme.error BC0b Either one or both of the compressed
			// or uncompressed file sizes exceeds 4GiB. (The uncompressed size;
			// The compressed size)}
			if (uncomp >= _MAX_FILE_SIZE || comp >= _MAX_FILE_SIZE)
				throw new IOException(String.format("BC0b %d %d", uncomp,
					comp));
			
			// Store sizes
			last._uncompressed = uncomp;
			last._compressed = comp;
			System.err.println("TODO -- Remember calculated CRC.");
			
			// The magic number of the data descriptor is not needed, however
			// it helps prevent some abiguity when the input data stream is
			// not compressed and contains a ZIP file.
			ExtendedDataOutputStream output = this.output;
			output.writeInt(_DATA_DESCRIPTOR_MAGIC_NUMBER);
			
			// Write CRC and sizes
			System.err.println("TODO -- Output calculated CRC.");
			output.writeInt(0);
			output.writeInt((int)comp);
			output.writeInt((int)uncomp);
			
			// Clear streams to allow for next entry
			this._inner = null;
			this._outer = null;
		}
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
		public final void flush()
			throws IOException
		{
			// Lock
			synchronized (ZipStreamWriter.this.lock)
			{
				// Ignore if finished since the streams should be disconnected
				// at this time
				if (this.finished)
					return;
				
				// Forward flush
				this.wrapped.flush();
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/07/15
		 */
		@Override
		public final void write(int __b)
			throws IOException
		{
			// Lock
			synchronized (ZipStreamWriter.this.lock)
			{
				// {@squirreljme.error BC05 Cannot write a single byte because
				// the stream is closed.}
				if (this.finished)
					throw new IOException("BC05");
				
				// {@squirreljme.error BC08 Cannot write a single byte because
				// the ZIP entry would exceed 4GiB.}
				int oldsize = this._size, newsize = oldsize + 1;
				if (newsize < 0 || newsize < oldsize)
					throw new IOException("BC08");
				
				// Write data and increase size
				this.wrapped.write(__b);
				this._size = newsize;
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/07/15
		 */
		@Override
		public final void write(byte[] __b, int __o, int __l)
			throws IndexOutOfBoundsException, IOException, NullPointerException
		{
			// Check
			if (__b == null)
				throw new NullPointerException("NARG");
			int n = __b.length;
			if (__o < 0 || __l < 0 || (__o + __l) > n)
				throw new IndexOutOfBoundsException("IOOB");
			
			// Lock
			synchronized (ZipStreamWriter.this.lock)
			{
				// {@squirreljme.error BC06 Cannot write multiple bytes because
				// the stream is closed.}
				if (this.finished)
					throw new IOException("BC06");
				
				// {@squirreljme.error BC07 Cannot write multiple bytes because
				// the ZIP entry would exceed 4GiB.}
				int oldsize = this._size, newsize = oldsize + __l;
				if (newsize < 0 || newsize < oldsize)
					throw new IOException("BC07");
				
				// Write data and increase size
				this.wrapped.write(__b, __o, __l);
				this._size = newsize;
			}
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
			// Lock
			synchronized (ZipStreamWriter.this.lock)
			{
				// Ignore if already finished
				if (this.finished)
					return;
				
				// Close and finish
				ZipStreamWriter.this.__closeEntry();
				this.finished = true;
			}
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
			// Lock
			synchronized (ZipStreamWriter.this.lock)
			{
				// Ignore if already finished
				if (this.finished)
					return;
				
				// Close the wrapped stream
				this.wrapped.close();
				this.finished = true;
			}
		}
	}
}

