// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf.file;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;

/**
 * Basic filtering for globs.
 *
 * @since 2025/12/28
 */
final class __BasicGlobFilter__
	implements Iterator<String>
{
	/** The filter to use. */
	private final String _filter;
	
	/** The iterator to process. */
	private final Iterator<String> _it;
	
	/**
	 * Initializes the glob filter.
	 *
	 * @param __filter The filter to use.
	 * @param __iterator The iterator to iterate over.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/28
	 */
	__BasicGlobFilter__(String __filter,
		Iterator<String> __iterator)
		throws NullPointerException
	{
		if (__filter == null || __iterator == null)
			throw new NullPointerException("NARG");
		
		this._filter = __filter;
		this._it = __iterator;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/28
	 */
	@Override
	public void remove()
	{
		throw new UnsupportedOperationException("RORO");
	}
	
	@Override
	public String next()
	{
		throw Debugging.todo();
	}
	
	@Override
	public boolean hasNext()
	{
		throw Debugging.todo();
	}
}
