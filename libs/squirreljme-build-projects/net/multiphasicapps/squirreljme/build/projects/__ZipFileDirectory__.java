// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.projects;

import java.io.InputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import net.multiphasicapps.squirreljme.build.base.FileDirectory;
import net.multiphasicapps.squirreljme.build.base.FileEntryNotFoundException;
import net.multiphasicapps.zip.blockreader.ZipBlockEntry;
import net.multiphasicapps.zip.blockreader.ZipBlockReader;
import net.multiphasicapps.zip.blockreader.ZipEntryNotFoundException;

/**
 * This is a file directory which wraps a ZIP file.
 *
 * @since 2016/12/27
 */
class __ZipFileDirectory__
	implements FileDirectory
{
	/** The ZIP to wrap. */
	protected final ZipBlockReader zip;
	
	/**
	 * Initializes the ZIP file directory.
	 *
	 * @param __zip The ZIP file to access.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/27
	 */
	__ZipFileDirectory__(ZipBlockReader __zip)
		throws IOException, NullPointerException
	{
		// Check
		if (__zip == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.zip = __zip;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/27
	 */
	@Override
	public void close()
		throws IOException
	{
		this.zip.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/27
	 */
	@Override
	public boolean contains(String __fn)
		throws IOException, NullPointerException
	{
		return this.zip.contains(__fn);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/27
	 */
	@Override
	public Iterator<String> iterator()
	{
		return new __Iterator__();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/27
	 */
	@Override
	public InputStream open(String __fn)
		throws FileEntryNotFoundException, IOException, NullPointerException
	{
		try
		{
			return this.zip.open(__fn);
		}
		
		// {@squirreljme.error AT0n Could not find the specified file entry.
		// (The entry name)}
		catch (ZipEntryNotFoundException e)
		{
			throw new FileEntryNotFoundException(
				String.format("AT0n %s", __fn), e);
		}
	}
	
	/**
	 * Iterator for entry names.
	 *
	 * @since 2016/12/30
	 */
	private class __Iterator__
		implements Iterator<String>
	{
		/** The base iterator. */
		protected final Iterator<ZipBlockEntry> iterator =
			__ZipFileDirectory__.this.zip.iterator();
		
		/**
		 * {@inheritDoc}
		 * @since 2016/12/30
		 */
		@Override
		public boolean hasNext()
		{
			return this.iterator.hasNext();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/12/30
		 */
		@Override
		public String next()
			throws NoSuchElementException
		{
			return this.iterator.next().toString();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/12/30
		 */
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException("RORO");
		}
	}
}

