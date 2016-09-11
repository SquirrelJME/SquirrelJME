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
	/** The ZIP to eventually open. */
	protected final Path path;
	
	/** Readers which have been opened. */
	private final List<__Iterator__> _entries =
		new ArrayList<>();
	
	/**
	 * Opens the directory.
	 *
	 * @param __p The binary path.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/11
	 */
	__Directory__(Path __p)
		throws NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.path = __p;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/11
	 */
	@Override
	public void close()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/11
	 */
	@Override
	public Iterator<JITNamespaceBrowser.Entry> iterator()
	{
		// Could fail
		try
		{
			// Create and add to the opened list
			__Iterator__ rv = new __Iterator__();
			this._entries.add(rv);
			
			// Return it
			return rv;
		}
		
		// {@squirreljme.error DW06 Failed to open the project as a ZIP.}
		catch (IOException e)
		{
			throw new JITException("DW06", e);
		}
	}
	
	/**
	 * Represents a single entry.
	 *
	 * @since 2016/09/11
	 */
	final class __Entry__
		implements JITNamespaceBrowser.Entry
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/09/11
		 */
		@Override
		public String name()
			throws JITException
		{
			throw new Error("TODO");
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/11
		 */
		@Override
		public InputStream open()
			throws IOException
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Iterates through ZIP entries.
	 *
	 * @since 2016/09/11
	 */
	final class __Iterator__
		implements Iterator<JITNamespaceBrowser.Entry>
	{
		/** The streamed ZIP. */
		protected final ZipStreamReader zip;
		
		/**
		 * Initializes the iterator.
		 *
		 * @since 2016/09/11
		 */
		private __Iterator__()
			throws IOException
		{
			// Open
			zip = new ZipStreamReader(Channels.newInputStream(FileChannel.open(
				__Directory__.this.path, StandardOpenOption.READ)));
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/11
		 */
		@Override
		public boolean hasNext()
		{
			throw new Error("TODO");
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/11
		 */
		@Override
		public __Entry__ next()
		{
			throw new Error("TODO");
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/09/11
		 */
		@Override
		public void remove()
		{
			throw new Error("TODO");
		}
	}
}

