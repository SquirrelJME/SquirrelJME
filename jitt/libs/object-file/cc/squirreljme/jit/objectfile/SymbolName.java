// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jit.objectfile;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This class is used to identify names of symbols and is intended for the
 * most part to make it easier to refer to symbols by a fixed form rather
 * than using pre-formed strings each time.
 *
 * @since 2018/02/25
 */
public abstract class SymbolName
	implements Comparable<SymbolName>
{
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/25
	 */
	@Override
	public final int compareTo(SymbolName __o)
		throws NullPointerException
	{
		if (__o == null)
			throw new NullPointerException("NARG");
		
		return this.toString().compareTo(__o.toString());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/25
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof SymbolName))
			return false;
		
		return this.toString().equals(((SymbolName)__o).toString());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/25
	 */
	@Override
	public final int hashCode()
	{
		return this.toString().hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/25
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
}

