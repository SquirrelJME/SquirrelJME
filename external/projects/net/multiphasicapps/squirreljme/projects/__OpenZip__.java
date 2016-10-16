// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.projects;

import java.io.InputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import net.multiphasicapps.zip.blockreader.ZipEntry;
import net.multiphasicapps.zip.blockreader.ZipFile;

/**
 * This manages an opened ZIP file.
 *
 * @since 2016/09/20
 */
class __OpenZip__
{
	/** Lock, for reference counting. */
	protected final Object lock =
		new Object();
	
	/** The project this represents. */
	protected final ProjectInfo info;
	
	/** The path to the ZIP. */
	protected final Path path;
	
	/** The currently open ZIP. */
	private volatile ZipFile _zip;
	
	/** The number of opened streams. */
	private volatile int _count;
	
	/**
	 * Initializes the information needed to access a ZIP file.
	 *
	 * @param __pi The project this opens data for.
	 * @param __p The path to the ZIP.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/20
	 */
	__OpenZip__(ProjectInfo __pi, Path __p)
		throws NullPointerException
	{
		// Check
		if (__pi == null || __p == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.info = __pi;
		this.path = __p;
	}
	
	/**
	 * Opens the given input file.
	 *
	 * @param __s The file to open.
	 * @return The input stream of the project.
	 * @throws IOException On read errors.
	 * @throws NoSuchFileException If the file does not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/20
	 */
	public final InputStream open(String __s)
		throws IOException, NoSuchFileException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// If the ZIP is not opened then open it
			ZipFile zip = this._zip;
			if (zip == null)
				this._zip = (zip = ZipFile.open(FileChannel.open(this.path,
					StandardOpenOption.READ)));
			
			// Attempt to locate the entry
			ZipEntry ent = zip.get(__s);
			
			// If opening fails for any reason then the ZIP should be closed
			// if there are no other streams open
			InputStream rv;
			try
			{
				// Does not exist, do nothing
				if (ent == null)
					rv = null;
			
				// Try opening it
				else
				{
					rv = new __Wrapped__(ent.open());
					
					// If this point was reached then the stream is valid
					this._count++;
				}
			}
			
			// If no file was opened then close the ZIP file so opened files
			// are not lying around
			finally
			{
				if (this._count == 0)
				{
					// After a close attempt, it may not be valid
					this._zip = null;
				
					// Try to close it, but ignore if it fails
					try
					{
						zip.close();
					}
				
					// Ignore
					catch (IOException e)
					{
					}
				}
			}
			
			// {@squirreljme.error CI0e The specified file does not exist
			// within the project. (The file)}
			if (rv == null)
				throw new NoSuchFileException(String.format("CI0e %s", __s));
			
			// Return the stream
			return rv;
		}
	}
	
	/**
	 * Reduces the open count.
	 *
	 * @since 2016/09/20
	 */
	private final void __reduce()
	{
		// Lock
		synchronized (this.lock)
		{
			// Read it
			int count = this._count;
			
			// Should not occur
			if (count <= 0)
				throw new RuntimeException("OOPS");
			
			// Set new count
			this._count = --count;
			
			// If the count goes back to zero then close the ZIP
			if (count == 0)
			{
				ZipFile zip = this._zip;
				this._zip = null;
				
				// Could fail
				try
				{
					zip.close();
				}
				
				// Ignore
				catch (IOException e)
				{
				}
			}
		}
	} 
	
	/**
	 * Initializes the wrapped stream.
	 *
	 * @since 2016/09/20
	 */
	private final class __Wrapped__
		extends InputStream
	{
		/** The input source. */
		protected final InputStream input;
		
		/** Already closed? */
		private volatile boolean _closed;
		
		/**
		 * Initializes the wrapped stream.
		 *
		 * @param __is The stream to wrap.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/09/20
		 */
		private __Wrapped__(InputStream __is)
			throws NullPointerException
		{
			// Check
			if (__is == null)
				throw new NullPointerException("NARG");
			
			// Set
			this.input = __is;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/20
		 */
		@Override
		public int available()
			throws IOException
		{
			return this.input.available();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/20
		 */
		@Override
		public void close()
			throws IOException
		{
			// Do nothing if closed
			if (this._closed)
				return;	
			
			// Mark closed
			this._closed = true;
			
			// Close the given stream
			try
			{
				this.input.close();
			}
			
			// Always try to reduce
			finally
			{
				// Reduce open count
				__OpenZip__.this.__reduce();
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/20
		 */
		@Override
		public void mark(int __rl)
		{
			this.input.mark(__rl);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/20
		 */
		@Override
		public boolean markSupported()
		{
			return this.input.markSupported();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/20
		 */
		@Override
		public int read()
			throws IOException
		{
			return this.input.read();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/20
		 */
		@Override
		public int read(byte[] __b, int __o, int __l)
			throws IOException
		{
			return this.input.read(__b, __o, __l);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/20
		 */
		@Override
		public void reset()
			throws IOException
		{
			this.input.reset();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/20
		 */
		@Override
		public long skip(long __n)
			throws IOException
		{
			return this.input.skip(__n);
		}
	}
}

