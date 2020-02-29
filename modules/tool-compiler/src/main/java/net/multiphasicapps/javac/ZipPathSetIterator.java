// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac;

import java.io.IOException;
import java.util.Iterator;
import net.multiphasicapps.zip.blockreader.ZipBlockEntry;

/**
 * This iterates over entries in the ZIP and provides input for those entries.
 *
 * @since 2017/11/29
 */
public final class ZipPathSetIterator
	implements Iterator<CompilerInput>
{
	/** The used iterator. */
	protected final Iterator<ZipBlockEntry> iterator;
	
	/**
	 * Initializes the iterator.
	 *
	 * @param __i The entry iterator to iterate over.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/29
	 */
	public ZipPathSetIterator(Iterator<ZipBlockEntry> __i)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		this.iterator = __i;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public boolean hasNext()
	{
		return this.iterator.hasNext();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public CompilerInput next()
	{
		// Go through entries
		for (Iterator<ZipBlockEntry> iterator = this.iterator;;)
		{
			// Ignore directories
			ZipBlockEntry ze = iterator.next();
			try
			{
				if (ze.isDirectory())
					continue;
			}
			catch (IOException e)
			{
				// {@squirreljme.error AQ3c Could not check if entry was
				// a directory or not.}
				throw new CompilerException("AQ3c", e);
			}
			
			return new ZipInput(ze);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public void remove()
	{
		throw new UnsupportedOperationException("RORO");
	}
}

