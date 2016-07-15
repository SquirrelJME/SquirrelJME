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
	public static final int LOCAL_FILE_MAGIC_NUMBER =
		0x04034B50;
	
	/** Lock for safety. */
	protected final Object lock =
		new Object();
	
	/** The output stream to write to. */
	protected final ExtendedDataOutputStream output;
	
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
			
			// Write ZIP header data
			ExtendedDataOutputStream output = this.output;
			output.writeInt(LOCAL_FILE_MAGIC_NUMBER);
			
			// Extract version
			output.writeShort(__comp.extractVersion());
			
			// General purpose flag (UTF-8 file names)
			output.writeShort((1 << 11));
			
			// Method
			output.writeShort(__comp.method());
			
			// Modification date/time
			output.writeShort(0);
			output.writeShort(0);
			
			// CRC-32 and compress/uncompressed size are unknown
			output.writeInt(0);
			output.writeInt(0);
			output.writeInt(0);
			
			// {@squirreljme.error BC03 The length of the input file exceeds
			// 65535 UTF-8 characters. (The filename length)}
			byte[] utfname = __name.getBytes("utf-8");
			int fnn;
			if ((fnn = utfname.length) > 65535)
				throw new IOException(String.format("BC03 %d", fnn));
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
	}
	
	/**
	 * This is an output stream which is used when writing an entry.
	 *
	 * @since 2016/07/15
	 */
	private final class __InnerOutputStream__
		extends OutputStream
	{
		/**
		 * Initializes a new output stream for writing an entry.
		 *
		 * @since 2016/07/15
		 */
		private __InnerOutputStream__()
		{
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
				throw new Error("TODO");
			}
		}
		
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
				throw new Error("TODO");
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
				throw new Error("TODO");
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/07/15
		 */
		@Override
		public final void write(byte[] __b, int __o, int __l)
			throws IOException
		{
			// Lock
			synchronized (ZipStreamWriter.this.lock)
			{
				throw new Error("TODO");
			}
		}
	}
	
	/**
	 * This is an output stream which is used when writing an entry.
	 *
	 * @since 2016/07/15
	 */
	private final class __OuterOutputStream__
		extends OutputStream
	{
		/** The wrapped stream. */
		protected final OutputStream wrapped;
		
		/**
		 * Initializes a new output stream for writing an entry.
		 *
		 * @param __os The output stream to wrap.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/07/15
		 */
		private __OuterOutputStream__(OutputStream __os)
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
		public final void close()
			throws IOException
		{
			// Lock
			synchronized (ZipStreamWriter.this.lock)
			{
				throw new Error("TODO");
			}
		}
		
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
				throw new Error("TODO");
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
				throw new Error("TODO");
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/07/15
		 */
		@Override
		public final void write(byte[] __b, int __o, int __l)
			throws IOException
		{
			// Lock
			synchronized (ZipStreamWriter.this.lock)
			{
				throw new Error("TODO");
			}
		}
	}
}

