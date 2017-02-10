// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

import java.util.AbstractList;
import java.util.RandomAccess;

/**
 * This represents a every exception that exists within a method.
 *
 * @since 2017/02/09
 */
public final class ExceptionHandlerTable
	extends AbstractList<ExceptionHandler>
	implements RandomAccess
{
	/** The exception handler table. */
	private final ExceptionHandler[] _table;
	
	/**
	 * Initializes the exception handler table.
	 *
	 * @param __table The read table, this is not copied.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/09
	 */
	ExceptionHandlerTable(ExceptionHandler... __table)
		throws NullPointerException
	{
		// Check
		if (__table == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._table = __table;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/09
	 */
	@Override
	public ExceptionHandler get(int __i)
	{
		return this._table[__i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/09
	 */
	@Override
	public int size()
	{
		return this._table.length;
	}
}

