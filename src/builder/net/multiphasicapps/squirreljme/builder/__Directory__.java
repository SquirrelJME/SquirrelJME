// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder;

import java.io.InputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITNamespaceBrowser;
import net.multiphasicapps.zip.streamreader.ZipStreamEntry;
import net.multiphasicapps.zip.streamreader.ZipStreamReader;

/**
 * This implements the directory of a package to provide it to the namespace
 * processor.
 *
 * @since 2016/09/11
 */
class __Directory__
	implements JITNamespaceBrowser.Directory
{
	/** The opened ZIP stream. */
	private final ZipStreamReader _zip;
	
	/**
	 * Opens the directory.
	 *
	 * @param __p The binary path.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/11
	 */
	__Directory__(Path __p)
		throws IOException, NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Open
		this._zip = new ZipStreamReader(Channels.newInputStream(FileChannel.
			open(__p, StandardOpenOption.READ)));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/11
	 */
	@Override
	public void close()
		throws IOException
	{
		// Close it
		this._zip.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/11
	 */
	@Override
	public JITNamespaceBrowser.Entry nextEntry()
		throws IOException
	{
		// If there is a new entry, wrap it
		ZipStreamEntry e = this._zip.nextEntry();
		if (e != null)
			return new __Entry__(e);
		
		// Deferred?
		IOException x = this._zip.deferred();
		if (x != null)
			throw x;
		
		// No more left
		return null;
	}
	
	/**
	 * Represents a single entry.
	 *
	 * @since 2016/09/11
	 */
	private final class __Entry__
		extends JITNamespaceBrowser.Entry
	{
		/** The wrapped entry. */
		private final ZipStreamEntry _entry;
		
		/**
		 * Initializes the entry.
		 *
		 * @param __e The entry to obtain.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/09/11
		 */
		private __Entry__(ZipStreamEntry __e)
			throws NullPointerException
		{
			// Check
			if (__e == null)
				throw new NullPointerException("NARG");
			
			// Set
			this._entry = __e;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/11
		 */
		@Override
		public int available()
			throws IOException
		{
			return this._entry.available();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/11
		 */
		@Override
		public void close()
			throws IOException
		{
			this._entry.close();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/11
		 */
		@Override
		public void mark(int __rl)
		{
			this._entry.mark(__rl);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/11
		 */
		@Override
		public boolean markSupported()
		{
			return this._entry.markSupported();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/11
		 */
		@Override
		public String name()
			throws JITException
		{
			return this._entry.name();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/11
		 */
		@Override
		public int read()
			throws IOException
		{
			return this._entry.read();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/11
		 */
		@Override
		public int read(byte[] __b)
			throws IOException
		{
			return this._entry.read(__b);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/11
		 */
		@Override
		public int read(byte[] __b, int __o, int __l)
			throws IOException
		{
			return this._entry.read(__b, __o, __l);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/11
		 */
		@Override
		public void reset()
			throws IOException
		{
			this._entry.reset();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/11
		 */
		@Override
		public long skip(long __n)
			throws IOException
		{
			return this._entry.skip(__n);
		}
	}
}

