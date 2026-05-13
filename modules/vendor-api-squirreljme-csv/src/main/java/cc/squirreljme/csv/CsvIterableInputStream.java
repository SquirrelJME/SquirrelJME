// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.csv;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Input stream for CSVs based on {@code Iterable}.
 *
 * @since 2023/09/12
 */
public class CsvIterableInputStream
	implements CsvInputStream
{
	/** The iterator used for accessing lines. */
	protected final Iterator<? extends CharSequence> iterator;
	
	/**
	 * Initializes the input stream.
	 *
	 * @param __it The iterable to source from.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	public CsvIterableInputStream(Iterable<? extends CharSequence> __it)
		throws NullPointerException
	{
		this(__it.iterator());
	}
	
	/**
	 * Initializes the input stream.
	 *
	 * @param __it The iterable to source from.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/14
	 */
	public CsvIterableInputStream(Iterator<? extends CharSequence> __it)
		throws NullPointerException
	{
		if (__it == null)
			throw new NullPointerException("NARG");
		
		this.iterator = __it;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/09/14
	 */
	@Override
	public void close()
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/09/12
	 */
	@Override
	public boolean next(StringBuilder __line)
		throws IOException, NullPointerException
	{
		if (__line == null)
			throw new NullPointerException("NARG");
		
		Iterator<? extends CharSequence> iterator = this.iterator;
		
		try
		{
			CharSequence from = (CharSequence)iterator.next();
			
			// Just grab the entire line, if there is one
			if (from != null)
				__line.append(from);
			
			return true;
		}
		catch (NoSuchElementException __ignored)
		{
			return false;
		}
	}
}
